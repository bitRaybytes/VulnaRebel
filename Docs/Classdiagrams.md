# Classdiagrams Of VulnaRebel

This document provides architectural overviews of VulnaRebel's main component groups. Each diagram was generated from PlantUML
source files located in `./resources/classdiagrams/`.


## Classdiagramm

### Web Application (svg)

Shows the relationship between `Main`, `Application`, `VulnaHttpServer`, `Router`, `Route`, and `BaseHandler` – the infrastructure every challenge builds on.

![Class diagram showing the core application architecture](./resources/classdiagrams/vwa-classdiagram.svg)

### Login SQL Injection (svg)

Shows how `LoginSqliChallenge` wires together `LoginHandler`, `LoginService`, `DatabaseManager`, and `SchemaInitializer` following the modular challenge pattern.

![Class diagram showing the Login SQL Injection challenge module](./resources/classdiagrams/vwa-loginsqliClassdiagram.svg)

### Resources / Articles (svg)

Shows how `ResourceHandler`, `ResourceIndexHandler`, `TemplateRenderer`, and `ArticleCard` collaborate to serve the knowledge centre.

![Class diagram showing the Resources and Articles architecture](./resources/classdiagrams/vwa-articleClassdiagram.svg)






