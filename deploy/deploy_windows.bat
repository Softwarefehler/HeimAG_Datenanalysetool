@echo off
REM Wechselt in das Verzeichnis, in dem das Batch-File liegt
cd /d "%~dp0"

REM Prüft, ob Docker läuft
docker info >nul 2>&1
if %errorlevel% neq 0 (
    echo Docker scheint nicht zu laufen. Bitte starte Docker Desktop oder stelle sicher, dass Docker installiert ist.
    pause
    exit /b
)

REM Docker Image aus .tar-Datei laden
echo Lade Docker-Image aus datenanalysetool.tar...
docker load -i datenanalysetool.tar
if %errorlevel% neq 0 (
    echo Das Docker-Image konnte nicht geladen werden.
    pause
    exit /b
)

REM Docker Compose Befehl ausführen
echo Starte Docker Compose...
docker-compose up -d

REM Überprüft, ob Docker Compose erfolgreich ausgeführt wurde
if %errorlevel% neq 0 (
    echo Docker Compose konnte nicht gestartet werden.
    pause
    exit /b
)

REM URL und Port anzeigen
echo Die Anwendung wird gestartet... Bitte warten.

REM Überprüfen, welche Container laufen und ihre Ports anzeigen
docker ps --format "table {{.Names}}\t{{.Ports}}"

REM Versuche die HTTPS-URL zu bestimmen
for /f "tokens=2 delims=:" %%a in ('docker-compose port datenanalysetool 8443') do set HTTPS_URL=https://localhost:%%a

REM Falls HTTPS verfügbar, öffne diese URL
if defined HTTPS_URL (
    echo Die Webseite laeuft unter: %HTTPS_URL%
    echo Warte 5 Sekunden, bevor die URL geoeffnet wird...
    timeout /t 5 /nobreak >nul
    start "" "%HTTPS_URL%"
) else (
    REM Falls HTTPS nicht verfügbar, versuche die HTTP-URL
    for /f "tokens=2 delims=:" %%a in ('docker-compose port datenanalysetool 8080') do set HTTP_URL=http://localhost:%%a
    if defined HTTP_URL (
        echo Die Webseite laeuft unter: %HTTP_URL%
        echo Warte 5 Sekunden, bevor die URL geoeffnet wird...
        timeout /t 5 /nobreak >nul
        start "" "%HTTP_URL%"
    ) else (
        echo Konnte keine URL bestimmen. Bitte pruefe die docker-compose.yml Datei oder den laufenden Container.
    )
)

echo Docker Compose wurde erfolgreich gestartet.
pause
