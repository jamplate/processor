///*
// *	Copyright 2020 Cufy
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
//package org.cufy.preprocessor.link;
//
//import org.cufy.text.Element;
//import org.cufy.text.Memory;
//
///**
// * A logic that can evaluate to a {@code string}.
// * <p>
// * Any logic will have 3 stages.
// * <ul>
// *     <li>
// *         {@code Construction} the time the logic get constructed by its
// *         constructor. Mostly by a parser.
// *     </li>
// *     <li>
// *         {@code Linkage} the time the logic get linked with other logics
// *         making a full formula.
// *     </li>
// *     <li>
// *         {@code Evaluation/Invocation} the time the logic get evaluated with a
// *         memory containing the variables needed to evaluate that logic.
// *     </li>
// * </ul>
// *
// * @param <V> the type of the value held by this logic.
// * @author LSafer
// * @version 0.0.1
// * @since 0.0.1 ~2020.09.17
// */
//public interface Logic<V> extends Element<Logic, V> {
//	/**
//	 * Evaluate this logic.
//	 * <p>
//	 * A logic should evaluate to one of these types only:
//	 * <ul>
//	 *     <li>{@link Number}</li>
//	 *     <li>{@link Boolean}</li>
//	 *     <li>{@link String}</li>
//	 * </ul>
//	 *
//	 * @param memory the memory where this logic should be evaluated.
//	 * @return the result of evaluating this logic.
//	 * @throws NullPointerException if the given {@code memory} is null.
//	 * @since 0.0.b ~2020.10.05
//	 */
//	Object evaluate(Memory memory);
//}
