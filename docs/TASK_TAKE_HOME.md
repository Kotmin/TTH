# Take-Home Task: Alert Rule Engine

**Position:** Junior Java Developer at SolDevelo  
**Expected time:** 2-6 hours  
**Deadline:** 4 calendar days from receiving this task, EOD on the deadline day (e.g., sent Monday → due Thursday EOD)  
**Submission:** Git repository - share a GitHub/GitLab link or send a ZIP of the repository (with .git folder included)

---

## Context

You are building a backend component for a monitoring system. The system receives numeric measurement readings from sensors and must generate alerts based on configurable rules.

Your task is to implement this component in **Java** (Java 17+). **You must use a build system: Maven (`pom.xml`) or Gradle (`build.gradle`).** Submissions without a build system will not be reviewed. No frameworks (Spring, etc.) are required - plain Java is fine. The program must be runnable from the command line via a single build-tool command (e.g., `mvn compile exec:java`, `gradle run`).

---

## Stage 1: Basic Implementation

Implement a program that processes a list of integer measurement values and produces alert output based on the following rules:

- If a value is divisible by 3, output: `LOW`
- If a value is divisible by 5, output: `ADVISORY`
- If a value is divisible by both 3 and 5, output both labels combined: `LOWADVISORY`
- Otherwise, output the original value

For input values 1 through 20, the expected output is:

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

**Commit your work before moving to the next stage.**

---

## Stage 2: Remove the Special Case

In Stage 1, the "divisible by both 3 and 5" rule likely required a separate condition. Refactor your code so that the `LOWADVISORY` output for values divisible by both 3 and 5 emerges naturally from combining the `LOW` and `ADVISORY` rules - without a dedicated `if` or branch for the combined case.

Hint: think about what "combining" means - when multiple rules match, their labels should be concatenated in rule order. `LOWADVISORY` should result from both `LOW` and `ADVISORY` matching, not from a third explicit check.

After this change, the output must remain identical to Stage 1.

**Commit your work before moving to the next stage.**

---

## Stage 3: Design for Extensibility

New alert rules will be added frequently in the future. Refactor your solution so that:

1. Each rule is a self-contained unit - adding a new rule should NOT require modifying existing rules or the engine that executes them.
2. The system can hold any number of rules.
3. The order of rule evaluation is predictable.

Think about what abstraction(s) you need. Design interfaces and/or classes that make the system open for extension.

After this refactoring, the output for values 1-20 must remain identical.

**Commit your work before moving to the next stage.**

---

## Stage 4: Prove It Works

Add a new rule to demonstrate your design:

- If a value is divisible by 7, output: `WARN`

When multiple rules match (e.g., a value divisible by both 3 and 7), their alert labels should combine in rule order - the same concatenation mechanism from Stage 2, now using your extensible design from Stage 3.

For value 21 (divisible by both 3 and 7), the output should be: `LOWWARN`  
For value 35 (divisible by both 5 and 7), the output should be: `ADVISORYWARN`  
For value 105 (divisible by 3, 5, and 7), the output should be: `LOWADVISORYWARN`

**Commit your work.**

---

## Stage 5: AI Usage Reflection

Create a file called `AI_REFLECTION.md` **at the root of your repository** (next to `pom.xml` / `build.gradle` — NOT inside `src/`, `docs/`, or any subdirectory). In it, answer the following:

1. **Which AI tools did you use** while working on this task? (e.g., ChatGPT, GitHub Copilot, Claude, none). If none, state that and skip questions 2-3.

2. **Describe one interaction with AI that helped you.** Paste the prompt you gave and a summary of the response. Explain what you took from it and why it was useful.

3. **Describe one AI suggestion that you modified or rejected.** What did the AI suggest? Why didn't you use it as-is? What did you do instead?

Be honest. There is no penalty for using AI tools - we are evaluating how you think, not whether you used assistance.

---

## Submission Checklist

Before submitting, verify:

- [ ] All four stages are implemented and each has its own commit
- [ ] The program compiles and runs
- [ ] Output for values 1-20 matches the expected output in Stage 1
- [ ] Stage 4 outputs are correct for values 21, 35, and 105
- [ ] `AI_REFLECTION.md` is included
- [ ] The repository is shared as a GitHub/GitLab link or a ZIP file (with `.git` folder)

---

## What We Are Looking For

- **Correctness** - the output matches the specification at each stage
- **Design thinking** - how you approach the extensibility problem in Stage 3
- **Code quality** - readable, idiomatic Java
- **Progression** - your git history shows how your thinking evolved across stages
- **Honest AI reflection** - we value critical thinking about tools, not avoidance of them

Good luck!
