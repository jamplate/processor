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
//import org.intellij.lang.annotations.Language;
//import org.jamplate.model.Reference;
//import org.jamplate.model.node.AbstractNode;
//import org.jamplate.model.node.Node;
//import org.jamplate.model.node.Parsing;
//
//import java.util.Collections;
//import java.util.Objects;
//import java.util.Set;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
///**
// * A parser that targets sequences that its start matching a start-pattern and its end
// * matching an end-pattern.
// *
// * @author LSafer
// * @version 0.2.0
// * @since 0.2.0 ~2021.02.08
// */
//@SuppressWarnings("ClassWithTooManyConstructors")
//public class ContextParser implements Parser {
//	//	/**
//	//	 * A function backing this parser when it needs to construct a context-sketch for a
//	//	 * match.
//	//	 *
//	//	 * @since 0.2.0 ~2021.02.09
//	//	 */
//	//	protected final Function<Reference, Sketch> contextConstructor;
//	//	/**
//	//	 * A function backing this parser when it needs to construct a new end-sketch for a
//	//	 * match.
//	//	 *
//	//	 * @since 0.2.0 ~2021.02.09
//	//	 */
//	//	protected final Function<Reference, Sketch> endConstructor;
//	/**
//	 * The pattern to be used by this parser to match the end of the sequences with.
//	 *
//	 * @since 0.2.0 ~2021.02.09
//	 */
//	protected final Pattern endPattern;
//	//	/**
//	//	 * A function backing this parser when it needs to construct a new start-sketch for a
//	//	 * match.
//	//	 *
//	//	 * @since 0.2.0 ~2021.02.09
//	//	 */
//	//	protected final Function<Reference, Sketch> startConstructor;
//	protected final String qualifier;
//	/**
//	 * The pattern to be used by this parser to match the start of the sequences with.
//	 *
//	 * @since 0.2.0 ~2021.02.09
//	 */
//	protected final Pattern startPattern;
//
//	//	/**
//	//	 * Construct a new parser that uses the given {@code startRegex} and {@code
//	//	 * endRegex}.
//	//	 *
//	//	 * @param startRegex  the start-regex to be used by the constructed parser.
//	//	 * @param endRegex    the end-regex to be used by the constructed parser.
//	//	 * @param constructor a function backing the constructed parser to construct a new
//	//	 *                    context-sketches.
//	//	 * @throws NullPointerException   if the given {@code startRegex} or {@code endRegex}
//	//	 *                                or {@code constructor} is null.
//	//	 * @throws PatternSyntaxException if the given {@code startRegex} or {@code endRegex}
//	//	 *                                has a syntax error.
//	//	 * @since 0.2.0 ~2021.02.09
//	//	 */
//	public ContextParser(String qualifier, @Language("RegExp") String startRegex, @Language("RegExp") String endRegex) {
//		Objects.requireNonNull(qualifier, "qualifier");
//		Objects.requireNonNull(startRegex, "startRegex");
//		Objects.requireNonNull(endRegex, "endRegex");
//		this.qualifier = qualifier;
//		this.startPattern = Pattern.compile(startRegex);
//		this.endPattern = Pattern.compile(endRegex);
//	}
//
//	//	/**
//	//	 * Construct a new parser that uses the given {@code startPattern} and {@code
//	//	 * endPattern}.
//	//	 *
//	//	 * @param startPattern the start-pattern to be used by the constructed parser.
//	//	 * @param endPattern   the end-pattern to be used by the constructed parser.
//	//	 * @param constructor  a function backing the constructed parser to construct a new
//	//	 *                     context-sketches.
//	//	 * @throws NullPointerException if the given {@code startPattern} or {@code
//	//	 *                              endPattern} or {@code constructor} is null.
//	//	 * @since 0.2.0 ~2021.02.09
//	//	 */
//	public ContextParser(String qualifier, Pattern startPattern, Pattern endPattern) {
//		Objects.requireNonNull(qualifier, "qualifier");
//		Objects.requireNonNull(startPattern, "startPattern");
//		Objects.requireNonNull(endPattern, "endPattern");
//		this.qualifier = qualifier;
//		this.startPattern = startPattern;
//		this.endPattern = endPattern;
//	}
//	//
//	//	@SuppressWarnings("JavaDoc")
//	//	@Deprecated
//	//	private static boolean check(Node node, int i, int j) {
//	//		Objects.requireNonNull(node, "node");
//	//		Reference reference = node.reference();
//	//		switch (Dominance.compute(reference, i, j)) {
//	//			case PART:
//	//			case EXACT:
//	//				return StreamSupport.stream(node.spliterator(), true)
//	//						.allMatch(n ->
//	//								Dominance.compute(
//	//										reference,
//	//										i,
//	//										j
//	//								) == Dominance.NONE
//	//						);
//	//			default:
//	//				return false;
//	//		}
//	//	}
//
//	@Override
//	public Set<Node> parse(Node node) {
//		Objects.requireNonNull(node, "node");
//
//		if (node.hasMeta("qualifier", this.qualifier))
//			return Collections.emptySet();
//
//		Reference reference = node.reference();
//		CharSequence content = node.document().read();
//
//		int p = reference.position();
//		int t = p + reference.length();
//
//		Matcher startMatcher = this.startPattern.matcher(content)
//				.region(p, t)
//				.useTransparentBounds(true)
//				.useAnchoringBounds(true);
//		Matcher endMatcher = this.endPattern.matcher(content)
//				.region(p, t)
//				.useTransparentBounds(true)
//				.useAnchoringBounds(true);
//
//		//find the first valid end
//		while (endMatcher.find()) {
//			int s = endMatcher.start();
//			int e = endMatcher.end();
//
//			//validate end
//			if (Parsing.check(node, s, e)) {
//				startMatcher.reset()
//						.region(p, t);
//
//				int i = -1;
//				int j = -1;
//
//				//find the nearest valid start before the end
//				while (startMatcher.find()) {
//					int ii = startMatcher.start();
//					int jj = startMatcher.end();
//
//					if (ii >= s)
//						//early break
//						break;
//
//					//validate start
//					if (Parsing.check(node, ii, jj)) {
//						i = ii;
//						j = jj;
//					}
//				}
//
//				//validate match
//				if (i >= 0 && j >= 0) {
//					//bingo!
//					Node context = new AbstractNode(reference.subReference(
//							i - p, e - i
//					));
//					Node start = new AbstractNode(reference.subReference(
//							i - p, j - i
//					));
//					Node end = new AbstractNode(reference.subReference(
//							s - p, e - s
//					));
//
//					return Collections.singleton(context);
//				}
//			}
//		}
//
//		//no valid matches
//		return Collections.emptySet();
//	}
//}
