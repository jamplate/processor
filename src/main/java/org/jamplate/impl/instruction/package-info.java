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
 * A package containing implementations of the interface {@link
 * org.jamplate.model.Instruction}.
 * <br><br>
 * Doc Methods:
 * <ul>
 *     <li>
 *         {@code CONSOLE( X : Text | Value ) : Void}
 *         <p>
 *             change the console to X
 *     </li>
 *     <li>
 *         {@code SOUT( X : Text | Value ) : Void}
 *         <p>
 *             print X to the system console.
 *     </li>
 *     <li>
 *         {@code SERR( X : Text | Value ) : Void}
 *         <p>
 *             print X to the system error console.
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
 *         {@code PUT( X : Address , Y : Value | Text , Z : Value : Text )}
 *         <p>
 *             Put Z to Y in the object stored at X in the heap;
 *             Allocate a new object if not allocated
 *     </li>
 *     <li>
 *         {@code FALLOC( X : Address , Y : Text | Value ) : Void}
 *         <p>
 *             allocate Y into heap of the last frame at X
 *     </li>
 *     <li>
 *         {@code SPREAD( X : Value | Text ) : Void}
 *         <p>
 *             Assuming X is a json map, Spread the mappings to the heap.
 *     </li>
 *     <li>
 *         {@code REPLLOC( X : Address , Y : Text | Value ) : Void}
 *         <p>
 *             allocate Y into heap at X;
 *             replace X to Y in all REPRINTS
 *     </li>
 *     <li>
 *         {@code CPED( X : Address , Y : Instruction[] ) : Void}
 *         <p>
 *             Allocate captured console when executing Y into the heap at X;
 *             Push frame before each execution;
 *             Dump frame after each execution
 *     </li>
 *     <li>
 *         {@code FREE( X : Address ) : Void}
 *         <p>
 *             delete the value at X in the heap
 *     </li>
 *     <li>
 *         {@code REPREE( X : Address ) : Void}
 *         <p>
 *             delete the value at X in the heap;
 *             revoke any replacing order for X
 *     </li>
 *     <li>
 *         {@code DEF( X : Address ) : Text}
 *         <p>
 *             return {@code true} if the address is defined, {@code false} otherwise.
 *     </li>
 *     <li>
 *         {@code NDEF( X : Address ) : Text}
 *         <p>
 *             return {@code false} if the address is defined, {@code true} otherwise.
 *     </li>
 *     <li>
 *         {@code EVAL( X : Address ) : Value}
 *         <p>
 *             return evaluation of X;
 *             return name of X if failed
 *     </li>
 *     <li>
 *         {@code GET( X : Address , Y : Value | Text ) : Value}
 *         <p>
 *             return the value at Y in the result of the evaluation of X.
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
 *         {@code ITER( X : Instruction[] ) : Void}
 *         <p>
 *             Execute the instructions in X
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
 *     <li>
 *         {@code FPED( X : ADDR , Y : Text | Value , Z : Instruction[] ) : Void }
 *         <p>
 *             Executes the instructions in Z foreach item in Y;
 *             Push frame on each iteration;
 *             Allocate the item of the iteration to X before each iteration;
 *             Push frame before each execution;
 *             Dump frame after each execution;
 *             Pop frame after each iteration
 *         <p>
 *             FPED stands for Foreach-Push-Execute-Dump
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