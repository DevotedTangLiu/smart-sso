package me.ltang.smart.sso.server.service;

import me.ltang.smart.sso.mvc.service.mybatis.Service;
import me.ltang.smart.sso.server.model.UserApp;

import java.util.List;

/**
 * 管理员角色映射服务接口
 *
 * @author Joe
 */
public interface UserAppService extends Service<UserApp, Integer> {

    /**
     * 根据用户ID和应用ID查询映射
     *
     * @param userId 用户ID
     * @param appId  应用ID
     * @return
     */
    UserApp findByUserAppId(Integer userId, Integer appId);

    /**
     * 根据用户ID给用户分配应用
     *
     * @param userId        用户ID
     * @param idList        分配的应用ID集合
     * @param uncheckIdList 未分配的应用ID集合
     * @param list          用户和应用映射集合
     * @return
     */
    void allocate(Integer userId, List<Integer> idList, List<Integer> uncheckIdList, List<UserApp> list);

    /**
     * 根据用户ID集合删除映射
     *
     * @param idList 管理员ID集合
     * @return
     */
    void deleteByUserIds(List<Integer> idList);

    /**
     * 根据应用ID集合删除映射
     *
     * @param idList 应用ID集合
     * @return
     */
    void deleteByAppIds(List<Integer> idList);
}
