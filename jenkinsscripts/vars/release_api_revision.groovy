#!/usr/bin/groovy

def call (config = [:]){

   config = config as ReleaseApiRevisionConfig
   AzureRegistryUtils azureRegistryUtils = new AzureRegistryUtils()
   PowershellUtils powershellUtils = new PowershellUtils()

   azureRegistryUtils.withAzureRegistry {
      docker.image('topaasAPIM.azurecr.io/apimanagementimage').inside('--entrypoint ""') {
         powershellUtils.connectAzure(config.servicePrincipal, config.subscriptionId)
         powershellUtils.runPowershellCommand """
            \$ApiMgmtContext = New-AzApiManagementContext -Resourcegroupname ${config.apiManagementRG} -ServiceName ${config.apiManagementName}
            New-AzApiManagementApiRelease -Context \$ApiMgmtcontext -ApiId "${config.apiId}" -ApiRevision ${config.apiRevision} -Note "${config.releaseNote}"
         """
      }
   }
}   