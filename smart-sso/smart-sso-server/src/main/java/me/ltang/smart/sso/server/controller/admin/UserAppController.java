package me.ltang.smart.sso.server.controller.admin;

import me.ltang.smart.sso.client.SessionUser;
import me.ltang.smart.sso.mvc.controller.BaseController;
import me.ltang.smart.sso.mvc.model.Result;
import me.ltang.smart.sso.mvc.validator.Validator;
import me.ltang.smart.sso.mvc.validator.annotation.ValidateParam;
import me.ltang.smart.sso.server.model.App;
import me.ltang.smart.sso.server.model.UserApp;
import me.ltang.smart.sso.server.service.AppService;
import me.ltang.smart.sso.server.service.UserAppService;
import me.ltang.smart.sso.server.service.impl.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Joe
 */
@Api(tags = "用户与应用关系管理")
@Controller
@RequestMapping("/admin/userApp")
public class UserAppController extends BaseController {

    @Resource
    private AppService appService;
    @Resource
    private UserAppService userAppService;
    @Resource
    private UserUtils userUtils;

    @ApiOperation("初始页")
    @RequestMapping(value = "/allocate", method = RequestMethod.GET)
    public String edit(HttpServletRequest request,
                       @ApiParam(value = "用户id", required = true) @ValidateParam({Validator.NOT_BLANK}) Integer userId, Model model) {

        SessionUser user = UserUtils.getCurrentUser(request);

        model.addAttribute("userId", userId);
        model.addAttribute("appList", getAppListByAllocated(user.getUserId(), userId));

        return "/admin/userApp";
    }

    @ApiOperation("用户与应用关联提交")
    @RequestMapping(value = "/allocateSave", method = RequestMethod.POST)
    public @ResponseBody
    Result allocateSave(
            @ApiParam(value = "用户id", required = true) @ValidateParam({Validator.NOT_BLANK}) Integer userId,
            @ApiParam(value = "分配的应用ids") String appIds,
            @ApiParam(value = "未分配的应用ids") String uncheckAppIds) {
        List<Integer> idList = getAjaxIds(appIds);
        List<Integer> uncheckIdList = getAjaxIds(uncheckAppIds);
        List<UserApp> list = new ArrayList<UserApp>();
        UserApp bean = null;
        for (Integer appId : idList) {
            bean = new UserApp();
            bean.setAppId(appId);
            bean.setUserId(userId);
            list.add(bean);
        }
        userAppService.allocate(userId, idList, uncheckIdList, list);
        return Result.createSuccessResult().setMessage("授权成功");
    }

    /**
     * 查询用户分配的应用
     *
     * @param userId
     * @return
     */
    private List<App> getAppList(Integer userId) {
        List<App> list = appService.findByAll(null);
        checkUserApp(list, userId);
        return list;
    }

    /**
     * 查询目标用户在当前用户被分配的应用中的分配情况
     * 1) 查出当前用户被分配的应用
     * 2) 分别判断目标用户是否被分配了步骤1)中的应用
     *
     * @param currentUserId
     * @param targetUserId
     * @return
     */
    private List<App> getAppListByAllocated(Integer currentUserId, Integer targetUserId) {
        List<App> list = userUtils.getAppListByUserId(currentUserId);
        checkUserApp(list, targetUserId);
        return list;
    }

    private List<App> checkUserApp(List<App> list, Integer userId) {

        for (App app : list) {
            UserApp userApp = userAppService.findByUserAppId(userId, app.getId());
            if (null != userApp) {
                app.setIsChecked(true);
            } else {
                app.setIsChecked(false);
            }
        }

        return list;
    }
}