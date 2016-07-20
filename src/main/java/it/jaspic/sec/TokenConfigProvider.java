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
	 * Non so che farmene per ora.
	 */
	private ServerAuthConfig serverAuthConfig;

	/**
	 * Le API indicano come obbligatorio fornire un costruttore con questa
	 * firma. Registro questo provider presso l'oggetto AuthConfigFactory
	 * ricevuto in input.
	 * 
	 * @throws AuthException
	 */
	public TokenConfigProvider(Map<String, String> properties, AuthConfigFactory factory) throws AuthException {
		factory.registerConfigProvider(this, /* String layer */null,
				/* String appContext */null, /* String description */null);
		serverAuthConfig = new TokenServerConfig();
	}

	/**
	 * Devo mettere la logica di business per recuperare il corretto oggetto
	 * ServerAuthContex da utilizzare. Ne ho solo uno...
	 */
	public ServerAuthConfig getServerAuthConfig(String layer, String appContext, CallbackHandler handler)
			throws AuthException {
		return serverAuthConfig;
	}

	/**
	 * Reinizializzare i factory da restituire (credo). Viene mai invocato?
	 */
	public void refresh() {
		// TODO Auto-generated method stub

	}

	public ClientAuthConfig getClientAuthConfig(String layer, String appContext, CallbackHandler handler)
			throws AuthException {
		return null;
	}
}
