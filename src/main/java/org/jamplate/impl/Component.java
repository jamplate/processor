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
package org.jamplate.impl;

import cufy.util.Node;
import cufy.util.Nodes;
import org.jetbrains.annotations.NotNull;

/**
 * A class containing the keys of the default components of sketch.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.25
 */
public final class Component {
	/**
	 * The key of the closing anchor.
	 *
	 * @since 0.2.0 ~2021.05.25
	 */
	@NotNull
	public static final Node.Key CLOSE = Nodes.key("Close", "");
	/**
	 * The key of the "key" component.
	 *
	 * @since 0.2.0 ~2021.05.25
	 */
	@NotNull
	public static final Node.Key KEY = Nodes.key("Key", "");
	/**
	 * The key of the opening anchor component.
	 *
	 * @since 0.2.0 ~2021.05.25
	 */
	@NotNull
	public static final Node.Key OPEN = Nodes.key("Open", "");
	/**
	 * The key of the parameter component.
	 *
	 * @since 0.2.0 ~2021.05.25
	 */
	@NotNull
	public static final Node.Key PARAMETER = Nodes.key("Parameter", "");
	/**
	 * The key of the type component.
	 *
	 * @since 0.2.0 ~2021.05.25
	 */
	@NotNull
	public static final Node.Key TYPE = Nodes.key("Type", "");
	/**
	 * The key of the value component.
	 *
	 * @since 0.2.0 ~2021.05.25
	 */
	@NotNull
	public static final Node.Key VALUE = Nodes.key("Value", "");

	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.05.25
	 */
	private Component() {
		throw new AssertionError("No instance for you");
	}
}
