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
 * A pseudo source that have been programmatically. (not from a real document)
 *
 * @param <D> the type of the pseudo document.
 * @author LSafer
 * @version 0.0.2
 * @since 0.0.2 ~2021.01.09
 */
public class PseudoSource<D extends Comparable> extends AbstractSource<D> {
	/**
	 * Construct a new "made" source that acts like it has took its content from the given
	 * {@code document} with the given {@code position}. When it is content is actually
	 * the given {@code content}.
	 *
	 * @param document the of the constructed source.
	 * @param content  the content of the constructed source.
	 * @param position the position of this source at the given {@code document}.
	 * @throws NullPointerException if the given {@code document} or {@code content} is
	 *                              null.
	 * @since 0.0.2 ~2021.01.9
	 */
	public PseudoSource(D document, CharSequence content, int position) {
		super(
				document,
				content,
				position
		);
	}

	/**
	 * Construct a new sub-source from the given {@code parent} source. The constructed
	 * source will have the same {@link #root()} and {@link #document()} as the given
	 * {@code parent} source. But, it will have its {@link #content()} equals to the
	 * {@link String#substring(int, int)} of the {@link #content()} of the given {@code
	 * parent} source. Also, the constructed source will have its {@link #position()}
	 * equals to the sum of the given {@code pos} and the {@link #position()} of the given
	 * {@code parent} source. Finally, its obvious that the constructed source will have
	 * the given {@code parent} source as its {@link #parent()}.
	 *
	 * @param parent the parent source.
	 * @param pos    the sub-position to get from the given {@code parent} source.
	 * @param len    the length to get from the given {@code parent} source.
	 * @throws NullPointerException      if the given {@code parent} is null.
	 * @throws IllegalArgumentException  if the given {@code pos} or {@code len} is
	 *                                   negative.
	 * @throws IndexOutOfBoundsException if {@code parent.content().substring(pos, len)}
	 *                                   throws it.
	 * @since 0.0.2 ~2021.01.8
	 */
	public PseudoSource(Source<D> parent, int pos, int len) {
		super(
				parent,
				pos,
				len
		);
	}

	@Override
	public Source<D> slice(int pos) {
		return new PseudoSource<>(
				this,
				pos,
				this.content.length() - pos
		);
	}

	@Override
	public Source<D> slice(int pos, int len) {
		return new PseudoSource<>(
				this,
				pos,
				len
		);
	}
}
