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
 * All about the interface {@link org.jamplate_x.node.Node}.
 * <br><br>
 * Instance Getters:
 * <table width="100%" style="margin: 20">
 *     <tr>
 *         <th>symbol</th>
 *         <th>method</th>
 *     </tr>
 *     <tr>
 *         <td>node.above</td>
 *         <td>{@link org.jamplate_x.node.Node#getAbove()}</td>
 *     </tr>
 *     <tr>
 *         <td>node.previous</td>
 *         <td>{@link org.jamplate_x.node.Node#getPrevious()}</td>
 *     </tr>
 *     <tr>
 *         <td>node.next</td>
 *         <td>{@link org.jamplate_x.node.Node#getNext()}</td>
 *     </tr>
 *     <tr>
 *         <td>node.below</td>
 *         <td>{@link org.jamplate_x.node.Node#getBelow()}</td>
 *     </tr>
 * </table>
 * <br><br>
 * Static Getters:
 * <table width="100%" style="margin: 20">
 *     <tr>
 *         <th>symbol</th>
 *         <th>method</th>
 *     </tr>
 *     <tr>
 *         <td>node:root</td>
 *         <td>{@link org.jamplate_x.node.Node#getRoot(Node)}</td>
 *     </tr>
 *     <tr>
 *         <td>node:parent</td>
 *         <td>{@link org.jamplate_x.node.Node#getParent(Node)}</td>
 *     </tr>
 *     <tr>
 *         <td>node:head</td>
 *         <td>{@link org.jamplate_x.node.Node#getHead(Node)}</td>
 *     </tr>
 *     <tr>
 *         <td>node:tail</td>
 *         <td>{@link org.jamplate_x.node.Node#getTail(Node)}</td>
 *     </tr>
 * </table>
 * <br><br>
 * Single Setters:
 * <table width="100%" style="margin: 20">
 *     <tr>
 *         <th>symbol</th>
 *         <th>method</th>
 *     </tr>
 *     <tr>
 *         <td>st above {@code node} to {@code other}</td>
 *         <td>node.above = other</td>
 *     </tr>
 *     <tr>
 *         <td>st previous {@code node} to {@code other}</td>
 *         <td>node.previous = other</td>
 *     </tr>
 *     <tr>
 *         <td>st next {@code node} to {@code other}</td>
 *         <td>node.next = other</td>
 *     </tr>
 *     <tr>
 *         <td>st below {@code node} to {@code other}</td>
 *         <td>node.below = other</td>
 *     </tr>
 * </table>
 * <br><br>
 * Double Setters:
 * <table width="100%" style="margin: 20">
 *     <tr>
 *         <th>symbol</th>
 *         <th>method</th>
 *     </tr>
 *     <tr>
 *         <td>set above {@code node} to {@code other}</td>
 *         <td>{@link org.jamplate_x.node.Node#setAbove(org.jamplate_x.node.Node)}</td>
 *     </tr>
 *     <tr>
 *         <td>set previous {@code node} to {@code other}</td>
 *         <td>{@link org.jamplate_x.node.Node#setPrevious(org.jamplate_x.node.Node)}</td>
 *     </tr>
 *     <tr>
 *         <td>set next {@code node} to {@code other}</td>
 *         <td>{@link org.jamplate_x.node.Node#setNext(org.jamplate_x.node.Node)}</td>
 *     </tr>
 *     <tr>
 *         <td>set previous {@code node} to {@code other}</td>
 *         <td>{@link org.jamplate_x.node.Node#setPrevious(org.jamplate_x.node.Node)}</td>
 *     </tr>
 * </table>
 * <br><br>
 * Single Remover:
 * <table width="100%" style="margin: 20">
 *     <tr>
 *         <th>symbol</th>
 *         <th>method</th>
 *     </tr>
 *     <tr>
 *         <td>rm above {@code node}</td>
 *         <td>node.above = null</td>
 *     </tr>
 *     <tr>
 *         <td>rm previous {@code node}</td>
 *         <td>node.previous = null</td>
 *     </tr>
 *     <tr>
 *         <td>rm next {@code node}</td>
 *         <td>node.next = null</td>
 *     </tr>
 *     <tr>
 *         <td>rm below {@code node}</td>
 *         <td>node.below = null</td>
 *     </tr>
 * </table>
 * <br><br>
 * Double Remover:
 * <table width="100%" style="margin: 20">
 *     <tr>
 *         <th>symbol</th>
 *         <th>method</th>
 *     </tr>
 *     <tr>
 *         <td>remove above {@code node}</td>
 *         <td>{@link org.jamplate_x.node.Node#removeAbove()}</td>
 *     </tr>
 *     <tr>
 *         <td>remove previous {@code node}</td>
 *         <td>{@link org.jamplate_x.node.Node#removePrevious()}</td>
 *     </tr>
 *     <tr>
 *         <td>remove next {@code node}</td>
 *         <td>{@link org.jamplate_x.node.Node#removeNext()}</td>
 *     </tr>
 *     <tr>
 *         <td>remove below {@code node}</td>
 *         <td>{@link org.jamplate_x.node.Node#removeBelow()}</td>
 *     </tr>
 * </table>
 * <br><br>
 * Custom Methods:
 * <table width="100%" style="margin: 20">
 *     <tr>
 *         <th>symbol</th>
 *         <th>method</th>
 *     </tr>
 *     <tr>
 *         <td>remove {@code node}</td>
 *         <td>{@link org.jamplate_x.node.Node#remove(org.jamplate_x.node.Node)}</td>
 *     </tr>
 *     <tr>
 *         <td>set {@code node} with {@code other}</td>
 *         <td>{@link org.jamplate_x.node.Node#set(Node, Node)}</td>
 *     </tr>
 * </table>
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.02.14
 */
package org.jamplate_x.node;
// * <table width="100%" style="margin: 20">
//		*     <tr>
// *         <th>Relation</th>
//		*         <th>Opposite</th>
//		*         <th>Getter</th>
//		*         <th>Description</th>
//		*     </tr>
//		*     <tr>
// *         <td>Above</td>
//		*         <td>Below</td>
//		*         <td>{@link org.jamplate.model.node.Node#getAbove()}</td>
//		*         <td>The node on top of the node.</td>
//		*     </tr>
//		*     <tr>
// *         <td>Below</td>
//		*         <td>Above</td>
//		*         <td>{@link org.jamplate.model.node.Node#getBelow()}</td>
//		*         <td>The first node below the node.</td>
//		*     </tr>
//		*     <tr>
// *         <td>Previous</td>
//		*         <td>Next</td>
//		*         <td>{@link org.jamplate.model.node.Node#getPrevious()}</td>
//		*         <td>The node before the node.</td>
//		*     </tr>
//		*     <tr>
// *         <td>Next</td>
//		*         <td>Previous</td>
//		*         <td>{@link org.jamplate.model.node.Node#getNext()}</td>
//		*         <td>The node after the node.</td>
//		*     </tr>
//		*     <tr>
// *         <td>Head</td>
//		*         <td></td>
//		*         <td>{@link org.jamplate.model.node.Node#getHead(Node)}</td>
//		*         <td>The most previous node to the node.</td>
//		*     </tr>
//		*     <tr>
// *         <td>Tail</td>
//		*         <td></td>
//		*         <td>{@link org.jamplate.model.node.Node#getTail(Node)}</td>
//		*         <td>The most next node to the node.</td>
//		*     </tr>
//		*     <tr>
// *         <td>Parent</td>
//		*         <td></td>
//		*         <td>{@link org.jamplate.model.node.Node#getParent(Node)}</td>
//		*         <td>The node above the most previous node of the node.</td>
//		*     </tr>
//		*     <tr>
// *         <td>Root</td>
//		*         <td></td>
//		*         <td>{@link org.jamplate.model.node.Node#getRoot(Node)}</td>
//		*         <td>The most previous/above node of the node.</td>
//		*     </tr>
//		* </table>
