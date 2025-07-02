package user.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Md5Util {

	private Md5Util() {
	}

	public static String getMd5Sum(String inputString) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] inputBytes = inputString.getBytes(StandardCharsets.UTF_8);
			byte[] messageDigest = md.digest(inputBytes);
			StringBuilder hexString = new StringBuilder();
			for (byte b : messageDigest) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			log.error("MD5 algorithm not found: " + e.getMessage());
			return null;
		}
	}
}
