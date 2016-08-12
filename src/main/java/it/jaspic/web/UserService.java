package it.jaspic.web;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.security.auth.Subject;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.config.ServerAuthConfig;
import javax.servlet.ServletContext;
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

import it.jaspic.sec.LoginMessage;
import it.jaspic.sec.TokenConfigProvider;
import it.jaspic.sec.TokenSAM;
import it.jaspic.sec.model.UserPrincipal;
import it.jaspic.web.conf.TokenSAMInitializer;
import it.jaspic.web.model.LoginRequest;

@Path("/user")
@Stateless
public class UserService {

	private Logger log = LoggerFactory.getLogger(UserService.class);

	@Inject
	private ServletContext servletContext;

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response login(@Context HttpServletRequest httpRequest, LoginRequest loginRequest) {
		try {
			log.info("Login. " + loginRequest);

			AuthConfigProvider authConfigProvider = AuthConfigFactory.getFactory().getConfigProvider(
					TokenConfigProvider.MESSAGELAYER, TokenSAMInitializer.getAppContextID(servletContext), null);
			ServerAuthConfig serverAuthConfig = authConfigProvider.getServerAuthConfig(TokenConfigProvider.MESSAGELAYER,
					TokenSAMInitializer.getAppContextID(servletContext), null);

			LoginMessage loginMessage = new LoginMessage();
			loginMessage.setUsername(loginRequest.getUsername());
			loginMessage.setPassword(loginRequest.getPassword());

			Subject subject = new Subject();
			subject.getPrincipals().add(new UserPrincipal(loginRequest.getUsername()));
			AuthStatus authStatus = serverAuthConfig.getAuthContext(TokenSAM.CONTEXTID, subject, null)
					.validateRequest(loginMessage, subject, null);
			if (httpRequest.getUserPrincipal() != null) {
				return Response.status(Status.OK).entity(httpRequest.getUserPrincipal().getName() + " logged in.")
						.type(MediaType.APPLICATION_JSON_TYPE).build();
			} else {
				return Response.status(Status.NOT_IMPLEMENTED).entity("user not logged.")
						.type(MediaType.APPLICATION_JSON_TYPE).build();
			}
		} catch (AuthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).type(MediaType.APPLICATION_JSON_TYPE)
					.build();
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
