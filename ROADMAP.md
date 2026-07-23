# VulnaRebel – Development Roadmap (v0.1)

Approach is not to implement step by step.

Instead, I will build a full vertical slice first.

These are the main steps:

1. ConfigurationLoader → Load and validate all configuration. 
2. DatabaseManager → Establish a JDBC connection and implement the retry logic. 
3. HttpServer → Start a basic server that can return "Hello, VulnaRebel!". 
4. Router → Route `/` to a handler. 
5. Static file serving → Serve `login.article` from `/static`. 
6. LoginService → Read form values and execute the (intentionally vulnerable) SQL query. 
7. Database initialization → Load `schema.sql` and `seed.sql`. 
8. Docker integration → Verify that everything starts with a single docker compose up.

## Phase 1 – Project Cleanup

* [ ] Remove `archetype-resources`
* [ ] Remove unnecessary Maven archetype files
* [X] Rename package `Service` → `service`
* [X] Ensure all Java packages follow lowercase naming conventions
* [X] Create proper package hierarchy

---

## Phase 2 – Configuration System

### Logging
* [ ] Log errors and infos from all classes
* [ ] Time and Date if possible
* [ ] Output in a file under `logs/` directory

### Configuration

* [X] Implement `ConfigurationLoader`
* [X] Read `application.properties`
* [ ] Support environment variable overrides
* [X] Create immutable `Configuration` object
* [X] Validate mandatory configuration values
* [ ] Implement default values when properties are missing

#### Configuration Topics

* [X] HTTP port
* [X] Database host
* [X] Database port
* [X] Database name
* [X] Database username
* [X] Database password
* [X] Retry count
* [X] Retry delay
* [X] Logging level
* [X] Static web directory

---

## Phase 3 – HTTP Infrastructure

### HttpServer

* [X] Create HTTP server
* [X] Start server
* [X] Stop server
* [X] Register routes

### Router

* [X] Route registration
* [ ] Route lookup
* [ ] GET support
* [ ] POST support
* [ ] Default 404 handler

#### Request / Response

* [X] Parse HTTP request
* [X] Read headers
* [X] Read query parameters
* [X] Read POST body
* [X] Create HTTP responses
* [X] Return HTML pages
* [ ] Return error pages

---

## Phase 4 – Database Layer

### DatabaseManager

* [X] Create JDBC connection
* [X] Close connection safely
* [X] Retry failed connections
* [ ] Connection validation

### SchemaInitializer

* [X] Execute `schema.sql`
* [X] Execute `seed.sql`
* [ ] Skip initialization if already initialized
* [ ] Log initialization status

---

## Phase 5 – Resources

### Resources

* [ ] Create `/web`
* [ ] Create `/database`
* [X] Create `/challenges`

### Database

* [X] schema.sql
* [X] seed.sql

### Web

* [ ] index.article
* [ ] login.article
* [ ] css/
* [ ] js/

---

## Phase 6 – Login Challenge (SQL Injection)

### Frontend

* [X] Login form
* [X] Username field
* [X] Password field
* [X] Submit button

### Backend

* [X] Login handler
* [X] Login service
* [X] SQL query execution
* [X] Return login result

### Database

* [X] users table
* [ ] Admin account
* [ ] Normal user account

---

## Phase 7 – Docker

### Dockerfile

* [X] Build application
* [ ] Package JAR
* [X] Start application

### docker-compose

* [ ] Java container
* [X] PostgreSQL container
* [ ] Network configuration
* [ ] Environment variables
* [ ] Persistent volume (optional)

---

## Phase 8 – Logging

* [X] Startup logging
* [ ] Database logging
* [ ] HTTP request logging
* [ ] Error logging
* [ ] Challenge logging

---

## Phase 9 – Future Engine

### Challenge Manager

* [ ] Challenge registry
* [ ] Challenge loader
* [ ] Current challenge

### Validation

* [ ] Validate challenge success
* [ ] Detect successful exploitation
* [ ] Generate success message

---

## Phase 10 – Future Modules

### SQL Injection

* [X] Login Bypass
* [ ] UNION Injection
* [X] Boolean Blind
* [ ] Time Based
* [ ] Stacked Queries

### XSS

* [X] Reflected XSS
* [X] Stored XSS
* [ ] DOM XSS

### Additional Modules

* [ ] CSRF
* [ ] IDOR
* [ ] File Upload
* [ ] XXE
* [ ] SSTI
* [ ] Command Injection
* [ ] JWT
* [ ] Deserialization

---

## Stretch Goals

* [X] Challenge configuration loader
* [X] Challenge-specific schema.sql
* [X] Challenge-specific seed.sql
* [ ] Flag validation engine
* [ ] Progress tracking
* [ ] Automatic challenge reset
* [ ] Admin reset endpoint
* [ ] Docker image release
* [ ] GitHub Actions CI
* [X] Documentation
* [X] Write README
