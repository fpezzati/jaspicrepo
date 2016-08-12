package it.jaspic.sec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.config.ServerAuthConfig;
import javax.security.auth.message.config.ServerAuthContext;

import org.apache.log4j.Logger;

public class TokenServerConfig implements ServerAuthConfig {

	private CallbackHandler handler;
	private Map<String, ServerAuthContext> context;
	private final String tokenKey = "tokenContext";
	private final Logger log = Logger.getLogger(this.getClass());

	public TokenServerConfig() throws AuthException {
		context = new HashMap<>();
		handler = new CallbackHandler() {
			@Override
			public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
				log.info("CallbackHandler is handling:");
				for (Callback callback : callbacks) {
					log.info("Callback: " + callback.toString());
				}
			}
		};
		context.put(tokenKey, new TokenAuthContext(handler));
	}

	/**
	 * I don't really get what a messageLayer is. I can return null because I
	 * don't use more than one messageLayer.
	 */
	@Override
	public String getMessageLayer() {
		return null;
	}

	/**
	 * Should this method return an Id to get the right ServerAuthConfig?
	 */
	@Override
	public String getAppContext() {
		return TokenSAM.CONTEXTID;
	}

	/**
	 * What should this method do? Get the right SAM by messageInfo contextID?
	 * What if I return null? Trajano says no authentication will be verified.
	 */
	@Override
	public String getAuthContextID(MessageInfo messageInfo) {
		if (messageInfo.getMap().containsKey(TokenSAM.IS_MANDATORY)
				&& Boolean.parseBoolean((String) messageInfo.getMap().get(TokenSAM.IS_MANDATORY))
				&& messageInfo instanceof TokenMessage) {
			return ((TokenMessage) messageInfo).getAuthContextID();
		}
		return null;
	}

	public String getAuthContextID(TokenMessage messageInfo) {
		return messageInfo.getAuthContextID();
	}

	/**
	 * I don't get purpose of this method.
	 */
	@Override
	public void refresh() {

	}

	@Override
	public boolean isProtected() {
		return false;
	}

	/**
	 * @param authContextID
	 *            identificativo che posso associare ad un ServerAuthContext (?)
	 *            Mi arriva sempre nullo per ora. Devo capire il motivo.
	 * @param serviceSubject
	 *            ???
	 * @param properties
	 *            ???
	 */
	@Override
	public ServerAuthContext getAuthContext(String authContextID, Subject serviceSubject,
			@SuppressWarnings("rawtypes") Map properties) throws AuthException {
		// if (!context.containsKey(authContextID))
		// throw new AuthException("No context found.");
		// return context.get(authContextID);
		/**
		 * Non capisco come faccio a passare authContextID quando faccio una
		 * chiamata http ad uno degli endpoint rest.
		 */
		return new TokenAuthContext(handler);
	}

}
