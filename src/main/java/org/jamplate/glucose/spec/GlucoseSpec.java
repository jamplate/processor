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
package org.jamplate.glucose.spec;

import org.jamplate.api.Spec;
import org.jamplate.glucose.spec.command.hashcapture.FlowCaptureSpec;
import org.jamplate.glucose.spec.command.hashcapture.HashCaptureSpec;
import org.jamplate.glucose.spec.command.hashcapture.HashEndcaptureSpec;
import org.jamplate.glucose.spec.command.hashconsole.HashConsoleSpec;
import org.jamplate.glucose.spec.command.hashdeclare.HashDeclareSpec;
import org.jamplate.glucose.spec.command.hashdeclare.TouchDeclareSpec;
import org.jamplate.glucose.spec.command.hashdefine.HashDefineSpec;
import org.jamplate.glucose.spec.command.hasherror.HashErrorSpec;
import org.jamplate.glucose.spec.command.hashfor.FlowForSpec;
import org.jamplate.glucose.spec.command.hashfor.HashEndforSpec;
import org.jamplate.glucose.spec.command.hashfor.HashForSpec;
import org.jamplate.glucose.spec.command.hashif.*;
import org.jamplate.glucose.spec.command.hashmessage.HashMessageSpec;
import org.jamplate.glucose.spec.command.hashspread.HashSpreadSpec;
import org.jamplate.glucose.spec.command.hashundec.HashUndecSpec;
import org.jamplate.glucose.spec.command.hashundef.HashUndefSpec;
import org.jamplate.glucose.spec.command.hashwhile.FlowWhileSpec;
import org.jamplate.glucose.spec.command.hashwhile.HashEndwhileSpec;
import org.jamplate.glucose.spec.command.hashwhile.HashWhileSpec;
import org.jamplate.glucose.spec.document.RootSpec;
import org.jamplate.glucose.spec.document.TextSpec;
import org.jamplate.glucose.spec.element.*;
import org.jamplate.glucose.spec.misc.*;
import org.jamplate.glucose.spec.parameter.extension.GetterSpec;
import org.jamplate.glucose.spec.parameter.operator.*;
import org.jamplate.glucose.spec.parameter.resource.*;
import org.jamplate.glucose.spec.standard.AnchorSpec;
import org.jamplate.glucose.spec.standard.ExtensionSpec;
import org.jamplate.glucose.spec.standard.OperatorSpec;
import org.jamplate.glucose.spec.syntax.enclosure.*;
import org.jamplate.glucose.spec.syntax.symbol.*;
import org.jamplate.glucose.spec.syntax.term.DigitsSpec;
import org.jamplate.glucose.spec.syntax.term.WordSpec;
import org.jamplate.impl.api.MultiSpec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

/**
 * The spec of the glucose implementation of jamplate.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.07.04
 */
public class GlucoseSpec extends MultiSpec {
	/**
	 * The qualified name of this spec.
	 *
	 * @since 0.3.0 ~2021.07.04
	 */
	@NotNull
	public static final String NAME = GlucoseSpec.class.getSimpleName();

