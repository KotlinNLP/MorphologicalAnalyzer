lexer grammar Symbols;

WS : [ \n\r\t]+ ;

DOT        : '.' ;
COMMA      : ',' ;
COLON      : ':' ;
SEMICOLON  : ';' ;
APEX       : ['’] ;
DASH       : '-' ;
SLASH      : '/' ;
BACKSLASH  : '\\' ;
DEGREE     : '°' ;
CIRCUMFLEX : '^' ;

NOT_DEFINED : '###???##??#?' ; // used to define mandatory tokens, of a given language, that do not exist in that language

OTHER_SYMBOLS : [<>()[\]{}!?|"“”‘#*+] ;
