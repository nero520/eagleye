package com.yougou.eagleye.admin.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.core.utils.WebUtils;


public class AuthorityInterceptor extends HandlerInterceptorAdapter {


	private static final Logger logger = Logger.getLogger(AuthorityInterceptor.class);
	

	/**
	 * 调用controller之前调用
	 * 
	 * @return 如果为真则继续执行请求的方法,如果为false则该请求将被阻断,不会执行相应的controller方法
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		HttpSession session = request.getSession();
		String url = request.getServletPath();
		url=request.getQueryString()!=null?url+"?"+request.getQueryString():url;
		String user=(String)session.getAttribute(AppConstants.EAGLEYE_SESSION_USERNAME);
		
		if (user == null) {//没有登录直接跳转到登录页
			if(url.indexOf("login.html") < 0 && url.indexOf("indexredirect.html")<0){
				if("application/x-www-form-urlencoded; charset=UTF-8".equals(request.getContentType())){//是ajax请求,没有打开页面流
					response.setContentType("application/x-json;charset=utf-8");
				}else{
					response.sendRedirect(WebUtils.getBasePath(request)+"/indexredirect.html");
				}
				
				return false;
			}
		}
		return true;
		
		
	}

	/**
	 * 调用controller之后调用做日志记录用
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
