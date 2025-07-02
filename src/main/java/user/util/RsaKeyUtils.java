package user.util;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import user.exception.RsaException;

public class RsaKeyUtils {

	private static final String RSA = "RSA";

	private RsaKeyUtils() {
	}

	public static PrivateKey getPrivateKey(String encodedPrivateKey) {
		encodedPrivateKey = encodedPrivateKey.replace(" ", "");
		byte[] encodedPrivateKeyBytes = encodedPrivateKey.getBytes(StandardCharsets.UTF_8);
		try {
			return getPrivateKey(encodedPrivateKeyBytes);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RsaException("Wrong private key");
		}
	}

	private static PrivateKey getPrivateKey(byte[] encodedKey)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decodedKey);
		KeyFactory factory = KeyFactory.getInstance(RSA);
		return factory.generatePrivate(spec);
	}

	public static PublicKey getPublicKey(String encodedPublicKey) {
		encodedPublicKey = encodedPublicKey.replace(" ", "");
		byte[] encodedPublicKeyBytes = encodedPublicKey.getBytes(StandardCharsets.UTF_8);
		try {
			return getPublicKey(encodedPublicKeyBytes);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RsaException("Wrong public key");
		}
	}

	private static PublicKey getPublicKey(byte[] encodedKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedKey);
		KeyFactory factory = KeyFactory.getInstance(RSA);
		return factory.generatePublic(spec);
	}
}
