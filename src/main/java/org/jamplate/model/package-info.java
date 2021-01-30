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
 * A package that specifies that primary components of a processor that uses the
 * jamplate-style processing.
 * <br>
 * The components are processed as follows:
 * <ul>
 *     <li>Source-Code are wrapped with {@link org.jamplate.model.document.Document}s.</li>
 *     <li>Documents get sliced into {@link org.jamplate.model.reference.Reference}s.</li>
 *     <li>References get referenced by {@link org.jamplate.model.sketch.Sketch}s.</li>
 *     <li>Sketches get converted into {@link org.jamplate.model.element.Element}s.</li>
 *     <li>Elements get compiled into {@link org.jamplate.model.instruction.Instruction}s.</li>
 *     <li>Instructions can be executed.</li>
 * </ul>
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.17
 */
package org.jamplate.model;
