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
package org.jamplate.impl.syntax;

import org.jamplate.compile.Parser;
import org.jamplate.impl.syntax.model.SyntaxScope;
import org.jamplate.impl.syntax.compile.LiteralParser;
import org.jamplate.impl.syntax.compile.ScopeParser;
import org.jetbrains.annotations.NotNull;

/**
 * The processors the jamplate default implementation offers for parsing.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.16
 */
public final class SyntaxParser {
	/**
	 * A parser parsing commas.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Parser COMMA = new LiteralParser(
			SyntaxPattern.COMMA
	).peek(tree -> tree.getSketch().setKind(SyntaxKind.COMMA));

	/**
	 * A parser parsing curly brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Parser CURLY = new ScopeParser(
			SyntaxPattern.CURLY_OPEN, SyntaxPattern.CURLY_CLOSE
	).peek(tree -> {
		tree.getSketch().setKind(SyntaxKind.CURLY);
		((SyntaxScope) tree.getSketch()).getOpenAnchor().setKind(SyntaxKind.CURLY_OPEN);
		((SyntaxScope) tree.getSketch()).getCloseAnchor().setKind(SyntaxKind.CURLY_CLOSE);
	});

	/**
	 * A parser parsing double quotes.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Parser DQUOTE = new ScopeParser(
			SyntaxPattern.DQUOTE
	).peek(tree -> {
		tree.getSketch().setKind(SyntaxKind.DQUOTE);
		((SyntaxScope) tree.getSketch()).getOpenAnchor().setKind(SyntaxKind.DQUOTE_OPEN);
		((SyntaxScope) tree.getSketch()).getCloseAnchor().setKind(SyntaxKind.DQUOTE_CLOSE);
	});

	/**
	 * A parser parsing escaped sequences.
	 *
	 * @since 0.2.0 ~2021.05.17
	 */
	@NotNull
	public static final Parser ESCAPE = new LiteralParser(
			SyntaxPattern.ESCAPE
	).peek(tree -> tree.getSketch().setKind(SyntaxKind.ESCAPE));

	/**
	 * A parser parsing quotes.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Parser QUOTE = new ScopeParser(
			SyntaxPattern.QUOTE
	).peek(tree -> {
		tree.getSketch().setKind(SyntaxKind.QUOTE);
		((SyntaxScope) tree.getSketch()).getOpenAnchor().setKind(SyntaxKind.QUOTE_OPEN);
		((SyntaxScope) tree.getSketch()).getCloseAnchor().setKind(SyntaxKind.QUOTE_CLOSE);
	});

	/**
	 * A parser parsing round brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Parser ROUND = new ScopeParser(
			SyntaxPattern.ROUND_OPEN, SyntaxPattern.ROUND_CLOSE
	).peek(tree -> {
		tree.getSketch().setKind(SyntaxKind.ROUND);
		((SyntaxScope) tree.getSketch()).getOpenAnchor().setKind(SyntaxKind.ROUND_OPEN);
		((SyntaxScope) tree.getSketch()).getCloseAnchor().setKind(SyntaxKind.ROUND_CLOSE);
	});

	/**
	 * A parser parsing square brackets.
	 *
	 * @since 0.2.0 ~2021.05.16
	 */
	@NotNull
	public static final Parser SQUARE = new ScopeParser(
			SyntaxPattern.SQUARE_OPEN, SyntaxPattern.SQUARE_CLOSE
	).peek(tree -> {
		tree.getSketch().setKind(SyntaxKind.SQUARE);
		((SyntaxScope) tree.getSketch()).getOpenAnchor().setKind(SyntaxKind.SQUARE_OPEN);
		((SyntaxScope) tree.getSketch()).getCloseAnchor().setKind(SyntaxKind.SQUARE_CLOSE);
	});

	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.05.16
	 */
	private SyntaxParser() {
		throw new AssertionError("No instance for you");
	}
}
