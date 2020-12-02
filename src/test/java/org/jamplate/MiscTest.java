package org.jamplate;

import org.cufy.preprocessor.link.Logic;
import org.cufy.preprocessor.AbstractScope;
import org.cufy.preprocessor.link.Scope;
import org.jamplate.logic.Equation;
import org.jamplate.logic.Literal;
import org.jamplate.logic.Negation;
import org.jamplate.logic.Reference;
import org.jamplate.memory.EmptyMemory;
import org.jamplate.scope.*;
import org.jamplate.util.Strings;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@SuppressWarnings("ALL")
public class MiscTest {
	@Test
	public void A() {
		//		System.out.println(
		//				Strings.extract(
		//						Pattern.compile(""),
		//						""
		//				)
		//		);
		//
		//		//		MatchResult result = Strings.group(
		//		//				"outer(inner(first)\")\"\"(\"(second))",
		//		//				new Pattern[]{Literal.PATTERN},
		//		//				Pattern.compile("[(]"),
		//		//				Pattern.compile("[)]")
		//		//		);
		//		//
		//		//		System.out.println(result.group());
		//
		//		String source = "first, second , \",\" , fourth, fifth, sixth";
		//		String[] strings = Strings.split(
		//				source,
		//				new Pattern[]{Literal.PATTERN},
		//				new Pattern[0],
		//				Pattern.compile(",")
		//		);
		//
		//		for (String str : strings)
		//			System.out.println(str);
	}

	@Test
	public void If_Elif_Else() throws IOException {
		//#BLOCK "START\n"
		//#DEFINE big "true"
		//#DEFINE small "true"
		//#IF big
		//#BLOCK "I am big\n"
		//#ELIF small
		//#BLOCK "I am small\n"
		//#ELSE
		//#BLOCK "I am neither big nor small\n"
		//#ENDIF
		//#BLOCK "END\n"

		for (String[] options : new String[][]{
				{"true", "true", "I am big\n"},
				{"true", "false", "I am big\n"},
				{"false", "true", "I am small\n"},
				{"false", "false", "I am neither big nor small\n"}
		}) {
			AbstractScope[] scopes = {
					new Text("START\n"),
					new Define("big", new Literal(options[0])),
					new Define("small", new Literal(options[1])),
					new If(new Reference("big")),
					new Text("I am big\n"),
					new Elif(new Reference("small")),
					new Text("I am small\n"),
					new Else(),
					new Text("I am neither big nor small\n"),
					new Endif(),
					new Text("END\n")
			};

			String expected =
					"START\n" +
					options[2] +
					"END\n";

			test(scopes, expected);
		}
	}

	@Test
	public void Switch() throws IOException {
		//		AbstractScope[] scopes = {
		//				new Paste(new Switch(
		//						new Literal("Makeup"),
		//						new Literal("Atom"), new Literal("Plain"),
		//						new Literal("Spark"), new Literal("Flu"),
		//						new Literal("Makeup"), new Literal("Jamplates")
		//				))
		//		};
		//
		//		String expected =
		//				//				"Plain" +
		//				//										"Flu" +
		//				//				"Spark" +
		//				"Jamplates" +
		//				"";
		//
		//		test(scopes, expected);
	}

	//	@Test
	//	public void logic() {
	//		LogicParser linker = (LogicParser) ((ScopeParser) Jamplate.INSTANCE.getParser()).getLogicParser();
	//
	//		Logic logic = linker.parse("!(Asteroid2 == \"People\") == (!(\"false\" == true))");
	//
	//		{
	//			Equation equation = (Equation) logic;
	//
	//			{
	//				Negation left = (Negation) equation.getLeft();
	//				Negation right = (Negation) equation.getRight();
	//
	//				{
	//					Equation equation1 = (Equation) left.getLogic();
	//
	//					{
	//						Reference left1 = (Reference) equation1.getLeft();
	//						Literal right1 = (Literal) equation1.getRight();
	//
	//						{
	//							Assert.assertEquals("Asteroid2", left1.getValue());
	//							Assert.assertEquals("People", right1.getValue());
	//						}
	//					}
	//				}
	//				{
	//					Equation equation1 = (Equation) right.getLogic();
	//
	//					{
	//						Literal left1 = (Literal) equation1.getLeft();
	//						Reference right1 = (Reference) equation1.getRight();
	//
	//						{
	//							Assert.assertEquals("false", left1.getValue());
	//							Assert.assertEquals("true", right1.getValue());
	//						}
	//					}
	//				}
	//			}
	//		}
	//
	//		Assert.assertEquals("false", logic.evaluateString(
	//				new MapMemory(Collections.singletonMap(
	//						"Asteroid2",
	//						new Literal("People")
	//				))
	//		));
	//	}

