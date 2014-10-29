/*
 * 类名 UserManageController.java
 *
 * 版本信息 
 *
 * 日期 2014年3月21日
 *
 * 版权声明Copyright (C) 2014 YouGou Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为优购科技开发研制，未经本公司正式书面同意，其他任何个人、团体不得
 * 使用、复制、修改或发布本软件。
 */
package com.yougou.eagleye.admin.controller;

import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.admin.domain.EagleyeUser;
import com.yougou.eagleye.admin.services.UserManageService;
import com.yougou.eagleye.admin.util.DtPageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Class description goes here
 * Created by ylq on 14-10-8 下午4:25.
 */
@Controller
@RequestMapping("/user")
public class UserManageController extends BaseController {
    @Autowired
    private UserManageService userManageService;

    /**
     * 打开管理页面
     */
    @RequestMapping(value = "/index")
    public ModelAndView index() throws Exception {
        return new ModelAndView("/user/index");
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
    public Map<String, Object> querySimpleUserList(DtPageBean<EagleyeUser> pageBean, String sSearch) throws Exception {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        pageBean = userManageService.querySimpleUserList(pageBean, sSearch);
        this.formatDtPageBean(modelMap, pageBean);
        return modelMap;
    }

    /**
     * 新增页面
     */
    @RequestMapping(value = "/add")
    public ModelAndView add() throws Exception {
        return new ModelAndView("/user/add");
    }

    /**
     * 添加动作
     *
     * @param eagleyeUser
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> create(EagleyeUser eagleyeUser) throws Exception {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        EagleyeUser user = userManageService.createUser(eagleyeUser);
        modelMap.put("flash", user.getFlash());
        return modelMap;
    }

    /**
     * 删除用户 逻辑删除
     *
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/destroy/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> destroy(@PathVariable("id") String id) throws Exception {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (userManageService.delete(id) > 0) {
            modelMap.put("flash", AppConstants.FLASH_SUCCESS);
        } else {
            modelMap.put("flash", AppConstants.FLASH_ERROR);
        }
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
            EagleyeUser eagleyeUser = userManageService.getUserById(id);
            request.setAttribute("user", eagleyeUser);
        }
        return new ModelAndView("/user/show");
    }

    /**
     * 查看用户详情
     *
     * @param request
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/{id}/edit")
    public ModelAndView edit(HttpServletRequest request, @PathVariable("id") String id) throws Exception {
        if (id != null && !id.trim().equals("")) {
            EagleyeUser eagleyeUser = userManageService.getUserById(id);
            request.setAttribute("user", eagleyeUser);
        }
        return new ModelAndView("/user/edit");
    }

    /**
     * 编辑动作
     *
     * @param eagleyeUser
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> update(EagleyeUser eagleyeUser) throws Exception {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        EagleyeUser user = userManageService.updateUser(eagleyeUser);
        modelMap.put("flash", user.getFlash());
        return modelMap;
    }
}
