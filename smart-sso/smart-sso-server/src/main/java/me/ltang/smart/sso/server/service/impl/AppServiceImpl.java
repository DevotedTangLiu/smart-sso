package me.ltang.smart.sso.server.service.impl;

import me.ltang.smart.sso.mvc.model.Pagination;
import me.ltang.smart.sso.mvc.service.mybatis.impl.ServiceImpl;
import me.ltang.smart.sso.server.dao.AppDao;
import me.ltang.smart.sso.server.model.App;
import me.ltang.smart.sso.server.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author
 */
@Service("appService")
public class AppServiceImpl extends ServiceImpl<AppDao, App, Integer> implements AppService {

    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private UserAppService userAppService;
    @Resource
    private RolePermissionService rolePermissionService;

    @Override
    @Autowired
    public void setDao(AppDao dao) {
        this.dao = dao;
    }

    @Override
    public void enable(Boolean isEnable, List<Integer> idList) {
        verifyRows(dao.enable(isEnable, idList), idList.size(), "应用数据库更新失败");
    }

    @Override
    public void save(App t) {
        super.save(t);
    }

    @Override
    public List<App> findByAll(String name) {
        return dao.findPaginationByName(name, null);
    }

    @Override
    public Pagination<App> findPaginationByName(String name, Pagination<App> p) {
        dao.findPaginationByName(name, p);
        return p;
    }

    @Override
    public App findByCode(String code) {
        return dao.findByCode(code);
    }

    @Override
    public List<App> findByUserId(Boolean isEnable, Integer userId) {
        return dao.findByUserId(isEnable, userId);
    }

    @Override
    @Transactional
    public void deleteById(List<Integer> idList) {
        rolePermissionService.deleteByAppIds(idList);
        userRoleService.deleteByAppIds(idList);
        userAppService.deleteByAppIds(idList);
        permissionService.deleteByAppIds(idList);
        roleService.deleteByAppIds(idList);
        verifyRows(dao.deleteById(idList), idList.size(), "应用数据库删除失败");
    }

    @Override
    public Set<String> findAppCodeByUserId(Boolean isEnable, Integer userId) {
        return dao.findAppCodeByUserId(isEnable, userId);
    }
}
