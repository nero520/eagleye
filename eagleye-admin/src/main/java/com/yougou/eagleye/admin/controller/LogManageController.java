 
package com.yougou.eagleye.admin.controller;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yougou.eagleye.admin.domain.EagleyeLog;
import com.yougou.eagleye.admin.services.LogManageService;
import com.yougou.eagleye.admin.util.DtPageBean;

@Controller
@RequestMapping("/log")
public class LogManageController extends BaseController{

	@Autowired
	private LogManageService logManageService;
	
	
	
	
	/**
	 * 打开管理页面
	 */
	@RequestMapping(value = "/index")
	public ModelAndView index(HttpServletRequest request,EagleyeLog eagleyeLog)throws Exception{
		
		return new ModelAndView("/log/index");
	}
	
	/**
	 * 简单查询
	 * 
	 * @param pageBean
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/query")
	@ResponseBody
	public Map<String,Object> query(HttpServletRequest request,DtPageBean<EagleyeLog> pageBean,String sSearch,String appName,String date) throws Exception{
		Map<String, Object> modelMap = new HashMap<String, Object>();
		this.logManageService.findByDateAndAppNameAndBody(date,appName, sSearch, pageBean);
		this.formatDtPageBean(modelMap, pageBean);
		return modelMap;
	}
	
	
	
	/**
	 * 简单查询
	 * 
	 * @param pageBean
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getAppNameList")
	@ResponseBody
	public Map<String,Object> getAppNameList(HttpServletRequest request,String date) throws Exception{
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<String> appNameList = this.logManageService.getAllAppNameByDate(date);
		modelMap.put("result", appNameList);
		return modelMap;
	}
	
	
	
}