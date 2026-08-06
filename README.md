# 🎓 Student Management - Spring Boot DevOps Project

## 📌 Description

Dans le cadre de ce projet, j’ai développé une application de **gestion des étudiants** basée sur une architecture **Spring Boot** pour la partie backend.

L’objectif principal est de construire une application backend moderne tout en mettant en place une chaîne complète **CI/CD**, un environnement de **déploiement automatisé**, ainsi qu’une solution de **supervision et monitoring**.

Le projet applique les pratiques DevOps modernes :

- Automatisation du build et des tests
- Analyse de qualité du code
- Conteneurisation avec Docker
- Déploiement Kubernetes
- Approche GitOps avec ArgoCD
- Gestion des artefacts avec Nexus
- Monitoring avec Prometheus et Grafana
- Déploiement Cloud avec Azure App Service


---

# 🏗️ Architecture globale

```
                         Developer
                             |
                             |
                         GitHub
                             |
          +------------------+------------------+
          |                                     |
          v                                     v
     Jenkins CI/CD                       GitHub Actions
          |                                     |
          |                                     |
 Build - Test - SonarQube              Maven Build
          |                                     |
          |                                     |
 Docker Image Build                    Spring Boot JAR
          |                                     |
          v                                     v
     Docker Hub                         Azure App Service
          |
          |
          v
 Kubernetes Cluster (Minikube)
          |
          |
          v
       ArgoCD
       GitOps
          |
          |
          v
 Prometheus + Grafana Monitoring
```


---

# 🚀 Fonctionnalités

L'application expose une API REST permettant la gestion des étudiants :

- Ajouter un étudiant
- Consulter les étudiants
- Rechercher un étudiant
- Modifier les informations d’un étudiant
- Supprimer un étudiant


---

# 🛠️ Technologies utilisées

## Backend

| Technologie | Description |
|---|---|
| Java | Langage de programmation |
| Spring Boot | Développement de l'API REST |
| Maven | Gestion des dépendances et build |
| MySQL | Base de données relationnelle |


## Tests et qualité

| Technologie | Description |
|---|---|
| JUnit | Tests unitaires |
| JaCoCo | Couverture de code |
| SonarQube | Analyse qualité du code |


## CI/CD

| Technologie | Description |
|---|---|
| Jenkins | Automatisation du pipeline CI/CD |
| Docker | Conteneurisation |
| Docker Hub | Stockage des images Docker |
| Nexus Repository | Gestion des artefacts Maven |


## Déploiement

| Technologie | Description |
|---|---|
| Kubernetes | Orchestration des conteneurs |
| Minikube | Cluster Kubernetes local |
| ArgoCD | Continuous Deployment avec GitOps |


## Monitoring

| Technologie | Description |
|---|---|
| Prometheus | Collecte des métriques |
| Grafana | Dashboards de supervision |
| Spring Boot Actuator | Exposition des métriques et health checks |


## Cloud

| Technologie | Description |
|---|---|
| GitHub Actions | Automatisation du déploiement Cloud |
| Microsoft Azure | Plateforme Cloud |
| Azure App Service | Hébergement de l'application |
| JAR Deployment | Publication du fichier `.jar` |


---

# 🔄 Pipeline CI/CD Jenkins

Le pipeline Jenkins automatise toutes les étapes nécessaires pour construire, tester et déployer l'application.


## 1. Checkout

Récupération du code source depuis GitHub.

```
GitHub → Jenkins
```


## 2. Build et tests

Compilation du projet et exécution des tests :

```bash
mvn clean verify
```


## 3. Analyse SonarQube

Analyse automatique de la qualité du code :

```bash
mvn sonar:sonar
```


Cette analyse permet de détecter :

- Bugs
- Vulnérabilités
- Code smells
- Couverture des tests


## 4. Packaging Maven

Création du fichier JAR Spring Boot :

```bash
mvn package
```


Résultat :

```
target/student-management.jar
```


## 5. Publication Nexus

Les artefacts Maven sont publiés dans Nexus Repository.


## 6. Création de l'image Docker

Construction de l'image :

```bash
docker build -t student-management .
```


## 7. Push Docker Hub

Publication de l'image Docker :

```bash
docker push <docker-image>
```


