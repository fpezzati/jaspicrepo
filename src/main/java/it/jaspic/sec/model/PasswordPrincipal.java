package it.jaspic.sec.model;

import java.security.Principal;

public class PasswordPrincipal implements Principal {

	private String password;

	public PasswordPrincipal(String password) {
		this.password = password;
	}

	@Override
	public String getName() {
		return password;
	}
}
