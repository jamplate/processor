/*
 *	Copyright 2020 Cufy
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
package org.jamplate.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Common utilities for {@link String}s.
 *
 * @author LSafer
 * @version 0.0.b
 * @since 0.0.b ~2020.10.04
 */
public final class Strings {
	/**
	 * Target escaping statements.
	 * <ul>
	 *     <li>{@code STATEMENT} the whole escaping statement.</li>
	 *     <li>{@code ESCAPED} the escaped part.</li>
	 * </ul>
	 *
	 * @since 0.0.b ~2020.10.01
	 */
	public static final Pattern DESCAPE = Pattern.compile("(?<STATEMENT>\\\\(?<ESCAPED>[tbnrf'\"\\\\]))");
	/**
	 * Target descaped statements.
	 * <ul>
	 *     <li>{@code STATEMENT} the whole descaped statement.</li>
	 *     <li>{@code DESCAPED} the descaped part.</li>
	 * </ul>
	 *
	 * @since 0.0.b ~2020.10.02
	 */
	public static final Pattern ESCAPE = Pattern.compile("(?<STATEMENT>(?<DESCAPED>[\t\b\n\r\f\"\\\\]))");

	/**
	 * De-escape the escaped fragments in the given {@code string}.
	 *
	 * @param string the string to be de-escaped.
	 * @return a de-escaped string from the given {@code string}.
	 * @throws NullPointerException if the given {@code string} is null.
	 * @since 0.0.b ~2020.10.01
	 */
	public static String descaped(String string) {
		Objects.requireNonNull(string, "string");
		Matcher matcher = Strings.DESCAPE.matcher(string);

		if (matcher.find()) {
			StringBuffer buffer = new StringBuffer();

			do {
				String statement = matcher.group("STATEMENT");
				String escaped = matcher.group("ESCAPED");

				String replacement;
				switch (escaped) {
					case "t":
						replacement = "\t";
						break;
					case "b":
						replacement = "\b";
						break;
					case "n":
						replacement = "\n";
						break;
					case "r":
						replacement = "\r";
						break;
					case "f":
						replacement = "\f";
						break;
					case "'":
						replacement = "'";
						break;
					case "\"":
						replacement = "\"";
						break;
					case "\\":
						replacement = "\\\\";
						break;
					default:
						replacement = statement;
						break;
				}

				matcher.appendReplacement(buffer, replacement);
			} while (matcher.find());

			return buffer.toString();
		}

		return string;
	}

	/**
	 * Escape the descaped fragments in the given {@code string}.
	 *
	 * @param string the string to be escaped.
	 * @return an escaped string from the given {@code string}.
	 * @throws NullPointerException if the given {@code string} is null.
	 * @since 0.0.b ~2020.10.01
	 */
	public static String escaped(String string) {
		Objects.requireNonNull(string, "string");
		Matcher matcher = Strings.ESCAPE.matcher(string);

		if (matcher.find()) {
			StringBuffer buffer = new StringBuffer();

			do {
				String descaped = matcher.group("DESCAPED");
				String statement = matcher.group("STATEMENT");

				String replacement;
				switch (descaped) {
					case "\t":
						replacement = "\\\\t";
						break;
					case "\b":
						replacement = "\\\\b";
						break;
					case "\n":
						replacement = "\\\\n";
						break;
					case "\r":
						replacement = "\\\\r";
						break;
					case "\f":
						replacement = "\\\\f";
						break;
					case "'":
						replacement = "\\\\'";
						break;
					case "\"":
						replacement = "\\\\\"";
						break;
					case "\\":
						replacement = "\\\\\\\\";
						break;
					default:
						replacement = statement;
						break;
				}

				matcher.appendReplacement(buffer, replacement);
			} while (matcher.find());

			return buffer.toString();
		}

		return string;
	}

	public static List<int[]> group(String string, List<List<Pattern>> escapables, List<Pattern> patterns) {
		Objects.requireNonNull(string, "string");
		Objects.requireNonNull(escapables, "escapables");
		Objects.requireNonNull(patterns, "patterns");

		switch (patterns.size()) {
			case 1:
				Pattern p = patterns.get(0);

				if (p != null)
					return Strings.group(string, escapables, p);
				break;
			case 2:
				Pattern o = patterns.get(0);
				Pattern c = patterns.get(1);

				if (o != null && c != null)
					return Strings.group(string, escapables, o, c);
				break;
		}

		return new ArrayList<>();
	}

