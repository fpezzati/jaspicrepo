package it.jaspic.sec;

import java.io.IOException;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.callback.CallerPrincipalCallback;
import javax.security.auth.message.module.ServerAuthModule;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import it.jaspic.sec.model.UserPrincipal;

public class TokenSAM implements ServerAuthModule {

	public static final String CONTEXTID = "jaspic.jwt";

	private CallbackHandler callbackHandler;
	/**
	 * I tipi di messaggio supportati da questo SAM. Voglio che gestisca le
	 * chiamate HTTP.
	 */
	@SuppressWarnings("rawtypes")
	private Class[] supportedMessageType = { ServletRequest.class, ServletResponse.class };
	private Logger log = Logger.getLogger(this.getClass());
	private final String IS_MANDATORY = "javax.security.auth.message.MessagePolicy.isMandatory";

	/**
	 * Richiesto come obbligatorio dalle API.
	 */
	public TokenSAM() {
	}

	/**
	 * Questo metodo deve incapsulare la logica di verifica delle credenziali
	 * utente. Il metodo validateRequest viene invocato per ogni chiamata
	 * rivolta al contesto web del SAM. Quando viene richiesta una risorsa che
	 * non Ë esplicitamente indicata come protetta, il valore dell'attributo
	 * "javax.security.auth.message.MessagePolicy.isMandatory" di MessageInfo Ë
	 * pari a "false". Invece, quando viene richiesta una risorsa protetta, il
	 * valore dell'attributo
	 * "javax.security.auth.message.MessagePolicy.isMandatory" di MessageInfo Ë
	 * pari a "true".
	 */
	@Override
	public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject)
			throws AuthException {
		log.info("validating..");
		Callback[] callbacks = null;
		if (messageInfo.getMap().containsKey(IS_MANDATORY)
				&& !(Boolean.parseBoolean((String) messageInfo.getMap().get(IS_MANDATORY)))) {
			log.info("request a unprotected resource.");
			callbacks = new Callback[] { new CallerPrincipalCallback(clientSubject, new UserPrincipal("MARIO")) };
			return AuthStatus.SEND_SUCCESS;
		} else {
			TokenMessage tokenMessage = (TokenMessage) messageInfo;
			HttpServletRequest httpReq = (HttpServletRequest) tokenMessage.getRequestMessage();
			log.info("username: " + httpReq.getParameter("username"));
		}
		try {
			// Communicate the details of the authenticated user to the
			// container. In many
			// cases the handler will just store the details and the container
			// will actually handle
			// the login after we return from this method.
			callbackHandler.handle(callbacks);
		} catch (IOException | UnsupportedCallbackException e) {
			throw (AuthException) new AuthException().initCause(e);
		}
		return AuthStatus.SUCCESS;
	}

	/**
	 * Non √® chiaro a cosa serva questo metodo. Diversi AS ne fanno un diverso
	 * uso.
	 */
	@Override
	public AuthStatus secureResponse(MessageInfo messageInfo, Subject serviceSubject) throws AuthException {
		return AuthStatus.SEND_SUCCESS;
	}

	@Override
	public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {
		// TODO Auto-generated method stub

	}

	/**
	 * Il parametro CallbackHandler serve per gestire le richieste. Analogo a
	 * JAAS (?). Salvo l'oggetto in un attributo in quanto verr√† usato in
	 * secureResponse.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void initialize(MessagePolicy requestPolicy, MessagePolicy responsePolicy, CallbackHandler handler,
			Map options) throws AuthException {
		setCallbackHandler(handler);
	}

	/**
	 * Restituisce i tipi di messaggio supportati.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Class[] getSupportedMessageTypes() {
		return supportedMessageType;
	}

	public CallbackHandler getCallbackHandler() {
		return callbackHandler;
	}

	public void setCallbackHandler(CallbackHandler callbackHandler) {
		this.callbackHandler = callbackHandler;
	}

}
