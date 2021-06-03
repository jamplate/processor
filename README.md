# Jamplate [![](https://jitpack.io/v/org.jamplate/processor.svg)](https://jitpack.io/#org.jamplate/processor)

Jamplate is a C-Style pre-processor. Although it is a C-Style, this does not mean it is
following the C standard. This pre-processor has almost the same expected behaviour as a
standard C pre-processors with some features added and some missing.

### Memory Control Commands

This section is describing the memory controlling commands that does not combine with
other commands.

- `#CONSOLE <Parameter>` this command changes the default console with the results of
  evaluating the parameter given to it. Changing the console will result to the next
  printed text being printed into the file given to the command. Calling this will lead to
  closing the previous console. Opening the same console again will lead to it being
  overwritten.
    - If the command failed to open the desired console, an `Execution` error will occur.


- `#DECLARE <Address> <Parameter>` this command allocates the results of evaluating the
  parameter given to it into the heap at the address given to it. Declaring an already
  declared address will result to overwriting it. Declaring a `#define`-ed address will
  only change the value at the heap, but not the replacement.

- `#DECLARE <Address>[Parameter]* <Parameter>` this command puts the results of evaluating
  the parameter given to it to the key (pass the key sequence to access a
  nested `json object`) given to it at the `json object` in the heap at the address given
  to it. If there was no valid `json object` already declared at the address, then a
  new `json object` will be allocated. If a required nested `json object` is missing, or
  is not a `json object`, then a new object will be put at the required place.


- `#DEFINE <Address> <Parameter>` this command allocates the results of evaluating the
  parameter given to it into the heap at the address given to it. Additionally, this
  command will result to the replacement of any printed text that is equal to the name of
  the address given to it to the result of evaluating the parameter given to it. Defining
  an already defined address will result to overwriting it.


- `#INCLUDE <Parameter>` this command will search for a compilation its document's name
  equal to the result of evaluating the parameter given to it.
    - If the command couldn't find such compilation, an `Execution` error will occur.
    - Note: direct circular including will throw `StackOverflowError`.


- `#SPREAD <Parameter>` this command will evaluate the parameter given to it and parse it
  as `json object`. If the parsing was successful, the mappings in the parsed object will
  be transferred to the heap.


- `#UNDEC <Address>` this command will allocate `NULL` to the heap at the address given to
  it (making it undefined). This command will not remove the effects caused by `#define`.


- `#UNDEF <Address>` this command will allocate `NULL` to the heap at the address given to
  it (making it undefined). Additionally, stops the replacing caused by `#define` to that
  address.

### Flow Control Commands

This section contains the flow controlling commands that demand other commands and perform
scopes.

- `#CAPTURE <Address>` this command captures the printing of the instructions between it
  and its closing command, then allocate the captured text to the address given to it.
    - If this command was not closed with an `#ENDCAPTURE`, then a `Compile` error will
      occur.


- `#ENDCAPTURE` this command closes the `#CAPTURE` command.
    - If this command was not closing a `#CAPTURE` command, then a `Compile` error will
      occur.


- `#IF <Parameter>` this command executes the instructions between it, and the next branch
  command (or the closing command). if the parameter given to it evaluated to none of
  (`"false"`, `"\0"`, `"0"`, `""`).
    - If this command was not closed with an `#ENDIF`, then a `Compile` error will occur.

- `#IFDEF <Address>` this command executes the instructions between it, and the next
  branch command (or the closing command). if the address given to it was not `NULL`.
    - If this command was not closed with an `#ENDIF`, then a `Compile` error will occur.

- `#IFNDEF <Address>` this command executes the instructions between it, and the next
  branch command (or the closing command). if the address given to it was `NULL`.
    - If this command was not closed with an `#ENDIF`, then a `Compile` error will occur.


- `#ELIF <Parameter>` this command executes the instructions between it, and the next
  branch command (or the closing command). if the branch previous to it was not executed,
  and the parameter given to it evaluated to none of
  (`"false"`, `"\0"`, `"0"`, `""`).
    - If this command was not between an `#IF`, `#IFDEF` or `#IFNDEF` and an `#ENDIF`
      , then a `Compile` error will occur.

