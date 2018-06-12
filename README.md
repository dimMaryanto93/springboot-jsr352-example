# Belajar jsr 352 

Batch proccession based on JSR 352 dengan Spring Batch.

## Setup / System Requirement

- PostgreSQL
- Maven
- Docker (Optional)

## Configuration

- Database PostgreSQL
    - user: `postgres`
    - password: `admin`
    - url: `jdbc:postgresql://localhost:5432/postgres`
- Database on Docker

```docker
docker-compose up -d
```

## initial database / tables

```bash
mvn clean -Dflyway.user=postgres -Dflyway.password=admin -Dflyway.url=jdbc:postgresql://localhost:5432/postgres flyway:clean flyway:migrate
```