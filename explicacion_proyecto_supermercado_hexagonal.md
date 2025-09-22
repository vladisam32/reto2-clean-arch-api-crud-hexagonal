# Explicación del Proyecto de Simulación de Supermercado con Arquitectura Hexagonal

## 1. Introducción

Este documento explica el proyecto de simulación de supermercado implementado con arquitectura hexagonal (también conocida como arquitectura de puertos y adaptadores). El proyecto es una aplicación CRUD que gestiona las entidades principales de un supermercado, como productos, inventario, cajeros, clientes y ventas.

## 2. Arquitectura Hexagonal: Principios Fundamentales

La arquitectura hexagonal, propuesta por Alistair Cockburn, es un estilo arquitectónico que permite a una aplicación ser igualmente conducida por usuarios, programas, pruebas automatizadas o scripts por lotes, y ser desarrollada y probada de forma aislada de sus eventuales dispositivos y bases de datos en tiempo de ejecución.

### Principios clave:

1. **Separación de Preocupaciones**: Separa claramente la lógica de negocio de los detalles técnicos.
2. **Independencia de Frameworks**: El dominio no depende de frameworks externos.
3. **Testabilidad**: Facilita las pruebas unitarias al aislar el dominio.
4. **Mantenibilidad**: Cambios en la infraestructura no afectan al dominio.
5. **Flexibilidad**: Permite cambiar fácilmente las implementaciones de infraestructura.

### Componentes principales:

- **Dominio (Hexágono Interior)**: Contiene las entidades de negocio y la lógica central.
- **Puertos**: Interfaces que definen cómo el dominio interactúa con el exterior.
  - **Puertos Primarios (Input)**: Definen servicios que el dominio ofrece.
  - **Puertos Secundarios (Output)**: Definen servicios que el dominio requiere.
- **Adaptadores**: Implementaciones concretas de los puertos.
  - **Adaptadores Primarios (Input)**: Consumen los puertos primarios (ej. controladores REST).
  - **Adaptadores Secundarios (Output)**: Implementan los puertos secundarios (ej. repositorios).

## 3. Implementación en el Proyecto de Supermercado

### Estructura del Proyecto

El proyecto está organizado en módulos Maven que reflejan la arquitectura hexagonal:

1. **domain**: Contiene el núcleo de la aplicación
   - Entidades de negocio (Producto, Cajero, Cliente, Inventario, Venta)
   - Puertos de entrada y salida (interfaces)

2. **application**: Implementa la lógica de aplicación
   - Servicios que implementan los puertos primarios
   - Orquestación de casos de uso
   - Implementación de CQRS (Command Query Responsibility Segregation)

3. **infrastructure**: Contiene adaptadores secundarios
   - Implementaciones de repositorios
   - Persistencia de datos
   - Mapeo entre entidades de dominio y entidades de persistencia

4. **rest-api**: Contiene adaptadores primarios
   - Controladores REST
   - Configuración de Spring Boot
   - Documentación de API (Swagger)

5. **cli**: Interfaz de línea de comandos
   - Cliente que consume la API REST
   - Interfaz de usuario basada en consola

6. **shared-dto**: DTOs compartidos entre módulos
   - Objetos de transferencia de datos
   - Comandos y consultas CQRS

### Flujo de Datos en la Arquitectura

1. **Solicitud Externa**: Un cliente (navegador, CLI, etc.) envía una solicitud.
2. **Adaptador Primario**: Un controlador REST recibe la solicitud y la convierte en una llamada a un servicio de aplicación.
3. **Puerto Primario**: Define la interfaz que el adaptador primario utiliza para comunicarse con el dominio.
4. **Servicio de Aplicación**: Implementa el puerto primario y orquesta la lógica de negocio.
5. **Entidades de Dominio**: Contienen la lógica de negocio central.
6. **Puerto Secundario**: Define la interfaz que el dominio utiliza para comunicarse con servicios externos.
7. **Adaptador Secundario**: Implementa el puerto secundario para proporcionar servicios externos (como persistencia).
8. **Infraestructura Externa**: Base de datos, servicios externos, etc.

## 4. Componentes Principales del Proyecto

### Dominio

- **Entidades**: Clases como `Producto`, `Cajero`, `Cliente`, `Inventario` y `Venta` que representan los conceptos centrales del negocio.
- **Puertos de Entrada**: Interfaces como `ServicioProductoPort` que definen las operaciones que la aplicación ofrece.
- **Puertos de Salida**: Interfaces como `RepositorioProductoPort` que definen las operaciones que la aplicación requiere.

### Aplicación

- **Servicios**: Clases como `ServicioProductoImpl` que implementan los puertos de entrada y orquestan la lógica de negocio.
- **Comandos y Consultas**: Implementación del patrón CQRS con clases como `CreateProductCommand` y `GetProductByIdQuery`.
- **Manejadores**: Clases como `CreateProductCommandHandler` que procesan comandos y consultas específicos.

### Infraestructura

- **Adaptadores de Repositorio**: Clases como `AdaptadorRepositorioProducto` que implementan los puertos de salida.
- **Entidades JPA**: Clases como `EntidadJpaProducto` que mapean las entidades de dominio a la persistencia.
- **Configuración**: Configuración de Spring, JPA, etc.

### API REST

- **Controladores**: Clases como `ControladorProducto` que exponen la funcionalidad como API REST.
- **DTOs**: Objetos de transferencia de datos para la comunicación con clientes.
- **Documentación**: Anotaciones Swagger para documentar la API.

## 5. Diagrama de la Arquitectura

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

## 6. Ventajas de la Arquitectura Hexagonal en este Proyecto

1. **Mantenibilidad**: La separación clara de responsabilidades facilita el mantenimiento del código.
2. **Testabilidad**: Es fácil probar cada componente de forma aislada mediante mocks o stubs.
3. **Flexibilidad**: Se pueden cambiar implementaciones (como el mecanismo de persistencia) sin afectar al dominio.
4. **Escalabilidad**: La arquitectura modular permite escalar componentes específicos según sea necesario.
5. **Evolución**: Facilita la evolución del sistema al minimizar el impacto de los cambios.

## 7. Conclusión

El proyecto de simulación de supermercado implementa la arquitectura hexagonal de manera efectiva, separando claramente las preocupaciones y permitiendo que el dominio se mantenga aislado de los detalles técnicos. Esta arquitectura proporciona una base sólida para el desarrollo, prueba y mantenimiento de la aplicación, y permite que evolucione con el tiempo sin acumular deuda técnica significativa.

La implementación incluye características avanzadas como CQRS, que separa las operaciones de lectura y escritura, y una API REST bien documentada con Swagger. El proyecto demuestra cómo la arquitectura hexagonal puede aplicarse a un sistema de gestión empresarial como un supermercado, proporcionando una solución robusta y mantenible.