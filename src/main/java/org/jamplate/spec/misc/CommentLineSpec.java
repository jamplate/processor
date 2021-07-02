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
package org.jamplate.spec.misc;

import org.jamplate.api.Spec;
import org.jamplate.function.Parser;
import org.jamplate.internal.function.parser.pattern.EnclosureParser;
import org.jamplate.spec.standard.AnchorSpec;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * Comment line specifications.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.24
 */
public class CommentLineSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	public static final CommentLineSpec INSTANCE = new CommentLineSpec();

	/**
	 * The kind of the comment line tree.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	public static final String KIND = "comment:line";

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.24
	 */
	@NotNull
	public static final String NAME = CommentLineSpec.class.getSimpleName();

	@NotNull
	@Override
	public Parser getParser() {
		//parse only on the first round
		return new EnclosureParser(
				Pattern.compile("//"),
				Pattern.compile("(?=[\r\n])(?<!\\\\)|(?=$)"),
				//enclosure constructor
				(d, r) -> new Tree(
						d,
						r,
						new Sketch(CommentLineSpec.KIND)
				),
				//open anchor constructor
				(t, r) -> t.offer(new Tree(
						t.getDocument(),
						t.getSketch()
						 .get(AnchorSpec.KEY_OPEN)
						 .setKind(AnchorSpec.KIND_OPEN)
				)),
				//close anchor constructor
				null,
				//body wrapper constructor
				(t, r) -> t.offer(new Tree(
						t.getDocument(),
						r,
						t.getSketch()
						 .get(AnchorSpec.KEY_BODY)
						 .setKind(AnchorSpec.KIND_BODY),
						AnchorSpec.WEIGHT_BODY
				))
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return CommentLineSpec.NAME;
	}
}
