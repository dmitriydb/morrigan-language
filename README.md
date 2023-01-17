# What's that?
## Morrigan programming language
Morrigan language is a simple programming language interpreter written on Java that was inspired by the book [Crafting Interpreters](https://craftinginterpreters.com/) by Robert Nystrom.

### Code examples

#### Hello World
`morrigan remembers what is [Hello World].`

or

```
morrigan says that helloWorld is function() {
  morrigan remembers what is [Hello, world!]
}.
morrigan remembers what is helloWorld().
```

#### Factorial 
```
morrigan says that n is 5.
morrigan says that f is function(n) { 
    morrigan says that if n = 1 then morrigan returns 1 else morrigan returns n * f(n - 1)
}.
morrigan says that a is f(n). morrigan remembers what is a.
```
## Morrigan programming platform

Microservice backend for distributed execution of Morrigan language programs. 



## Platform architecture
![](https://files.catbox.moe/o8xify.png)

### Language grammar
```
program ::= <statement>* EOF
statement ::= morrigan <callStatement> | <returnStatement> | <printStatement> | <whileStatement> | <declarationStatement > | <ifStatement> [x <NUMBER>]
callStatement ::= calls <call>.
returnStatement ::= returns <expression>.
printStatement ::= remembers what is <expression>.
whileStatement ::= says that while <expression> do <statement group>.
ifStatement ::= says that if <expression> then <statement group> else <statement group>. 
declarationStatement ::= says that <assignmentGroup> | <functionDeclaration>.
assignmentGroup::= <assignmentStatement> [, <assignmentStatement>]*
assignmentStatement = <identifier> is <expression>
functionDeclaration ::= <identifier> is function “(” parameters? “)” “{” statementGroup “}”
parameters = IDENTIFIER (“,” IDENTIFIER)*  
statementGroup ::= <statement> [and <statement>]*
expression ::= equality | logicalExpression
equality ::= comparison ( ‘=’ comparison)* 
logicalExpression = equality ( ‘|’ | ‘&’ equality)+
comparison ::= term ( (’>’ | ‘<’) term)*
term ::= factor ( (’+’ | ‘-’ factor)* 
factor ::= unary ( (’*’ | ‘/’ unary)* 
unary ::= (’+’ | ‘-’) unary | call;
call ::= primary ( “(” arguments? “)” )*
arguments ::= expression (”,” expression )*
primary ::= NUMBER | STRING | <LITERAL STRING> | FALSE | TRUE;
literal string ::= “[” STRING “]”

```

### Platform stack
- Apache Kafka
- Redis
- Spring Boot, MVC, Data, Cloud
- Javelin
- Docker, Jenkins
