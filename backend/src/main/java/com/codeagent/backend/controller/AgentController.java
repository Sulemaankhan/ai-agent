package com.codeagent.backend.controller;

import org.springframework.web.bind.annotation.*;
import com.codeagent.backend.service.AutomationWorkflowService;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/agent")
@CrossOrigin(origins = "*")
public class AgentController {

    private final AutomationWorkflowService workflowService;

    public AgentController(AutomationWorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @PostMapping("/run")
    public Map<String, String> runAutomation(@RequestBody Map<String, String> payload) {
        String localPath = payload.get("localPath");

        workflowService.executeAgentRun(localPath);

        Map<String, String> response = new HashMap<>();
        response.put("status", "Started");
        response.put("message", "The MCP agent has started updating dependencies.");
        return response;
    }
}
