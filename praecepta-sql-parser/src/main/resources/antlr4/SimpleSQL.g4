grammar SimpleSQL;

sql
  : (statementQuery | ';')+ EOF
  ;

statementQuery
    : insert_statement
    | update_statement
    | select_statement
    | execute_statment
    | delete_statement
    ;

insert_statement
    : INSERT INTO tableIdentifier inputExpr
    ;
    
update_statement
    : UPDATE tableIdentifier inputExpr
    ;

execute_statment
    : EXECUTE tableIdentifier havingKeyValExpr executeExprIdentifier
    ;
    
delete_statement
    : DELETE tableIdentifier havingKeyValExpr
    ;        
	
tableIdentifier
    : identifier
    ;

inputExpr
    :  columnNames VALUES columnValues
    | WITH LEFT_PARENTHESIS jsonExpression RIGHT_PARENTHESIS
    ;

identifier
    : IDENTIFIER DOT IDENTIFIER DOT IDENTIFIER
    | IDENTIFIER DOT IDENTIFIER
    | IDENTIFIER
    ;	

columnNames
    : LEFT_PARENTHESIS identifier  (COMMA identifier)* RIGHT_PARENTHESIS
    ;
	

columnValues
    :  LEFT_PARENTHESIS constant (COMMA constant)* RIGHT_PARENTHESIS
    ;	

havingKeyValExpr 
    : HAVING havingConditionList
    ;

havingConditionList 
    : havingCondition (COMMA havingCondition)*
    ;
	
havingCondition
    : identifier compare expression
    ;

executeExprIdentifier 
    : WHERE executeExpressionList
    | WITH LEFT_PARENTHESIS jsonExpression RIGHT_PARENTHESIS
    ;
    
executeExpressionList
    : executeExpression (COMMA executeExpression)*
    ;
	
executeExpression
    : identifier compare expression
    ;            	

constant
    : NULL
    | identifier
    | (MINUS | PLUS)? INTEGER_VALUE
    | (MINUS | PLUS)? DECIMAL_VALUE
    | QUOTED_STRING+
    ;

jsonExpression
     :   jsonObject 
     |  jsonArr
     | xmlExpression
     ;

xmlExpression
     :'<' XMLATTR '>' (xmlContent)* '<' '/' XMLATTR '>'
     ;
    
xmlContent
    : XMLATTR
    | NUMBER
    | xmlExpression
    | 'true'
    | 'false'
    | 'null'
    ; 

 jsonObject
    : '{' pair (',' pair)* '}'
    | '{' '}'
    ;

pair
    : STRING ':' value
    ;

jsonArr
    : '[' value (',' value)* ']'
    | '[' ']'
    ;

value
    : STRING
    | NUMBER
    | jsonObject
    | jsonArr
    | 'true'
    | 'false'
    | 'null'
    ;

STRING
    : '"' (ESC | SAFECODEPOINT)* '"'
    ;

select_statement
    : SELECT expressionList FROM tableIdentifier (WHERE conditionExpression)?
    ;


expressionList
    : expression (COMMA expression)*
    ;
	
expression
    : ASTERISK
    | identifier
    | constant
    ;	

conditionExpression
    : identifier compare expression
    | conditionExpression AND conditionExpression
    | conditionExpression OR conditionExpression
    ;

compare:
    EQUALS | GT | GE| LT| LE | NE;



CREATE : 'CREATE' | 'create';
SELECT: 'SELECT' | 'select';
FROM: 'FROM' | 'from';
INSERT : 'INSERT' | 'insert';
UPDATE : 'UPDATE' | 'update';
EXECUTE : 'EXECUTE' | 'execute';
INTO : 'INTO' | 'into';
VALUES : 'VALUES' | 'values';
WHERE : 'WHERE' | 'where';
NULL : 'NULL' | 'null';
WITH : 'WITH' | 'with';
HAVING : 'HAVING' | 'having';
DELETE : 'DELETE'|'delete';


DOT: '.';
COMMA: ',';
ASTERISK: '*';
LEFT_PARENTHESIS: '(';
RIGHT_PARENTHESIS: ')';
EQUALS: '=';
NOT : '!';
MINUS : '-';
PLUS: '+';
GT: '>';
GE: '>=';
LT: '<';
LE: '<=';
NE: '!=';


AND: 'AND' | 'and' | '&&';
OR: 'OR' | 'or' | '||';


QUOTED_STRING
    : '\'' ( ~('\''|'\\') | ('\\' .) )* '\''
    | '"' ( ~('"'|'\\') | ('\\' .) )* '"'
    ;

INTEGER_VALUE
    : DIGIT+
    ;

DECIMAL_VALUE
    : DECIMAL_DIGITS
    ;

IDENTIFIER
    : (LETTER | DIGIT)+
    ;

XMLATTR 
    : ATTR+
    ;
    
fragment ATTR 
    : [a-zA-Z0-9.;@#$:_'\s-]
    ;

fragment DECIMAL_DIGITS
    : DIGIT+ '.' DIGIT*
    | '.' DIGIT+
    ;

fragment DIGIT
    : [0-9]
    ;

fragment LETTER
    : [a-zA-Z_0-9]
    ;

fragment ESC
    : '\\' (["\\/bfnrt] | UNICODE)
    ;

fragment UNICODE
    : 'u' HEX HEX HEX HEX
    ;

fragment HEX
    : [0-9a-fA-F]
    ;

fragment SAFECODEPOINT
    : ~ ["\\\u0000-\u001F]
    ;

NUMBER
    : '-'? INT ('.' [0-9]+)? EXP?
    ;

fragment INT
    // integer part forbis leading 0s (e.g. `01`)
    : '0'
    | [1-9] [0-9]*
    ;

// no leading zeros

fragment EXP
    // exponent number permits leading 0s (e.g. `1e01`)
    : [Ee] [+\-]? [0-9]+
    ;  

WS
    : [ \r\n\t]+ -> channel(HIDDEN)
    ;