package it.jaspic.sec;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.message.MessageInfo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is my MessageInfo.
 * 
 * @author Francesco
 *
 */
class TokenMessage implements MessageInfo {

	public static final String TOKENATTRIBUTE = "token";
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HashMap<String, String> properties;
	private static final String authContextKey = "authContextID";

	public TokenMessage() {
		properties = new HashMap<>();
		setAuthContextID(TokenSAM.CONTEXTID);
	}

	@Override
	public Object getRequestMessage() {
		return request;
	}

	@Override
	public Object getResponseMessage() {
		return response;
	}

	@Override
	public void setRequestMessage(Object request) {
		if (request instanceof HttpServletRequest) {
			this.request = (HttpServletRequest) request;
		}
	}

	@Override
	public void setResponseMessage(Object response) {
		if (response instanceof HttpServletResponse) {
			this.response = (HttpServletResponse) response;
		}
	}

	@Override
	public Map<String, String> getMap() {
		return properties;
	}

	public String getAuthContextID() {
		return properties.get(authContextKey);
	}

	public void setAuthContextID(String authContextID) {
		properties.put(authContextKey, authContextID);
	}
}