package com.tornadoes.workflowdemo.service;

import org.flowable.task.api.Task;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OneTaskProcessService {

    @Transactional
    void startProcess();

    @Transactional
    List<Task> getTasks(String assignee);
}
