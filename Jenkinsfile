pipeline {
    agent any

    stages {
        stage('github clone') {
            steps {
                git branch: 'main', credentialsId: 'tintin010',
                    url: 'https://github.com/KU-OOAD/DVM.git'
            }
        }
        
        stage('Build') {
            steps {
                sh 'chmod +x ./gradlew'  // gradlew 파일에 실행 권한 추가
                sh './gradlew build'     // 빌드 실행 .
            }
        }
        
        stage('Test') {
            steps {
                sh 'echo Testing...'
                sh './gradlew test'  // 실제 테스트 명령어를 여기에 추가.
            }
            post {
                always {
                    junit '**/build/test-results/test/*.xml'  // JUnit 테스트 결과 파일 경로..
                }
            }
        }
        
        stage('Deploy') {
            steps {
                sshagent(['tintin010']) {
                    sh '''
                        ssh -o StrictHostKeyChecking=no ec2-user@ec2-13-124-36-229.ap-northeast-2.compute.amazonaws.com \
                        "sudo mkdir -p /home/ec2-user/deploy && \
                        sudo chmod -R 777 /home/ec2-user/deploy"
                        
                        scp /var/lib/jenkins/workspace/jenkins0531/build/libs/*.jar ec2-user@ec2-13-124-36-229.ap-northeast-2.compute.amazonaws.com:/home/ec2-user/deploy
                        
                        ssh -o StrictHostKeyChecking=no ec2-user@ec2-13-124-36-229.ap-northeast-2.compute.amazonaws.com \
                        "sudo chmod +x /home/ec2-user/deploy/*.jar && \
                        currentPid=$(ps -ef | grep java | grep dokotlin | awk '{print \$2}') && \
                        if [ -n \"\$currentPid\" ]; then \
                            echo 'Stopping current Java process with PID: \$currentPid' && \
                            kill -9 \$currentPid && \
                            sleep 10; \
                        fi && \
                        echo 'Starting new Java application' && \
                        nohup java -jar /home/ec2-user/deploy/*.jar >> /home/ec2-user/deploy/application.log 2>&1 &"
                    '''

                        }
            }
        }
    }
    
    post {
        success {
            echo 'Build, test, and deploy succeeded!'
        }
        failure {
            mail to: 'tintin4601@gmail.com',
                 subject: "Failed Build ${currentBuild.fullDisplayName}",
                 body: "The build FAILED: Check console output at ${env.BUILD_URL} to view the results."
        }
        always {
             mail to: 'tintin4601@gmail.com',
                 subject: "Build ${currentBuild.fullDisplayName} - ${currentBuild.result}",
                 body: """
                    <html>
                        <body>
                            <h2>Build Notification</h2>
                            <p>The build result is: ${currentBuild.result}.</p>
                            <p>Check console output at <a href="${env.BUILD_URL}">${env.BUILD_URL}</a> to view the results.</p>
                        </body>
                    </html>
                 """
        }
    }
}