	@Test
	@Ignore
	public void makeFile() throws IOException {
		Map<String, Logic>[] options0 = new LinkedHashMap[]{
				new LinkedHashMap(),
				new LinkedHashMap()
		};
		Map<String, Logic>[] options1 = new LinkedHashMap[]{
				new LinkedHashMap(),
				new LinkedHashMap()
		};

		options0[0].put("A", new Literal("A"));
		options0[0].put("B", new Literal("B"));
		options0[1].put("A", new Literal("X"));
		options0[1].put("B", new Literal("Y"));

		options1[0].put("0", new Literal("0"));
		options1[0].put("1", new Literal("1"));
		options1[0].put("0", new Literal("8"));
		options1[0].put("1", new Literal("9"));

		Scope make0 = new Make(options0);
		Scope make1 = new Make(options1);
		Scope with0 = new With(options0);
		Scope with1 = new With(options1);
		Scope iff = new If(new Equation(new Reference("A"), new Literal("X")));
		Scope ift = new Text("\nIT IS AN X");
		Scope ei = new Endif();
		Scope text = new Text(
				"AB 01"
		);

		System.out.println(make0.pushElement(make1));
		System.out.println(make0.pushElement(with0));
		System.out.println(make0.pushElement(with1));
		System.out.println(make0.pushElement(iff));
		System.out.println(make0.pushElement(ift));
		System.out.println(make0.pushElement(ei));
		System.out.println(make0.pushElement(text));

		make0.invoke(new File("test/AB 01"), new EmptyMemory());
	}

	@Test
	@Ignore
	public void makeParsement() throws IOException {
		Map<String, Logic>[] options = new Map[]{
				new HashMap(),
				new HashMap()
		};

		options[0].put("A", new Literal("Arch"));
		options[0].put("B", new Literal("Boat"));
		options[1].put("A", new Literal("Ant"));
		options[1].put("B", new Literal("Billy"));

		Map<String, Logic> redirects = new HashMap();

		redirects.put("A", new Reference("A"));
		redirects.put("B", new Reference("B"));

		Make make = new Make(options);
		With with = new With(redirects);
		If i = new If(new Equation(new Reference("A"), new Literal("Ant")));
		Text t = new Text("As are good");
		Endif endIf = new Endif();
		Text text = new Text("AB");

		make.push(with);
		make.push(i);
		make.push(t);
		make.push(endIf);
		make.push(text);

		make.invoke(new File("test/AB"), new EmptyMemory());
	}

	//	@Test
	//	public void parser() {
	//		Parser<Scope> parser = Jamplate.INSTANCE.getParser();
	//
	//		Scope scope = parser.parse(
	//				"#Define a null\n" +
	//				"#Text Oh my\\\n" +
	//				"Is not that great?\n #PASTE a#"
	//		);
	//
	//		int i = 0;
	//	}

	@Test
	public void scopeChain() throws IOException {
		//#BLOCK "FirstLine\n"
		//#DEFINE a "a"
		//#DEFINE b "b"
		//#DEFINE condition !a==b
		//#IF condition
		//#BLOCK "IfLine\n"
		//#BLOCK "Another IfLine\n"
		//#ENDIF
		//#BLOCK "LastLine\n"
		//#PASTE a

		AbstractScope[] code = {
				new Text("FirstLine\n"),
				new Define("a", "a"),
				new Define("b", "b"),
				new Define("condition", new Negation(new Equation(new Reference("a"), new Reference("b")))),
				new If(new Reference("condition")),
				new Text("IfLine\n"),
				new Text("Another IfLine\n"),
				new Endif(),
				new Text("LastLine\n"),
				new Paste(new Reference("a"))
		};

		String expected =
				"FirstLine\n" +
				"IfLine\n" +
				"Another IfLine\n" +
				"LastLine\n" +
				"a";

		test(code, expected);
	}

