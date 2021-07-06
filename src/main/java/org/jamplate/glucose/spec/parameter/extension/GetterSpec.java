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
package org.jamplate.glucose.spec.parameter.extension;

import org.jamplate.api.Spec;
import org.jamplate.function.Analyzer;
import org.jamplate.function.Compiler;
import org.jamplate.glucose.instruction.memory.resource.PushConst;
import org.jamplate.glucose.instruction.operator.struct.Get;
import org.jamplate.glucose.spec.element.ParameterSpec;
import org.jamplate.glucose.spec.standard.ExtensionSpec;
import org.jamplate.glucose.spec.syntax.enclosure.BracketsSpec;
import org.jamplate.impl.instruction.Block;
import org.jamplate.model.CompileException;
import org.jamplate.model.Instruction;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;

import static org.jamplate.glucose.internal.util.Values.number;
import static org.jamplate.impl.analyzer.FilterAnalyzer.filter;
import static org.jamplate.impl.analyzer.HierarchyAnalyzer.hierarchy;
import static org.jamplate.impl.compiler.FilterCompiler.filter;
import static org.jamplate.internal.analyzer.UnaryExtensionAnalyzer.unaryExtension;
import static org.jamplate.internal.util.Functions.analyzer;
import static org.jamplate.internal.util.Functions.compiler;
import static org.jamplate.internal.util.Query.*;
import static org.jamplate.internal.util.Source.read;

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
		return analyzer(
				//search in the whole hierarchy
				a -> hierarchy(a),
				//target valid non processed trees
				a -> filter(a, and(
						//target brackets symbols
						is(BracketsSpec.KIND),
						//skip if already wrapped
						parent(not(GetterSpec.KIND))
				)),
				//analyze
				a -> unaryExtension(
						//context wrapper constructor
						(d, r) -> new Tree(
								d,
								r,
								new Sketch(GetterSpec.KIND),
								ExtensionSpec.WEIGHT
						),
						//extension constructor
						(w, t) -> w.getSketch().set(
								ExtensionSpec.KEY_SIGN,
								t.getSketch()
						),
						//left-side wrapper constructor
						(w, r) -> w.offer(new Tree(
								w.getDocument(),
								r,
								w.getSketch()
								 .get(ExtensionSpec.KEY_TARGET)
								 .setKind(ParameterSpec.KIND),
								ParameterSpec.WEIGHT
						))
				)
		);
	}

	@NotNull
	@Override
	public Compiler getCompiler() {
		return compiler(
				//target getter context
				c -> filter(c, is(GetterSpec.KIND)),
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
								read(targetT) +
								"> with <" +
								read(signT) +
								">",
								tree
						);

					return new Block(
							tree,
							//run the target
							targetI,
							//key sandbox
							new Block(
									tree,
									//run the sign
									signI,
									//push `0` to access the key
									new PushConst(tree, number(0)),
									//get the key
									new Get(tree)
							),
							//get
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
