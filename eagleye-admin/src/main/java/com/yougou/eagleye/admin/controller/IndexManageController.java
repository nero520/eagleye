 
package com.yougou.eagleye.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.admin.domain.EagleyeGroup;
import com.yougou.eagleye.admin.services.GroupManageService;
import com.yougou.eagleye.core.dao.page.PageBean;

@Controller
@RequestMapping("")
public class IndexManageController extends BaseController{

	@Autowired
	private GroupManageService groupManageService;
	
	
	
	
	
	/**
	 * 用户登录
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/login")
	public String login(HttpServletRequest request,String userAccount,String userPwd) throws Exception {
		HttpSession session = request.getSession();
		if(session.getAttribute(AppConstants.EAGLEYE_SESSION_USERNAME) == null){
			if("administrator".equals(userAccount) && "administrator".equals(userPwd)){
				session.setAttribute(AppConstants.EAGLEYE_SESSION_USERNAME, "admin");
				return "forward:/index.html";
			}else if("guest".equals(userAccount) && "guest".equals(userPwd)){
				session.setAttribute(AppConstants.EAGLEYE_SESSION_USERNAME, "guest");
				return "forward:/index.html";
			}else{
				return "forward:/viewlogin.html";
			}
			
		}else{//如果当前session不为空，则直接跳转到主页
			return "redirect:/index.html";
		}
	}
	
	
	/**
	 * 用户退出
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request) throws Exception {
		if(request.getSession()!=null){
			request.getSession().removeAttribute(AppConstants.EAGLEYE_SESSION_USERNAME);
		}
		return "redirect:/";
	}
	
	
	
	/**
	 * 打开管理页面
	 */
	@RequestMapping(value = "viewlogin")
	public ModelAndView viewlogin(HttpServletRequest request)throws Exception{
		
		return new ModelAndView("/login");
	}
	
	
	/**
	 * 跳转
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/indexredirect")
	public String indexredirect(HttpServletRequest request) throws Exception{
		return "/indexredirect";
	}
	
	
	/**
	 * 打开管理页面
	 */
	@RequestMapping(value = "index")
	public ModelAndView index(HttpServletRequest request)throws Exception{
		
		return new ModelAndView("/index");
	}
	
	@RequestMapping(value = "/left")
	public String left(HttpServletRequest request) throws Exception{
		return "/left";
	}
	
	@RequestMapping(value = "/top")
	public String top(HttpServletRequest request) throws Exception{
		return "/top";
	}
	
	@RequestMapping(value = "/mid")
	public String mid(HttpServletRequest request) throws Exception{
		return "/mid";
	}
	
	@RequestMapping(value = "/right")
	public String right(HttpServletRequest request) throws Exception{
		return "/right";
	}
	
	
	@RequestMapping(value = "/redis")
	public ModelAndView redis(HttpServletRequest request)throws Exception{
		
		return new ModelAndView("/menu/redis");
	}
	
	
	@RequestMapping(value = "/other")
	public ModelAndView other(HttpServletRequest request)throws Exception{
		
		return new ModelAndView("/menu/other");
	}
	
}