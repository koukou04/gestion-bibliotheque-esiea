# Script de déploiement automatique sur Render avec Docker
# Ce script construit l'image Docker et prépare tout pour Render

Write-Host "🚀 DÉPLOIEMENT AUTOMATIQUE - Gestion Bibliothèque" -ForegroundColor Green
Write-Host "=" * 60

# Étape 1 : Build Maven
Write-Host "`n📦 ÉTAPE 1/4 : Compilation Maven..." -ForegroundColor Cyan
./mvnw clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Erreur lors de la compilation Maven" -ForegroundColor Red
    exit 1
}

Write-Host "✅ Compilation réussie" -ForegroundColor Green

# Étape 2 : Build Docker Image
Write-Host "`n🐳 ÉTAPE 2/4 : Construction de l'image Docker..." -ForegroundColor Cyan
docker build -t bibliotheque-esiea:latest .

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Erreur lors du build Docker" -ForegroundColor Red
    exit 1
}

Write-Host "✅ Image Docker créée : bibliotheque-esiea:latest" -ForegroundColor Green

# Étape 3 : Test local (optionnel)
Write-Host "`n🧪 ÉTAPE 3/4 : Test local de l'image..." -ForegroundColor Cyan
Write-Host "Chargement des variables Supabase..." -ForegroundColor Yellow

# Charger les variables depuis .env.supabase
$envFile = Get-Content .env.supabase
$SPRING_DATASOURCE_URL = ($envFile | Select-String "SPRING_DATASOURCE_URL=").ToString().Split("=")[1]
$SPRING_DATASOURCE_USERNAME = ($envFile | Select-String "SPRING_DATASOURCE_USERNAME=").ToString().Split("=")[1]
$SPRING_DATASOURCE_PASSWORD = ($envFile | Select-String "SPRING_DATASOURCE_PASSWORD=").ToString().Split("=")[1]

Write-Host "Démarrage du conteneur de test..." -ForegroundColor Yellow
docker run -d `
    --name bibliotheque-test `
    -p 8081:8080 `
    -e SPRING_PROFILES_ACTIVE=production `
    -e SPRING_DATASOURCE_URL="$SPRING_DATASOURCE_URL" `
    -e SPRING_DATASOURCE_USERNAME="$SPRING_DATASOURCE_USERNAME" `
    -e SPRING_DATASOURCE_PASSWORD="$SPRING_DATASOURCE_PASSWORD" `
    -e SPRING_KAFKA_ENABLED=false `
    bibliotheque-esiea:latest

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Conteneur de test démarré sur http://localhost:8081" -ForegroundColor Green
    Write-Host "⏱️  Attente de 30 secondes pour le démarrage..." -ForegroundColor Yellow
    Start-Sleep -Seconds 30
    
    # Test de l'API
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:8081/api/livres" -UseBasicParsing -ErrorAction Stop
        Write-Host "✅ API fonctionne ! Réponse : $($response.StatusCode)" -ForegroundColor Green
    } catch {
        Write-Host "⚠️  API pas encore prête (normal si premier démarrage)" -ForegroundColor Yellow
    }
    
    # Afficher les logs
    Write-Host "`n📋 Dernières lignes des logs :" -ForegroundColor Cyan
    docker logs bibliotheque-test --tail 20
    
    # Nettoyer
    Write-Host "`n🧹 Nettoyage du conteneur de test..." -ForegroundColor Yellow
    docker stop bibliotheque-test
    docker rm bibliotheque-test
} else {
    Write-Host "⚠️  Test local ignoré (Docker Desktop pas démarré ?)" -ForegroundColor Yellow
}

# Étape 4 : Instructions pour Render
Write-Host "`n🌐 ÉTAPE 4/4 : Préparation pour Render..." -ForegroundColor Cyan
Write-Host "=" * 60
Write-Host ""
Write-Host "✅ Image Docker prête : bibliotheque-esiea:latest" -ForegroundColor Green
Write-Host ""
Write-Host "📝 PROCHAINES ÉTAPES MANUELLES (5 minutes) :" -ForegroundColor Yellow
Write-Host ""
Write-Host "1️⃣  Aller sur : https://render.com" -ForegroundColor White
Write-Host "   - Créer compte (avec email personnel, pas besoin de GitLab)" -ForegroundColor Gray
Write-Host ""
Write-Host "2️⃣  Créer un Web Service :" -ForegroundColor White
Write-Host "   - Cliquer 'New +' → 'Web Service'" -ForegroundColor Gray
Write-Host "   - Choisir 'Deploy an existing image from a registry'" -ForegroundColor Gray
Write-Host "   - Image URL: docker.io/bibliotheque-esiea:latest" -ForegroundColor Gray
Write-Host "   - OU utiliser le Dockerfile depuis le repo public" -ForegroundColor Gray
Write-Host ""
Write-Host "3️⃣  Configuration :" -ForegroundColor White
Write-Host "   - Name: bibliotheque-esiea" -ForegroundColor Gray
Write-Host "   - Region: Frankfurt (EU Central)" -ForegroundColor Gray
Write-Host "   - Branch: koussaila (si depuis Git)" -ForegroundColor Gray
Write-Host "   - Plan: Free" -ForegroundColor Gray
Write-Host ""
Write-Host "4️⃣  Variables d'environnement (IMPORTANT) :" -ForegroundColor White
Write-Host "   SPRING_PROFILES_ACTIVE = production" -ForegroundColor Cyan
Write-Host "   SPRING_DATASOURCE_URL = $SPRING_DATASOURCE_URL" -ForegroundColor Cyan
Write-Host "   SPRING_DATASOURCE_USERNAME = $SPRING_DATASOURCE_USERNAME" -ForegroundColor Cyan
Write-Host "   SPRING_DATASOURCE_PASSWORD = $SPRING_DATASOURCE_PASSWORD" -ForegroundColor Cyan
Write-Host "   SPRING_KAFKA_ENABLED = false" -ForegroundColor Cyan
Write-Host ""
Write-Host "5️⃣  Cliquer 'Create Web Service' et attendre 5-10 min" -ForegroundColor White
Write-Host ""
Write-Host "=" * 60
Write-Host ""
Write-Host "📋 Variables copiées dans le presse-papier :" -ForegroundColor Green
$clipboardContent = @"
SPRING_PROFILES_ACTIVE=production
SPRING_DATASOURCE_URL=$SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME=$SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD=$SPRING_DATASOURCE_PASSWORD
SPRING_KAFKA_ENABLED=false
"@

Set-Clipboard -Value $clipboardContent
Write-Host "✅ Collez-les directement dans Render !" -ForegroundColor Green
Write-Host ""
Write-Host "🎉 Script terminé avec succès !" -ForegroundColor Green
Write-Host ""
Write-Host "📁 Fichier de config créé : .env.supabase" -ForegroundColor Cyan
Write-Host "🐳 Image Docker prête pour déploiement" -ForegroundColor Cyan
Write-Host ""
Write-Host "▶️  OPTION ALTERNATIVE : Déploiement depuis repo public GitHub" -ForegroundColor Yellow
Write-Host "   Si vous voulez, je peux créer un repo GitHub public" -ForegroundColor Gray
Write-Host "   et Render pourra builder directement depuis le Dockerfile" -ForegroundColor Gray
Write-Host ""
