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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * An abstraction for the {@link Memory} class. Implementing the essential methods any
 * memory would implement.
 *
 * @author LSafer
 * @version 0.0.b
 * @since 0.0.b ~2020.10.02
 */
public abstract class AbstractMemory implements Memory {
	/**
	 * The mappings stored in this memory.
	 *
	 * @since 0.0.b ~2020.10.17
	 */
	protected final Map<String, Element> map = new HashMap();
	/**
	 * The parent memory of this memory.
	 *
	 * @since 0.0.b ~2020.10.17
	 */
	protected Memory memory;
	/**
	 * The output of this memory. (could be null!)
	 *
	 * @since 0.0.b ~2020.10.17
	 */
	protected Appendable output;

	@Override
	public Element getElement(String name) {
		Objects.requireNonNull(name, "name");

		Element element = this.map.get(name);

		return element == null ?
			   this.memory.getElement(name) :
			   element;
	}

	@Override
	public Appendable getOutput() {
		return this.output;
	}

	@Override
	public Memory getParent() {
		return this.memory;
	}

	@Override
	public void putElement(String name, Element element) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(element, "element");

		Element current = this.map.get(name);

		if (element != current) {
			if (current != null)
				throw new IllegalStateException("Element already set on this memory");

			this.map.put(name, element);
		}
	}

	@Override
	public void setOutput(Appendable output) {
		Objects.requireNonNull(output, "output");
		if (this.output == null)
			this.output = output;
		else
			throw new IllegalStateException("Memory output already has been set!");
	}

	@Override
	public void setParent(Memory memory) {
		Objects.requireNonNull(memory, "memory");
		if (this.memory == null)
			this.memory = memory;
		else
			throw new IllegalStateException("Memory parent already has been set!");
	}
}
