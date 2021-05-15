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
//package org.jamplate.processor.parser;
//
//import org.jamplate.model.Dominance;
//import org.jamplate.model.sketch.Sketch;
//
//import java.util.*;
//import java.util.stream.Collectors;
//import java.util.stream.StreamSupport;
//
///**
// * A wrapping parser that uses other parsers and solve any clashes (if any) by choosing
// * the first sketch in position. (see {@link Sketch#COMPARATOR})
// *
// * @author LSafer
// * @version 0.2.0
// * @since 0.2.0 ~2021.02.08
// */
//public class PrecedentialParser implements Parser {
//	/**
//	 * The parsers used by this parser. (the wrapped parsers)
//	 *
//	 * @since 0.2.0 ~2021.02.09
//	 */
//	protected final Set<Parser> parsers;
//
//	/**
//	 * Construct a new parser wrapping the given {@code parsers} solving any clashes by
//	 * choosing the first in position.
//	 *
//	 * @param parsers the parsers to be wrapped by this parser.
//	 * @throws NullPointerException if the given {@code parsers} is null.
//	 * @since 0.2.0 ~2021.02.09
//	 */
//	public PrecedentialParser(Parser... parsers) {
//		Objects.requireNonNull(parsers, "parsers");
//		this.parsers = Arrays.stream(parsers)
//				.filter(Objects::nonNull)
//				.collect(Collectors.toSet());
//	}
//
//	/**
//	 * Construct a new parser wrapping the given {@code parsers} solving any clashes by
//	 * choosing the first in position.
//	 *
//	 * @param parsers the parsers to be wrapped by this parser.
//	 * @throws NullPointerException if the given {@code parsers} is null.
//	 * @since 0.2.0 ~2021.02.09
//	 */
//	public PrecedentialParser(Iterable<Parser> parsers) {
//		Objects.requireNonNull(parsers, "parsers");
//		//noinspection ConstantConditions
//		this.parsers = StreamSupport.stream(parsers.spliterator(), true)
//				.filter(Parser.class::isInstance)
//				.collect(Collectors.toSet());
//	}
//
//	@Override
//	public Set<Sketch> parse(Sketch sketch) {
//		Objects.requireNonNull(sketch, "sketch");
//		List<Sketch> sketches = this.parsers.stream()
//				.parallel()
//				.map(parser -> parser.parse(sketch))
//				.flatMap(Collection::stream)
//				.sorted(Sketch.COMPARATOR)
//				.collect(Collectors.toCollection(ArrayList::new));
//
//		Iterator<Sketch> iterator = sketches.iterator();
//		if (iterator.hasNext()) {
//			Sketch prev = iterator.next();
//
//			while (iterator.hasNext()) {
//				Sketch next = iterator.next();
//
//				switch (Dominance.compute(prev, next)) {
//					case SHARE:
//					case EXACT:
//						iterator.remove();
//						break;
//					default:
//						prev = next;
//						break;
//				}
//			}
//		}
//
//		return Collections.unmodifiableSet(new HashSet<>(sketches));
//	}
//}
