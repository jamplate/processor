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
package org.jamplate.model.source;

/**
 * An implementation of the interface {@link Source} that is a slice of another source.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.13
 */
public class SourceSlice extends AbstractSource {
	/**
	 * Construct a new sub-source from the given {@code parent} source. The constructed
	 * source will have the same {@link #document()} as the given {@code parent} source.
	 * It will have its {@link #content()} lazily initialized and equals to the {@link
	 * String#substring(int, int)} of the {@link Document#readContent()} of the document
	 * of the given {@code parent} source. Also, the constructed source will have its
	 * {@link #position()} equals to the sum of the given {@code position} and the {@link
	 * #position()} of the given {@code parent} source. Finally, its obvious that the
	 * constructed source will have the given {@code parent} source as its {@link
	 * #parent()}.
	 * <br>
	 * Note: this constructor was built on trust. It trusts the implementation of the
	 * given {@code parent} source.
	 *
	 * @param parent   the parent source.
	 * @param position the sub-position to get from the given {@code parent} source.
	 * @param length   the length to get from the given {@code parent} source.
	 * @throws NullPointerException      if the given {@code parent} is null.
	 * @throws IllegalArgumentException  if the given {@code position} or {@code length}
	 *                                   is negative.
	 * @throws IndexOutOfBoundsException if {@code position + length} is more than the
	 *                                   length of the given {@code parent}.
	 * @see AbstractSource#AbstractSource(Source, int, int)
	 * @since 0.2.0 ~2021.01.17
	 */
	public SourceSlice(Source parent, int position, int length) {
		super(
				parent,
				position,
				length
		);
	}

	@Override
	public Source slice(int position) {
		//checked in the constructor
		return new SourceSlice(
				this,
				position,
				this.length() - position
		);
	}

	@Override
	public Source slice(int position, int length) {
		//checked in the constructor
		return new SourceSlice(
				this,
				position,
				length
		);
	}
}
