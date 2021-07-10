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
package org.jamplate.glucose.spec.tool;

import org.jamplate.Jamplate;
import org.jamplate.function.Listener;
import org.jamplate.glucose.Glucose;
import org.jamplate.glucose.internal.memory.Address;
import org.jamplate.impl.unit.Action;
import org.jamplate.memory.Memory;
import org.jamplate.unit.Spec;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.jamplate.glucose.internal.util.Values.*;

/**
 * Default memory spec. Allocates default memory variables before the execution.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.10
 */
@SuppressWarnings({"UseOfObsoleteDateTimeApi", "UnsecureRandomNumberGeneration"})
public class DefaultMemorySpec implements Spec {
	/**
	 * An instance of this spec.
	 *
	 * @since 0.3.0 ~2021.07.10
	 */
	@NotNull
	public static final DefaultMemorySpec INSTANCE = new DefaultMemorySpec();

	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.07.10
	 */
	@NotNull
	public static final String NAME = DefaultMemorySpec.class.getSimpleName();

	@NotNull
	@Override
	public Listener getListener() {
		return event -> {
			if (event.getAction().equals(Action.PRE_EXEC)) {
				Memory memory = event.getMemory();

				//version
				memory.set(Address.JAMPLATE, text(Jamplate.VERSION));
				memory.set(Address.GLUCOSE, text(Glucose.VERSION));
				memory.set(Address.FLAVOR, text(Jamplate.FLAVOR));

				//date and time
				memory.set(Address.DATE, text((m, v) ->
						new SimpleDateFormat("MMM dd yyyy")
								.format(new Date()))
				);
				memory.set(Address.TIME, text((m, v) ->
						new SimpleDateFormat("HH:mm:ss")
								.format(new Date())
				));

				//init
				memory.set(Address.DEFINE, obj());

				//features
				memory.set(Address.RANDOM, number((m, v) ->
						(long) ((1L << 62) + Math.random() * (1L << 62))
				));
			}
		};
	}

	@NotNull
	@Override
	public String getQualifiedName() {
		return DefaultMemorySpec.NAME;
	}
}
