pipeline {
    agent any
    tools {
        maven 'my-maven'
        jdk 'my-jdk'
    }

    stages {
        stage('Git') {
            steps {
                echo 'Pull code from GitHub'
                git url: 'https://github.com/AJABHIMANYU/car-api.git', branch: 'master'
            }
        }
        stage('Build') {
            steps {
                echo 'Build project using Maven'
                bat "mvn clean install -DskipTests"
            }
        }
        stage('Test') {
            steps {
                echo 'Test your application'
                bat "mvn test"
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploy the project'
                bat 'docker rm -f api-gateway-sr || true'
                bat 'docker rmi -f api-gateway || true'
                bat 'docker build -t api-gateway .'
                bat 'docker run --network demoproject_app-network -p 6565:6565 -d --name api-gateway-sr api-gateway'
            }
        }
    }
}
