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
//package org.jamplate.x.memory;
//
//import java.util.LinkedHashMap;
//import java.util.LinkedList;
//import java.util.Map;
//import java.util.Queue;
//
///**
// * A memory that uses {@link java.util.LinkedList} as its stack and {@link
// * java.util.LinkedHashMap} as its heap.
// *
// * @author LSafer
// * @version 0.2.0
// * @since 0.2.0 ~2021.01.31
// */
//public class LinkedMemory implements Memory {
//	/**
//	 * A list of freed memory addresses.
//	 *
//	 * @since 0.2.0 ~2021.01.31
//	 */
//	protected final Queue<Long> free = new LinkedList<>();
//	/**
//	 * A map backing the heap part of this memory.
//	 *
//	 * @since 0.2.0 ~2021.01.31
//	 */
//	protected final Map<Long, Object> heap = new LinkedHashMap<>();
//	/**
//	 * A list backing the stack part of this memory.
//	 *
//	 * @since 0.2.0 ~2021.01.31
//	 */
//	protected final Queue<Object> stack = new LinkedList<>();
//
//	/**
//	 * The last available free memory.
//	 *
//	 * @since 0.2.0 ~2021.01.31
//	 */
//	protected long head;
//
//	@Override
//	public long allocate() {
//		long address = this.free.isEmpty() ?
//					   ++this.head :
//					   this.free.poll();
//		this.heap.put(address, null);
//		return address;
//	}
//
//	@Override
//	public Object free(long address) {
//		if (this.heap.containsKey(address)) {
//			this.free.add(address);
//			return this.heap.remove(address);
//		}
//
//		throw new IllegalStateException("address not allocated");
//	}
//
//	@Override
//	public Object get(long address) {
//		if (this.heap.containsKey(address))
//			return this.heap.get(address);
//
//		throw new IllegalStateException("address not allocated");
//	}
//
//	@Override
//	public Object peek() {
//		if (this.stack.isEmpty())
//			throw new IllegalStateException("stack is empty");
//
//		return this.stack.peek();
//	}
//
//	@Override
//	public Object pop() {
//		if (this.stack.isEmpty())
//			throw new IllegalStateException("stack is empty");
//
//		return this.stack.poll();
//	}
//
//	@Override
//	public void push(Object value) {
//		this.stack.add(value);
//	}
//
//	@Override
//	public Object put(long address, Object value) {
//		if (this.heap.containsKey(address))
//			return this.heap.put(address, value);
//
//		throw new IllegalStateException("address not allocated");
//	}
//}