	@Test
	public void split() {
		//		String source = "abc|def|j\"||||\"hi";
		String source = "abc\"|\"de|f\"|\"j\"||||\"hi";
		String[] split = Strings.split(
				source,
				new Pattern[]{Literal.PATTERN},
				new Pattern[0],
				Pattern.compile("[|]")
		);

		System.out.println(Arrays.toString(split));
	}

	@Test
	public void test10() throws IOException {
		Scope firstScope = new Text("A text block");
		Scope s1 = new Define("Variable", new Literal(""));
		Scope s2 = new If(new Equation(new Reference("Variable"), new Literal("VariableValue")));
		Scope s3 = new Text("IF was true");
		Scope s4 = new Elif(new Equation(new Reference("Variable"), new Literal("AnotherVariableValue")));
		Scope s5 = new Text("Elif was true");
		Scope s6 = new Else();
		Scope s7 = new Text("Else invoked");
		Scope s8 = new Endif();
		Scope s9 = new Text("If ended");

		firstScope.pushElement(s1);
		firstScope.pushElement(s2);
		firstScope.pushElement(s3);
		firstScope.pushElement(s4);
		firstScope.pushElement(s5);
		firstScope.pushElement(s6);
		firstScope.pushElement(s7);
		firstScope.pushElement(s8);
		firstScope.pushElement(s9);

		String results = firstScope.invoke(new EmptyMemory());

		System.out.println(results);

		//    |/
		//0---0---0----0
	}

	@Test
	@Ignore
	public void test40() throws IOException {
		new Text("Hello World")
				.invoke(new File("test/MyTest"), new EmptyMemory());
	}

	@Test
	public void with() throws IOException {
		//now WITH is seperated from MAKE
		Map<String, Logic>[] options = new HashMap[]{
				new LinkedHashMap(),
				new LinkedHashMap(),
				new LinkedHashMap()
		};

		options[0].put("A", new Literal("A"));
		options[0].put("B", new Literal("B"));
		options[0].put("C", new Literal("C"));
		options[1].put("A", new Literal("L"));
		options[1].put("B", new Literal("M"));
		options[1].put("C", new Literal("N"));
		options[2].put("A", new Literal("X"));
		options[2].put("B", new Literal("Y"));
		options[2].put("C", new Literal("Z"));

		AbstractScope[] scopes = {
				new Text("START\n"),
				new With(options),
				new Text("ABC\n"),
				new Endwith(),
				new Text("END\n")
		};

		String expected =
				"START\n" +
				"ABC\n" +
				"LMN\n" +
				"XYZ\n" +
				"END\n";

		this.test(scopes, expected);
	}

	@Test
	@Ignore
	public void withFile() throws IOException {
		//		With letters = new With(
		//				new String[]{"A", "B"},
		//				new Logic[][]{
		//						{new Constant("A"), new Constant("B")},
		//						{new Constant("X"), new Constant("Y")}
		//				}
		//		);
		//		With numbers = new With(
		//				new String[]{"0", "1"},
		//				new Logic[][]{
		//						{new Constant("0"), new Constant("1")},
		//						{new Constant("8"), new Constant("9")}
		//				}
		//		);
		//		Text text = new Text(
		//				"AB 01"
		//		);
		//
		//		System.out.println(letters.tryPush(numbers));
		//		System.out.println(letters.tryPush(text));
		//		letters.invoke(new File("test/AB 01"), new EmptyMemory());
	}

	protected AbstractScope build(AbstractScope[] scopes) {
		Objects.requireNonNull(scopes, "scopes");
		if (scopes.length == 0)
			return new Text("");
		if (scopes.length == 1)
			return scopes[0];

		AbstractScope head = scopes[0];

		for (int i = 1; i < scopes.length; i++) {
			AbstractScope scope = scopes[i];

			if (!head.pushElement(scope))
				throw new RuntimeException(
						"Scope scopes[" + i + "](" + scope + ") cannot be pushed"
				);
		}

		return head;
	}

	protected void test(AbstractScope[] scopes, String expected) throws IOException {
		StringBuilder builder = new StringBuilder();

		this.build(scopes)
				.invoke(builder, new EmptyMemory());

		String actual = builder.toString();
		Assert.assertEquals(
				expected,
				actual
		);
	}
}
