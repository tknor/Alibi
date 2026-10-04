# Project conventions

Only conventions that are easy to miss or specific to Alibi belong here.

## API and application boundaries

- Keep the existing singular resource paths (`/api/person`, `/api/item`, and
  `/api/operation`) unless an intentional contract revision changes them.
- Do not return JPA entities from controllers. Use request, summary, detail, command,
  and page DTOs according to the existing boundary.
- Translate HTTP requests into self-contained application commands at the controller
  boundary when a use case needs identifiers or context beyond the request body.
- Keep domain invariants that depend on an operation's state on `Operation`; services
  orchestrate loading, locking, transactions, and persistence.
- Preserve the summary/detail privacy boundary: operation crews and person lists do
  not expose phone numbers.
- Use central exception advice and `ProblemDetail` for application API errors rather
  than controller-specific error bodies.

## Persistence and changes

- Use UUID identifiers and explicit database column/constraint names.
- Evolve the schema with a new ordered Liquibase change set. Keep JPA mappings and
  migrations consistent; Hibernate remains in validation mode.
- Preserve the operation write-lock pattern for crew mutations unless concurrency
  behavior is deliberately redesigned.
- Avoid broad eager relationships. Add a purpose-built projection or fetch graph for
  a read shape that needs related data.

## Contract and security synchronization

- Update `openapi.yaml` in the same change as an endpoint, payload, status, or public
  authorization-contract change.
- Update `SecurityConfig`, its role-matrix tests, and `requirements.md` together when
  access policy changes. Do not assume roles inherit from one another.
- Keep secrets and environment-specific production values out of committed
  configuration. Local-only demo credentials belong only in the Docker setup.

## Knowledge maintenance

Finish feature work by checking whether the domain, requirements, architecture,
project map, conventions, or test obligations changed. Update only the affected
notes, and remove stale statements rather than accumulating historical annotations.
