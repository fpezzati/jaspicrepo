package it.jaspic.web;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.jaspic.sec.model.UserPrincipal;
import it.jaspic.web.auth.AuthService;
import it.jaspic.web.auth.JWTTokenProvider;
import it.jaspic.web.model.LoginRequest;
import it.jaspic.web.model.LoginResponse;

@Path("/user")
@Stateless
@DeclareRoles({ "userA", "userB", "userC" })
public class UserService {

	private Logger log = LoggerFactory.getLogger(UserService.class);

	@Inject
	private AuthService authService;
	@Inject
	private JWTTokenProvider jWTTokenProvider;

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response login(@Context HttpServletRequest httpRequest, LoginRequest loginRequest) {
		log.info("Login. " + loginRequest);
		UserPrincipal user = authService.getUser(loginRequest);
		if (user != null) {
			LoginResponse loginResponse = new LoginResponse();
			loginResponse.setUsername(user.getName());
			loginResponse.setToken(jWTTokenProvider.getToken(user.getName()));
			return Response.status(Status.OK).entity(loginResponse).type(MediaType.APPLICATION_JSON_TYPE).build();
		} else {
			return Response.status(Status.NOT_IMPLEMENTED).entity("user not logged in.")
					.type(MediaType.APPLICATION_JSON_TYPE).build();
		}
	}

	@POST
	@Path("/logout")
	@PermitAll
	public Response logout() {
		return null;
	}

	@GET
	@Path("/userA")
	@RolesAllowed("userA")
	@Produces(MediaType.APPLICATION_JSON)
	public String postUserA() {
		log.info("userA invoked.");
		return "You're user A.";
	}

	@GET
	@Path("/userB")
	@RolesAllowed("userB")
	@Produces(MediaType.APPLICATION_JSON)
	public String postUserB() {
		log.info("userB invoked.");
		return "You're user B.";
	}

	@GET
	@Path("/userC")
	@RolesAllowed("userC")
	@Produces(MediaType.APPLICATION_JSON)
	public String postUserC() {
		log.info("userC invoked.");
		return "You're user C.";
	}
}
