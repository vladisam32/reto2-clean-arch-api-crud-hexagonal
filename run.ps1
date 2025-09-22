# PowerShell script for running the Supermarket API application
# This script provides options for compiling, running with Docker, or running directly with Java

Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "API de Supermercado - Menu de Opciones" -ForegroundColor Cyan
Write-Host "===================================================" -ForegroundColor Cyan
Write-Host
Write-Host "1. Compilar con pruebas (mvn clean install)"
Write-Host "2. Compilar sin pruebas (mvn clean install -DskipTests)"
Write-Host "3. Desplegar con Docker"
Write-Host "4. Ejecutar con Spring Boot"
Write-Host "5. Salir"
Write-Host

$opcion = Read-Host "Seleccione una opcion (1-5)"

switch ($opcion) {
    "1" { 
        # Compilar con pruebas
        Write-Host
        Write-Host "===================================================" -ForegroundColor Cyan
        Write-Host "Compilando el proyecto con pruebas..." -ForegroundColor Cyan
        Write-Host "===================================================" -ForegroundColor Cyan
        
        & .\mvnw clean install
        
        if ($LASTEXITCODE -ne 0) {
            Write-Host "Error: La compilacion con Maven ha fallado." -ForegroundColor Red
            Read-Host "Presione Enter para continuar..."
            exit 1
        }
        
        Write-Host
        Write-Host "Compilacion con pruebas completada exitosamente." -ForegroundColor Green
        Read-Host "Presione Enter para continuar..."
    }
    
    "2" { 
        # Compilar sin pruebas
        Write-Host
        Write-Host "===================================================" -ForegroundColor Cyan
        Write-Host "Compilando el proyecto sin pruebas..." -ForegroundColor Cyan
        Write-Host "===================================================" -ForegroundColor Cyan
        
        & .\mvnw clean install -DskipTests
        
        if ($LASTEXITCODE -ne 0) {
            Write-Host "Error: La compilacion con Maven ha fallado." -ForegroundColor Red
            Read-Host "Presione Enter para continuar..."
            exit 1
        }
        
        Write-Host
        Write-Host "Compilacion sin pruebas completada exitosamente." -ForegroundColor Green
        Read-Host "Presione Enter para continuar..."
    }
    
    "3" { 
        # Desplegar con Docker
        Write-Host
        Write-Host "===================================================" -ForegroundColor Cyan
        Write-Host "Iniciando la API de Supermercado con Docker Compose" -ForegroundColor Cyan
        Write-Host "===================================================" -ForegroundColor Cyan
        
        # Verificar si Docker está instalado
        try {
            docker --version | Out-Null
        } catch {
            Write-Host "Error: Docker no está instalado o no está en el PATH." -ForegroundColor Red
            Write-Host "Por favor, instala Docker Desktop desde https://www.docker.com/products/docker-desktop" -ForegroundColor Yellow
            Read-Host "Presione Enter para continuar..."
            exit 1
        }
        
        # Verificar si Docker Compose está instalado
        try {
            docker-compose --version | Out-Null
        } catch {
            Write-Host "Error: Docker Compose no está instalado o no está en el PATH." -ForegroundColor Red
            Write-Host "Docker Compose debería estar incluido con Docker Desktop." -ForegroundColor Yellow
            Read-Host "Presione Enter para continuar..."
            exit 1
        }
        
        # Compilar el proyecto con Maven
        Write-Host
        Write-Host "Compilando el proyecto con Maven..." -ForegroundColor Cyan
        & .\mvnw clean package -DskipTests
        
        if ($LASTEXITCODE -ne 0) {
            Write-Host "Error: La compilación con Maven ha fallado." -ForegroundColor Red
            Read-Host "Presione Enter para continuar..."
            exit 1
        }
        
        # Crear directorio para datos persistentes si no existe
        if (-not (Test-Path -Path "data")) {
            New-Item -ItemType Directory -Path "data" | Out-Null
        }
        
        # Iniciar los contenedores con Docker Compose
        Write-Host
        Write-Host "Iniciando los contenedores con Docker Compose..." -ForegroundColor Cyan
        docker-compose up -d --build
        
        # Verificar si los contenedores están en ejecución
        Write-Host
        Write-Host "Verificando el estado de los contenedores..." -ForegroundColor Cyan
        docker-compose ps
        
        Write-Host
        Write-Host "===================================================" -ForegroundColor Green
        Write-Host "La API de Supermercado está en ejecución!" -ForegroundColor Green
        Write-Host
        Write-Host "Accede a la API en: http://localhost:8080/api" -ForegroundColor Yellow
        Write-Host "Accede a la consola H2 en: http://localhost:8080/h2-console" -ForegroundColor Yellow
        Write-Host
        Write-Host "Para detener la aplicación, ejecuta: docker-compose down" -ForegroundColor Yellow
        Write-Host "===================================================" -ForegroundColor Green
        Read-Host "Presione Enter para continuar..."
    }
    
    "4" { 
        # Ejecutar con Spring Boot
        Write-Host
        Write-Host "===================================================" -ForegroundColor Cyan
        Write-Host "Ejecutando la aplicación con Spring Boot" -ForegroundColor Cyan
        Write-Host "===================================================" -ForegroundColor Cyan
        
        # Verificar si Java está instalado
        try {
            $javaVersion = & java -version 2>&1
            Write-Host "Usando Java:" -ForegroundColor Cyan
            Write-Host $javaVersion -ForegroundColor Yellow
        } catch {
            # Intentar encontrar JAVA_HOME
            if ($env:JAVA_HOME) {
                Write-Host "JAVA_HOME encontrado en: $env:JAVA_HOME" -ForegroundColor Cyan
                $javaPath = Join-Path -Path $env:JAVA_HOME -ChildPath "bin\java.exe"
                
                if (Test-Path $javaPath) {
                    Write-Host "Java encontrado en JAVA_HOME" -ForegroundColor Green
                    $env:Path = "$env:JAVA_HOME\bin;$env:Path"
                } else {
                    Write-Host "Error: No se encontró java.exe en JAVA_HOME\bin" -ForegroundColor Red
                    Read-Host "Presione Enter para continuar..."
                    exit 1
                }
            } else {
                Write-Host "Error: Java no está instalado o no está en el PATH y JAVA_HOME no está configurado." -ForegroundColor Red
                Write-Host "Por favor, instala Java o configura JAVA_HOME correctamente." -ForegroundColor Yellow
                Read-Host "Presione Enter para continuar..."
                exit 1
            }
        }
        
        Write-Host
        Write-Host "Compilando el proyecto..." -ForegroundColor Cyan
        & .\mvnw clean package -DskipTests
        
        if ($LASTEXITCODE -ne 0) {
            Write-Host "Error: La compilación con Maven ha fallado." -ForegroundColor Red
            Read-Host "Presione Enter para continuar..."
            exit 1
        }
        
        Write-Host
        Write-Host "Iniciando la aplicación Spring Boot..." -ForegroundColor Cyan
        Push-Location -Path "rest-api"
        & ..\mvnw spring-boot:run
        Pop-Location
        Read-Host "Presione Enter para continuar..."
    }
    
    "5" { 
        # Salir
        Write-Host "Saliendo..." -ForegroundColor Yellow
        exit 0
    }
    
    default {
        Write-Host "Opción inválida. Por favor, seleccione una opción válida (1-5)." -ForegroundColor Red
        Read-Host "Presione Enter para continuar..."
    }
}