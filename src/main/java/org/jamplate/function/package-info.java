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
 * All about processors. Processors are functions that processes objects from the {@link
 * org.jamplate.model} package.
 * <br>
 * Functions:
 * <ul>
 *     <li>
 *         {@link org.jamplate.function.Parser Parser}
 *         <br>
 *         A function tries to constructs a set of {@link org.jamplate.model.Tree Trees}
 *         from a {@link org.jamplate.model.Tree Tree}.
 *     </li>
 *     <li>
 *         {@link org.jamplate.function.Analyzer Analyzer}
 *         <br>
 *         A function that analyzes and modifies a {@link org.jamplate.model.Tree Tree}.
 *     </li>
 *     <li>
 *         {@link org.jamplate.function.Compiler Compiler}
 *         <br>
 *         A function tires to construct an {@link org.jamplate.model.Instruction
 *         Instruction} from a {@link org.jamplate.model.Tree Tree}.
 *     </li>
 *     <li>
 *         {@link org.jamplate.function.Processor Processor}
 *         <br>
 *         An function that processes a {@link org.jamplate.model.Compilation
 *         Compilation}.
 *     </li>
 * </ul>
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.26
 */
package org.jamplate.function;