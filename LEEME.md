# DOCUMENTACIÓN TÉCNICA - SISTEMA DE GESTIÓN DE SUPERMERCADO

## ARQUITECTURA DEL SISTEMA

Este proyecto implementa una arquitectura limpia (Clean Architecture) con una estructura modular que separa claramente las responsabilidades y dependencias. La aplicación está diseñada siguiendo los principios SOLID y el patrón de arquitectura clean code.

### Estructura de Módulos

El proyecto está organizado en los siguientes módulos:

1. **domain**: Contiene las entidades de negocio y las interfaces de repositorio
   - Entidades: Producto, Cajero, Inventario, Venta, ItemVenta
   - Interfaces de repositorio: Define contratos para acceso a datos

2. **application**: Contiene la lógica de negocio y las interfaces de servicio
   - Servicios: Implementa casos de uso de la aplicación
   - Mappers: Convierte entre entidades de dominio y DTOs

3. **infrastructure**: Contiene la implementación de repositorios y servicios externos
   - Implementaciones de repositorios: Acceso a datos concreto
   - Configuración de persistencia

4. **rest-api**: Contiene los controladores REST y endpoints de la API
   - Controladores: Expone funcionalidades como API REST
   - Configuración de Spring Boot para la API

5. **cli**: Contiene la aplicación de interfaz de línea de comandos
   - Servicios CLI: Interactúa con la API REST
   - Interfaz de usuario basada en consola

6. **shared-dto**: Contiene los objetos de transferencia de datos compartidos
   - DTOs: Objetos inmutables para transferencia de datos entre capas

### Flujo de Datos

1. La capa de presentación (CLI o REST API) recibe solicitudes del usuario
2. Las solicitudes se transforman en llamadas a servicios de aplicación
3. La capa de aplicación implementa la lógica de negocio utilizando entidades de dominio
4. La capa de infraestructura proporciona implementaciones concretas para persistencia
5. Los datos fluyen de vuelta a través de las capas hasta el usuario

## STACK TECNOLÓGICO

### Lenguaje y Plataforma
- **Java 24**: Lenguaje de programación principal
- **Spring Boot 3.5.5**: Framework para desarrollo de aplicaciones Java

### Persistencia y Acceso a Datos
- **Spring Data**: Para implementaciones de repositorios
- **Almacenamiento CSV**: Para datos persistentes en archivos CSV

### API REST
- **Spring MVC**: Para controladores REST
- **Spring RestTemplate**: Para comunicación HTTP entre servicios
- **Swagger/OpenAPI**: Documentación interactiva de la API disponible en http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: Especificación de la API disponible en http://localhost:8080/api-docs

### Interfaz de Usuario
- **CLI (Command Line Interface)**: Interfaz basada en consola para interacción con el usuario

### Logging y Monitoreo
- **Log4j2**: Framework de logging
- **SLF4J**: Fachada para logging

### Herramientas de Desarrollo
- **Maven**: Gestión de dependencias y construcción del proyecto
- **JUnit 5**: Framework para pruebas unitarias
- **Mockito**: Framework para mocks en pruebas

### Patrones de Diseño Implementados
- **Patrón Repositorio**: Para abstracción de acceso a datos
- **Patrón Servicio**: Para encapsular lógica de negocio
- **Patrón DTO (Data Transfer Object)**: Para transferencia de datos entre capas
- **Patrón Mapper**: Para conversión entre entidades y DTOs
- **Patrón Inyección de Dependencias**: Para acoplamiento débil entre componentes

## DIAGRAMA DE ARQUITECTURA

```
┌─────────────────┐      ┌─────────────────┐
│                 │      │                 │
│  CLI (Cliente)  │◄────►│    REST API     │
│                 │      │                 │
└────────┬────────┘      └────────┬────────┘
         │                        │
         │                        │
         │                        ▼
         │               ┌─────────────────┐
         │               │                 │
         └──────────────►│   Aplicación    │
                         │                 │
                         └────────┬────────┘
                                  │
                                  │
                                  ▼
                         ┌─────────────────┐
                         │                 │
                         │     Dominio     │
                         │                 │
                         └────────┬────────┘
                                  │
                                  │
                                  ▼
                         ┌─────────────────┐
                         │                 │
                         │ Infraestructura │
                         │                 │
                         └─────────────────┘
```

## GUÍA DE DESARROLLO

### Requisitos para Desarrollo
- JDK 24 o superior
- Maven 3.8 o superior
- IDE compatible con Java (IntelliJ IDEA, Eclipse, VS Code)

### Compilación del Proyecto
```bash
mvn clean install
```

### Ejecución de Pruebas
```bash
mvn test
```

### Ejecución de la Aplicación
1. Iniciar la API REST:
```bash
cd rest-api
mvn spring-boot:run
```

2. Iniciar la CLI:
```bash
cd cli
mvn spring-boot:run
```

### Documentación de la API
Una vez que la API REST está en funcionamiento, puede acceder a la documentación interactiva:

1. Abra un navegador web y visite:
```
http://localhost:8080/swagger-ui.html
```

Esta interfaz de Swagger UI le permite:
- Explorar todos los endpoints disponibles
- Ver los modelos de datos y esquemas
- Probar las operaciones directamente desde el navegador
- Entender los parámetros de entrada y respuestas

También puede acceder a la especificación OpenAPI en formato JSON:
```
http://localhost:8080/api-docs
```

Esta documentación es esencial para:
- Desarrolladores que necesiten integrar con la API
- Pruebas de integración
- Generación de clientes para diferentes lenguajes

### Estructura de Paquetes
- `org.litethinking.domain`: Entidades y repositorios
- `org.litethinking.application`: Servicios e implementaciones
- `org.litethinking.infrastructure`: Implementaciones de repositorios
- `org.litethinking.restapi`: Controladores REST
- `org.litethinking.cli`: Aplicación de línea de comandos
- `org.litethinking.shareddto`: DTOs compartidos

## EXTENSIBILIDAD Y MANTENIMIENTO

El diseño modular y la arquitectura limpia facilitan:

1. **Extensibilidad**: Nuevas funcionalidades pueden agregarse con mínimo impacto en el código existente
2. **Testabilidad**: Cada capa puede probarse de forma aislada
3. **Mantenibilidad**: La separación clara de responsabilidades facilita el mantenimiento
4. **Adaptabilidad**: Cambiar implementaciones (como el mecanismo de persistencia) requiere cambios mínimos

## CONSIDERACIONES DE SEGURIDAD

- La aplicación no implementa autenticación robusta en esta versión
- Para entornos de producción, se recomienda implementar:
  - Autenticación OAuth2 o JWT
  - HTTPS para comunicaciones seguras
  - Validación de entrada en todos los endpoints
  - Manejo adecuado de excepciones y errores