	/**
	 * Construct a new jamplate glucose implementation specification.
	 *
	 * @param subspecs extra specs to be added.
	 * @since 0.3.0 ~2021.07.04
	 */
	public GlucoseSpec(@Nullable Spec @Nullable ... subspecs) {
		super(
				//name
				GlucoseSpec.NAME,
				//document
				RootSpec.INSTANCE,
				//misc
				NewlineSpec.INSTANCE,
				NewlineEscapedSpec.INSTANCE,
				NewlineSuppressedSpec.INSTANCE,
				//standard
				AnchorSpec.INSTANCE,
				ExtensionSpec.INSTANCE,
				OperatorSpec.INSTANCE,
				//collisions
				new CollisionSpec(
						//injection
						InjectionSpec.INSTANCE,
						//quotes
						QuotesSpec.INSTANCE,
						DoubleQuotesSpec.INSTANCE,
						//comments
						CommentBlockSpec.INSTANCE,
						CommentLineSpec.INSTANCE
				),
				//flow
				new FlowSpec(
						//#for #endfor
						FlowForSpec.INSTANCE,
						//#while #endwhile
						FlowWhileSpec.INSTANCE,
						//#capture #endcapture
						FlowCaptureSpec.INSTANCE,
						//#if #ifdef #ifndef #elif #elifdef #elifndef #else #endif
						FlowIfSpec.INSTANCE
				),
				//commands
				new CommandSpec(
						//#while #endwhile
						HashWhileSpec.INSTANCE,
						HashEndwhileSpec.INSTANCE,
						//#for #endfor
						HashForSpec.INSTANCE,
						HashEndforSpec.INSTANCE,
						//#capture #endcapture
						HashCaptureSpec.INSTANCE,
						HashEndcaptureSpec.INSTANCE,
						//#if #ifdef #ifndef #elif #elifdef #elifndef #else #endif
						HashIfSpec.INSTANCE,
						HashIfdefSpec.INSTANCE,
						HashIfndefSpec.INSTANCE,
						HashElifSpec.INSTANCE,
						HashElifdefSpec.INSTANCE,
						HashElifndefSpec.INSTANCE,
						HashElseSpec.INSTANCE,
						HashEndifSpec.INSTANCE,
						//#declare
						HashDeclareSpec.INSTANCE,
						TouchDeclareSpec.INSTANCE,
						//#define
						HashDefineSpec.INSTANCE,
						//#undec
						HashUndecSpec.INSTANCE,
						//#undef
						HashUndefSpec.INSTANCE,
						//#spread
						HashSpreadSpec.INSTANCE,
						//#error
						HashErrorSpec.INSTANCE,
						//#message
						HashMessageSpec.INSTANCE,
						//#console
						HashConsoleSpec.INSTANCE
				),
				//parameters
				new ParameterSpec(
						//term
						DigitsSpec.INSTANCE,
						WordSpec.INSTANCE,
						//symbol
						AndAndSpec.INSTANCE,
						AsteriskSpec.INSTANCE,
						CloseChevronEqualSpec.INSTANCE,
						CloseChevronSpec.INSTANCE,
						ColonSpec.INSTANCE,
						CommaSpec.INSTANCE,
						EqualEqualSpec.INSTANCE,
						ExclamationEqualSpec.INSTANCE,
						ExclamationSpec.INSTANCE,
						MinusSpec.INSTANCE,
						OpenChevronEqualSpec.INSTANCE,
						OpenChevronSpec.INSTANCE,
						PercentSpec.INSTANCE,
						PipePipeSpec.INSTANCE,
						PlusSpec.INSTANCE,
						SlashSpec.INSTANCE,
						//enclosure
						BracesSpec.INSTANCE,
						BracketsSpec.INSTANCE,
						ParenthesesSpec.INSTANCE,
						//resource
						ArraySpec.INSTANCE,
						EscapedStringSpec.INSTANCE,
						GroupSpec.INSTANCE,
						NumberSpec.INSTANCE,
						ObjectSpec.INSTANCE,
						ReferenceSpec.INSTANCE,
						StringSpec.INSTANCE,
						//operator
						AdderSpec.INSTANCE,
						DividerSpec.INSTANCE,
						EqualsSpec.INSTANCE,
						LessThanEqualsSpec.INSTANCE,
						LessThanSpec.INSTANCE,
						LogicalAndSpec.INSTANCE,
						LogicalOrSpec.INSTANCE,
						ModuloSpec.INSTANCE,
						MoreThanEqualsSpec.INSTANCE,
						MoreThanSpec.INSTANCE,
						MultiplierSpec.INSTANCE,
						NotEqualsSpec.INSTANCE,
						NotSpec.INSTANCE,
						PairSpec.INSTANCE,
						SubtractorSpec.INSTANCE,
						//extension
						GetterSpec.INSTANCE
				),
				//glue
				TextSpec.INSTANCE
		);
		if (subspecs != null)
			Arrays.stream(subspecs)
				  .filter(Objects::nonNull)
				  .forEach(this.specs::add);
	}
}
