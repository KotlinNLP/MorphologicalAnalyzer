grammar Datetime;

import Symbols, Dates, Times;

root : datetime EOL? | EOF;

datetime : date | time | date time | time date ;
