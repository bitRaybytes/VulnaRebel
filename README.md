```text
██╗   ██╗██╗   ██╗██╗     ███╗   ██╗ █████╗ ██████╗ ███████╗██████╗ ███████╗██╗
██║   ██║██║   ██║██║     ████╗  ██║██╔══██╗██╔══██╗██╔════╝██╔══██╗██╔════╝██║
██║   ██║██║   ██║██║     ██╔██╗ ██║███████║██████╔╝█████╗  ██████╔╝█████╗  ██║
╚██╗ ██╔╝██║   ██║██║     ██║╚██╗██║██╔══██║██╔══██╗██╔══╝  ██╔══██╗██╔══╝  ██║
 ╚████╔╝ ╚██████╔╝███████╗██║ ╚████║██║  ██║██║  ██║███████╗██████╔╝███████╗███████╗
  ╚═══╝   ╚═════╝ ╚══════╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚═════╝ ╚══════╝╚══════╝
```

```text
There are many ways to spell its name...

ASCII
86 117 108 110 97 82 101 98 101 108

Binary
01010110 01110101 01101100 01101110 01100001 
01010010 01100101 01100010 01100101 01101100

Hexadecimal
56 75 6C 6E 61 52 65 62 65 6C
```

# VulnaRebel – A Portable Web Security Training Platform

⚠️ WARNING: VulnaRebel contains intentionally vulnerable code.   
Run locally only. Never expose to a public network.

> Author: Ray  

## Table of Content

  * [Overview](#overview)
  * [Requirements](#requirements)
  * [Getting Started](#getting-started)
  * [API Documentation](#api-documentation)
  * [Project Goals](#project-goals)
  * [Current Status](#current-status)
  * [Planned Challenges](#planned-challenges)
  * [Project Architecture](#project-architecture)
  * [Technology Stack](#technology-stack)
  * [Educational Purpose](#educational-purpose)
  * [Roadmap](#roadmap)
  * [Screenshots](#screenshots)
  * [Contributing](#contributing)
  * [License](#license)

## Overview

VulnaRebel is a portable and modular cybersecurity wargame designed to demonstrate real-world web application vulnerabilities in a controlled environment.

The project is built with pure Java, PostgreSQL, and Docker, without relying on heavyweight web frameworks. Its goal is to provide an educational platform where developers, students, and security enthusiasts can learn how common web vulnerabilities work, understand their impact, and practice identifying and exploiting them in a safe environment.

Every challenge is designed to resemble realistic application behavior while remaining isolated and reproducible through Docker.

## Requirements

- [Git](https://git-scm.com/)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)

## Getting Started

**Prerequisites:** Docker, Docker Compose

```bash
git clone https://github.com/bitRaybytes/VulnaRebel
cd vulnarebel
docker-compose up --build
```

Open `http://localhost:8080` in your browser.

⚠️ **WARNING**: VulnaRebel contains intentionally vulnerable code.
Run locally only. Never expose to a public network.

## API Documentation

Generate the Javadoc locally with:

```bash
mvn javadoc:javadoc
```

## Project Goals

- Build a lightweight web application framework for security challenges
- Demonstrate common web application vulnerabilities
- Keep the architecture modular and easily extensible
- Provide reproducible environments using Docker
- Focus on understanding vulnerabilities instead of using automated tools

## Current Status

VulnaRebel is currently under active development.

The first implemented challenges focuses on vulnerable login form that demonstrates SQL Injection, a search example demonstrating blind SQL Injection and a Reflected XSS challenge that demonstrates client side vulnerabilities.

Current implementation includes:

- Maven-based Java project
- Custom HTTP server `VulnaHttpServer`
- Dockerized application environment
- PostgreSQL database
- Configuration system using `application.properties`
- Database connection management `DatabaseManager`
- Modular project architecture prepared for future challenge modules
- Challenges: Login SQLI, Blind SQLI and Reflected XSS

## Planned Challenges

The long-term goal is to provide multiple independent challenge modules, including:

- SQL Injection
- Cross-Site Scripting (XSS)
- Cross-Site Request Forgery (CSRF)
- Insecure Direct Object References (IDOR)
- Server-Side Template Injection (SSTI)
- XML External Entity Injection (XXE)
- Command Injection
- File Upload Vulnerabilities
- JWT Vulnerabilities
- Insecure Deserialization

Each challenge will be self-contained and configurable, allowing new modules to be added without changing the core platform.

## Project Architecture

The project is divided into independent components:

```text
src/main/java/
  ├── article/       Challenge related resources like articles
  ├── challenge/     Challenges like SQLi, BlindSQLi ...
  ├── config/        Configuration loading
  ├── core/          Application, Main entry point
  ├── database/      DatabaseManager, SchemaInitializer
  ├── http/          VulnaHttpServer, Router, Route, BaseHandler
  ├── challenge/     
  │   ├── login/     SQL Injection challenge
  │   └── ...        More upcoming
  └── exceptions/    Custom exceptions
```

This architecture allows the platform to grow while keeping challenges isolated from the underlying infrastructure.

## Technology Stack

- Java
- Maven
- PostgreSQL
- Docker
- Docker Compose
- HTML
- CSS
- JDBC

## Educational Purpose

VulnaRebel is intended solely for educational purposes.

The application intentionally contains insecure code to demonstrate common security vulnerabilities. It should only be deployed in isolated environments such as local machines, virtual machines, or Docker containers.

Do not expose VulnaRebel to public networks or production environments.

## Roadmap

### Completed
- Maven project structure
- Custom HTTP server
- Routing system
- Database connection management
- Docker environment
- SQL Injection login challenge

### In Progress
- Html refinements

### Planned
- Challenge validation
- Blind SQL Injection
- Additional vulnerability categories

## Screenshots
### Dashboard
![](/Docs/screenshots/VWA_Dashboard.png)

### Challenges

<details><summary>SQL Injection (easy)</summary>

#### Login SQLi Challenge - Main
![](/Docs/screenshots/VWA_Login_Challenge.png)

#### Login SQLi Challenge - Failed Attempt
![](/Docs/screenshots/VWA_failedAttempt.png)

#### Login SQLi Challenge - Revealed flag
![](/Docs/screenshots/VWA_Flag.png)

</details>

<details><summary>Reflected Xss (easy)</summary>

#### Reflected Xss Challenge
![](/Docs/screenshots/VWA_xss_challenge.png)

#### Reflected Xss - Revealed flag
![](/Docs/screenshots/VWA_xss_flag.png)

</details>

## Developer Notes

Most users only read documentation.

Security professionals inspect everything.

```
Y3Rme0xldF9UaGVfQ2hhbGxlbmdlc19CZWdpbn0=
```

## Contributing

Contributions, ideas, bug reports, and feature suggestions are welcome.

As the project evolves, additional challenge modules and improvements to the core architecture will be added.

To run outside Docker for local development, you additionally need:
- JDK 25
- Maven 3.9+

Publish the database port for local JDBC connections by adding `5432:5432` under the `db` service ports in `docker-compose.yaml`,
then update `database.url.prod` in `DatabaseManager` to use `database.url.dev` instead of `db`.

## License

This project is under the MIT license.   
[License](LICENSE)