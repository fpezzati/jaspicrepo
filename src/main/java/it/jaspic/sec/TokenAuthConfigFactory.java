package it.jaspic.sec;

import java.util.Map;

import javax.security.auth.message.AuthException;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.config.RegistrationListener;

public class TokenAuthConfigFactory extends AuthConfigFactory {

	@Override
	public AuthConfigProvider getConfigProvider(String layer, String appContext, RegistrationListener listener) {
		/**
		 * No properties for now.
		 */
		try {
			return new TokenConfigProvider(null, this);
		} catch (AuthException e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String registerConfigProvider(String className, Map properties, String layer, String appContext,
			String description) {
		return null;
	}

	@Override
	public String registerConfigProvider(AuthConfigProvider provider, String layer, String appContext,
			String description) {
		return null;
	}

	@Override
	public boolean removeRegistration(String registrationID) {
		return false;
	}

	@Override
	public String[] detachListener(RegistrationListener listener, String layer, String appContext) {
		return null;
	}

	@Override
	public String[] getRegistrationIDs(AuthConfigProvider provider) {
		return null;
	}

	@Override
	public RegistrationContext getRegistrationContext(String registrationID) {
		return null;
	}

	@Override
	public void refresh() {

	}
}
