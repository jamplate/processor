/*
 *	Copyright 2020 Cufy
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
package org.cufy.text;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * An abstract for the class for the interface {@link Element}.
 *
 * @author LSafer
 * @version 0.0.b
 * @since 0.0.b ~2020.10.11
 */
public abstract class AbstractElement implements Element {
	/**
	 * The mappings of the positions after this position.
	 *
	 * @since 0.0.c ~2020.11.02
	 */
	@NotNull
	protected final Map<String, Element> next = new HashMap();
	/**
	 * The mappings of the positions before this position.
	 *
	 * @since 0.0.c ~2020.11.02
	 */
	@NotNull
	protected final Map<String, Element> previous = new HashMap();
	/**
	 * The source of this element. (could be null!)
	 *
	 * @since 0.0.c ~2020.11.04
	 */
	@Nullable
	protected Source source;

	@Override
	@Nullable
	public Element getNext(@NotNull String relation) {
		Objects.requireNonNull(relation, "relation");
		return this.next.get(relation);
	}

	@Override
	@NotNull
	public Map<String, Element> getNextMap() {
		return Collections.unmodifiableMap(this.next);
	}

	@Override
	@Nullable
	public Element getPrevious(@NotNull String relation) {
		Objects.requireNonNull(relation, "relation");
		return this.previous.get(relation);
	}

	@Override
	@NotNull
	public Map<String, Element> getPreviousMap() {
		return Collections.unmodifiableMap(this.previous);
	}

	@Override
	@Nullable
	public Source getSource() {
		return this.source;
	}

	@Override
	public void putNext(@NotNull String relation, @NotNull Element element) {
		Objects.requireNonNull(relation, "relation");
		Objects.requireNonNull(element, "element");

		Element current = this.next.get(relation);

		if (current != element) {
			this.next.put(relation, element);

			if (current != null)
				current.removePrevious(relation);

			element.putPrevious(relation, this);
		}
	}

	@Override
	public void putPrevious(@NotNull String relation, @NotNull Element element) {
		Objects.requireNonNull(relation, "relation");
		Objects.requireNonNull(element, "element");

		Element current = this.previous.get(relation);

		if (element != current) {
			this.previous.put(relation, element);

			if (current != null)
				current.removeNext(relation);

			element.putPrevious(relation, this);
		}
	}

	@Override
	public void removeNext(@NotNull String relation) {
		Objects.requireNonNull(relation, "relation");

		Element current = this.next.get(relation);

		if (current != null) {
			this.next.remove(relation);
			current.removePrevious(relation);
		}
	}

	@Override
	public void removePrevious(@NotNull String relation) {
		Objects.requireNonNull(relation, "relation");

		Element current = this.previous.get(relation);

		if (current != null) {
			this.previous.remove(relation);
			current.removeNext(relation);
		}
	}

	@Override
	public void setSource(@NotNull Source source) {
		Objects.requireNonNull(source, "source");

		Source current = this.source;

		if (source != current) {
			if (current != null)
				throw new IllegalStateException("Source already set!");

			this.source = source;
		}
	}
}

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
//	/**
//	 * The position this element is at. (could be null!)
//	 *
//	 * @since 0.0.c ~2020.11.02
//	 */
//	protected Position position;
//	/**
//	 * The source string this element is from.
//	 *
//	 * @since 0.0.b ~2020.10.15
//	 */
//	protected Source source;
//	/**
//	 * The value of this linkable. (could be null!)
//	 *
//	 * @since 0.0.b ~2020.10.11
//	 */
//	protected Object value;
//
//	@Override
//	public Position getPosition() {
//		return this.position;
//	}
//
//	@Override
//	public Source getSource() {
//		return this.source;
//	}
//
//	@Override
//	public Object getValue() {
//		return this.value;
//	}
//
//	@Override
//	public Object invoke(Memory memory) throws IOException {
//		Objects.requireNonNull(memory, "memory");
//
//		Position position = this.position.getNext(Position.DEFAULT);
//		if (position != null) {
//			Element element = position.getElement();
//
//			if (element != null)
//				element.invoke(memory);
//		}
//
//		return null;
//	}
//
//	@Override
//	public void setPosition(Position position) {
//		Objects.requireNonNull(position, "position");
//
//		Position current = this.position;
//
//		if (position != current) {
//			if (current != null)
//				throw new IllegalStateException("Position already set!");
//
//			this.position = position;
//			position.setElement(this);
//		}
//	}
//
//	@Override
//	public void setValue(Object value) {
//		Objects.requireNonNull(value, "value");
//		this.value = value;
//	}
//
//	@Override
//	public void setSource(Source source) {
//		Objects.requireNonNull(source, "source");
//		if (this.source == null)
//			this.source = source;
//		else
//			throw new IllegalStateException("Element source already has been set!");
//	}
//
//	/**
//	 * An abstract for the interface {@link Element.Parser}.
//	 *
//	 * @author LSafer
//	 * @version 0.0.b
//	 * @since 0.0.b ~2020.10.16
//	 */
//	public abstract static class AbstractParser implements Parser {
//	}
