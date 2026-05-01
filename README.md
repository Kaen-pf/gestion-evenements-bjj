# GES — Application de gestion des événements sportifs BJJ

Application de bureau développée en JavaFX permettant de gérer les événements sportifs de Brazilian Jiu-Jitsu, les compétiteurs, les inscriptions et les résultats des combats, connectée à une base de données MariaDB.

---

## Sommaire

- [Présentation](#présentation)
- [Fonctionnalités](#fonctionnalités)
- [Prérequis](#prérequis)
- [Installation et lancement](#installation-et-lancement)
- [Accès à l'application](#accès-à-lapplication)
- [Base de données](#base-de-données)
- [Architecture](#architecture)
- [Arborescence](#arborescence)
- [Technologies utilisées](#technologies-utilisées)

---

## Présentation

GES (Gestion des Événements Sportifs) est une application de bureau développée en Java 21 avec JavaFX. Elle permet de gérer les compétitions de Brazilian Jiu-Jitsu avec un système d'authentification par rôles, un tableau de bord statistique, et une interface professionnelle en navigation intégrée.

---

## Fonctionnalités

### Authentification

- Connexion par identifiant et mot de passe
- Deux rôles : Administrateur et Organisateur
- Déconnexion depuis l'interface principale

### Tableau de bord

- Statistiques en temps réel (compétiteurs, événements, inscriptions, sports)
- Liste des 5 prochains événements à venir
- Classement des 5 meilleurs compétiteurs par victoires

### Gestion des sports

- Ajout, modification et suppression de disciplines sportives
- Confirmation avant suppression

### Gestion des compétiteurs

- Champs : nom, prénom, poids, ceinture, club
- Ajout, modification et suppression avec confirmation

### Gestion des événements

- Champs : nom, date (DatePicker), lieu, sport associé, nombre de participants max
- Suppression en cascade des inscriptions et combats liés

### Gestion des inscriptions

- Inscription d'un compétiteur à un événement avec catégorie
- Vérification des doublons et de la limite de participants
- Désinscription avec confirmation

### Résultats et classements

- Enregistrement des résultats de combats par événement
- Affichage du classement par victoires

### Gestion des utilisateurs *(Admin uniquement)*

- Création, modification et suppression de comptes utilisateurs
- Attribution des rôles (Admin / Organisateur)

---

## Prérequis

| Outil | Version |
|-------|---------|
| Java (JDK) | 21 LTS |
| Apache Maven | 3.9+ |
| MariaDB / MySQL | 8.0+ |
| Docker | Latest (optionnel) |

---

## Installation et lancement

### 1. Cloner le dépôt

```bash
git clone https://github.com/Kaen-pf/gestion-evenements-bjj.git
cd gestion-evenements-bjj
```

### 2. Créer la base de données

Importez le script SQL fourni dans le dossier `database/` :

```bash
mysql -u votre_utilisateur -p < database/gestion_bjj.sql
```

Ou importez-le directement via **phpMyAdmin**.

### 3. Configurer la connexion

Modifiez le fichier `src/main/java/com/bjj/database/DatabaseConnection.java` :

```java
private static final String URL = "jdbc:mariadb://localhost:3306/gestion_bjj";
private static final String USER = "votre_utilisateur";
private static final String PASSWORD = "votre_mot_de_passe";
```

### 4. Lancer l'application

```bash
mvn javafx:run
```

---

## Accès à l'application

| Identifiant | Mot de passe | Rôle |
|-------------|-------------|------|
| admin | admin123 | Administrateur |

---

## Base de données

### Script SQL

Le script de création de la base de données est disponible dans le dossier `database/bdd.sql`. Il contient :

- La création de toutes les tables
- Les contraintes de clés étrangères
- Des données de test (compétiteurs, sports, événements)

### Tables

| Table | Rôle |
|-------|------|
| `utilisateur` | Comptes utilisateurs avec rôles |
| `sport` | Disciplines sportives |
| `club` | Clubs sportifs |
| `competiteur` | Profils des compétiteurs |
| `evenement` | Événements sportifs |
| `inscription` | Inscriptions des compétiteurs aux événements |
| `combat` | Résultats des combats |
| `classement` | Classements par événement |

---

## Architecture

L'application suit le patron de conception **MVC (Modèle - Vue - Contrôleur)** :

| Couche | Rôle | Fichiers |
|--------|------|----------|
| **Modèle** | Classes métier (POJO) | `model/Sport.java`, `model/Competiteur.java`, etc. |
| **Vue** | Interfaces graphiques FXML | `*.fxml`, `style.css` |
| **Contrôleur** | Logique métier et accès BDD | `controller/*.java` |
| **BDD** | Connexion MariaDB via JDBC | `database/DatabaseConnection.java` |

---

## Arborescence

```
gestion-bjj/
├── database/
│   └── gestion_bjj.sql                     # Script de création de la BDD
├── src/
│   └── main/
│       ├── java/com/bjj/
│       │   ├── App.java
│       │   ├── controller/
│       │   │   ├── MainController.java
│       │   │   ├── DashboardController.java
│       │   │   ├── LoginController.java
│       │   │   ├── SportController.java
│       │   │   ├── EvenementController.java
│       │   │   ├── ParticipantController.java
│       │   │   ├── InscriptionController.java
│       │   │   ├── ResultatController.java
│       │   │   └── UtilisateurController.java
│       │   ├── model/
│       │   │   ├── Sport.java
│       │   │   ├── Club.java
│       │   │   ├── Competiteur.java
│       │   │   └── Evenement.java
│       │   └── database/
│       │       └── DatabaseConnection.java
│       └── resources/com/bjj/
│           ├── main.fxml
│           ├── dashboard.fxml
│           ├── login.fxml
│           ├── sport.fxml
│           ├── evenement.fxml
│           ├── participant.fxml
│           ├── inscription.fxml
│           ├── resultat.fxml
│           ├── utilisateur.fxml
│           └── style.css
└── pom.xml
```

---

## Technologies utilisées

- **Java 21** — langage de programmation principal
- **JavaFX 21** — framework pour l'interface graphique de bureau
- **Apache Maven 3.9** — gestion des dépendances et build
- **FXML** — description déclarative des interfaces JavaFX
- **CSS JavaFX** — mise en forme des interfaces
- **MariaDB / MySQL 8.0** — base de données relationnelle
- **JDBC (MariaDB Connector)** — connexion Java vers MariaDB
- **Docker** — hébergement local de la base de données
- **Git / GitHub** — gestion de version du code source

---

*Projet réalisé dans le cadre du BTS SIO option SLAM — Polynésie française - 2026*