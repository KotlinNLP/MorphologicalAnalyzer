grammar Datetime;

import Dates, Times;

root : datetime ;

datetime : date | time | date time | time date ;
