package com.mcp.order;

import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

	@Override
	public String getOrderStatus(String orderId) {

		if ("123".equals(orderId)) {
			return "shipped";
		}
		return "Not found";
	}

}
