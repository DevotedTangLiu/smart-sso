package me.ltang.smart.sso.server.dao;

import java.util.List;
import java.util.Set;

import me.ltang.smart.sso.mvc.model.Pagination;
import me.ltang.smart.sso.server.model.App;
import org.apache.ibatis.annotations.Param;

import me.ltang.smart.sso.mvc.dao.mybatis.Dao;

/**
 * 应用持久化接口
 * 
 * @author Joe
 */
public interface AppDao extends Dao<App, Integer> {
	
	public int enable(@Param("isEnable") Boolean isEnable, @Param("idList") List<Integer> idList);
	
	public List<App> findPaginationByName(@Param("name") String name, Pagination<App> p);
	
	public App findByCode(@Param("code") String code);
	
	public List<App> findByUserId(@Param("isEnable") Boolean isEnable, @Param("userId") Integer userId);
	
	public Set<String> findAppCodeByUserId(@Param("isEnable") Boolean isEnable, @Param("userId") Integer userId);
}
