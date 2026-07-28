pipeline {

    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK17'
    }

    environment {
        IMAGE_NAME = 'amaidiaabdelmajiddo/student-management'
        IMAGE_TAG  = "${env.BUILD_NUMBER}"
        NAMESPACE  = "student-test"
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


        stage('SonarQube') {
            steps {
                script {

                    if (env.CHANGE_ID) {

                        echo "Pull Request détectée"
                        sh "mvn clean test"

                    } else if (env.BRANCH_NAME == "main") {

                        echo "Merge effectué - Analyse complète"

                        withSonarQubeEnv('SonarQube') {
                            sh "mvn clean verify sonar:sonar"
                        }
                    }
                }
            }
        }



        stage('Deploy to Nexus Repository') {
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

            steps {

                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."

                sh "docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${IMAGE_NAME}:latest"
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

                        docker push ${IMAGE_NAME}:${IMAGE_TAG}

                        docker push ${IMAGE_NAME}:latest

                        docker logout
                    '''
                }
            }
        }




        stage('Deploy to Kubernetes') {

            steps {

                input message: "Déployer la version ${IMAGE_TAG} ?", ok: 'Déployer'

                withKubeConfig([credentialsId: 'kubeconfig-prod']) {

                    sh "./scripts/deploy.sh ${IMAGE_TAG}"

                }
            }
        }



        /*
         Nouvelle étape :
         Vérification que Kubernetes démarre bien
        */

        stage('Verify Kubernetes Deployment') {

            steps {

                withKubeConfig([credentialsId: 'kubeconfig-prod']) {

                    sh '''
                    kubectl rollout status deployment/student-management \
                    -n ${NAMESPACE}

                    kubectl get pods -n ${NAMESPACE}
                    '''
                }
            }
        }



        /*
         Vérification Prometheus
         L'application doit exposer :
         /actuator/prometheus
        */

        stage('Verify Prometheus Metrics') {

            steps {

                withKubeConfig([credentialsId: 'kubeconfig-prod']) {

                    sh '''
                    echo "Checking Spring Boot Prometheus endpoint..."

                    kubectl run curl-test \
                    --image=curlimages/curl \
                    --restart=Never \
                    -n ${NAMESPACE} \
                    -- curl -f http://student-service:8080/actuator/prometheus


                    kubectl delete pod curl-test \
                    -n ${NAMESPACE} \
                    --ignore-not-found
                    '''
                }
            }
        }



        /*
         Vérification ServiceMonitor
        */

        stage('Verify ServiceMonitor') {

            steps {

                withKubeConfig([credentialsId: 'kubeconfig-prod']) {

                    sh '''
                    echo "Checking ServiceMonitor..."

                    kubectl get servicemonitor \
                    -n ${NAMESPACE}
                    '''
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

            echo "📊 Prometheus metrics verified"

            echo "📈 Grafana dashboard ready"

        }



        failure {

            echo '❌ Pipeline FAILED'


            script {

                if (env.BRANCH_NAME == 'main') {


                    withKubeConfig([credentialsId: 'kubeconfig-prod']) {


                        sh '''

                        if kubectl get deployment student-management \
                        -n ${NAMESPACE} >/dev/null 2>&1;

                        then

                            echo "Rollback vers version précédente..."

                            kubectl rollout undo deployment/student-management \
                            -n ${NAMESPACE}

                        else

                            echo "Déploiement introuvable"

                        fi

                        '''
                    }
                }
            }
        }
    }
}
