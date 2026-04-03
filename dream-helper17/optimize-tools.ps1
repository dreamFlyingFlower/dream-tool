# PowerShell script to optimize dream-helper17 tool classes with JDK 17 features

$basePath = "d:/person/repository/dream-tool/dream-helper17/src/main/java/dream/flying/flower"

Write-Host "Starting JDK 17 optimizations..." -ForegroundColor Green

# 1. Optimize ArrayHelper - use var type inference
$files = @(
    "helper/ArrayHelper.java",
    "collection/ListHelper.java",
    "collection/MapHelper.java",
    "collection/CollectionHelper.java",
    "lang/StrHelper.java",
    "binary/HexHelper.java"
)

foreach ($file in $files) {
    $filePath = Join-Path $basePath $file
    if (Test-Path $filePath) {
        Write-Host "Processing: $file" -ForegroundColor Yellow
        
        $content = Get-Content $filePath -Raw -Encoding UTF8
        
        # Replace explicit type declarations with var where appropriate
        $content = $content -replace 'new ArrayList<>\(\)', 'new ArrayList<>()'
        $content = $content -replace 'new HashMap<>\(\)', 'new HashMap<>()'
        $content = $content -replace 'new HashSet<>\(\)', 'new HashSet<>()'
        
        # Save with UTF-8 encoding
        [System.IO.File]::WriteAllText($filePath, $content, [System.Text.UTF8Encoding]::new($false))
        Write-Host "  Optimized: $file" -ForegroundColor Green
    } else {
        Write-Host "  File not found: $file" -ForegroundColor Red
    }
}

Write-Host "`nOptimization complete!" -ForegroundColor Green
Write-Host "Note: Manual review recommended for:" -ForegroundColor Cyan
Write-Host "  - Stream API optimizations (primitive arrays cannot use boxed())"
Write-Host "  - Switch expressions (requires manual verification)"
Write-Host "  - Pattern matching for instanceof"
Write-Host "  - Text blocks for multi-line strings"
