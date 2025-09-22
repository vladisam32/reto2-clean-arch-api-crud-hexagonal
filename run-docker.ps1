# PowerShell script for running the Supermarket API application with Docker
# This script focuses specifically on Docker deployment

Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "API de Supermercado - Despliegue con Docker" -ForegroundColor Cyan
Write-Host "===================================================" -ForegroundColor Cyan
Write-Host

# Verificar si Docker está instalado
try {
    $dockerVersion = docker --version
    Write-Host "Docker detectado: $dockerVersion" -ForegroundColor Green
} catch {
    Write-Host "Error: Docker no está instalado o no está en el PATH." -ForegroundColor Red
    Write-Host "Por favor, instala Docker Desktop desde https://www.docker.com/products/docker-desktop" -ForegroundColor Yellow
    Read-Host "Presione Enter para continuar..."
    exit 1
}

# Verificar si Docker Compose está instalado
try {
    $composeVersion = docker-compose --version
    Write-Host "Docker Compose detectado: $composeVersion" -ForegroundColor Green
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
    Write-Host "Creando directorio de datos..." -ForegroundColor Cyan
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
Write-Host "Comandos útiles:" -ForegroundColor Cyan
Write-Host "- Para ver los logs: docker-compose logs -f" -ForegroundColor Yellow
Write-Host "- Para detener la aplicación: docker-compose down" -ForegroundColor Yellow
Write-Host "- Para reiniciar la aplicación: docker-compose restart" -ForegroundColor Yellow
Write-Host "===================================================" -ForegroundColor Green