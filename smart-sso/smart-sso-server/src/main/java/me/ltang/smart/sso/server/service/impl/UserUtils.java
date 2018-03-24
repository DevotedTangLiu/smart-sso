package me.ltang.smart.sso.server.service.impl;

import me.ltang.smart.sso.client.SessionUser;
import me.ltang.smart.sso.client.SessionUtils;
import me.ltang.smart.sso.client.SsoResultCode;
import me.ltang.smart.sso.mvc.exception.ServiceException;
import me.ltang.smart.sso.server.model.App;
import me.ltang.smart.sso.server.model.UserRole;
import me.ltang.smart.sso.server.service.AppService;
import me.ltang.smart.sso.server.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;

/**
 * @author tangliu
 */
@Service("userUtils")
public class UserUtils {

    @Resource
    private AppService appService;

    @Autowired
    UserRoleService userRoleService;

    /**
     * 通过httServletRequest获取当前登录用户
     *
     * @param request
     * @return
     */
    public static SessionUser getCurrentUser(HttpServletRequest request) {

        SessionUser user = SessionUtils.getSessionUser(request);
        if (user == null) {
            throw new ServiceException(SsoResultCode.SSO_TOKEN_ERROR, "未登录或已超时");
        }
        return user;
    }

    /**
     * 判断用户是否单点登录系统管理员
     *
     * @param userId
     * @return
     */
    public boolean checkIsAdministrator(Integer userId) {
        UserRole userRole = userRoleService.findByUserRoleId(userId, 1);
        return userRole == null ? false : true;
    }

    /**
     * 根据用户id，获取用户分配的应用
     * <p>
     * 注意：
     * 1. 如果用户是单点登录系统管理员，则返回所有应用
     * 2. 如果不是，不返回单点登录系统
     */
    public List<App> getAppListByUserId(Integer userId) {

        //todo roleId = 1 以及 app.getId == 1,使用配置文件读取
        UserRole userRole = userRoleService.findByUserRoleId(userId, 1);
        if (userRole != null) {
            return getAppList();
        }

        List<App> apps = appService.findByUserId(true, userId);
        if (apps != null && apps.size() > 0) {
            Iterator<App> iterator = apps.iterator();
            while (iterator.hasNext()) {
                App app = iterator.next();
                if (app.getId() == 1) {
                    iterator.remove();
                }
            }
        }
        return apps;
    }

    /**
     * 查询所有App
     *
     * @return
     */
    private List<App> getAppList() {
        return appService.findByAll(null);
    }
}
