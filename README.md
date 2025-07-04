# Reservationssystem - Spring Boot 3 mit Docker

<p align="center">
    <img src="https://img.shields.io/badge/license-MIT-yellow" alt="License">
    <img src="https://img.shields.io/badge/language-Java-red" alt="Java">
    <img src="https://img.shields.io/badge/framework-Spring Boot-orange" alt="Spring Boot">
    <img src="https://img.shields.io/badge/framework-Docker-blue" alt="Docker">
</p>

### Ein containerisiertes Reservationssystem basierend auf Spring Boot 3 und MariaDB.

> [!IMPORTANT]
> Da dieses Projekt lokal mit Spring Boot und Docker entwickelt wurde, funktioniert es nicht direkt auf GitHub.
>
> Wir empfehlen, dieses Projekt lade die Programm in Voraussetzungen herunter um dieses Projekt auszuführen.


##  Features

- **Spring Boot 3** mit Java 21
- **MariaDB** Datenbank
- **Docker Compose** für einfache Deployment
- **REST API** für Terminbuchungen
- **Automatische Schema-Erstellung**
- **Health Checks** für Monitoring

## Voraussetzungen

- Docker Desktop
- Java (IntelliJ) 
- Maven
- Spring Boot

##  Installation & Start

### 1. Repository klonen
```bash
git clone https://github.com/yourusername/reservationssystem.git
cd reservationssystem
```

### 2. Anwendung starten
```bash
# Alle Services bauen und starten
docker-compose up --build

# Im Hintergrund starten
docker-compose up -d --build
```

### 3. Zugriff
- **Anwendung**: http://localhost:8080
- **Datenbank**: localhost:3307
  - Username: `root`
  - Password: `password`
  - Database: `reservationen`

##  Architektur

```
┌─────────────────┐    ┌─────────────────┐
│   App Service   │    │   DB Service    │
│  (Spring Boot)  │◄──►│   (MariaDB)     │
│   Port: 8080    │    │   Port: 3306    │
└─────────────────┘    └─────────────────┘
```

## Projektstruktur

```
├── src/                    # Spring Boot Anwendung
│   ├── main/java/         # Java Source Code
│   └── main/resources/    # Konfigurationsdateien
├── Dockerfile             # Container Definition
├── docker-compose.yml     # Multi-Container Setup
├── pom.xml               # Maven Dependencies
└── README.md             # Diese Dokumentation
```

##  Datenbank-Zugriff

### Direkte Verbindung
```bash
# In MariaDB Container einloggen
docker exec -it reservationssystem-db-1 mysql -u root -p reservationen
```

## Troubleshooting

### Häufige Probleme

**Port bereits belegt**
```bash
# Prüfen welcher Prozess Port 8080 verwendet
netstat -ano | findstr :8080
```

**Datenbank-Verbindung fehlgeschlagen**
```bash
# DB-Logs prüfen
docker-compose logs db
```

**Build-Fehler**
```bash
# Cache leeren und neu bauen
docker-compose build --no-cache
```

## Konfiguration

### Umgebungsvariablen
Die Anwendung kann über folgende Umgebungsvariablen konfiguriert werden:

- `SPRING_DATASOURCE_URL` - Datenbank-URL
- `SPRING_DATASOURCE_USERNAME` - DB-Benutzername
- `SPRING_DATASOURCE_PASSWORD` - DB-Passwort
- `SPRING_JPA_HIBERNATE_DDL_AUTO` - Schema-Management

### Docker Compose Anpassungen
Editiere `docker-compose.yml` für:
- Port-Änderungen
- Umgebungsvariablen
- Volume-Konfiguration

### Dieses Projekt ist unter der MIT Lizenz lizenziert.
