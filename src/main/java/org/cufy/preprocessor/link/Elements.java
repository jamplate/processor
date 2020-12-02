package org.cufy.preprocessor.link;

import org.cufy.text.Element;

import java.util.Objects;

public final class Elements {
	/**
	 * Get the head element in the chain the given {@code element} is part of.
	 *
	 * @param element an element of the chain that its head element to be found.
	 * @return the head element of the chain of the given {@code element}.
	 * @throws NullPointerException if the given {@code element} is null.
	 * @since 0.0.b ~2020.10.17
	 */
	public static Element getHead(Element element) {
		Objects.requireNonNull(element, "element");
		Element previous = element.getPrevious();
		return previous == null ? element : Elements.getHead(previous);
	}

	/**
	 * Get the tail element in the chain the given {@code element} is part of.
	 *
	 * @param element an element of the chain that its tail element to be found.
	 * @return the tail element of the chain of the given {@code element}.
	 * @throws NullPointerException if the given {@code element} is null.
	 * @since 0.0.b ~2020.10.17
	 */
	public static Element getTail(Element element) {
		Objects.requireNonNull(element, "element");
		Element next = element.getNext();
		return next == null ? element : Elements.getTail(element);
	}

	public static void nullify(Element element, char[] chars) {
		Objects.requireNonNull(element, "element");
		Objects.requireNonNull(chars, "chars");

	}

	//	/**
	//	 * Check if the given range intercepts with the range of the given {@code element}.
	//	 *
	//	 * @param element the element to check if the given range intercept with its range.
	//	 * @param i       the first index at the range to be checked.
	//	 * @param j       one past the last index at the range to be checked.
	//	 * @return true, if the given range intercepts with the range of the given {@code element}.
	//	 * @throws NullPointerException if the given {@code element} is null.
	//	 * @since 0.0.b ~2020.10.15
	//	 */
	//	static boolean intercept(Element element, int i, int j) {
	//		Objects.requireNonNull(element, "element");
	//		int m = element.getBeginIndex();
	//		int n = element.getEndIndex();
	//		return (i >= m && i < n) || //i in [m, n)
	//			   (m >= i && m < j) || //m in [i, j)
	//			   (j > m && j <= n) || //j in (m, n]
	//			   (n > i && n <= j);    //n in (i, j]
	//	}

	//	public static Element findInterceptor(Element element, int i, int j) {
	//
	//	}

	//	/**
	//	 * Check if the given range is intercepting with any reserved range by any element in the chain
	//	 * of the given {@code element}.
	//	 *
	//	 * @param element an element in the chain to be checked.
	//	 * @param i       the first index of the range to be checked.
	//	 * @param j       one past the last index of the range to be checked.
	//	 * @return true, if the given range intercepts with a reserved range by an element in the chain
	//	 * 		the given {@code element} is on.
	//	 * @throws NullPointerException if the given {@code element} is null.
	//	 * @since 0.0.b ~2020.10.17
	//	 */
	//	public static Element getReservee(Element element, int i, int j) {
	//		Objects.requireNonNull(element, "element");
	//		Element head = Elements.getHead(element);
	//		return Elements.getReservee0(head, i, j);
	//	}

	//	/**
	//	 * The method backing the method {@link #getReservee(Element, int, int)}. Since before starting
	//	 * the recursion we need to go to the head of the chain, the method {@link #getReservee(Element,
	//	 * int, int)} is responsible of finding the head and this method is responsible for invoking the
	//	 * recursion. So, this method start a ternary-tree recursion starting from the given {@code e}.
	//	 *
	//	 * @param e the head to start the recursion from.
	//	 * @param i the first index of the range to be checked.
	//	 * @param j one past the last index of the range to be checked.
	//	 * @return the first element that has reserved the range intercepting with the given range.
	//	 * @throws NullPointerException if the given {@code e} is null.
	//	 * @since 0.0.b ~2020.10.17
	//	 */
	//	private static Element getReservee0(Element e, int i, int j) {
	//		Objects.requireNonNull(e, "e");
	//		Element f, b, n, r;
	//		return e.isReserved() && Elements.intercept(e, i, j) ? e :
	//			   (f = e.getFork()) != null && (r = Elements.getReservee0(f, i, j)) != null ? r :
	//			   (b = e.getBranch()) != null && (r = Elements.getReservee0(b, i, j)) != null ? r :
	//			   (n = e.getNext()) != null && (r = Elements.getReservee0(n, i, j)) != null ? r :
	//			   null;
	//	}
}
