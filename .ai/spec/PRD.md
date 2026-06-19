# PRD: Alert Rule Engine Take-Home Implementation Plan

## 1. Document Metadata

- **Project:** Alert Rule Engine
- **Source task:** `docs/TASK_TAKE_HOME.md`
- **Target role:** Junior Java Developer
- **Primary stack:** Java 17+, Maven, JUnit 5, Docker, GitHub Actions
- **Document type:** Product Requirements Document + implementation task plan
- **Main goal:** Deliver a clean, testable, Docker-runnable Java solution with clear git progression and README explanation.
- **Important constraint:** The original task expects visible staged progress through git commits.

---

## 2. Safety and Submission Risk Assessment

### 2.1 Safety assessment

The assignment is safe to implement. It is a deterministic Java coding exercise similar to FizzBuzz:

- No network access is required.
- No credentials, secrets, tokens, or user data are required.
- No privileged OS operations are required.
- No database, filesystem mutation, external APIs, or cloud resources are required.
- Docker is used only to provide a repeatable local Java/Maven environment.
- GitHub Actions is used only to run unit tests and verify the build.

### 2.2 Technical risks

| Risk | Impact | Mitigation |
|---|---:|---|
| Overengineering the solution | Medium | Keep Strategy implementation small and explain it as a deliberate extensibility demonstration. |
| Final Stage 4 rule changes output for values 1-20 | High | Support two rule sets: `base` for 3/5 rules and `extended` for 3/5/7 rules. The default run should preserve the original 1-20 output. |
| Missing commit-by-stage requirement | High | Plan atomic commits before implementation and do not squash them before submission. |
| Missing `AI_REFLECTION.md` at repository root | High | Add it explicitly as a final documentation task. |
| Missing build system | Critical | Use Maven with `pom.xml`. |
| Too many tests for a small task | Medium | Keep tests focused: 2-3 happy-path tests and 4-8 meaningful edge cases. |
| Docker/CI drift | Medium | Use the same Maven command locally and in CI: `mvn -B verify`. |

### 2.3 AI usage safety

Using AI is allowed by the task, but must be disclosed honestly in `AI_REFLECTION.md`.

Recommended approach:

- Mention that AI was used for planning, PRD, test strategy, or code review.
- Do not claim the code was written entirely manually if AI helped.
- Include one useful AI interaction and one suggestion that was rejected or modified.
- Keep final code personally reviewed and understood.

---

## 3. Problem Statement

Build a Java command-line program that processes integer sensor measurements and outputs alert labels according to divisibility rules.

Base rules:

- Divisible by 3 → `LOW`
- Divisible by 5 → `ADVISORY`
- Divisible by both 3 and 5 → labels are concatenated in rule order, producing `LOWADVISORY`
- No matching rule → original numeric value as a string

Extended demonstration rule:

- Divisible by 7 → `WARN`

When several rules match, labels are concatenated in rule order.

Examples for the extended rule set:

- `21` → `LOWWARN`
- `35` → `ADVISORYWARN`
- `105` → `LOWADVISORYWARN`

---

## 4. Goals

1. Implement the base alert rules correctly.
2. Refactor from a simple implementation to a more extensible Strategy-based implementation.
3. Keep a simple implementation as a baseline/reference variant.
4. Provide a common adapter so both implementations can be tested and used through one interface.
5. Add unit tests without over-testing.
6. Run tests in GitHub Actions CI.
7. Make local build/test/run possible only through Docker.
8. Explain the chosen approach clearly in `README.md`.
9. Preserve staged, atomic git history using Conventional Commits.
10. Include a root-level `AI_REFLECTION.md`.

---

## 5. Non-Goals

The solution should not include:

- Spring Boot or any web framework.
- AWS services.
- Databases.
- REST APIs.
- Complex plugin loading.
- Runtime configuration files unless they simplify CLI usage.
- Excessive abstraction beyond what is useful for the take-home task.
- Large test matrices or property-based testing.

---

