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
/**
 * A package specialized to organize source flow.
 * <br>
 * Common Syntax:
 * <pre>
 *     source: a name for a source representing the first area.
 *     i: the first index of the first area.
 *     j: one past the last index of the first area.
 *     m: the length of the first area.
 *     other: a name for a source representing the second area.
 *     s: the first index of the second area.
 *     e: one past the last index of the second area.
 *     n: the length of the second area.
 * </pre>
 * <br>
 * Document reading is as follows:
 * <ul>
 *     <li>First, the source is converted into a {@link org.jamplate.model.source.Document}</li>
 *     <li>Second, the document is converted into a {@link org.jamplate.model.source.Source}</li>
 *     <li>Third, the source is converted into a {@link org.jamplate.model.source.Sketch}</li>
 *     <li>Forth, the sketch is solved via a {@link org.jamplate.model.source.Sketch.Visitor}</li>
 *     <li>Fifth, the sketch is converted into an element via a {@link org.jamplate.model.source.Sketch.Visitor}</li>
 *     <li>Sixth, the elements are used</li>
 * </ul>
 *
 * @author LSafer
 * @version 0.0.2
 * @since 0.0.2 ~2021.01.06
 */
package org.jamplate.model.source;