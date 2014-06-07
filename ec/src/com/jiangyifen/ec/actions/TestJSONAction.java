package com.jiangyifen.ec.actions;

import org.json.JSONObject;

public class TestJSONAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2156813812150290239L;

	private String jsonString;
	private String p1;

	public String execute() throws Exception {

		
		Long[] ar = {System.currentTimeMillis(),(long)(Math.random()*100)};
		
		JSONObject json = new JSONObject();
		json.put("array",ar);
		json.put("p1", p1);

		
		jsonString = json.toString();
		
		return SUCCESS;

	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setP1(String p1) {
		this.p1 = p1;
	}

	public String getP1() {
		return p1;
	}



}

