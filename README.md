# Alibi

Alibi is a Spring Boot REST API built as a learning project. Its domain is a
fictional, safe, movie-style heist planning and crew-management application.

## Prerequisites

- JDK 21
- Docker Desktop, for local infrastructure and integration tests

## Run locally

Start the local PostgreSQL and Keycloak services:

```powershell
docker compose -f docker/docker-compose.yml up -d
```

Then start the application. `local` is the default profile, so this command uses
the PostgreSQL container published at `localhost:5432`.

```powershell
.\gradlew.bat bootRun
```

To make the selected environment explicit:

```powershell
.\gradlew.bat bootRun --args="--spring.profiles.active=local"
```

See [docker/USAGE.md](docker/USAGE.md) for the local Keycloak console, demo
accounts, and how to reset the local infrastructure.

Stop the infrastructure when it is no longer needed:

```powershell
docker compose -f docker/docker-compose.yml down
```

## Tests

Run all tests with:

```powershell
.\gradlew.bat test
```

## Configuration profiles

| Profile | Intended use | Connection source |
| --- | --- | --- |
| `local` | Developer machine | Committed Docker Compose defaults |
| `test` | Automated integration tests | Testcontainers, configured dynamically |
| `prod` | Deployed application | Environment variables / secret manager |

## Production

Build a container image:

```powershell
docker build -t alibi:local .
```

In a deployment environment, explicitly activate `prod` and inject the database credentials:

```text
SPRING_PROFILES_ACTIVE=prod
DB_URL=jdbc:postgresql://database-host:5432/alibi
DB_USERNAME=<username>
DB_PASSWORD=<secret>
```
