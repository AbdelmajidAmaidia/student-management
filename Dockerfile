# Étape 1 : Utiliser une image Maven avec JDK 17 pour compiler le projet
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copier les fichiers Maven
COPY pom.xml .

# Télécharger les dépendances
RUN mvn dependency:go-offline

# Copier le code source
COPY src ./src

# Compiler et générer le JAR
RUN mvn clean package -DskipTests

# Étape 2 : Image légère pour exécuter l'application
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copier le JAR depuis l'étape de build
COPY --from=builder /app/target/*.jar app.jar

# Exposer le port de Spring Boot
EXPOSE 8080

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
