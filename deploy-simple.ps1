# Script de deploiement automatique sur Render avec Docker

Write-Host "Deploiement automatique - Gestion Bibliotheque" -ForegroundColor Green
Write-Host "============================================================"

# Etape 1 : Build Maven
Write-Host ""
Write-Host "ETAPE 1/3 : Compilation Maven..." -ForegroundColor Cyan
./mvnw clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host "Erreur lors de la compilation Maven" -ForegroundColor Red
    exit 1
}

Write-Host "Compilation reussie !" -ForegroundColor Green

# Etape 2 : Build Docker Image
Write-Host ""
Write-Host "ETAPE 2/3 : Construction de l'image Docker..." -ForegroundColor Cyan
docker build -t bibliotheque-esiea:latest .

if ($LASTEXITCODE -ne 0) {
    Write-Host "Erreur lors du build Docker" -ForegroundColor Red
    exit 1
}

Write-Host "Image Docker creee : bibliotheque-esiea:latest" -ForegroundColor Green

# Etape 3 : Instructions pour Render
Write-Host ""
Write-Host "ETAPE 3/3 : Preparation pour Render..." -ForegroundColor Cyan
Write-Host "============================================================"
Write-Host ""
Write-Host "Image Docker prete !" -ForegroundColor Green
Write-Host ""
Write-Host "PROCHAINES ETAPES MANUELLES (5 minutes) :" -ForegroundColor Yellow
Write-Host ""
Write-Host "1. Aller sur : https://render.com" -ForegroundColor White
Write-Host "   - Creer compte avec email personnel" -ForegroundColor Gray
Write-Host ""
Write-Host "2. Creer un Web Service :" -ForegroundColor White
Write-Host "   - Cliquer New + > Web Service" -ForegroundColor Gray
Write-Host "   - Public Git repository" -ForegroundColor Gray
Write-Host "   - URL: https://gitlab.esiea.fr/ramanadane/gestion_bibliotheque_messai_ramanadane_ouallii" -ForegroundColor Gray
Write-Host ""
Write-Host "3. Configuration :" -ForegroundColor White
Write-Host "   - Name: bibliotheque-esiea" -ForegroundColor Gray
Write-Host "   - Region: Frankfurt" -ForegroundColor Gray
Write-Host "   - Branch: koussaila" -ForegroundColor Gray
Write-Host "   - Runtime: Docker" -ForegroundColor Gray
Write-Host "   - Plan: Free" -ForegroundColor Gray
Write-Host ""
Write-Host "4. Variables d'environnement :" -ForegroundColor White

# Charger les variables depuis .env.supabase
$envContent = Get-Content .env.supabase -Raw
$DATASOURCE_URL = ($envContent -match 'SPRING_DATASOURCE_URL=(.+)') | Out-Null; $matches[1]
$DATASOURCE_USER = ($envContent -match 'SPRING_DATASOURCE_USERNAME=(.+)') | Out-Null; $matches[1]
$DATASOURCE_PASS = ($envContent -match 'SPRING_DATASOURCE_PASSWORD=(.+)') | Out-Null; $matches[1]

Write-Host ""
Write-Host "   SPRING_PROFILES_ACTIVE = production" -ForegroundColor Cyan
Write-Host "   SPRING_DATASOURCE_URL = $DATASOURCE_URL" -ForegroundColor Cyan
Write-Host "   SPRING_DATASOURCE_USERNAME = $DATASOURCE_USER" -ForegroundColor Cyan
Write-Host "   SPRING_DATASOURCE_PASSWORD = $DATASOURCE_PASS" -ForegroundColor Cyan
Write-Host "   SPRING_KAFKA_ENABLED = false" -ForegroundColor Cyan
Write-Host ""
Write-Host "5. Cliquer Create Web Service et attendre 5-10 min" -ForegroundColor White
Write-Host ""
Write-Host "============================================================"
Write-Host ""

# Copier dans le presse-papier
$clipboardText = @"
SPRING_PROFILES_ACTIVE=production
SPRING_DATASOURCE_URL=$DATASOURCE_URL
SPRING_DATASOURCE_USERNAME=$DATASOURCE_USER
SPRING_DATASOURCE_PASSWORD=$DATASOURCE_PASS
SPRING_KAFKA_ENABLED=false
"@

Set-Clipboard -Value $clipboardText
Write-Host "Variables copiees dans le presse-papier !" -ForegroundColor Green
Write-Host "Collez-les directement dans Render !" -ForegroundColor Green
Write-Host ""
Write-Host "Script termine avec succes !" -ForegroundColor Green
Write-Host ""
