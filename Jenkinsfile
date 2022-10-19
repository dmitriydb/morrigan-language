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
      stage('Unit tests') {
          steps {
              dir('morrigan-language') {
                 bat 'mvn test'
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
              dir('morrigan-language') {
                 bat 'mvn package'
              }
              dir ('morrigan-language/anonymous-coderun-service') {
                  bat 'mvn clean package -Pproduction -DskipTests'
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