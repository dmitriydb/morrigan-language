pipeline {
    agent any

    stages {
      stage('Pulling from repo') {
          steps {
              cleanWs()
              bat 'git clone https://github.com/dmitriydb/morrigan-language'
              bat 'dir'
          }
      }
      stage('Build') {
          steps {
              dir('morrigan-language') {
                 bat 'mvn install'
              }
          }
      }
      stage('Integration tests') {
          steps {
              dir('morrigan-language/anonymous-coderun-service') {
                  bat 'mvn verify -PIT'
              }
          }
      }
      stage('Deploy to production') {
        steps {
            dir ('morrigan-language/anonymous-coderun-service') {
                bat 'mvn package assembly:single -Pproduction'
            }
            dir ('morrigan-language') {
                bat 'docker compose rm -svf'
                bat 'docker compose up --build -d'
            }
        }
      }
    }

    post {
        always {
            junit 'morrigan-language/anonymous-coderun-service/target/surefire-reports/*.xml'
            archiveArtifacts artifacts: 'morrigan-language/anonymous-coderun-service/target/*.jar', fingerprint: true
        }
    }
}