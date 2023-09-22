package com.tornadoes.workflowdemo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmResponse {
    Boolean isApproved;
    String taskId;
    String destination;

}
