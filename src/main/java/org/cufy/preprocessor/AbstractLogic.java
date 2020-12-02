///*
// *	Copyright 2020 Cufy
// *
// *	Licensed under the Apache License, Version 2.0 (the "License");
// *	you may not use this file except in compliance with the License.
// *	You may obtain a copy of the License at
// *
// *	    http://www.apache.org/licenses/LICENSE-2.0
// *
// *	Unless required by applicable law or agreed to in writing, software
// *	distributed under the License is distributed on an "AS IS" BASIS,
// *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *	See the License for the specific language governing permissions and
// *	limitations under the License.
// */
//package org.cufy.preprocessor;
//
//import org.cufy.text.AbstractElement;
//import org.cufy.text.Element;
//import org.cufy.preprocessor.link.Logic;
//
//import java.util.Objects;
//
///**
// * An abstraction for the {@link Logic} class. Implementing the essential methods any logic would
// * implement.
// *
// * @param <V> the type of the value held by this logic.
// * @author LSafer
// * @version 0.0.b
// * @since 0.0.b ~2020.10.02
// */
//public abstract class AbstractLogic<V> extends AbstractElement<Logic, V> implements Logic<V> {
//	/**
//	 * Construct a new logic with its {@link #value} not initialized.
//	 *
//	 * @since 0.0.b ~2020.10.11
//	 */
//	public AbstractLogic() {
//	}
//
//	/**
//	 * Construct a new logic with its {@link #value} not initialized.
//	 *
//	 * @param value the value of the constructed logic.
//	 * @throws NullPointerException if the given {@code value} is null.
//	 * @since 0.0.b ~2020.10.11
//	 */
//	public AbstractLogic(V value) {
//		super(value);
//	}
//
//	@Override
//	public boolean setBranch(Element branch) {
//		Objects.requireNonNull(branch, "branch");
//		return branch instanceof Logic &&
//			   super.setBranch(branch);
//	}
//
//	@Override
//	public boolean setFork(Element fork) {
//		Objects.requireNonNull(fork, "fork");
//		return fork instanceof Logic &&
//			   super.setFork(fork);
//	}
//
//	@Override
//	public boolean setNext(Element next) {
//		Objects.requireNonNull(next, "next");
//		return next instanceof Logic &&
//			   super.setNext(next);
//	}
//
//	@Override
//	public boolean setPrevious(Element previous) {
//		Objects.requireNonNull(previous, "previous");
//		return previous instanceof Logic &&
//			   super.setPrevious(previous);
//	}
//}
