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
	static void assertRelation(Source<?> source, Source<?> other, SourceRelation relation) {
		assertSame("Relation of " + other + " to " + source, relation, source.relationOf(other));
		assertSame("Relation of " + source + " to " + other, relation.opposite(), other.relationOf(source));
	}

	@Test
	public void relations() {
		Source<?> source = new PseudoSource<>(0, "ABC0123", 0);
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

		assertRelation(source, source, SourceRelation.SAME);
		assertRelation(source, letters, SourceRelation.START);
		assertRelation(source, numbers, SourceRelation.END);
		assertRelation(source, b, SourceRelation.FRAGMENT);
		assertRelation(source, bc, SourceRelation.FRAGMENT);
		assertRelation(source, c0, SourceRelation.FRAGMENT);

		assertRelation(letters, letters, SourceRelation.SAME);
		assertRelation(letters, numbers, SourceRelation.NEXT);
		assertRelation(letters, b, SourceRelation.FRAGMENT);
		assertRelation(letters, bc, SourceRelation.END);
		assertRelation(letters, c0, SourceRelation.OVERFLOW);

		assertRelation(numbers, numbers, SourceRelation.SAME);
		assertRelation(numbers, b, SourceRelation.BEFORE);
		assertRelation(numbers, bc, SourceRelation.PREVIOUS);
		assertRelation(numbers, c0, SourceRelation.UNDERFLOW);

		assertRelation(b, b, SourceRelation.SAME);
		assertRelation(b, bc, SourceRelation.AHEAD);
		assertRelation(b, c0, SourceRelation.NEXT);

		assertRelation(bc, bc, SourceRelation.SAME);
		assertRelation(bc, c0, SourceRelation.OVERFLOW);

		assertRelation(c0, c0, SourceRelation.SAME);
	}
}
