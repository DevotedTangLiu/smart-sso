package me.ltang.smart.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import me.ltang.smart.sso.client.SessionPermission;
import me.ltang.smart.sso.client.SessionUser;
import me.ltang.smart.sso.client.SessionUtils;

/**
 * @author Joe
 */
@Controller
@RequestMapping("/index")
public class IndexController {

	@RequestMapping(method = RequestMethod.GET)
	public String execute(HttpServletRequest request, Model model) {
		SessionUser sessionUser = SessionUtils.getSessionUser(request);
		model.addAttribute("userName", sessionUser.getAccount());
		
		SessionPermission sessionPermission = SessionUtils.getSessionPermission(request);
		if (sessionPermission != null){
			model.addAttribute("userMenus", sessionPermission.getMenuList());
			model.addAttribute("userPermissions", sessionPermission.getPermissionSet());
		}
		return "/index";
	}
}