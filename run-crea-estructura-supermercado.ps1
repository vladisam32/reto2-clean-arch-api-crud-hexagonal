# Ruta raíz del proyecto
$root = Get-Location

# Lista de módulos Maven
$modulos = @("domain", "application", "infrastructure", "cli", "rest-api", "shared-dto")

# Crear estructura de paquetes en cada módulo
foreach ($modulo in $modulos) {
    $rutaModulo = Join-Path $root $modulo
    $rutaDestino = Join-Path $rutaModulo "src/main/java/org/litethinking/supermercado"

    if (-not (Test-Path $rutaDestino)) {
        New-Item -ItemType Directory -Path $rutaDestino -Force
        Write-Host "Ruta creada: $rutaDestino"
    } else {
        Write-Host "Ya existe: $rutaDestino"
    }
}
