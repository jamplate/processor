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
package org.jamplate;

/**
 * A visitor is a general purpose callback object that get passed as an argument to a {@link Node#visit(Visitor)}.
 *
 * @param <N> the type of nodes this visitor can visit.
 * @author LSafer
 * @version 0.0.2
 * @since 0.0.2 ~2021.01.06
 */
public interface Visitor<N extends Node> {
	/**
	 * Get called when the {@link Node#visit(Visitor)} method of a node got some node for this visitor to visit.
	 *
	 * @param node the node that this visitor should accept.
	 * @throws NullPointerException if the given {@code node} is null.
	 * @since 0.0.2 ~2021.01.6
	 */
	void visit(N node);
}
