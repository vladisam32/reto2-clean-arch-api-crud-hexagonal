FROM openjdk:24-slim

# Establecer directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR de la aplicación
COPY target/*.jar app.jar

# Exponer el puerto que usa la aplicación
EXPOSE 8080

# Variables de entorno por defecto
ENV SPRING_DATASOURCE_URL=jdbc:h2:mem:supermarketdb
ENV SPRING_DATASOURCE_USERNAME=sa
ENV SPRING_DATASOURCE_PASSWORD=password
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update
ENV SERVER_PORT=8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]