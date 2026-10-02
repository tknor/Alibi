
# Local Keycloak

## Start and open the Keycloak admin console

From the repository root:

```powershell
docker compose -f docker/docker-compose.yml up -d
```

Open [the Keycloak admin console](http://localhost:8081/admin/) and sign in with:

| Field    | Value   |
|----------|---------|
| Username | `admin` |
| Password | `admin` |

Imported realm contains the `CREW_MEMBER`, `CREW_MANAGER`, and `ADMIN` roles,
plus these local-only demo accounts:

| User           | Password       | Realm role     |
|----------------|----------------|----------------|
| `crew.member`  | `crew-member`  | `CREW_MEMBER`  |
| `crew.manager` | `crew-manager` | `CREW_MANAGER` |
| `alibi.admin`  | `alibi-admin`  | `ADMIN`        |

There's a configured `alibi-api` OpenID Connect client.

## Keycloak DB

Keycloak stores its users, roles, clients, and settings in the named
`keycloak-postgres-data` Docker volume.

The realm JSON in `keycloak/import/alibi-realm.json` is imported only when
the `alibi` realm does not already exist.

To return to the committed initial state:

```powershell
docker compose -f docker/docker-compose.yml down -v
docker compose -f docker/docker-compose.yml up -d
```

## Stop

```powershell
docker compose -f docker/docker-compose.yml down
```
