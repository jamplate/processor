///*
// *	Copyright 2020 Cufy
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
//package org.cufy.text;
//
//import java.util.Map;
//import java.util.Objects;
//
///**
// * An abstraction for the interface {@link Position}.
// *
// * @author LSafer
// * @version 0.0.c
// * @since 0.0.c ~2020.11.02
// */
//public abstract class AbstractPosition implements Position {
//	/**
//	 * The element at this position. (could be null!)
//	 *
//	 * @since 0.0.c ~2020.11.02
//	 */
//	protected Element element;
//	/**
//	 * The mappings of the positions after this position.
//	 *
//	 * @since 0.0.c ~2020.11.02
//	 */
//	protected Map<String, Position> next;
//	/**
//	 * The mappings of the positions before this position.
//	 *
//	 * @since 0.0.c ~2020.11.02
//	 */
//	protected Map<String, Position> previous;
//
//	{
//		char c = getElement().getSource().getArray()[0];
//	}
//	@Override
//	public Element getElement() {
//		return this.element;
//	}
//
//	@Override
//	public Position getNext(String relation) {
//		Objects.requireNonNull(relation, "relation");
//		return this.next.get(relation);
//	}
//
//	@Override
//	public Position getPrevious(String relation) {
//		Objects.requireNonNull(relation, "relation");
//		return this.previous.get(relation);
//	}
//
//	@Override
//	public void putNext(String relation, Position position) {
//		Objects.requireNonNull(relation, "relation");
//		Objects.requireNonNull(position, "position");
//
//		Position current = this.next.get(relation);
//
//		if (current != position) {
//			this.next.put(relation, position);
//
//			if (current != null)
//				current.removePrevious(relation);
//
//			position.putPrevious(relation, this);
//		}
//	}
//
//	@Override
//	public void putPrevious(String relation, Position position) {
//		Objects.requireNonNull(relation, "relation");
//		Objects.requireNonNull(position, "position");
//
//		Position current = this.previous.get(relation);
//
//		if (position != current) {
//			this.previous.put(relation, position);
//
//			if (current != null)
//				current.removeNext(relation);
//
//			position.putPrevious(relation, this);
//		}
//	}
//
//	@Override
//	public void removeNext(String relation) {
//		Objects.requireNonNull(relation, "relation");
//
//		Position current = this.next.get(relation);
//
//		if (current != null) {
//			this.next.remove(relation);
//			current.removePrevious(relation);
//		}
//	}
//
//	@Override
//	public void removePrevious(String relation) {
//		Objects.requireNonNull(relation, "relation");
//
//		Position current = this.previous.get(relation);
//
//		if (current != null) {
//			this.previous.remove(relation);
//			current.removeNext(relation);
//		}
//	}
//
//	@Override
//	public void setElement(Element element) {
//		Objects.requireNonNull(element, "element");
//
//		Element current = this.element;
//
//		if (element != current) {
//			if (current != null)
//				throw new IllegalStateException("Element already set!");
//
//			this.element = element;
//			element.setPosition(this);
//		}
//	}
//}
