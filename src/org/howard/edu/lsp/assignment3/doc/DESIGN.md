# Assignment 3 – Design Discussion

**Name:** Avion Hicks

## How was the Assignment #2 solution organized?
Assignment #2 was a single class, `ETLPipeline`, containing everything: file
reading, CSV field parsing and validation, the payroll math (regular pay,
overtime, IT bonus), pay-level/employment-status rules, output formatting,
CSV writing, and the run summary. All logic lived in `main` and a handful of
`private static` helper methods that passed primitive values (`BigDecimal`,
`String`) back and forth. There was no object representing an employee —
a valid row was just a formatted `String` built directly inside
`transformRow`.

## What design changes were made for Assignment #3?
The same behavior is now split across six classes, each with one job:

- **`Employee`** – a data object holding one employee's fields (id, name,
  department, hours, rate, and the computed gross pay / pay level /
  employment status). It also knows how to render itself as an output CSV
  row (`toCsvRow()`), since formatting a record is the record's own
  concern.
- **`EmployeeParser`** – turns one raw CSV line into an `Employee`,
  handling trimming, field-count checking, numeric parsing, and the
  non-negative check. Throws `InvalidEmployeeRecordException` for anything
  that must be skipped, instead of returning `null` or a sentinel value.
- **`InvalidEmployeeRecordException`** – a small custom checked exception
  that makes "this row is invalid" an explicit, named outcome the caller
  must handle, rather than an implicit `null` check.
- **`PayrollCalculator`** – applies the payroll business rules (overtime,
  IT bonus, rounding, pay level, employment status) to an already-valid
  `Employee`.
- **`EmployeeCsvReader`** / **`EmployeeCsvWriter`** – extraction and load
  only: reading raw lines from the input file and writing finished
  `Employee` rows to the output file. Neither knows anything about payroll
  rules.
- **`ETLPipeline`** – now just orchestration. It wires the other five
  objects together, loops over the raw lines, counts read/transformed/
  skipped, and prints the summary. It contains no parsing or calculation
  logic itself.

## What classes/abstractions were introduced, and why?
`Employee` was the key abstraction missing from Assignment #2 — there was
no object representing "one employee record," only loose variables passed
between static methods. Making it a real class let each stage of the
pipeline operate on a meaningful object instead of a bag of primitives, and
let output formatting live next to the data it formats.

`InvalidEmployeeRecordException` replaced the `null`-return convention from
Assignment #2's `transformRow`. Using an exception makes an invalid row an
explicit control-flow branch at the call site in `ETLPipeline`, rather than
an implicit `if (result == null)` check that's easy to forget.

`EmployeeCsvReader` and `EmployeeCsvWriter` were split into their own
classes to isolate file I/O from business logic — the extract and load
steps of the pipeline should be swappable (e.g., a different file format)
without touching parsing or payroll rules.

## How were responsibilities divided differently?
Assignment #2 had one class doing extraction, validation, calculation, and
loading all at once. Assignment #3 follows single-responsibility separation
along the ETL steps themselves:

- Extract → `EmployeeCsvReader`
- Validate/normalize → `EmployeeParser`
- Transform (business rules) → `PayrollCalculator`
- Load → `EmployeeCsvWriter`
- Orchestrate → `ETLPipeline`

`Employee` sits underneath all of them as the shared domain object each
step reads from or writes to.

## Why is this an improvement?
- **Single responsibility:** each class can be understood, tested, and
  changed on its own. Changing the payroll formula only touches
  `PayrollCalculator`; changing the output format only touches
  `Employee.toCsvRow()` or `EmployeeCsvWriter`.
- **Encapsulation:** `Employee` owns its own data and its own CSV
  representation, instead of an external method reaching in and building a
  formatted string from loose fields.
- **Explicit error handling:** `InvalidEmployeeRecordException` names the
  "skip this row" case instead of relying on a `null` convention.
- **Testability:** `EmployeeParser` and `PayrollCalculator` can each be
  exercised directly with sample input, independent of file I/O.

Functionally, Assignment #3 is a refactor, not a rewrite: the CSV output
and console run summary are identical to Assignment #2 for the grading
dataset (verified by diffing both outputs).

## AI and Internet Resources
This assignment was completed with the assistance of Claude (Claude Code),
an AI coding assistant, which helped design the class breakdown described
above and write/verify the code and this document.

Transcript: **[link pending — add before submitting]**

No other Internet resources were used beyond the Java standard library
documentation.

---

I acknowledge that I have read the course syllabus and understand that I am responsible for complying with all course policies, assignment requirements, and monitoring Piazza throughout the semester.
