# Dev Trends Explorer

Dev Trends Explorer is a full-stack web application designed to track, aggregate, and analyze emerging trends and technologies in the software development ecosystem. 

---

## Project Architecture 

The application follows a decoupled monorepo structure utilizing modern enterprise paradigms:

- **Backend:** Java / Spring Boot managed via Gradle.
- **Frontend:** Single Page Application (SPA) showcasing data trends and handling OAuth login.
- **Database:** PostgreSQL with automated migrations.
- **Security:** OAuth 2.0 via a containerized Keycloak instance.
- **Deployment:** Fully dockerized environments using multi-container Docker Compose.

```text
├── debug-requests/        # Collection of local HTTP/API test requests
├── docker/                # Environment configuration files (Keycloak realms, etc.)
├── frontend/              # Web user interface (with OAuth integration)
├── gradle/wrapper/        # Gradle wrapper distribution
├── src/                   # Spring Boot backend source code
│   ├── main/
│   │   ├── java/          # Enterprise backend logic
│   │   └── resources/     # Database migration scripts & configurations
│   └── test/              # Unit, Integration, and Performance test suites
├── Dockerfile             # Production Backend Dockerfile
├── build.gradle           # Root Gradle build configuration
└── docker-compose.yml     # Multi-container service orchestrator
