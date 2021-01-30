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
/**
 * This package is all about converting source code into {@link
 * org.jamplate.model.sketch.Sketch sketches}.
 * <br>
 * Default layout:
 * <ul>
 *     <li>
 *         A {@link org.jamplate.parse.crawler.Crawler} search in a source to find
 *         matching references.
 *     </li>
 *     <li>
 *         A {@link org.jamplate.parse.maker.Maker} takes a reference and convert it
 *         into a sketch.
 *     </li>
 *     <li>
 *         A {@link org.jamplate.parse.sketcher.Sketcher} uses a
 *         {@link org.jamplate.parse.crawler.Crawler} to find references and multiple
 *         {@link org.jamplate.parse.maker.Maker}s to convert them into sketches.
 *         Also, it can be a poll sketcher that generates results using other sketchers.
 *     </li>
 *     <li>
 *         A {@link org.jamplate.parse.parser.Parser} loops on multiple
 *         {@link org.jamplate.parse.sketcher.Sketcher}s to build a sketch hierarchy.
 *     </li>
 * </ul>
 *
 * @author LSafer
 * @version 0.2.0
 * @since 0.2.0 ~2021.01.26
 */
package org.jamplate.parse;