## 6. Recommended Implementation Approach

### 6.1 Why two implementations?

The task naturally starts as a FizzBuzz-style exercise. A simple implementation is useful because it shows the basic behavior clearly.

However, Stage 3 asks for extensibility:

> New alert rules will be added frequently in the future.

That is a good justification for the Strategy pattern. Each rule becomes a self-contained strategy. The engine does not need to know what each rule means; it only asks each rule whether it matches and concatenates matching labels in a predictable order.

Therefore, the final project should keep:

1. **Simple implementation**
   - Good for demonstrating the initial FizzBuzz-like logic.
   - Useful as a baseline in tests.
   - Not the preferred extensibility model.

2. **Strategy implementation**
   - Preferred/default production-style implementation.
   - Open for new rules without modifying the engine.
   - Clear fit for Stage 3 and Stage 4.

### 6.2 Architecture overview

```text
CLI Main
  |
  v
MeasurementProcessor
  |
  v
AlertEngineProcessorAdapter
  |
  v
AlertEngine interface
  |-----------------------------|
  |                             |
  v                             v
SimpleAlertEngine          StrategyAlertEngine
                                |
                                v
                            AlertRule[]
                                |
                                v
                   DivisibilityAlertRule(3, "LOW")
                   DivisibilityAlertRule(5, "ADVISORY")
                   DivisibilityAlertRule(7, "WARN")
```

### 6.3 Proposed package structure

```text
src/
  main/
    java/
      com/example/alertrules/
        Main.java
        app/
          MeasurementProcessor.java
          AlertEngineProcessorAdapter.java
          AlertApplication.java
        engine/
          AlertEngine.java
          RuleSet.java
        engine/simple/
          SimpleAlertEngine.java
        engine/strategy/
          AlertRule.java
          DivisibilityAlertRule.java
          StrategyAlertEngine.java
        config/
          AlertEngineFactory.java
          RuleSetFactory.java
  test/
    java/
      com/example/alertrules/
        engine/simple/
          SimpleAlertEngineTest.java
        engine/strategy/
          StrategyAlertEngineTest.java
        app/
          AlertEngineProcessorAdapterTest.java
        config/
          AlertEngineFactoryTest.java
```

Package name may be changed to match the GitHub username or repository name, for example:

```text
pl.hotor22.alertrules
```

---

## 7. Functional Requirements

### FR-1: Base alert evaluation

The application must evaluate values using the base rules:

| Input | Output |
|---:|---|
| 3 | `LOW` |
| 5 | `ADVISORY` |
| 15 | `LOWADVISORY` |
| 1 | `1` |

### FR-2: Natural combination

The combined output must emerge from concatenating matched rule labels in order.

Required behavior:

```text
3-rule match + 5-rule match = LOWADVISORY
```

Avoid a dedicated combined-case branch such as:

```java
if (value % 15 == 0) {
    return "LOWADVISORY";
}
```

### FR-3: Extensible rule model

The Strategy implementation must support any number of rules.

Adding the `WARN` rule should be done by adding/configuring a new rule object, not by modifying the engine loop.

### FR-4: Predictable rule order

Rules must be evaluated in the order they are supplied.

Recommended default orders:

Base rule set:

```text
LOW, ADVISORY
```

Extended rule set:

```text
LOW, ADVISORY, WARN
```

### FR-5: Default CLI behavior

The default command should print the Stage 1 expected output for values `1..20` using the base rule set.

This avoids ambiguity with the Stage 4 `WARN` rule, because adding `WARN` would otherwise change outputs for `7` and `14`.

### FR-6: Extended CLI behavior

The application should provide a simple way to demonstrate the Stage 4 rule.

Acceptable options:

```text
--rules=extended
```

or:

```text
--demo=extended
```

The README must show how to verify:

```text
21 -> LOWWARN
35 -> ADVISORYWARN
105 -> LOWADVISORYWARN
```

### FR-7: Local execution through Docker

The README should instruct running and testing locally through Docker only.

