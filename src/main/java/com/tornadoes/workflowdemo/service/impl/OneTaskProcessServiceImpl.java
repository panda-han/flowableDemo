package com.tornadoes.workflowdemo.service.impl;

import com.tornadoes.workflowdemo.service.OneTaskProcessService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OneTaskProcessServiceImpl implements OneTaskProcessService {

    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;


    @Override
    public void startProcess() {
        System.out.println("Number of tasks : " + taskService.createTaskQuery().count());
        runtimeService.createProcessInstanceBuilder()
                .processDefinitionKey("oneTaskProcess")
                .transientVariable("currentUser", "kermit")
                .start();

        TaskQuery taskQuery = taskService.createTaskQuery();
        System.out.println("Number of tasks after process start: "
                + taskQuery.count());
    }

    @Override
    public List<Task> getTasks(String assignee) {
        return taskService.createTaskQuery().taskAssignee(assignee).list();
    }
}
