lexer grammar Symbols;

SPACE_SEP    : [ \n]+ ;
OTHER_SPACES : [\r\t] ;

DOT       : '.' ;
COMMA     : ',' ;
COLON     : ':' ;
SEMICOLON : ';' ;
APEX      : ['’] ;
DASH      : '-' ;
SLASH     : '/' ;

OTHER_SYMBOLS : [<>()[\]{}!?|\\^"“”‘] ;
