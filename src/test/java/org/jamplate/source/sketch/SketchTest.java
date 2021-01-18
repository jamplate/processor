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
package org.jamplate.source.sketch;

import org.jamplate.impl.sketch.CurlyBracketsSketch;
import org.jamplate.impl.sketch.ParenthesesSketch;
import org.jamplate.impl.sketch.SquareBracketsSketch;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.SortedSet;

import static org.jamplate.InternalAssert.assertCount;
import static org.jamplate.InternalAssert.assertDimensions;

@SuppressWarnings("JUnitTestNG")
public class SketchTest {
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
	public void liveMix() {
		Sketch sketch = new DocumentSketch("()[([]({}))]{[]}()");

		while (sketch.accept(ParenthesesSketch.VISITOR))
			;
		while (sketch.accept(SquareBracketsSketch.VISITOR))
			;
		while (sketch.accept(CurlyBracketsSketch.VISITOR))
			;

		//document 1
		//	parentheses 3
		//	square-brackets 3
		//		parentheses 3
		//			square-brackets 3
		//			parentheses 3
		//				curly-brackets 3
		//	curly-brackets 3
		//		square-brackets 3
		//	parentheses 3
		assertCount(sketch, 28);
	}

	@Test
	public void matcher() {
		Sketch sketch = new DocumentSketch("(()()())");

		while (sketch.accept(ParenthesesSketch.VISITOR))
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
		assertDimensions(px.reference(), 0, 8);
		assertDimensions(pxo.reference(), 0, 1);
		assertDimensions(pxc.reference(), 7, 1);
		//first group
		Sketch p1 = getSketchAt(px, 1);
		Sketch p1o = getSketchAt(p1, 0);
		Sketch p1c = getSketchAt(p1, 1);
		assertCount(p1, 3);
		assertCount(p1o, 1);
		assertCount(p1c, 1);
		assertDimensions(p1.reference(), 1, 2);
		assertDimensions(p1o.reference(), 1, 1);
		assertDimensions(p1c.reference(), 2, 1);
		//second group
		Sketch p2 = getSketchAt(px, 2);
		Sketch p2o = getSketchAt(p2, 0);
		Sketch p2c = getSketchAt(p2, 1);
		assertCount(p2, 3);
		assertCount(p2o, 1);
		assertCount(p2c, 1);
		assertDimensions(p2.reference(), 3, 2);
		assertDimensions(p2o.reference(), 3, 1);
		assertDimensions(p2c.reference(), 4, 1);
		//third group
		Sketch p3 = getSketchAt(px, 3);
		Sketch p3o = getSketchAt(p3, 0);
		Sketch p3c = getSketchAt(p3, 1);
		assertCount(p3, 3);
		assertCount(p3o, 1);
		assertCount(p3c, 1);
		assertDimensions(p3.reference(), 5, 2);
		assertDimensions(p3o.reference(), 5, 1);
		assertDimensions(p3c.reference(), 6, 1);
	}
}
