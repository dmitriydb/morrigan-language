pipeline {
    agent any

    stages {
      stage('Pulling from repo') {
          steps {
              cleanWs()
              bat 'git clone https://github.com/dmitriydb/morrigan-language'
          }
      }
      stage('Unit tests') {
          steps {
              dir('morrigan-language') {
                 bat 'mvn clean install'
              }
          }
      }
      stage('Integration tests') {
          steps {
              dir('morrigan-language/anonymous-coderun-service') {
                  bat 'mvn verify -PIT'
              }
              dir('morrigan-language/service-registry') {
                  bat 'mvn verify -PIT'
              }
          }
      }
      stage('Build') {
          steps {
              dir ('morrigan-language/anonymous-coderun-service') {
                  bat 'mvn package -Pproduction -DskipTests'
              }
          }
      }
      stage('Deploy to production') {
        steps {
            dir ('morrigan-language') {
                bat 'docker compose rm -svf'
                bat 'docker compose up --build -d --force-recreate'
            }
        }
      }
     }

    post {
        always {
            junit 'morrigan-language/anonymous-coderun-service/target/surefire-reports/*.xml'
            junit 'morrigan-language/morrigan-language/target/surefire-reports/*.xml'
            archiveArtifacts artifacts: 'morrigan-language/anonymous-coderun-service/target/*.jar', fingerprint: true
        }
    }
}