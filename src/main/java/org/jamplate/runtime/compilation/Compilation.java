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
package org.jamplate.runtime.compilation;

import org.jamplate.model.document.Document;
import org.jamplate.model.reference.Reference;
import org.jamplate.model.sketch.Sketch;
import org.jamplate.processor.parser.Parser;

/**
 * A compilation is a fully-stated linear-storage object that acts like the core processor
 * of a single process.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.31
 */
public interface Compilation {
	/**
	 * Adapt the given {@code compilation} as a sub compilation to this compilation.
	 * <br>
	 * The adapted compilation shall be read-only by this compilation.
	 * <br>
	 * Invoking this method multiple times with the {@link Object#equals(Object) same}
	 * argument will result to the same behaviour as invoking it one time.
	 *
	 * @param compilation the compilation to be adapted by this compilation.
	 * @throws NullPointerException if the given {@code compilation} is null.
	 * @since 0.2.0 ~2021.02.08
	 */
	void adapt(Compilation compilation);

	/**
	 * Append the given {@code document} to this compilation.
	 * <br>
	 * Invoking this method multiple times with the {@link Object#equals(Object) same}
	 * argument will result to the same behaviour as invoking it one time.
	 *
	 * @param document the document to be appended to this compilation.
	 * @throws NullPointerException if the given {@code document} is null.
	 * @since 0.2.0 ~2021.02.01
	 */
	void append(Document document);

	/**
	 * Append the given {@code reference} to this compilation.
	 * <br>
	 * Invoking this method multiple times with the {@link Object#equals(Object) same}
	 * argument will result to the same behaviour as invoking it one time.
	 *
	 * @param reference the reference to be appended to this compilation.
	 * @throws NullPointerException if the given {@code reference} is null.
	 * @since 0.2.0 ~2021.02.01
	 */
	void append(Reference reference);

	/**
	 * Append the given {@code sketch} to this compilation.
	 * <br>
	 * Invoking this method multiple times with the {@link Object#equals(Object) same}
	 * argument will result to the same behaviour as invoking it one time.
	 *
	 * @param sketch the sketch to be appended.
	 * @throws NullPointerException if the given {@code sketch} is null.
	 * @since 0.2.0 ~2021.02.09
	 */
	void append(Sketch sketch);

	/**
	 * Parse the not-yet-parsed elements in this compilation.
	 * <br>
	 * After invoking this method. Any non-parsed element that has got appended should be
	 * parsed.
	 *
	 * @since 0.2.0 ~2021.02.01
	 */
	void parse();

	/**
	 * Add the given {@code parser} to the parsers set of this compilation to be used by
	 * it.
	 * <br>
	 * Very Important Note: the parsers in a compilation must not target clash-able
	 * syntax. Otherwise, clash exceptions might be thrown!
	 * <br>
	 * Invoking this method multiple times with the {@link Object#equals(Object) same}
	 * argument will result to the same behaviour as invoking it one time.
	 *
	 * @param parser the parser to be used.
	 * @throws NullPointerException if the given {@code parser} is null.
	 * @since 0.2.0 ~2021.02.01
	 */
	void use(Parser parser);
}
