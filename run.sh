#!/bin/bash
# Shell script for running the Supermarket API application
# This script provides options for compiling, running with Docker, or running directly with Java

# Function to display the menu
show_menu() {
  echo "==================================================="
  echo "API de Supermercado - Menu de Opciones"
  echo "==================================================="
  echo
  echo "1. Compilar con pruebas (mvn clean install)"
  echo "2. Compilar sin pruebas (mvn clean install -DskipTests)"
  echo "3. Desplegar con Docker"
  echo "4. Ejecutar con Spring Boot"
  echo "5. Salir"
  echo
  read -p "Seleccione una opcion (1-5): " opcion
  echo
  
  case $opcion in
    1) compilar_con_pruebas ;;
    2) compilar_sin_pruebas ;;
    3) desplegar_docker ;;
    4) ejecutar_springboot ;;
    5) salir ;;
    *) 
      echo "Opción inválida. Por favor, seleccione una opción válida (1-5)."
      read -p "Presione Enter para continuar..."
      ;;
  esac
}

# Function to compile with tests
compilar_con_pruebas() {
  echo
  echo "==================================================="
  echo "Compilando el proyecto con pruebas..."
  echo "==================================================="
  
  ./mvnw clean install
  
  if [ $? -ne 0 ]; then
    echo "Error: La compilacion con Maven ha fallado."
    read -p "Presione Enter para continuar..."
    exit 1
  fi
  
  echo
  echo "Compilacion con pruebas completada exitosamente."
  read -p "Presione Enter para continuar..."
}

# Function to compile without tests
compilar_sin_pruebas() {
  echo
  echo "==================================================="
  echo "Compilando el proyecto sin pruebas..."
  echo "==================================================="
  
  ./mvnw clean install -DskipTests
  
  if [ $? -ne 0 ]; then
    echo "Error: La compilacion con Maven ha fallado."
    read -p "Presione Enter para continuar..."
    exit 1
  fi
  
  echo
  echo "Compilacion sin pruebas completada exitosamente."
  read -p "Presione Enter para continuar..."
}

# Function to deploy with Docker
desplegar_docker() {
  echo
  echo "==================================================="
  echo "Iniciando la API de Supermercado con Docker Compose"
  echo "==================================================="
  
  # Verificar si Docker está instalado
  if ! command -v docker &> /dev/null; then
    echo "Error: Docker no está instalado o no está en el PATH."
    echo "Por favor, instala Docker desde https://www.docker.com/get-started"
    read -p "Presione Enter para continuar..."
    exit 1
  fi
  
  # Verificar si Docker Compose está instalado
  if ! command -v docker-compose &> /dev/null; then
    echo "Error: Docker Compose no está instalado o no está en el PATH."
    echo "Docker Compose debería estar incluido con Docker Desktop o puede instalarse por separado."
    read -p "Presione Enter para continuar..."
    exit 1
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
  echo "Para detener la aplicación, ejecuta: docker-compose down"
  echo "==================================================="
  read -p "Presione Enter para continuar..."
}

# Function to run with Spring Boot
ejecutar_springboot() {
  echo
  echo "==================================================="
  echo "Ejecutando la aplicación con Spring Boot"
  echo "==================================================="
  
  # Verificar si Java está instalado
  if ! command -v java &> /dev/null; then
    # Intentar encontrar JAVA_HOME
    if [ -n "$JAVA_HOME" ]; then
      echo "JAVA_HOME encontrado en: $JAVA_HOME"
      if [ -f "$JAVA_HOME/bin/java" ]; then
        echo "Java encontrado en JAVA_HOME"
        export PATH="$JAVA_HOME/bin:$PATH"
      else
        echo "Error: No se encontró java en JAVA_HOME/bin"
        read -p "Presione Enter para continuar..."
        exit 1
      fi
    else
      echo "Error: Java no está instalado o no está en el PATH y JAVA_HOME no está configurado."
      echo "Por favor, instala Java o configura JAVA_HOME correctamente."
      read -p "Presione Enter para continuar..."
      exit 1
    fi
  fi
  
  # Mostrar la versión de Java
  echo "Usando Java:"
  java -version
  
  echo
  echo "Compilando el proyecto..."
  ./mvnw clean package -DskipTests
  
  if [ $? -ne 0 ]; then
    echo "Error: La compilación con Maven ha fallado."
    read -p "Presione Enter para continuar..."
    exit 1
  fi
  
  echo
  echo "Iniciando la aplicación Spring Boot..."
  cd rest-api
  ../mvnw spring-boot:run
  cd ..
  read -p "Presione Enter para continuar..."
}

# Function to exit
salir() {
  echo "Saliendo..."
  exit 0
}

# Main execution
show_menu