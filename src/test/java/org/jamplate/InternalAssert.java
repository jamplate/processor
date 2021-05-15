package org.jamplate;

import org.jamplate.model.Dominance;
import org.jamplate.model.Reference;
import org.jamplate.model.Relation;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertSame;

@SuppressWarnings("UtilityClass")
public final class InternalAssert {
	public static void assertDimensions(Reference reference, int position, int length) {
		Objects.requireNonNull(reference, "source");
		assertSame(
				position,
				reference.position(),
				reference + " has an unexpected position "
		);
		assertSame(
				length,
				reference.length(),
				reference + " has an unexpected length "
		);
	}

	public static void assertRelation(Reference reference, Reference other, Relation relation) {
		assertSame(
				relation,
				Relation.compute(reference, other),
				"Relation of " + other + " to " + reference
		);
		assertSame(
				relation.opposite(),
				Relation.compute(other, reference),
				"Relation of " + reference + " to " + other
		);
		assertSame(
				relation.dominance(),
				Dominance.compute(reference, other),
				"Dominance of " + other + " over " + reference
		);
		assertSame(
				relation.dominance().opposite(),
				Dominance.compute(other, reference),
				"Dominance of " + reference + " over " + other
		);
	}
}
//
//	public static void assertLine(Reference reference, int line) {
//		Objects.requireNonNull(reference, "reference");
//		assertSame(
//				"Reference " + reference + " calculated to unexpected line",
//				line,
//				reference.line()
//		);
//		Reference lineReference = reference.lineReference();
//		assertSame(
//				"Has an invalid line reference " + reference,
//				line,
//				lineReference.line()
//		);
//		assertFalse(
//				"Has an invalid line reference " + reference,
//				lineReference.content().
//						toString()
//						.substring(1)
//						.contains("\n")
//		);
//	}
//
//	public static void assertCount(Sketch sketch, int count) {
//		Objects.requireNonNull(sketch, "sketch");
//
//		Deque<Sketch> deque = new LinkedList<>(Collections.singleton(sketch));
//		int c = 0;
//		while (!deque.isEmpty()) {
//			c++;
//			deque.poll().forEach(deque::add);
//		}
//
//		assertSame(
//				sketch + " has an unexpected inner sketches count",
//				count,
//				c
//		);
//	}
