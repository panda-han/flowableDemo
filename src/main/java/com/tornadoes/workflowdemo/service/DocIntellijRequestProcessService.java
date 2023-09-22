package com.tornadoes.workflowdemo.service;

import com.tornadoes.workflowdemo.dto.ConfirmResponse;
import com.tornadoes.workflowdemo.dto.DocIntellijRequest;

public interface DocIntellijRequestProcessService {

    void docIntellijRequest(DocIntellijRequest docIntellijRequest);

    void endTask(ConfirmResponse confirmResponse);
}
