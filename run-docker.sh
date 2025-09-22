#!/bin/bash
# Shell script for running the Supermarket API application with Docker
# This script focuses specifically on Docker deployment

echo "==================================================="
echo "API de Supermercado - Despliegue con Docker"
echo "==================================================="
echo

# Verificar si Docker está instalado
if ! command -v docker &> /dev/null; then
  echo "Error: Docker no está instalado o no está en el PATH."
  echo "Por favor, instala Docker desde https://www.docker.com/get-started"
  read -p "Presione Enter para continuar..."
  exit 1
else
  docker_version=$(docker --version)
  echo "Docker detectado: $docker_version"
fi

# Verificar si Docker Compose está instalado
if ! command -v docker-compose &> /dev/null; then
  echo "Error: Docker Compose no está instalado o no está en el PATH."
  echo "Docker Compose debería estar incluido con Docker Desktop o puede instalarse por separado."
  read -p "Presione Enter para continuar..."
  exit 1
else
  compose_version=$(docker-compose --version)
  echo "Docker Compose detectado: $compose_version"
fi

# Compilar el proyecto con Maven
echo
echo "Compilando el proyecto con Maven..."
./mvnw clean package -DskipTests

if [ $? -ne 0 ]; then
  echo "Error: La compilación con Maven ha fallado."
  read -p "Presione Enter para continuar..."
  exit 1
fi

# Crear directorio para datos persistentes si no existe
if [ ! -d "data" ]; then
  echo "Creando directorio de datos..."
  mkdir -p data
fi

# Iniciar los contenedores con Docker Compose
echo
echo "Iniciando los contenedores con Docker Compose..."
docker-compose up -d --build

# Verificar si los contenedores están en ejecución
echo
echo "Verificando el estado de los contenedores..."
docker-compose ps

echo
echo "==================================================="
echo "La API de Supermercado está en ejecución!"
echo
echo "Accede a la API en: http://localhost:8080/api"
echo "Accede a la consola H2 en: http://localhost:8080/h2-console"
echo
echo "Comandos útiles:"
echo "- Para ver los logs: docker-compose logs -f"
echo "- Para detener la aplicación: docker-compose down"
echo "- Para reiniciar la aplicación: docker-compose restart"
echo "==================================================="