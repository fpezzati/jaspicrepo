package it.jaspic.sec;

import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.config.ServerAuthConfig;
import javax.security.auth.message.config.ServerAuthContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenServerConfig implements ServerAuthConfig {

	private CallbackHandler handler;
	private Logger log = LoggerFactory.getLogger(getClass());

	public TokenServerConfig() throws AuthException {
	}

	public TokenServerConfig(CallbackHandler handler) {
		this.handler = handler;
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
		log.info("getAuthContextID. MessageInfo: " + messageInfo);
		if (messageInfo.getMap().containsKey(TokenSAM.IS_MANDATORY)
				&& Boolean.parseBoolean((String) messageInfo.getMap().get(TokenSAM.IS_MANDATORY))
				&& messageInfo instanceof TokenMessage) {
			return ((TokenMessage) messageInfo).getAuthContextID();
		}
		return null;
	}

	public String getAuthContextID(TokenMessage messageInfo) {
		log.info("getAuthContextID. TokenMessage: " + messageInfo);
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
	 * The runtime invoke this method to get a SAM instance.
	 * 
	 * @param authContextID
	 * @param serviceSubject
	 * @param properties
	 */
	@Override
	public ServerAuthContext getAuthContext(String authContextID, Subject serviceSubject,
			@SuppressWarnings("rawtypes") Map properties) throws AuthException {
		log.info("getAuthContext. authContextID: " + authContextID + ", serviceSubject: " + serviceSubject
				+ ", properties: " + properties);
		log.info("TokenAuthContext will be instantiated with this CallbackHandler: " + handler.getClass().getName());
		return new TokenAuthContext(handler);
	}
}
