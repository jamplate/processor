///*
// *	Copyright 2021 Cufy
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
//package org.jamplate_x.runtime.diagnostic;
//
//import org.jamplate.model.Reference;
//
//import java.io.PrintStream;
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * A diagnostic message describing a message from a component while it is working.
// *
// * @author LSafer
// * @version 0.2.0
// * @since 0.2.0 ~2021.01.17
// */
//public class DiagnosticMessage {
//	/**
//	 * A detailed message describing this message.
//	 *
//	 * @since 0.2.0 ~2021.01.17
//	 */
//	protected final String details;
//	/**
//	 * The type of this message.
//	 *
//	 * @since 0.2.0 ~2021.01.17
//	 */
//	protected final DiagnosticKind kind;
//	/**
//	 * The references caused this message. (non-null, cheat checked)
//	 *
//	 * @since 0.2.0 ~2021.01.17
//	 */
//	protected final List<Reference> references;
//	/**
//	 * The title of this message.
//	 *
//	 * @since 0.2.0 ~2021.01.17
//	 */
//	protected final String title;
//
//	/**
//	 * Construct a new message that caused by the given {@code references}.
//	 *
//	 * @param kind       the type of the message.
//	 * @param references the references that caused the constructed message.
//	 * @throws NullPointerException if the given {@code kind} or {@code references} is
//	 *                              null.
//	 * @since 0.2.0 ~2021.01.17
//	 */
//	public DiagnosticMessage(DiagnosticKind kind, Reference... references) {
//		Objects.requireNonNull(kind, "kind");
//		Objects.requireNonNull(references, "references");
//		this.kind = kind;
//		this.title = "";
//		this.details = "";
//		this.references = Arrays.stream(references)
//				.filter(Objects::nonNull)
//				.collect(Collectors.toList());
//	}
//
//	/**
//	 * Construct a new message that have the given {@code title} and caused by the given
//	 * {@code references}.
//	 *
//	 * @param kind       the type of the constructed message.
//	 * @param title      the title for the constructed message.
//	 * @param references the references caused this message.
//	 * @throws NullPointerException if the given {@code kind} or {@code title} or {@code
//	 *                              references} is null.
//	 * @since 0.2.0 ~2021.01.17
//	 */
//	public DiagnosticMessage(DiagnosticKind kind, String title, Reference... references) {
//		Objects.requireNonNull(kind, "kind");
//		Objects.requireNonNull(title, "title");
//		Objects.requireNonNull(references, "references");
//		this.kind = kind;
//		this.title = title;
//		this.details = "";
//		this.references = Arrays.stream(references)
//				.filter(Objects::nonNull)
//				.collect(Collectors.toList());
//	}
//
//	/**
//	 * Construct a new message that have the given {@code title} and the given {@code
//	 * details} and caused by the given {@code references}.
//	 *
//	 * @param kind       the type of the constructed message.
//	 * @param references the references caused the constructed message.
//	 * @param title      the title for the constructed message.
//	 * @param details    the detailed message for the constructed message.
//	 * @throws NullPointerException if the given {@code kind} or {@code title} or {@code
//	 *                              details} or {@code references} is null.
//	 * @since 0.2.0 ~2021.01.17
//	 */
//	public DiagnosticMessage(DiagnosticKind kind, String title, String details, Reference... references) {
//		Objects.requireNonNull(kind, "kind");
//		Objects.requireNonNull(title, "title");
//		Objects.requireNonNull(details, "details");
//		Objects.requireNonNull(references, "references");
//		this.kind = kind;
//		this.title = title;
//		this.details = details;
//		this.references = Arrays.stream(references)
//				.filter(Objects::nonNull)
//				.collect(Collectors.toList());
//	}
//
//	@Override
//	public boolean equals(Object object) {
//		return object == this;
//	}
//
//	@Override
//	public int hashCode() {
//		return this.kind.hashCode() + this.title.hashCode() + this.details.hashCode();
//	}
//
//	@Override
//	public String toString() {
//		return this.title + ": " + this.details;
//	}
//
//	/**
//	 * A detailed message about this message.
//	 *
//	 * @return the details of this message.
//	 * @since 0.2.0 ~2021.01.17
//	 */
//	public String getDetails() {
//		return this.details;
//	}
//
//	/**
//	 * The type of this message.
//	 *
//	 * @return the type of this message.
//	 * @since 0.2.0 ~2021.01.17
//	 */
//	public DiagnosticKind getKind() {
//		return this.kind;
//	}
//
//	/**
//	 * The references that caused this message.
//	 * <br>
//	 *
//	 * @return the references that caused this message.
//	 * @since 0.2.0 ~2021.01.17
//	 */
//	public List<Reference> getReferences() {
//		return Collections.unmodifiableList(this.references);
//	}
//
//	/**
//	 * The title of this message.
//	 *
//	 * @return the title of this message.
//	 * @since 0.2.0 ~2021.01.17
//	 */
//	public String getTitle() {
//		return this.title;
//	}
//
//	/**
//	 * Print this message to the given {@code stream}.
//	 *
//	 * @param stream the stream to print this message to.
//	 * @throws NullPointerException if the given {@code stream} is null.
//	 * @since 0.2.0 ~2021.01.26
//	 */
//	public void print(PrintStream stream) {
////		Objects.requireNonNull(stream, "stream");
////		if (this.references.isEmpty())
////			this.printPhrase(stream);
////		else
////			//noinspection OverlyLongLambda
////			this.references.stream()
////					.distinct()
////					.collect(Collectors.toMap(
////							Reference::lineReference,
////							r -> new ArrayList<>(Collections.singleton(r)),
////							(rp, rn) -> {
////								rp.addAll(rn);
////								return rp;
////							}
////					))
////					.entrySet()
////					.stream()
////					.sorted(Map.Entry.comparingByKey(Reference.COMPARATOR))
////					.forEach(entry -> {
////						Reference line = entry.getKey();
////						List<Reference> list = entry.getValue();
////
////						stream.print(line.document());
////						stream.print(":");
////						stream.print(line.line());
////						stream.print(": ");
////
////						this.printPhrase(stream);
////
////						//noinspection DynamicRegexReplaceableByCompiledPattern
////						stream.print(
////								line.content()
////										.toString()
////										.replaceAll("\n", " ")
////						);
////						stream.println();
////
////						//noinspection ResultOfMethodCallIgnored,OverlyLongLambda
////						list.stream()
////								.mapToInt(r -> r.position() - line.position())
////								.distinct()
////								.sorted()
////								.reduce(0, (i, j) -> {
////									//noinspection AssignmentToLambdaParameter
////									while (++i < j)
////										stream.print(" ");
////
////									stream.print("^");
////									stream.flush();
////									return j;
////								});
////
////						stream.println();
////					});
//	}
//
//	/**
//	 * Print this message to the default print stream.
//	 *
//	 * @since 0.2.0 ~2021.01.26
//	 */
//	public void print() {
//		switch (this.kind) {
//			case DEBUG:
//			case ERROR:
//			case WARNING:
//				//noinspection UseOfSystemOutOrSystemErr
//				this.print(System.err);
//				break;
//			case PROGRESS:
//			case NOTE:
//			default:
//				//noinspection UseOfSystemOutOrSystemErr
//				this.print(System.out);
//				break;
//		}
//	}
//
//	/**
//	 * Print only the phrase of this message to the given {@code stream}.
//	 *
//	 * @param stream the stream to print the phrase of this message.
//	 * @throws NullPointerException if the given {@code stream} is null.
//	 * @since 0.2.0 ~2021.01.30
//	 */
//	public void printPhrase(PrintStream stream) {
//		Objects.requireNonNull(stream, "stream");
//		//noinspection StringToUpperCaseOrToLowerCaseWithoutLocale
//		stream.print(
//				this.kind.toString()
//						.toLowerCase()
//		);
//		if (!this.title.isEmpty()) {
//			stream.print(": ");
//			stream.print(this.title);
//		}
//		if (!this.details.isEmpty()) {
//			stream.print(": ");
//			stream.print(this.details);
//		}
//		stream.println();
//	}
//
//	/**
//	 * Print only the phrase of this message to the default print stream.
//	 *
//	 * @since 0.2.0 ~2021.01.30
//	 */
//	public void printPhrase() {
//		switch (this.kind) {
//			case DEBUG:
//			case ERROR:
//			case WARNING:
//				//noinspection UseOfSystemOutOrSystemErr
//				this.printPhrase(System.err);
//				break;
//			case PROGRESS:
//			case NOTE:
//			default:
//				//noinspection UseOfSystemOutOrSystemErr
//				this.printPhrase(System.out);
//				break;
//		}
//	}
//}
