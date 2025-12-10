# Evetic - Event Ticketing System

> **Status**: First iteration in development 🚀

Evetic is a comprehensive event ticketing platform built with Spring Boot that manages events, tickets, QR codes, and ticket validations.

## 📋 Table of Contents

- [Project Overview](#project-overview)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Entities & Data Model](#entities--data-model)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Docker Compose Setup](#docker-compose-setup)
- [API Endpoints](#api-endpoints)
- [Development](#development)
- [Future Enhancements](#future-enhancements)

## 🎯 Project Overview

Evetic is a Spring Boot application designed to handle:
- **Event Management**: Create and manage events
- **Ticket System**: Issue and manage tickets for events
- **QR Code Generation**: Generate QR codes for tickets
- **Ticket Validation**: Validate tickets at event entry points
- **User Authentication**: Secure authentication (custom or Spring Security-based)
- **User Management**: Filter and manage users

The application is containerized and uses Docker Compose for easy deployment with PostgreSQL and Adminer.

## 🛠️ Tech Stack

### Backend
- **Java 17**: Primary programming language
- **Spring Boot 4.0.0**: Application framework
- **Spring Data JPA**: ORM and database access
- **Spring Security**: Authentication and authorization (customizable)

### Database
- **PostgreSQL**: Primary data persistence layer
- **H2 Database**: Testing database

### Authentication & Authorization
- **Spring Security**: Custom authentication 

### Additional Libraries
- **Lombok 1.18.36**: Code generation (annotations for boilerplate)
- **MapStruct 1.6.3**: Object mapping (DTOs to entities)
- **Springdoc OpenAPI**: API documentation and Swagger UI

### Development & Deployment
- **Maven**: Build automation
- **Docker & Docker Compose**: Containerization
- **Adminer**: Database management UI

## 📁 Project Structure

```
docker-compose.yml
HELP.md
mvnw
mvnw.cmd
pom.xml
README.md
src/
  main/
    java/
      org/
        zalmoxis/
          evetic/
            EveticApplication.java
            config/
            controllers/
            dtos/
            entities/
            exceptions/
            filters/
            mappers/
            repositories/
            services/
    resources/
      application.properties
      META-INF/
        orm.xml
      static/
      templates/
  test/
    java/
      org/
        zalmoxis/
          evetic/
            EveticApplicationTests.java
```

target/
  classes/
    application.properties
    META-INF/
      orm.xml
    org/
      zalmoxis/
        evetic/
          EveticApplication.class
          config/
          controllers/
          dtos/
          entities/
          exceptions/
          filters/
          mappers/
          repositories/
          services/

## 🗄️ Entities & Data Model

### User
- Represents application users
- Manages user authentication and profiles
- Relationships:
  - **ManyToMany**: Events (can attend/organize)
  - **OneToMany**: Tickets (owns tickets)

### Event
- Represents events in the system
- Tracks event details and status
- Status: Enum-based (draft, published, ongoing, completed, cancelled)
- Relationships:
  - **ManyToOne**: Event organizer (User)
  - **OneToMany**: Tickets
  - **ManyToMany**: Attendees (Users)

### TicketType
- Defines different ticket categories/tiers
- Examples: VIP, General Admission, Early Bird

### Ticket
- Represents an actual ticket issued for an event
- Status: Pending, Active, Used, Cancelled
- Relationships:
  - **ManyToOne**: Event
  - **ManyToOne**: TicketType
  - **ManyToOne**: User (ticket owner)
  - **OneToMany**: QR Codes

### QrCode
- Generated for each ticket
- Used for ticket validation at entry points
- Status: Active, Used, Revoked, Expired
- Relationships:
  - **ManyToOne**: Ticket
  - **OneToMany**: Validations

### TicketValidation
- Records validation events (when tickets are scanned/verified)
- Validation Method: QR Code, Manual, API, NFC (extensible)
- Status: Pending, Success, Failed, Cancelled
- Relationships:
  - **ManyToOne**: QR Code
  - **ManyToOne**: User (validator)

## 🔧 Prerequisites

Before running the project, ensure you have:

- **Java 17+** installed ([Download](https://openjdk.java.net/))
- **Maven 3.6+** ([Download](https://maven.apache.org/download.cgi))
- **Docker & Docker Compose** ([Download](https://www.docker.com/products/docker-desktop))
- **Git** for version control

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone <repository-url>
cd evetic
```

### 2. Start Docker Services
```bash
docker-compose up -d
```

This will start:
- **PostgreSQL**: `jdbc:postgresql://localhost:5432/evetic`
- **Adminer**: `http://localhost:8181` (Database UI)

### 3. Build the Project
```bash
mvnw clean install
```

Or if using Maven directly:
```bash
mvn clean install
```

### 4. Run the Application
```bash
mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

### 5. Verify Setup
- Application should be running and connected to PostgreSQL
- Check logs for successful startup

## ⚙️ Configuration

### Application Properties (`src/main/resources/application.properties`)

```properties
# Application
spring.application.name=evetic

# PostgreSQL Database
spring.datasource.url=jdbc:postgresql://localhost:5432/evetic
spring.datasource.username=postgres
spring.datasource.password=adminPassword!

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update          # Auto-create/update schema
spring.jpa.show-sql=true                      # Log SQL queries
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true

```

### Security Configuration
- All endpoints require authentication (customizable via Spring Security)
- CSRF protection is disabled (stateless API)
- Session creation is disabled (STATELESS policy)
- Custom filters can process user information from tokens or credentials

## 🐳 Docker Compose Setup

The `docker-compose.yml` includes:

### PostgreSQL
- **Port**: 5432
- **Password**: `adminPassword!`
- **Database**: Auto-created as `evetic`

### Adminer
- **Port**: 8181
- **Purpose**: Web UI for database management
- **Login**: Use PostgreSQL credentials

### Stop Services
```bash
docker-compose down
```

### View Logs
```bash
docker-compose logs -f <service-name>
```

## 📡 API Endpoints

*(To be documented with controllers)*

Current project structure includes:
- User filtering and management
- Event CRUD operations
- Ticket management
- QR code generation
- Ticket validation processing

## 💻 Development

### Building
```bash
mvnw clean install
```

### Testing
```bash
mvnw test
```

### Code Generation
- **Lombok**: Reduces boilerplate with `@Getter`, `@Setter`, `@Builder`, etc.
- **MapStruct**: Generates type-safe bean mappings between entities and DTOs

### Database Management
- Hibernate auto-updates schema based on entities (`ddl-auto=update`)
- Access via Adminer: `http://localhost:8181`

## 🔒 Security Notes

⚠️ **Development Only**:
- Hardcoded credentials in `application.properties`
- CSRF disabled for API simplicity

**For Production**:
- Use environment variables for sensitive data
- Enable proper CSRF protection if needed
- Configure proper authentication and validation
- Use secure password policies
- Enable HTTPS/TLS

## 📝 Development Roadmap

### Completed
- ✅ Core entity model (User, Event, Ticket, QrCode, TicketValidation)
- ✅ PostgreSQL integration
- ✅ Custom authentication with Spring Security
- ✅ Docker Compose setup
- ✅ JPA auditing and configuration

### In Progress / Planned
- 🚧 REST API endpoints for all entities
- 🚧 DTO classes and MapStruct mappings
- 🚧 Business logic services
- 🚧 Request validation
- 🚧 QR code generation library integration
- 🚧 Event notification system
- 🚧 Comprehensive API documentation (Swagger/OpenAPI)
- 🚧 Advanced filtering and search
- 🚧 Integration tests
- 🚧 Performance optimization

## 🐛 Known Issues & TODOs

- Controllers not yet implemented
- Service layer not yet implemented
- API documentation pending
- Comprehensive error handling needed

## 📚 References

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [Lombok Documentation](https://projectlombok.org/)
- [MapStruct Documentation](https://mapstruct.org/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)

## 🤝 Contribution Guidelines

We welcome contributions! To contribute:
1. Fork the repository and create your branch from `main`.
2. Ensure your code follows the existing style and conventions.
3. Write clear commit messages and document your changes.
4. Add or update tests as needed.
5. Open a pull request with a detailed description of your changes.

For major changes, please open an issue first to discuss what you would like to change.

## 📬 Contact & Support

- For issues, please use the [GitHub Issues](https://github.com/your-repo/issues) page.
- For questions or support, contact the maintainer at: your.email@example.com

---

**Last Updated**: December 2025  
**Current Version**: 0.0.1-SNAPSHOT  
**Java Version**: 17  
**Spring Boot Version**: 4.0.0

---

**Note**: This is the first iteration of the project. The codebase is actively under development and subject to significant changes.