- `#ELIFDEF <Address>` this command executes the instructions between it, and the next
  branch command (or the closing command). if the branch previous to it was not executed,
  and the address given to it was not `NULL`.
    - If this command was not between an `#IF`, `#IFDEF` or `#IFNDEF` and an `#ENDIF`
      , then a `Compile` error will occur.

- `#ELIFNDEF <Address>` this command executes the instructions between it, and the next
  branch command (or the closing command). if the branch previous to it was not executed,
  and the address given to it was `NULL`.
    - If this command was not between an `#IF`, `#IFDEF` or `#IFNDEF` and an `#ENDIF`
      , then a `Compile` error will occur.


- `#ELSE` this command executes the instructions between it, and the closing command. if
  all the branches previous to it was not executed.
    - If this command was not between an `#IF`, `#IFDEF` or `#IFNDEF` and an `#ENDIF`, or
      not directly followed by an `#ENDIF`, then a `Compile` error will occur.


- `#ENDIF` this command closes the `#IF`, `#IFDEF` or `#IFNDEF` commands.
    - If this command was not closing an `#If`, `#IFDEF` or `#IFNDEF` command, then
      a `Compile` error will occur.


- `#FOR <Address> <Array>` foreach item in the given array, this command will allocate
  that item to the heap at the address given to it and executes the instructions between
  it and its closing command.
    - If this command was not closed with an `#ENDFOR`, then a `Compile` error will occur.


- `#ENDFOR` this command closes the `#FOR` command.
    - If this command was not closing a `#FOR` , then a `Compile` error will occur.


- `#WHILE <Parameter>` this command keeps executing the instructions between it and its
  closing command until the parameter given to it evaluates to false.
    - If this command was not closed with an `#ENDWHILE`, then a `Compile` error will
      occur.
    - Note: infinite loops will trigger any error and this might lead to unnoticed severe
      errors. Like, consuming a lot of RAM, or files do not get closed, or unstoppable
      processes.


- `#ENDWHILE` this command closes the `#WHILE` command.
    - If this command was not closing a `#WHILE` , then a `Compile` error will occur.


- `#{ <Parameter> }#` this is called 'injection', and it injects the parameter given to it
  to the console. Different from commands, injections can be placed anywhere (but not
  clashing inside another injection or inside a command), and it does not suppress the
  line separators before nor after it.

### Debugging

- `#MESSAGE <Parameter>` evaluates the parameter given to it and print the evaluated text
  to the `System.out`.


- `#ERROR <Parameter>` evaluates the parameter given to it and print the evaluated text to
  the `System.error`.

### Processor Variables

These variables are managed (allocated) automatically by the processor.

- `__LINE__ : Number` this variable holds the line number exactly where it was accessed.


- `__FILE__ : Text` this variable holds the name of the file it was accessed at.


- `__PATH__ : Text` this variable holds the path of the file it was accessed at.


- `__DIR__ : Text` this variable holds the path of the directory of the file it was
  accessed at.


- `__PROJECT__ : Text` this variable holds the path of the project.


- `__JAMPLATE__ : Text` this variable contains the version of the jamplate processor.


- `__DATE__ : Text` this variable contains the current date at the time accessing it
  in `MMM dd yyyy` format.


- `__TIME__ : Text` this variable contains the current time at the time accessing it
  in `HH:mm:ss` format.


- `__DEFINE__ : Object` an internal variable for the processor to manage replace and
  replacements done by the `#define` command.

### Data Types

Jamplate has no actual data types, since jamplate stores the data in plain text, but some
operations treat the data given to it differently depending on the text and the operation
itself.

- `Parameter` applies to any text. In commands, means that the command will evaluate the
  parameter logically.


- `Address` applies to any text, In commands, means that the command will take it AS-IS.
  Also, in commands, only whitespace-free addresses will be taken.


- `Number` applies to numeric text.


- `Object` applies to valid JSON object text. When passed as an array, the resultant array
  will be an array of the keys in the object.


- `Array` applies to valid JSON array text. When passed as an object, the resultant object
  will be each item mapped to its index.

### Syntax

- To reference a variable, write the variable name.
    - Example `MyVariable`.


- To reference a property of a variable, write the variable name followed by two square
  brackets (`[]`) with the name of the property inside the two brackets.
    - Example `MyVariable['MyProperty']`


- A `Number` is defined by writing its value.
    - Example `12`


