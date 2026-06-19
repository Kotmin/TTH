# AI Reflection

## 1. AI tools used

- **GPT-5.5** (ChatGPT web GUI) — for initial domain modelling and event storming
- **Claude Sonnet 4.6** (`claude-sonnet-4-6`, Claude Code CLI) — for implementation, guided by the spec

---

## 2. Workflow — Spec-Driven Development

This task was treated as an exercise in **Spec-Driven Development (SDD)**: the specification was produced and locked before any implementation code was written. The AI tools played different roles at each stage.

### Stage A — Event storming with GPT-5.5

Before writing a single line of Java, a short event storming session with GPT-5.5 was used to answer:

- What are the core domain events? (*sensor reading received → alert label emitted*)
- What stages should the implementation progress through?
- Which architectural pattern fits extensibility better — a simple if-chain or a Strategy?
- What are the meaningful edge cases (zero, negatives, combined labels)?

The output of this session was a set of decisions: four staged increments, two engine variants (simple baseline + strategy), a factory abstraction, and a list of edge cases worth testing.

### Stage B — PRD and constraint authoring (human)

From the event storming decisions, a Product Requirements Document was written in `.ai/spec/PRD.md`. It includes:

- Functional requirements per stage with explicit input/output tables
- Architecture diagram and package structure
- A 16-commit plan using Conventional Commits
- Docker multi-stage build requirements
- CI requirements (push/PR triggers, workflow_dispatch, linter before tests)
- Test strategy: 2–3 happy-path tests and 4–8 focused edge cases
- Definition of Done checklist

Four explicit constraints were also added to `CLAUDE.md` and `AGENTS.md`:
1. Local workflow uses Docker only — no host Java or Gradle
2. All commit messages follow Conventional Commits 1.0.0
3. Every logical unit of behaviour must have focused JUnit 5 unit tests
4. No code comments unless they serve a genuine documentation purpose

### Stage C — Plan mode review with Claude Sonnet 4.6

Claude Code was invoked in **plan mode**. The plan agent:

- Explored the repository (existing workflows, gitignore, directory layout)
- Asked three clarifying questions before writing any code: Gradle DSL (Kotlin vs Groovy), Dockerfile structure (one multi-stage file vs two separate files), and Java package name
- Produced a blocking dependency graph showing which tasks had to precede which
- Wrote a numbered commit sequence

The plan was reviewed and approved before any implementation began.

### Stage D — Implementation by Claude Sonnet 4.6

After plan approval, Claude Code executed all commits in order: Stages 1–4, interfaces, adapter, factory, Dockerfile, CI, README, and this file. The implementation followed the PRD spec exactly — it was not a free-form generation.

Later enhancement rounds added: Spotless linter (Palantir Java Format), PIT mutation testing workflow, Node 24 action upgrades, tricky edge-case tests, and the terminal I/O examples in this README.

A subsequent refactoring session introduced the `AlertCondition + AlertEffect + AlertResult` model. This was proposed in plan mode: instead of each `AlertRule` returning a fixed label string, rules now describe an *effect* applied to a mutable `AlertResult` accumulator. The practical additions are `AlertEffect.removeLastCharacter()`, `AlertEffect.noOp()`, and composable `AlertCondition.and() / or() / not()` combinators — none of which fit the label-only model cleanly. The engine loop itself (`StrategyAlertEngine`) did not change at all, which is the intended property of this design: the engine is open for extension without modification. The `engine/strategy/` subpackage was removed and all types moved to `engine/` directly, since the strategy concept is now expressed by `ConditionalAlertRule` holding a condition and an effect rather than by a subpackage name.

---

## 3. One AI suggestion I modified or rejected

**What GPT-5.5 suggested:**

During event storming, GPT-5.5 proposed making the rule sets configurable through external YAML or JSON files loaded at startup, so rules could be changed without recompiling.

**Why I rejected it:**

The task is explicitly scoped to 2–6 hours with "plain Java, no frameworks required." Adding YAML parsing would require an external library, a configuration schema, and error handling for malformed files — none of which is justified for a four-stage take-home exercise. Rule configuration stayed as plain Java constants in `RuleSetFactory`, which is easier to review, test, and understand for the intended audience.