Required commands should be similar to:

```bash
docker build --target test -t alert-rule-engine:test .
docker build -t alert-rule-engine:local .
docker run --rm alert-rule-engine:local
docker run --rm alert-rule-engine:local --rules=extended --values=21,35,105
```

Exact command syntax may differ depending on CLI implementation.

---

## 8. Non-Functional Requirements

### NFR-1: Java version

Use Java 17 or higher.

### NFR-2: Build tool

Use Maven.

Recommended command:

```bash
mvn -B verify
```

### NFR-3: Test framework

Use JUnit 5.

### NFR-4: Code quality

Code should be:

- simple,
- readable,
- immutable where practical,
- deterministic,
- free from unnecessary dependencies,
- easy to review for a Junior Java take-home submission.

### NFR-5: Docker quality

Use a multi-stage Dockerfile:

- build/test stage with Maven and JDK,
- runtime stage with JRE if packaging a runnable jar,
- no unnecessary packages,
- `.dockerignore` to avoid copying `target/`, `.git/`, IDE files, and local artifacts.

### NFR-6: CI quality

GitHub Actions should run at least:

```bash
mvn -B verify
```

on:

- pull requests to `main`,
- pushes to `main`.

Optional but useful:

- Maven dependency cache,
- Java 17 setup,
- CI badge in README.

---

## 9. Testing Strategy

Keep testing intentionally small but meaningful.

### 9.1 Happy-path tests

Recommended 2-3 happy-path tests:

1. `baseRuleSetProducesExpectedOutputForOneToTwenty`
   - Verifies Stage 1 output exactly.

2. `extendedRuleSetCombinesWarnRule`
   - Verifies:
     - `21 -> LOWWARN`
     - `35 -> ADVISORYWARN`
     - `105 -> LOWADVISORYWARN`

3. `simpleAndStrategyEnginesProduceSameBaseOutput`
   - Confirms both implementation variants agree for base rules.

### 9.2 Edge-case tests

Recommended 4-8 edge tests:

1. `nonMatchingValueReturnsOriginalNumber`
   - Example: `1 -> "1"`

2. `zeroMatchesAllDivisibilityRules`
   - Base: `0 -> LOWADVISORY`
   - Extended: `0 -> LOWADVISORYWARN`
   - This is mathematically consistent because `0 % n == 0` for non-zero divisors in Java.

3. `negativeValueCanMatchRule`
   - Example: `-3 -> LOW`

4. `emptyRuleListReturnsOriginalNumber`
   - Strategy engine with no rules should return `"value"`.

5. `ruleOrderIsPreserved`
   - Given rules `[WARN, LOW]`, `21 -> WARNLOW`.

6. `divisibilityRuleRejectsZeroDivisor`
   - Creating `DivisibilityAlertRule(0, "INVALID")` should throw `IllegalArgumentException`.

7. `adapterProcessesListInInputOrder`
   - Input list order must equal output list order.

8. `factoryRejectsUnknownEngineName`
   - Invalid CLI/factory option should produce a clear error.

---

## 10. README Requirements

`README.md` should explain:

1. What the project does.
2. Why this is FizzBuzz-like.
3. Why the first/simple implementation exists.
4. Why the Strategy pattern is used despite feeling slightly overengineered.
5. How the adapter keeps CLI/list processing independent from the engine implementation.
6. How to run tests locally using Docker.
7. How to run the app locally using Docker.
8. How to run base and extended rule demonstrations.
9. How GitHub Actions CI verifies the project.
10. How commits are structured.

Recommended README explanation:

```markdown
The task is intentionally small, so a plain `if`-based solution would be enough for the initial rules.
However, Stage 3 explicitly says that new rules will be added frequently and that existing rules or the engine should not need modification.
For that reason, the final implementation uses the Strategy pattern: every alert rule is a small object that knows how to match a value and what label to return.
The engine only iterates over ordered rules and concatenates labels, so adding `WARN` for divisibility by 7 requires adding a rule, not changing engine logic.

A simple implementation is kept as a baseline because it makes the FizzBuzz nature of the task easy to understand and allows tests to prove that the Strategy implementation preserves behavior.
```

