/*
 *	Copyright 2020-2021 Cufy
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
 * This is the top package of the jamplate preprocessor.
 * <br><br>
 * Abstract Packages:
 * <ul>
 *     <li>{@link org.jamplate.diagnostic}</li>
 *     <li>{@link org.jamplate.function}</li>
 *     <li>{@link org.jamplate.memory}</li>
 *     <li>{@link org.jamplate.model}</li>
 *     <li>{@link org.jamplate.unit}</li>
 * </ul>
 * <br>
 * Default implementations packages:
 * <ul>
 *     <li>{@link org.jamplate.impl}</li>
 * </ul>
 * <br>
 * Utility packages:
 * <ul>
 *     <li>{@link org.jamplate.util}</li>
 * </ul>
 * <br>
 * Glucose implementation packages:
 * <ul>
 *     <li>{@link org.jamplate.glucose}</li>
 * </ul>
 * <br><br>
 * <table style="width: 100%;">
 *     <tr>
 *         <th>Class</th>
 *         <th>Members</th>
 *     </tr>
 *     <tr><h2>Api</h2></tr>
 *     <tr>
 *         <td>{@link org.jamplate.unit.Unit}</td>
 *         <td><ul>
 *             <li>spec: {@link org.jamplate.unit.Spec}</li>
 *             <li>environment: {@link org.jamplate.model.Environment}</li>
 *         </ul></td>
 *     </tr>
 *     <tr>
 *         <td>{@link org.jamplate.unit.Spec}</td>
 *         <td><ul>
 *             <li>{@link org.jamplate.unit.Spec}[]</li>
 *             <li>qualifiedName: {@link java.lang.String}</li>
 *             <li>initializer: {@link org.jamplate.function.Initializer}</li>
 *             <li>parser: {@link org.jamplate.function.Parser}</li>
 *             <li>analyzer: {@link org.jamplate.function.Analyzer}</li>
 *             <li>compiler: {@link org.jamplate.function.Compiler}</li>
 *             <li>listener: {@link org.jamplate.function.Listener}</li>
 *             <li>parserProcessor: {@link org.jamplate.function.Processor}</li>
 *             <li>analyzeProcessor: {@link org.jamplate.function.Processor}</li>
 *             <li>compileProcessor: {@link org.jamplate.function.Processor}</li>
 *         </ul></td>
 *     </tr>
 *     <tr>
 *         <td>{@link org.jamplate.unit.Event}</td>
 *         <td><ul>
 *             <li>action: {@link java.lang.String}</li>
 *             <li>compilation?: {@link org.jamplate.model.Compilation}</li>
 *             <li>diagnostic?: {@link org.jamplate.diagnostic.Diagnostic}</li>
 *             <li>document?: {@link org.jamplate.model.Document}</li>
 *             <li>environment?: {@link org.jamplate.model.Environment}</li>
 *             <li>instruction?: {@link org.jamplate.model.Instruction}</li>
 *             <li>memory?: {@link org.jamplate.memory.Memory}</li>
 *             <li>tree?: {@link org.jamplate.model.Tree}</li>
 *             <li>unit?: {@link org.jamplate.unit.Unit}</li>
 *             <li>extra: {{@link java.lang.String}:{@link java.lang.Object}}</li>
 *         </ul></td>
 *     </tr>
 *     <tr><h2>Model</h2></tr>
 *     <tr>
 *         <td>{@link org.jamplate.model.Environment}</td>
 *         <td><ul>
 *             <li>{@link org.jamplate.model.Compilation}[]</li>
 *             <li>diagnostic: {@link org.jamplate.diagnostic.Diagnostic}</li>
 *         </ul></td>
 *     </tr>
 *     <tr>
 *         <td>{@link org.jamplate.model.Compilation}</td>
 *         <td><ul>
 *             <li>environment: {@link org.jamplate.model.Environment}</li>
 *             <li>rootTree: {@link org.jamplate.model.Tree}</li>
 *             <li>instruction?: {@link org.jamplate.model.Instruction}</li>
 *         </ul></td>
 *     </tr>
 *     <tr>
 *         <td>{@link org.jamplate.model.Instruction}</td>
 *         <td><ul>
 *             <li>{@link org.jamplate.model.Instruction}[]</li>
 *             <li>tree?: {@link org.jamplate.model.Tree}</li>
 *         </ul></td>
 *     </tr>
 *     <tr>
 *         <td>{@link org.jamplate.model.Tree}</td>
 *         <td><ul>
 *             <li>{@link org.jamplate.model.Tree}[]</li>
 *             <li>document: {@link org.jamplate.model.Document}</li>
 *             <li>reference: {@link org.jamplate.model.Reference}</li>
 *             <li>sketch: {@link org.jamplate.model.Sketch}</li>
 *             <li>weight: {@link java.lang.Integer}</li>
 *             <li>parent?: {@link org.jamplate.model.Tree}</li>
 *             <li>previous?: {@link org.jamplate.model.Tree}</li>
 *             <li>next?: {@link org.jamplate.model.Tree}</li>
 *             <li>child?: {@link org.jamplate.model.Tree}</li>
 *         </ul></td>
 *     </tr>
 *     <tr>
 *         <td>{@link org.jamplate.model.Sketch}</td>
 *         <td><ul>
 *             <li>name: {@link java.lang.String}</li>
 *             <li>kind: {@link java.lang.String}</li>
 *             <li>tree?: {@link org.jamplate.model.Tree}</li>
 *             <li>components: {{@link cufy.util.Node.Key}:{@link org.jamplate.model.Sketch}}</li>
 *         </ul></td>
 *     </tr>
 *     <tr><h2>Diagnostic</h2></tr>
 *     <tr>
 *         <td>{@link org.jamplate.diagnostic.Diagnostic}</td>
 *         <td><ul>
 *             <li>{@link org.jamplate.diagnostic.Message}[]</li>
 * 	       </ul></td>
 *     </tr>
 *     <tr>
 *         <td>{@link org.jamplate.diagnostic.Message}</td>
 *         <td><ul>
 *             <li>criticalPoints: {@link org.jamplate.model.Tree}[]</li>
 *             <li>errorKind: {@link java.lang.String}</li>
 *             <li>exception?: {@link java.lang.Throwable}</li>
 *             <li>messagePhrase: {@link java.lang.String}</li>
 *             <li>priority: {@link java.lang.String}</li>
 *             <li>stackTrace: {@link org.jamplate.model.Tree}[]</li>
 *             <li>fetal: {@link java.lang.Boolean}</li>
 *         </ul></td>
 *     </tr>
 *     <tr><h2>Memory</h2></tr>
 *     <tr>
 *         <td>{@link org.jamplate.memory.Memory}</td>
 *         <td><ul>
 *             <li>{@link org.jamplate.memory.Frame}[]</li>
 *             <li>frame: {@link org.jamplate.memory.Frame}</li>
 *             <li>console: {@link org.jamplate.memory.Console}</li>
 *         </ul></td>
 *     </tr>
 *     <tr>
 *         <td>{@link org.jamplate.memory.Frame}</td>
 *         <td><ul>
 *             <li>{@link org.jamplate.memory.Value}[]</li>
 *             <li>stack: {@link org.jamplate.memory.Value}[]</li>
 *             <li>heap: {{@link java.lang.String}:{@link org.jamplate.memory.Value}}</li>
 *             <li>instruction?: {@link org.jamplate.model.Instruction}</li>
 *         </ul></td>
 *     </tr>
 *     <tr>
 *         <td>{@link org.jamplate.memory.Value}</td>
 *         <td><ul>
 *             <li>pipe: {@link org.jamplate.memory.Pipe}</li>
 *         </ul></td>
 *     </tr>
 *     <tr><h2>Function</h2></tr>
 *     <tr>
 *         <td>{@link org.jamplate.function.Analyzer}</td>
 *         <td><ul>
 *             <li>{@link org.jamplate.function.Analyzer}[]</li>
 *         </ul></td>
 *     </tr>
 *     <tr>
 *         <td>{@link org.jamplate.function.Compiler}</td>
 *         <td><ul>
 *             <li>{@link org.jamplate.function.Compiler}[]</li>
 *         </ul></td>
 *     </tr>
 *     <tr>
 *         <td>{@link org.jamplate.function.Initializer}</td>
 *         <td><ul>
 *             <li>{@link org.jamplate.function.Initializer}[]</li>
 *         </ul></td>
 *     </tr>
 *     <tr>
 *         <td>{@link org.jamplate.function.Listener}</td>
 *         <td><ul>
 *             <li>{@link org.jamplate.function.Listener}[]</li>
 *         </ul></td>
 *     </tr>
 *     <tr>
 *         <td>{@link org.jamplate.function.Parser}</td>
 *         <td><ul>
 *             <li>{@link org.jamplate.function.Parser}[]</li>
 *         </ul></td>
 *     </tr>
 *     <tr>
 *         <td>{@link org.jamplate.function.Processor}</td>
 *         <td><ul>
 *             <li>{@link org.jamplate.function.Processor}[]</li>
 *         </ul></td>
 *     </tr>
 * </table>
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.0.1 ~2020.09.20
 */
package org.jamplate;