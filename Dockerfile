# Etape 1: Utiliser une image Java de base
FROM openjdk:17-jdk-slim

# Etape 2: Définir le répertoire de travail
WORKDIR /app

# Etape 3: Copier pom.xml dans le conteneur
COPY pom.xml /app/pom.xml

# Etape 4: Copier le code source dans le conteneur
COPY src /app/src

# Etape 5: Compiler le projet avec Maven

RUN apt-get update && apt-get install -y maven  # Installer Maven
RUN mvn clean package  # Compilation du projet avec Maven

# Etape 6: Exécuter la classe principale (JPandas.java)
CMD ["java", "-cp", "target/JPandas-1.0-SNAPSHOT.jar", "com.jpandas.JPandas"]
