package me.ltang.smart.sso.server.dao;

import java.util.List;

import me.ltang.smart.sso.mvc.model.Pagination;
import me.ltang.smart.sso.server.model.Role;
import org.apache.ibatis.annotations.Param;

import me.ltang.smart.sso.mvc.dao.mybatis.Dao;

/**
 * 角色持久化接口
 * 
 * @author Joe
 */
public interface RoleDao extends Dao<Role, Integer> {

	public int enable(@Param("isEnable") Boolean isEnable, @Param("idList") List<Integer> idList);

	public int resetPassword(@Param("password") String password, @Param("idList") List<Integer> idList);

	public List<Role> findPaginationByName(@Param("name") String name, @Param("isEnable") Boolean isEnable,
			@Param("appId") Integer appId, Pagination<Role> p);

	public int deleteByAppIds(@Param("idList") List<Integer> idList);
}
