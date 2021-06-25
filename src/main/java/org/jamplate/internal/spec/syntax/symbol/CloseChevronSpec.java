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
package org.jamplate.internal.spec.syntax.symbol;

import org.jamplate.api.Spec;
import org.jamplate.function.Parser;
import org.jamplate.internal.util.Functions;
import org.jamplate.internal.util.parser.pattern.TermParser;
import org.jamplate.internal.util.parser.wrapper.HierarchyParser;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * Close Chevron {@code >} specification.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.20
 */
public class CloseChevronSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final CloseChevronSpec INSTANCE = new CloseChevronSpec();

	/**
	 * The kind of the close chevron {@code >} symbol.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final String KIND = "symbol:close_chevron";

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final String NAME = CloseChevronSpec.class.getSimpleName();

	@NotNull
	@Override
	public Parser getParser() {
		return Functions.parser(
				//search in the whole hierarchy
				HierarchyParser::new,
				//target close
				p -> new TermParser(
						Pattern.compile(">"),
						(d, r) -> new Tree(d, r, new Sketch(CloseChevronSpec.KIND))
				)
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return CloseChevronSpec.NAME;
	}
}
