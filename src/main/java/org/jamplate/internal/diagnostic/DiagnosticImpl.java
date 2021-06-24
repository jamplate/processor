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
package org.jamplate.internal.diagnostic;

import org.jamplate.diagnostic.Diagnostic;
import org.jamplate.diagnostic.Message;
import org.jamplate.internal.util.IO;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

/**
 * A basic implementation of the interface {@link Diagnostic}.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.19
 */
public class DiagnosticImpl implements Diagnostic {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 3123631055963070960L;

	/**
	 * The message queue.
	 *
	 * @since 0.2.0 ~2021.05.31
	 */
	@NotNull
	protected final Deque<Message> queue = new LinkedList<>();

	@NotNull
	@Override
	public Diagnostic clear() {
		this.queue.clear();
		return this;
	}

	@NotNull
	@Override
	public Diagnostic flush(boolean debug, PrintStream out, PrintStream err) {
		Objects.requireNonNull(out, "out");
		Objects.requireNonNull(err, "err");
		while (true) {
			Message message = this.queue.pollFirst();

			if (message == null)
				return this;

			String formatted = this.format(debug, message);

			if (message.isFetal())
				err.println(formatted);
			else
				out.println(formatted);
		}
	}

	@NotNull
	@Override
	public String format(boolean debug, @NotNull Message message) {
		StringBuilder builder = new StringBuilder();

		builder.append(message.getMessagePhrase());

		if (message.getErrorKind().equals(MessageKind.COMPILE))
			for (Tree criticalPoint : message.getCriticalPoints())
				builder.append("\n\tat ")
					   .append(criticalPoint.getSketch())
					   .append("(")
					   .append(criticalPoint.document())
					   .append(":")
					   .append(IO.line(criticalPoint))
					   .append(") ")
					   .append("\n\t")
					   .append(IO.readLine(criticalPoint))
					   .append("\n\t")
					   .append(String.join(
							   "",
							   Collections.nCopies(
									   IO.positionInLine(criticalPoint),
									   " "
							   )
					   ))
					   .append("^")
					   .append(String.join(
							   "",
							   Collections.nCopies(
									   Math.max(
											   criticalPoint.reference().length() - 1, 0),
									   "-"
							   )
					   ));

		for (Tree trace : message.getStackTrace())
			builder.append("\n\tat ")
				   .append(trace.getSketch())
				   .append("(")
				   .append(trace.document())
				   .append(":")
				   .append(IO.line(trace))
				   .append(")");

		if (debug || message.getPriority().equals(MessagePriority.DEBUG)) {
			Throwable exception = message.getException();

			if (exception != null) {
				StringWriter buffer = new StringWriter();

				exception.printStackTrace(new PrintWriter(buffer));

				builder.append("\n\n")
					   .append(buffer);
			}
		}

		return builder.toString();
	}

	@NotNull
	@Override
	public Iterator<Message> iterator() {
		return Collections.unmodifiableCollection(this.queue)
						  .iterator();
	}

	@NotNull
	@Override
	public Diagnostic print(@NotNull Message message) {
		Objects.requireNonNull(message, "message");
		this.queue.addLast(message);
		return this;
	}
}
