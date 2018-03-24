package me.ltang.smart.sso.server.dao;

import me.ltang.smart.sso.mvc.dao.mybatis.Dao;
import me.ltang.smart.sso.server.model.UserApp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 管理员角色映射持久化接口
 *
 * @author Joe
 */
public interface UserAppDao extends Dao<UserApp, Integer> {

    UserApp findByUserAppId(@Param("userId") Integer userId, @Param("appId") Integer appId);

    int deleteByAppIds(@Param("idList") List<Integer> idList);

    int deleteByUserIds(@Param("idList") List<Integer> idList);

    /**
     * 根据用户id和应用id，删除映射记录
     *
     * @param userId
     * @param appIds
     * @return
     */
    int deleteByUserIdAppIds(@Param("userId") Integer userId, @Param("appIds") List<Integer> appIds);
}
