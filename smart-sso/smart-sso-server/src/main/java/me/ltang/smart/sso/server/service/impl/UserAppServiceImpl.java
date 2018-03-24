package me.ltang.smart.sso.server.service.impl;

import me.ltang.smart.sso.mvc.service.mybatis.impl.ServiceImpl;
import me.ltang.smart.sso.server.dao.UserAppDao;
import me.ltang.smart.sso.server.model.UserApp;
import me.ltang.smart.sso.server.service.UserAppService;
import me.ltang.smart.sso.server.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
@Service("userAppService")
public class UserAppServiceImpl extends ServiceImpl<UserAppDao, UserApp, Integer> implements UserAppService {

    @Resource
    private UserRoleService userRoleService;

    @Override
    @Autowired
    public void setDao(UserAppDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void allocate(Integer userId, List<Integer> idList, List<Integer> uncheckIdList, List<UserApp> list) {

        /**
         * 删除未分配的应用中的用户角色映射
         */
        userRoleService.deleteByUserAppIds(userId, uncheckIdList);

        List<Integer> appIds = new ArrayList<>();
        for (Integer id : idList) {
            appIds.add(id);
        }
        for (Integer id : uncheckIdList) {
            appIds.add(id);
        }

        /**
         * 删除用户应用映射记录
         */
        dao.deleteByUserIdAppIds(userId, appIds);
        super.save(list);
    }

    @Override
    public UserApp findByUserAppId(Integer userId, Integer roleId) {
        return dao.findByUserAppId(userId, roleId);
    }

    @Override
    public void deleteByUserIds(List<Integer> idList) {
        dao.deleteByUserIds(idList);
    }

    @Override
    public void deleteByAppIds(List<Integer> idList) {
        dao.deleteByAppIds(idList);
    }
}
