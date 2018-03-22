lexer grammar Symbols;

SPACE_SEP    : [ \n]+ ;
OTHER_SPACES : [\r\t] ;

DOT        : '.' ;
COMMA      : ',' ;
COLON      : ':' ;
SEMICOLON  : ';' ;
APEX       : ['’] ;
DASH       : '-' ;
SLASH      : '/' ;
DEGREE     : '°' ;
CIRCUMFLEX : '^' ;

NOT_DEFINED : '###???##??#?' ; // used to define mandatory tokens, of a given language, that do not exist in that language

OTHER_SYMBOLS : [<>()[\]{}!?|\\"“”‘#*+] ;
