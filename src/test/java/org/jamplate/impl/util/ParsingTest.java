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
package org.jamplate.impl.util;

import org.jamplate.internal.util.Parsing;
import org.jamplate.internal.model.PseudoDocument;
import org.jamplate.model.Document;
import org.jamplate.model.Reference;
import org.jamplate.model.Tree;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ParsingTest {
	@Test
	public void parse() {
		Pattern sp = Pattern.compile("\\[");
		Pattern ep = Pattern.compile("\\]");

		Document document = new PseudoDocument("{[],[],[]}");
		Tree tree = new Tree(document);

		List<Reference> firstParse = Parsing.parseFirst(tree, sp, ep, 0);

		assertSame(
				3,
				firstParse.size(),
				"Expected 3 components"
		);
		assertEquals(
				new Reference(1, 2),
				firstParse.get(0),
				"The match is missing"
		);
		assertEquals(
				new Reference(1, 1),
				firstParse.get(1),
				"The opening is missing"
		);
		assertEquals(
				new Reference(2, 1),
				firstParse.get(2),
				"True ending is missing"
		);

		firstParse.stream()
				  .map(reference -> new Tree(document, reference))
				  .forEach(tree::offer);

		Set<List<Reference>> remainingParse = Parsing.parseAll(tree, sp, ep, 0);

		assertSame(
				2,
				remainingParse.size(),
				"Expected 2 remaining"
		);
		assertTrue(
				remainingParse.contains(
						Arrays.asList(
								new Reference(4, 2),
								new Reference(4, 1),
								new Reference(5, 1)
						)
				),
				"Second match is missing"
		);
		assertTrue(
				remainingParse.contains(
						Arrays.asList(
								new Reference(7, 2),
								new Reference(7, 1),
								new Reference(8, 1)
						)
				),
				"Third match is missing"
		);

		List<Tree> list = remainingParse
				.stream()
				.flatMap(List::stream)
				.map(reference -> new Tree(document, reference))
				.collect(Collectors.toList());
		Collections.shuffle(list);
		list.forEach(tree::offer);

		//todo convert from manual inspection to auto the insertion
		int i = 0;
	}

	@SuppressWarnings("JUnitTestMethodWithNoAssertions")
	@Test
	public void splits() {
		Document document = new PseudoDocument("#console X\n#include X\n#define X Y");
		Tree console = new Tree(document, new Reference(0, 10));
		Tree include = new Tree(document, new Reference(11, 10));
		Tree define = new Tree(document, new Reference(22, 11));

		List<Reference> splits = Parsing.parse(define, Pattern.compile("#(define)\\s(\\S+)\\s(.+)"));

		splits.forEach(
				split -> System.out.println(document.read(split))
		);
	}
}
