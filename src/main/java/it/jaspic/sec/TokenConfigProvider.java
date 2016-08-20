package it.jaspic.sec;

import java.util.Map;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.config.ClientAuthConfig;
import javax.security.auth.message.config.ServerAuthConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory per recuperare un'istanza di ServerAuthConfig e operare le
 * validazioni.
 * 
 * @author Francesco
 *
 */
public class TokenConfigProvider implements AuthConfigProvider {

	public static final String MESSAGELAYER = "HttpServlet";
	private Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * This constructor is mandatory.
	 * 
	 * @throws AuthException
	 */
	public TokenConfigProvider(Map<String, String> properties, AuthConfigFactory factory) throws AuthException {
		factory.registerConfigProvider(this, /* String layer */null,
				/* String appContext */null, /* String description */null);
	}

	/**
	 * Here goes how to retreive the right ServerAuthConfig. I got only one...
	 */
	public ServerAuthConfig getServerAuthConfig(String layer, String appContext, CallbackHandler handler)
			throws AuthException {
		log.info("getting a ServerAuthConfig instance.");
		return new TokenServerConfig(handler);
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
