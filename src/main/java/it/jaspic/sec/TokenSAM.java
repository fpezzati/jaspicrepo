package it.jaspic.sec;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

import javax.inject.Inject;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.callback.CallerPrincipalCallback;
import javax.security.auth.message.callback.GroupPrincipalCallback;
import javax.security.auth.message.module.ServerAuthModule;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import it.jaspic.sec.model.UserPrincipal;
import it.jaspic.web.auth.AuthService;
import it.jaspic.web.auth.JWTTokenProvider;

public class TokenSAM implements ServerAuthModule {

	/**
	 * SAM's supported messages. It will handle HTTP calls.
	 */
	@SuppressWarnings("rawtypes")
	private Class[] supportedMessageType = { ServletRequest.class, ServletResponse.class };
	private CallbackHandler callbackHandler;
	private Logger log = Logger.getLogger(this.getClass());

	@Inject
	private AuthService authService;
	@Inject
	private JWTTokenProvider jWTTokenProvider;

	public static final String CONTEXTID = "jaspic.jwt";
	public static final String IS_MANDATORY = "javax.security.auth.message.MessagePolicy.isMandatory";
	public static final String IS_LOGIN = "login";

	/**
	 * This constructor is mandatory.
	 */
	public TokenSAM() {
	}

	/**
	 * Here is where requests are validated. When request is about an
	 * unprotected resource, MessageInfo property
	 * "javax.security.auth.message.MessagePolicy.isMandatory" is set to
	 * "false". When request is about a protected resource, the property is set
	 * to "true".
	 */
	@Override
	public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject)
			throws AuthException {
		log.info("validating..");
		try {
			if (isUnprotectedResourceRequest(messageInfo)) {
				Principal p = getAnonymousPrincipal(messageInfo);
				callbackHandler.handle(new Callback[] { new CallerPrincipalCallback(clientSubject, p) });
			} else {

				// TokenMessage tokenMessage = (TokenMessage) messageInfo;

				Principal p = getRegisteredUser(messageInfo);
				String[] roles = getPrincipalRoles(p);
				callbackHandler.handle(new Callback[] { new CallerPrincipalCallback(clientSubject, p),
						new GroupPrincipalCallback(clientSubject, roles) });
				log.info("username: " + p.getName());
			}
		} catch (IOException | UnsupportedCallbackException e) {
			throw (AuthException) new AuthException().initCause(e);
		}
		return AuthStatus.SUCCESS;
	}

	private boolean isUnprotectedResourceRequest(MessageInfo messageInfo) {
		return messageInfo.getMap().containsKey(IS_MANDATORY)
				&& !(Boolean.parseBoolean((String) messageInfo.getMap().get(IS_MANDATORY)));
	}

	private UserPrincipal getAnonymousPrincipal(MessageInfo message) {
		log.info("request a unprotected resource.");
		return new UserPrincipal(null);
	}

	private UserPrincipal getRegisteredUser(MessageInfo message) throws AuthException {
		String token = ((HttpServletRequest) message.getRequestMessage()).getHeader("Bearer");
		jWTTokenProvider.parseToken(token);
		// TODO retreive the user claim and check if it's registered.
		return new UserPrincipal("userA");
	}

	private String[] getPrincipalRoles(Principal p) throws AuthException {
		if ("userA".equals(p.getName())) {
			return new String[] { "userA" };
		}
		throw new AuthException("No roles found!");
	}

	/**
	 * I don't known what to do with this method.
	 */
	@Override
	public AuthStatus secureResponse(MessageInfo messageInfo, Subject serviceSubject) throws AuthException {
		return AuthStatus.SEND_SUCCESS;
	}

	/**
	 * What is this? Maybe to logout a subject?
	 */
	@Override
	public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void initialize(MessagePolicy requestPolicy, MessagePolicy responsePolicy, CallbackHandler handler,
			Map options) throws AuthException {
		setCallbackHandler(handler);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class[] getSupportedMessageTypes() {
		return supportedMessageType;
	}

	public CallbackHandler getCallbackHandler() {
		return callbackHandler;
	}

	public void setCallbackHandler(CallbackHandler callbackHandler) {
		this.callbackHandler = callbackHandler;
	}

}
