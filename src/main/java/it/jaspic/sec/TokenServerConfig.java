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
	 * Non ho ancora capito cosa sia un messageLayer. Posso ritornare null se
	 * non sono previsti layer specifici.
	 */
	@Override
	public String getMessageLayer() {
		return null;
	}

	/**
	 * Identificatore per associare un contesto applicativo (?) ad un oggetto
	 * ServerAuthConfig.
	 */
	@Override
	public String getAppContext() {
		return null;
	}

	@Override
	public String getAuthContextID(MessageInfo messageInfo) {
		if (messageInfo instanceof TokenMessage) {
			return ((TokenMessage) messageInfo).getAuthContextID();
		}
		return null;
	}

	public String getAuthContextID(TokenMessage messageInfo) {
		return messageInfo.getAuthContextID();
	}

	/**
	 * Non ho capito lo scopo di questo metodo.
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