---

## 11. GitHub Actions CI Requirements

Create:

```text
.github/workflows/ci.yml
```

Recommended workflow:

```yaml
name: CI

on:
  push:
    branches: [ "main" ]
  pull_request:
    branches: [ "main" ]

jobs:
  test:
    name: Build and test
    runs-on: ubuntu-latest

    steps:
      - name: Checkout
        uses: actions/checkout@v4

      - name: Set up Java
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: "17"
          cache: maven

      - name: Verify with Maven
        run: mvn -B verify
```

---

## 12. Docker Requirements

Create:

```text
Dockerfile
.dockerignore
```

### 12.1 Recommended Dockerfile behavior

The Dockerfile should support:

1. Building and testing:
   ```bash
   docker build --target test -t alert-rule-engine:test .
   ```

2. Building runnable image:
   ```bash
   docker build -t alert-rule-engine:local .
   ```

3. Running default Stage 1 output:
   ```bash
   docker run --rm alert-rule-engine:local
   ```

4. Running Stage 4 demonstration:
   ```bash
   docker run --rm alert-rule-engine:local --rules=extended --values=21,35,105
   ```

### 12.2 Recommended `.dockerignore`

```gitignore
.git
.github
.idea
.vscode
target
*.iml
.DS_Store
README.md
AI_REFLECTION.md
PRD.md
```

Do not ignore files needed during Docker build, such as `pom.xml` or `src/`.

---

## 13. Commit Plan Using Conventional Commits

Use small, atomic commits. At minimum, keep one commit per original task stage.

Recommended commit sequence:

1. `chore(project): initialize Maven Java 17 project`
2. `feat(stage-1): implement basic alert output`
3. `test(stage-1): verify output for values one to twenty`
4. `refactor(stage-2): combine matching labels without special case`
5. `test(stage-2): cover combined low advisory output`
6. `feat(stage-3): introduce alert engine abstraction`
7. `feat(stage-3): add strategy based rule engine`
8. `feat(app): add processor adapter for list inputs`
9. `test(app): verify adapter preserves input order`
10. `test(stage-3): verify simple and strategy engines match base rules`
11. `feat(stage-4): add warn rule to extended rule set`
12. `test(stage-4): verify extended warn combinations`
13. `chore(docker): add Dockerfile for local build and run`
14. `ci(github): run Maven verify on push and pull request`
15. `docs(readme): explain design and Docker usage`
16. `docs(ai): add AI usage reflection`

If time is limited, some adjacent commits can be combined, but keep the original stages visible.

---

## 14. Implementation Task Breakdown

### Epic 1: Project setup

#### Task 1.1: Initialize repository

Acceptance criteria:

- Maven project exists.
- Java version is set to 17.
- Project compiles with `mvn -B verify`.
- `.gitignore` exists.
- Initial commit is created.

Suggested commit:

```text
chore(project): initialize Maven Java 17 project
```

#### Task 1.2: Add base CLI skeleton

Acceptance criteria:

- `Main` class exists.
- Program can print output line by line.
- No business logic complexity yet.

Suggested commit:

```text
chore(app): add command line entry point
```

---

### Epic 2: Stage 1 basic implementation

#### Task 2.1: Implement basic rules

Acceptance criteria:

- Values divisible by 3 produce `LOW`.
- Values divisible by 5 produce `ADVISORY`.
- Values divisible by both produce `LOWADVISORY`.
- Other values return the number.

Suggested commit:

```text
feat(stage-1): implement basic alert output
```

#### Task 2.2: Test Stage 1 output

Acceptance criteria:

- Unit test verifies exact output for values `1..20`.

Suggested commit:

```text
test(stage-1): verify output for values one to twenty
```

---

### Epic 3: Stage 2 remove special combined branch

