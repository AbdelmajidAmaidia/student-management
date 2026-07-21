pipeline {

    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK17'
    }

    environment {
        IMAGE_NAME = 'amaidiaabdelmajiddo/student-management'
        IMAGE_TAG  = "${env.BUILD_NUMBER}"
    }

    options {
        timeout(time: 30, unit: 'MINUTES')
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '15'))
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/AbdelmajidAmaidia/student-management.git'
            }
        }

        stage('Build & Test') {
            steps {
                // clean + compile + test + package en un seul cycle de vie Maven
                sh 'mvn clean verify'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('JaCoCo Code Coverage') {
           steps {
        echo 'JaCoCo report already generated during verify.'
    }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

     

        stage('Deploy to Nexus Repository') {
            when { branch 'main' }
            steps {
                sh 'mvn deploy -DskipTests'
            }
        }

        stage('Publish Artifact') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Build Docker Image') {
            when { branch 'main' }
            steps {
                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
                sh "docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${IMAGE_NAME}:latest"
            }
        }

        stage('Push Docker Image') {
            when { branch 'main' }
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push ${IMAGE_NAME}:${IMAGE_TAG}
                        docker push ${IMAGE_NAME}:latest
                        docker logout
                    '''
                }
            }
        }

        stage('Deploy to Kubernetes') {
            when { branch 'main' }
            steps {
                input message: "Déployer la version ${IMAGE_TAG} en production ?", ok: 'Déployer'
                withKubeConfig([credentialsId: 'kubeconfig-prod']) {
                    sh "./scripts/deploy.sh ${IMAGE_TAG}"
                }
            }
        }
    }

    post {

        always {
            publishHTML(target: [
                allowMissing: true,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'target/site/jacoco',
                reportFiles: 'index.html',
                reportName: 'JaCoCo Report'
            ])

            archiveArtifacts artifacts: 'target/jacoco.exec',
                              allowEmptyArchive: true,
                              fingerprint: true

            cleanWs()
        }

        success {
            echo "✅ Pipeline SUCCESS — version ${IMAGE_TAG}"
        }

        failure {
            echo '❌ Pipeline FAILED'
            script {
                if (env.BRANCH_NAME == 'main') {
                    withKubeConfig([credentialsId: 'kubeconfig-prod']) {
                        sh '''
                            if kubectl get deployment student-management >/dev/null 2>&1; then
                                echo "Rollback vers la version précédente..."
                                kubectl rollout undo deployment/student-management
                            else
                                echo "Déploiement introuvable. Rollback ignoré."
                            fi
                        '''
                    }
                }
            }
        }
    }
}       
