## Morrigan programming language
Morrigan language is a simple programming language written on Java that was inspired by the book [Crafting Interpreters](https://craftinginterpreters.com/) by Robert Nystrom.
### Grammar
```
program ::= <statement>* EOF
statement ::= morrigan <printStatement> | <whileStatement> | <declarationStatement > | <ifStatement> [x <NUMBER>]
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