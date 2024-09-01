@echo off

:: Schritt 0: Überprüfen, ob Docker Engine läuft
echo Checking if Docker Engine is running...
docker info >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo Docker Engine is not running. Please start Docker and try again.
    exit /b %ERRORLEVEL%
)
echo Docker Engine is running.

:: Schritt 1: BuildFatJar Task ausführen
echo Running buildFatJar...
call gradlew buildFatJar
if %ERRORLEVEL% NEQ 0 (
    echo buildFatJar failed. Exiting...
    exit /b %ERRORLEVEL%
)

:: Schritt 4: Docker-Image bauen
echo Building Docker image...
docker build --no-cache -t datenanalysetool:dev-latest -f ./deploy/Dockerfile .
if %ERRORLEVEL% NEQ 0 (
    echo Docker build failed. Exiting...
    exit /b %ERRORLEVEL%
)

:: Schritt 5: Docker-Image speichern
echo Saving Docker image...
docker save -o ./deploy/datenanalysetool.tar datenanalysetool:dev-latest
if %ERRORLEVEL% NEQ 0 (
    echo Docker save failed. Exiting...
    exit /b %ERRORLEVEL%
)
echo Docker image saved as datenanalysetool.tar

:: Schritt 4: Den gesamten deploy-Ordner zippen
echo Zipping the deploy directory...
if exist deploy.zip (del deploy.zip)
powershell -Command "Compress-Archive -Path deploy\* -DestinationPath deploy.zip"
if %ERRORLEVEL% NEQ 0 (
    echo Failed to zip the deploy directory. Exiting...
    exit /b %ERRORLEVEL%
)
echo Deploy directory zipped as deploy.zip





