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
package org.jamplate.impl;

import org.jamplate.impl.model.EnvironmentImpl;
import org.jamplate.impl.model.PseudoDocument;
import org.jamplate.model.Compilation;
import org.jamplate.model.Document;
import org.jamplate.model.Environment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author LSafer
 * @version 0.0.0
 * @since 0.0.0 ~2021.05.30
 */
public class ParsersTest {
	@Test
	public void ifs() {
		Document document = new PseudoDocument("#declare [HI =] true");
		Environment environment = new EnvironmentImpl();
		Compilation compilation = environment.optCompilation(document);

		Jamplate.PARSER_PROCESSOR.process(compilation);
		Processors.CX_CMD.process(compilation);

		assertEquals(
				Kind.CX_CMD_DECLARE,
				compilation.getRootTree().getChild().getSketch().getKind(),
				"Declare not recognized"
		);
	}
}