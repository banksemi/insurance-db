pipeline {
    agent any
    options {
        timeout(time: 1, unit: 'HOURS')
    }
    environment {
        SOURCECODE_JENKINS_CREDENTIAL_ID = 'banksemi'
        SOURCE_CODE_URL = 'https://github.com/banksemi/insurance-db.git'
        RELEASE_BRANCH = 'release'
    }
    stages {
        stage('Init') {
            steps {
                echo 'clear'
            }
        }
    }
}