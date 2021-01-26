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
package org.jamplate.source.reference;

import org.jamplate.source.Relation;
import org.junit.Test;

import static org.jamplate.InternalAssert.assertRelation;
import static org.junit.Assert.assertEquals;

@SuppressWarnings({"MigrateAssertToMatcherAssert", "JUnitTestNG"})
public class ReferenceTest {
	@Test
	public void relations() {
		Reference reference = new DocumentReference("ABC0123");
		Reference letters = reference.subReference(0, 3);
		Reference numbers = reference.subReference(3, 4);
		Reference b = reference.subReference(1, 1);
		Reference bc = letters.subReference(1, 2);
		Reference c0 = reference.subReference(2, 2);

		assertEquals("Wrong Slice", "ABC", letters.content());
		assertEquals("Wrong Slice", "0123", numbers.content());
		assertEquals("Wrong Slice", "B", b.content());
		assertEquals("Wrong Slice", "BC", bc.content());
		assertEquals("Wrong Slice", "C0", c0.content());

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
