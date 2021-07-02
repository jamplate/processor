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
package org.jamplate.spec.parameter.resource;

import org.jamplate.api.Spec;
import org.jamplate.function.Analyzer;
import org.jamplate.function.Compiler;
import org.jamplate.instruction.flow.Block;
import org.jamplate.instruction.memory.frame.DumpFrame;
import org.jamplate.instruction.memory.frame.JoinFrame;
import org.jamplate.instruction.memory.frame.PushFrame;
import org.jamplate.instruction.operator.cast.CastObject;
import org.jamplate.instruction.operator.cast.CastPair;
import org.jamplate.internal.function.analyzer.alter.MultiSeparatorAnalyzer;
import org.jamplate.internal.function.analyzer.filter.FilterByNotChildKindAnalyzer;
import org.jamplate.internal.function.analyzer.router.ChildrenAnalyzer;
import org.jamplate.internal.function.analyzer.filter.FilterByKindAnalyzer;
import org.jamplate.internal.function.analyzer.router.HierarchyAnalyzer;
import org.jamplate.internal.function.compiler.router.FlattenCompiler;
import org.jamplate.internal.function.compiler.concrete.ToPushConstCompiler;
import org.jamplate.internal.function.compiler.group.FirstCompileCompiler;
import org.jamplate.internal.function.compiler.router.FallbackCompiler;
import org.jamplate.internal.function.compiler.filter.FilterByKindCompiler;
import org.jamplate.internal.util.Functions;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jamplate.spec.standard.AnchorSpec;
import org.jamplate.spec.syntax.enclosure.BracesSpec;
import org.jamplate.spec.syntax.symbol.CommaSpec;
import org.jetbrains.annotations.NotNull;

/**
 * Parameter object specification.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.20
 */
@SuppressWarnings({"OverlyCoupledMethod", "OverlyCoupledClass"})
public class ObjectSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final ObjectSpec INSTANCE = new ObjectSpec();
	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final String NAME = ObjectSpec.class.getSimpleName();

	@NotNull
	@Override
	public Analyzer getAnalyzer() {
		return Functions.analyzer(
				//search in the whole hierarchy
				HierarchyAnalyzer::new,
				//target braces
				a -> new FilterByKindAnalyzer(BracesSpec.KIND, a),
				//foreach child
				ChildrenAnalyzer::new,
				//target the body
				a -> new FilterByKindAnalyzer(AnchorSpec.KIND_BODY, a),
				//skip if already separated
				a -> new FilterByNotChildKindAnalyzer(AnchorSpec.KIND_SLOT, a),
				//separate
				a -> new MultiSeparatorAnalyzer(
						CommaSpec.KIND,
						(t, r) -> t.offer(new Tree(
								t.getDocument(),
								r,
								new Sketch(AnchorSpec.KIND_SLOT),
								AnchorSpec.WEIGHT_SLOT
						))
				)
		);
	}

	@NotNull
	@Override
	public Compiler getCompiler() {
		return Functions.compiler(
				//target braces
				c -> new FilterByKindCompiler(BracesSpec.KIND, c),
				//compile the whole context
				c -> (compiler, compilation, tree) ->
						new Block(
								tree,
								//push a frame to encapsulate the content of the object
								new PushFrame(tree),
								//execute inner parts
								c.compile(compiler, compilation, tree),
								//join the execution results
								new JoinFrame(tree),
								//cast to object
								new CastObject(tree),
								//dump the frame
								new DumpFrame(tree)
						),
				//flatten parts
				FlattenCompiler::new,
				//compile anchors, body, commas, slots, else
				c -> new FirstCompileCompiler(
						//compile opening anchors to PushConst
						new FilterByKindCompiler(AnchorSpec.KIND_OPEN, ToPushConstCompiler.INSTANCE),
						//compile closing anchors to PushConst
						new FilterByKindCompiler(AnchorSpec.KIND_CLOSE, ToPushConstCompiler.INSTANCE),
						//compile body, commas, slots, else
						c
				),
				//target body
				c -> new FilterByKindCompiler(AnchorSpec.KIND_BODY, c),
				//flatten body parts
				FlattenCompiler::new,
				//compile commas, slots, else
				c -> new FirstCompileCompiler(
						//compile commas (,) to PushConst
						new FilterByKindCompiler(CommaSpec.KIND, ToPushConstCompiler.INSTANCE),
						//compile the slots
						c
				),
				c -> (compiler, compilation, tree) -> new Block(
						//compile
						c.compile(compiler, compilation, tree),
						//cast
						new CastPair(tree)
				),
				//flatten slots parts
				FlattenCompiler::new,
				//compile pairs using the fallback compiler
				c -> FallbackCompiler.INSTANCE
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return ObjectSpec.NAME;
	}
}
