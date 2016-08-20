package it.jaspic.web.conf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.jaspic.sec.TokenConfigProvider;
import it.jaspic.sec.TokenSAM;

@WebListener
public class TokenSAMInitializer implements ServletContextListener {

	@Inject
	private TokenSAM tokenAuthModule;

	public static final String RUNTIMECALLBACKHANDLER = "authconfigprovider.client.callbackhandler";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			// CallbackHandler callbackHandler = (CallbackHandler)
			// getClass().getClassLoader()
			// .loadClass(System.getProperty(RUNTIMECALLBACKHANDLER)).newInstance();
			CallbackHandler callbackHandler = new CallbackHandler() {
				Logger log = LoggerFactory.getLogger(getClass());

				@Override
				public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
					log.info("Handling callbacks.");
				}
			};
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
