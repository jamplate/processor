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
package org.jamplate;

import org.jamplate.model.Dominance;
import org.jamplate.model.Relation;
import org.jamplate.model.reference.Reference;
import org.jamplate.model.sketch.Sketch;

import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

@SuppressWarnings({"MigrateAssertToMatcherAssert", "UtilityClass"})
public final class InternalAssert {
	private InternalAssert() {
		throw new AssertionError("No instance for you!");
	}

	public static void assertCount(Sketch sketch, int count) {
		Objects.requireNonNull(sketch, "sketch");

		Deque<Sketch> deque = new LinkedList<>(Collections.singleton(sketch));
		int c = 0;
		while (!deque.isEmpty()) {
			c++;
			deque.poll().forEach(deque::add);
		}

		assertSame(
				sketch + " has an unexpected inner sketches count",
				count,
				c
		);
	}

	public static void assertDimensions(Reference reference, int position, int length) {
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

	public static void assertLine(Reference reference, int line) {
		Objects.requireNonNull(reference, "reference");
		assertSame(
				"Reference " + reference + " calculated to unexpected line",
				line,
				reference.line()
		);
		Reference lineReference = reference.lineReference();
		assertSame(
				"Has an invalid line reference " + reference,
				line,
				lineReference.line()
		);
		assertFalse(
				"Has an invalid line reference " + reference,
				lineReference.content().
						toString()
						.substring(1)
						.contains("\n")
		);
	}

	public static void assertRelation(Reference reference, Reference other, Relation relation) {
		assertSame(
				"Relation of " + other + " to " + reference,
				relation,
				Relation.compute(reference, other)
		);
		assertSame(
				"Relation of " + reference + " to " + other,
				relation.opposite(),
				Relation.compute(other, reference)
		);
		assertSame(
				"Dominance of " + other + " over " + reference,
				relation.dominance(),
				Dominance.compute(reference, other)
		);
		assertSame(
				"Dominance of " + reference + " over " + other,
				relation.dominance().opposite(),
				Dominance.compute(other, reference)
		);
	}
}
