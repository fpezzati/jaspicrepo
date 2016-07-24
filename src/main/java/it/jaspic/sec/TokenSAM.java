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
import javax.security.auth.message.module.ServerAuthModule;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

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
				log.info("request a unprotected resource.");
				Principal p = new Principal() {
					@Override
					public String getName() {
						return null;
					}
				};
				callbackHandler.handle(new Callback[] { new CallerPrincipalCallback(clientSubject, p) });
			} else {
				TokenMessage tokenMessage = (TokenMessage) messageInfo;
				HttpServletRequest httpReq = (HttpServletRequest) tokenMessage.getRequestMessage();
				log.info("username: " + httpReq.getParameter("username"));
				callbackHandler.handle(callbacks);
			}
		} catch (IOException | UnsupportedCallbackException e) {
			throw (AuthException) new AuthException().initCause(e);
		}
		return AuthStatus.SUCCESS;
	}

	/**
	 * Non è chiaro a cosa serva questo metodo. Diversi AS ne fanno un diverso
	 * uso.
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
