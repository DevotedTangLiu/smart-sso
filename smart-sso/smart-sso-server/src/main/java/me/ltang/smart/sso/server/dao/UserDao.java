package me.ltang.smart.sso.server.dao;

import me.ltang.smart.sso.mvc.dao.mybatis.Dao;
import me.ltang.smart.sso.mvc.model.Pagination;
import me.ltang.smart.sso.server.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 管理员持久化接口
 *
 * @author Joe
 */
public interface UserDao extends Dao<User, Integer> {

    public int enable(@Param("isEnable") Boolean isEnable, @Param("idList") List<Integer> idList);

    public int resetPassword(@Param("password") String password, @Param("idList") List<Integer> idList);

    public List<User> findPaginationByAccount(@Param("account") String account, @Param("appId") Integer appId, Pagination<User> p);

    List<User> findPaginationByAccountAndApps(@Param("account") String account, @Param("appIds") List<Integer> appIds, @Param("createdBy") Integer createdBy, Pagination<User> p);

    public User findByAccount(@Param("account") String account);
}
