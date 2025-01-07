
# Application E-commerce en Microservices

Ce projet est une application e-commerce modulaire construite en utilisant une architecture microservices. Chaque service est un système autonome développé en Java (avec Jakarta EE et Spring Data JPA) et géré indépendamment. Cette architecture garantit une meilleure scalabilité, maintenabilité et simplicité de déploiement.

## Table des Matières

- [Vue d'ensemble du projet](#vue-densemble-du-projet)
- [Modules et fonctionnalités](#modules-et-fonctionnalités)
- [Technologies utilisées](#technologies-utilisées)
- [Prise en main](#prise-en-main)
- [Exécution de l'application](#exécution-de-lapplication)
- [APIs](#apis)
- [Contribution](#contribution)
- [Licence](#licence)

## Vue d'ensemble du projet

L'**application e-commerce en microservices** comprend plusieurs services pour gérer différents aspects d'une plateforme e-commerce, notamment :

- **Gestion des stocks**
- **Traitement des commandes**
- **Passerelle API et routage**
- **Configuration centralisée**

### Points forts :

- Déploiement et mise à l'échelle indépendants des services.
- Interactions sécurisées entre services.
- Documentation des API avec Swagger.
- Communication interservices via des clients Feign.

---

## Modules et fonctionnalités

Le projet est organisé en plusieurs modules :

### 1. **Service de Configuration**

- Fournit une configuration centralisée pour tous les microservices.

### 2. **Service de Passerelle (Gateway)**

- Agit comme une passerelle API, dirigeant les requêtes externes vers les microservices correspondants.
- Gère la sécurité et les flux d'authentification.

### 3. **Service de Gestion des Stocks**

- Gère l'inventaire des produits.
- Opérations CRUD pour la gestion des produits.
- Accès sécurisé avec une autorisation basée sur les rôles et la validation des jetons.

### 4. **Service des Commandes**

- Gère les commandes des clients ainsi que les entités associées (ex. : produits dans une commande).
- Communique avec le Service de Gestion des Stocks via des clients Feign pour vérifier la disponibilité des produits.

---

## Technologies utilisées

### Frameworks Backend :
- **Java 22** avec **Jakarta EE** et **Spring Data JPA**

### Sécurité :
- Keycloak pour l'authentification.
- Sécurité basée sur des jetons JWT.

### Communication :
- Clients Feign pour les appels entre services.

### Gestion des APIs :
- Contrôleurs REST avec Spring Web.
- Documentation API avec Swagger.

### Infrastructure Microservices :
- Service de configuration centralisée.
- Passerelle pour le routage unifié.

### Outils de Build :
- Maven pour la gestion des dépendances et des builds.

### Bases de données :
- **H2** (base de données en mémoire pour le développement local).

---

## Prise en main

### Prérequis :

1. Installer le JDK (version 22 ou supérieure).
2. Installer [Maven](https://maven.apache.org/).
3. Lancer **Keycloak** si vous utilisez ses fonctionnalités d'authentification.

### Cloner le Repository :

```bash
git clone https://github.com/YASSINE-ABHIR/ecommerce-microservices-app.git
cd ecommerce-microservices-app
```

### Construire le Projet :

Dans le dossier racine où se trouve le fichier `pom.xml`, exécutez :

```bash
mvn clean install
```

### Configuration :

Vérifiez que les fichiers `application.properties` de chaque service sont correctement configurés pour votre environnement (base de données, Keycloak, etc.).

---

## Exécution de l'application

### Lancer le Service de Configuration :

Dans le répertoire `config-service`, exécutez :
```bash
mvn spring-boot:run
```

### Lancer le Service de Passerelle :

Dans le répertoire `gateway-service`, exécutez :
```bash
mvn spring-boot:run
```

### Lancer le Service de Gestion des Stocks :

Dans le répertoire `inventory-service`, exécutez :
```bash
mvn spring-boot:run
```

### Lancer le Service des Commandes :

Dans le répertoire `order-service`, exécutez :
```bash
mvn spring-boot:run
```

---

## APIs

### Documentation Swagger :

Swagger est activé pour plusieurs services. Une fois démarré, l'interface Swagger est accessible via les URLs suivantes :

- **Service de Gestion des Stocks :** `http://localhost:<port>/swagger-ui.html`
- **Service des Commandes :** `http://localhost:<port>/swagger-ui.html`

### Exemples d'Endpoints :

- **Service de Gestion des Stocks :**
  - `GET /api/products` - Liste tous les produits.
  - `POST /api/products` - Ajoute un nouveau produit.

- **Service des Commandes :**
  - `GET /api/orders` - Liste toutes les commandes.
  - `POST /api/orders` - Crée une nouvelle commande.

### Authentification :

Les services sont sécurisés avec des jetons JWT. Vous devez obtenir un jeton valide via Keycloak pour accéder aux endpoints protégés.

---

## Contribution

Les contributions sont les bienvenues ! Si vous souhaitez contribuer :

1. Forkez ce repository.
2. Créez une branche pour votre fonctionnalité (`git checkout -b feature/ma-fonctionnalité`).
3. Soumettez une Pull Request.


