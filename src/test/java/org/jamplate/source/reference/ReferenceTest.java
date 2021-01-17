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

import org.jamplate.impl.Parentheses;
import org.jamplate.source.sketch.AbstractContextSketch;
import org.jamplate.source.sketch.DocumentSketch;
import org.jamplate.source.sketch.Sketch;
import org.jamplate.source.reference.Reference.Relation;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.SortedSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@SuppressWarnings({"MigrateAssertToMatcherAssert", "JUnitTestNG"})
public class ReferenceTest {
	static void assertCount(Sketch sketch, int count) {
		Objects.requireNonNull(sketch, "sketch");
		int[] sCount = {0};
		sketch.accept(s -> {
			sCount[0]++;
			return false;
		});
		assertSame(
				sketch + " has an unexpected inner sketches count",
				count,
				sCount[0]
		);
	}

	static void assertDimensions(Reference reference, int position, int length) {
		Objects.requireNonNull(reference, "source");
		assertSame(
				reference + " has an unexpected position ",
				position,
				reference.position()
		);
		assertSame(
				reference + " has an unexpected length ",
				length,
				reference.length()
		);
	}

	static void assertRelation(Reference reference, Reference other, Relation relation) {
		assertSame(
				"Relation of " + other + " to " + reference,
				relation,
				Reference.relation(reference, other)
		);
		assertSame(
				"Relation of " + reference + " to " + other,
				relation.opposite(),
				Reference.relation(other, reference)
		);
		assertSame(
				"Dominance of " + other + " over " + reference,
				relation.dominance(),
				Reference.dominance(reference, other)
		);
		assertSame(
				"Dominance of " + reference + " over " + other,
				relation.dominance().opposite(),
				Reference.dominance(other, reference)
		);
	}

	static Sketch getSketchAt(Sketch sketch, int position) {
		AbstractContextSketch contextSketch = (AbstractContextSketch) sketch;
		try {
			Field field = AbstractContextSketch.class.getDeclaredField("sketches");
			field.setAccessible(true);
			SortedSet set = (SortedSet) field.get(sketch);
			return (Sketch) set.stream()
					.skip(position)
					.findFirst()
					.get();
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void matcher() {
		Sketch sketch = new DocumentSketch("(()()())");

		while (sketch.accept(Parentheses.SKETCHER))
			;

		//base sketch
		assertCount(sketch, 13);
		//context group
		Sketch px = getSketchAt(sketch, 0);
		Sketch pxo = getSketchAt(px, 0);
		Sketch pxc = getSketchAt(px, 4);
		assertCount(px, 12);
		assertCount(pxo, 1);
		assertCount(pxc, 1);
		assertDimensions(px.source(), 0, 8);
		assertDimensions(pxo.source(), 0, 1);
		assertDimensions(pxc.source(), 7, 1);
		//first group
		Sketch p1 = getSketchAt(px, 1);
		Sketch p1o = getSketchAt(p1, 0);
		Sketch p1c = getSketchAt(p1, 1);
		assertCount(p1, 3);
		assertCount(p1o, 1);
		assertCount(p1c, 1);
		assertDimensions(p1.source(), 1, 2);
		assertDimensions(p1o.source(), 1, 1);
		assertDimensions(p1c.source(), 2, 1);
		//second group
		Sketch p2 = getSketchAt(px, 2);
		Sketch p2o = getSketchAt(p2, 0);
		Sketch p2c = getSketchAt(p2, 1);
		assertCount(p2, 3);
		assertCount(p2o, 1);
		assertCount(p2c, 1);
		assertDimensions(p2.source(), 3, 2);
		assertDimensions(p2o.source(), 3, 1);
		assertDimensions(p2c.source(), 4, 1);
		//third group
		Sketch p3 = getSketchAt(px, 3);
		Sketch p3o = getSketchAt(p3, 0);
		Sketch p3c = getSketchAt(p3, 1);
		assertCount(p3, 3);
		assertCount(p3o, 1);
		assertCount(p3c, 1);
		assertDimensions(p3.source(), 5, 2);
		assertDimensions(p3o.source(), 5, 1);
		assertDimensions(p3c.source(), 6, 1);
	}

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
