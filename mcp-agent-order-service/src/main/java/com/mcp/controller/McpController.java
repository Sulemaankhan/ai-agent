package com.mcp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mcp.model.tool.ToolRequest;
import com.mcp.tool.OrderTool;

@RestController
public class McpController {

	private final OrderTool orderTool;

	public McpController(OrderTool orderTool) {
		this.orderTool = orderTool;
	}

	// List available tools (AI reads this)
	@GetMapping("/api/v2/tools")
	public List<Map<String, Object>> getTools() {

		return List.of(Map.of("name", "get_order_status", "description", "Get order status by order ID", "input_schema",
				Map.of("type", "object", "properties", Map.of("orderId", Map.of("type", "string")), "required",
						List.of("orderId"))));
	}

	// Tool execution end-point
	@GetMapping(value = "/api/v1/calls")
	public Object callTool(@RequestBody ToolRequest toolRequest) {

		if ("get_order_status".equals(toolRequest.getName())) {
			return orderTool.execute(toolRequest.getArguments());
		}
		throw new RuntimeException("Unknown tool" + toolRequest.getName());

	}

}
