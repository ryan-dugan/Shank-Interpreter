<h1>Interpreter for the Shank Programming Language</h1>

<h2>Description</h2>
<b>Shank</b> is a programming language developed by Professor Michael Phipps at UAlbany.<br />
- [Language Definition.pdf](https://github.com/ryan-dugan/ShankInterpreter/blob/main/Language%20Definition.pdf)
<br />
<br />

<b>Project consists of 4 main components:</b>
- Lexical Analysis and Tokenizing (Lexer)
- Parsing of Tokens into an Abstract Symbol Tree (Parser)
- Semantical Analysis
- And finally, the Interpreter

Users enter Shank code into the input file "input.shank" and the program runs the code.
<br />



<h2>Languages and Utilities Used</h2>

- <b>Java:</b> The language used for coding this project.

<h2>Environments Used </h2>

- <b>Eclipse:</b> The integrated development environment (IDE) utilized for development.
- <b>JDK 16.0.2:</b> The Java Development Kit version employed for compiling and running this project.

<h2>Program walk-through:</h2>

<p align="center">
Example Input - Greatest Common Denominator Program: <br/>
<img src="https://i.imgur.com/gifw53J.png" height="80%" width="80%" alt="Shank Code"/>
<br />
<br />
Lexer Output:  <br/>
<img src="https://i.imgur.com/cZNMdQl.png" height="80%" width="80%" alt="Disk Sanitization Steps"/>
<br />
<br />
Parser Output: <br/>
<img src="https://i.imgur.com/tiK4h1r.png" height="80%" width="80%" alt="Disk Sanitization Steps"/>
<br />

<h2>Shank Language Definition</h2>
Shank is different in some significant ways from languages that you may already know.<br />
<br />

The biggest differences are:<br />
1)	Shank functions don’t have return values. <br />
2)	Shank functions can alter variables that are passed into the function, when the variable is marked as “var”<br />
3)	Shank doesn’t use curly braces {} for blocks – it uses indentation. <br />
4)	Shank for loops are much simpler than Java or C for loops.<br />
5)	Assignment uses :=, but comparison uses =<br />

<h3>Overall Function Format</h3>
- function definition (define)<br />
- constants (optional)<br />
- variables (optional)<br />
- body<br />
<br />

For example:<br />
<br />
define start (args : array of string)<br />
constants pi=3.141<br />
variables a,b,c : integer<br />
a := 1<br />
b := 2<br />
c := 3<br />

<h3>Comments</h3>
Comments can span multiple lines. They start with { and end with }<br />
<br />
Example:<br />
<br />
{ This is a comment }<br />
<br />

<h3>Blocks</h3>
Blocks are one or more statements that are run one after another. Blocks are indented one level more than the “owner” of the block. To end a block, you simply “un-indent” one level. Indentation on empty lines or lines that contain only a comment is not counted.<br />
<br />

Example:<br />
<br />
for a from 1 to 10<br />
	write a<br />
	write a+1<br />
write “not in the block” <br />

<h3>Built-in types:</h3>
- integer (32-bit signed number)<br />
- real (floating point)<br />
- boolean (constant values: true, false)<br />
- character (a single number/letter/symbol)<br />
- string (arbitrarily large string of characters)<br />
- array of (any of the above; no arrays of arrays)<br />
<br />

<b>Arrays</b><br />
<br />
Arrays are declared much like other variables:<br />
<br />
variables names : array from 0 to 5 of string<br />
All arrays are 1 dimensional and must have a range declared at definition time. Referencing a variable is done using square brackets:<br />
names[0] := “mphipps”<br />
write names[0] <br />

<h3>Variables</h3>
Variable declarations are defined by the keyword variables, a list of names, then a “:” and then the data type. A name must start with a letter (lower or upper case) and then can have any number of letters and/or numbers. There can be more than one variables line in a function.<br />
<br />

Example: <br />
<br />
variables variable1, a, foo9 : integer<br />
variables name, address, country : string<br />

<h3>Constants</h3>
Constants are variables that are set at definition and cannot be changed after definition. They do not require a data type since the data type is inferred from the value. There can be more than one per function. The data type of the function need not be consistent in the single line.<br />
<br />
constants myName = “mphipps”<br />
constants pi = 3.141<br />
constants class = ”ICSI311”, goodGrade = 95<br />

<h3>Type limits</h3>
Types can be limited at declaration time using “from” and “to”. Does not apply to booleans.<br />
<br />

Example:<br />
<br />
variables numberOfCards : integer from 0 to 52<br />
variables waterTemperature: real from 0.0 to 100.0<br />
variables shortString : string from 0 to 20 (* string has a length limit *)<br />

