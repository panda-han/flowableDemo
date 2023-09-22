package com.tornadoes.workflowdemo.controller;

import com.tornadoes.workflowdemo.dto.ConfirmResponse;
import com.tornadoes.workflowdemo.dto.DocIntellijRequest;
import com.tornadoes.workflowdemo.service.DocIntellijRequestProcessService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DocIntellijProcessController {

    @Resource
    private DocIntellijRequestProcessService docIntellijRequestProcessService;

    @PostMapping("/DocIntellijRequest")
    public void docIntellijRequest(@RequestBody DocIntellijRequest docIntellijRequest) {
        docIntellijRequestProcessService.docIntellijRequest(docIntellijRequest);
    }
    @PostMapping("/Confirm")
    public void response(@RequestBody ConfirmResponse confirmResponse) {
        docIntellijRequestProcessService.endTask(confirmResponse);
    }
}
