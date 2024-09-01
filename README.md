# vertx-sandbox

## Overview

The `vertx-sandbox` project is built using Java and Maven. It serves as a sandbox environment for
experimenting with Vert.x, a toolkit for building reactive applications on the JVM.

## Project Structure

- **Parent Project**: `vertx-sandbox`
    - **Module**: `vertx-web-bug`

## Maven Configuration

### Parent `pom.xml`

The parent `pom.xml` file defines the overall project structure, including the group ID, artifact
ID, version, and modules. It also includes properties for the Maven compiler and dependency
management for Vert.x.

### Module `vertx-web-bug/pom.xml`

The module `pom.xml` file inherits from the parent `pom.xml` and defines the specific configuration
for the `vertx-web-bug` module.

## Installation Instructions :rocket:

To install and run the vertx-sandbox project, follow these steps:

1. Clone the repository:

```bash
git clone https://github.com/vertx-sandbox.git
cd vertx-sandbox
```

2. Build the project using Maven:  
```bash
./mvnw clean install
```
## Additional Information

- __SCM__: The project uses Git for source control management.
- __Java Version__: The project is configured to use Java 21.
- __JUnit Version__: The project uses JUnit 5.11.0 for testing.
- __Vert.x Version__: The project uses Vert.x 4.5.9.