<h3>Functions (also known as: Procedures/Methods/Subroutines)</h3>
A function is an (optional) constant section, an (optional) variable section and a block. Functions have a name and a set of parameters; this combination must be unique. <br />
Function parameters are read-only (treated as constant) by default. To allow them to be changed, we proceed them by the keyword “var” both in the function declaration and in the call to the function.<br />
<br />

Example:<br />
<br />
define addTwo(x,y : integer; var sum: integer)<br />
	sum := x + y<br />
<br />

To call this function:<br />
addTwo 5,4,var total { total was declared somewhere else }<br />
<br />

The var keyword must be used before each variable declaration that is alterable.<br />
<br />
define someFunction(readOnly:integer; var changeable : integer; alsoReadOnly : integer)<br />
someFunction someVariable, var answer, 6 <br />
<br />

In contrast to other languages, functions never return anything except through the “var” variables. While this is unfamiliar to people who have used other languages, it is actually powerful, since you can return as many values as you choose.<br />
<br />
define average(values:array of integer; var mean, median, mode : real)<br />
<br />

When the program starts, the function “start” will be called. <br />
<br />

<h3>Control structures and Loops</h3>
The only conditional control structure that we support is “if-elsif-else”. Its format is:<br />
<br />
if booleanExpression then block {elsif booleanExpression then block}[else block]<br />
<br />

Examples:<br />
<br />
if a<5 then <br />
	a := 5<br />
<br />
if i mod 15=0 then<br />
<t>write “FizzBuzz “</t><br />
elsif i mod 3=0 then<br />
<t>write “Fizz “</t><br />
elsif i mod 5 = 0 then <br />
<t>write “Buzz “</t><br />
else<br />
<t>write I, “ “</t><br />
<br />

There are three types of loops that we support:<br />
<br />
for integerVariable from value to value <br />
block <br />
<br />

while booleanexpression <br />
block<br />
<br />

repeat until booleanExpression<br />
block<br />
Note – the control variable in the for loop is not automatically declared – it must be declared before the for statement is encountered. From only counts forward by one. <br />
<br />

Examples:<br />
<br />
for i from 1 to 10 <br />
	write i { prints values 1 … 10 }<br />
<br />

for j from 10 to 2 { this loop will never execute}<br />
	write j<br />
<br />

while j < 5<br />
	j:=j+1<br />
<br />

repeat until j = 0<br />
	j:=j-1<br />
<br />

Since these are statements, they can be embedded within each other:<br />
<br />

if a<5 then<br />
	repeat until a=6<br />
		for j from 0 to 5<br />
			while k < 2<br />
				k:=k+1<br />
		a := a + 1<br />
	<br />

<h3>Operators and comparison </h3>
Integers and reals have the following operators: +,-,*,/, mod. The order of operations is parenthesis, *,/,mod (left to right), then +,- (also left to right).<br />
Booleans have: not, and, or. The order of operations is not, and, or.<br />
Characters have no operators.<br />
Strings have only + (concatenation) of characters or strings.<br />
Arrays have only the index operator [], but all relevant operators apply to an indexed element.<br />
Comparison can only take place between the same data types.<br />
= (equals), <> (not equal), <, <=, >, >= (all done from left to right).<br />

<h3>Built-in functions</h3>
</b>I/O Functions</b><br />
Read var a, var b, var c { for example – these are variadic }<br />
	Reads (space delimited) values from the user<br />
Write a,b,c { for example – these are variadic }<br />
	Writes the values of a,b and c separated by spaces<br />
 <br />
 
<b>String Functions</b><br />
	Left someString, length, var resultString<br />
		ResultString = first length characters of someString<br />
Right someString, length, var resultString<br />
	ResultString = last length characters of someString<br />
 Substring someString, index, length, var resultString<br />
	ResultString = length characters from someString, starting at index<br />
 <br />
 
<b>Number Functions</b><br />
	SquareRoot someFloat, var result<br />
		Result = square root of someFloat<br />
GetRandom var resultInteger<br />
		resultInteger = some random integer<br />
IntegerToReal someInteger, var someReal<br />
		someReal = someInteger  (so if someInteger = 5, someReal = 5.0)<br />
RealToInteger someReal, var someInt<br />
		someInt = truncate someReal (so if someReal = 5.5, someInt = 5)<br />
  <br />
  
<b>Array Functions</b><br />
	Start var start<br />
		start = the first index of this array<br />
End var end<br />
	end = the last index of this array<br />
<br />

Future:<br />
Enums, Records<br />


<!--
 ```diff
- text in red
+ text in green
! text in orange
# text in gray
@@ text in purple (and bold)@@
```
--!>
