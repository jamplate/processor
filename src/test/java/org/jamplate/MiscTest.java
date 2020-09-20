package org.jamplate;

import org.jamplate.logic.*;
import org.jamplate.memory.EmptyMemory;
import org.jamplate.memory.MapMemory;
import org.jamplate.parser.LogicParser;
import org.jamplate.parser.PollParser;
import org.jamplate.parser.ScopeParser;
import org.jamplate.scope.*;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("ALL")
public class MiscTest {
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
					new Define("big", new Constant(options[0])),
					new Define("small", new Constant(options[1])),
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
		AbstractScope[] scopes = {
				new Paste(new Switch(
						new Constant("Makeup"),
						new Constant("Atom"), new Constant("Plain"),
						new Constant("Spark"), new Constant("Flu"),
						new Constant("Makeup"), new Constant("Jamplates")
				))
		};

		String expected =
				//				"Plain" +
				//										"Flu" +
				//				"Spark" +
				"Jamplates" +
				"";

		test(scopes, expected);
	}

	@Test
	public void logic() {
		LogicParser linker = new LogicParser();

		Logic logic = linker.parse("!(Asteroid2 == \"People\") == (!(\"false\" == true))");

		{
			Equation equation = (Equation) logic;

			{
				Negation left = (Negation) equation.left();
				Negation right = (Negation) equation.right();

				{
					Equation equation1 = (Equation) left.logic();

					{
						Reference left1 = (Reference) equation1.left();
						Constant right1 = (Constant) equation1.right();

						{
							Assert.assertEquals("Asteroid2", left1.address());
							Assert.assertEquals("People", right1.value());
						}
					}
				}
				{
					Equation equation1 = (Equation) right.logic();

					{
						Constant left1 = (Constant) equation1.left();
						Reference right1 = (Reference) equation1.right();

						{
							Assert.assertEquals("false", left1.value());
							Assert.assertEquals("true", right1.address());
						}
					}
				}
			}
		}

		Assert.assertEquals("false", logic.evaluate(
				new MapMemory(Collections.singletonMap(
						"Asteroid2",
						new Constant("People")
				))
		));
	}

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

		options0[0].put("A", new Constant("A"));
		options0[0].put("B", new Constant("B"));
		options0[1].put("A", new Constant("X"));
		options0[1].put("B", new Constant("Y"));

		options1[0].put("0", new Constant("0"));
		options1[0].put("1", new Constant("1"));
		options1[0].put("0", new Constant("8"));
		options1[0].put("1", new Constant("9"));

		Scope make0 = new Make(options0);
		Scope make1 = new Make(options1);
		Scope with0 = new With(options0);
		Scope with1 = new With(options1);
		Scope iff = new If(new Equation(new Reference("A"), new Constant("X")));
		Scope ift = new Text("\nIT IS AN X");
		Scope ei = new Endif();
		Scope text = new Text(
				"AB 01"
		);

		System.out.println(make0.tryPush(make1));
		System.out.println(make0.tryPush(with0));
		System.out.println(make0.tryPush(with1));
		System.out.println(make0.tryPush(iff));
		System.out.println(make0.tryPush(ift));
		System.out.println(make0.tryPush(ei));
		System.out.println(make0.tryPush(text));

		make0.invoke(new File("test/AB 01"), new EmptyMemory());
	}

	@Test
	@Ignore
	public void makeParsement() throws IOException {
		Map<String, Logic>[] options = new Map[]{
				new HashMap(),
				new HashMap()
		};

		options[0].put("A", new Constant("Arch"));
		options[0].put("B", new Constant("Boat"));
		options[1].put("A", new Constant("Ant"));
		options[1].put("B", new Constant("Billy"));

		Map<String, Logic> redirects = new HashMap();

		redirects.put("A", new Reference("A"));
		redirects.put("B", new Reference("B"));

		Make make = new Make(options);
		With with = new With(redirects);
		If i = new If(new Equation(new Reference("A"), new Constant("Ant")));
		Text t = new Text("As are good");
		Endif endIf = new Endif();
		Text text = new Text("AB");

		make.tryPush(with);
		make.tryPush(i);
		make.tryPush(t);
		make.tryPush(endIf);
		make.tryPush(text);

		make.invoke(new File("test/AB"), new EmptyMemory());
	}

	@Test
	public void parser() {
		PollParser<Scope> parser = new ScopeParser(new LogicParser());

		Scope scope = parser.parse(
				"#Define a null\n" +
				"#Text Oh my\\\n" +
				"Is not that great?\n #PASTE a#"
		);

		int i = 0;
	}

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
	public void test10() throws IOException {
		Scope firstScope = new Text("A text block");
		Scope s1 = new Define("Variable", new Constant(""));
		Scope s2 = new If(new Equation(new Reference("Variable"), new Constant("VariableValue")));
		Scope s3 = new Text("IF was true");
		Scope s4 = new Elif(new Equation(new Reference("Variable"), new Constant("AnotherVariableValue")));
		Scope s5 = new Text("Elif was true");
		Scope s6 = new Else();
		Scope s7 = new Text("Else invoked");
		Scope s8 = new Endif();
		Scope s9 = new Text("If ended");

		firstScope.tryPush(s1);
		firstScope.tryPush(s2);
		firstScope.tryPush(s3);
		firstScope.tryPush(s4);
		firstScope.tryPush(s5);
		firstScope.tryPush(s6);
		firstScope.tryPush(s7);
		firstScope.tryPush(s8);
		firstScope.tryPush(s9);

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

		options[0].put("A", new Constant("A"));
		options[0].put("B", new Constant("B"));
		options[0].put("C", new Constant("C"));
		options[1].put("A", new Constant("L"));
		options[1].put("B", new Constant("M"));
		options[1].put("C", new Constant("N"));
		options[2].put("A", new Constant("X"));
		options[2].put("B", new Constant("Y"));
		options[2].put("C", new Constant("Z"));

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

			if (!head.tryPush(scope))
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
