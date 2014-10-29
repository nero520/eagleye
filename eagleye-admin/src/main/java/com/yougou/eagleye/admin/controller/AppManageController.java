 
package com.yougou.eagleye.admin.controller;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.admin.domain.EagleyeApp;
import com.yougou.eagleye.admin.domain.EagleyeGroup;
import com.yougou.eagleye.admin.services.AppManageService;
import com.yougou.eagleye.admin.services.GroupManageService;
import com.yougou.eagleye.admin.util.DtPageBean;

@Controller
@RequestMapping("/app")
public class AppManageController extends BaseController{

	@Autowired
	private AppManageService appManageService;
	
	@Autowired
	private GroupManageService groupManageService;
	
	/**
	 * 打开管理页面
	 */
	@RequestMapping(value = "/index")
	public ModelAndView index(HttpServletRequest request,EagleyeApp eagleyeApp)throws Exception{
		
		return new ModelAndView("/app/index");
	}
	
	/**
	 * 简单查询
	 * 
	 * @param pageBean
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/querySimple")
	@ResponseBody
	public Map<String,Object> querySimpleAppList(HttpServletRequest request,DtPageBean<EagleyeApp> pageBean,String sSearch) throws Exception{
		Map<String, Object> modelMap = new HashMap<String, Object>();
		pageBean = this.appManageService.querySimpleAppList(pageBean, sSearch);
		this.formatDtPageBean(modelMap, pageBean);
		return modelMap;
	}
	
	
	/**
	 * 查看用户详情
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/show/{id}") 
	public ModelAndView show(HttpServletRequest request,@PathVariable("id")String id) throws Exception{
		if(id!=null && !id.trim().equals("")){
			EagleyeApp eagleyeApp= this.appManageService.getAppById(id);
			request.setAttribute("app", eagleyeApp);
		}
		List<EagleyeGroup> groups= this.groupManageService.getList();
		request.setAttribute("groups", groups);
		return new ModelAndView("/app/show");
	}
	
	
	
	/**
	 * 新增页面
	 */
	@RequestMapping(value = "/add")
	public ModelAndView add(HttpServletRequest request)throws Exception{
		List<EagleyeGroup> groups= this.groupManageService.getList();
		request.setAttribute("groups", groups);
		return new ModelAndView("/app/add");
	}
	
	/**
	 * 添加动作
	 * @param eagleyeApp
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/create",method=RequestMethod.POST)
	@ResponseBody 
	public Map<String, Object> create(HttpServletRequest request,EagleyeApp eagleyeApp) throws Exception{
		Map<String, Object> modelMap = new HashMap<String, Object>();
		EagleyeApp app = this.appManageService.createApp(eagleyeApp);
		modelMap.put("flash", app.getFlash());
		return modelMap;
	}
	
	/**
	 * 进入修改页面
	 */
	@RequestMapping(value = "/{id}/edit")
	public ModelAndView edit(HttpServletRequest request,@PathVariable("id")String id)throws Exception{
		
		if(id!=null && !id.trim().equals("")){
			EagleyeApp eagleyeApp= this.appManageService.getAppById(id);
			request.setAttribute("app", eagleyeApp);
		}
		List<EagleyeGroup> groups= this.groupManageService.getList();
		request.setAttribute("groups", groups);
		return new ModelAndView("/app/edit");
	}
	
	
	/**
	 * 编辑动作
	 * @param eagleyeApp
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/update",method=RequestMethod.POST)
	@ResponseBody 
	public Map<String, Object> update(HttpServletRequest request,EagleyeApp eagleyeApp) throws Exception{
		Map<String, Object> modelMap = new HashMap<String, Object>();
		EagleyeApp app = this.appManageService.updateApp(eagleyeApp);
		modelMap.put("flash", app.getFlash());
		return modelMap;
	}
	
	
	
	/**
	 * 删除用户 逻辑删除
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/destroy/{id}",method=RequestMethod.POST)  
	@ResponseBody 
	public Map<String, Object> destroy(HttpServletRequest request,@PathVariable("id")String id) throws Exception{
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if(this.appManageService.delete(id) > 0){
			modelMap.put("flash", AppConstants.FLASH_SUCCESS);
		}else{
			modelMap.put("flash", AppConstants.FLASH_ERROR);
		}
		return modelMap;
	}
	
}