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

import org.jamplate.model.source.Source.Relation;
import org.junit.Test;

import java.util.Objects;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@SuppressWarnings({"MigrateAssertToMatcherAssert", "JUnitTestNG"})
public class SourceTest {
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

	static void assertDimensions(Source source, int position, int length) {
		Objects.requireNonNull(source, "source");
		assertSame(
				source + " has an unexpected position ",
				position,
				source.position()
		);
		assertSame(
				source + " has an unexpected length ",
				length,
				source.length()
		);
	}

	static void assertRelation(Source source, Source other, Relation relation) {
		assertSame(
				"Relation of " + other + " to " + source,
				relation,
				Source.relation(source, other)
		);
		assertSame(
				"Relation of " + source + " to " + other,
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
	public void matcher() {
		TestContextSketch sketch = new TestContextSketch(new SourceSlice(new PseudoDocument("(()()())")));

		while (sketch.accept(s -> {
			Source source = Sketch.find(s, Pattern.compile("[(]"), Pattern.compile("[)]"));

			if (source != null) {
				Sketch it = new TestContextSketch(source);
				Sketch open = new TestConcreteSketch(source.slice(0, 1));
				Sketch close = new TestConcreteSketch(source.slice(
						source.length() - 1, 1));

				it.put(open);
				it.put(close);
				s.put(it);

				return true;
			}

			return false;
		}))
			;

		//base sketch
		assertCount(sketch, 13);
		//context group
		TestContextSketch px = (TestContextSketch) sketch.sketches.first();
		TestConcreteSketch pxo = (TestConcreteSketch) px.sketches.first();
		TestConcreteSketch pxc = (TestConcreteSketch) px.sketches.last();
		assertCount(px, 12);
		assertCount(pxo, 1);
		assertCount(pxc, 1);
		assertDimensions(px.source, 0, 8);
		assertDimensions(pxo.source, 0, 1);
		assertDimensions(pxc.source, 7, 1);
		//first group
		TestContextSketch p1 = (TestContextSketch) px.sketches.stream().skip(1).findFirst().get();
		TestConcreteSketch p1o = (TestConcreteSketch) p1.sketches.first();
		TestConcreteSketch p1c = (TestConcreteSketch) p1.sketches.last();
		assertCount(p1, 3);
		assertCount(p1o, 1);
		assertCount(p1c, 1);
		assertDimensions(p1.source, 1, 2);
		assertDimensions(p1o.source, 1, 1);
		assertDimensions(p1c.source, 2, 1);
		//second group
		TestContextSketch p2 = (TestContextSketch) px.sketches.stream().skip(2).findFirst().get();
		TestConcreteSketch p2o = (TestConcreteSketch) p2.sketches.first();
		TestConcreteSketch p2c = (TestConcreteSketch) p2.sketches.last();
		assertCount(p2, 3);
		assertCount(p2o, 1);
		assertCount(p2c, 1);
		assertDimensions(p2.source, 3, 2);
		assertDimensions(p2o.source, 3, 1);
		assertDimensions(p2c.source, 4, 1);
		//third group
		TestContextSketch p3 = (TestContextSketch) px.sketches.stream().skip(3).findFirst().get();
		TestConcreteSketch p3o = (TestConcreteSketch) p3.sketches.first();
		TestConcreteSketch p3c = (TestConcreteSketch) p3.sketches.last();
		assertCount(p3, 3);
		assertCount(p3o, 1);
		assertCount(p3c, 1);
		assertDimensions(p3.source, 5, 2);
		assertDimensions(p3o.source, 5, 1);
		assertDimensions(p3c.source, 6, 1);
	}

	@Test
	public void relations() {
		Source source = new SourceSlice(new PseudoDocument("ABC0123"));
		Source letters = source.slice(0, 3);
		Source numbers = source.slice(3, 4);
		Source b = source.slice(1, 1);
		Source bc = letters.slice(1, 2);
		Source c0 = source.slice(2, 2);

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

	public static class TestConcreteSketch extends AbstractConcreteSketch {
		public TestConcreteSketch(Source source) {
			super(source);
		}
	}

	public static class TestContextSketch extends AbstractContextSketch {
		public TestContextSketch(Source source) {
			super(source);
		}
	}
}
