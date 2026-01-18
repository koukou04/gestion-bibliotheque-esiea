#!/bin/bash

# Script de démarrage pour Render
# Transforme l'URL PostgreSQL de Render en format JDBC pour Spring Boot

if [ -n "$DATABASE_URL" ]; then
  # Render fournit souvent DATABASE_URL au format:
  #   postgresql://user:password@host:port/db
  # Le driver JDBC n'accepte pas user:password@ dans la partie host, il faut extraire
  # les credentials et reconstruire une URL JDBC propre.
  RAW_DB_URL="$DATABASE_URL"

  # Normalisation : enlever un éventuel préfixe jdbc:
  RAW_DB_URL="${RAW_DB_URL#jdbc:}"

  if [[ "$RAW_DB_URL" == postgresql://* ]] || [[ "$RAW_DB_URL" == postgres://* ]]; then
    RAW_DB_URL="${RAW_DB_URL#postgresql://}"
    RAW_DB_URL="${RAW_DB_URL#postgres://}"

    if [[ "$RAW_DB_URL" == *"@"* ]]; then
      CREDS="${RAW_DB_URL%@*}"
      HOST_AND_PATH="${RAW_DB_URL#*@}"

      DATABASE_USER="${CREDS%%:*}"
      DATABASE_PASS="${CREDS#*:}"

      HOSTPORT="${HOST_AND_PATH%%/*}"
      PATH_AND_QUERY="${HOST_AND_PATH#*/}"
      DB_NAME="${PATH_AND_QUERY%%\?*}"

      QUERY=""
      if [[ "$PATH_AND_QUERY" == *"?"* ]]; then
        QUERY="${PATH_AND_QUERY#*\?}"
      fi

      DB_HOST="${HOSTPORT%%:*}"
      DB_PORT="${HOSTPORT#*:}"
      if [[ "$DB_HOST" == "$DB_PORT" ]]; then
        DB_PORT="5432"
      fi

      export DATABASE_USERNAME="$DATABASE_USER"
      export DATABASE_PASSWORD="$DATABASE_PASS"

      JDBC_URL="jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}"
      if [[ -n "$QUERY" ]]; then
        if [[ "$QUERY" != *"sslmode="* ]]; then
          QUERY="${QUERY}&sslmode=require"
        fi
        JDBC_URL="${JDBC_URL}?${QUERY}"
      else
        JDBC_URL="${JDBC_URL}?sslmode=require"
      fi

      export DATABASE_URL="$JDBC_URL"
      echo "✓ URL JDBC PostgreSQL construite (host=${DB_HOST}, port=${DB_PORT}, db=${DB_NAME})"
    else
      # Pas de credentials dans l'URL, juste ajouter jdbc:
      export DATABASE_URL="jdbc:postgresql://$RAW_DB_URL"
      echo "✓ URL PostgreSQL transformée en format JDBC"
    fi
  fi
  
  # Configuration PostgreSQL pour Spring Boot
  export DATABASE_DRIVER="org.postgresql.Driver"
  export DATABASE_PLATFORM="org.hibernate.dialect.PostgreSQLDialect"
  export DDL_AUTO="update"
  
  echo "✓ Configuration PostgreSQL appliquée"
fi

# Désactiver Kafka en production
export KAFKA_ENABLED=false
export SPRING_KAFKA_ENABLED=false

# Port de l'application (Render fournit la variable PORT)
SERVER_PORT=${PORT:-8080}

# Démarrer l'application
echo "Démarrage de l'application sur le port $SERVER_PORT..."
java -Dserver.port=$SERVER_PORT -jar /app/app.jar