	/**
	 * Split the given {@code string} depending on the given {@code splitter}. Any substring within
	 * any area in the result of invoking {@link #ignore(String, List)} with the given {@code
	 * string} and {@code escapables} will not be matched.
	 *
	 * @param string     the string to be splatted.
	 * @param escapables the escapables to ignore.
	 * @param splitter   the pattern to split the given {@code string} depending on.
	 * @return an array of two elements arrays. Each first element in a subarray is the index of the
	 * 		start of the slice in the given string. Each second index in a subarray is the index of the
	 * 		end of the slice in the given string.
	 * @throws NullPointerException if the given {@code string} or {@code escapables} or {@code
	 *                              splitter} is null.
	 * @see #ignore(String, List)
	 * @see #find(Matcher, List)
	 * @since 0.0.b ~2020.10.12
	 */
	public static int[][] split(String string, List<List<Pattern>> escapables, Pattern splitter) {
		Objects.requireNonNull(string, "string");
		Objects.requireNonNull(escapables, "escapables");
		Objects.requireNonNull(splitter, "splitter");

		//prepare
		List<int[]> ignore = Strings.ignore(string, escapables);
		List<int[]> list = new ArrayList();

		Matcher spm = splitter.matcher(string);
		int i = 0;

		//process
		while (Strings.find(spm, ignore)) {
			list.add(new int[]{i, spm.start()});

			i = spm.end();
		}

		list.add(new int[]{i, string.length()});

		return list.toArray(new int[0][]);
	}

	//------------------------------------------------------------------------------------------------------------------------------------------------

	@SuppressWarnings("ALL")
	private static boolean find(Matcher matcher, List<int[]> ignore) {
		Objects.requireNonNull(matcher, "matcher");
		Objects.requireNonNull(ignore, "ignore");

		while0:
		while (matcher.find()) {
			int i = matcher.start();
			int j = matcher.end();

			//check if any intercept
			for (int[] a : ignore) {
				int m = a[0];
				int n = a[1];
				if (
						(i >= m && i < n) || //i in [m, n)
						(m >= i && m < j) || //m in [i, j)
						(j > m && j <= n) || //j in (m, n]
						(n > i && n <= j)    //n in (i, j]
				)
					continue while0;
			}

			return true;
		}

		return false;
	}

	private static List<int[]> group(String string, List<List<Pattern>> escapables, Pattern group) {
		Objects.requireNonNull(string, "string");
		Objects.requireNonNull(escapables, "escapables");
		Objects.requireNonNull(group, "group");

		List<int[]> ignore = Strings.ignore(string, escapables);
		List<int[]> list = new ArrayList();

		Matcher grm = group.matcher(string);
		while (true) {
			if (Strings.find(grm, ignore)) {
				list.add(new int[]{
						grm.start(),
						grm.end()
				});

				continue;
			}

			break;
		}

		return list;
	}

	private static List<int[]> group(String string, List<List<Pattern>> escapable, Pattern open, Pattern close) {
		Objects.requireNonNull(string, "string");
		Objects.requireNonNull(escapable, "escapable");
		Objects.requireNonNull(open, "open");
		Objects.requireNonNull(close, "close");

		List<int[]> ignore = Strings.ignore(string, escapable);
		List<int[]> list = new ArrayList<>();

		Matcher opm = open.matcher(string);
		Matcher clm = close.matcher(string);
		while0:
		while (true) {
			//process
			if (Strings.find(opm, ignore)) {
				//collect
				int start = opm.start();

				//process
				while (Strings.find(clm, ignore)) {
					if (Strings.find(opm, ignore))
						if (opm.end() < clm.start())
							continue;

					//collect
					list.add(new int[]{
							start,
							clm.end()
					});
					continue while0;
				}
			}

			break;
		}

		return list;
	}

	@SuppressWarnings("ALL")
	private static List<int[]> ignore(String string, List<List<Pattern>> patterns) {
		Objects.requireNonNull(string, "string");
		Objects.requireNonNull(patterns, "patterns");
		return patterns.stream()
				.map(pattern ->
						Strings.group(
								string,
								patterns.stream()
										.filter(p -> p == pattern)
										.collect(Collectors.toList()),
								pattern
						)
				)
				.reduce((list, other) -> {
					list.addAll(other);
					return list;
				})
				.orElseGet(ArrayList::new);
	}
}
//		List<int[]> ignore = Strings.ignore(string, patterns);
//		List<int[]> list = new ArrayList();
//
//		if (pattern.length == 1) {
//			//----------------------------------------------------------------------
//			Pattern group = pattern[0];
//
//			if (group != null) {
//				Matcher grm = group.matcher(string);
//
//				while (true) {
//					if (Strings.find(grm, ignore)) {
//						list.add(new int[]{
//								grm.start(),
//								grm.end()
//						});
//
//						continue;
//					}
//
//					break;
//				}
//			}
//			//----------------------------------------------------------------------
//		} else if (pattern.length == 2) {
//			//----------------------------------------------------------------------
//			Pattern open = pattern[0];
//			Pattern close = pattern[1];
//
//			if (open != null && close != null) {
//				Matcher opm = open.matcher(string);
//				Matcher clm = close.matcher(string);
//
//				while0:
//				while (true) {
//					//process
//					if (Strings.find(opm, ignore)) {
//						//collect
//						int start = opm.start();
//
//						//process
//						while (Strings.find(clm, ignore)) {
//							if (Strings.find(opm, ignore))
//								if (opm.end() < clm.start())
//									continue;
//
//							//collect
//							list.add(new int[]{
//									start,
//									clm.end()
//							});
//							continue while0;
//						}
//					}
//
//					break;
//				}
//			}
//			//----------------------------------------------------------------------
//		}

