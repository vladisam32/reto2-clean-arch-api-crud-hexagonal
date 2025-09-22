# Script para generar un PDF a partir del archivo Markdown
# Requiere la instalación de pandoc y wkhtmltopdf

# Comprobar si pandoc está instalado
try {
    $pandocVersion = pandoc --version
    Write-Host "Pandoc encontrado: $($pandocVersion[0])"
} catch {
    Write-Host "Error: Pandoc no está instalado. Por favor, instálelo desde https://pandoc.org/installing.html"
    exit 1
}

# Comprobar si wkhtmltopdf está instalado
try {
    $wkhtmltopdfVersion = wkhtmltopdf --version
    Write-Host "wkhtmltopdf encontrado: $wkhtmltopdfVersion"
} catch {
    Write-Host "Error: wkhtmltopdf no está instalado. Por favor, instálelo desde https://wkhtmltopdf.org/downloads.html"
    exit 1
}

# Rutas de archivos
$markdownFile = "explicacion_proyecto_supermercado_hexagonal.md"
$pdfFile = "explicacion_proyecto_supermercado_hexagonal.pdf"

# Generar el PDF
Write-Host "Generando PDF a partir de $markdownFile..."
pandoc $markdownFile -o $pdfFile --pdf-engine=wkhtmltopdf --variable margin-top=20 --variable margin-right=20 --variable margin-bottom=20 --variable margin-left=20

# Verificar si se generó el PDF
if (Test-Path $pdfFile) {
    Write-Host "PDF generado exitosamente: $pdfFile"
    # Abrir el PDF
    Invoke-Item $pdfFile
} else {
    Write-Host "Error: No se pudo generar el PDF."
}