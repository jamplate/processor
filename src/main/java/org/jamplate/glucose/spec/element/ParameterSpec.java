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
package org.jamplate.glucose.spec.element;

import org.jamplate.api.Spec;
import org.jamplate.function.Analyzer;
import org.jamplate.function.Compiler;
import org.jamplate.function.Parser;
import org.jamplate.glucose.instruction.flow.Idle;
import org.jamplate.impl.api.MultiSpec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Collectors;

import static org.jamplate.impl.analyzer.FilterAnalyzer.filter;
import static org.jamplate.impl.analyzer.HierarchyAnalyzer.hierarchy;
import static org.jamplate.impl.analyzer.SequentialAnalyzer.sequential;
import static org.jamplate.impl.compiler.ExclusiveCompiler.exclusive;
import static org.jamplate.impl.compiler.FallbackCompiler.fallback;
import static org.jamplate.impl.compiler.FilterCompiler.filter;
import static org.jamplate.impl.compiler.FirstCompileCompiler.first;
import static org.jamplate.impl.parser.FilterParser.filter;
import static org.jamplate.impl.parser.HierarchyParser.hierarchy;
import static org.jamplate.internal.compiler.FlattenCompiler.flatten;
import static org.jamplate.internal.compiler.MandatoryCompiler.mandatory;
import static org.jamplate.internal.parser.NaturalMergeParser.merge;
import static org.jamplate.internal.util.Collisions.nested;
import static org.jamplate.internal.util.Functions.*;
import static org.jamplate.internal.util.Query.is;
import static org.jamplate.internal.util.Query.whitespace;

/**
 * A class containing parameter context internal specifications.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.13
 */
public class ParameterSpec extends MultiSpec {
	/**
	 * The kind of a parameter context.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	@NotNull
	public static final String KIND = "parameter";

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final String NAME = ParameterSpec.class.getSimpleName();

	/**
	 * The default {@code weight} of a parameter tree.
	 *
	 * @since 0.3.0 ~2021.06.22
	 */
	public static final int WEIGHT = -100;

	/**
	 * Construct a new parameter spec.
	 *
	 * @param subspecs the initial subspecs.
	 * @throws NullPointerException if the given {@code subspecs} is null.
	 * @since 0.3.0 ~2021.06.25
	 */
	public ParameterSpec(@Nullable Spec @NotNull ... subspecs) {
		super(ParameterSpec.NAME, subspecs);
	}

	@NotNull
	@Override
	public Analyzer getAnalyzer() {
		return analyzer(
				a -> sequential(
						//foreach subspec analyzer
						this.specs.stream().map(spec -> analyzer(
								//search the whole hierarchy
								aa -> hierarchy(aa),
								//stop at parameter scopes
								aa -> filter(aa, is(ParameterSpec.KIND)),
								//search the whole parameter scope
								aa -> hierarchy(aa),
								//analyze
								aa -> spec.getAnalyzer()
						)).collect(Collectors.toList())
				)
		);
	}

	@NotNull
	@Override
	public Compiler getCompiler() {
		return compiler(
				//compile parameters only
				c -> filter(c, is(ParameterSpec.KIND)),
				//flatten
				c -> flatten(c),
				//set fallback to be this compiler
				c -> exclusive(c),
				//compiling is mandatory
				c -> mandatory(c),
				//compile
				c -> first(
						//compile using subspecs compilers
						super.getCompiler(),
						//compile nested parameters using this
						compiler(
								//target parameters
								cc -> filter(cc, is(ParameterSpec.KIND)),
								//flatten components
								cc -> flatten(cc),
								//fallback to this
								cc -> fallback()
						),
						//compile leftover whitespaces to Idle
						compiler(
								cc -> filter(cc, whitespace()),
								cc -> (compiler, compilation, tree) ->
										new Idle(tree)
						)
				)
		);
	}

	@NotNull
	@Override
	public Parser getParser() {
		return parser(
				//merge
				p -> merge(p, nested()),
				//search in the whole hierarchy
				p -> hierarchy(p),
				//parse parameter trees
				p -> filter(p, is(ParameterSpec.KIND)),
				//parse using subspecs parsers
				p -> super.getParser()
		);
	}
}
