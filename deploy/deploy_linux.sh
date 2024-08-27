#!/bin/bash

# Wechselt in das Verzeichnis, in dem das Shell-Skript liegt
cd "$(dirname "$0")"

# Prüft, ob Docker läuft
if ! docker info > /dev/null 2>&1; then
    echo "Docker scheint nicht zu laufen. Bitte starte Docker oder stelle sicher, dass Docker installiert ist."
    echo "Beenden..."
    exit 1
fi

# Docker Image aus .tar-Datei laden
echo "Lade Docker-Image aus datenanalysetool.tar..."
docker load -i datenanalysetool.tar
if [ $? -ne 0 ]; then
    echo "Das Docker-Image konnte nicht geladen werden."
    echo "Beenden..."
    exit 1
fi

# Docker Compose Befehl ausführen
echo "Starte Docker Compose..."
docker-compose up -d
if [ $? -ne 0 ]; then
    echo "Docker Compose konnte nicht gestartet werden."
    echo "Beenden..."
    exit 1
fi

# URL und Port anzeigen
echo "Die Anwendung wird gestartet... Bitte warten."

# Überprüfen, welche Container laufen und ihre Ports anzeigen
docker ps --format "table {{.Names}}\t{{.Ports}}"

echo "Docker Compose wurde erfolgreich gestartet."
echo "Beenden..."
