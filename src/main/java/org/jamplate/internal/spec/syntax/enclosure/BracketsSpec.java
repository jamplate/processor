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
package org.jamplate.internal.spec.syntax.enclosure;

import org.jamplate.api.Spec;
import org.jamplate.function.Parser;
import org.jamplate.internal.function.parser.pattern.EnclosureParser;
import org.jamplate.internal.function.parser.wrapper.HierarchyParser;
import org.jamplate.internal.spec.standard.AnchorSpec;
import org.jamplate.internal.util.Functions;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * Square brackets {@code []} specification.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.15
 */
public class BracketsSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	public static final BracketsSpec INSTANCE = new BracketsSpec();

	/**
	 * The kind of a square brackets {@code []} context.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final String KIND = "enclosure:brackets";

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.19
	 */
	@NotNull
	public static final String NAME = BracketsSpec.class.getSimpleName();

	@NotNull
	@Override
	public Parser getParser() {
		return Functions.parser(
				//search in the whole hierarchy
				HierarchyParser::new,
				//target brackets
				p -> new EnclosureParser(
						Pattern.compile("\\["),
						Pattern.compile("\\]"),
						//enclosure constructor
						(d, r) -> new Tree(
								d,
								r,
								new Sketch(BracketsSpec.KIND)
						),
						//open anchor constructor
						(t, r) -> t.offer(new Tree(
								t.document(),
								r,
								t.getSketch()
								 .get(AnchorSpec.KEY_OPEN)
								 .setKind(AnchorSpec.KIND_OPEN)
						)),
						//close anchor constructor
						(t, r) -> t.offer(new Tree(
								t.document(),
								r,
								t.getSketch()
								 .get(AnchorSpec.KEY_CLOSE)
								 .setKind(AnchorSpec.KIND_CLOSE)
						)),
						//body wrapper constructor
						(t, r) -> t.offer(new Tree(
								t.document(),
								r,
								t.getSketch()
								 .get(AnchorSpec.KEY_BODY)
								 .setKind(AnchorSpec.KIND_BODY),
								AnchorSpec.Z_INDEX_BODY
						))
				)
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return BracketsSpec.NAME;
	}
}
