package it.jaspic.web;

import javax.annotation.security.PermitAll;
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
public class UserService {

	private Logger log = LoggerFactory.getLogger(UserService.class);

	// @Inject
	// private ServletContext servletContext;

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
		// try {
		log.info("Login. " + loginRequest);

		// AuthConfigProvider authConfigProvider =
		// AuthConfigFactory.getFactory().getConfigProvider(
		// TokenConfigProvider.MESSAGELAYER,
		// TokenSAMInitializer.getAppContextID(servletContext), null);
		// ServerAuthConfig serverAuthConfig =
		// authConfigProvider.getServerAuthConfig(TokenConfigProvider.MESSAGELAYER,
		// TokenSAMInitializer.getAppContextID(servletContext), null);
		//
		// /**
		// * It does not work. I guess because the SAM is a HttpServlet one.
		// */
		// LoginMessage loginMessage = new LoginMessage();
		// loginMessage.setUsername(loginRequest.getUsername());
		// loginMessage.setPassword(loginRequest.getPassword());
		//
		// Subject subject = new Subject();
		// subject.getPrincipals().add(new
		// UserPrincipal(loginRequest.getUsername()));
		// AuthStatus authStatus =
		// serverAuthConfig.getAuthContext(TokenSAM.CONTEXTID, subject,
		// null)
		// .validateRequest(loginMessage, subject, null);

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
		// } catch (AuthException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// return
		// Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).type(MediaType.APPLICATION_JSON_TYPE)
		// .build();
		// }
	}

	@POST
	@Path("/logout")
	@PermitAll
	public Response logout() {
		return null;
	}

	@GET
	@Path("/userA")
	// @Secured(Roles.UserA)
	@Produces(MediaType.TEXT_HTML)
	public String postUserA() {
		log.info("userA invoked.");
		return "You're user A.";
	}

	@GET
	@Path("/userB")
	// @Secured(Roles.UserB)
	@Produces(MediaType.TEXT_HTML)
	public String postUserB() {
		log.info("userB invoked.");
		return "You're user B.";
	}

	@GET
	@Path("/userC")
	// @Secured(Roles.UserC)
	@Produces(MediaType.TEXT_HTML)
	public String postUserC() {
		log.info("userC invoked.");
		return "You're user C.";
	}
}
