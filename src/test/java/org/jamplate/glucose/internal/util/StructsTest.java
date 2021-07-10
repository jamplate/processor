package org.jamplate.glucose.internal.util;

import org.jamplate.glucose.value.VArray;
import org.jamplate.glucose.value.VObject;
import org.jamplate.memory.Memory;
import org.junit.jupiter.api.Test;

import static org.jamplate.glucose.internal.util.Structs.remove;
import static org.jamplate.glucose.internal.util.Values.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StructsTest {
	@Test
	public void removeElement0() {
		VArray array = arr(
				value("element0"),
				value("element1"),
				value("element2")
		);

		array = remove(array, number(1));

		assertEquals(
				"[\"element0\",\"element2\"]",
				array.eval(new Memory()),
				"Not removed properly"
		);
	}

	@Test
	public void removePair0() {
		VObject object = obj(
				pair("key0:value0"),
				pair("key1:value1"),
				pair("key2:value2")
		);

		object = remove(object, quote("key1"));

		assertEquals(
				"{\"key0\":\"value0\",\"key2\":\"value2\"}",
				object.eval(new Memory()),
				"Not removed properly"
		);
	}
}
