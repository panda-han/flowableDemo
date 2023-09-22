package com.tornadoes.workflowdemo.controller;

import com.tornadoes.workflowdemo.service.OneTaskProcessService;
import org.flowable.task.api.Task;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OneTaskProcessController {

    @Resource
    private OneTaskProcessService oneTaskProcessService;

    @PostMapping("/process")
    public void startProcessInstance() {
        oneTaskProcessService.startProcess();
    }

    @GetMapping(value = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TaskRepresentation> getTasks(@RequestParam String assignee){
        List<Task> tasks = oneTaskProcessService.getTasks(assignee);
        return tasks.stream().map(task -> new TaskRepresentation(task.getId(), task.getName()))
                .collect(Collectors.toList());
    }

    static class TaskRepresentation {

        private String id;
        private String name;

        public TaskRepresentation(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }
}
