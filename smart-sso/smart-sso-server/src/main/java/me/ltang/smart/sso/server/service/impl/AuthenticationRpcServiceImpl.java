package me.ltang.smart.sso.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import me.ltang.smart.sso.rpc.AuthenticationRpcService;
import me.ltang.smart.sso.rpc.RpcPermission;
import me.ltang.smart.sso.rpc.RpcUser;
import me.ltang.smart.sso.server.common.LoginUser;
import me.ltang.smart.sso.server.common.TokenManager;
import me.ltang.smart.sso.server.service.PermissionService;
import me.ltang.smart.sso.server.service.UserService;
import me.ltang.smart.sso.mvc.util.StringUtils;
import org.springframework.stereotype.Service;

@Service("authenticationRpcService")
public class AuthenticationRpcServiceImpl implements AuthenticationRpcService {

	@Resource
	private PermissionService permissionService;
	@Resource
	private UserService userService;
	@Resource
	private TokenManager tokenManager;

	@Override
	public boolean validate(String token) {
		return tokenManager.validate(token) != null;
	}
	
	@Override
	public RpcUser findAuthInfo(String token) {
		LoginUser user = tokenManager.validate(token);
		if (user != null) {
			return new RpcUser(user.getUserId(), user.getAccount());
		}
		return null;
	}
	
	@Override
	public List<RpcPermission> findPermissionList(String token, String appCode) {
		if (StringUtils.isBlank(token)) {
			return permissionService.findListById(appCode, null);
		}
		else {
			LoginUser user = tokenManager.validate(token);
			if (user != null) {
				return permissionService.findListById(appCode, user.getUserId());
			}
			else {
				return new ArrayList<RpcPermission>(0);
			}
		}
	}
}