#### Task 3.1: Refactor combination logic

Acceptance criteria:

- No dedicated branch for divisible by both 3 and 5.
- `LOWADVISORY` is produced by concatenating `LOW` and `ADVISORY`.
- Stage 1 output remains unchanged.

Suggested commit:

```text
refactor(stage-2): combine matching labels without special case
```

#### Task 3.2: Add focused combination test

Acceptance criteria:

- Test proves `15 -> LOWADVISORY`.
- Test name explains that output emerges from rule combination.

Suggested commit:

```text
test(stage-2): cover combined low advisory output
```

---

### Epic 4: Stage 3 extensibility with Strategy pattern

#### Task 4.1: Introduce core interfaces

Acceptance criteria:

- `AlertEngine` interface exists.
- `AlertRule` interface exists.
- Engine logic depends on abstractions, not hardcoded rule classes.

Suggested commit:

```text
feat(stage-3): introduce alert rule abstractions
```

#### Task 4.2: Implement Strategy engine

Acceptance criteria:

- `StrategyAlertEngine` accepts ordered rules.
- It concatenates matching labels.
- It returns the original value if no rules match.
- Adding a new rule does not modify the engine.

Suggested commit:

```text
feat(stage-3): add strategy based rule engine
```

#### Task 4.3: Implement divisibility rule strategy

Acceptance criteria:

- `DivisibilityAlertRule` supports divisor and label.
- Constructor rejects divisor `0`.
- Rule is immutable.

Suggested commit:

```text
feat(stage-3): add divisibility alert rule
```

#### Task 4.4: Keep simple implementation

Acceptance criteria:

- `SimpleAlertEngine` remains available.
- It implements the same `AlertEngine` interface.
- It is used only as baseline/reference unless selected explicitly.

Suggested commit:

```text
refactor(engine): align simple engine with alert engine interface
```

---

### Epic 5: Adapter and application layer

#### Task 5.1: Add processor adapter

Acceptance criteria:

- `MeasurementProcessor` processes `List<Integer>` into `List<String>`.
- `AlertEngineProcessorAdapter` delegates single-value evaluation to `AlertEngine`.
- The adapter preserves input order.

Suggested commit:

```text
feat(app): add alert engine processor adapter
```

#### Task 5.2: Test adapter

Acceptance criteria:

- Unit test verifies list input/output behavior.
- Unit test verifies order preservation.

Suggested commit:

```text
test(app): verify processor adapter behavior
```

#### Task 5.3: Add engine/rule-set factory

Acceptance criteria:

- Base rule set contains 3 and 5 rules.
- Extended rule set contains 3, 5, and 7 rules.
- Unknown engine or rule-set names fail clearly.

Suggested commit:

```text
feat(config): add engine and rule set factory
```

---

### Epic 6: Stage 4 WARN rule

#### Task 6.1: Add extended `WARN` rule

Acceptance criteria:

- Divisible by 7 produces `WARN` in extended rule set.
- `21 -> LOWWARN`.
- `35 -> ADVISORYWARN`.
- `105 -> LOWADVISORYWARN`.

Suggested commit:

```text
feat(stage-4): add warn rule to extended rule set
```

#### Task 6.2: Test extended rule combinations

Acceptance criteria:

- Unit test covers `21`, `35`, and `105`.
- Test verifies rule-order concatenation.

Suggested commit:

```text
test(stage-4): verify warn rule combinations
```

---

### Epic 7: Docker local workflow

#### Task 7.1: Add Dockerfile

Acceptance criteria:

- Docker image can run tests.
- Docker image can run the app.
- Local README commands use Docker only.

Suggested commit:

```text
chore(docker): add Dockerfile for local build and run
```

#### Task 7.2: Add `.dockerignore`

Acceptance criteria:

- Build context excludes local build outputs and IDE files.
- Required source files are not excluded.

Suggested commit:

```text
chore(docker): reduce Docker build context
```

---

### Epic 8: CI workflow

