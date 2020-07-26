pipeline {
    agent any
    environment {
        def CommitId = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
    }
    stages {
        stage('Checkout') {
            steps {
                echo "Current branch is ${BRANCH}"
                git(
                    branch: "${BRANCH}",
                    credentialsId: "b9a67cc7-1bf3-4c58-ad2f-3afd18283ff8",
                    url: "git@git.vmcshop.com:spingcloud/angsi-back-end/${SERVICES}.git",
                    changelog: true
                )
                echo "Checkout"
            }
        }

        stage('Build') {
            steps {
                    script {
                        sh """
                            sudo /usr/maven/apache-maven-3.6.1/bin/mvn -B -f ./pom.xml clean install -P${ENV} docker:build -DpushImageTag -DdockerImageTags=latest,${CommitId} -Dmaven.test.skip=true -Dmaven.compile.fork=true
                        """
                    }
                echo "Build"
            }
        }

        stage('Deploy') {
            steps {
                echo "Current operation is ${OPERATION}"
                script {
                    def InitYet = sh(script: "sudo ssh root@k8s-master 'ls /root/k8s/PROJECTS/${PROJECT}/${OWNER}/${SERVICES}.yaml'", returnStatus: true)
                    def Port = sh(script: "sudo grep -r 'server.port' ./src/main/resources/bootstrap-${ENV}.properties |gawk -F'[=]' '{print \$2}'", returnStdout: true).trim()
                    if (InitYet != 0) {
                        sh """
                            sudo cp -rp ./deployment.tmpl ./${SERVICES}.yaml
                            sudo sed -i 's/{{APPNAME}}/${SERVICES}/g' ./${SERVICES}.yaml
                            sudo sed -i 's/{{NAMESPACE}}/${PROJECT}/g' ./${SERVICES}.yaml
                            sudo sed -i 's/{{PORT}}/${Port}/g' ./${SERVICES}.yaml
                            sudo sed -i 's/{{FRONTBACK}}/${OWNER}/g' ./${SERVICES}.yaml
                            sudo scp -rp ./${SERVICES}.yaml root@k8s-master:/root/k8s/PROJECTS/${PROJECT}/${OWNER}/
                            sudo ssh root@k8s-master "kubectl apply -f /root/k8s/PROJECTS/${PROJECT}/${OWNER}/${SERVICES}.yaml"
                        """
                    }
                    else {
                        if (OPERATION == 'update') {
                            sh "sudo ssh root@k8s-master 'kubectl -n ${PROJECT} set image deployments/${SERVICES} ${SERVICES}=repos.vmcshop.com/k8s-${PROJECT}/${SERVICES}:${CommitId}'"
                        }
                        else if (OPERATION == 'rollback') {
                            sh "sudo ssh root@k8s-master 'kubectl -n ${PROJECT} rollout undo deployments/${SERVICES}'"
                        }
                        else {
                            echo "Unexpected error! Please contact admin(chenguo@vmcshop.com)."
                        }
                    }
                }
                echo "Deploy"
            }
        }
    }
}
