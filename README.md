# Schach 

Implementierung eines erweiterbares Schachspiels, das sowohl in einer Konsolenversion als auch in einer 2D-GUI-Version zur Verfügung steht. Es bietet die Möglichkeit, gegen einen menschlichen Gegner oder gegen eine künstliche Intelligenz (AI) zu spielen. Das Spiel ist in Java implementiert und verwendet das MVC Design-Pattern für die GUI, wodurch eine klare Trennung zwischen den Daten und der Darstellung gewährleistet ist.

## Features

- **Konsolenspiel:** Spiele Schach in einer klaren und einfach zu bedienenden Konsolenansicht.
- **2D-GUI:** Genieße das Spiel mit einer visuell ansprechenden grafischen Benutzeroberfläche.
- **Künstliche Intelligenz:** Fordere die AI heraus, mit verschiedenen Schwierigkeitsgraden.
- **Netzwerkspiel:** Spiele gegen andere über eine Netzwerkverbindung.
- **Erweiterbare Architektur:** Dank klar definierter Komponenten und Schnittstellen lässt sich das Spiel leicht erweitern und warten.
- **Undo/Redo Funktionen:** Mache Züge rückgängig oder stelle sie wieder her.
- **Spielhistorie und geschlagene Figuren:** Behalte den Überblick über den Spielverlauf und geschlagene Figuren.
- **Anpassbare Einstellungen:** Passe das Spiel nach deinen Vorlieben an (z.B. Board-Drehung, Touch-Move-Regel, Schachwarnungen).

## Installation und Start

**Builden:**
- Das Projekt wird mit Maven kompiliert. Verwende den Befehl: mvn compile javafx:jlink.

**Spiel in der Konsole starten:**
- Unter Unix-Systemen: ./target/schach/bin/schach --no-gui.
- Unter Windows: Starte die schach.bat.

**Spiel in der 2D-GUI starten:**
- Starte das Spiel im 2D-GUI Modus. Die GUI hat eine fixe Auflösung von 700x400 Pixeln.


## Spielanleitung

**Spielauswahl:**
- Wähle zu Spielbeginn zwischen Mensch vs. AI, Mensch vs. Mensch (lokal) oder Netzwerkspiel.

**Ziehen:**
- Konsolenspiel: Gib Züge im Format SpalteA+ZeileA-SpalteB+ZeileB(+Umwandlung) ein.
- 2D-GUI: Wähle Figuren und Ziele durch Klicken.

**Spielverlauf:**
- Verfolge die Historie der Züge und betrachte geschlagene Figuren.

**Spielende:**
- Das Spiel endet bei Schachmatt, Remis oder wenn eine der beiden Spieler aufgibt.


## Einstellungen und Hinweise

Das Spiel bietet Einstellungen wie die Touch-Move-Regel, Schachwarnungen und die Möglichkeit, das Board nach jedem Zug zu drehen.
Beachte die Spielregeln und nutze die Undo/Redo-Funktionen, um den Spielverlauf nach Bedarf zu ändern.

Hinweis: Die detaillierte Beschreibung der Architektur und der einzelnen Komponenten des Spiels (Board, AI, Konsolen-Interface, 2D-GUI) findest du in der technischen Dokumentation des Projekts.
