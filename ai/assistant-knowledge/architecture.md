# Architecture

## Shape and stack

Alibi is a compact Spring Boot 4.1 / Java 21 REST backend packaged as one deployable
application. It uses Spring Web MVC, Security resource server, Jakarta Validation,
Spring Data JPA, Liquibase, PostgreSQL, Actuator, and a manually maintained OpenAPI
document. Lombok removes entity and constructor boilerplate.

The current package root is `cz.tomas.alibi`. Code beneath `common` follows a
layered flow:

`controller -> service -> repository -> PostgreSQL`

Controllers own HTTP translation and validation. Services define transaction and
use-case boundaries. JPA entities also carry local domain behavior. Mappers define
the DTO boundary; API records do not expose JPA entities directly.

## Read and write paths

- Person and item use focused application services.
- Operations use a lightweight command/query split. `OperationCommandService`
  creates operations and changes crews; `OperationQueryService` serves reads. This
  is an in-process responsibility split, not separate storage or infrastructure.
- Operation list reads use an aggregate projection so crew size is calculated in the
  query without loading the detail graph.
- Operation detail fetches crew members and people; person detail fetches assignments
  and operations. These purpose-built entity graphs support mapping within the
  service/controller transaction flow.

## Persistence and consistency

UUIDs identify all persisted entities. `CrewMember` is the association between
`Operation` and `Person`; the database enforces foreign keys and uniqueness of each
operation/person pair. Operation deletion cascades to its assignments.

Crew rules live on the `Operation` entity, close to the state they protect. Crew
mutation first takes a pessimistic write lock on the operation and then loads the
required entity graph in the same transaction. Keep this concurrency protection when
changing the mutation flow.

## Security and API boundary

The application is stateless and disables CSRF because it authenticates bearer
tokens rather than browser sessions. A JWT converter maps Keycloak realm roles to
Spring authorities. Authorization is expressed centrally in `SecurityConfig`; the
role matrix is captured in `requirements.md` and verified by MVC security tests.

API responses use purpose-specific summary/detail DTOs. Central advice translates
known application and request failures to `ProblemDetail`. The checked-in OpenAPI
file is the public contract and drives the anonymous Swagger UI; it is not generated
from controller annotations.

## Runtime profiles

- `local` uses manually started Docker Compose PostgreSQL and Keycloak services and
  exposes all Actuator endpoints for development.
- `test` uses a Testcontainers PostgreSQL database and a mocked JWT decoder.
- `prod` obtains database and issuer settings from the environment, uses graceful
  shutdown, and limits Actuator exposure to health and info.
