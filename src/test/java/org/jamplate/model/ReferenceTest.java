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
package org.jamplate.model;

import org.jamplate.impl.model.PseudoDocument;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class ReferenceTest {
	public static void assertRelation(Reference reference, Reference other, Intersection intersection) {
		assertSame(
				intersection,
				Intersection.compute(reference, other),
				"Relation of " + other + " to " + reference
		);
		assertSame(
				intersection.opposite(),
				Intersection.compute(other, reference),
				"Relation of " + reference + " to " + other
		);
		assertSame(
				intersection.dominance(),
				Dominance.compute(reference, other),
				"Dominance of " + other + " over " + reference
		);
		assertSame(
				intersection.dominance().opposite(),
				Dominance.compute(other, reference),
				"Dominance of " + reference + " over " + other
		);
	}

	@Test
	public void relations0() {
		Document document = new PseudoDocument("ABC0123");
		Reference reference = new Reference(0, document.read().length());
		Reference letters = reference.subReference(0, 3);
		Reference numbers = reference.subReference(3, 4);
		Reference b = reference.subReference(1, 1);
		Reference bc = letters.subReference(1, 2);
		Reference c0 = reference.subReference(2, 2);

		assertEquals("ABC", document.read(letters).toString(), "Wrong Slice");
		assertEquals("0123", document.read(numbers).toString(), "Wrong Slice");
		assertEquals("B", document.read(b).toString(), "Wrong Slice");
		assertEquals("BC", document.read(bc).toString(), "Wrong Slice");
		assertEquals("C0", document.read(c0).toString(), "Wrong Slice");

		assertRelation(reference, reference, Intersection.SAME);
		assertRelation(reference, letters, Intersection.START);
		assertRelation(reference, numbers, Intersection.END);
		assertRelation(reference, b, Intersection.FRAGMENT);
		assertRelation(reference, bc, Intersection.FRAGMENT);
		assertRelation(reference, c0, Intersection.FRAGMENT);

		assertRelation(letters, letters, Intersection.SAME);
		assertRelation(letters, numbers, Intersection.NEXT);
		assertRelation(letters, b, Intersection.FRAGMENT);
		assertRelation(letters, bc, Intersection.END);
		assertRelation(letters, c0, Intersection.OVERFLOW);

		assertRelation(numbers, numbers, Intersection.SAME);
		assertRelation(numbers, b, Intersection.BEFORE);
		assertRelation(numbers, bc, Intersection.PREVIOUS);
		assertRelation(numbers, c0, Intersection.UNDERFLOW);

		assertRelation(b, b, Intersection.SAME);
		assertRelation(b, bc, Intersection.AHEAD);
		assertRelation(b, c0, Intersection.NEXT);

		assertRelation(bc, bc, Intersection.SAME);
		assertRelation(bc, c0, Intersection.OVERFLOW);

		assertRelation(c0, c0, Intersection.SAME);
	}
}
