# Project map

Use this map to choose a starting point; use search and the code itself for exact
class-level details.

| Area                   | Location                                              | Contains                                                           |
|------------------------|-------------------------------------------------------|--------------------------------------------------------------------|
| Public onboarding      | `README.md`, `docker/USAGE.md`                        | Prerequisites, local run, authentication, production configuration |
| AI collaboration       | `AGENTS.md`, `ai/assistant-knowledge`                 | Entry point and task-oriented project knowledge                    |
| Build                  | `build.gradle`, Gradle wrapper                        | Java toolchain, dependencies, test task                            |
| Application code       | `src/main/java/cz/tomas/alibi`                        | Boot entry point and all production Java code                      |
| HTTP boundary          | `common/controller`, `common/dto`, `common/dtomapper` | Endpoints, request/response records, entity/DTO translation        |
| Use cases              | `common/service`                                      | Person/item services and operation command/query services          |
| Domain and persistence | `common/entity`, `common/domain`, `common/repository` | JPA entities, enums, repositories, projections                     |
| Error handling         | `common/exception`                                    | Domain/application exceptions and `ProblemDetail` advice           |
| Security               | `config/SecurityConfig.java`                          | Route authorization and JWT realm-role conversion                  |
| Public API contract    | `src/main/resources/static/openapi.yaml`              | Paths, payload schemas, status codes, security declarations        |
| Database schema        | `src/main/resources/db/changelog`                     | Ordered Liquibase migrations                                       |
| Configuration          | `src/main/resources/application*.yaml`                | Shared and profile-specific runtime settings                       |
| Automated tests        | `src/test/java/cz/tomas/alibi`                        | Domain, MVC, security, repository, and context tests               |
| Manual requests        | `http`                                                | IntelliJ HTTP examples for tokens, API calls, and health           |
| Local infrastructure   | `docker`                                              | Compose services and imported Keycloak realm                       |
| Demo data              | `db/seed`                                             | Manually applied sample SQL; not an application migration          |

The test package structure mirrors production where practical.
`AbstractIntegrationTest` owns the shared Testcontainers PostgreSQL lifecycle.
