# Clean Architecture CRUD Application

Este proyecto, tambien está documentado en español, ver LEEME.MD.  
This project is documented in Spanish.

[LEEME.md](LEEME.md)

This is a multi-module Maven project implementing a clean architecture approach for a CRUD application.

## Project Structure

The project is organized into the following modules:

- **domain**: Contains the business entities and repository interfaces
- **application**: Contains the business logic and service interfaces
- **infrastructure**: Contains the implementation of repositories and external services
- **rest-api**: Contains the REST controllers and API endpoints
- **cli**: Contains the command-line interface application

## Building the Project

### Using Maven from Command Line

To build the project with tests:
```
mvn clean install
```

To build the project without running tests:
```
mvn clean install -DskipTests
```

### Using IntelliJ IDEA

This project includes pre-configured IntelliJ IDEA run configurations for building the project:

1. **Maven Clean Install with Tests**: Builds the project and runs all tests
2. **Maven Clean Install without Tests**: Builds the project without running tests

To use these configurations:
1. Open the project in IntelliJ IDEA
2. Select the desired configuration from the run configurations dropdown in the toolbar
3. Click the run button (green triangle)

For more details about these configurations, see the [IntelliJ IDEA Build Configurations](.idea/README.md).

## Running the Applications

### REST API

To run the REST API application:
```
cd rest-api
mvn spring-boot:run
```

### CLI Application

To run the CLI application:
```
cd cli
mvn spring-boot:run
```

## Documentation

- For REST API logging implementation details, see the [REST API README](rest-api/README.md).
- **API Documentation (Swagger UI)**: After starting the REST API, access the interactive API documentation at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI Specification**: Available at [http://localhost:8080/api-docs](http://localhost:8080/api-docs) when the API is running
