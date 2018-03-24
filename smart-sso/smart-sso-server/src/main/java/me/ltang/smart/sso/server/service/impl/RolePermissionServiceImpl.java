package me.ltang.smart.sso.server.service.impl;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import me.ltang.smart.sso.server.dao.RolePermissionDao;
import me.ltang.smart.sso.server.service.AppService;
import me.ltang.smart.sso.server.model.RolePermission;
import me.ltang.smart.sso.server.service.PermissionJmsService;
import me.ltang.smart.sso.server.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.ltang.smart.sso.mvc.service.mybatis.impl.ServiceImpl;
import me.ltang.smart.sso.server.service.RolePermissionService;

@Service("rolePermissionService")
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionDao, RolePermission, Integer> implements RolePermissionService {
	
	@Resource
	private RoleService roleService;
	@Resource
	private AppService appService;
	@Resource
	private PermissionJmsService permissionJmsService;

	@Autowired
	public void setDao(RolePermissionDao dao) {
		this.dao = dao;
	}

	@Transactional
	public void allocate(Integer roleId, List<RolePermission> list) {
		dao.deleteByRoleIds(Arrays.asList(roleId));
		super.save(list);
		// JMS通知权限变更
		permissionJmsService.send(appService.get(roleService.get(roleId).getAppId()).getCode());
	}

	public List<RolePermission> findByRoleId(Integer roleId) {
		return dao.findByRoleId(roleId);
	}

	public void deleteByPermissionIds(List<Integer> idList) {
		dao.deleteByPermissionIds(idList);
	}
	
	public void deleteByRoleIds(List<Integer> idList) {
		dao.deleteByRoleIds(idList);
	}
	
	public void deleteByAppIds(List<Integer> idList) {
		dao.deleteByAppIds(idList);
	}
}
