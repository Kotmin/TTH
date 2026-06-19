# Alert Rule Engine

A Java 17 command-line application that processes integer sensor readings and produces alert labels based on configurable divisibility rules.

[![CI](https://github.com/Kotmin/TTH/actions/workflows/ci.yml/badge.svg)](https://github.com/Kotmin/TTH/actions/workflows/ci.yml)

---

## What it does

Given a list of integer values, the engine outputs a label for each value:

- Divisible by 3 → `LOW`
- Divisible by 5 → `ADVISORY`
- Divisible by both → `LOWADVISORY` (labels concatenated in rule order)
- Divisible by 7 (extended set) → `WARN`
- No rule matches → the original integer as a string

---

## Why it resembles FizzBuzz

The base rule set (3 → LOW, 5 → ADVISORY, both → LOWADVISORY) is structurally identical to FizzBuzz. The key insight from Stage 2 is that `LOWADVISORY` must not have a dedicated branch — it must emerge naturally from evaluating each rule independently and concatenating the matching labels.

---

## Design decisions

### Why a simple implementation exists

`SimpleAlertEngine` uses a plain loop with `StringBuilder`. It serves two purposes: it demonstrates the FizzBuzz-like nature of the problem clearly, and it acts as a baseline in tests to prove that the Strategy engine produces identical output for the base rule set.

### Why the Strategy pattern is used

Stage 3 of the task states: *"new alert rules will be added frequently — adding a new rule should NOT require modifying existing rules or the engine."* A plain `if`-chain satisfies Stage 1, but it requires editing the engine to add `WARN`. With the Strategy pattern, each rule is a self-contained object. The engine only iterates over its ordered list of rules and concatenates matching labels. Adding `WARN` (divisible by 7) means adding a `DivisibilityAlertRule(7, "WARN")` to the extended rule set — the engine does not change.

### Why the adapter exists

`AlertEngineProcessorAdapter` decouples the engine (which evaluates a single integer) from the application layer (which processes lists). This keeps the `AlertEngine` interface minimal and the CLI free from direct engine wiring.

---

## Rule sets

| Rule set | Rules |
|---|---|
| `base` (default) | LOW (÷3), ADVISORY (÷5) |
| `extended` | LOW (÷3), ADVISORY (÷5), WARN (÷7) |

---

## Running from a terminal (Java 17 required locally)

If Java 17 is installed, you can run without Docker using the Gradle wrapper:

```
$ ./gradlew run

1
2
LOW
4
ADVISORY
LOW
7
8
LOW
ADVISORY
11
LOW
13
14
LOWADVISORY
16
17
LOW
19
ADVISORY
```

```
$ ./gradlew run --args="--rules=extended --values=21,35,105"

LOWWARN
ADVISORYWARN
LOWADVISORYWARN
```

```
$ ./gradlew run --args="--rules=extended --values=63,210,-21"

LOWWARN
LOWADVISORYWARN
LOWWARN
```

---

## Local workflow (Docker only)

All build, test, and run operations use Docker. No local Java or Gradle installation is required.

### Run tests

```bash
docker build --target test -t alert-rule-engine:test .
```

### Build the runnable image

```bash
docker build -t alert-rule-engine:local .
```

### Run default output (values 1–20, base rules)

```bash
docker run --rm alert-rule-engine:local
```

Expected output:
```
1
2
LOW
4
ADVISORY
LOW
7
8
LOW
ADVISORY
11
LOW
13
14
LOWADVISORY
16
17
LOW
19
ADVISORY
```

### Run extended rule demonstration

```bash
docker run --rm alert-rule-engine:local --rules=extended --values=21,35,105
```

Expected output:
```
LOWWARN
ADVISORYWARN
LOWADVISORYWARN
```

### Override both rule set and values

```bash
docker run --rm alert-rule-engine:local --rules=extended --values=1,3,7,21,35,105
```

---

## GitHub Actions CI

The CI workflow runs on every push and pull request to `main` and `dev`, and can also be triggered manually via workflow dispatch.

It uses:
- Java 17 (Eclipse Temurin)
- Gradle dependency cache
- `./gradlew test --no-daemon`

---

## Commit structure

Each original task stage has its own commit following the [Conventional Commits](https://www.conventionalcommits.org/) specification. The git history shows the progression from a basic if-chain (Stage 1) through refactoring (Stage 2), extensible design (Stage 3), and the WARN rule demonstration (Stage 4).
