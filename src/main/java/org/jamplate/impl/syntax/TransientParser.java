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
import org.jamplate.impl.model.Command;
import org.jamplate.impl.model.Scope;
import org.jamplate.impl.syntax.compile.ScopeParser;
import org.jamplate.model.Document;
import org.jamplate.model.Reference;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;

/**
 * Parsers that parses sketches that to be elements.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.19
 */
public final class TransientParser {
	/**
	 * A parser that parses a single-line command.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@SuppressWarnings("OverlyLongLambda")
	@NotNull
	public static final Parser COMMAND = new ScopeParser(
			Command::new, TransientPattern.COMMAND_OPEN, TransientPattern.COMMAND_CLOSE
	).peek(tree -> {
		Command sketch = (Command) tree.getSketch();
		sketch.setKind(TransientKind.COMMAND);
		sketch.getOpenAnchor().setKind(TransientKind.COMMAND_OPEN);
		sketch.getCloseAnchor().setKind(TransientKind.COMMAND_CLOSE);
		sketch.getType().setKind(TransientKind.COMMAND_TYPE);
		sketch.getParameter().setKind(TransientKind.COMMAND_PARAMETER);

		//define the trees of `type` and `parameter`
		Document document = tree.document();
		Reference open = sketch.getOpenAnchor().getTree().reference();
		Reference close = sketch.getCloseAnchor().getTree().reference();
		int position = open.position() + open.length();
		int length = close.position() - position;

		int middle = document.read(new Reference(position, length))
							 .toString()
							 .indexOf(' ');

		Tree t = new Tree(document, new Reference(
				position,
				middle == -1 ? length : middle
		));
		Tree p = new Tree(document, new Reference(
				middle == -1 ? position + length : middle + 1,
				middle == -1 ? 0 : length - middle
		));

		sketch.getType().setTree(t);
		sketch.getParameter().setTree(p);

		if (t.reference().length() != 0)
			tree.offer(t);
		if (p.reference().length() != 0)
			tree.offer(p);
	});

	/**
	 * A parser that parses comment blocks.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final Parser COMMENT_BLOCK = new ScopeParser(
			TransientPattern.COMMENT_BLOCK_OPEN, TransientPattern.COMMENT_BLOCK_CLOSE
	).peek(tree -> {
		tree.getSketch().setKind(TransientKind.COMMENT_BLOCK);
		((Scope) tree.getSketch()).getOpenAnchor().setKind(TransientKind.COMMENT_BLOCK_OPEN);
		((Scope) tree.getSketch()).getCloseAnchor().setKind(TransientKind.COMMENT_BLOCK_CLOSE);
	});

	/**
	 * A parser that parses commented lines.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final Parser COMMENT_LINE = new ScopeParser(
			TransientPattern.COMMENT_LINE_OPEN, TransientPattern.COMMENT_LINE_CLOSE
	).peek(tree -> {
		tree.getSketch().setKind(TransientKind.COMMENT_LINE);
		((Scope) tree.getSketch()).getOpenAnchor().setKind(TransientKind.COMMENT_LINE_OPEN);
		((Scope) tree.getSketch()).getCloseAnchor().setKind(TransientKind.COMMENT_LINE_CLOSE);
	});

	/**
	 * A parser parsing injection sequences.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	public static final Parser INJECTION = new ScopeParser(
			TransientPattern.INJECTION_OPEN, TransientPattern.INJECTION_CLOSE
	).peek(tree -> {
		tree.getSketch().setKind(TransientKind.INJECTION);
		((Scope) tree.getSketch()).getOpenAnchor().setKind(TransientKind.INJECTION_OPEN);
		((Scope) tree.getSketch()).getCloseAnchor().setKind(TransientKind.INJECTION_CLOSE);
	});

	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.05.19
	 */
	private TransientParser() {
		throw new AssertionError("No instance for you");
	}
}
