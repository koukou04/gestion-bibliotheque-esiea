#!/usr/bin/env bash
# Script de démarrage pour Render

echo "🚀 Démarrage de l'application Gestion Bibliothèque..."
echo "📍 Profil Spring actif : ${SPRING_PROFILES_ACTIVE:-production}"
echo "🗄️ Base de données : PostgreSQL (Supabase)"

# Démarrer l'application Spring Boot avec le profil production
java -Dserver.port=${PORT:-8080} \
     -Dspring.profiles.active=production \
     -XX:+UseContainerSupport \
     -XX:MaxRAMPercentage=75.0 \
     -jar target/gestion-bibliotheque-0.0.1-SNAPSHOT.jar
