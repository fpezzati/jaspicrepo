package it.jaspic.sec;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.message.MessageInfo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginMessage implements MessageInfo {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private HashMap<String, String> properties;

	private final static String USERNAME = "username";
	private final static String PASSWORD = "password";

	public LoginMessage() {
		properties = new HashMap<>();
	}

	@Override
	public Object getRequestMessage() {
		return request;
	}

	@Override
	public Object getResponseMessage() {
		return response;
	}

	@Override
	public void setRequestMessage(Object request) {
		if (request instanceof HttpServletRequest)
			this.request = (HttpServletRequest) request;
	}

	@Override
	public void setResponseMessage(Object response) {
		if (response instanceof HttpServletResponse)
			this.response = (HttpServletResponse) response;

	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map getMap() {
		return properties;
	}

	public String getUsername() {
		return properties.get(USERNAME);
	}

	public void setUsername(String username) {
		properties.put(USERNAME, username);
	}

	public String getPassword() {
		return properties.get(PASSWORD);
	}

	public void setPassword(String password) {
		properties.put(PASSWORD, password);
	}
}
