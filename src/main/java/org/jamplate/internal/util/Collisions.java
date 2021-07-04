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
package org.jamplate.internal.util;

import org.jamplate.model.Dominance;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiPredicate;

/**
 * Collisions predicates.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.04
 */
public final class Collisions {
	/**
	 * Tool classes does not need instantiating.
	 *
	 * @throws AssertionError when called.
	 * @since 0.3.0 ~2021.07.04
	 */
	private Collisions() {
		throw new AssertionError("No instance for you!");
	}

	/**
	 * Return a collision predicate that does not allow any intersection between the
	 * trees.
	 *
	 * @return a predicate as described above.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(pure = true)
	public static BiPredicate<Tree, Tree> flat() {
		return (a, b) -> {
			switch (Dominance.compute(a, b)) {
				case NONE:
					//no intersection
					return true;
				case CONTAIN:
					//if `b` can contain `a`
				case PART:
					//if `a` can contain `b`
				case SHARE:
					//if `a` clash with `b`
					return false;
				case EXACT:
					//if `a` has same bounds as `b`
					return false;
				default:
					//unexpected
					throw new InternalError();
			}
		};
	}

	/**
	 * Return a collision predicate that does allow merging but with no clashes.
	 *
	 * @return a predicate as described above.
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	@Contract(pure = true)
	public static BiPredicate<Tree, Tree> nested() {
		return (a, b) -> {
			switch (Dominance.compute(a, b)) {
				case CONTAIN:
					//if `a` fits in `b`
					for (Tree bc : b)
						//check if `a` clash with a child in `b`
						if (!Collisions.nested().test(a, bc))
							//if `a` clash with a child in `b
							return false;

					//if `a` fits perfectly in `b`
					return true;
				case PART:
					//if `a` can contain `b`
					for (Tree ac : a)
						//check if `b` clash with a child in `a`
						if (!Collisions.nested().test(ac, b))
							//if `b` clash with a child in `a`
							return false;

					//if `b` fits perfectly in `a`
					return true;
				case NONE:
					//if the two trees do not intersect
					return true;
				case SHARE:
					//if `b` clash with `a`
					return false;
				case EXACT:
					//if `a` has same bounds of `b`
					return a.getWeight() != b.getWeight();
				default:
					//unexpected
					throw new InternalError();
			}
		};
	}
}
