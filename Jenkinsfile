pipeline {

    agent {
        label "wsl"
    }


    tools {

        maven 'Maven3'
        jdk 'JDK17'

    }


    environment {

        IMAGE_NAME = 'amaidiaabdelmajiddo/student-management'
        IMAGE_TAG  = "${env.BUILD_NUMBER}"

        K8S_REPO   = 'https://github.com/AbdelmajidAmaidia/student-management-k8s.git'

    }


    options {

        timeout(time: 30, unit: 'MINUTES')

        timestamps()

        disableConcurrentBuilds()

        buildDiscarder(logRotator(numToKeepStr: '15'))

    }



    stages {


        /*
         * 1 - Récupération du code Spring Boot
         */

        stage('Checkout Application') {

            steps {

                git branch: 'main',
                    credentialsId: 'github-token',
                    url: 'https://github.com/AbdelmajidAmaidia/student-management.git'

            }
        }




        /*
         * 2 - Compilation + Tests
         */

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




        /*
         * 3 - Analyse SonarQube
         */

        stage('SonarQube Analysis') {

            steps {

                withSonarQubeEnv('SonarQube') {

                    sh '''
                    mvn clean verify sonar:sonar
                    '''

                }

            }

        }




        /*
         * 4 - Publication Nexus
         */

        stage('Deploy Artifact Nexus') {

            steps {

                sh '''
                mvn deploy -DskipTests
                '''

            }

        }




        /*
         * 5 - Création image Docker
         */

        stage('Build Docker Image') {

            steps {

                sh """

                docker build \
                -t ${IMAGE_NAME}:${IMAGE_TAG} .

                docker tag \
                ${IMAGE_NAME}:${IMAGE_TAG} \
                ${IMAGE_NAME}:latest

                """

            }

        }





        /*
         * 6 - Push Docker Hub
         */

        stage('Push Docker Image') {


            steps {


                withCredentials([usernamePassword(

                    credentialsId: 'dockerhub',

                    usernameVariable: 'DOCKER_USER',

                    passwordVariable: 'DOCKER_PASS'


                )]) {


                    sh '''

                    echo "$DOCKER_PASS" | docker login \
                    -u "$DOCKER_USER" \
                    --password-stdin


                    docker push ${IMAGE_NAME}:${IMAGE_TAG}


                    docker push ${IMAGE_NAME}:latest


                    docker logout

                    '''

                }

            }

        }





        /*
         * 7 - Mise à jour du repo Kubernetes
         */

        stage('Update Kubernetes Manifest') {


            steps {


                dir('k8s-repo') {


                    git branch: 'main',

                        credentialsId: 'github-token',

                        url: "${K8S_REPO}"



                    sh """


                    echo "Avant modification :"

                    grep image k8s/deployment.yaml



                    sed -i \
                    's#image:.*#image: ${IMAGE_NAME}:${IMAGE_TAG}#' \
                    k8s/deployment.yaml



                    echo "Après modification :"

                    grep image k8s/deployment.yaml



                    """


                }

            }

        }





        /*
         * 8 - Commit + Push vers GitOps repo
         */

        stage('Push GitOps Change') {


            steps {


                dir('k8s-repo') {


                    sh """


                    git config user.name "Jenkins"

                    git config user.email "jenkins@company.com"



                    git add k8s/deployment.yaml



                    git commit \
                    -m "Update image version ${IMAGE_TAG}" || true



                    """



                    withCredentials([gitUsernamePassword(

                        credentialsId: 'github-token',

                        gitToolName: 'Default'


                    )]) {


                        sh """

                        git push origin main

                        """

                    }


                }


            }


        }



    }




    post {


        always {


            publishHTML([

                allowMissing: true,

                alwaysLinkToLastBuild: true,

                keepAll: true,

                reportDir: 'target/site/jacoco',

                reportFiles: 'index.html',

                reportName: 'JaCoCo Report'

            ])



            archiveArtifacts(

                artifacts: 'target/*.jar',

                allowEmptyArchive: true,

                fingerprint: true

            )



            cleanWs()

        }



        success {


            echo """

            ✅ Pipeline SUCCESS

            Docker Image :
            ${IMAGE_NAME}:${IMAGE_TAG}

            Argo CD va synchroniser Kubernetes

            """

        }



        failure {


            echo """

            ❌ Pipeline FAILED

            Aucun déploiement Kubernetes effectué

            """

        }


    }


}
