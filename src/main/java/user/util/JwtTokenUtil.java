package user.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import user.exception.TokenException;

@Component
public class JwtTokenUtil {

	private static final String BEARER = "Bearer ";
	private static final String ID_CLAIM = "id";

	@Value("${rsa.key.private}")
	private String privateKey;

	@Value("${rsa.key.public}")
	private String publicKey;

	public String createAccessToken(Long userId) {
		return Jwts.builder().signWith(RsaKeyUtils.getPrivateKey(privateKey), SignatureAlgorithm.RS256)
				.claim(ID_CLAIM, userId).compact();
	}

	public Long getUserId(String token) {
		if (!token.isEmpty()) {
			String tokenWithoutBearer = token.substring(BEARER.length());
			Claims claims = Jwts.parserBuilder().setSigningKey(RsaKeyUtils.getPublicKey(publicKey)).build()
					.parseClaimsJws(tokenWithoutBearer).getBody();
			Long userId = claims.get(ID_CLAIM, Long.class);
			if (userId != null) {
				return userId;
			}
		}
		throw new TokenException();
	}
}
