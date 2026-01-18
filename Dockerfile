# Multi-stage build pour optimiser la taille de l'image

# Stage 1: Build avec Maven
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /build

# Copier les fichiers de configuration Maven
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Télécharger les dépendances (mise en cache Docker)
RUN mvn dependency:go-offline -B

# Copier le code source
COPY src ./src

# Construire l'application
RUN mvn clean package -DskipTests -B

# Stage 2: Image d'exécution légère
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Installer bash pour le script de démarrage
RUN apk add --no-cache bash

# Copier le JAR depuis l'étape de build
COPY --from=build /build/target/*.jar app.jar

# Copier le script de démarrage
COPY start.sh /app/start.sh
RUN chmod +x /app/start.sh

# Exposer le port (Render utilise la variable PORT)
EXPOSE ${PORT:-8080}

# Utiliser le script de démarrage
CMD ["/app/start.sh"]
