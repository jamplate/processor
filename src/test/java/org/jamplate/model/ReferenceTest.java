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

import org.junit.jupiter.api.Test;

import static org.jamplate.InternalAssert.assertRelation;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReferenceTest {
	@Test
	public void relations() {
		Document document = new PseudoDocument("ABC0123");
		Reference reference = new Reference(0, document.read().length());
		Reference letters = reference.subReference(0, 3);
		Reference numbers = reference.subReference(3, 4);
		Reference b = reference.subReference(1, 1);
		Reference bc = letters.subReference(1, 2);
		Reference c0 = reference.subReference(2, 2);

		assertEquals("Wrong Slice", "ABC", document.read(letters).toString());
		assertEquals("Wrong Slice", "0123", document.read(numbers).toString());
		assertEquals("Wrong Slice", "B", document.read(b).toString());
		assertEquals("Wrong Slice", "BC", document.read(bc).toString());
		assertEquals("Wrong Slice", "C0", document.read(c0).toString());

		assertRelation(reference, reference, Relation.SAME);
		assertRelation(reference, letters, Relation.START);
		assertRelation(reference, numbers, Relation.END);
		assertRelation(reference, b, Relation.FRAGMENT);
		assertRelation(reference, bc, Relation.FRAGMENT);
		assertRelation(reference, c0, Relation.FRAGMENT);

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
//	@Test
//	public void lines() {
//		Document document = new PseudoDocument("ABC\n123\n=+-");
//		Reference reference = new Reference(0, document.read().length());
//		Reference c = reference.subReference(2);
//		Reference ln = reference.subReference(3);
//		Reference n3 = reference.subReference(6);
//		Reference ln2 = reference.subReference(7);
//		Reference eod = reference.subReference(11);
//
//		assertLine(reference, 1);
//		assertLine(c, 1);
//		assertLine(ln, 2);
//		assertLine(n3, 2);
//		assertLine(ln2, 3);
//		assertLine(eod, 3);
//	}