## 8. Déploiement Kubernetes

L'image Docker est déployée dans Kubernetes avec ArgoCD selon l'approche GitOps.


---

# 🐳 Docker

L'application Spring Boot est conteneurisée avec Docker.


Création de l'image :

```bash
docker build -t student-management .
```


Exécution locale :

```bash
docker run -p 8080:8080 student-management
```


---

# ☸️ Déploiement Kubernetes

Les fichiers Kubernetes sont organisés comme suit :

```
k8s/
│
├── namespace.yaml
├── deployment.yaml
├── service.yaml
├── configmap.yaml
└── ingress.yaml
```


Les composants déployés :

- Spring Boot Application
- MySQL Database
- Kubernetes Deployment
- Kubernetes Service
- ConfigMap pour la configuration


Commandes utiles :

Voir les pods :

```bash
kubectl get pods -n student-test
```


Voir les services :

```bash
kubectl get svc -n student-test
```


---

# 🔁 GitOps avec ArgoCD

ArgoCD permet d'assurer le déploiement continu avec une approche GitOps.


Principe :

```
Git Repository
        |
        v
      ArgoCD
        |
        v
 Kubernetes Cluster
```


Toute modification du repository Kubernetes est automatiquement synchronisée avec le cluster.


---

# ☁️ Déploiement Azure avec GitHub Actions

En complément du pipeline Jenkins, un workflow **GitHub Actions** permet de déployer automatiquement l'application Spring Boot sur Microsoft Azure.


Structure :

```
.github/
│
└── workflows/
    └── azure-deploy.yml
```


Le workflow réalise :

```
GitHub Repository
        |
        v
GitHub Actions
        |
        |
        |---- Checkout Code
        |---- Setup Java
        |---- Maven Build
        |---- Generate JAR
        |
        v
Azure App Service
```


## Étapes du déploiement


### 1. Checkout

Récupération automatique du code depuis GitHub.


### 2. Configuration Java

Installation de l'environnement Java nécessaire.


### 3. Build Maven

Compilation et génération du package :

```bash
mvn clean package
```


Création du fichier :

```
target/student-management.jar
```


### 4. Publication Azure

Le fichier `.jar` est déployé automatiquement sur :

```
Azure App Service
```


---

# 🔐 Gestion des secrets Azure

Les informations sensibles sont stockées dans les secrets GitHub Actions.


Configuration :

```
GitHub Repository

Settings

Secrets and variables

Actions
```


Exemples :

```
AZURE_WEBAPP_NAME

AZURE_WEBAPP_PUBLISH_PROFILE
```


---

# 📊 Monitoring

L'application utilise **Spring Boot Actuator** afin d'exposer les métriques.


Endpoint Prometheus :

```
/actuator/prometheus
```


Exemple :

```bash
curl http://localhost:8080/actuator/prometheus
```


Les métriques sont collectées par Prometheus et visualisées dans Grafana.


Métriques surveillées :

- JVM Memory
- CPU Usage
- HTTP Requests
- Application Health
- Database Connections


---

# ⚙️ Installation locale


## Prérequis

Installer :

- Java 17+
- Maven
- Docker
- Kubernetes
- Minikube
- kubectl


---

## Cloner le projet

```bash
git clone <repository-url>
```


Accéder au projet :

```bash
cd student-management
```


---

## Lancer l'application

```bash
mvn spring-boot:run
```


Application disponible :

```
http://localhost:8080
```


---

# 🧪 Tests


Exécuter les tests :

```bash
mvn test
```


Générer le rapport JaCoCo :

```bash
mvn verify
```


---

# 📈 Améliorations futures

- Ajouter Spring Security avec JWT
- Ajouter une gestion des rôles utilisateurs
- Déployer sur Azure Kubernetes Service (AKS)
- Ajouter Terraform pour l'Infrastructure as Code
- Ajouter une stratégie DevSecOps
- Ajouter des tests d’intégration avec Testcontainers


---

# 👨‍💻 Auteur

**Abdelmajid Amaidia**

Projet DevOps - Student Management Application


Technologies principales :

Spring Boot  
Jenkins  
Docker  
Kubernetes  
ArgoCD  
Azure  
Prometheus  
Grafana
