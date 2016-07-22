package it.jaspic.sec;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.config.ServerAuthContext;
import javax.security.auth.message.module.ServerAuthModule;

public class TokenAuthContext implements ServerAuthContext {

	private ServerAuthModule tokenAuthModule;

	public TokenAuthContext(CallbackHandler handler) throws AuthException {
		tokenAuthModule = new TokenSAM();
		tokenAuthModule.initialize(/* MessagePolicy requestPolicy */null,
				/* MessagePolicy responsePolicy */null,
				/* CallbackHandler handler */handler, /* Map options */null);
	}

	/**
	 * This method is invoked for each request. Duplicate of SAM
	 * validateRequest?
	 */
	@Override
	public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject)
			throws AuthException {
		// if (messageInfo.getMap().containsKey(TokenSAM.IS_MANDATORY)
		// && Boolean.parseBoolean((String)
		// messageInfo.getMap().get(TokenSAM.IS_MANDATORY))) {
		return tokenAuthModule.validateRequest(messageInfo, clientSubject, serviceSubject);
		// } else {
		// return null;
		// }
	}

	@Override
	public AuthStatus secureResponse(MessageInfo messageInfo, Subject serviceSubject) throws AuthException {
		return tokenAuthModule.secureResponse(messageInfo, serviceSubject);
	}

	@Override
	public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {
		tokenAuthModule.cleanSubject(messageInfo, subject);
	}
}
