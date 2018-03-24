package me.ltang.smart.sso.server.controller.admin;

import me.ltang.smart.sso.client.SessionUser;
import me.ltang.smart.sso.mvc.controller.BaseController;
import me.ltang.smart.sso.mvc.model.Result;
import me.ltang.smart.sso.mvc.validator.Validator;
import me.ltang.smart.sso.mvc.validator.annotation.ValidateParam;
import me.ltang.smart.sso.server.model.Permission;
import me.ltang.smart.sso.server.service.PermissionService;
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
@Api(tags = "权限(含菜单)管理")
@Controller
@RequestMapping("/admin/permission")
public class PermissionController extends BaseController {

    @Resource
    private PermissionService permissionService;

    @Resource
    private UserUtils userUtils;

    @ApiOperation("初始页")
    @RequestMapping(method = RequestMethod.GET)
    public String execute(HttpServletRequest request, Model model) {
        SessionUser user = UserUtils.getCurrentUser(request);
        model.addAttribute("appList", userUtils.getAppListByUserId(user.getUserId()));
        return "/admin/permission";
    }

    @ApiOperation("权限树节点")
    @RequestMapping(value = "/nodes", method = RequestMethod.GET)
    public @ResponseBody
    List<Permission> nodes(@ApiParam(value = "应用id") Integer appId, @ApiParam(value = "名称") String name, @ApiParam(value = "是否启用 ") Boolean isEnable) {

        if(appId == null){
            return new ArrayList<>();
        }

        List<Permission> list = permissionService.findByName(name, appId, isEnable);
        Permission permission = new Permission();
        permission.setId(null);
        permission.setParentId(-1);
        permission.setName("根节点");
        permission.setAppId(appId);
        list.add(0, permission);
        return list;
    }

    @ApiOperation("新增/修改提交")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public @ResponseBody
    Result save(
            @ApiParam(value = "id") Integer id,
            @ApiParam(value = "应用id", required = true) @ValidateParam({Validator.NOT_BLANK}) Integer appId,
            @ApiParam(value = "父id", required = true) Integer parentId,
            @ApiParam(value = "图标") String icon,
            @ApiParam(value = "名称", required = true) @ValidateParam({Validator.NOT_BLANK}) String name,
            @ApiParam(value = "权限URL", required = true) @ValidateParam({Validator.NOT_BLANK}) String url,
            @ApiParam(value = "排序", required = true) @ValidateParam({Validator.NOT_BLANK}) Integer sort,
            @ApiParam(value = "是否菜单", required = true) @ValidateParam({Validator.NOT_BLANK}) Boolean isMenu,
            @ApiParam(value = "是否启用", required = true) @ValidateParam({Validator.NOT_BLANK}) Boolean isEnable) {
        Permission permission;
        if (id == null) {
            permission = new Permission();
        } else {
            permission = permissionService.get(id);
        }
        permission.setAppId(appId);
        permission.setParentId(parentId);
        permission.setIcon(icon);
        permission.setName(name);
        permission.setUrl(url);
        permission.setSort(sort);
        permission.setIsMenu(isMenu);
        permission.setIsEnable(isEnable);
        permissionService.save(permission);
        return Result.createSuccessResult().setMessage("保存成功");
    }

    @ApiOperation("删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Result delete(
            @ApiParam(value = "id", required = true) @ValidateParam({Validator.NOT_BLANK}) Integer id,
            @ApiParam(value = "应用id", required = true) @ValidateParam({Validator.NOT_BLANK}) Integer appId) {
        permissionService.deletePermission(id, appId);
        return Result.createSuccessResult().setMessage("删除成功");
    }
}