package me.ltang.smart.sso.server.dao;

import me.ltang.smart.sso.mvc.dao.mybatis.Dao;
import me.ltang.smart.sso.server.model.UserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色映射持久化接口
 *
 * @author Joe
 */
public interface UserRoleDao extends Dao<UserRole, Integer> {

    UserRole findByUserRoleId(@Param("userId") Integer userId, @Param("roleId") Integer roleId);

    int deleteByRoleIds(@Param("idList") List<Integer> idList);

    int deleteByUserIds(@Param("idList") List<Integer> idList, @Param("appId") Integer appId);

    int deleteByAppIds(@Param("idList") List<Integer> idList);

    int deleteForChangeApp(@Param("userId") Integer userId, @Param("idList") List<Integer> idList);

    int deleteByUserAppIds(@Param("userId") Integer userId, @Param("uncheckAppIds") List<Integer> uncheckAppIds);
}
