pipeline {
    agent any
    
    tools {
        maven 'Maven-3.9'
        jdk 'JDK-17'
    }
    
    environment {
        DOCKER_IMAGE = 'tdd-projeto'
        DOCKER_TAG = "${BUILD_NUMBER}"
        JACOCO_THRESHOLD = '99'
    }
    
    stages {
        stage('📥 Checkout') {
            steps {
                echo '=== Clonando repositório ==='
                checkout scm
            }
        }
        
        stage('🧹 Clean') {
            steps {
                echo '=== Limpando projeto ==='
                bat 'mvn clean'
            }
        }
        
        stage('🔨 Build') {
            steps {
                echo '=== Compilando projeto ==='
                bat 'mvn compile'
            }
        }
        
        stage('🧪 Pipeline-test-dev') {
            steps {
                echo '=== Executando testes unitários e de integração ==='
                bat 'mvn test'
            }
            post {
                always {
                    // Publicar relatórios JUnit
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('📊 Análise de Código - PMD') {
            steps {
                echo '=== Executando análise PMD ==='
                bat 'mvn pmd:pmd pmd:cpd'
            }
            post {
                always {
                    // Publicar relatório PMD
                    recordIssues enabledForFailure: true, 
                                 tool: pmdParser(pattern: '**/target/pmd.xml')
                }
            }
        }
        
        stage('📈 Cobertura de Código - JaCoCo') {
            steps {
                echo '=== Gerando relatório JaCoCo ==='
                bat 'mvn jacoco:report'
            }
            post {
                always {
                    // Publicar relatório JaCoCo
                    jacoco execPattern: '**/target/jacoco.exec',
                           classPattern: '**/target/classes',
                           sourcePattern: '**/src/main/java',
                           exclusionPattern: '**/test/**'
                }
            }
        }
        
        stage('✅ Quality Gate - 99%') {
            steps {
                script {
                    echo '=== Verificando Quality Gate de 99% ==='
                    try {
                        bat 'mvn verify'
                        env.QUALITY_GATE_PASSED = 'true'
                        echo '✅ Quality Gate PASSOU! Cobertura >= 99%'
                    } catch (Exception e) {
                        env.QUALITY_GATE_PASSED = 'false'
                        error '❌ Quality Gate FALHOU! Cobertura < 99%'
                    }
                }
            }
        }
        
        stage('🐳 Image_Docker') {
            when {
                expression { env.QUALITY_GATE_PASSED == 'true' }
            }
            steps {
                script {
                    echo '=== Construindo imagem Docker ==='
                    bat """
                        docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .
                        docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest
                    """
                }
            }
        }
        
        stage('📦 Package') {
            when {
                expression { env.QUALITY_GATE_PASSED == 'true' }
            }
            steps {
                echo '=== Gerando JAR da aplicação ==='
                bat 'mvn package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts artifacts: '**/target/*.jar', 
                                     fingerprint: true
                }
            }
        }
    }
    
    post {
        always {
            echo '=== Pipeline finalizado ==='
            cleanWs()
        }
        success {
            echo '✅ Pipeline executado com SUCESSO!'
            emailext(
                subject: "✅ Build ${BUILD_NUMBER} - SUCESSO",
                body: """
                    Pipeline executado com sucesso!
                    
                    Build: ${BUILD_NUMBER}
                    Quality Gate: PASSOU (>= 99%)
                    Imagem Docker: ${DOCKER_IMAGE}:${DOCKER_TAG}
                    
                    Veja os detalhes: ${BUILD_URL}
                """,
                to: 'equipe@example.com'
            )
        }
        failure {
            echo '❌ Pipeline FALHOU!'
            emailext(
                subject: "❌ Build ${BUILD_NUMBER} - FALHOU",
                body: """
                    Pipeline falhou!
                    
                    Build: ${BUILD_NUMBER}
                    Motivo: Verifique os logs
                    
                    Veja os detalhes: ${BUILD_URL}
                """,
                to: 'equipe@example.com'
            )
        }
    }
}