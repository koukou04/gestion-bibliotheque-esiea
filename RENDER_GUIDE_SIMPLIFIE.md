# DEPLOIEMENT RENDER - GUIDE ULTRA SIMPLIFIE
# Tout se fait sur le site Render, aucun Docker requis !

## CE QUI EST DEJA FAIT
✅ Code modifié pour PostgreSQL
✅ Supabase configuré
✅ JAR compilé

## VARIABLES A COPIER DANS RENDER

```
SPRING_PROFILES_ACTIVE=production
SPRING_DATASOURCE_URL=jdbc:postgresql://db.jpydsnfjiefdjsvbacyu.supabase.co:5432/postgres
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=esiea2026Ivry
SPRING_KAFKA_ENABLED=false
```

---

## ETAPES A FAIRE MANUELLEMENT (10 minutes max)

### 1. RENDRE LE REPO GITLAB PUBLIC (2 min)

Sur GitLab ESIEA :
1. Aller dans votre projet : https://gitlab.esiea.fr/ramanadane/gestion_bibliotheque_messai_ramanadane_ouallii
2. Settings > General > Visibility
3. Changer en "Public"
4. Save changes

**Pourquoi ?** Render gratuit ne peut accéder qu'aux repos publics

---

### 2. CREER COMPTE RENDER (1 min)

1. Aller sur : https://render.com
2. Cliquer "Get Started"
3. Créer compte avec votre email personnel (pas ESIEA)
4. Confirmer l'email

---

### 3. CREER WEB SERVICE (2 min)

Dans Render Dashboard :
1. Cliquer "New +"
2. Sélectionner "Web Service"
3. Choisir "Public Git repository"
4. Coller l'URL : `https://gitlab.esiea.fr/ramanadane/gestion_bibliotheque_messai_ramanadane_ouallii`
5. Cliquer "Continue"

---

### 4. CONFIGURER LE SERVICE (3 min)

Remplir le formulaire :

**Nom** : `bibliotheque-esiea`

**Region** : `Frankfurt (EU Central)`

**Branch** : `koussaila`

**Runtime** : `Docker`

**Plan** : `Free`

Laisser vide :
- Build Command (le Dockerfile sera utilisé)
- Start Command (le Dockerfile sera utilisé)

---

### 5. AJOUTER VARIABLES D'ENVIRONNEMENT (2 min)

Cliquer "Advanced" puis "Add Environment Variable"

Ajouter 5 variables (copier-coller) :

| Key | Value |
|-----|-------|
| `SPRING_PROFILES_ACTIVE` | `production` |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://db.jpydsnfjiefdjsvbacyu.supabase.co:5432/postgres` |
| `SPRING_DATASOURCE_USERNAME` | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | `esiea2026Ivry` ⚠️ Cliquer 🔒 pour secret |
| `SPRING_KAFKA_ENABLED` | `false` |

---

### 6. DEPLOYER ! (1 clic)

1. Cliquer "Create Web Service" en bas
2. ⏱️ Attendre 5-10 minutes (première fois)
3. Suivre les logs en temps réel

---

## CE QUI SE PASSE

Render va :
1. ✅ Cloner votre repo GitLab public
2. ✅ Détecter le Dockerfile
3. ✅ Builder l'image (avec Maven)
4. ✅ Démarrer l'application
5. ✅ Vous donner une URL publique

---

## VERIFICATION

Une fois déployé :

**URL de votre API** : `https://bibliotheque-esiea.onrender.com`

**Tester** :
- Swagger : `https://bibliotheque-esiea.onrender.com/swagger-ui.html`
- API Livres : `https://bibliotheque-esiea.onrender.com/api/livres`

---

## CAPTURES D'ECRAN A FAIRE

1. **Swagger UI en ligne** : Ouvrir l'URL Swagger, capturer la page
2. **API en ligne** : Tester /api/livres, capturer la réponse JSON
3. **Dashboard Render** : Capturer le service "Live" avec URL
4. **Supabase Tables** : Aller dans Supabase > Table Editor, capturer les tables créées

---

## IMPORTANT : ⚠️

**Premier accès lent** : Le service gratuit Render s'endort après 15 min d'inactivité. La première requête après inactivité prend 30-60 secondes (réveil du service). C'est NORMAL !

---

## SI PROBLEME

**Logs Render** : Cliquer sur votre service > Logs (voir les erreurs)

**Erreur PostgreSQL** : Vérifier que les variables d'environnement sont bien copiées

**Timeout** : Augmenter dans Dockerfile si nécessaire

---

## ALTERNATIVES SI GITLAB NE FONCTIONNE PAS

### Option A : Créer repo GitHub miroir
```powershell
git remote add github https://github.com/VOTRE_USER/gestion-bibliotheque.git
git push github koussaila
```
Puis utiliser ce repo sur Render

### Option B : Upload ZIP
Render permet aussi d'uploader un ZIP directement

---

TOUT EST PRET ! Il ne reste que les étapes manuelles sur le site Render.

Temps estimé : 10 minutes maximum
