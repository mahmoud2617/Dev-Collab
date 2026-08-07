## Running Flyway Maven Plugin Locally

The following commands are only required when using the **Flyway Maven Plugin** with a locally installed PostgreSQL database. Replace the placeholders with your local database configuration.

### Clean the database

```bash
./mvnw flyway:clean \
  -Dflyway.url=<your_database_url> \
  -Dflyway.user=<your_username> \
  -Dflyway.password="<your_password>"
```

### Apply migrations

```bash
./mvnw flyway:migrate \
  -Dflyway.url=<your_database_url> \
  -Dflyway.user=<your_username> \
  -Dflyway.password="<your_password>"
```

### Repair migration history (if needed)

```bash
./mvnw flyway:repair \
  -Dflyway.url=<your_database_url> \
  -Dflyway.user=<your_username> \
  -Dflyway.password="<your_password>"
```

### Validate migrations

```bash
./mvnw flyway:validate \
  -Dflyway.url=<your_database_url> \
  -Dflyway.user=<your_username> \
  -Dflyway.password="<your_password>"
```
**Example database URL:** `jdbc:postgresql://localhost:5432/devcollab`

> **Note:** These commands are only required when using a local PostgreSQL instance. If you are running the application with Docker Compose, Flyway uses the database configuration provided by the Docker environment.