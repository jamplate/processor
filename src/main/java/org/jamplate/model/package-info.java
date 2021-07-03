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
 * <br><br>
 * Enums
 * <ul>
 *     <li>
 *         {@link org.jamplate.model.Relation Relation}
 *         <br>
 *         An enumeration representing a relation between two trees.
 *     </li>
 *     <li>
 *         {@link org.jamplate.model.Dominance Dominance}
 *         <br>
 *         An enumeration representing how dominance a tree over another.
 *     </li>
 *     <li>
 *         {@link org.jamplate.model.Intersection Intersection}
 *         <br>
 *         An enumeration representing how a tree intersect with another.
 *     </li>
 * </ul>
 * <br>
 * Constant Components:
 * <ul>
 *     <li>
 *         {@link org.jamplate.model.Document Document}
 *         <br>
 *         A constant that represents a source unit.
 *     </li>
 *     <li>
 *         {@link org.jamplate.model.Reference Reference}
 *         <br>
 *         A constant that represents a range (area).
 *     </li>
 * </ul>
 * <br>
 * Building Components:
 * <ul>
 *     <li>
 *         {@link org.jamplate.model.Tree Tree}
 *         <br>
 *         An absolute building block that reserves an area in a document.
 *         <ul>
 *             <li>Its {@link org.jamplate.model.Document Document}.</li>
 *             <li>Its {@link org.jamplate.model.Reference Reference}.</li>
 *             <li>The {@link org.jamplate.model.Tree Trees} around it.</li>
 *             <li>A {@link org.jamplate.model.Sketch Sketch} representing the tree.</li>
 *         </ul>
 *     </li>
 *     <li>
 *         {@link org.jamplate.model.Sketch Sketch}
 *         <br>
 *         A relative building block that represent its data without a position.
 *         <ul>
 *             <li>Its origin {@link org.jamplate.model.Tree Tree}.</li>
 *             <li>Its component {@link org.jamplate.model.Sketch Sketchs}.</li>
 *             <li>Additional meta data.</li>
 *         </ul>
 *     </li>
 * </ul>
 * <br>
 * Built Components:
 * <ul>
 *     <li>
 *         {@link org.jamplate.memory.Value Value}
 *         <br>
 *         A built component that can be evaluated into a string.
 *     </li>
 *     <li>
 *         {@link org.jamplate.model.Instruction Instruction}
 *         <br>
 *         A built component that can be executed.
 *         <ul>
 *             <li>Its origin {@link org.jamplate.model.Tree}.</li>
 *         </ul>
 *     </li>
 * </ul>
 * <br>
 * Environment Components:
 * <ul>
 *     <li>
 *         {@link org.jamplate.model.Environment Environment}
 *         <br>
 *         A fixed structure that holds the variables of a whole process.
 *         <ul>
 *             <li>A {@link org.jamplate.diagnostic.Diagnostic Diagnostic} Manager.</li>
 *             <li>A set of {@link org.jamplate.model.Compilation Compilations}.</li>
 *             <li>Additional meta data.</li>
 *         </ul>
 *     </li>
 *     <li>
 *         {@link org.jamplate.model.Compilation Compilation}
 *         <br>
 *         A fixed structure that holds the variables of a whole document.
 *         <ul>
 *             <li>The represented {@link org.jamplate.model.Document Document}.</li>
 *             <li>A root {@link org.jamplate.model.Tree Tree}.</li>
 *             <li>A compiled {@link org.jamplate.model.Instruction Instruction}.</li>
 *             <li>Additional meta data.</li>
 *         </ul>
 *     </li>
 * </ul>
 * <br>
 * Volatile Components:
 * <ul>
 *     <li>
 *         {@link org.jamplate.memory.Frame Frame}
 *         <br>
 *         A highly stateful mutable structure that can store and release values/data in a
 *         stack and heap fashion.
 *         <li>The host {@link org.jamplate.model.Instruction Instruction}.</li>
 *         <li>A stack of values.</li>
 *         <li>A heap of values.</li>
 *     </li>
 *     <li>
 *         {@link org.jamplate.memory.Memory Memory}
 *         <br>
 *         A highly stateful mutable structure that can store and release frames in a
 *         stack fashion.
 *         <ul>
 *             <li>A stack of {@link org.jamplate.memory.Frame Frames}.</li>
 *             <li>A console.</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.17
 */
package org.jamplate.model;
