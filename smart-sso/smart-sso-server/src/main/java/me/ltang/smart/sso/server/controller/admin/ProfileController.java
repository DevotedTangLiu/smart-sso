package me.ltang.smart.sso.server.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import me.ltang.smart.sso.mvc.controller.BaseController;
import me.ltang.smart.sso.mvc.model.Result;
import me.ltang.smart.sso.mvc.model.ResultCode;
import me.ltang.smart.sso.mvc.validator.Validator;
import me.ltang.smart.sso.mvc.validator.annotation.ValidateParam;
import me.ltang.smart.sso.client.SessionUtils;
import me.ltang.smart.sso.server.common.LoginUser;
import me.ltang.smart.sso.server.common.TokenManager;
import me.ltang.smart.sso.server.service.UserService;

/**
 * @author Joe
 */
@Api(tags = "个人中心")
@Controller
@RequestMapping("/admin/profile")
public class ProfileController extends BaseController {

	@Resource
	private TokenManager tokenManager;
	@Resource
	private UserService userService;

	@ApiOperation("初始页")
	@RequestMapping(method = RequestMethod.GET)
	public String execute(Model model, HttpServletRequest request) {
		LoginUser loginUser = tokenManager.validate(SessionUtils.getSessionUser(request).getToken());
		if (loginUser != null) {
			model.addAttribute("user", userService.get(loginUser.getUserId()));
		}
		return "/admin/profile";
	}

	@ApiOperation("修改密码提交")
	@RequestMapping(value = "/savePassword", method = RequestMethod.POST)
	public @ResponseBody Result save(
			@ApiParam(value = "新密码", required = true) @ValidateParam({ Validator.NOT_BLANK }) String newPassword,
			@ApiParam(value = "确认密码", required = true) @ValidateParam({ Validator.NOT_BLANK }) String confirmPassword,
			HttpServletRequest request) {
		LoginUser loginUser = tokenManager.validate(SessionUtils.getSessionUser(request).getToken());
		if (loginUser != null) {
			userService.updatePassword(loginUser.getUserId(), newPassword);
			return Result.createSuccessResult().setMessage("修改成功");
		}
		else {
			return Result.create(ResultCode.VALIDATE_ERROR).setMessage("修改失败");
		}
	}
}