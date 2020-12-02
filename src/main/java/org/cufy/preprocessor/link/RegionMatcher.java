package org.cufy.preprocessor.link;

import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;

/**
 *
 */
public class RegionMatcher implements MatchResult {
	/**
	 * The source matcher.
	 *
	 * @since 0.0.b ~2020.10.18
	 */
	protected Matcher matcher;

	public RegionMatcher(Matcher matcher, IntBiPredicate predicate) {
		Objects.requireNonNull(matcher, "matcher");
		Objects.requireNonNull(predicate, "predicate");
		this.matcher = matcher;
	}

	@Override
	public int end() {
		return 0;
	}

	@Override
	public int end(int group) {
		return 0;
	}

	@Override
	public String group() {
		return null;
	}

	@Override
	public String group(int group) {
		return null;
	}

	@Override
	public int groupCount() {
		return 0;
	}

	@Override
	public int start(int group) {
		return 0;
	}

	@Override
	public int start() {
		return 0;
	}

	@FunctionalInterface
	public interface IntBiPredicate {
		boolean test(int a, int b);
	}
}
