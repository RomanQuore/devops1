def call(Map param){

    pipeline{

        agent any

        tools{
            nodejs 'NodeJS'
        }
          triggers {
        pollSCM('* * * * *') // Programa la verificación del repositorio cada minuto
    }
       environment{
           PROJECT = "${env.GIT_URL}".replaceAll('.+/(.+)\\.git', '$1')toLowerCase()
       } 
        stages{
            
      
             stage('Construccion App') {
                steps {
                    script {
                        def buildapp = new org.devops.lb_buildartefacto()
                        buildapp.install()
                        buildapp.testCoverage()
                        def cloneapp = new org.devops.lb_buildartefacto()
                        cloneapp.clone(scmUrl:params.scmUrl)
                    }
                }
                
            }

  stage('Sonar Analisis'){
                 steps{
                    script{
                       def analisiSonar = new org.devops.lb_analisissonarqube()
                       analisiSonar.analisisSonar("${PROJECT}")
                    }
                 }
           }

         
                   
        }
        
        }
}
