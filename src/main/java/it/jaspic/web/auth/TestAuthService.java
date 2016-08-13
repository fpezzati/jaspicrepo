package it.jaspic.web.auth;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import it.jaspic.sec.model.PasswordPrincipal;
import it.jaspic.sec.model.RolePrincipal;
import it.jaspic.sec.model.UserPrincipal;
import it.jaspic.web.model.LoginRequest;

@Stateless
public class TestAuthService implements AuthService {

	private Logger log = Logger.getLogger(this.getClass());
	private Map<String, UserPrincipal> users;
	private Map<UserPrincipal, PasswordPrincipal> passwords;
	private Map<UserPrincipal, RolePrincipal[]> roles;

	public TestAuthService() {
		log.info(getClass().getName() + " initialized.");
		UserPrincipal userA = new UserPrincipal("userA");
		PasswordPrincipal pwdA = new PasswordPrincipal("Aresu");
		users = new HashMap<>();
		users.put(userA.getName(), userA);
		passwords = new HashMap<>();
		passwords.put(userA, pwdA);
		RolePrincipal roleA = new RolePrincipal("userA");
		roles = new HashMap<>();
		roles.put(userA, new RolePrincipal[] { roleA });
	}

	@Override
	public UserPrincipal getUser(LoginRequest loginRequest) {
		if (users.containsKey(loginRequest.getUsername())
				&& passwords.containsKey(users.get(loginRequest.getUsername())) && (passwords
						.get(users.get(loginRequest.getUsername())).getName().equals(loginRequest.getPassword()))) {
			return users.get(loginRequest.getUsername());
		}
		return null;
	}

	@Override
	public List<RolePrincipal> getRoles(UserPrincipal userPrincipal) {
		if (users.containsKey(userPrincipal.getName()) && roles.containsKey(users.get(userPrincipal.getName()))) {
			return Arrays.asList(roles.get(users.get(userPrincipal.getName())));
		}
		return null;
	}
}
