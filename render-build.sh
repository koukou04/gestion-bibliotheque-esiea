#!/usr/bin/env bash
# Script de build pour Render

echo "🚀 Démarrage du build pour Render..."

# Build Maven sans tests (pour gagner du temps sur Render)
echo "📦 Compilation du projet avec Maven..."
./mvnw clean package -DskipTests

echo "✅ Build terminé avec succès!"
echo "📦 JAR créé : target/gestion-bibliotheque-0.0.1-SNAPSHOT.jar"
