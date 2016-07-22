package it.jaspic.sec;

import java.util.Map;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.config.ClientAuthConfig;
import javax.security.auth.message.config.ServerAuthConfig;

/**
 * Factory per recuperare un'istanza di ServerAuthConfig e operare le
 * validazioni.
 * 
 * @author Francesco
 *
 */
public class TokenConfigProvider implements AuthConfigProvider {

	public static final String MESSAGELAYER = "HttpServlet";
	/**
	 * I don't known what to do with this.
	 */
	private ServerAuthConfig serverAuthConfig;

	/**
	 * This constructor is mandatory.
	 * 
	 * @throws AuthException
	 */
	public TokenConfigProvider(Map<String, String> properties, AuthConfigFactory factory) throws AuthException {
		factory.registerConfigProvider(this, /* String layer */null,
				/* String appContext */null, /* String description */null);
		serverAuthConfig = new TokenServerConfig();
	}

	/**
	 * Here goes how to retreive the right ServerAuthConfig. I got only one...
	 */
	public ServerAuthConfig getServerAuthConfig(String layer, String appContext, CallbackHandler handler)
			throws AuthException {
		return serverAuthConfig;
	}

	/**
	 * Reinitialize the factories. Is it ever invoked?
	 */
	public void refresh() {

	}

	public ClientAuthConfig getClientAuthConfig(String layer, String appContext, CallbackHandler handler)
			throws AuthException {
		return null;
	}
}
