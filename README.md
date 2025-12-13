# Evetic - Event Ticketing Management System

A modern, RESTful event ticketing platform built with Spring Boot that enables event organizers to create, manage, and validate tickets through QR code technology.

## Overview

Evetic is a comprehensive event management and ticketing system designed to streamline the entire lifecycle of event ticket management. From event creation and ticket type configuration to QR-based validation, the platform provides a complete solution for event organizers and attendees.

## Key Features

### 🎫 Event Management
- **Event Lifecycle Control**: Create, update, and manage events through their complete lifecycle
- **Status Management**: Track events through DRAFT, PUBLISHED, CANCELLED, and COMPLETED states
- **Event Details**: Manage comprehensive event information including name, description, location, dates, and capacity
- **Advanced Filtering**: Query events by status, date ranges, and other criteria
- **Public API**: Dedicated endpoints for browsing published events

### 🎟️ Ticket System
- **Multiple Ticket Types**: Support for diverse ticket categories (VIP, Standard, Early Bird, etc.)
- **Dynamic Inventory**: Real-time tracking of ticket availability and sales
- **Automated Generation**: Automatic ticket creation with unique identifiers
- **Status Tracking**: Monitor tickets through ACTIVE, USED, CANCELLED, and EXPIRED states
- **Purchase Controls**: Configurable purchase limits and availability management
- **Pricing Flexibility**: Individual pricing per ticket type

### 📱 QR Code Integration
- **Automatic Generation**: Each ticket receives a unique QR code upon creation
- **Secure Validation**: Cryptographically secure QR codes for ticket verification
- **Status Monitoring**: Track QR codes as ACTIVE, SCANNED, or INVALID
- **Duplicate Prevention**: Built-in safeguards against multiple ticket scans
- **Efficient Scanning**: Fast validation for seamless event entry

### ✅ Validation System
- **Real-time Validation**: Instant ticket verification at event entry points
- **Multiple Methods**: Support for QR_CODE scanning and MANUAL validation
- **Comprehensive History**: Complete audit trail of all validation attempts
- **Status Responses**: Detailed validation results (SUCCESS, ALREADY_VALIDATED, INVALID_TICKET, EXPIRED_TICKET)
- **Event Association**: Validation tied to specific events for security

### 🔐 Security & Authentication
- **JWT Authentication**: Stateless token-based authentication system
- **Role-Based Access Control**: Three-tier access system (ORGANIZER, CUSTOMER, ADMIN)
- **Endpoint Protection**: Secured REST endpoints with Spring Security
- **User Management**: Complete registration and authentication flow
- **Password Security**: Encrypted password storage with BCrypt

## Technology Stack

**Backend Framework**
- Spring Boot 3.x
- Spring Data JPA
- Spring Security
- Spring Web

**Database**
- PostgreSQL
- Hibernate ORM
- Connection Pooling

**Security**
- JWT (JSON Web Tokens)
- BCrypt Password Encoding
- Spring Security Filter Chain

**Libraries & Tools**
- ZXing (QR Code Generation)
- Lombok (Code Generation)
- MapStruct (Object Mapping)
- OpenAPI 3.0 / Swagger (API Documentation)

**Build & Development**
- Maven
- Java 17+

## Architecture

### Project Structure

```
src/main/java/org/zalmoxis/evetic/
├── config/              # Application configuration
│   ├── JpaConfiguration.java
│   ├── JwtUtil.java
│   ├── OpenApiConfig.java
│   ├── QrCodeConfig.java
│   └── SecurityConfig.java
├── controllers/         # REST API endpoints
│   ├── AuthController.java
│   ├── EventController.java
│   ├── PublishedEventController.java
│   ├── TicketController.java
│   ├── TicketTypeController.java
│   ├── TicketValidationController.java
│   └── GlobalExceptionHandler.java
├── dtos/               # Data Transfer Objects
│   ├── auth/
│   ├── event/
│   ├── ticket/
│   ├── ticketType/
│   └── ticketValidation/
├── entities/           # JPA entities
│   ├── Event.java
│   ├── Ticket.java
│   ├── TicketType.java
│   ├── TicketValidation.java
│   ├── QrCode.java
│   └── User.java
├── exceptions/         # Custom exception handling
├── filters/           # Security filters
│   └── JwtAuthenticationFilter.java
├── mappers/           # Entity-DTO mappers
├── repositories/      # Data access layer
│   ├── EventRepo.java
│   ├── TicketRepo.java
│   ├── TicketTypeRepo.java
│   ├── TicketValidationRepo.java
│   ├── QrCodeRepo.java
│   └── UserRepo.java
├── services/          # Business logic layer
│   └── implementations/
└── utils/             # Utility classes
```

### Core Entities

**Event**: Central entity representing an event with dates, location, capacity, and status

**TicketType**: Defines available ticket categories for events with pricing and limits

**Ticket**: Individual ticket instance linked to a customer and event

**QrCode**: Unique QR code associated with each ticket for validation

**TicketValidation**: Records of ticket validation attempts and results

**User**: System users with roles (ORGANIZER, CUSTOMER, ADMIN)


