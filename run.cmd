@echo off
echo ===================================================
echo API de Supermercado - Opciones
echo ===================================================
echo.
echo 1. Compilar con pruebas (mvn clean install)
echo 2. Compilar sin pruebas (mvn clean install -DskipTests)
echo 3. Desplegar con Docker
echo 4. Ejecutar con Spring Boot
echo 5. Salir
echo.

set /p opcion=Seleccione una opcion (1-5): 

if "%opcion%"=="1" goto compilar_con_pruebas
if "%opcion%"=="2" goto compilar_sin_pruebas
if "%opcion%"=="3" goto desplegar_docker
if "%opcion%"=="4" goto ejecutar_springboot
if "%opcion%"=="5" goto salir

echo Opcion invalida. Por favor, seleccione una opcion valida.
goto :eof

:compilar_con_pruebas
echo.
echo ===================================================
echo Compilando el proyecto con pruebas...
echo ===================================================
call mvnw clean install
if %errorlevel% neq 0 (
    echo Error: La compilacion con Maven ha fallado.
    pause
    exit /b 1
)
echo.
echo Compilacion con pruebas completada exitosamente.
pause
goto :eof

:compilar_sin_pruebas
echo.
echo ===================================================
echo Compilando el proyecto sin pruebas...
echo ===================================================
call mvnw clean install -DskipTests
if %errorlevel% neq 0 (
    echo Error: La compilacion con Maven ha fallado.
    pause
    exit /b 1
)
echo.
echo Compilacion sin pruebas completada exitosamente.
pause
goto :eof

:desplegar_docker
echo.
echo ===================================================
echo Iniciando la API de Supermercado con Docker Compose
echo ===================================================

REM Verificar si Docker está instalado
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Docker no está instalado o no está en el PATH.
    echo Por favor, instala Docker Desktop desde https://www.docker.com/products/docker-desktop
    pause
    exit /b 1
)

REM Verificar si Docker Compose está instalado
docker-compose --version >nul 2>&1
if %errorlevel% neq 0 (
    echo Err: Dock Compose no está instalado o no está en el PATH.
    echo Docke compose debería estar incluido con Docker Desktop.
    pause
    exit /b 1
)

REM Compilar el proyecto con Maven
echo.
echo Compilando el proyecto con Maven...
call mvnw clean package -DskipTests
if %errorlevel% neq 0 (
    echo Error: La compilación con Maven ha fallado.
    pause
    exit /b 1
)

REM Crear directorio para datos persistentes si no existe
if not exist data mkdir data

REM Iniciar los contenedores con Docker Compose
echo.
echo Iniciando los contenedores con Docker Compose...
docker-compose up -d --build

REM Verificar si los contenedores están en ejecución
echo.
echo Verificando el estado de los contenedores...
docker-compose ps

echo.
echo ===================================================
echo La API de Supermercado está en ejecución!
echo.
echo Accede a la API en: http://localhost:8080/api
echo Accede a la consola H2 en: http://localhost:8080/h2-console
echo.
echo Para detener la aplicación, ejecuta: docker-compose down
echo ===================================================
pause
goto :eof

:ejecutar_springboot
echo.
echo ===================================================
echo Ejecutando la aplicación con Spring Boot
echo ===================================================
echo.
echo Compilando el proyecto...
call mvnw clean package -DskipTests
if %errorlevel% neq 0 (
    echo Error: La compilación con Maven ha fallado.
    pause
    exit /b 1
)

echo.
echo Iniciando la aplicación Spring Boot...
cd rest-api
call ..\mvnw spring-boot:run
cd ..
pause
goto :eof

:salir
echo Saliendo...
exit /b 0