#### Task 8.1: Add GitHub Actions workflow

Acceptance criteria:

- Workflow runs on push and PR to main.
- Workflow uses Java 17.
- Workflow runs `mvn -B verify`.
- Unit tests are executed in CI.

Suggested commit:

```text
ci(github): run Maven verify on push and pull request
```

---

### Epic 9: Documentation

#### Task 9.1: Write README

Acceptance criteria:

- Explains the task.
- Explains FizzBuzz-like nature.
- Explains why Strategy pattern was chosen.
- Explains why simple implementation remains.
- Explains adapter responsibility.
- Shows Docker build/test/run commands.
- Shows base and extended examples.
- Mentions GitHub Actions CI.

Suggested commit:

```text
docs(readme): explain implementation approach
```

#### Task 9.2: Add AI reflection

Acceptance criteria:

- `AI_REFLECTION.md` is at repository root.
- Answers all required questions.
- Is honest and specific.
- Mentions at least one AI suggestion modified or rejected.

Suggested commit:

```text
docs(ai): add ai usage reflection
```

---

## 15. Definition of Done

The project is ready for submission when:

- [ ] Maven project exists and uses Java 17+.
- [ ] Application runs from command line.
- [ ] Default output for `1..20` matches Stage 1 expected output.
- [ ] Stage 2 has no dedicated combined-case branch.
- [ ] Strategy implementation supports ordered, extensible rules.
- [ ] `WARN` rule exists in an extended rule set.
- [ ] `21`, `35`, and `105` extended outputs are correct.
- [ ] Simple and Strategy implementations are both present.
- [ ] Adapter exists and is unit-tested.
- [ ] Unit tests are focused and meaningful.
- [ ] GitHub Actions runs unit tests.
- [ ] Dockerfile exists.
- [ ] Local build/test/run instructions use Docker.
- [ ] README explains the design trade-off.
- [ ] `AI_REFLECTION.md` exists at repository root.
- [ ] Git history shows staged progression.
- [ ] Commit messages follow Conventional Commits.
- [ ] Repository can be shared as GitHub/GitLab link or ZIP with `.git` folder.

---

## 16. GitHub Issue Template

Use this as a GitHub issue body for tracking the take-home submission.