//switch
//		while (true) {
//			int i = -1;
//			int j = -1;
//
//			if (pattern.length == 1) {
//				//prepare
//				Pattern p = pattern[0];
//
//				if (p != null) {
//					//prepare
//					Matcher grm = p.matcher(string);
//
//					//process
//					if (Strings.find(grm, ignore)) {
//						i = grm.start();
//						j = grm.end();
//					}
//				}
//			} else if (pattern.length == 2) {
//				//prepare
//				Pattern o = pattern[0];
//				Pattern c = pattern[1];
//
//				if (o != null && c != null) {
//					//prepare
//					Matcher opm = o.matcher(string);
//					Matcher clm = c.matcher(string);
//
//					//process
//					if (Strings.find(opm, ignore)) {
//						//collect
//						i = opm.start();
//
//						//process
//						while (Strings.find(clm, ignore)) {
//							if (Strings.find(opm, ignore))
//								if (opm.end() < clm.start())
//									continue;
//
//							//collect
//							j = clm.end();
//							break;
//						}
//					}
//				}
//			}
//
//			if (i != -1 && j != -1) {
//				int[] array = {i, j};
//				list.add(array);
//				ignore.add(array);
//				continue;
//			}
//
//			break;
//		}

//collect
//		return list;

//	/**
//	 * Group the substring between the first string matching the given {@code open} and the closing
//	 * string matching the given {@code close}. Any substring within any area in the result of
//	 * invoking {@link #ignore(String, Pattern[][])} with the given {@code string} and {@code
//	 * patterns} will not be matched.
//	 *
//	 * @param string   the string to be grouped.
//	 * @param patterns the patterns to ignore.
//	 * @param open     the pattern of the substring opening the group.
//	 * @param close    the pattern of the substring closing the group.
//	 * @return an array of two elements. The first element is the index of the start of the group in
//	 * 		the given {@code string} and the second element is the index of the end of the group.
//	 * @throws NullPointerException if the given {@code string} or {@code patterns} or {@code open}
//	 *                              or {@code close} is null.
//	 * @see #ignore(String, Pattern[][])
//	 * @see #find(Matcher, int[][])
//	 * @since 0.0.b ~2020.10.12
//	 */
//	public static int[] group(String string, Pattern[][] patterns, Pattern open, Pattern close) {
//		Objects.requireNonNull(string, "string");
//		Objects.requireNonNull(patterns, "patterns");
//		Objects.requireNonNull(open, "open");
//		Objects.requireNonNull(close, "close");
//
//		//prepare
//		int[][] ignore = Strings.ignore(string, patterns);
//		Matcher opm = open.matcher(string);
//		Matcher clm = close.matcher(string);
//		int i = -1;
//		int j = -1;
//
//		//process
//		if (Strings.find(opm, ignore)) {
//			i = opm.start();
//
//			while (Strings.find(clm, ignore)) {
//				if (Strings.find(opm, ignore))
//					if (opm.end() < clm.start())
//						continue;
//
//				j = clm.end();
//				break;
//			}
//		}
//
//		//collect
//		return new int[]{i, j};
//		//		int i = indexOf(string, indicies, open, 0);
//		//		int j = i;
//		//		int k = i;
//		//		while (!(j < k || k == -1)) {
//		//			k = indexOf(string, indicies, open, k + 1);
//		//			j = indexOf(string, indicies, close, j + 1);
//		//		}
//		//		return new int[]{
//		//				i,
//		//				j + 1
//		//		};
//		//		int start = i;
//		//		int end = j + 1;
//		//		return new MatchResult() {
//		//			@Override
//		//			public int end(int group) {
//		//				if (group != 0)
//		//					throw new IndexOutOfBoundsException();
//		//
//		//				return this.end();
//		//			}
//		//
//		//			@Override
//		//			public int end() {
//		//				if (start == -1 || end == -1)
//		//					throw new IllegalStateException();
//		//
//		//				return end;
//		//			}
//		//
//		//			@Override
//		//			public String group(int group) {
//		//				if (group != 0)
//		//					throw new IndexOutOfBoundsException();
//		//
//		//				return this.group();
//		//			}
//		//
//		//			@Override
//		//			public String group() {
//		//				if (start == -1 || end == -1)
//		//					throw new IllegalStateException();
//		//
//		//				return string.substring(start, end);
//		//			}
//		//
//		//			@Override
//		//			public int groupCount() {
//		//				return start == -1 || end == -1 ? -1 : 0;
//		//			}
//		//
//		//			@Override
//		//			public int start(int group) {
//		//				if (group != 0)
//		//					throw new IndexOutOfBoundsException();
//		//
//		//				return this.start();
//		//			}
//		//
//		//			@Override
//		//			public int start() {
//		//				if (start == -1 || end == -1)
//		//					throw new IllegalStateException();
//		//
//		//				return start;
//		//			}
//		//		};
//	}

