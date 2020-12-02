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

/**
 * A memory that can hold constant context variables. Memory will be accessed only at the invocation
 * time. Before that it should not be accessed at all. Unless it is a constant non-changing memory
 * (like environment variables).
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2020.09.19
 */
public interface Memory {
	/**
	 * Get the element mapped to the given {@code name}.
	 *
	 * @param name the name of the element to look for. Or null if no element has been mapped to the
	 *             given {@code name}.
	 * @return the element mapped to the given {@code name}.
	 * @throws NullPointerException if the given {@code name} is null.
	 * @since 0.0.b ~2020.10.15
	 */
	Element getElement(String name);

	/**
	 * Get the output of this memory.
	 *
	 * @return the output of this memory. Or null if it has not been set yet.
	 * @since 0.0.b ~2020.10.15
	 */
	Appendable getOutput();

	/**
	 * Get the parent memory of this memory.
	 *
	 * @return the parent memory of this memory. Or null if it has not been set yet.
	 * @since 0.0.b ~2020.10.17
	 */
	Memory getParent();

	/**
	 * Map the given {@code element} to the given {@code name} in this memory.
	 *
	 * @param name    the name to map the given {@code element} to.
	 * @param element the element to be mapped to the given {@code name}.
	 * @throws NullPointerException  if the given {@code name} or {@code element} is null.
	 * @throws IllegalStateException if the given {@code name} already has been mapped to another
	 *                               element in this memory (parent memory not included).
	 * @since 0.0.b ~2020.10.15
	 */
	void putElement(String name, Element element);

	/**
	 * Set the {@code output} of this memory to be the given {@code output}.
	 *
	 * @param output the output to be set as the output of this memory.
	 * @throws NullPointerException  if the given {@code output} is null.
	 * @throws IllegalStateException if the output of this memory already has been set.
	 * @since 0.0.b ~2020.10.17
	 */
	void setOutput(Appendable output);

	/**
	 * Set the given {@code memory} to be the parent memory of this memory.
	 *
	 * @param memory the memory to be the parent memroy of this memory.
	 * @throws NullPointerException  if the given {@code memory} is null.
	 * @throws IllegalStateException if the parent memory of this memory already has been set.
	 * @since 0.0.b ~2020.10.17
	 */
	void setParent(Memory memory);
}
