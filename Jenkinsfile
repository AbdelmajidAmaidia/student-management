pipeline {

    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK17'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/AbdelmajidAmaidia/student-management.git'
            }
        }

       stage('Build') {
    steps {
        sh 'mvn clean compile'
    }
}
        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Test Report') {
            steps {
                junit 'target/surefire-reports/*.xml'
            }
        }

        stage('JaCoCo Code Coverage') {
            steps {
                echo '📊 Generating JaCoCo Code Coverage Report...'
                // Le rapport est généré automatiquement par le plugin Maven
                // Vérifier que la couverture est >= 80%
                sh 'echo "JaCoCo report generated at: target/site/jacoco/index.html"'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package'
            }
        }

        stage('Publish Artifact') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t student-management .'
            }
        }

        stage('Tag Docker Image') {
            steps {
                sh 'docker tag student-management amaidiaabdelmajiddo/student-management:latest'
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {

                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin

                        docker push amaidiaabdelmajiddo/student-management:latest

                        docker logout
                    '''
                }
            }
        }

        stage('Deploy Kubernetes') {
            steps {
                sh './scripts/deploy.sh'
            }
        }

    }

    post {

        always {
            // 📊 Publier le rapport JaCoCo
       publishHTML(target: [
    allowMissing: true,
    alwaysLinkToLastBuild: true,
    keepAll: true,
    reportDir: 'target/site/jacoco',
    reportFiles: 'index.html',
    reportName: 'JaCoCo Report'
])

            // 📈 Archiver le fichier d'exécution JaCoCo pour l'historique
            archiveArtifacts artifacts: 'target/jacoco.exec', 
                             allowEmptyArchive: true,
                             fingerprint: true
        }

        success {
            echo '✅ Pipeline SUCCESS'
            echo '📊 JaCoCo Code Coverage Report is available'
        }

        failure {
            echo '❌ Pipeline FAILED'
            echo '⚠️ Check JaCoCo Code Coverage Report for details'

            sh '''
                if kubectl get deployment student-management >/dev/null 2>&1; then
                    echo "Rollback to previous version..."
                    kubectl rollout undo deployment/student-management
                else
                    echo "Deployment not found. Rollback skipped."
                fi
            '''
        }

    }

}
