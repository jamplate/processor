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
package org.jamplate.impl.model;

import org.jamplate.diagnostic.Diagnostic;
import org.jamplate.impl.Kind;
import org.jamplate.impl.diagnostic.DiagnosticImpl;
import org.jamplate.model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;

/**
 * A basic implementation of the interface {@link Environment}.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.05.19
 */
public class EnvironmentImpl implements Environment {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -1644390742511931321L;

	/**
	 * The compilations in this environment.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	protected final Map<Document, Compilation> compilations = new HashMap<>();

	/**
	 * The diagnostic manager in this enlivenment.
	 *
	 * @since 0.2.0 ~2021.05.19
	 */
	@NotNull
	protected final Diagnostic diagnostic = new DiagnosticImpl();
	/**
	 * The additional meta-data of this sketch.
	 *
	 * @since 0.2.0 ~2021.05.21
	 */
	@NotNull
	protected final Map<String, Object> meta = new HashMap<>();

	/**
	 * A set of the compilations in this environment.
	 *
	 * @since 0.2.0 ~2021.05.23
	 */
	@Nullable
	@UnmodifiableView
	protected Set<Compilation> compilationSet;

	@NotNull
	@Override
	public Set<Compilation> compilationSet() {
		if (this.compilationSet == null) {
			Collection<Compilation> compilations = this.compilations.values();
			this.compilationSet = new AbstractSet<Compilation>() {
				@Override
				public boolean contains(@Nullable Object object) {
					return compilations.contains(object);
				}

				@Override
				public Iterator<Compilation> iterator() {
					Iterator<Compilation> iterator = compilations.iterator();
					return new Iterator<Compilation>() {
						@Override
						public boolean hasNext() {
							return iterator.hasNext();
						}

						@Override
						public Compilation next() {
							return iterator.next();
						}

						@Override
						public void remove() {
							iterator.remove();
						}
					};
				}

				@Override
				public int size() {
					return compilations.size();
				}
			};
		}

		return this.compilationSet;
	}

	@Nullable
	@Override
	public Compilation getCompilation(@NotNull Document document) {
		Objects.requireNonNull(document, "document");
		return this.compilations.get(document);
	}

	@Nullable
	@Override
	public Compilation getCompilation(@NotNull String name) {
		Objects.requireNonNull(name, "name");
		return this.compilations
				.entrySet()
				.parallelStream()
				.filter(entry -> entry.getKey().toString().equals(name))
				.map(Map.Entry::getValue)
				.findAny()
				.orElse(null);
	}

	@NotNull
	@Override
	public Diagnostic getDiagnostic() {
		return this.diagnostic;
	}

	@NotNull
	@Override
	public Map<String, Object> getMeta() {
		return Collections.checkedMap(this.meta, String.class, Object.class);
	}

	@NotNull
	@Override
	public Compilation optCompilation(@NotNull Document document) {
		Objects.requireNonNull(document, "document");
		return this.compilations.computeIfAbsent(document, k ->
				new CompilationImpl(this, new Tree(document, new Sketch(Kind.DC_ROT), -1))
		);
	}

	@Override
	public void setCompilation(@NotNull Document document, @NotNull Compilation compilation) {
		Objects.requireNonNull(document, "document");
		Objects.requireNonNull(compilation, "compilation");
		this.compilations.put(document, compilation);
	}
}
