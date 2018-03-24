package me.ltang.smart.sso.server.service.impl;

import me.ltang.smart.sso.mvc.enums.TrueFalseEnum;
import me.ltang.smart.sso.mvc.model.Pagination;
import me.ltang.smart.sso.mvc.model.Result;
import me.ltang.smart.sso.mvc.model.ResultCode;
import me.ltang.smart.sso.mvc.provider.PasswordProvider;
import me.ltang.smart.sso.mvc.service.mybatis.impl.ServiceImpl;
import me.ltang.smart.sso.server.dao.UserDao;
import me.ltang.smart.sso.server.model.User;
import me.ltang.smart.sso.server.service.AppService;
import me.ltang.smart.sso.server.service.UserAppService;
import me.ltang.smart.sso.server.service.UserRoleService;
import me.ltang.smart.sso.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, User, Integer> implements UserService {

    @Resource
    private UserAppService userAppService;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private AppService appService;

    @Override
    @Autowired
    public void setDao(UserDao dao) {
        this.dao = dao;
    }

    @Override
    public Result login(String ip, String appCode, String account, String password) {
        Result result = Result.createSuccessResult();
        User user = findByAccount(account);
        if (user == null) {
            result.setCode(ResultCode.ERROR).setMessage("登录名不存在");
        } else if (!user.getPassword().equals(password)) {
            result.setCode(ResultCode.ERROR).setMessage("密码不正确");
        } else if (TrueFalseEnum.FALSE.getValue().equals(user.getIsEnable())) {
            result.setCode(ResultCode.ERROR).setMessage("已被管理员禁用");
        } else {
            Set<String> set = appService.findAppCodeByUserId(TrueFalseEnum.TRUE.getValue(), user.getId());
            if (CollectionUtils.isEmpty(set)) {
                result.setCode(ResultCode.ERROR).setMessage("不存在可操作应用");
            } else if (!set.contains(appCode)) {
                result.setCode(ResultCode.ERROR).setMessage("没有应用操作权限");
            } else {
                user.setLastLoginIp(ip);
                user.setLoginCount(user.getLoginCount() + 1);
                user.setLastLoginTime(new Date());
                dao.update(user);
                result.setData(user);
            }
        }
        return result;
    }

    @Override
    public void enable(Boolean isEnable, List<Integer> idList) {
        verifyRows(dao.enable(isEnable, idList), idList.size(), "管理员数据库更新失败");
    }

    @Override
    public void save(User t) {
        super.save(t);
    }

    @Override
    public void resetPassword(String password, List<Integer> idList) {
        verifyRows(dao.resetPassword(password, idList), idList.size(), "管理员密码数据库重置失败");
    }

    @Override
    public Pagination<User> findPaginationByAccount(String account, Integer appId, Pagination<User> p) {
        dao.findPaginationByAccount(account, appId, p);
        return p;
    }

    @Override
    public Pagination<User> findPaginationByAccountAndApps(String account, List<Integer> appIds, Integer createdBy, Pagination<User> p) {
        dao.findPaginationByAccountAndApps(account, appIds, createdBy, p);
        return p;
    }

    @Override
    public User findByAccount(String account) {
        return dao.findByAccount(account);
    }

    @Override
    @Transactional
    public void deleteById(List<Integer> idList) {
        userAppService.deleteByUserIds(idList);
        userRoleService.deleteByUserIds(idList, null);
        verifyRows(dao.deleteById(idList), idList.size(), "管理员数据库删除失败");
    }

    @Override
    public void updatePassword(Integer id, String newPassword) {
        User user = get(id);
        user.setPassword(PasswordProvider.encrypt(newPassword));
        update(user);
    }
}
