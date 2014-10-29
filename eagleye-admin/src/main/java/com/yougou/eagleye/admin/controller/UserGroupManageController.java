
package com.yougou.eagleye.admin.controller;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

import com.yougou.eagleye.admin.services.UserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.admin.domain.EagleyeUserGroup;
import com.yougou.eagleye.admin.services.UserGroupManageService;
import com.yougou.eagleye.admin.util.DtPageBean;

@Controller
@RequestMapping("/usergroup")
public class UserGroupManageController extends BaseController {

    @Autowired
    private UserGroupManageService userGroupManageService;
    @Autowired
    private UserManageService userManageService;

    /**
     * 打开管理页面
     */
    @RequestMapping(value = "/{groupId}/index")
    public ModelAndView index(HttpServletRequest request, @PathVariable("groupId") String groupId) throws Exception {
        request.setAttribute("groupId", groupId);
        return new ModelAndView("/usergroup/index");
    }

    /**
     * 简单查询
     *
     * @param pageBean
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{groupId}/querySimple")
    @ResponseBody
    public Map<String, Object> querySimpleByGroupId(HttpServletRequest request, DtPageBean<EagleyeUserGroup> pageBean, String sSearch, @PathVariable("groupId") String groupId) throws Exception {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        pageBean = this.userGroupManageService.querySimpleUserGroupListByGroupId(pageBean, sSearch, groupId);
        this.formatDtPageBean(modelMap, pageBean);
        return modelMap;
    }


    /**
     * 查看用户详情
     *
     * @param request
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/show/{id}")
    public ModelAndView show(HttpServletRequest request, @PathVariable("id") String id) throws Exception {
        if (id != null && !id.trim().equals("")) {
            EagleyeUserGroup eagleyeUserGroup = this.userGroupManageService.getUserGroupById(id);
            request.setAttribute("userGroup", eagleyeUserGroup);
            request.setAttribute("user", userManageService.getUserById(eagleyeUserGroup.getUserId()));

        }
        return new ModelAndView("/usergroup/show");
    }


    /**
     * 新增页面
     */
    @RequestMapping(value = "/{groupId}/add")
    public ModelAndView add(HttpServletRequest request, @PathVariable("groupId") String groupId) throws Exception {
        request.setAttribute("groupId", groupId);
        request.setAttribute("userList", userManageService.getAllEagleyeUser());
        return new ModelAndView("/usergroup/add");
    }

    /**
     * 添加动作
     *
     * @param eagleyeRule
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> create(HttpServletRequest request, EagleyeUserGroup eagleyeUserGroup) throws Exception {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        EagleyeUserGroup userGroup = this.userGroupManageService.createUserGroup(eagleyeUserGroup);
        modelMap.put("flash", userGroup.getFlash());
        return modelMap;
    }


    /**
     * 进入修改页面
     */
    @RequestMapping(value = "/{id}/edit")
    public ModelAndView edit(HttpServletRequest request, @PathVariable("id") String id) throws Exception {

        if (id != null && !id.trim().equals("")) {
            EagleyeUserGroup eagleyeUserGroup = this.userGroupManageService.getUserGroupById(id);
            request.setAttribute("userGroup", eagleyeUserGroup);
            request.setAttribute("userList", userManageService.getAllEagleyeUser());
        }
        return new ModelAndView("/usergroup/edit");
    }


    /**
     * 编辑动作
     *
     * @param eagleyeUserGroup
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> update(HttpServletRequest request, EagleyeUserGroup eagleyeUserGroup) throws Exception {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        EagleyeUserGroup userUserGroup = this.userGroupManageService.updateUserGroup(eagleyeUserGroup);
        modelMap.put("flash", userUserGroup.getFlash());
        return modelMap;
    }


    /**
     * 删除用户 逻辑删除
     *
     * @param request
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/destroy/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> destroy(HttpServletRequest request, @PathVariable("id") String id) throws Exception {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (this.userGroupManageService.delete(id) > 0) {
            modelMap.put("flash", AppConstants.FLASH_SUCCESS);
        } else {
            modelMap.put("flash", AppConstants.FLASH_ERROR);
        }
        return modelMap;
    }

}