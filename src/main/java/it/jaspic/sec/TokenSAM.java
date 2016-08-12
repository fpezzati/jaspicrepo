package it.jaspic.sec;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

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

public class TokenSAM implements ServerAuthModule {

	public static final String CONTEXTID = "jaspic.jwt";
	private CallbackHandler callbackHandler;
	/**
	 * I tipi di messaggio supportati da questo SAM. Voglio che gestisca le
	 * chiamate HTTP.
	 */
	@SuppressWarnings("rawtypes")
	private Class[] supportedMessageType = { ServletRequest.class, ServletResponse.class };
	private Logger log = Logger.getLogger(this.getClass());
	public static final String IS_MANDATORY = "javax.security.auth.message.MessagePolicy.isMandatory";

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
		Callback[] callbacks = null;
		try {
			if (messageInfo.getMap().containsKey(IS_MANDATORY)
					&& !(Boolean.parseBoolean((String) messageInfo.getMap().get(IS_MANDATORY)))) {
				Principal p = getAnonymousPrincipal(messageInfo);
				callbackHandler.handle(new Callback[] { new CallerPrincipalCallback(clientSubject, p) });
			} else {
				if (messageInfo instanceof LoginMessage) {
					Principal p = getRegisteredUser((LoginMessage) messageInfo);
					String[] roles = getPrincipalRoles(p);
					callbackHandler.handle(new Callback[] { new CallerPrincipalCallback(clientSubject, p),
							new GroupPrincipalCallback(clientSubject, roles) });
				} else {
					TokenMessage tokenMessage = (TokenMessage) messageInfo;
					HttpServletRequest httpReq = (HttpServletRequest) tokenMessage.getRequestMessage();
					log.info("username: " + httpReq.getParameter("username"));
					callbackHandler.handle(callbacks);
				}
			}
		} catch (IOException | UnsupportedCallbackException e) {
			throw (AuthException) new AuthException().initCause(e);
		}
		return AuthStatus.SUCCESS;
	}

	private UserPrincipal getAnonymousPrincipal(MessageInfo message) {
		log.info("request a unprotected resource.");
		return new UserPrincipal(null);
	}

	private UserPrincipal getRegisteredUser(LoginMessage loginMessage) throws AuthException {
		if ("userA".equals(loginMessage.getUsername()) && "Aresu".equals(loginMessage.getPassword())) {
			return new UserPrincipal("userA");
		}
		throw new AuthException("No user found!");
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
