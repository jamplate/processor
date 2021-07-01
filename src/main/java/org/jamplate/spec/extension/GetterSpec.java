/*
 *	Copyright 2021 Cufy
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	    http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package org.jamplate.spec.extension;

import org.jamplate.api.Spec;
import org.jamplate.function.Analyzer;
import org.jamplate.function.Compiler;
import org.jamplate.instruction.flow.Block;
import org.jamplate.instruction.memory.resource.PushConst;
import org.jamplate.instruction.operator.struct.Get;
import org.jamplate.internal.function.analyzer.alter.UnaryExtensionAnalyzer;
import org.jamplate.internal.function.analyzer.wrapper.FilterByKindAnalyzer;
import org.jamplate.internal.function.analyzer.wrapper.FilterByNotParentKindAnalyzer;
import org.jamplate.internal.function.analyzer.wrapper.HierarchyAnalyzer;
import org.jamplate.internal.function.compiler.wrapper.FilterByKindCompiler;
import org.jamplate.internal.util.Functions;
import org.jamplate.internal.util.IO;
import org.jamplate.model.CompileException;
import org.jamplate.model.Instruction;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jamplate.spec.element.ParameterSpec;
import org.jamplate.spec.standard.ExtensionSpec;
import org.jamplate.spec.syntax.enclosure.BracketsSpec;
import org.jamplate.value.NumberValue;
import org.jetbrains.annotations.NotNull;

/**
 * Getter extension specifications.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.20
 */
public class GetterSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.21
	 */
	@NotNull
	public static final GetterSpec INSTANCE = new GetterSpec();

	/**
	 * The kind of a getter extension context.
	 *
	 * @since 0.3.0 ~2021.06.22
	 */
	@NotNull
	public static final String KIND = "extension:getter";

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.22
	 */
	@NotNull
	public static final String NAME = GetterSpec.class.getSimpleName();

	@NotNull
	@Override
	public Analyzer getAnalyzer() {
		return Functions.analyzer(
				//search in the whole hierarchy
				HierarchyAnalyzer::new,
				//filter only if not already wrapped
				a -> new FilterByNotParentKindAnalyzer(GetterSpec.KIND, a),
				//target square brackets
				a -> new FilterByKindAnalyzer(BracketsSpec.KIND, a),
				//analytic
				a -> new UnaryExtensionAnalyzer(
						//context wrapper constructor
						(d, r) -> new Tree(
								d,
								r,
								new Sketch(GetterSpec.KIND),
								ExtensionSpec.Z_INDEX
						),
						//extension constructor
						(w, t) -> w.getSketch().set(
								ExtensionSpec.KEY_SIGN,
								t.getSketch()
						),
						//left-side wrapper constructor
						(w, r) -> w.offer(new Tree(
								w.document(),
								r,
								w.getSketch()
								 .get(ExtensionSpec.KEY_TARGET)
								 .setKind(ParameterSpec.KIND),
								ParameterSpec.Z_INDEX
						))
				)
		);
	}

	@NotNull
	@Override
	public Compiler getCompiler() {
		return Functions.compiler(
				//target getter context
				c -> new FilterByKindCompiler(GetterSpec.KIND, c),
				//compile
				c -> (compiler, compilation, tree) -> {
					Tree targetT = tree.getSketch().get(ExtensionSpec.KEY_TARGET).getTree();
					Tree signT = tree.getSketch().get(ExtensionSpec.KEY_SIGN).getTree();

					if (targetT == null || signT == null)
						throw new CompileException(
								"Extension GETTER is missing some components",
								tree
						);

					Instruction targetI = compiler.compile(
							compiler,
							compilation,
							targetT
					);
					Instruction signI = compiler.compile(
							compiler,
							compilation,
							signT
					);

					if (targetI == null || signI == null)
						throw new CompileException(
								"The extension GETTER cannot be applied to <" +
								IO.read(targetT) +
								"> with <" +
								IO.read(signT) +
								">",
								tree
						);

					Block signWrapI = new Block(
							signI,
							new PushConst(tree, new NumberValue(0)),
							new Get(tree)
					);

					return new Block(
							tree,
							targetI,
							signWrapI,
							new Get(tree)
					);
				}
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return GetterSpec.NAME;
	}
}