- A `String` is defined by encapsulating a text inside two double-quotes (`""`).
    - Example `"My String"`


- An escaped `String` is defined by encapsulating a text inside two quotes (`''`).
    - Example `'My Escaped String'`


- An `Array` is defined with two square brackets (`[]`) with its items between the two
  brackets and separated by a comma (`,`).
    - Example `[A, B, C]`.


- An `Object` is defined with two curly braces (`{}`) with its mappings between the two
  braces and separated by a comma (`,`). Also, with the `:` separating the key and value
  of each mapping.
    - Example `{A: X, B: Y, C: Z}`.

### Operators

Just like any basic programming language, jamplate support value operators. This section
contains the supported operators.

- `!` (`NOT`) this operator negates the value after it.
    - If this operator has a value before it, then a `Compile` error will occur.


- `*` (`Multiply`) this operator multiplies the value before it with the value after it.
    - If this operator does not have a value before and after it, then a `Compiler` error
      will occur.
    - If the value before or after it is not a number, then an `Execution` error will
      occur.


- `/` (`Divide`) this operator divides the value before it by the value after it.
    - If this operator does not have a value before and after it, then a `Compiler` error
      will occur.
    - If the value before or after it is not a number, then an `Execution` error will
      occur.
    - If the value after it is `0`, then an `Execution` error will occur with the
      face (`:P`).


- `%` (`Division Remainder`) this operator evaluates to the remainder of dividing value
  before it by the value after it.
    - If this operator does not have a value before and after it, then a `Compiler` error
      will occur.
    - If the value before or after it is not a number, then an `Execution` error will
      occur.
    - If the value after it is `0`, then an `Execution` error will occur with the
      face (`:P`).


- `+` (`Add`) this operator will add the value before it with the value after it. If one
  of the two values is not a number, then the values will be concatenated.
    - If this operator does not have a value after it, then a `Compiler` error will occur.


- `-` (`Subtract`) this operator will subtract the value after it from the value before
  it. If no value before it (at compile time), then it will flip the sign of the value
  after it.
    - If this operator does not have a value after it, then a `Compiler` error will occur.
    - If the value before or after it is not a number, then an `Execution` error will
      occur.


- `<` (`less than`) this operator will evaluate to `true` if the value before it is less
  than the value after it.
    - If this operator does not have a value before and after it, then a `Compiler` error
      will occur.


- `<=` (`less than or equal`) this operator will evaluate to `true` if the value before it
  is less than or equal the value after it.
    - If this operator does not have a value before and after it, then a `Compiler` error
      will occur.


- `>` (`more than`) this operator will evaluate to `true` if the value before it is more
  than the value after it.
    - If this operator does not have a value before and after it, then a `Compiler` error
      will occur.


- `>=` (`more than or equal`) this operator will evaluate to `true` if the value before it
  is more than or equal the value after it.
    - If this operator does not have a value before and after it, then a `Compiler` error
      will occur.


- `==` (`equals`) this operator will evaluate to `true` if the value before it equals the
  value after it.
    - If this operator does not have a value before and after it, then a `Compiler` error
      will occur.


- `!=` (`not equals`) this operator will evaluate to `true` if the value before it does
  not equal the value after it.
    - If this operator does not have a value before and after it, then a `Compiler` error
      will occur.


- `&&` (`logical and`) this operator will evaluate to `true` if the value before it and
  after it both evaluated to `true`.
    - If this operator does not have a value before and after it, then a `Compiler` error
      will occur.


- `||` (`logical or`) this operator will evaluate to `true` if the value before it, or the
  value after it either evaluated to true.

### Distribution

This repository is the core processor and contain no application. You might use the
[Jamplate Gradle Plugin](https://github.com/jamplate/gradle) instead. If you want to use
the processor directly or just extend it, you might download the repository or
use `jitpack.io`.

```gradle
repositories {
	maven { url 'https://jitpack.io' }
}

dependencies {
	//replace `Tag` with the targeted version.
	implementation 'org.jamplate:processor:Tag'
}
```

### Project Info

- GitHub Repository:
  [https://github.com/jamplate/processor](https://github.com/jamplate/processor)
- Website: [https://jamplate.org](https://jamplate.org)
- Author: [LSafer](https://lsafer.net)
- Licences: [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0)

### Licence

    Copyright 2021 Cufy

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
