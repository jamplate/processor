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
package org.jamplate.model.source;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@SuppressWarnings({"MigrateAssertToMatcherAssert", "JUnitTestNG"})
public class SourceTest {
	static void assertRelation(Source<?> source, Source<?> other, Relation relation) {
		assertSame(
				"Relation of " + other + " to " + source,
				relation,
				Source.relation(source, other)
		);
		assertSame(
				"Relation of " + source + " to " +
				other,
				relation.opposite(),
				Source.relation(other, source)
		);
		assertSame(
				"Dominance of " + other + " over " + source,
				relation.dominance(),
				Source.dominance(source, other)
		);
		assertSame(
				"Dominance of " + source + " over " + other,
				relation.dominance().opposite(),
				Source.dominance(other, source)
		);
	}

	@Test
	public void relations() {
		Source<?> source = new PseudoSource<>("pseudo", "ABC0123", 0);
		Source<?> letters = source.slice(0, 3);
		Source<?> numbers = source.slice(3, 4);
		Source<?> b = source.slice(1, 1);
		Source<?> bc = letters.slice(1, 2);
		Source<?> c0 = source.slice(2, 2);

		assertEquals("Wrong Slice", "ABC", letters.content());
		assertEquals("Wrong Slice", "0123", numbers.content());
		assertEquals("Wrong Slice", "B", b.content());
		assertEquals("Wrong Slice", "BC", bc.content());
		assertEquals("Wrong Slice", "C0", c0.content());

		assertRelation(source, source, Relation.SAME);
		assertRelation(source, letters, Relation.START);
		assertRelation(source, numbers, Relation.END);
		assertRelation(source, b, Relation.FRAGMENT);
		assertRelation(source, bc, Relation.FRAGMENT);
		assertRelation(source, c0, Relation.FRAGMENT);

		assertRelation(letters, letters, Relation.SAME);
		assertRelation(letters, numbers, Relation.NEXT);
		assertRelation(letters, b, Relation.FRAGMENT);
		assertRelation(letters, bc, Relation.END);
		assertRelation(letters, c0, Relation.OVERFLOW);

		assertRelation(numbers, numbers, Relation.SAME);
		assertRelation(numbers, b, Relation.BEFORE);
		assertRelation(numbers, bc, Relation.PREVIOUS);
		assertRelation(numbers, c0, Relation.UNDERFLOW);

		assertRelation(b, b, Relation.SAME);
		assertRelation(b, bc, Relation.AHEAD);
		assertRelation(b, c0, Relation.NEXT);

		assertRelation(bc, bc, Relation.SAME);
		assertRelation(bc, c0, Relation.OVERFLOW);

		assertRelation(c0, c0, Relation.SAME);
	}
}
