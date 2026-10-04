# Instructions for AI collaborators

Before planning or changing Alibi, read
[`ai/assistant-knowledge/README.md`](ai/assistant-knowledge/README.md).
It is the index to the project's tool-agnostic collaboration memory. Load only
the linked notes relevant to the task, then inspect the authoritative code or
versioned documentation named by those notes.

Keep the knowledge base current as part of the same change whenever durable domain
knowledge, requirements, architecture, project structure, conventions, or test
obligations change. Do not add session logs, speculative plans, or details that are
easy to recover from a small part of the codebase.

When sources disagree, do not silently choose one. Apply an explicit requirement
from the current request; otherwise identify the discrepancy and resolve it with the
user before building further assumptions on it.
