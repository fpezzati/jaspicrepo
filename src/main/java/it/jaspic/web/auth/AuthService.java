package it.jaspic.web.auth;

import java.util.List;

import it.jaspic.sec.model.RolePrincipal;
import it.jaspic.sec.model.UserPrincipal;
import it.jaspic.web.model.LoginRequest;

public interface AuthService {

	public UserPrincipal getUser(LoginRequest loginRequest);

	public List<RolePrincipal> getRoles(UserPrincipal userPrincipal);
}
