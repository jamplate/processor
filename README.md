### Jamplate

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


- `#DEFINE <Address> <Parameter>` this command allocates the results of evaluating the
  parameter given to it into the heap at the address given to it. Additionally, this
  command will result to the replacement of any printed text that is equal to the name of
  the address given to it to the result of evaluating the parameter given to it. Defining
  an already defined address will result to overwriting it.


- `#INCLUDE <Parameter>` this command will search for a compilation its document's name
  equal to the result of evaluating the parameter given to it.
    - If the command couldn't find such compilation, an `Execution` error will occur.


- `#SPREAD <Parameter>` this command will evaluate the parameter given to it and parse it
  as `json object`. If the parsing was successful, the mappings in the parsed object will
  be transferred to the heap. Otherwise, the call is ignored.


- `#UNDEC <Address>` this command will allocate `NULL` to the heap at the address given to
  it (making it undefined). This command will not remove the effects caused by `#define`.


- `#UNDEF <Address>` this command will allocate `NULL` to the heap at the address given to
  it (making it undefined). Additionally, stops the replacing caused by `#define` to that
  address.

### Flow Control Commands

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

- `#ELIFDEF <Parameter>` this command executes the instructions between it, and the next
  branch command (or the closing command). if the branch previous to it was not executed,
  and the address given to it was not `NULL`.
    - If this command was not between an `#IF`, `#IFDEF` or `#IFNDEF` and an `#ENDIF`
      , then a `Compile` error will occur.

- `#ELIFNDEF <Parameter>` this command executes the instructions between it, and the next
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


- `#FOR <Address> <Parameter>` this command evaluates the parameter given to it and parses
  it as a `json array`, then foreach item in the parsed array, this command will allocate
  that item to the heap at the address given to it and executes the instructions between
  it and its closing command.
    - If this command was not closed with an `#ENDFOR`, then a `Compile` error will occur.


- `#ENDFOR` this command closes the `#FOR` command.
    - If this command was not closing a `#FOR` , then a `Compile` error will occur.

### Processor Variables

These variables are managed (allocated) automatically by the processor.

- `__LINE__` this variable holds the line number exactly where it was accessed.


- `__FILE__` this variable holds the name of the file it was accessed at.


- `__PATH__` this variable holds the path of the file it was accessed at.


- `__DIR__` this variable holds the path of the directory of the file it was accessed at.


- `__PROJECT__` this variable holds the path of the project.


- `__JAMPLATE__` this variable contains the version of the jamplate processor.


- `__DEFINE__` this is an internal variable for the processor to manage replace and
  replacements done by the `#define` command.

