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
package org.jamplate.glucose.spec.syntax.enclosure;

import org.jamplate.api.Spec;
import org.jamplate.function.Parser;
import org.jamplate.glucose.spec.standard.AnchorSpec;
import org.jamplate.model.Sketch;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;

import static org.jamplate.impl.parser.HierarchyParser.hierarchy;
import static org.jamplate.internal.parser.EnclosureParser.enclosure;
import static org.jamplate.internal.util.Functions.parser;

/**
 * Curly braces {@code {}} specification.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.06.20
 */
public class BracesSpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final BracesSpec INSTANCE = new BracesSpec();

	/**
	 * The kind of a curly braces {@code {}} context.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final String KIND = "enclosure:braces";

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.06.20
	 */
	@NotNull
	public static final String NAME = BracesSpec.class.getSimpleName();

	@NotNull
	@Override
	public Parser getParser() {
		return parser(
				//search in the whole hierarchy
				p -> hierarchy(p),
				//target braces
				p -> enclosure(
						"\\{",
						"\\}",
						//enclosure constructor
						(d, r) -> new Tree(
								d,
								r,
								new Sketch(BracesSpec.KIND)
						),
						//open anchor constructor
						(t, r) -> t.offer(new Tree(
								t.getDocument(),
								r,
								t.getSketch()
								 .get(AnchorSpec.KEY_OPEN)
								 .setKind(AnchorSpec.KIND_OPEN)
						)),
						//close anchor constructor
						(t, r) -> t.offer(new Tree(
								t.getDocument(),
								r,
								t.getSketch()
								 .get(AnchorSpec.KEY_CLOSE)
								 .setKind(AnchorSpec.KIND_CLOSE)
						)),
						//body wrapper constructor
						(t, r) -> t.offer(new Tree(
								t.getDocument(),
								r,
								t.getSketch()
								 .get(AnchorSpec.KEY_BODY)
								 .setKind(AnchorSpec.KIND_BODY),
								AnchorSpec.WEIGHT_BODY
						))
				)
		);
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return BracesSpec.NAME;
	}
}
