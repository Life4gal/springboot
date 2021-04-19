package com.springboot_vue.backend.oauth;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.springboot_vue.backend.common.constant.Base;
import com.springboot_vue.backend.entity.User;
import com.springboot_vue.backend.entity.UserStatus;
import com.springboot_vue.backend.service.UserService;

public class Realm extends AuthorizingRealm {

	@Autowired
	private UserService userService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String account = (String) principals.getPrimaryPrincipal();
		User user = userService.getUserByAccount(account);
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		Set<String> roles = new HashSet<>();

		//简单处理   只有admin一个角色
		if (user.getAdmin()) {
			roles.add(Base.ROLE_ADMIN);
		}

		authorizationInfo.setRoles(roles);

		return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		String account = (String) token.getPrincipal();

		User user = userService.getUserByAccount(account);

		if (user == null) {
			throw new UnknownAccountException();//没找到帐号
		}

		if (UserStatus.blocked.equals(user.getStatus())) {
			throw new LockedAccountException(); //帐号锁定
		}

		return new SimpleAuthenticationInfo(
				user.getAccount(),
				user.getPassword(),
				ByteSource.Util.bytes(user.getSalt()),
				getName()
		);
	}

}
