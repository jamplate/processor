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
package org.jamplate.impl;

import org.jamplate.impl.analyzer.ConditionTransformAnalyzer;
import org.jamplate.impl.util.Trees;
import org.jamplate.model.Tree;
import org.jamplate.model.function.Analyzer;
import org.jetbrains.annotations.NotNull;

/**
 * Jamplate analyzers.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.28
 */
public final class Analyzers {
	//CX CMD

	/**
	 * An analyzer for {@code #console} commands.
	 *
	 * @since 0.2.0 ~2021.05.29
	 */
	@NotNull
	public static final Analyzer CX_CMD_CONSOLE =
			new ConditionTransformAnalyzer(
					Kind.CX_CMD, Kind.CX_CMD_CONSOLE,
					(compilation, tree) ->
							Trees.read(tree.getSketch().get(Component.TYPE).getTree())
								 .toString().contentEquals("console")
			);

	/**
	 * An analyzer for {@code #declare} commands.
	 *
	 * @since 0.2.0 ~2021.05.29
	 */
	@NotNull
	public static final Analyzer CX_CMD_DECLARE =
			new ConditionTransformAnalyzer(
					Kind.CX_CMD, Kind.CX_CMD_DECLARE,
					(compilation, tree) ->
							Trees.read(tree.getSketch().get(Component.TYPE).getTree())
								 .toString().contentEquals("declare")
			).then((compilation, tree) -> {
				tree.getSketch().get(Component.PARAMETER).getTree().pop();
				tree.offer(tree.getSketch().get(Component.PARAMETER).get(Component.VALUE).getTree());
			});

	/**
	 * An analyzer for {@code #define} commands.
	 *
	 * @since 0.2.0 ~2021.05.29
	 */
	@NotNull
	public static final Analyzer CX_CMD_DEFINE =
			new ConditionTransformAnalyzer(
					Kind.CX_CMD, Kind.CX_CMD_DEFINE,
					(compilation, tree) ->
							Trees.read(tree.getSketch().get(Component.TYPE).getTree())
								 .toString().contentEquals("define")
			).then((compilation, tree) -> {
				tree.getSketch().get(Component.PARAMETER).getTree().pop();
				tree.offer(tree.getSketch().get(Component.PARAMETER).get(Component.VALUE).getTree());
			});

	/**
	 * An analyzer for {@code #elif} commands.
	 *
	 * @since 0.2.0 ~2021.05.29
	 */
	@NotNull
	public static final Analyzer CX_CMD_ELIF =
			new ConditionTransformAnalyzer(
					Kind.CX_CMD, Kind.CX_CMD_ELIF,
					(compilation, tree) ->
							Trees.read(tree.getSketch().get(Component.TYPE).getTree())
								 .toString().contentEquals("elif")
			);

	/**
	 * An analyzer for {@code #elifdef} commands.
	 *
	 * @since 0.2.0 ~2021.05.29
	 */
	@NotNull
	public static final Analyzer CX_CMD_ELIFDEF =
			new ConditionTransformAnalyzer(
					Kind.CX_CMD, Kind.CX_CMD_ELIFDEF,
					(compilation, tree) ->
							Trees.read(tree.getSketch().get(Component.TYPE).getTree())
								 .toString().contentEquals("elifdef")
			);

	/**
	 * An analyzer for {@code #elifndef} commands.
	 *
	 * @since 0.2.0 ~2021.05.29
	 */
	@NotNull
	public static final Analyzer CX_CMD_ELIFNDEF =
			new ConditionTransformAnalyzer(
					Kind.CX_CMD, Kind.CX_CMD_ELIFNDEF,
					(compilation, tree) ->
							Trees.read(tree.getSketch().get(Component.TYPE).getTree())
								 .toString().contentEquals("elifndef")
			);

	/**
	 * An analyzer for {@code #else} commands.
	 *
	 * @since 0.2.0 ~2021.05.29
	 */
	@NotNull
	public static final Analyzer CX_CMD_ELSE =
			new ConditionTransformAnalyzer(
					Kind.CX_CMD, Kind.CX_CMD_ELSE,
					(compilation, tree) ->
							Trees.read(tree.getSketch().get(Component.TYPE).getTree())
								 .toString().contentEquals("else")
			);

	/**
	 * An analyzer for {@code #endif} commands.
	 *
	 * @since 0.2.0 ~2021.05.29
	 */
	@NotNull
	public static final Analyzer CX_CMD_ENDIF =
			new ConditionTransformAnalyzer(
					Kind.CX_CMD, Kind.CX_CMD_ENDIF,
					(compilation, tree) ->
							Trees.read(tree.getSketch().get(Component.TYPE).getTree())
								 .toString().contentEquals("endif")
			);

	/**
	 * An analyzer for {@code #if} commands.
	 *
	 * @since 0.2.0 ~2021.05.29
	 */
	@NotNull
	public static final Analyzer CX_CMD_IF =
			new ConditionTransformAnalyzer(
					Kind.CX_CMD, Kind.CX_CMD_IF,
					(compilation, tree) ->
							Trees.read(tree.getSketch().get(Component.TYPE).getTree())
								 .toString().contentEquals("if")
			);

	/**
	 * An analyzer for {@code #ifdef} commands.
	 *
	 * @since 0.2.0 ~2021.05.29
	 */
	@NotNull
	public static final Analyzer CX_CMD_IFDEF =
			new ConditionTransformAnalyzer(
					Kind.CX_CMD, Kind.CX_CMD_IFDEF,
					(compilation, tree) ->
							Trees.read(tree.getSketch().get(Component.TYPE).getTree())
								 .toString().contentEquals("ifdef")
			);

	/**
	 * An analyzer for {@code #ifndef} commands.
	 *
	 * @since 0.2.0 ~2021.05.29
	 */
	@NotNull
	public static final Analyzer CX_CMD_IFNDEF =
			new ConditionTransformAnalyzer(
					Kind.CX_CMD, Kind.CX_CMD_IFNDEF,
					(compilation, tree) ->
							Trees.read(tree.getSketch().get(Component.TYPE).getTree())
								 .toString().contentEquals("ifndef")
			);

	/**
	 * An analyzer for {@code #include} commands.
	 *
	 * @since 0.2.0 ~2021.05.29
	 */
	@NotNull
	public static final Analyzer CX_CMD_INCLUDE =
			new ConditionTransformAnalyzer(
					Kind.CX_CMD, Kind.CX_CMD_INCLUDE,
					(compilation, tree) ->
							Trees.read(tree.getSketch().get(Component.TYPE).getTree())
								 .toString().contentEquals("include")
			);

	//SX

	/**
	 * An analyzer for suppressing line separators.
	 *
	 * @since 0.2.0 ~2021.05.29
	 */
	@SuppressWarnings("OverlyLongLambda")
	@NotNull
	public static final Analyzer SX_EOL_SUPPRESSED =
			new ConditionTransformAnalyzer(
					Kind.SX_EOL, Kind.SX_EOL_SUPPRESSED,
					(compilation, tree) -> {
						Tree previous = tree.getPrevious();
						Tree next = tree.getNext();

						if (previous != null)
							switch (previous.getSketch().getKind()) {
								case Kind.CX_CMD:
									return true;
							}

						if (next != null)
							switch (next.getSketch().getKind()) {
								case Kind.CX_CMD:
									return true;
							}

						return false;
					}
			);

	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.05.28
	 */
	private Analyzers() {
		throw new AssertionError("No instance for you");
	}
}
