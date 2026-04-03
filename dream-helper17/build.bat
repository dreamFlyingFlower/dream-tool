@echo off
REM Build script for dream-helper17 (JDK17)

echo ========================================
echo Building dream-helper17 with JDK 17
echo ========================================
echo.

REM Check Java version
echo Checking Java version...
java -version
echo.

REM Check Maven version
echo Checking Maven version...
mvn -version
echo.

REM Clean and compile
echo Cleaning and compiling project...
mvn clean compile

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Build completed successfully!
    echo ========================================
) else (
    echo.
    echo ========================================
    echo Build failed! Please check the errors above.
    echo ========================================
)

pause
