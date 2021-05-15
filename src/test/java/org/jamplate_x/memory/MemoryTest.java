///*
// *	Copyright 2021 Cufy
// *
// *	Licensed under the Apache License, Version 2.0 (the "License");
// *	you may not use this file except in compliance with the License.
// *	You may obtain a copy of the License at
// *
// *	    http://www.apache.org/licenses/LICENSE-2.0
// *
// *	Unless required by applicable law or agreed to in writing, software
// *	distributed under the License is distributed on an "AS IS" BASIS,
// *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *	See the License for the specific language governing permissions and
// *	limitations under the License.
// */
//package org.jamplate_x.memory;
//
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
//@SuppressWarnings({"MigrateAssertToMatcherAssert", "JUnitTestNG"})
//public class MemoryTest {
//	@Test
//	public void heap() {
//		Memory memory = new LinkedMemory();
//
//		long address = memory.allocate();
//		assertNull("initial value must be null", memory.get(address));
//		assertNull("initial value must be null", memory.put(address, "value"));
//		assertEquals("value must be stored in heap", "value", memory.get(address));
//		assertEquals("value must be stored in heap", "value", memory.free(address));
//		try {
//			memory.put(address, "ww");
//			fail("Address should be freed");
//		} catch (IllegalStateException ignored) {
//		}
//		long address2 = memory.allocate(); //implementation specific
//		assertSame("freed address not used", address, address2);
//		assertNull("a recycled address should have null as initial value", memory.get(address));
//		memory.free(address2);
//	}
//
//	@Test
//	public void stack() {
//		Memory memory = new LinkedMemory();
//
//		memory.push("value");
//		assertEquals("value must be stored in stack", "value", memory.peek());
//		assertEquals("value must be stored in stack", "value", memory.pop());
//		try {
//			memory.peek();
//			fail("Stack should be empty");
//		} catch (IllegalStateException ignored) {
//		}
//	}
//}