```markdown
# Take-Home Submission: Alert Rule Engine

## Goal

Implement the Alert Rule Engine take-home task in Java 17+ with Maven, focused tests, Docker-based local workflow, GitHub Actions CI, clean README documentation, and honest AI reflection.

## Scope

- Java 17+
- Maven
- Plain Java, no Spring
- Simple implementation variant
- Strategy pattern implementation variant
- Adapter for list processing
- Unit tests
- Dockerfile for local build/test/run
- GitHub Actions CI
- README explanation
- AI_REFLECTION.md at repository root
- Conventional Commits

## Implementation Checklist

### Stage 1: Basic implementation

- [ ] Implement LOW rule for values divisible by 3
- [ ] Implement ADVISORY rule for values divisible by 5
- [ ] Implement LOWADVISORY behavior
- [ ] Print expected output for values 1 through 20
- [ ] Commit Stage 1 work

### Stage 2: Remove special combined case

- [ ] Remove dedicated divisible-by-15 branch
- [ ] Produce LOWADVISORY by concatenating matching rule labels
- [ ] Keep output for 1 through 20 unchanged
- [ ] Commit Stage 2 work

### Stage 3: Design for extensibility

- [ ] Add AlertEngine abstraction
- [ ] Add AlertRule abstraction
- [ ] Implement StrategyAlertEngine
- [ ] Implement DivisibilityAlertRule
- [ ] Preserve predictable rule order
- [ ] Keep SimpleAlertEngine as baseline variant
- [ ] Commit Stage 3 work

### Stage 4: Add WARN rule

- [ ] Add divisible-by-7 WARN rule in extended rule set
- [ ] Verify 21 -> LOWWARN
- [ ] Verify 35 -> ADVISORYWARN
- [ ] Verify 105 -> LOWADVISORYWARN
- [ ] Commit Stage 4 work

### Adapter

- [ ] Add MeasurementProcessor abstraction
- [ ] Add AlertEngineProcessorAdapter
- [ ] Test adapter list processing
- [ ] Test input order preservation

### Tests

- [ ] Add 2-3 happy-path tests
- [ ] Add 4-8 edge-case tests
- [ ] Verify simple and strategy base outputs match
- [ ] Verify zero behavior
- [ ] Verify negative number behavior
- [ ] Verify empty rule list behavior
- [ ] Verify rule order behavior
- [ ] Verify invalid zero divisor is rejected

### Docker

- [ ] Add Dockerfile
- [ ] Add .dockerignore
- [ ] Document Docker build command
- [ ] Document Docker test command
- [ ] Document Docker run command
- [ ] Confirm local testing does not require host Maven/Java

### CI

- [ ] Add .github/workflows/ci.yml
- [ ] Use Java 17
- [ ] Run mvn -B verify
- [ ] Trigger on push to main
- [ ] Trigger on pull_request to main

### Documentation

- [ ] Add README.md
- [ ] Explain FizzBuzz-like nature
- [ ] Explain Strategy pattern trade-off
- [ ] Explain why simple implementation exists
- [ ] Explain adapter role
- [ ] Document base and extended rule sets
- [ ] Document Docker-only local workflow
- [ ] Add AI_REFLECTION.md at root

## Submission Checklist

- [ ] All four implementation stages are done
- [ ] Each original stage has its own commit
- [ ] Commit messages follow Conventional Commits
- [ ] Program compiles and runs
- [ ] Output for values 1-20 matches Stage 1 expected output
- [ ] Stage 4 outputs are correct for 21, 35, and 105
- [ ] Unit tests pass locally in Docker
- [ ] Unit tests pass in GitHub Actions
- [ ] README is clear
- [ ] AI_REFLECTION.md is included
- [ ] Repository is shared as GitHub/GitLab link or ZIP with .git folder

## Notes

- Keep the implementation small.
- Strategy pattern is justified by Stage 3 extensibility requirements.
- Do not add Spring or cloud infrastructure.
- Do not over-test the FizzBuzz-like behavior.
```

---

## 17. Suggested AI_REFLECTION.md Outline

```markdown
# AI Reflection

## 1. AI tools used

I used ChatGPT to help plan the implementation, review the task requirements, and think through a clean design and test strategy.

## 2. One useful AI interaction

Prompt:

> I have a Java take-home task for an alert rule engine, similar to FizzBuzz. Please help me design a simple implementation and then a Strategy pattern implementation that still stays small and readable.

Summary of response:

The response suggested keeping a simple baseline implementation and introducing an `AlertRule` interface plus a rule-based engine for extensibility. It also suggested using ordered rules so combined labels emerge naturally from concatenation.

What I used:

I used the idea of ordered rule objects and a small engine that concatenates matching labels.

Why it was useful:

It helped me satisfy the extensibility requirement without adding a framework or unnecessary infrastructure.

## 3. One AI suggestion modified or rejected

The AI suggested making the rules configurable through external JSON/YAML files.

I rejected that because the assignment is intentionally small, has a 2-6 hour expectation, and does not require runtime configuration. I kept rule configuration in Java factories instead, which is easier to review and test for this task.
```

Edit this reflection so it accurately matches the actual AI usage during implementation.

---

## 18. Validation References

The following references were checked while preparing this PRD:

- Original task: `TASK_TAKE_HOME.md`
- GitHub Actions official Maven CI guidance: https://docs.github.com/en/actions/how-tos/use-cases-and-examples/building-and-testing/building-and-testing-java-with-maven
- Docker official Dockerfile best practices: https://docs.docker.com/develop/develop-images/dockerfile_best-practices/
- Conventional Commits 1.0.0 specification: https://www.conventionalcommits.org/en/v1.0.0/
