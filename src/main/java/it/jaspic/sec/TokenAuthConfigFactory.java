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
		 * Non so che uso potrei fare delle properties.
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String registerConfigProvider(AuthConfigProvider provider, String layer, String appContext,
			String description) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeRegistration(String registrationID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String[] detachListener(RegistrationListener listener, String layer, String appContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getRegistrationIDs(AuthConfigProvider provider) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RegistrationContext getRegistrationContext(String registrationID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

}
