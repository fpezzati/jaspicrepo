package it.jaspic.web.auth;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Stateless
public class JWTTokenProvider {

	private String secret = "someusefulsupersecret";

	public JWTTokenProvider() {
		if (System.getProperty("tokenSecret") != null) {
			secret = System.getProperty("tokenSecret");
		}
	}

	public String getToken(String subject) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		Date expire = calendar.getTime();
		return Jwts.builder().setExpiration(expire).setSubject(subject)
				.signWith(SignatureAlgorithm.HS256, DatatypeConverter.printBase64Binary(secret.getBytes())).compact();
	}

	public Map<String, Object> parseToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
		HashMap<String, Object> claimset = new HashMap<>();
		claimset.put("sub", claims.getSubject());
		return claimset;
	}
}
