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
package org.jamplate.model.sketch;

import org.cufyplate.sketch.group.*;
import org.jamplate.runtime.compilation.Compilation;
import org.jamplate.runtime.compilation.DynamicCompilation;
import org.jamplate.processor.parser.Parser;
import org.jamplate.processor.parser.PrecedentialParser;
import org.jamplate.processor.parser.SequentialParser;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.SortedSet;

import static org.jamplate.InternalAssert.assertCount;
import static org.jamplate.InternalAssert.assertDimensions;
import static org.junit.Assert.assertSame;

@SuppressWarnings({"JUnitTestNG", "MigrateAssertToMatcherAssert"})
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
	public void contexts() {
		Parser parser = new SequentialParser(
				ParenthesesSketch.PARSER,
				BracketsSketch.PARSER,
				BracesSketch.PARSER
		);
		Sketch sketch = new DocumentSketch("()[([]({}))]{[]}()");

		Compilation compilation = new DynamicCompilation();
		compilation.use(parser);
		compilation.append(sketch);
		compilation.parse();

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
	public void doubleQuotes() {
		Parser parser = new SequentialParser(
				new PrecedentialParser(
						DoubleQuotesSketch.PARSER,
						QuotesSketch.PARSER
				)
		);
		Sketch sketch = new DocumentSketch("\"'\"'\"'\"");

		Compilation compilation = new DynamicCompilation();
		compilation.use(parser);
		compilation.append(sketch);
		compilation.parse();

		assertSame(
				"Double quotes occurred first. So, it should be the dominant",
				DoubleQuotesSketch.class,
				getSketchAt(sketch, 0).getClass()
		);
		assertSame(
				"Single quotes occurred first. So, it should be the dominant",
				QuotesSketch.class,
				getSketchAt(sketch, 1).getClass()
		);
	}

	@Test
	public void parentheses() {
		Parser parser = ParenthesesSketch.PARSER;
		Sketch sketch = new DocumentSketch("(()()())");

		Compilation compilation = new DynamicCompilation();
		compilation.use(parser);
		compilation.append(sketch);
		compilation.parse();

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

	@Test
	public void quotes() {
		Parser parser = new SequentialParser(
				QuotesSketch.PARSER,
				ParenthesesSketch.PARSER,
				BracketsSketch.PARSER,
				BracesSketch.PARSER
		);
		Sketch sketch = new DocumentSketch("()'([{')}]");

		Compilation compilation = new DynamicCompilation();
		compilation.use(parser);
		compilation.append(sketch);
		compilation.parse();

		//document 1
		//	parentheses 3
		//	quotes 3
		assertCount(sketch, 7);
	}
}
