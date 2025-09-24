# Web Module with Thymeleaf UI

Este módulo proporciona una interfaz web basada en Thymeleaf para el sistema de gestión de supermercado. Utiliza la misma API REST que la interfaz de línea de comandos (CLI).

## Características

- Interfaz web amigable con Bootstrap
- Gestión de productos (listar, crear, editar, eliminar)
- Gestión de inventario (pendiente de implementar)
- Gestión de ventas (pendiente de implementar)
- Gestión de cajeros (pendiente de implementar)

## Requisitos

- Java 24
- Maven
- Módulo REST API en ejecución

## Configuración

La configuración se encuentra en el archivo `application.properties`. Los principales parámetros son:

```properties
# Puerto del servidor web (diferente al de la API REST)
server.port=8081
server.servlet.context-path=/web

# URL base de la API REST
api.base-url=http://localhost:8080/api
```

## Ejecución

Para ejecutar el módulo web, asegúrese de que el módulo REST API esté en ejecución y luego ejecute:

```bash
mvn spring-boot:run -pl web
```

O desde el directorio del módulo web:

```bash
cd web
mvn spring-boot:run
```

## Acceso

Una vez iniciado, puede acceder a la aplicación web en:

```
http://localhost:8081/web
```

## Estructura de Directorios

- `src/main/java/org/litethinking/supermercado/web`: Código fuente Java
  - `WebApplication.java`: Clase principal de la aplicación
  - `config/`: Configuraciones
  - `controller/`: Controladores web
  - `service/`: Servicios que interactúan con la API REST
- `src/main/resources`: Recursos
  - `templates/`: Plantillas Thymeleaf
  - `static/`: Recursos estáticos (CSS, JS, imágenes)
  - `application.properties`: Configuración de la aplicación

## Implementación Actual

Actualmente, solo se ha implementado la gestión de productos. Las demás funcionalidades (inventario, ventas, cajeros) están pendientes de implementar.

## Notas

- Este módulo utiliza Bootstrap para el diseño de la interfaz.
- Se comunica con la API REST mediante RestTemplate.
- Utiliza Thymeleaf como motor de plantillas.