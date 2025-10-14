# Gestion Spectacle Front

Application mobile Android pour la gestion des spectacles, permettant la consultation, la réservation et le paiement de billets.

## Fonctionnalités

- Liste des spectacles
- Détail d’un spectacle (artistes, lieux, dates)
- Réservation de billets (utilisateur connecté ou invité)
- Paiement en ligne
- Authentification (connexion, inscription)
- Confirmation de réservation

## Structure du projet

```
app/
 ├── src/
 │    ├── main/
 │    │    ├── java/com/example/spectacleapp/
 │    │    │    ├── MainActivity.java
 │    │    │    ├── activities/
 │    │    │    ├── adapter/
 │    │    │    ├── dtos/
 │    │    │    ├── model/
 │    │    │    ├── service/
 │    │    │    └── utils/
 │    │    ├── res/
 │    │    │    ├── layout/
 │    │    │    ├── drawable/
 │    │    │    ├── menu/
 │    │    │    ├── mipmap-*/
 │    │    │    ├── values/
 │    │    │    └── xml/
 │    │    └── AndroidManifest.xml
 │    ├── test/
 │    └── androidTest/
 ├── build.gradle
 ├── proguard-rules.pro
...
```

## Installation

1. **Prérequis** :
   - Android Studio (Arctic Fox ou plus récent)
   - JDK 8+
   - Gradle

2. **Cloner le projet** :
   ```powershell
   git clone https://github.com/souissi-ons/gestionSpectacleFront
   ```

3. **Ouvrir dans Android Studio** :
   - Fichier > Ouvrir > Sélectionner le dossier du projet

4. **Lancer l’application** :
   - Connecter un appareil ou utiliser un émulateur
   - Cliquer sur "Run"

## Configuration

- Les dépendances sont gérées via Gradle (`build.gradle`).
- Les ressources (images, layouts, etc.) sont dans `app/src/main/res/`.
- Les activités principales sont dans `app/src/main/java/com/example/spectacleapp/activities/`.

## Arborescence des principales classes

- `MainActivity.java` : Page d’accueil
- `activities/` : Activités (écrans) de l’application
- `adapter/` : Adapters pour RecyclerView
- `dtos/` : Objets de transfert de données
- `service/` : Services pour la logique métier et accès aux API
- `utils/` : Utilitaires
