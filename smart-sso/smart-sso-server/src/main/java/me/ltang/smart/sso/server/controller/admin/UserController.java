package me.ltang.smart.sso.server.controller.admin;

import me.ltang.smart.sso.client.SessionUser;
import me.ltang.smart.sso.mvc.config.ConfigUtils;
import me.ltang.smart.sso.mvc.controller.BaseController;
import me.ltang.smart.sso.mvc.exception.ValidateException;
import me.ltang.smart.sso.mvc.model.Pagination;
import me.ltang.smart.sso.mvc.model.Result;
import me.ltang.smart.sso.mvc.model.ResultCode;
import me.ltang.smart.sso.mvc.provider.PasswordProvider;
import me.ltang.smart.sso.mvc.util.StringUtils;
import me.ltang.smart.sso.mvc.validator.Validator;
import me.ltang.smart.sso.mvc.validator.annotation.ValidateParam;
import me.ltang.smart.sso.server.model.User;
import me.ltang.smart.sso.server.service.UserService;
import me.ltang.smart.sso.server.service.impl.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Joe
 */
@Api(tags = "用户管理")
@Controller
@RequestMapping("/admin/user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @Resource
    UserUtils userUtils;

    @ApiOperation("初始页")
    @RequestMapping(method = RequestMethod.GET)
    public String execute(HttpServletRequest request, Model model) {
        SessionUser user = UserUtils.getCurrentUser(request);
        model.addAttribute("appList", userUtils.getAppListByUserId(user.getUserId()));
        return "/admin/user";
    }

    @ApiOperation("新增/修改页")
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(@ApiParam(value = "id") Integer id, Model model) {
        User user;
        if (id == null) {
            user = new User();
        } else {
            user = userService.get(id);
        }
        model.addAttribute("user", user);
//		model.addAttribute("appList", getAppList());
        return "/admin/userEdit";
    }

    @ApiOperation("列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody
    Result list(
            HttpServletRequest request,
            @ApiParam(value = "登录名") String account,
            @ApiParam(value = "应用id") Integer appId,
            @ApiParam(value = "开始页码", required = true) @ValidateParam({Validator.NOT_BLANK}) Integer pageNo,
            @ApiParam(value = "显示条数", required = true) @ValidateParam({Validator.NOT_BLANK}) Integer pageSize) {

        SessionUser user = UserUtils.getCurrentUser(request);
        /**
         * 如果是单点登录系统管理员，可以查询所有用户
         */
        if (userUtils.checkIsAdministrator(user.getUserId())) {
            return Result.createSuccessResult().setData(userService.findPaginationByAccount(account, appId, new Pagination<User>(pageNo, pageSize)));
        }
        /**
         * 初次进入页面，根据管理用户分配的应用，搜索应用下的用户,或当前用户创建的用户
         */
        if (appId == null) {
            List<Integer> appIds = userUtils.getAppListByUserId(user.getUserId()).stream().map(app -> app.getId()).collect(Collectors.toList());
            if (appIds == null || appIds.size() <= 0) {
                appIds = new ArrayList<>();
                appIds.add(-1);
            }
            return Result.createSuccessResult().setData(userService.findPaginationByAccountAndApps(account, appIds, user.getUserId(), new Pagination<User>(pageNo, pageSize)));
        }

        //TODO 判断用户是否单点登录系统管理员，如果不是，需要判断是否有权限获取此appId下用户
        return Result.createSuccessResult().setData(userService.findPaginationByAccount(account, appId, new Pagination<User>(pageNo, pageSize)));
    }

    @ApiOperation("验证登录名")
    @RequestMapping(value = "/validateAccount", method = RequestMethod.POST)
    public @ResponseBody
    Result validateAccount(
            @ApiParam(value = "id") Integer id,
            @ApiParam(value = "登录名", required = true) @ValidateParam({Validator.NOT_BLANK}) String account) {
        Result result = Result.createSuccessResult();
        User user = userService.findByAccount(account);
        if (null != user && !user.getId().equals(id)) {
            result.setCode(ResultCode.ERROR).setMessage("登录名已存在");
        }
        return result;
    }

    @ApiOperation("启用/禁用")
    @RequestMapping(value = "/enable", method = RequestMethod.POST)
    public @ResponseBody
    Result enable(
            @ApiParam(value = "ids", required = true) @ValidateParam({Validator.NOT_BLANK}) String ids,
            @ApiParam(value = "是否启用", required = true) @ValidateParam({Validator.NOT_BLANK}) Boolean isEnable) {
        userService.enable(isEnable, getAjaxIds(ids));
        return Result.createSuccessResult();
    }

    @ApiOperation("新增/修改提交")
    @ApiResponse(response = Result.class, code = 200, message = "success")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public @ResponseBody
    Result save(
            HttpServletRequest request,
            @ApiParam(value = "id") Integer id,
            @ApiParam(value = "登录名", required = true) @ValidateParam({Validator.NOT_BLANK}) String account,
            @ApiParam(value = "密码 ") String password,
            @ApiParam(value = "是否启用", required = true) @ValidateParam({Validator.NOT_BLANK}) Boolean isEnable) {
        SessionUser currentUser = UserUtils.getCurrentUser(request);

        User user;
        if (id == null) {
            if (StringUtils.isBlank(password)) {
                throw new ValidateException("密码不能为空");
            }
            user = new User();
            user.setCreatedBy(currentUser.getUserId());
            user.setCreateTime(new Date());
        } else {
            user = userService.get(id);
        }
        user.setAccount(account);
        if (StringUtils.isNotBlank(password)) {
            user.setPassword(PasswordProvider.encrypt(password));
        }
        user.setIsEnable(isEnable);
        userService.save(user);
        return Result.createSuccessResult();
    }

    @ApiOperation("重置密码")
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public @ResponseBody
    Result resetPassword(
            @ApiParam(value = "ids", required = true) @ValidateParam({Validator.NOT_BLANK}) String ids) {
        userService.resetPassword(PasswordProvider.encrypt(ConfigUtils.getProperty("system.init.password")), getAjaxIds(ids));
        return Result.createSuccessResult();
    }

    @ApiOperation("删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Result delete(
            @ApiParam(value = "ids", required = true) @ValidateParam({Validator.NOT_BLANK}) String ids) {
        userService.deleteById(getAjaxIds(ids));
        return Result.createSuccessResult();
    }

}