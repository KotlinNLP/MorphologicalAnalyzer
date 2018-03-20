# Info about the test resources

## `test_date_times.json`

A JSON file containing a list of objects, each representing a test for the `DateTimeProcessor`.

All tests have `text` and `type` fields. Depending on the value of `type` they can contain adding specific fields.

### Fields

* `text`: the text that can contain a date-time expression and will be evaluated by the `DateTimeProcessor`.

* `type`: represents the type of `DateTime` contained in the `text` and it can have one of the following
values: "date", "time", "datetime", "null".

#### The `type` "null"

Tests of this type have not adding fields, given that their `text` does not contain any date-time in it.

#### Adding fields for the `type` "date"

All the following fields are optional, but at least one of `D`, `week-D`, `M` or `Y` must be present. 

* `D`: the day as int number in the range [1, 31]
* `week-D`: the week day as int number in the range [1, 7]
* `M`: the month as int number in the range [1, 12]
* `Y`: the year as int number in the range [0, 9999]
* `Y-abbr`: a boolean indicating whether the year is intended as abbreviation

#### Adding fields for the `type` "time"

All the following fields are optional, but at least one must be present. 

* `h`: the hours as int number in the range [0, 23]
* `m`: the minutes as int number in the range [0, 59]
* `s`: the seconds as int number in the range [0, 59]
* `ms`: the milliseconds as int number in the range [0, 999]

#### Adding fields for the `type` "datetime"

All the following fields are required. 

* `date` 
* `time` 

Both contain the same adding fields of the test with `type` equal to their name, and two fields more: 

* `start`: the index of the char at which it starts in the `text`
* `end`: the index of the char at which it ends in the `text`
