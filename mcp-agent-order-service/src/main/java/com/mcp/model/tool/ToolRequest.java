package com.mcp.model.tool;

import java.io.Serializable;
import java.util.Map;

public class ToolRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private Map<String, Object> arguments;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, Object> getArguments() {
		return arguments;
	}
	public void setArguments(Map<String, Object> arguments) {
		this.arguments = arguments;
	}	

}
