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

	static void assertRelation(Source source, Source other, Source.Relation relation) {
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
	public void matcher() {
		TestSketch sketch = new TestSketch(new SourceSlice(new PseudoDocument("(()()())")));

		//These 2 loops shall be interchangeable, mergable and independent
		while (sketch.accept(s -> {
			Source source = s.find(Pattern.compile("[(][^()]*[)]"));

			if (source != null) {
				Sketch it = new TestSketch(source);
				Sketch open = new TestSketch(source.slice(0, 1), true);
				Sketch close = new TestSketch(source.slice(source.length() - 1, 1), true);

				it.put(open);
				it.put(close);
				s.put(it);

				return true;
			}

			return false;
		}))
			;
		while (sketch.accept(s -> {
			Source source = s.find(Pattern.compile("[(]"), Pattern.compile("[)]"));

			if (source != null) {
				Sketch it = new TestSketch(source);
				Sketch open = new TestSketch(source.slice(0, 1), true);
				Sketch close = new TestSketch(source.slice(source.length() - 1, 1), true);

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
		TestSketch px = (TestSketch) sketch.sketches.first();
		TestSketch pxo = (TestSketch) px.sketches.first();
		TestSketch pxc = (TestSketch) px.sketches.last();
		assertCount(px, 12);
		assertCount(pxo, 1);
		assertCount(pxc, 1);
		assertDimensions(px.source, 0, 8);
		assertDimensions(pxo.source, 0, 1);
		assertDimensions(pxc.source, 7, 1);
		//first group
		TestSketch p1 = (TestSketch) px.sketches.stream().skip(1).findFirst().get();
		TestSketch p1o = (TestSketch) p1.sketches.first();
		TestSketch p1c = (TestSketch) p1.sketches.last();
		assertCount(p1, 3);
		assertCount(p1o, 1);
		assertCount(p1c, 1);
		assertDimensions(p1.source, 1, 2);
		assertDimensions(p1o.source, 1, 1);
		assertDimensions(p1c.source, 2, 1);
		//second group
		TestSketch p2 = (TestSketch) px.sketches.stream().skip(2).findFirst().get();
		TestSketch p2o = (TestSketch) p2.sketches.first();
		TestSketch p2c = (TestSketch) p2.sketches.last();
		assertCount(p2, 3);
		assertCount(p2o, 1);
		assertCount(p2c, 1);
		assertDimensions(p2.source, 3, 2);
		assertDimensions(p2o.source, 3, 1);
		assertDimensions(p2c.source, 4, 1);
		//third group
		TestSketch p3 = (TestSketch) px.sketches.stream().skip(3).findFirst().get();
		TestSketch p3o = (TestSketch) p3.sketches.first();
		TestSketch p3c = (TestSketch) p3.sketches.last();
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

		assertRelation(source, source, Source.Relation.SAME);
		assertRelation(source, letters, Source.Relation.START);
		assertRelation(source, numbers, Source.Relation.END);
		assertRelation(source, b, Source.Relation.FRAGMENT);
		assertRelation(source, bc, Source.Relation.FRAGMENT);
		assertRelation(source, c0, Source.Relation.FRAGMENT);

		assertRelation(letters, letters, Source.Relation.SAME);
		assertRelation(letters, numbers, Source.Relation.NEXT);
		assertRelation(letters, b, Source.Relation.FRAGMENT);
		assertRelation(letters, bc, Source.Relation.END);
		assertRelation(letters, c0, Source.Relation.OVERFLOW);

		assertRelation(numbers, numbers, Source.Relation.SAME);
		assertRelation(numbers, b, Source.Relation.BEFORE);
		assertRelation(numbers, bc, Source.Relation.PREVIOUS);
		assertRelation(numbers, c0, Source.Relation.UNDERFLOW);

		assertRelation(b, b, Source.Relation.SAME);
		assertRelation(b, bc, Source.Relation.AHEAD);
		assertRelation(b, c0, Source.Relation.NEXT);

		assertRelation(bc, bc, Source.Relation.SAME);
		assertRelation(bc, c0, Source.Relation.OVERFLOW);

		assertRelation(c0, c0, Source.Relation.SAME);
	}

	public static class TestSketch extends AbstractSketch {
		private final boolean reserved;

		public TestSketch(Source source) {
			this(source, false);
		}

		public TestSketch(Source source, boolean reserved) {
			super(source);
			this.reserved = reserved;
		}

		@Override
		public Source find(Pattern pattern) {
			return this.reserved ? null : super.find(pattern);
		}

		@Override
		public Source find(Pattern startPattern, Pattern endPattern) {
			return this.reserved ? null : super.find(startPattern, endPattern);
		}

		@Override
		public void put(Sketch sketch) {
			if (this.reserved)
				throw new UnsupportedOperationException();
			super.put(sketch);
		}
	}
}
