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
//package org.jamplate.model.document;
//
//import java.util.Objects;
//
///**
// * An abstraction of the basic functionality of a document. fixme remove | no purpose
// *
// * @author LSafer
// * @version 0.2.0
// * @since 0.2.0 ~2021.01.17
// */
//public abstract class AbstractDocument implements Document {
//	@SuppressWarnings("JavaDoc")
//	private static final long serialVersionUID = -1442065210677397016L;
//
//	@Override
//	public boolean equals(Object object) {
//		return object instanceof Document &&
//			   Objects.equals(this.toString(), ((Document) object).toString());
//	}
//
//	@Override
//	public int hashCode() {
//		return this.toString().hashCode();
//	}
//}
////	@Override
////	public String toString() {
////		return this.name.toQualifiedString();
////	}
////
////	@Override
////	public Name name() {
////		return this.name;
////	}
//
////
////	/**
////	 * True, if this document bas been constructed using its constructor. (in other words
////	 * 'not deserialized')
////	 *
////	 * @since 0.2.0 ~2021.01.17
////	 */
////	@SuppressWarnings("TransientFieldNotInitialized")
////	protected final transient boolean constructed;
////
////	/**
////	 * Pieces of the content of this document. It is initialized on the constructor since
////	 * it has no logical purpose to exist when this document is deserialized. (lazily
////	 * initialized content)
////	 *
////	 * @since 0.2.0 ~2021.02.16
////	 */
////	@SuppressWarnings("TransientFieldNotInitialized")
////	protected final transient Map<int[], CharSequence> slices;
////
////	/**
////	 * The content of this document. (lazily initialized)
////	 *
////	 * @since 0.2.0 ~2021.02.17
////	 */
////	protected transient CharSequence content;
////
////	@Override
////	public int length() {
////		if (!this.constructed)
////			throw new IllegalStateException("Deserialized Document");
////		if (this.length == -1)
////			this.length = this.read().length();
////
////		return this.length;
////	}
////	//
////	//	@Override
////	//	public IntStream lines() {
////	//		if (!this.constructed)
////	//			throw new IllegalStateException("Deserialized Document");
////	//		if (this.lines == null) {
////	//			CharSequence content = this.read();
////	//			//noinspection MagicCharacter
////	//			this.lines = IntStream.range(0, content.length())
////	//					.filter(i -> i == 0 || content.charAt(i) == '\n')
////	//					.toArray();
////	//		}
////	//
////	//		return Arrays.stream(this.lines);
////	//	}
////
////	@SuppressWarnings("MagicCharacter")
////	@Override
////	public int line(Reference reference) {
////		Objects.requireNonNull(reference, "reference");
////		if (this.steps == null) {
////			CharSequence content = this.read();
////			this.steps = IntStream.range(0, content.length())
////					.filter(i -> {
////						switch (content.charAt(i)) {
////							case '\n':
////								return i == 0 || content.charAt(i - 1) != '\r';
////							case '\r':
////								return true;
////							default:
////								return false;
////						}
////					})
////					.toArray();
////		}
////
////		int p = reference.position();
////		return IntStream.range(0, this.steps.length)
////				.parallel()
////				.filter(i -> p < this.steps[i])
////				.findFirst()
////				.orElse(1);
////	}
////
////	@SuppressWarnings("MagicCharacter")
////	@Override
////	public int lines() {
////		if (!this.constructed)
////			throw new IllegalStateException("Deserialized Document");
////		if (this.lines == -1) {
////			CharSequence content = this.read();
////			this.lines = IntStream.range(0, content.length())
////					.parallel()
////					.filter(i -> {
////						switch (content.charAt(i)) {
////							case '\n':
////								return i == 0 || content.charAt(i - 1) != '\r';
////							case '\r':
////								return true;
////							default:
////								return false;
////						}
////					})
////					.sum();
////		}
////
////		return this.lines;
////	}
//
////
////	/**
////	 * The length of this document. (lazily initialized)
////	 *
////	 * @since 0.2.0 ~2021.01.27
////	 */
////	@SuppressWarnings("TransientFieldNotInitialized")
////	protected transient int length = -1;
////	/**
////	 * The count of lines this document has got. (lazily initialized)
////	 *
////	 * @since 0.2.0 ~2021.02.17
////	 */
////	protected transient int lines = -1;
////	/**
////	 * An array of the positions of the lines in this document. (lazily initialized)
////	 *
////	 * @since 0.2.0 ~2021.01.27
////	 */
////	protected transient int[] steps;
