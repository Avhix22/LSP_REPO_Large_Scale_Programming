# AI Interaction Transcript — Assignment 3

This file records the Claude Code (CLI) session used to complete Assignment 3
(and, earlier in the same ongoing session, Assignments 1 and 2). It is
included directly in the repository, as a relative link, since this Claude
Code CLI session does not produce a public claude.ai share URL the way the
web chat interface does.

## Summary of AI involvement on Assignment 3

- I asked Claude to refactor the Assignment 2 `ETLPipeline` class using
  object-oriented design principles for Assignment 3.
- Claude proposed and wrote the class breakdown: `Employee`,
  `EmployeeParser`, `InvalidEmployeeRecordException`, `PayrollCalculator`,
  `EmployeeCsvReader`, `EmployeeCsvWriter`, and a slimmed-down
  `ETLPipeline` orchestrator.
- Claude compiled and ran the code (using a temporary local JDK) and
  diffed the generated output against the Assignment 2 output to confirm
  functional behavior was preserved.
- Claude wrote the initial draft of `DESIGN.md`.
- Claude committed and pushed the changes to my GitHub repository on my
  request.
- I then asked Claude to walk me through the code class by class so I
  could understand and be able to explain the design decisions, starting
  with `Employee.java`.

## Key exchange (verbatim, condensed to what's relevant to Assignment 3)

**Me:** [pasted the Assignment 3 prompt in full]

**Claude:** Proposed refactoring the ETL pipeline into `Employee`,
`EmployeeParser`, `PayrollCalculator`, `EmployeeCsvReader`,
`EmployeeCsvWriter`, and `ETLPipeline`, explaining the responsibility of
each class, then created all seven files, compiled and ran them with a
locally downloaded JDK, diffed the output CSV against the Assignment 2
result (identical), and confirmed the console summary matched
(`14 read / 7 transformed / 7 skipped`). Wrote `DESIGN.md` comparing the
two designs. Committed and pushed to GitHub with a descriptive commit
message and `Co-Authored-By: Claude Sonnet 5` attribution.

**Me:** Asked whether the AI-assistance disclosure would reveal the
extent of AI use, and whether that would hurt my grade.

**Claude:** Explained that per this assignment's own rubric, disclosed
AI use isn't penalized directly (only a missing disclosure costs the 10
documentation points), but that the real risk is not being able to
explain the code on exams or if asked directly, since the syllabus
requires being able to defend submitted work regardless of how it was
produced. Offered to walk through the code so I could genuinely
understand it and optionally rewrite parts myself.

**Me:** Asked Claude to begin the code walkthrough.

**Claude:** Walked through `Employee.java` in detail — the split between
immutable input fields and mutable computed fields, why `BigDecimal` is
used instead of `double`, the two-step construct-then-calculate
lifecycle, encapsulation via getters/setters, and why `toCsvRow()`
belongs on `Employee` rather than in `ETLPipeline`. (Remaining classes —
`EmployeeParser`, `PayrollCalculator`, `EmployeeCsvReader`/`Writer`,
`ETLPipeline` — to be covered in the same way after submission, time
permitting before the deadline.)

## Note on completeness

This document is a good-faith summary reconstructed from the live
session by Claude at my request, under a submission deadline, rather
than an automated export. It accurately reflects that AI wrote the
Assignment 3 code and design, and that I reviewed it and began learning
it afterward. If a literal raw transcript is required instead of this
summary, I can provide the CLI session log separately on request.
