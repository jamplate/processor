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
package org.jamplate.glucose.spec.syntax.term;

import org.jamplate.api.Spec;
import org.jamplate.function.Parser;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;

import static org.jamplate.impl.parser.HierarchyParser.hierarchy;
import static org.jamplate.internal.parser.TermParser.term;
import static org.jamplate.internal.util.Functions.parser;

/**
 * Digits sequence specification.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.20
 */
public class DigitsSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final DigitsSpec INSTANCE = new DigitsSpec();

	/**
	 * The kind of a digits sequence.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final String KIND = "term:digits";

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final String NAME = DigitsSpec.class.getSimpleName();

	@NotNull
	@Override
	public Parser getParser() {
		return parser(
				//search in the whole hierarchy
				p -> hierarchy(p),
				//target digits sequences
				p -> term(
						"(?:0[xb]|[0-9.])[0-9A-Fa-f_.]*(?:[Ee][0-9]*)?",
						(d, r) -> new Tree(
								d,
								r,
								new Sketch(DigitsSpec.KIND)
						)
				)
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return DigitsSpec.NAME;
	}
}