//	/**
//	 * Extract the pattern of the group with the given {@code group} name in the given {@code
//	 * pattern}.
//	 *
//	 * @param pattern the pattern to get the group with the given {@code group} name from it.
//	 * @param group   the name of the group to get its pattern in the given {@code pattern}.
//	 * @return the pattern of the group with the given {@code group} name in the given {@code
//	 * 		pattern}.
//	 * @throws NullPointerException     if the given {@code pattern} or {@code group} is null.
//	 * @throws IllegalArgumentException if the given {@code group} is invalid (does not match the
//	 *                                  regex ({@code \\w[\\w\\d]*})). Or if the given {@code group}
//	 *                                  is not in the given {@code pattern}.
//	 * @since 0.0.b ~2020.10.07
//	 */
//	private static Pattern extract(Pattern pattern, String group) {
//		Objects.requireNonNull(pattern, "pattern");
//		Objects.requireNonNull(group, "group");
//
//		if (!group.matches("\\w[\\w\\d]*"))
//			throw new IllegalArgumentException("Illegal group name: " + group);
//
//		String regex = pattern.pattern();
//		String header = "(?<" + group + ">";
//
//		int offset = regex.indexOf(header);
//		if (offset == -1)
//			throw new IllegalArgumentException("Group not found: " + group);
//
//		int start = offset + header.length();
//		int end = start;
//		int n = 0;
//		boolean e_block = false;
//		boolean e_quick = false;
//		for (int i = start; i < regex.length(); i++) {
//			char c = regex.charAt(i);
//
//			if (e_block)
//				//in escape block
//				if (e_quick)
//					//in quick escape
//					e_quick = false;
//				else if (c == ']')
//					//end escape block
//					e_block = false;
//				else if (c == '\\')
//					//start quick escape
//					e_quick = true;
//				else
//					//escape
//					//noinspection UnnecessaryContinue
//					continue;
//			else if (e_quick)
//				//end quick escape
//				e_quick = false;
//			else if (c == '[')
//				//start escape block
//				e_block = true;
//			else if (c == '\\')
//				//start quick escape
//				e_quick = true;
//			else if (c == '(')
//				//start nest
//				n++;
//			else if (c == ')')
//				if (n != 0)
//					//end nest
//					n--;
//				else {
//					//yatta!
//					end = i;
//					break;
//				}
//		}
//
//		return Pattern.compile(regex.substring(start, end));
//	}

