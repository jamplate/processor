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
//package org.cufyplate.sketch.punctuation;
//
//import org.jamplate.model.Reference;
//import org.jamplate.model.sketch.AbstractConcreteSketch;
//import org.jamplate.processor.parser.Parser;
//import org.jamplate.processor.parser.ConcreteParser;
//
///**
// * A sketch for comma symbol.
// * <pre>
// *     ,
// * </pre>
// *
// * @author LSafer
// * @version 0.2.0
// * @since 0.2.0 ~2021.01.18
// */
//public final class CommaSketch extends AbstractConcreteSketch implements PunctuationSketch {
//	/**
//	 * A parser that parses sketches of this sketch kind from a sketch.
//	 *
//	 * @since 0.2.0 ~2021.02.08
//	 */
//	public static final Parser PARSER = new ConcreteParser("[,]", CommaSketch::new);
//
//	@SuppressWarnings("JavaDoc")
//	private static final long serialVersionUID = 1417694492546192558L;
//
//	/**
//	 * Construct a new sketch with the given {@code reference}. The given source reference
//	 * is the reference the constructed sketch will reserve.
//	 *
//	 * @param reference the source reference of the constructed sketch.
//	 * @throws NullPointerException if the given {@code reference} is null.
//	 * @since 0.2.0 ~2021.01.26
//	 */
//	public CommaSketch(Reference reference) {
//		super(reference);
//	}
//}
