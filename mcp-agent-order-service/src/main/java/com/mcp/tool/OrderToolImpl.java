package com.mcp.tool;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.mcp.order.OrderService;

@Component
public class OrderToolImpl implements OrderTool {

	private final OrderService orderService;

	public OrderToolImpl(OrderService orderService) {
		this.orderService = orderService;
	}

	@Override
	public Object execute(Map<String, Object> arguments) {
		String orderId = (String) arguments.get("orderId");
		String status = orderService.getOrderStatus(orderId);
		return Map.of("orderId", orderId, "status", status);
	}

}
