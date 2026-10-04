# Domain

Alibi models operation planning, crew assignments, people, and equipment. Domain
language should remain generic; examples such as code names do not define a more
specific real-world setting.

## Terms

### Person

An application-owned crew profile with a generated UUID, name, and required phone
number. A person is not an authenticated Keycloak user: identity-provider accounts
authorize API callers, while people are business data that can be assigned to
operations.

Person detail includes the code names of operations to which the person is assigned.
Person summaries expose only ID and name so a crew listing does not disclose phone
numbers.

### Operation

A planned activity identified by a generated UUID and a required code name. It may
have a positive crew-size limit; no limit means crew size is unrestricted.

An operation owns its crew-assignment rules:

- A person can appear at most once in one operation.
- A person can participate in multiple operations.
- A limited operation cannot exceed its crew-size limit.
- Removing a person who is not assigned is a domain conflict.

### Crew member

The assignment of one existing person to one operation. It is a relationship entity,
not a separate person or login, and has no independent API lifecycle. Deleting an
operation deletes its assignments; deleting a referenced person is not defined as an
application capability.

### Item

A generic equipment or inventory record with a generated UUID, required label, and
one category: `VEHICLE`, `TOOL`, `WEARABLE`, or `OTHER`. Items currently have no
domain relationship to operations or people.

## Authorization terms

- `ADMIN` maintains people and items and may read people and operations.
- `CREW_MANAGER` reads people and operations and manages operations and crews.
- `CREW_MEMBER` is an identity-provider role but currently has no protected business
  capability of its own.

Roles are independent rather than hierarchical. In particular, `ADMIN` does not
inherit operation mutation permissions from `CREW_MANAGER`.
