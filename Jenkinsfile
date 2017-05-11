pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                // in special folder so that gradle-home does not get wiped after checkout of repository
                dir("webshop") {
                    // Temporary solution until https://issues.jenkins-ci.org/browse/JENKINS-38046
                    checkout scm
                }
            }
        }

        stage('Build') {
            agent {
                docker "rancher-server:5000/openjdk:8-jdk"
            }
            steps {
                withEnv(["GRADLE_USER_HOME=${pwd()}" + "/.gradle-home"]) {
                    // Sub directory within the workspace but not in the repository folder
                    dir("webshop") {
                        withCredentials([usernamePassword(credentialsId: '6cc6afa8-0869-43f0-91a2-d306642ecafa', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                            // Execute gradle script
                            sh './gradlew clean web:check -is -Pnexus_user=$USERNAME -Pnexus_password=$PASSWORD -Pnexus_contextUrl=$NEXUS_CONTEXTURL -Dorg.gradle.jvmargs="-Xmx512M"'
                        }
                    }
                }
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/build/test-results/**/*.xml'
//                    step([$class: 'Mailer', notifyEveryUnstableBuild: true, sendToIndividuals: true,
//                        recipients: 'rburgst@gmail.com'])
                }
            }
        }

    }
}
