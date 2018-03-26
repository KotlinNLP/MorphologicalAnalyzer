# Info about the test resources

## `LANGUAGE/test_TYPE.json`

Date-time tests are put in directories, one per language. The name of the directory is the language iso-a2 code.

In each directory there is one JSON file for each `TYPE` of `DateTime` to test, named `test_TYPE.json`.

Each JSON file contains a list of objects, each representing a test for the `DateTimeProcessor`.

All tests have the `text` field. Depending on the test `TYPE` they can contain adding specific fields.

The following types are supported:
* "null"
* "date"
* "time"
* "datetime"
* "offset"
* "date_offset"
* "date_ordinal"

### Common Fields

* `text`: the text that can contain a date-time expression and will be evaluated by the `DateTimeProcessor`.

#### The `TYPE` "null"

Tests of this type have not adding fields, given that their `text` does not contain any date-time in it.

#### Adding fields for the `TYPE` "date"

All the following fields are optional, but at least one of `D`, `week-D`, `M`, `Y` or `holiday` must be present. 

* `D`: the day as int number in the range [1, 31]
* `week-D`: the week day as int number in the range [1, 7]
* `M`: the month as int number in the range [1, 12]
* `Y`: the year as int number in the range [0, 9999]
* `Y-abbr`: a boolean indicating whether the year is intended as abbreviation
* `holiday`: a string indicating an holiday among the following: "Christmas", "ChristmasEve", "Easter"

#### Adding fields for the `TYPE` "time"

All the following fields are optional, but at least one of `h`, `m`, `s` or `generic` must be present. 

* `h`: the hours as int number in the range [0, 23]
* `m`: the minutes as int number in the range [0, 59]
* `s`: the seconds as int number in the range [0, 59]
* `ms`: the milliseconds as int number in the range [0, 999]
* `generic`: a generic time (one of the following values: "morning", "lunch", "afternoon", "evening", "night")
* `tz`: the timezone (e.g. "UTC")

#### Adding fields for the `TYPE` "datetime"

The following fields are required. 

* `date`: an inner "date" object
* `time`: an inner "time" object

Both contain the same adding fields of the test with `TYPE` equal to their name, and two fields more: 

* `start`: the index of the char at which it starts respect to the date-time start index
* `end`: the index of the char at which it ends respect to the date-time start index

#### Adding fields for the `TYPE` "offset"

The following fields are required. 

* `offset-type`: the type of the offset (possible values: "date", "hour", "min", "sec", "day", "week", "weekend", 
"month", "year")
* `units`: the units of the offset length as int (positive or negative)

If `offset-type` is "date" then the same adding fields of the test of `TYPE` "date" are required, and two fields more:

* `start`: the index of the char at which it starts respect to the offset start index
* `end`: the index of the char at which it ends respect to the offset start index

#### Adding fields for the `TYPE` "date_offset"

The following fields are required. 

* `date`: an inner "date" object
* `offset`: an inner "offset" object

Both contain the same adding fields of the test with `TYPE` equal to their name, and two fields more: 

* `start`: the index of the char at which it starts respect to the date-offset start index
* `end`: the index of the char at which it ends respect to the date-offset start index

#### Adding fields for the `TYPE` "date_ordinal"

The following fields are required. 

* `type`: the type of ordinal date (possible values: "date", "day", "week", "weekend", "month", "year")
* `ref`: the reference date-time object that can contains one of the following fields:
    * `date`: an inner "date" object that represents a week day (e.g. "Monday")
    * `offset`: an inner "offset" object

Both `ref.date` and `ref.offset` contain the same adding fields of the test with `TYPE` equal to their name, and two 
fields more: 

* `start`: the index of the char at which it starts respect to the date-offset start index
* `end`: the index of the char at which it ends respect to the date-offset start index

If `type` is "date" then the same adding fields of the test of `TYPE` "date" are required, and the same `start` and 
`end` adding fields.
