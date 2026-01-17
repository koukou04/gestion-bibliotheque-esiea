# ========================================
# STAGE 1 : Build avec Maven
# ========================================
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /build

# Copier les fichiers Maven
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Télécharger les dépendances (cache Docker)
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copier le code source
COPY src src

# Compiler l'application (sans tests pour gagner du temps)
RUN ./mvnw clean package -DskipTests

# ========================================
# STAGE 2 : Image finale légère
# ========================================
FROM eclipse-temurin:17-jdk

# Informations sur le maintainer
LABEL maintainer="bibliotheque@esiea.fr"
LABEL description="Application de Gestion de Bibliothèque - Backend Spring Boot"
LABEL version="1.0"

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le JAR depuis le stage de build
COPY --from=builder /build/target/gestion-bibliotheque-0.0.1-SNAPSHOT.jar app.jar

# Exposer le port 8080 (port par défaut de Spring Boot)
EXPOSE 8080

# Variables d'environnement par défaut
ENV SPRING_PROFILES_ACTIVE=default
ENV KAFKA_BOOTSTRAP_SERVERS=kafka:9092
ENV KAFKA_ENABLED=true

# Point d'entrée pour exécuter l'application
# Options JVM pour optimiser les performances en conteneur
ENTRYPOINT ["java", \
            "-XX:+UseContainerSupport", \
            "-XX:MaxRAMPercentage=75.0", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-jar", \
            "app.jar"]

# Health check pour vérifier que l'application est en bonne santé
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1
