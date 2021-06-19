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

import org.jamplate.impl.analyzer.ConditionTransformAnalyzer;
import org.jamplate.model.Tree;
import org.jamplate.function.Analyzer;
import org.jetbrains.annotations.NotNull;

/**
 * Jamplate analyzers.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.28
 */
@Deprecated
public final class Analyzers {
	//DC

	/**
	 * An analyzer for suppressing line separators.
	 *
	 * @since 0.2.0 ~2021.05.29
	 */
	@SuppressWarnings("OverlyLongLambda")
	@NotNull
	public static final Analyzer DC_EOL_SUPPRESSED =
			new ConditionTransformAnalyzer(
					Kind.DC_EOL, Kind.DC_EOL_SUPPRESSED,
					(compilation, tree) -> {
						Tree previous = tree.getPrevious();
						Tree next = tree.getNext();

						if (previous != null)
							switch (previous.getSketch().getKind()) {
								case Kind.CX_CMD:
									return true;
							}

						if (next != null)
							switch (next.getSketch().getKind()) {
								case Kind.CX_CMD:
									return true;
							}

						return false;
					}
			);

	/**
	 * Utility classes must not be initialized.
	 *
	 * @throws AssertionError when called.
	 * @since 0.2.0 ~2021.05.28
	 */
	private Analyzers() {
		throw new AssertionError("No instance for you");
	}
}
