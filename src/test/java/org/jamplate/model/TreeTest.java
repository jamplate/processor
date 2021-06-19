package org.jamplate.model;

import org.jamplate.internal.model.PseudoDocument;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public class TreeTest {
	@Test
	public void same1() {
		Document d = new PseudoDocument();

		Tree bgr = new Tree(d, new Reference(0, 10), 0);
		Tree prv = new Tree(d, new Reference(2, 1), 0);
		Tree ctx = new Tree(d, new Reference(3, 3), -1);
		Tree chd1 = new Tree(d, new Reference(3, 1), 0);
		Tree chd2 = new Tree(d, new Reference(5, 1), 0);
		Tree nxt = new Tree(d, new Reference(6, 1), 0);

		bgr.offer(prv);
		bgr.offer(ctx);
		bgr.offer(nxt);
		bgr.offer(chd1);
		bgr.offer(chd2);

		Tree overlap1 = new Tree(d, new Reference(3, 3), 0);
		Tree overlap2 = new Tree(d, new Reference(3, 3), 1);

		chd2.offer(overlap2);
		chd2.offer(overlap1);

		assertSame(
				prv,
				bgr.getChild(),
				""
		);
		assertSame(
				ctx,
				bgr.getChild().getNext(),
				"Ctx lost its place"
		);
		assertSame(
				nxt,
				bgr.getChild().getNext().getNext(),
				""
		);
		assertSame(
				overlap1,
				bgr.getChild().getNext().getChild(),
				"First overlap not placed currently"
		);
		assertSame(
				overlap2,
				bgr.getChild().getNext().getChild().getChild(),
				"Second overlap not placed currently"
		);
		assertSame(
				chd1,
				bgr.getChild().getNext().getChild().getChild().getChild(),
				""
		);
		assertSame(
				chd2,
				bgr.getChild().getNext().getChild().getChild().getChild().getNext(),
				""
		);
	}
}
