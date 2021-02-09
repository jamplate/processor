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
package org.jamplate.runtime.compilation;

import org.jamplate.model.document.Document;
import org.jamplate.model.reference.Reference;
import org.jamplate.model.sketch.DocumentSketch;
import org.jamplate.model.sketch.ReferenceSketch;
import org.jamplate.model.sketch.Sketch;
import org.jamplate.processor.parser.Parser;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * An abstraction for the interface {@link Compilation} implementing the basic
 * functionality of a compilation.
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.02.08
 */
public abstract class AbstractCompilation implements Compilation {
	/**
	 * The compilations adapted by this compilation.
	 *
	 * @since 0.2.0 ~2021.02.08
	 */
	protected final Set<Compilation> adaptedCompilations = new HashSet<>();

	/**
	 * A set of documents {@link #append(Document) appended} to this compilation.
	 *
	 * @since 0.2.0 ~2021.02.08
	 */
	protected final Set<Document> appendedDocuments = new HashSet<>();
	/**
	 * A set of references {@link #append(Reference) appended} to this compilation.
	 *
	 * @since 0.2.0 ~2021.02.09
	 */
	protected final Set<Reference> appendedReferences = new HashSet<>();
	/**
	 * A set of references {@link #append(Sketch) appended} to this compilation.
	 *
	 * @since 0.2.0 ~2021.02.09
	 */
	protected final Set<Sketch> appendedSketches = new HashSet<>();

	/**
	 * A set of the {@link Parser}s to be used by this.
	 *
	 * @since 0.2.0 ~2021.02.08
	 */
	protected final Set<Parser> usedParsers = new HashSet<>();

	@Override
	public void adapt(Compilation compilation) {
		Objects.requireNonNull(compilation, "compilation");
		this.adaptedCompilations.add(compilation);
	}

	@Override
	public void append(Reference reference) {
		Objects.requireNonNull(reference, "reference");
		if (this.appendedReferences.add(reference))
			this.append(new ReferenceSketch(reference));
	}

	@Override
	public void append(Sketch sketch) {
		Objects.requireNonNull(sketch, "sketch");
		this.appendedSketches.add(sketch);
	}

	@Override
	public void append(Document document) {
		Objects.requireNonNull(document, "document");
		if (this.appendedDocuments.add(document))
			this.append(new DocumentSketch(document));
	}

	@Override
	public void use(Parser parser) {
		Objects.requireNonNull(parser, "parser");
		this.usedParsers.add(parser);
	}
}
