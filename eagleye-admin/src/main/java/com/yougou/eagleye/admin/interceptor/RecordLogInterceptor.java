package com.yougou.eagleye.admin.interceptor;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.yougou.eagleye.admin.constants.AppConstants;

public class RecordLogInterceptor extends HandlerInterceptorAdapter {

	public final static Logger logger = Logger
			.getLogger(RecordLogInterceptor.class.getName());

	/**
	 * 记录方法调用的起始时间
	 */
	private static ThreadLocal<Long> startTime = new ThreadLocal<Long>();

	/**
	 * 调用controller之前调用
	 * 
	 * @return 如果为真则继续执行请求的方法,如果为false则该请求将被阻断,不会执行相应的controller方法
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		startTime.set(System.currentTimeMillis());// 记录当前时间
		return true;
	}

	/**
	 * 调用controller之后调用做日志记录用
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		String url = request.getServletPath();
		Long endtime = System.currentTimeMillis();// 记下结束时间
		Long sTime = startTime.get();
		if (sTime != null) {
			String logDotime = "===============[" + url + "] 用时["
					+ (endtime - sTime) + "]ms";
			logger.info(logDotime);
		}
		startTime.remove();// 释放
		// 获取具体的操作内容,具体格式为 [方法名][具体操作内容]
		String syslogContent = AppConstants.syslogContent.get();
		if(syslogContent!=null && !syslogContent.equals("")){
			logger.info("++++++++++ controller log: [" + syslogContent + "]");
		}
		
		
		// 添加完成以后,需要清空缓存
		AppConstants.syslogContent.remove();
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
