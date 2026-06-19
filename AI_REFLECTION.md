# AI Reflection

## 1. AI tools used

I used Claude (claude.ai) throughout this task — for planning, designing the architecture, generating the PRD, structuring the commit sequence, and reviewing the implementation.

## 2. One interaction that helped

**Prompt:**

> I have a Java take-home task from SolDevelo. It's a FizzBuzz-style alert engine with 4 stages. Stage 3 requires extensibility without modifying the engine when new rules are added. Help me design a two-implementation approach: a simple baseline and a Strategy-pattern engine, keeping both small and reviewable.

**Summary of response:**

The AI suggested keeping `SimpleAlertEngine` as a baseline (plain `StringBuilder` loop, no special combined branch) and introducing a minimal `AlertRule` interface with a `StrategyAlertEngine` that iterates an ordered `List<AlertRule>`. It also suggested a `DivisibilityAlertRule` record as the concrete rule, a factory for rule sets, and an adapter to decouple list processing from the engine interface.

**What I took from it:**

The ordered-rule-list approach — concatenating matching labels in iteration order — cleanly handles both Stage 2 (no combined branch) and Stage 4 (WARN) without changing the engine. I used this structure directly.

**Why it was useful:**

It gave me a concrete, minimal architecture that satisfies all four stages without over-engineering. The `AlertEngineProcessorAdapter` in particular was a suggestion I adopted because it keeps the `AlertEngine` interface focused on a single integer, not on list logic.

## 3. One AI suggestion I modified or rejected

**What the AI suggested:**

The AI proposed making the rule sets configurable through an external YAML or properties file, so rules could be changed at runtime without recompiling.

**Why I rejected it:**

The task is explicitly scoped to 2–6 hours and states "plain Java is fine — no frameworks required." Adding YAML parsing would require an external library (e.g., SnakeYAML), a configuration schema, and error handling for malformed files — none of which is justified for a take-home with four well-defined stages. I kept rule configuration in `RuleSetFactory` as plain Java, which is easier to review, test, and understand for the intended audience.
