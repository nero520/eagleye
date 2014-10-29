 
package com.yougou.eagleye.admin.controller;

import javax.servlet.http.HttpServletRequest;

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
import com.yougou.eagleye.admin.util.DtPageBean;
import com.yougou.eagleye.core.dao.page.PageBean;

@Controller
@RequestMapping("/group")
public class GroupManageController extends BaseController{

	@Autowired
	private GroupManageService groupManageService;
	
	/**
	 * 打开管理页面
	 */
	@RequestMapping(value = "/index")
	public ModelAndView index(HttpServletRequest request,EagleyeGroup eagleyeGroup)throws Exception{
		
		return new ModelAndView("/group/index");
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
	public Map<String,Object> querySimpleGroupList(HttpServletRequest request,DtPageBean<EagleyeGroup> pageBean,String sSearch) throws Exception{
		Map<String, Object> modelMap = new HashMap<String, Object>();
		pageBean = this.groupManageService.querySimpleGroupList(pageBean, sSearch);
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
			EagleyeGroup eagleyeGroup= this.groupManageService.getGroupById(id);
			request.setAttribute("group", eagleyeGroup);
		}
		return new ModelAndView("/group/show");
	}
	
	
	
	/**
	 * 新增页面
	 */
	@RequestMapping(value = "/add")
	public ModelAndView add(HttpServletRequest request)throws Exception{
		return new ModelAndView("/group/add");
	}
	
	/**
	 * 添加动作
	 * @param eagleyeGroup
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/create",method=RequestMethod.POST)
	@ResponseBody 
	public Map<String, Object> create(HttpServletRequest request,EagleyeGroup eagleyeGroup) throws Exception{
		Map<String, Object> modelMap = new HashMap<String, Object>();
		EagleyeGroup group = this.groupManageService.createGroup(eagleyeGroup);
		modelMap.put("flash", group.getFlash());
		return modelMap;
	}
	
	/**
	 * 进入修改页面
	 */
	@RequestMapping(value = "/{id}/edit")
	public ModelAndView edit(HttpServletRequest request,@PathVariable("id")String id)throws Exception{
		
		if(id!=null && !id.trim().equals("")){
			EagleyeGroup eagleyeGroup= this.groupManageService.getGroupById(id);
			request.setAttribute("group", eagleyeGroup);
		}
		return new ModelAndView("/group/edit");
	}
	
	
	/**
	 * 编辑动作
	 * @param eagleyeGroup
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/update",method=RequestMethod.POST)
	@ResponseBody 
	public Map<String, Object> update(HttpServletRequest request,EagleyeGroup eagleyeGroup) throws Exception{
		Map<String, Object> modelMap = new HashMap<String, Object>();
		EagleyeGroup group = this.groupManageService.updateGroup(eagleyeGroup);
		modelMap.put("flash", group.getFlash());
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
		if(this.groupManageService.delete(id) > 0){
			modelMap.put("flash", AppConstants.FLASH_SUCCESS);
		}else{
			modelMap.put("flash", AppConstants.FLASH_ERROR);
		}
		return modelMap;
	}
	
}