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

import cufy.util.Node;
import org.jamplate.api.Spec;
import org.jamplate.function.Parser;
import org.jamplate.impl.api.MultiSpec;
import org.jamplate.internal.function.parser.router.PseudoTreeParser;
import org.jamplate.internal.function.parser.pattern.EnclosureParser;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

/**
 * A class containing command internal specifications.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.15
 */
public class CommandSpec extends MultiSpec {
	/**
	 * The key to access the accessor part in a command.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	@NotNull
	public static final Node.Key KEY_ACCESSOR = Sketch.component("command:accessor");
	/**
	 * The key to access the extension part of a command.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	@NotNull
	public static final Node.Key KEY_EXTENSION = Sketch.component("command:extension");
	/**
	 * The key to access the key of a command.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	@NotNull
	public static final Node.Key KEY_KEY = Sketch.component("command:key");
	/**
	 * The key to access the type of a command.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	@NotNull
	public static final Node.Key KEY_TYPE = Sketch.component("command:type");
	/**
	 * The key to access the value of a command.
	 *
	 * @since 0.3.0 ~2021.06.15
	 */
	@NotNull
	public static final Node.Key KEY_VALUE = Sketch.component("command:value");

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	public static final String NAME = CommandSpec.class.getSimpleName();

	/**
	 * Construct a new commands spec.
	 *
	 * @since 0.3.0 ~2021.06.25
	 */
	public CommandSpec() {
		super(ParameterSpec.NAME);
	}

	/**
	 * Construct a new commands spec.
	 *
	 * @param subspecs the initial subspecs.
	 * @throws NullPointerException if the given {@code subspecs} is null.
	 * @since 0.3.0 ~2021.06.25
	 */
	public CommandSpec(@Nullable Spec @NotNull ... subspecs) {
		super(ParameterSpec.NAME, subspecs);
	}

	@NotNull
	@Override
	public Parser getParser() {
		//parse only in the first round
		return new PseudoTreeParser(
				new EnclosureParser(
						Pattern.compile("(?<=^|[\r\n])[\\t ]*#"),
						Pattern.compile("(?=(?<!\\\\)[\\r\\n]|$)"),
						Tree::new
				),
				super.getParser()
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return CommandSpec.NAME;
	}
}
