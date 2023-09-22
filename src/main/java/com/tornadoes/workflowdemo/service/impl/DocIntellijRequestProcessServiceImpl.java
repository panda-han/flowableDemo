package com.tornadoes.workflowdemo.service.impl;

import com.tornadoes.workflowdemo.dto.ConfirmResponse;
import com.tornadoes.workflowdemo.dto.DocIntellijRequest;
import com.tornadoes.workflowdemo.service.DocIntellijRequestProcessService;
import com.tornadoes.workflowdemo.service.ai.FormRecognizer;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DocIntellijRequestProcessServiceImpl implements DocIntellijRequestProcessService {

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;

    @Resource
    private HistoryService historyService;

    @Override
    public void docIntellijRequest(DocIntellijRequest docIntellijRequest) {
        Map<String, Object> variables = new HashMap<>(8);
        variables.put("requester", docIntellijRequest.getRequester());
        variables.put("docLocation", docIntellijRequest.getDocLocation());
        variables.put("description", docIntellijRequest.getDescription());

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("DocIntellijRequest", variables);
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();
        System.out.println("You have " + tasks.size() + " tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ") " + tasks.get(i).getName());
        }
        if (!CollectionUtils.isEmpty(tasks)) {
            Task task = tasks.get(tasks.size() - 1);
            Map<String, Object> processVariables = taskService.getVariables(task.getId());
            System.out.println("Processing Doc for " + processVariables.get("requester") + ", power by AI service.");

//            print out as json message
            FormRecognizer formRecognizer = new FormRecognizer((String) processVariables.get("docLocation"));
            String outPut = formRecognizer.docToJson();
            System.out.println(outPut);

//            request confirmation
            System.out.println("Please confirm if it's fine to send message...");


            taskService.complete(task.getId(), variables);
        }

        List<HistoricActivityInstance> activities =
                historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(processInstance.getId())
                        .finished()
                        .orderByHistoricActivityInstanceEndTime().asc()
                        .list();

        for (HistoricActivityInstance activity : activities) {
            System.out.println(activity.getActivityId() + " took "
                    + activity.getDurationInMillis() + " milliseconds");
        }
    }

    @Override
    public void endTask(ConfirmResponse confirmResponse) {
        Map<String, Object> variables = new HashMap<>(8);

        variables.put("isApproved", confirmResponse.getIsApproved());
        variables.put("taskId", confirmResponse.getTaskId());
        variables.put("destination", confirmResponse.getDestination());

        System.out.println(confirmResponse.getIsApproved() ? "do approved" : "do rejected");
        variables = new HashMap<>();
        variables.put("approved", confirmResponse.getIsApproved());

        taskService.complete(confirmResponse.getTaskId(), variables);
    }


    @PostConstruct
    private void deploy() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/DocIntellij-request.bpmn20.xml")
                .deploy();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();

        System.out.println("Found process definition : " + processDefinition.getName());
    }
}