//			if (result.groupCount() == 0)
//				indices.add(new int[]{
//						result.start(),
//						result.end()
//				});
//	/**
//	 * Determine if the area ({@code i} -> {@code j}) intercepts with any area in {@code a} ({@code
//	 * a[?][0]} -> {@code a[?][1]}).
//	 *
//	 * @param a an array of the areas to check if any intercepts with the array ({@code i} -> {@code
//	 *          j}).
//	 * @param i the first index of the area to be checked.
//	 * @param j one past the last index of the area to be checked.
//	 * @return true, if any area in the given {@code a} intercepts with the area ({@code i} ->
//	 *        {@code j}).
//	 * @throws NullPointerException      if the given {@code a} is null. Or has any null element.
//	 * @throws IndexOutOfBoundsException if any array inside the given {@code a} has less than 2
//	 *                                   parameters.
//	 * @since 0.0.b ~2020.10.07
//	 */
//	private static boolean intercept(int[][] a, int i, int j) {
//		Objects.requireNonNull(a, "a");
//		for (int[] b : a) {
//			int m = b[0];
//			int n = b[1];
//			if (
//					(i >= m && i < n) || //i in [m, n)
//					(m >= i && m < j) || //m in [i, j)
//					(j > m && j <= n) || //j in (m, n]
//					(n > i && n <= j)    //n in (i, j]
//			)
//				return true;
//		}
//
//		return false;
//	}
//	@SuppressWarnings("ALL")
//	private static int indexOf(String string, int[][] indices, Pattern target, int fromIndex) {
//		Objects.requireNonNull(string, "string");
//		Objects.requireNonNull(indices, "indices");
//		Objects.requireNonNull(target, "target");
//
//		//		while0:
//		while (true) {
//			Matcher matcher = target.matcher(string.substring(fromIndex));
//
//			if (matcher.find()) {
//				int i = fromIndex + matcher.start();
//				int j = fromIndex + matcher.end();
//
//				//
//				//				String subsub = string.substring(i, j);
//
//				//				for (int[] area : indices) {
//				//					int s = area[0];
//				//					int e = area[1];
//
//				//					if ((s >= i && s < j) ||
//				//						(i >= s && i < e) ||
//				//						(e > i && e <= j) ||
//				//						(j > s && j <= e)) {
//				//						fromIndex = j + 1;
//				//						continue while0;
//				//					}
//				//				}
//				if (intercept(indices, i, j)) {
//					fromIndex = j + 1;
//					continue;
//				}
//
//				return i;
//			}
//
//			return -1;
//		}
//	}

//	@SuppressWarnings("ALL")
//	@Deprecated
//	private static int indexOf(String string, int[][] indices, char target, int fromIndex) {
//		Objects.requireNonNull(string, "string");
//		Objects.requireNonNull(indices, "indices");
//
//		while0:
//		while (true) {
//			int index = string.indexOf(target, fromIndex);
//
//			for (int[] area : indices)
//				if (index >= area[0] && index < area[1]) {
//					fromIndex = index + 1;
//					continue while0;
//				}
//
//			return index;
//		}
//	}

//	@SuppressWarnings("ALL")
//	@Deprecated
//	public static String[] split(String string, Pattern[] escapes, char splitter) {
//		Objects.requireNonNull(string, "string");
//		Objects.requireNonNull(escapes, "escapes");
//		int[][] indicies = indices(string, escapes);
//
//		List<String> split = new ArrayList();
//
//		int last = 0;
//		while (true) {
//			int index = indexOf(string, indicies, splitter, last);
//
//			if (index == -1) {
//				split.add(string.substring(last));
//				return split.toArray(new String[0]);
//			} else {
//				split.add(string.substring(last, index));
//				last = index + 1;
//			}
//		}
//	}

//	@SuppressWarnings("ALL")
//	@Deprecated
//	public static MatchResult group(String string, Pattern[] escapes, char open, char close) {
//		Objects.requireNonNull(string, "string");
//		Objects.requireNonNull(escapes, "escapes");
//		int[][] indicies = indices(string, escapes);
//
//		int start = indexOf(string, indicies, open, 0);
//		int mid = start;
//		int end = start;
//		while (!(end < mid || mid == -1)) {
//			mid = indexOf(string, indicies, open, mid + 1);
//			end = indexOf(string, indicies, close, end + 1);
//		}
//
//		//result
//		int fStart = start;
//		int fEnd = end + 1;
//		return new MatchResult() {
//			@Override
//			public int end(int group) {
//				if (group != 0)
//					throw new IndexOutOfBoundsException();
//
//				return this.end();
//			}
//
//			@Override
//			public int end() {
//				if (fStart == -1 || fEnd == -1)
//					throw new IllegalStateException();
//
//				return fEnd;
//			}
//
//			@Override
//			public String group(int group) {
//				if (group != 0)
//					throw new IndexOutOfBoundsException();
//
//				return this.group();
//			}
//
//			@Override
//			public String group() {
//				if (fStart == -1 || fEnd == -1)
//					throw new IllegalStateException();
//
//				return string.substring(fStart, fEnd);
//			}
//
//			@Override
//			public int groupCount() {
//				return fStart == -1 || fEnd == -1 ? -1 : 0;
//			}
//
//			@Override
//			public int start(int group) {
//				if (group != 0)
//					throw new IndexOutOfBoundsException();
//
//				return this.start();
//			}
//
//			@Override
//			public int start() {
//				if (fStart == -1 || fEnd == -1)
//					throw new IllegalStateException();
//
//				return fStart;
//			}
//		};
//	}
