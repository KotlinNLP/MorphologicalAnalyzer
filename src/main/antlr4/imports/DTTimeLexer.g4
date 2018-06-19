lexer grammar DTTimeLexer;

import DTLetters;

TIME_H : H ;
TIME_T : T ;
TIME_Z : Z ;

TIME_SUFFIX
    : A DOT? M DOT?
    | P DOT? M DOT?
    ;

TIME_ZONE
    : U T C
    | G M T
    | C E T
    | C E S T
    | E D T
    | E E T
    | W S T
    | J S T
    | A S T
    | E S T
    | C S T
    | M D T
    | M S T
    | P S T
    ;