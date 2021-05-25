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
 * A set of instructions.
 * <br><br>
 * Doc Methods:
 * <ul>
 *     <li>
 *         {@code CONSOLE( X : Text | Value ) : Void}
 *         <p>
 *             change the console to X
 *     </li>
 *     <li>
 *         {@code PRINT( X : Text | Vale ) : Void}
 *         <p>
 *             prints X to console
 *     </li>
 *     <li>
 *         {@code REPRNT( X : Text | Vale ) : Void}
 *         <p>
 *             prints X to console;
 *             replace allocated by REPLLOC
 *     </li>
 *     <li>
 *         {@code PUSH( X : Text | Value ) : Void}
 *         <p>
 *             pushes X to stack.
 *     </li>
 *     <li>
 *         {@code ALLOC( X : Address , Y : Text | Value ) : Void}
 *         <p>
 *             allocate Y into heap at X
 *     </li>
 *     <li>
 *         {@code REPLLOC( X : Address , Y : Text | Value ) : Void}
 *         <p>
 *             allocate Y into heap at X;
 *             replace X to Y in all REPRINTS
 *     </li>
 *     <li>
 *         {@code DEF( X : Address ) : Text}
 *         <p>
 *             return the {@code true} if the address is defined, {@code false} otherwise.
 *     </li>
 *     <li>
 *         {@code EVAL( X : Address ) : Value}
 *         <p>
 *             return evaluation of X;
 *             return name of X if failed
 *     </li>
 *     <li>
 *         {@code EXEC( X : Instruction ) : Value}
 *         <p>
 *             return result of executing X;
 *             push frame before execution;
 *             pop after execution and getting results;
 *     </li>
 *     <li>
 *         {@code BRANCH( X : Text | Value , Y : Instruction , Z : Instruction ) : Void}
 *         <p>
 *             if X evaluated to true execute Y otherwise execute Z;
 *             push frame before execution;
 *             pop frame after execution
 *     </li>
 *     <li>
 *         {@code IMPORT( X : Text | Value ) : Instruction}
 *         <p>
 *             return imported instruction X;
 *             push frame before execution;
 *             pop frame after execution
 *     </li>
 *     <li>
 *         {@code IEXEC( X : Instruction[] ) : Void}
 *         <p>
 *             Execute the instructions in X;
 *             Push frame before each exeuction;
 *             Pop frame after each execution
 *     </li>
 *     <li>
 *         {@code IPED( X : Instruction[] ) : Void}
 *         <p>
 *             Execute the instructions in X;
 *             Push frame before each execution;
 *             Dump frame after each execution
 *         <p>
 *             IPED stands for Iterate-Push-Execute-Dump
 *     </li>
 * </ul>
 * <br><br>
 * Doc Variables:
 * <ul>
 *     <li>
 *         {@code CONST : Text}
 *         <p>
 *             provided text.
 *     </li>
 *     <li>
 *         {@code INSTR : Instruction}
 *         <p>
 *             provided instruction.
 *     </li>
 *     <li>
 *         {@code ADDR : Address}
 *         <p>
 *             provided address.
 *     </li>
 * </ul>
 * <br><br>
 * Doc Variable Prefixes/Postfixes:
 * <ul>
 *     <li>
 *         {@code 0-5}
 *         <p>
 *             a postfix number to distinguish variables from another when dealing with multiple
 *             variables with common type.
 *     </li>
 *     <li>
 *         {@code X}
 *         <p>
 *             a prefix to tell that the variable is an array of its type.
 *     </li>
 * </ul>
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.25
 */
package org.jamplate.impl.instruction;