# JPandas

JPandas est une bibliothèque Java inspirée de la bibliothèque Python Pandas. Elle permet de manipuler, analyser et traiter des données de manière efficace en Java.

## Fonctionnalités principales
- Compatibilité avec Maven et intégration continue via GitHub Actions.

## Installation
### Prérequis
- Java 17 ou plus
- Maven

### Cloner le projet
```sh
git clone https://github.com/ton-utilisateur/JPandas.git
cd JPandas
```

### Construire le projet avec Maven
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

## Intégration Continue (CI)
Une pipeline GitHub Actions est configurée pour :
- Compiler et tester automatiquement le projet à chaque push/pull request.
- Générer et déployer la documentation Javadoc sur GitHub Pages.

## Documentation
Générer la documentation Javadoc :
```sh
mvn javadoc:javadoc
```
Elle sera disponible dans `target/site/apidocs`.

## Auteurs
- Lisa ZANNETTACCI  
- Justine REAT
