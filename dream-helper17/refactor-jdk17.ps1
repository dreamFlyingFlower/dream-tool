# PowerShell script to refactor Java files for JDK17 features
$projectRoot = "d:\person\repository\dream-tool\dream-helper17\src\main\java\dream\flying\flower"

# Get all Java files
$javaFiles = Get-ChildItem -Path $projectRoot -Filter *.java -Recurse

foreach ($file in $javaFiles) {
    $content = Get-Content $file.FullName -Raw -Encoding UTF8
    $modified = $false
    
    # Replace var keyword (JDK10+) for explicit types where appropriate
    # This is a simple example - real refactoring would be more sophisticated
    
    # Replace StringBuilder with more efficient patterns
    # Example: Optimize string concatenation in loops
    
    # Add @SuppressWarnings("varargs") where needed
    
    # Update deprecated APIs
    # Example: Replace deprecated Date/Calendar with java.time API
    
    # Pattern 1: Replace Vector with ArrayList (modern collections)
    if ($content -match 'import java\.util\.Vector') {
        $content = $content -replace 'import java\.util\.Vector', '// JDK17: Consider using ArrayList instead of Vector'
        $modified = $true
    }
    
    # Pattern 2: Replace Hashtable with ConcurrentHashMap
    if ($content -match 'import java\.util\.Hashtable') {
        $content = $content -replace 'import java\.util\.Hashtable', 'import java.util.concurrent.ConcurrentHashMap'
        $content = $content -replace 'Hashtable<', 'ConcurrentHashMap<'
        $modified = $true
    }
    
    # Pattern 3: Replace System.currentTimeMillis() with Instant.now().toEpochMilli()
    if ($content -match 'System\.currentTimeMillis\(\)') {
        $content = $content -replace 'System\.currentTimeMillis\(\)', 'Instant.now().toEpochMilli()'
        if ($content -notmatch 'import java\.time\.Instant') {
            $content = $content -replace '(package .*;)', '$1`n`nimport java.time.Instant;'
        }
        $modified = $true
    }
    
    # Pattern 4: Use Objects.isNull() instead of == null
    if ($content -match 'if\s*\(\s*(\w+)\s*==\s*null\s*\)') {
        $content = $content -replace 'if\s*\(\s*(\w+)\s*==\s*null\s*\)', 'if (Objects.isNull($1))'
        if ($content -notmatch 'import java\.util\.Objects') {
            $content = $content -replace '(package .*;)', '$1`n`nimport java.util.Objects;'
        }
        $modified = $true
    }
    
    # Pattern 5: Use Objects.nonNull() instead of != null
    if ($content -match 'if\s*\(\s*(\w+)\s*!=\s*null\s*\)') {
        $content = $content -replace 'if\s*\(\s*(\w+)\s*!=\s*null\s*\)', 'if (Objects.nonNull($1))'
        if ($content -notmatch 'import java\.util\.Objects') {
            $content = $content -replace '(package .*;)', '$1`n`nimport java.util.Objects;'
        }
        $modified = $true
    }
    
    # Pattern 6: Replace new Integer() with Integer.valueOf() for caching
    if ($content -match 'new Integer\(([^)]+)\)') {
        $content = $content -replace 'new Integer\(([^)]+)\)', 'Integer.valueOf($1)'
        $modified = $true
    }
    
    if ($content -match 'new Long\(([^)]+)\)') {
        $content = $content -replace 'new Long\(([^)]+)\)', 'Long.valueOf($1)'
        $modified = $true
    }
    
    if ($content -match 'new Double\(([^)]+)\)') {
        $content = $content -replace 'new Double\(([^)]+)\)', 'Double.valueOf($1)'
        $modified = $true
    }
    
    # Save if modified
    if ($modified) {
        Set-Content -Path $file.FullName -Value $content -Encoding UTF8 -NoNewline
        Write-Host "Refactored: $($file.Name)"
    }
}

Write-Host "JDK17 refactoring completed!"
