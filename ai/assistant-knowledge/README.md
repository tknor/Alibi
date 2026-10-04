# Alibi assistant knowledge

This is concise, tool-agnostic working memory for AI assistants and human
collaborators. The root [`AGENTS.md`](../../AGENTS.md) points here. This directory is
versioned with the application so knowledge changes can accompany the changes they
describe.

Start here, then read only the notes needed for the task:

| When the task concerns...                      | Read                               |
|------------------------------------------------|------------------------------------|
| Business meaning or terminology                | [domain.md](domain.md)             |
| Expected behavior or authorization             | [requirements.md](requirements.md) |
| Design boundaries or technical decisions       | [architecture.md](architecture.md) |
| Finding implementation or supporting artifacts | [project-map.md](project-map.md)   |
| Project-specific implementation practices      | [conventions.md](conventions.md)   |
| Test level, scenarios, or current coverage     | [testing.md](testing.md)           |

For most feature work, read `domain.md`, the relevant part of `requirements.md`,
and one or two task-specific notes. Do not load every file by default.

## Sources of truth

Different artifacts answer different questions; no single artifact supersedes all
others.

| Question                                | Primary authority                                                |
|-----------------------------------------|------------------------------------------------------------------|
| What should the product do?             | The current user request, then `requirements.md` and `domain.md` |
| What is the public HTTP contract?       | `src/main/resources/static/openapi.yaml`                         |
| What is the persisted schema?           | Liquibase changelogs under `src/main/resources/db/changelog`     |
| What does the application currently do? | Production code and configuration                                |
| What behavior has executable evidence?  | Tests; absence of a test is not absence of a requirement         |
| How is the project run by a newcomer?   | The root `README.md` and versioned operational documentation     |

When these disagree, treat the difference as a defect or an unresolved decision.
Do not infer intended behavior from a likely-buggy implementation. A new explicit
user requirement takes precedence and the affected artifacts should be reconciled
in the same change.

## Maintenance rules

- Update the relevant note in the same task that changes a durable fact. The
  maintenance map above indicates which file owns which kind of fact.
- Keep facts concise and link to authoritative artifacts instead of copying classes,
  schemas, endpoint payloads, or setup instructions.
- Record the current agreed state, not request history, session summaries, personal
  context, speculative roadmaps, or generic engineering advice.
- Include rationale only when it prevents a future collaborator from undoing a
  non-obvious decision.
- Reflect feature changes in `requirements.md`; domain-language changes in
  `domain.md`; structural or design changes in `architecture.md` and `project-map.md`;
  and changed test obligations or coverage in `testing.md`.
- Put public onboarding and operational information in the appropriate project
  documentation as well. This knowledge base does not replace it.
