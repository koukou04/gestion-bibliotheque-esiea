#!/bin/bash

# Script de démarrage pour Render
# Transforme l'URL PostgreSQL de Render en format JDBC pour Spring Boot

if [ -n "$DATABASE_URL" ]; then
  # Si DATABASE_URL commence par postgresql://, le transformer en jdbc:postgresql://
  if [[ "$DATABASE_URL" == postgresql://* ]]; then
    export DATABASE_URL="jdbc:$DATABASE_URL"
    echo "✓ URL de base de données transformée en format JDBC"
  fi
  
  # Configuration PostgreSQL pour Spring Boot
  export DATABASE_DRIVER="org.postgresql.Driver"
  export DATABASE_PLATFORM="org.hibernate.dialect.PostgreSQLDialect"
  export DDL_AUTO="update"
  
  echo "✓ Configuration PostgreSQL appliquée"
fi

# Désactiver Kafka en production
export KAFKA_ENABLED=false

# Port de l'application (Render fournit la variable PORT)
SERVER_PORT=${PORT:-8080}

# Démarrer l'application
echo "Démarrage de l'application sur le port $SERVER_PORT..."
java -Dserver.port=$SERVER_PORT -jar /app/app.jar
