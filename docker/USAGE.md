# Local Keycloak

This Compose project starts a local-only Keycloak instance with its own PostgreSQL
database. It is development/demo infrastructure, not a production deployment.
It deliberately assigns the fixed container names `alibi-keycloak` and
`alibi-keycloak-postgres`; this makes the local Docker Desktop view easier to read.
Consequently, this Compose project cannot be scaled to multiple replicas or run twice
on the same Docker host without changing those names.

## Start and open the admin console

From the repository root:

```powershell
docker compose -f docker/docker-compose.yml up -d
```

Open [the Keycloak admin console](http://localhost:8081/admin/) and sign in with:

| Field | Value |
| --- | --- |
| Username | `admin` |
| Password | `admin` |

Use the realm selector in the top-left corner to switch from `master` to `alibi`.
The imported realm contains the `CREW_MEMBER`, `CREW_MANAGER`, and `ADMIN` roles,
plus these local-only demo accounts:

| User | Password | Realm role |
| --- | --- | --- |
| `crew.member` | `crew-member` | `CREW_MEMBER` |
| `crew.manager` | `crew-manager` | `CREW_MANAGER` |
| `alibi.admin` | `alibi-admin` | `ADMIN` |

The `alibi-api` OpenID Connect client is configured for local Swagger UI/Postman
demonstrations. It allows Authorization Code flow and the password/direct-grant flow
for these disposable demo accounts only.

## What persists

Keycloak stores its users, roles, clients, and settings in the named
`keycloak-postgres-data` Docker volume. Restarting the containers retains admin-console
changes. The realm JSON in `keycloak/import/alibi-realm.json` is imported only when
the `alibi` realm does not already exist.

To return to the committed demo state, stop the stack and remove only this Compose
project's volumes, then start it again:

```powershell
docker compose -f docker/docker-compose.yml down -v
docker compose -f docker/docker-compose.yml up -d
```

This deletes the local Keycloak database volume. It does not affect unrelated Docker
projects. Do not use these demo credentials or development mode in production.

## Stop

```powershell
docker compose -f docker/docker-compose.yml down
```
