package me.ltang.smart.sso.server.dao;

import java.util.List;

import me.ltang.smart.sso.server.model.RolePermission;
import org.apache.ibatis.annotations.Param;

import me.ltang.smart.sso.mvc.dao.mybatis.Dao;

/**
 * 角色权限映射持久化接口
 * 
 * @author Joe
 */
public interface RolePermissionDao extends Dao<RolePermission, Integer> {
	
	public List<RolePermission> findByRoleId(@Param("roleId") Integer roleId);
	
	public int deleteByPermissionIds(@Param("idList") List<Integer> idList);
	
	public int deleteByRoleIds(@Param("idList") List<Integer> idList);
	
	public int deleteByAppIds(@Param("idList") List<Integer> idList);
}
