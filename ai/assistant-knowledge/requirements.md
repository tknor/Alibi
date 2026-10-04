# Requirements

This is the agreed behavioral baseline, not a backlog. Endpoint payload shapes and
response schemas belong in the checked-in OpenAPI specification rather than being
duplicated here.

## Access control

| Capability                               | Anonymous | `CREW_MEMBER` | `CREW_MANAGER` | `ADMIN` |
|------------------------------------------|----------:|--------------:|---------------:|--------:|
| List items                               |       Yes |           Yes |            Yes |     Yes |
| Create an item                           |        No |            No |             No |     Yes |
| List people or read person detail        |        No |            No |            Yes |     Yes |
| Create a person                          |        No |            No |             No |     Yes |
| List operations or read operation detail |        No |            No |            Yes |     Yes |
| Create an operation                      |        No |            No |            Yes |      No |
| Add or remove an operation crew member   |        No |            No |            Yes |      No |

Authentication uses bearer JWTs. Realm roles from the token's
`realm_access.roles` claim become Spring `ROLE_...` authorities. Swagger UI, its
specification, and health probes are public. Other exposed Actuator endpoints require
authentication.

## People

- An admin can create a person with a non-blank name and non-blank phone number.
- Whitespace within a submitted phone number is removed before persistence. No
  stronger phone-format validation is currently required.
- A crew manager or admin can list all people as summaries containing ID and name.
- A crew manager or admin can read person detail containing ID, name, phone, and an
  alphabetically sorted list of assigned operation code names.
- Reading an unknown person returns `404 Not Found`.

## Items

- Anyone can list items using zero-based pagination and optional exact category
  filtering.
- The default page size is 20 and the default sort is ascending by label.
- An invalid category value or malformed paging value returns `400 Bad Request`.
- An admin can create an item with a non-blank label and a supported category.

## Operations and crews

- A crew manager can create an operation with a non-blank code name and an optional
  positive crew-size limit.
- A crew manager or admin can list operation summaries containing current crew size
  and optional crew-size limit.
- A crew manager or admin can read operation detail with crew members represented as
  person summaries; phone numbers are not exposed in this view.
- A crew manager can add an existing person to an existing operation and receives
  the updated operation detail.
- A crew manager can remove an assigned person and receives the updated operation
  detail.
- Missing referenced people or operations return `404 Not Found`.
- Duplicate assignment, exceeding the crew-size limit, or removing an unassigned
  person returns `409 Conflict` and does not change the crew.
- Concurrent crew mutations on one operation must be serialized so those rules are
  evaluated against a current crew.

## API errors and operations

- Application-generated API failures use RFC 9457 `ProblemDetail` responses.
- Request validation failures return `400` with field errors; malformed values return
  `400`; missing resources return `404`; and crew-rule violations return `409`.
- Database structure is managed by Liquibase and Hibernate validates rather than
  creates the schema.
- The `local`, `test`, and `prod` profiles keep environment-specific configuration
  separate. Production exposes only Actuator `health` and `info`, with liveness and
  readiness health groups enabled.
