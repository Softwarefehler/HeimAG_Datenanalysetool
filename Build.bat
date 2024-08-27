@echo off

:: Schritt 1: BuildFatJar Task ausführen
echo Running buildFatJar...
call gradlew buildFatJar
if %ERRORLEVEL% NEQ 0 (
    echo buildFatJar failed. Exiting...
    exit /b %ERRORLEVEL%
)

:: Schritt 2: Docker-Image bauen
echo Building Docker image...
docker build --no-cache -t datenanalysetool:dev-latest -f ./deploy/Dockerfile .
if %ERRORLEVEL% NEQ 0 (
    echo Docker build failed. Exiting...
    exit /b %ERRORLEVEL%
)

:: Schritt 3: Docker-Image speichern
echo Docker image saving...
docker save -o ./deploy/datenanalysetool.tar datenanalysetool:dev-latest
if %ERRORLEVEL% NEQ 0 (
    echo Docker save failed. Exiting...
    exit /b %ERRORLEVEL%
)
echo Docker image saved as datenanalysetool.tar

pause
