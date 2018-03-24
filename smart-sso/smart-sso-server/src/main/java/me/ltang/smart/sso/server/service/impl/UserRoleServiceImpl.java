package me.ltang.smart.sso.server.service.impl;

import me.ltang.smart.sso.mvc.service.mybatis.impl.ServiceImpl;
import me.ltang.smart.sso.server.dao.UserRoleDao;
import me.ltang.smart.sso.server.model.UserRole;
import me.ltang.smart.sso.server.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleDao, UserRole, Integer> implements UserRoleService {

    @Autowired
    public void setDao(UserRoleDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void allocate(Integer userId, Integer appId, List<UserRole> list) {
        dao.deleteByUserIds(Arrays.asList(userId), appId);
        super.save(list);
    }

    @Override
    public UserRole findByUserRoleId(Integer userId, Integer roleId) {
        return dao.findByUserRoleId(userId, roleId);
    }

    @Override
    public void deleteByRoleIds(List<Integer> idList) {
        dao.deleteByRoleIds(idList);
    }

    @Override
    public void deleteByUserIds(List<Integer> idList, Integer appId) {
        dao.deleteByUserIds(idList, appId);
    }

    @Override
    public void deleteByAppIds(List<Integer> idList) {
        dao.deleteByAppIds(idList);
    }

    @Override
    public void deleteForChangeApp(Integer userId, List<Integer> idList) {
        dao.deleteForChangeApp(userId, idList);
    }

    @Override
    public void deleteByUserAppIds(Integer userId, List<Integer> uncheckAppIds) {
        if (uncheckAppIds != null && uncheckAppIds.size() > 0) {
            dao.deleteByUserAppIds(userId, uncheckAppIds);
        }
    }
}
