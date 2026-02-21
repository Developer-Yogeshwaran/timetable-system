# Time Table Management System

Simple Spring Boot app to generate and export timetables as PDF.

## Quick overview
- Runs with an embedded Tomcat server on port `8080` by default.
- Uses H2 in-memory database by default (for local/dev). Production can use MySQL via env vars.

## Requirements
- Java 17+ (JDK)
- Maven (or use the included `mvnw` wrapper)

## Run locally (development)
1. Build (optional):
```bash
./mvnw -DskipTests package
```
2. Run with Maven (uses H2):
```bash
./mvnw spring-boot:run
```
Or run the packaged jar:
```bash
java -jar target/timetable-0.0.1-SNAPSHOT.jar
```

Open the site: http://localhost:8080/
H2 console: http://localhost:8080/h2-console
 - JDBC URL: `jdbc:h2:mem:timetable`
 - User: `sa`, Password: (leave blank)

## Important endpoints
- `GET /` — static UI (index page)
- `POST /api/timetable/generate` — generate timetable (accepts `TimetableRequest` JSON)
- `GET  /api/timetable/last` — return last generated timetable JSON
- `GET  /api/timetable/pdf` — download PDF for last generated timetable
- `POST /api/pdf/generate` — generate PDF from provided timetable JSON

## Environment variables
- `PORT` — port the app listens on (default `8080`). Render will set this automatically.
- `SPRING_DATASOURCE_URL` — JDBC URL (set to MySQL in production, e.g. `jdbc:mysql://...`). If unset, app uses H2 in-memory.
- `SPRING_DATASOURCE_USERNAME` / `SPRING_DATASOURCE_PASSWORD` — DB credentials.

## Deploy to Render (recommended using Docker)
1. Push repository to GitHub.
2. Create a new Web Service on Render and connect your GitHub repo.
3. Choose a Docker environment (Render will use the repository `Dockerfile`).
4. Render sets `$PORT`; the app reads `server.port=${PORT}`.

Alternative (no Docker): set build and start commands in Render:
 - Build: `./mvnw -DskipTests package`
 - Start: `java -Dserver.port=$PORT -jar target/timetable-0.0.1-SNAPSHOT.jar`

## Troubleshooting
- If the site doesn't load, check logs (local run shows logs in console).
- Port conflict: set `PORT` or stop the process using the port.
- Database: if using MySQL, ensure `SPRING_DATASOURCE_URL`/credentials are correct and reachable.

## Next steps I can help with
- Add sample `data.sql` to seed H2 on startup.
- Create a GitHub repo and push changes.
- Create a complete `render.yaml` with secrets and scaling settings.

---
Files changed for Render/H2 support: `pom.xml`, `src/main/resources/application.properties`, `Procfile`, `render.yaml`.

## Project folder (key files)
- `pom.xml` — Maven project file and dependencies
- `Dockerfile` — Docker build for container deployments
- `Procfile` — simple start command used by some platforms
- `render.yaml` — starter Render service config (Docker)
- `src/main/java` — application source code
	- `com.example.timetable` — main package
	- `controller` — REST controllers (`TimetableController`, `PdfController`)
	- `service` — business logic (`TimetableService`, `TimetableGenerator`)
	- `entity` — JPA entities (`TimetableEntry`, `TimetableConfig`, `Subject`)
	- `repository` — Spring Data JPA repositories
- `src/main/resources` — application resources
	- `static` — frontend `index.html`, JS, CSS
	- `templates` — (optional server-side templates)
	- `application.properties` — runtime configuration (H2 / MySQL toggles)

## Usage (quick)
- Build:
```bash
./mvnw -DskipTests package
```
- Run (dev / H2):
```bash
./mvnw spring-boot:run
```
Or run package:
```bash
java -jar target/timetable-0.0.1-SNAPSHOT.jar
```

Common actions
- Open UI: `http://localhost:8080/`
- H2 console: `http://localhost:8080/h2-console` (JDBC `jdbc:h2:mem:timetable`, user `sa`)
- Generate timetable (example):
```bash
curl -X POST http://localhost:8080/api/timetable/generate \
	-H "Content-Type: application/json" \
	-d '{ "numberOfDays":5, "periodsPerDay":6, "periodDuration":50 }'
```
- Download last timetable PDF:
```bash
curl -O http://localhost:8080/api/timetable/pdf
```

## Technologies used
- Java 17
- Spring Boot (Web, Data JPA)
- H2 (in-memory) and MySQL Connector/J (runtime)
- Hibernate ORM
- iText 7 (PDF generation)
- Lombok (boilerplate reduction)
- Maven (build)
- Docker (container), Render (deployment)

