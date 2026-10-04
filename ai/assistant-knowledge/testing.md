# Testing

Tests are executable evidence for requirements, not the definition of those
requirements. Choose the narrowest level that can prove the behavior, then add a
broader test only for a distinct integration risk.

## Test levels

- **Unit tests** cover entity/domain rules and pure mapping or normalization logic
  without Spring or a database.
- **MVC slice tests** cover request binding and validation, DTO shape, controller
  delegation, error translation, and route authorization with mocked services.
- **Repository integration tests** use PostgreSQL for queries, fetch graphs,
  projections, mappings, constraints, and locking behavior that an in-memory or mock
  test cannot establish.
- **Full-context integration tests** prove application wiring, profile configuration,
  Liquibase startup, and critical end-to-end use cases when layer tests are
  insufficient.

All PostgreSQL integration tests extend `AbstractIntegrationTest`. Its static
container deliberately lives for the test JVM so Spring's cached application context
does not retain a connection to a container stopped between concrete test classes.
Docker must be running for the full test task.

## Required scenario coverage

Coverage status describes the repository now; a gap is not permission to omit the
behavior.

| Behavior to prove                                                                      | Appropriate level       | Current evidence                                |
|----------------------------------------------------------------------------------------|-------------------------|-------------------------------------------------|
| Duplicate crew assignment is rejected                                                  | Domain unit             | Covered                                         |
| Crew-size limit is enforced; unlimited crew remains allowed                            | Domain unit             | Limit covered; unlimited addition only implicit |
| Assigned person can be removed; unassigned person is rejected                          | Domain unit             | Covered                                         |
| Phone whitespace normalization and DTO summary/detail boundaries                       | Mapper unit             | Gap; mapper test classes are empty              |
| Item category binding/filter forwarding and malformed category error                   | MVC slice               | Covered                                         |
| Request validation produces field-level `400` problems                                 | MVC slice               | Gap                                             |
| Missing resources produce `404` and crew conflicts produce `409`                       | MVC slice               | Covered for operation paths                     |
| Operation collection returns occupancy summaries                                       | MVC slice               | Covered                                         |
| Controller command translation and response mapping for mutations                      | MVC slice               | Partly covered by security tests                |
| Every endpoint follows the role matrix, including anonymous `401` and wrong-role `403` | Security MVC slice      | Main paths covered                              |
| Realm-role claims map to Spring authorities                                            | Security MVC slice      | Covered through representative tokens           |
| Item category filtering and pagination work in PostgreSQL                              | Repository integration  | Covered                                         |
| Operation summary query returns correct crew counts                                    | Repository integration  | Covered                                         |
| Person and operation detail queries load the relationships needed by mappers           | Repository integration  | Covered                                         |
| Database rejects duplicate crew assignments and invalid foreign keys                   | Repository integration  | Gap                                             |
| Concurrent crew changes cannot bypass crew rules                                       | Integration/concurrency | Gap                                             |
| Application starts with Liquibase-managed PostgreSQL schema                            | Full context            | Covered                                         |
| Checked-in OpenAPI paths and schemas remain aligned with MVC behavior                  | Contract/integration    | Gap                                             |

When behavior changes, update this table and its tests in the same task. Do not keep
empty placeholder test classes; either add meaningful tests or remove the placeholder.
