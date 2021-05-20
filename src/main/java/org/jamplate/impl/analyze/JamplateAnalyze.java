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
package org.jamplate.impl.analyze;

import org.jamplate.compile.Processor;
import org.jamplate.util.compile.SequentialProcessor;
import org.jetbrains.annotations.NotNull;

/**
 * The gate class to the package {@link org.jamplate.impl.analyze} containing its main
 * ready-to-use components.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.20
 */
public final class JamplateAnalyze {
	/**
	 * A processor that analyzes the default jamplate components searching for known
	 * jamplate specs.
	 *
	 * @since 0.2.0 ~2021.05.20
	 */
	@NotNull
	public static final Processor PROCESSOR =
			new SequentialProcessor(
					CommandAnalyzer.INCLUDE
			);

	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.05.20
	 */
	private JamplateAnalyze() {
		throw new AssertionError("No instance for you");
	}
}
