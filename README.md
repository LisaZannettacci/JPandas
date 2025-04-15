# JPandas
[![Pandas](https://img.shields.io/badge/pandas-%23150458.svg?style=for-the-badge&logo=pandas&logoColor=white)](https://github.com/pandas-dev/pandas/blob/main/README.md)

![Coverage](https://img.shields.io/badge/Coverage-93%25-brightgreen)

JPandas est une bibliothèque Java inspirée de la bibliothèque Python [Pandas (librairie Python)](https://pandas.pydata.org/). Elle permet de manipuler, analyser et traiter des données de manière efficace en Java.

## Table des matières
- [JPandas](#jpandas)
- [Fonctionnalités principales](#-fonctionnalités-principales)
- [Installation](#-installation)
- [Tests et Couverture de Code](#-tests-et-couverture-de-code)
- [Intégration Continue (CI)](#-intégration-continue-ci)
- [Documentation](#-documentation)
- [Choix de conception](#-choix-de-conception)
- [Workflow CI/CD](#-workflow-cicd)
- [Docker](#-docker)
- [Feedback](#-feedback)
- [Remarques](#-remarques)
- [Auteurs](#-auteurs)

## Fonctionnalités principales
- Lecture de fichiers CSV en DataFrame
- Représentation des colonnes sous forme de Series
- Affichage de tout ou partie d’un DataFrame (début / fin / complet)
- Chargement dynamique depuis un fichier
- Analyse statistique (moyenne, min, max, écart-type)
- Intégration continue via GitHub Actions
- Tests unitaires (JUnit) et couverture de code (JaCoCo)
- Génération de documentation via Javadoc

## Installation
### Prérequis
![Visual Studio Code](https://img.shields.io/badge/Visual%20Studio%20Code-0078d7.svg?style=for-the-badge&logo=visual-studio-code&logoColor=white)
![Eclipse](https://img.shields.io/badge/Eclipse-FE7A16.svg?style=for-the-badge&logo=Eclipse&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
- Java 17 ou supérieur
- Maven installé

### Cloner le projet
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)

```sh
git clone https://github.com/ton-utilisateur/JPandas.git
cd JPandas
```

### Construire le projet avec Maven
![Maven]((https://img.shields.io/badge/apachemaven-C71A36.svg?style=for-the-badge&logo=apachemaven&logoColor=white))

```sh
mvn clean package
```

## Tests et Couverture de Code
JPandas utilise **JUnit** pour les tests unitaires et **JaCoCo** pour la couverture de code.

Exécuter les tests :
```sh
mvn verify
```
Générer le rapport de couverture de code :
```sh
mvn jacoco:report
```
Les rapports seront disponibles dans `target/site/jacoco`.

Des tests sont effectués sur Ubuntu via gitHub.

![Ubuntu](https://img.shields.io/badge/Ubuntu-E95420?style=for-the-badge&logo=ubuntu&logoColor=white)
![Windows](https://img.shields.io/badge/Windows-0078D6?style=for-the-badge&logo=windows&logoColor=white)


## Intégration Continue (CI) 
![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)

Une pipeline GitHub Actions est configurée pour :
- Compiler et tester automatiquement le projet à chaque push/pull request.
- Générer et déployer la documentation Javadoc sur GitHub Pages.

## Documentation
![Github Pages](https://img.shields.io/badge/github%20pages-121013?style=for-the-badge&logo=github&logoColor=white)

Générer la documentation Javadoc :
```sh
mvn javadoc:javadoc
```
Elle sera disponible localement dans `target/site/apidocs`.

Et en ligne : [Jpandas Javadoc](https://lisazannettacci.github.io/JPandas/)

## Description des choix que nous avons fait:
### 1) Stucture du DataFrame
Le `DataFrame` est conçu comme une table de hachage ([`LinkedHashMap<String, Series<?>>`](https://docs.oracle.com/javase/8/docs/api/java/util/LinkedHashMap.html)) où chaque clé représente une colonne, et chaque valeur est une `Series` (liste de données).

Pourquoi `LinkedHashMap` ?
Cela permet de préserver l'ordre d'insertion des colonnes, ce qui permet de rendre l'affichage cohérent et de faciliter les tests d'affichage.

### 2) Organisation du code
- `core`: classes principales `DataFrame` et `Series`

- `io`: lecture de CSV avec `LecteurCSV`

- `utils`: utilitaires pour les tests

- `test`: tests unitaires avec `JUnit`


## Description du workflow mis en place
Sur GitHub, nous avons adopté la méthode suivante pour essayer de garantir la qualité, la lisibilité et la maintenabilité du code. Voici les principales pratiques mises en place :

- Protection de la branche main:

La branche main est protégée afin de s'assurer que seul du code validé, testé et relu puisse y être intégré.
Toute modification passe obligatoirement par une Pull Request (PR), validée par l’autre membre de l’équipe.

- Une branche par fonctionnalité:

Pour chaque nouvelle fonctionnalité ou correction de gros bug, nous créons une branche dédiée à partir de main, nommée de manière explicite (feature/x, fix/y, etc.).
Cela permet de travailler en parallèle sans impacter le code stable, et de suivre précisément l'historique des changements.

- Stratégie de développement:

Nous adoptons une méthode en trois étapes :
    1) Définition de la signature de la méthode ou fonctionnalité.
    2) Implémentation de la logique métier.
    3) Écriture des tests unitaires avec JUnit, dans l’objectif de couvrir l’ensemble des cas d’usage possibles.

- Gestion des push intermédiaires:

Nous faisons des pushs réguliers sur nos branches de travail, afin de :
    - sauvegarder notre avancement,
    - permettre une collaboration en temps réel,
    - et vérifier l’état du code via GitHub Actions.
Nous faisons en sorte que le code reste toujours dans un état stable : les tests doivent passer sur la pipeline même si la fonctionnalité est encore incomplète. Cela évite de bloquer le travail futur.


- Procédure de validation d'une Pull Request:

    1) Analyse du lancement automatique de la CI/CD (GitHub Actions) pour vérifier que :
        - le projet compile correctement,
        - tous les tests passent,
        - la génération de la documentation est réussie.
    2) Relecture croisée du code :
        - Analyse de la clarté et lisibilité du code.
        - Propositions d'améliorations éventuelles.
        - Vérification de la cohérence de la fonctionnalité avec la logique du projet, analyse de la spécification pour analyser la cohérence avec l'implémentation.
        - Relecture orthographique et stylistique de la documentation liée. Plus vérification de l'exhaustivité.
    3) Contrôle de la couverture de tests avec JaCoCo :
        - Nous exigeons une couverture minimale de 85% sur les nouvelles parties du code.
        - Si la couverture est insuffisante, nous demandons l’ajout de tests supplémentaires.
        
- Déploiement automatique de la documentation :

Une fois la PR fusionnée, un bot GitHub Actions génère automatiquement la documentation Javadoc.
Cette documentation est publiée via la branche gh-pages, ce qui la rend accessible en ligne via GitHub Pages.

## Description des images Docker/ lien vers le dépôt

![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

## Remarques
La première chose que nous avons fait est l'initialisation du workflow de GitHub Action.
Selon nous, partir sur des bases solides est essentiel pour pouvoir coder de manière fiable et maintenable.
Nous avons donc la première semaine mis en place:

- La configuration du bot GitHub Pages (création et ajout d’un Personal Access Token sécurisé).

- La protection de la branche main, afin d’imposer le passage par des Pull Requests pour toute modification.

- L’initialisation du fichier pom.xml pour gérer les dépendances et la compilation via Maven.

- La création du fichier ci.yml pour automatiser les étapes de build, test et déploiement de la documentation.

Nous nous sommes assurées de leur bon fonctionnement en corrigeant les bug via la branche fix_ci.

### Points à améliorer: 
Nous avons fait le choix d’écrire :

- nos commentaires,
- la documentation,
- les noms de méthodes,
- une grande partie de notre workflow Git
… en français, afin de favoriser notre compréhension mutuelle et de rester efficaces dans un contexte pédagogique.

Cependant, nous sommes conscientes que, dans un environnement professionnel ou open source, il est recommandé d’utiliser l’anglais comme langue de référence, notamment pour favoriser :

- la collaboration internationale,

- l’intégration de contributions externes,

- et la standardisation des projets open source.

## Feedback

## Auteurs
- Lisa ZANNETTACCI  
- Justine REAT
