package it.jaspic.web.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LoginResponse implements Serializable {

	private String username;
	private String token;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
