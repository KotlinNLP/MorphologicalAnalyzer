lexer grammar NUMBaseLexer;

// -----
// -- Symbols
// -----

NOT_DEFINED : '\u0000' ; // used to define mandatory tokens, of a given language, that do not exist in that language
COMMA : ',' ;
DOT   : '.' ;
HYPEN : '-' ;

// -----
// -- Whitespace
// -----

EOL : [\r\n] ;
WS  : [ \t]+ ;

// -----
// -- Digits
// -----

D_0 : '0' ;
D_1 : '1' ;
D_2 : '2' ;
D_3 : '3' ;
D_4 : '4' ;
D_5 : '5' ;
D_6 : '6' ;
D_7 : '7' ;
D_8 : '8' ;
D_9 : '9' ;

// -----
// -- Letters
// -----

fragment A : [Aa] ;
fragment B : [Bb] ;
fragment C : [Cc] ;
fragment D : [Dd] ;
fragment E : [Ee] ;
fragment F : [Ff] ;
fragment G : [Gg] ;
fragment H : [Hh] ;
fragment I : [Ii] ;
fragment J : [Jj] ;
fragment K : [Kk] ;
fragment L : [Ll] ;
fragment M : [Mm] ;
fragment N : [Nn] ;
fragment O : [Oo] ;
fragment P : [Pp] ;
fragment Q : [Qq] ;
fragment R : [Rr] ;
fragment S : [Ss] ;
fragment T : [Tt] ;
fragment U : [Uu] ;
fragment V : [Vv] ;
fragment W : [Ww] ;
fragment X : [Xx] ;
fragment Y : [Yy] ;
fragment Z : [Zz] ;

fragment AA : [ÀÁÂàáâ] ;
fragment EE : [ÈÉËèéë] ;
fragment II : [ÌÍìí] ;
fragment OO : [ÒÓòó] ;
fragment UU : [ÙÚÛùúû] ;

// -----
// -- Other
// -----

WORDDIV: [<>()[\]{}!?|"“”‘#*+:;'’/\\°^%€£$&¥~] ;
ANY : . ;
