/*
 * 类名 UserManageService.java
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
package com.yougou.eagleye.admin.services;

import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.admin.dao.EagleyeUserDao;
import com.yougou.eagleye.admin.domain.EagleyeUser;
import com.yougou.eagleye.admin.util.DtPageBean;
import com.yougou.eagleye.core.reflection.ReflectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Class description goes here
 * Created by ylq on 14-10-8 下午5:21.
 */
@Service("userManageService")
@Transactional
public class UserManageService {
    @Autowired
    private EagleyeUserDao eagleyeUserDao;
    @Autowired
    private UserGroupManageService userGroupManageService;

    /**
     * 简单查询
     *
     * @param pageBean
     * @param sSearch
     * @return
     */
    public DtPageBean<EagleyeUser> querySimpleUserList(DtPageBean<EagleyeUser> pageBean, String sSearch) {
        pageBean = eagleyeUserDao.querySimpleUserList(pageBean, sSearch);
        return pageBean;
    }

    /**
     * @param eagleyeUser
     * @return
     */
    public EagleyeUser createUser(EagleyeUser eagleyeUser) {
        if (eagleyeUser == null)
            return eagleyeUser;
        //判断空
        if (StringUtils.isBlank(eagleyeUser.getUserAccount())
                || StringUtils.isBlank(eagleyeUser.getUserName())
                || StringUtils.isBlank(eagleyeUser.getUserPhone())) {
            eagleyeUser.setFlash(AppConstants.FLASH_ERROR);
            return eagleyeUser;
        }
        //验证重复，只验证邮箱,此处是添加，只需要判断是否存在该邮箱，不需要判断是否是同一账号
        EagleyeUser user = eagleyeUserDao.getEagleyeUserByAccount(eagleyeUser.getUserAccount());
        if (user != null) {
            eagleyeUser.setFlash(AppConstants.FLASH_REPEAT);
            return eagleyeUser;
        }
        eagleyeUser.setDataStatus(AppConstants.DATASTATUS_VALID);
        eagleyeUser.setCreateTime(new Date());
        eagleyeUser.setUpdateTime(new Date());
        //保存到数据库
        eagleyeUserDao.save(eagleyeUser);
        return eagleyeUser;
    }

    /**
     * @param userAccount
     * @return
     */
    public EagleyeUser getEagleyeUserByAccount(String userAccount) {
        return eagleyeUserDao.getEagleyeUserByAccount(userAccount);
    }

    /**
     * 逻辑删除用户
     *
     * @param id
     * @return
     */
    public Integer delete(String id) {
        return eagleyeUserDao.delete(id);
    }

    /**
     * 根据id获取用户信息
     *
     * @param id
     * @return
     */
    public EagleyeUser getUserById(String id) {
        return eagleyeUserDao.getUserById(Integer.parseInt(id));
    }

    /**
     * 更新用户
     *
     * @param eagleyeUser
     * @return
     */
    public EagleyeUser updateUser(EagleyeUser eagleyeUser) throws Exception {
        if (eagleyeUser == null)
            return eagleyeUser;
        //判断空
        if (StringUtils.isBlank(eagleyeUser.getUserAccount())
                || StringUtils.isBlank(eagleyeUser.getUserName())
                || StringUtils.isBlank(eagleyeUser.getUserPhone())) {
            eagleyeUser.setFlash(AppConstants.FLASH_ERROR);
            return eagleyeUser;
        }
        //验证重复，只验证邮箱,此处是修改，既要判断是否存在该邮箱，还需要判断是否是同一账号
        EagleyeUser user = eagleyeUserDao.getEagleyeUserByAccount(eagleyeUser.getUserAccount());
        if (user == null) {
            //改了邮箱
            user = eagleyeUserDao.getUserById(eagleyeUser.getId());
        } else if (!user.getId().equals(eagleyeUser.getId())) {
            //没有修改邮箱，和别的账号邮箱重复了
            eagleyeUser.setFlash(AppConstants.FLASH_REPEAT);
            return eagleyeUser;
        }
        eagleyeUser.setFlash(AppConstants.FLASH_SUCCESS);
        eagleyeUser.setUpdateTime(new Date());
        //暂存更新前的数据
        String oldUserAccount = user.getUserAccount();
        String oldUserPhone = user.getUserPhone();
        ReflectionUtils.mergeObject(user, eagleyeUser);
        //保存到数据库
        eagleyeUser = eagleyeUserDao.save(user);
        if (!eagleyeUser.getUserAccount().equals(oldUserAccount)
                || !eagleyeUser.getUserPhone().equals(oldUserPhone)) {
            //修改了邮箱或者手机，需要更新用户组信息和预警规则
            userGroupManageService.updateUserInfo(user.getId(),user.getUserAccount(),user.getUserPhone());
        }
        return eagleyeUser;
    }

    /**
     * @return
     */
    public List<EagleyeUser> getAllEagleyeUser() {
        return eagleyeUserDao.getAllEagleyeUser();
    }
}
