import org.springframework.cloud.dataflow.*
import org.springframework.web.client.RestTemplate

def template = new RestTemplate()
DataFlowTemplate restTemplate = new DataFlowTemplate("localhost:9393");

/*
** Scripting the DSL:
* app register --name jdbc-gemfire-task
* --type task
* --uri file:///Users/wlund/Dropbox/git-workspace/wxlund/jdbc-gemfire-samples/jdbc-gemfire-task/target/jdbc-gemfire-task-1.0.jar
* --force
 */

restTemplate.appRegistryOperations.register("jdbc-gemfire-task", ApplicationType.task,
        "file:///Users/wlund/Dropbox/git-workspace/wxlund/jdbc-gemfire-samples/jdbc-gemfire-task/target/jdbc-gemfire-task-1.0.jar",
        TRUE)
