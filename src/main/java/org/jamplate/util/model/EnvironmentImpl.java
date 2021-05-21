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
package org.jamplate.util.model;

import org.jamplate.diagnostic.Diagnostic;
import org.jamplate.model.Compilation;
import org.jamplate.model.Document;
import org.jamplate.model.Environment;
import org.jamplate.model.Tree;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

	@NotNull
	@Override
	public Compilation getCompilation(@NotNull Document document) {
		Objects.requireNonNull(document, "document");
		return this.compilations.computeIfAbsent(document,
				k -> new CompilationImpl(this, new Tree(document))
		);
	}

	@NotNull
	@Override
	public Map<Document, Compilation> getCompilations() {
		return Collections.unmodifiableMap(this.compilations);
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

	@Override
	public void setCompilation(@NotNull Document document, @NotNull Compilation compilation) {
		Objects.requireNonNull(document, "document");
		Objects.requireNonNull(compilation, "compilation");
		this.compilations.put(document, compilation);
	}
}
