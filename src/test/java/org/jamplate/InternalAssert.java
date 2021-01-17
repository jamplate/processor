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

import org.jamplate.source.reference.Reference;
import org.jamplate.source.sketch.Sketch;

import java.util.Objects;

import static org.junit.Assert.assertSame;

@SuppressWarnings({"MigrateAssertToMatcherAssert", "UtilityClass"})
public final class InternalAssert {
	private InternalAssert() {
		throw new AssertionError("No instance for you!");
	}

	public static void assertCount(Sketch sketch, int count) {
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

	public static void assertRelation(Reference reference, Reference other, Reference.Relation relation) {
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
}
