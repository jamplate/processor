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
//package org.jamplate_x.parser;
//
//import cufy.util.Node;
//import org.intellij.lang.annotations.Language;
//import org.jamplate.model.sketch.Sketch;
//import org.jamplate.processor.Compilation;
//import org.jamplate.processor.Processor;
//
//import java.util.Objects;
//import java.util.regex.Pattern;
//import java.util.regex.PatternSyntaxException;
//
///**
// * A parser that targets sequences matching a pattern.
// *
// * @author LSafer
// * @version 0.2.0
// * @since 0.2.0 ~2021.01.26
// */
//public class ConcreteParser implements Processor {
//	/**
//	 * The pattern to be used by this parser to match the sequences with.
//	 *
//	 * @since 0.2.0 ~2021.02.09
//	 */
//	protected final Pattern pattern;
//	protected final String qualifier;
//
//	/**
//	 * Construct a new parser that uses the given {@code regex}.
//	 *
//	 * @param regex the regex to be used by the constructed parser.
//	 * @throws NullPointerException   if the given {@code regex} is null.
//	 * @throws PatternSyntaxException if the given {@code regex} has a syntax error.
//	 * @since 0.2.0 ~2021.02.09
//	 */
//	public ConcreteParser(String qualifier, @Language("RegExp") String regex) {
//		Objects.requireNonNull(qualifier, "qualifier");
//		Objects.requireNonNull(regex, "regex");
//		this.pattern = Pattern.compile(regex);
//		this.qualifier = qualifier;
//	}
//
//	/**
//	 * Construct a new parser that uses the given {@code pattern}.
//	 *
//	 * @param pattern the pattern to be used by the constructed parser.
//	 * @throws NullPointerException if the given {@code pattern} is null.
//	 * @since 0.2.0 ~2021.02.09
//	 */
//	public ConcreteParser(String qualifier, Pattern pattern) {
//		Objects.requireNonNull(qualifier, "qualifier");
//		Objects.requireNonNull(pattern, "pattern");
//		this.pattern = pattern;
//		this.qualifier = qualifier;
//	}
//
//	//	@Override
//	//	public Set<Node> parse(Node node) {
//	//		Objects.requireNonNull(node, "node");
//	//
//	//		if (node.hasMeta("qualifier", this.qualifier))
//	//			return Collections.emptySet();
//	//
//	//		Document document = node.document();
//	//		Reference reference = node.reference();
//	//
//	//		CharSequence content = document.read();
//	//		int p = reference.position();
//	//		int t = p + reference.length();
//	//
//	//		Matcher matcher = this.pattern.matcher(content)
//	//									  .region(p, t)
//	//									  .useTransparentBounds(true)
//	//									  .useAnchoringBounds(true);
//	//
//	//		//search for a valid match
//	//		while (matcher.find()) {
//	//			int i = matcher.start();
//	//			int j = matcher.end();
//	//
//	//			//validate match
//	//			if (Parsing.check(node, i, j)) {
//	//				//bingo!
//	//				Node match = new AbstractNode(
//	//						document,
//	//						reference.subReference(
//	//								i - p, j - i
//	//						)
//	//				);
//	//
//	//				return Collections.singleton(match);
//	//			}
//	//		}
//	//
//	//		//no valid matches
//	//		return Collections.emptySet();
//	//	}
//
//	@Override
//	public void process(Compilation compilation, Node<Sketch> node) {
//		Objects.requireNonNull(compilation, "compilation");
//		Objects.requireNonNull(node, "node");
//	}
//}
