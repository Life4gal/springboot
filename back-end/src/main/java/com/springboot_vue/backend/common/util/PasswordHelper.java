package com.springboot_vue.backend.common.util;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import com.springboot_vue.backend.entity.User;

public class PasswordHelper {

	private static final RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

	private static final String algorithmName = "md5";
	private static final int hashIterations = 2;

	public static void encryptPassword(User user) {

		user.setSalt(randomNumberGenerator.nextBytes().toHex());

		String newPassword = new SimpleHash(
				algorithmName,
				user.getPassword(),
				ByteSource.Util.bytes(user.getSalt()),
				hashIterations).toHex();

		user.setPassword(newPassword);
	}

}
