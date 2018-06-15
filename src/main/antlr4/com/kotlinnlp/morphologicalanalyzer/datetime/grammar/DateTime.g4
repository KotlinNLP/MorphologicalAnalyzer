grammar DateTime;

import LexerEN, Date, Time, DateTimeSimple, DateOrdinal, Offset, DateOffset;

// -----
// -- Input text
// -----

root : text_sep? text text_sep? EOF? ;

text           : text_chunk sep_text_chunk* ;
sep_text_chunk : text_sep text_chunk;
text_chunk     : datetime | string ;

text_sep : (WS | punct)+ ;

string : (~WS)+ ;
punct  : DOT | COMMA | COLON | SEMICOLON | APEX | DASH | SLASH | BACKSLASH | DEGREE | CIRCUMFLEX | OTHER_SYMBOLS ;

datetime : interval | single_datetime ;

// -----
// -- Intervals
// -----

interval
    : interval_from text_sep interval_to
    | interval_from
    | interval_to
    ;

interval_from : interval_expr_from text_sep interval_datetime_from ;
interval_to   : interval_expr_to text_sep interval_datetime_to ;

interval_expr_from : FROM | AFTER ;
interval_expr_to   : TO | AT | BEFORE ;

interval_datetime_from : single_datetime | interval_offset_from ;
interval_datetime_to   : single_datetime | interval_offset_to ;

interval_offset_from : offset_numerable ;
interval_offset_to   : offset_numerable ;

// -----
// -- Single date-times
// -----

single_datetime
    : date_ordinal
    | date_offset
    | offset
    | date_time_simple
    | time
    | date
    ;
