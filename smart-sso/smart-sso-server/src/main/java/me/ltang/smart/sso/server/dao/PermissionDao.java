package me.ltang.smart.sso.server.dao;

import java.util.List;

import me.ltang.smart.sso.rpc.RpcPermission;
import me.ltang.smart.sso.server.model.Permission;
import org.apache.ibatis.annotations.Param;

import me.ltang.smart.sso.mvc.dao.mybatis.Dao;

/**
 * 权限持久化接口
 * 
 * @author Joe
 */
public interface PermissionDao extends Dao<Permission, Integer> {
	
	public int enable(@Param("isEnable") Boolean isEnable, @Param("idList") List<Integer> idList);
	
	public int resetPassword(@Param("password") String password, @Param("idList") List<Integer> idList);

	public List<Permission> findByName(@Param("name") String name, @Param("appId") Integer appId, @Param("isEnable") Boolean isEnable);
	
	public int deleteByAppIds(@Param("idList") List<Integer> idList);
	
	public List<RpcPermission> findListById(@Param("appCode") String appCode, @Param("userId") Integer userId);
}
