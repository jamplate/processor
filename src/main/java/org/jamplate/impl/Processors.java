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

import org.jamplate.impl.util.model.CommandSketch;
import org.jamplate.model.Tree;
import org.jamplate.model.function.Processor;
import org.jamplate.util.Trees;
import org.jamplate.util.model.function.SequentialProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * The post-parse processors.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.20
 */
public final class Processors {
	/**
	 * A processor that analyzes the default jamplate components searching for known
	 * jamplate specs.
	 *
	 * @since 0.2.0 ~2021.05.20
	 */
	@NotNull
	public static final Processor PROCESSOR =
			new SequentialProcessor(
					Syntax.LN_SUPPRESSED,
					Commands.CONSOLE,
					Commands.DECLARE,
					Commands.DEFINE,
					Commands.INCLUDE,
					Values.STRING
			);

	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.05.20
	 */
	private Processors() {
		throw new AssertionError("No instance for you");
	}

	/**
	 * A utility class containing the commands processors.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.05.20
	 */
	public static final class Commands {
		/**
		 * A parser that parses the include command.
		 *
		 * @since 0.2.0 ~2021.05.20
		 */
		@SuppressWarnings("OverlyLongLambda")
		@NotNull
		public static final Processor CONSOLE = compilation -> {
			boolean[] modified = {false};
			Trees.collect(compilation.getRootTree())
				 .parallelStream()
				 .forEach(tree -> {
					 if (tree.getSketch().getKind().equals(Kind.Transient.COMMAND)) {
						 CommandSketch command = (CommandSketch) tree.getSketch();
						 String content = Trees.read(command.getTypeSketch().getTree()).toString();

						 if (content.equalsIgnoreCase("console")) {
							 command.setKind(Kind.Command.CONSOLE);

							 modified[0] = true;
						 }
					 }
				 });
			return modified[0];
		};

		/**
		 * A parser that parses the declare command.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@SuppressWarnings("OverlyLongLambda")
		@NotNull
		public static final Processor DECLARE = compilation -> {
			boolean[] modified = {false};
			Trees.collect(compilation.getRootTree())
				 .parallelStream()
				 .forEach(tree -> {
					 if (tree.getSketch().getKind().equals(Kind.Transient.COMMAND)) {
						 CommandSketch command = (CommandSketch) tree.getSketch();
						 String content = Trees.read(command.getTypeSketch().getTree()).toString();

						 if (content.equalsIgnoreCase("declare")) {
							 command.setKind(Kind.Command.DECLARE);
							 command.getParameterSketch().getTree().pop();
							 tree.offer(command.getParameterSketch().getValueSketch().getTree());

							 modified[0] = true;
						 }
					 }
				 });
			return modified[0];
		};

		/**
		 * A parser that parses the define command.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@SuppressWarnings("OverlyLongLambda")
		@NotNull
		public static final Processor DEFINE = compilation -> {
			boolean[] modified = {false};
			Trees.collect(compilation.getRootTree())
				 .parallelStream()
				 .forEach(tree -> {
					 if (tree.getSketch().getKind().equals(Kind.Transient.COMMAND)) {
						 CommandSketch command = (CommandSketch) tree.getSketch();
						 String content = Trees.read(command.getTypeSketch().getTree()).toString();

						 if (content.equalsIgnoreCase("define")) {
							 command.setKind(Kind.Command.DEFINE);
							 command.getParameterSketch().getTree().pop();
							 tree.offer(command.getParameterSketch().getValueSketch().getTree());

							 modified[0] = true;
						 }
					 }
				 });
			return modified[0];
		};

		/**
		 * A parser that parses the include command.
		 *
		 * @since 0.2.0 ~2021.05.20
		 */
		@SuppressWarnings("OverlyLongLambda")
		@NotNull
		public static final Processor INCLUDE = compilation -> {
			boolean[] modified = {false};
			Trees.collect(compilation.getRootTree())
				 .parallelStream()
				 .forEach(tree -> {
					 if (tree.getSketch().getKind().equals(Kind.Transient.COMMAND)) {
						 CommandSketch command = (CommandSketch) tree.getSketch();
						 String content = Trees.read(command.getTypeSketch().getTree()).toString();

						 if (content.equalsIgnoreCase("include")) {
							 command.setKind(Kind.Command.INCLUDE);

							 modified[0] = true;
						 }
					 }
				 });
			return modified[0];
		};

		/**
		 * Utility classes must not be initialized.
		 *
		 * @throws AssertionError when called.
		 * @since 0.2.0 ~2021.05.20
		 */
		private Commands() {
			throw new AssertionError("No instance for you");
		}
	}

	/**
	 * Syntax-level processors.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.05.23
	 */
	public static final class Syntax {
		/**
		 * A processor that suppresses line-separator sketches that are after a command.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@SuppressWarnings("OverlyLongLambda")
		@NotNull
		public static final Processor LN_SUPPRESSED = compilation -> {
			boolean[] modified = {false};
			Trees.collect(compilation.getRootTree())
				 .parallelStream()
				 .filter(tree -> tree.getSketch().getKind().equals(Kind.Syntax.LN))
				 .forEach(tree -> {
					 Tree previous = tree.getPrevious();

					 if (previous != null)
						 switch (previous.getSketch().getKind()) {
							 case Kind.Transient.COMMAND:
								 tree.getSketch().setKind(Kind.Syntax.LN_SUPPRESSED);
								 modified[0] = true;
						 }
				 });
			return modified[0];
		};

		/**
		 * Utility classes must not be initialized.
		 *
		 * @throws AssertionError when called.
		 * @since 0.2.0 ~2021.05.23
		 */
		private Syntax() {
			throw new AssertionError("No instance for you");
		}
	}

	/**
	 * Values processors.
	 *
	 * @author LSafer
	 * @version 0.2.0
	 * @since 0.2.0 ~2021.05.23
	 */
	public static final class Values {
		/**
		 * The condition for the value components to be processed.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@NotNull
		private static final Predicate<Tree> CONDITION = tree -> {
			switch (tree.getSketch().getKind()) {
				case Kind.Transient.COMMAND_PARAMETER:
				case Kind.Transient.COMMAND_PARAMETER_VALUE:
				case Kind.Transient.INJECTION_PARAMETER:
					return true;
				default:
					return false;
			}
		};

		/**
		 * A processor that sets the string kind to valid strings.
		 *
		 * @since 0.2.0 ~2021.05.23
		 */
		@SuppressWarnings("OverlyLongLambda")
		@NotNull
		public static final Processor STRING = compilation -> {
			boolean[] modified = {false};
			Trees.collect(compilation.getRootTree())
				 .stream()
				 .filter(Values.CONDITION)
				 .forEach(tree ->
						 Trees.children(tree)
							  .parallelStream()
							  .filter(child -> {
								  switch (child.getSketch().getKind()) {
									  case Kind.Syntax.DQUOTE:
									  case Kind.Syntax.QUOTE:
										  return true;
									  default:
										  return false;
								  }
							  })
							  .forEach(child -> {
								  child.getSketch().setKind(Kind.Value.STRING);
								  modified[0] = true;
							  })
				 );
			return modified[0];
		};

		/**
		 * Utility classes must not be initialized.
		 *
		 * @throws AssertionError when called.
		 * @since 0.2.0 ~2021.05.23
		 */
		private Values() {
			throw new AssertionError("No instance for you");
		}
	}
}
