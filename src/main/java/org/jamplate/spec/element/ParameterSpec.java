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
package org.jamplate.spec.element;

import org.jamplate.api.Spec;
import org.jamplate.function.Analyzer;
import org.jamplate.function.Compiler;
import org.jamplate.function.Parser;
import org.jamplate.internal.api.MultiSpec;
import org.jamplate.internal.function.analyzer.group.SequentialAnalyzer;
import org.jamplate.internal.function.analyzer.filter.FilterByHierarchyKindAnalyzer;
import org.jamplate.internal.function.analyzer.router.HierarchyAnalyzer;
import org.jamplate.internal.function.compiler.router.FlattenCompiler;
import org.jamplate.internal.function.compiler.concrete.ToIdleCompiler;
import org.jamplate.internal.function.compiler.group.FirstCompileCompiler;
import org.jamplate.internal.function.compiler.router.FallbackCompiler;
import org.jamplate.internal.function.compiler.mode.ExclusiveCompiler;
import org.jamplate.internal.function.compiler.filter.FilterByKindCompiler;
import org.jamplate.internal.function.compiler.filter.FilterWhitespaceCompiler;
import org.jamplate.internal.function.compiler.mode.MandatoryCompiler;
import org.jamplate.internal.function.parser.filter.FilterByKindParser;
import org.jamplate.internal.function.parser.router.HierarchyParser;
import org.jamplate.internal.util.Functions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Collectors;

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
	 * The {@code z-index} of a parameter tree.
	 *
	 * @since 0.3.0 ~2021.06.22
	 */
	public static final int Z_INDEX = -100;

	/**
	 * Construct a new parameter spec.
	 *
	 * @since 0.3.0 ~2021.06.25
	 */
	public ParameterSpec() {
		super(ParameterSpec.NAME);
	}

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
		return new SequentialAnalyzer(
				this.specs.stream()
						  .map(Spec::getAnalyzer)
						  .map(a -> new FilterByHierarchyKindAnalyzer(ParameterSpec.KIND, a))
						  .map(HierarchyAnalyzer::new)
						  .collect(Collectors.toList())
		);
	}

	@NotNull
	@Override
	public Compiler getCompiler() {
		return Functions.compiler(
				//compile parameters only
				c -> new FilterByKindCompiler(ParameterSpec.KIND, c),
				//flatten
				FlattenCompiler::new,
				//set fallback to be this compiler
				ExclusiveCompiler::new,
				//compiling is mandatory
				MandatoryCompiler::new,
				//compile
				c -> new FirstCompileCompiler(
						//compile using subspecs compilers
						super.getCompiler(),
						//compile nested parameters using this
						Functions.compiler(
								//target parameters
								cc -> new FilterByKindCompiler(ParameterSpec.KIND, cc),
								//flatten components
								FlattenCompiler::new,
								//fallback to this
								cc -> FallbackCompiler.INSTANCE
						),
						//compile leftover whitespaces to Idle
						new FilterWhitespaceCompiler(ToIdleCompiler.INSTANCE)
				)
		);
	}

	@NotNull
	@Override
	public Parser getParser() {
		return Functions.parser(
				//search in the whole hierarchy
				HierarchyParser::new,
				//parse parameter trees
				p -> new FilterByKindParser(ParameterSpec.KIND, p),
				//parse using subspecs parsers
				p -> super.getParser()
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return ParameterSpec.NAME;
	}
}
