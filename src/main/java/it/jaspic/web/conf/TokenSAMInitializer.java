package it.jaspic.web.conf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.module.ServerAuthModule;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.jboss.sasl.callback.TokenCallback;

import it.jaspic.sec.TokenConfigProvider;
import it.jaspic.sec.TokenSAM;

@WebListener
public class TokenSAMInitializer implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			CallbackHandler callbackHandler = new CallbackHandler() {
				@Override
				public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
					for (Callback callback : callbacks) {
						if (callback instanceof TokenCallback)
							return;
					}
					throw new UnsupportedCallbackException(callbacks[0]);
				}
			};
			TokenSAM tokenAuthModule = new TokenSAM();
			tokenAuthModule.initialize(null, null, callbackHandler, null);
			registerSAM(sce.getServletContext(), tokenAuthModule);
		} catch (AuthException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

	public static void registerSAM(ServletContext context, ServerAuthModule serverAuthModule) throws AuthException {
		AuthConfigFactory authConfFactory = AuthConfigFactory.getFactory();
		Map<String, String> properties = new HashMap<String, String>();
		/**
		 * HttpServlet mandatory value to use JASPIC with Servlet.
		 */
		authConfFactory.registerConfigProvider(new TokenConfigProvider(properties, authConfFactory), "HttpServlet",
				getAppContextID(context), "Default single SAM authentication config provider");
	}

	public static String getAppContextID(ServletContext context) {
		return context.getVirtualServerName() + " " + context.getContextPath();
	}
}
