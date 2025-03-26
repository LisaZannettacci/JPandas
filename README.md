# JPandas
![Pandas](https://img.shields.io/badge/pandas-%23150458.svg?style=for-the-badge&logo=pandas&logoColor=white)

JPandas est une bibliothèque Java inspirée de la bibliothèque Python Pandas. Elle permet de manipuler, analyser et traiter des données de manière efficace en Java.

## Fonctionnalités principales
- Compatibilité avec Maven et intégration continue via GitHub Actions.

## Installation
### Prérequis
![Visual Studio Code](https://img.shields.io/badge/Visual%20Studio%20Code-0078d7.svg?style=for-the-badge&logo=visual-studio-code&logoColor=white)
![Eclipse](https://img.shields.io/badge/Eclipse-FE7A16.svg?style=for-the-badge&logo=Eclipse&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
- Java 17 ou plus
- Maven

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

Des tests sont effectués sur Ubuntu

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
Elle sera disponible dans `target/site/apidocs`.

## Description des choix que nous avons fait:

## Description du workflow mis en place

## Description des images Docker/ lien vers le dépôt

![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

## Feedback

## Auteurs
- Lisa ZANNETTACCI  
- Justine REAT
