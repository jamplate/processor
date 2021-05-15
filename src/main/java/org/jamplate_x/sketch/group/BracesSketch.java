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
//package org.cufyplate.sketch.group;
//
//import org.cufyplate.sketch.anchor.BraceSketch;
//import org.jamplate.model.Reference;
//import org.jamplate.model.sketch.AbstractContextSketch;
//import org.jamplate.processor.parser.ContextParser;
//import org.jamplate.processor.parser.Parser;
//
///**
// * A sketch for brackets context.
// * <pre>
// *     {}
// * </pre>
// *
// * @author LSafer
// * @version 0.2.0
// * @since 0.2.0 ~2021.01.18
// */
//public final class BracesSketch extends AbstractContextSketch implements GroupSketch {
//	/**
//	 * A parser that parses sketches of this sketch kind from a sketch.
//	 *
//	 * @since 0.2.0 ~2021.02.08
//	 */
//	public static final Parser PARSER = new ContextParser("[{]", "[}]", BracesSketch::new, BraceSketch::new);
//
//	@SuppressWarnings("JavaDoc")
//	private static final long serialVersionUID = 1738781066145922168L;
//
//	/**
//	 * Construct a new sketch for the given {@code source}. The given source is the source
//	 * the constructed sketch will reserve.
//	 *
//	 * @param reference the source of the constructed sketch.
//	 * @throws NullPointerException if the given {@code source} is null.
//	 * @since 0.2.0 ~2021.01.18
//	 */
//	public BracesSketch(Reference reference) {
//		super(reference);
//	}
//}
