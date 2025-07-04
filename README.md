# M233 Reservation System


<p align="center">
    <img src="https://img.shields.io/badge/license-MIT-green" alt="License">
    <img src="https://img.shields.io/badge/language-Java-blue" alt="Java">
    <img src="https://img.shields.io/badge/framework-Spring Boot-orange" alt="Spring Boot">
</p>

> [!IMPORTANT]
> Da dieses Projekt lokal mit Spring Boot entwickelt wurde, funktioniert es nicht direkt auf GitHub.
>
> Wir empfehlen, dieses Projekt herunterzuladen und mit Java sowie Maven auszuführen.

Dies ist eine Spring-Boot-Webanwendung, die dir hilft, Raumreservationen in einem Schul- oder Unternehmenskontext zu verwalten. Sie bietet eine umfassende Reihe von Funktionen, um Reservationen zu erstellen, zu bearbeiten und einzusehen, ohne dass ein Benutzerkonto erforderlich ist.

---

## Funktionen

* **Reservationerstellung**: Füge neue Raumreservationen mit Datum, Uhrzeit, Raum und Teilnehmern hinzu.
* **Verfügbarkeitsprüfung**: Überprüfe die Verfügbarkeit eines Raums vor der Reservation.
* **Schlüsselgenerierung**: Erstelle öffentliche und private Schlüssel für den Zugriff und die Bearbeitung von Reservationen.
* **Reservationseinsicht**: Zeige bestehende Reservationen detailliert an.
* **Bearbeitungsoptionen**: Bearbeite Reservationen mit dem privaten Schlüssel.
* **Übersicht aller Reservationen**: Liste alle Reservationen in einer übersichtlichen Ansicht.
* **Fehlerbehandlung**: Zeige Fehlermeldungen bei ungültigen Eingaben oder Schlüsseln.

---

## So führst du das Projekt aus

Um dieses Projekt auf deinem lokalen Computer zum Laufen zu bringen, befolge diese einfachen Schritte:

### Voraussetzungen

Stelle sicher, dass du die folgenden Tools installiert hast:
- **Java Development Kit (JDK):** JDK Version 21.
- **Maven:** Version 3.6.0 oder höher für das Build-Management.
- **MySQL:** Server-Version 8.0 oder höher für die Datenbank.

### Installation

1. **Repository klonen**:
   Wenn du die Projektdateien als ZIP hast, entpacke sie in dein gewünschtes Verzeichnis. Andernfalls, wenn du ein Git-Repository hast, klone es mit:
   ```bash
   git clone <repository-url>
   ```
   Navigiere dann in das Projektverzeichnis:
   ```bash
   cd <projekt-ordnername>
   ```

2. **Datenbank einrichten**:
   - Starte deinen MySQL-Server.
   - Erstelle eine Datenbank namens `terminkalender`:
     ```sql
     CREATE DATABASE terminkalender;
     ```
   - Passe die Konfigurationsdatei an (siehe unten).

3. **Konfigurationsdatei anpassen**:
   - Öffne die Datei `src/main/resources/application.properties` oder `application.yml`.
   - Passe die Datenbankverbindung an deine lokale Umgebung an, z.B.:
     ```
     spring.datasource.url=jdbc:mysql://localhost:3307/terminkalender
     spring.datasource.username=your_username
     spring.datasource.password=your_password
     spring.jpa.hibernate.ddl-auto=update
     ```
     Ersetze `your_username` und `your_password` mit deinen MySQL-Anmeldeinformationen.

4. **Abhängigkeiten installieren**:
   Führe den folgenden Maven-Befehl aus, um alle Abhängigkeiten zu laden:
   ```bash
   mvn clean install
   ```

### Anwendung starten

Nach der Installation der Abhängigkeiten kannst du den Entwicklungsserver starten:

```bash
mvn spring-boot:run
```

Dieser Befehl wird:
- Den Spring-Boot-Server starten.
- Die Anwendung unter `http://localhost:8080` verfügbar machen.

Öffne einen Webbrowser und navigiere zu `http://localhost:8080`, um die Startseite (`index.html`) zu laden. Die Anwendung lädt automatisch Testdaten und aktualisiert sich bei Code-Änderungen nach einem Neustart.

---

## Projektstruktur

Das Projekt folgt einer standardmäßigen Spring-Boot-Anwendungsstruktur. Wichtige Verzeichnisse und Dateien sind:

- `src/main/java/com/example/terminkalender/`:
  - `TerminkalenderApplication.java`: Die Hauptanwendungsklasse.
  - `controller/`: Enthält Controller-Klassen wie `HomeController` und `ReservationController`.
  - `model/`: Enthält Entity-Klassen wie `Reservation` und `Participant`.
  - `service/`: Enthält Service-Klassen wie `ReservationService`.
  - `repository/`: Enthält Repository-Interfaces wie `ReservationRepository`.
  - `config/`: Enthält Konfigurationsklassen wie `DataInitializer`.
- `src/main/resources/`:
  - `application.properties`: Konfigurationsdatei für Datenbank und Server.
  - `templates/`: Enthält Thymeleaf-HTML-Seiten wie `index.html` und `create-reservation.html`.
- `pom.xml`: Maven-Build-Datei mit Projektabhängigkeiten.

---

## Lizenz

Dieses Projekt ist unter der MIT-Lizenz lizenziert. Weitere Details findest du in der Datei `LICENSE`.
