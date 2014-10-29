package com.yougou.eagleye.admin.services;


import java.util.Date;
import java.util.List;

import com.yougou.eagleye.admin.domain.EagleyeUser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.admin.dao.EagleyeUserGroupDao;
import com.yougou.eagleye.admin.domain.EagleyeUserGroup;
import com.yougou.eagleye.admin.util.DtPageBean;
import com.yougou.eagleye.core.reflection.ReflectionUtils;

@Service("userGroupManageService")
@Transactional
public class UserGroupManageService {

    private final static Logger logger = LoggerFactory.getLogger(UserGroupManageService.class);

    @Autowired
    private EagleyeUserGroupDao eagleyeUserGroupDao;

    @Autowired
    private AlertManageService alertManageService;

    @Autowired
    private UserManageService userManageService;

    /**
     * 根据id获得userGroup
     *
     * @param id
     * @return 返回查询到的EagleyeuserGroup对象
     */
    public EagleyeUserGroup getUserGroupById(String id) throws Exception {
        EagleyeUserGroup userGroup = eagleyeUserGroupDao.getEagleyeUserGroupById(id);
        return userGroup;
    }


    /**
     * 新增
     *
     * @param eagleyeUserGroup 待更新的对象
     * @return 返回更新后的对象
     */
    public EagleyeUserGroup createUserGroup(EagleyeUserGroup eagleyeUserGroup) throws Exception {
        if (eagleyeUserGroup != null) {
            eagleyeUserGroup.setDataStatus(AppConstants.DATASTATUS_VALID);
            eagleyeUserGroup.setCreateTime(new Date());
            eagleyeUserGroup.setUpdateTime(new Date());
            //需要排重
            if (eagleyeUserGroupDao.verifyRepeat(eagleyeUserGroup)) {//有重复数据
                eagleyeUserGroup.setFlash(AppConstants.FLASH_REPEAT);
            } else {//没有重复的
                EagleyeUser eagleyeUser = userManageService.getUserById(eagleyeUserGroup.getUserId());
                eagleyeUserGroup.setUserAccount(eagleyeUser.getUserAccount());
                eagleyeUserGroup.setUserPhone(eagleyeUser.getUserPhone());
                eagleyeUserGroup = eagleyeUserGroupDao.createOrUpdate(eagleyeUserGroup);
            }

        }

        //更新redis预警规则
        this.alertManageService.updateRuleListToCache();
        return eagleyeUserGroup;
    }


    /**
     * 更新
     *
     * @param 页面传入的对象
     * @return 返回更新后的用户对象
     */
    public EagleyeUserGroup updateUserGroup(EagleyeUserGroup pageParam) throws Exception {
        EagleyeUserGroup userGroup = null;
        if (pageParam != null) {
            //需要排重
            if (eagleyeUserGroupDao.verifyRepeat(pageParam)) {//有重复数据
                pageParam.setFlash(AppConstants.FLASH_REPEAT);
                userGroup = pageParam;
            } else {//没有重复的
                userGroup = eagleyeUserGroupDao.getEagleyeUserGroupById(pageParam.getId() + "");
                EagleyeUser eagleyeUser = userManageService.getUserById(pageParam.getUserId());
                pageParam.setUserAccount(eagleyeUser.getUserAccount());
                pageParam.setUserPhone(eagleyeUser.getUserPhone());
                ReflectionUtils.mergeObject(userGroup, pageParam);
                userGroup.setUpdateTime(new Date());

                userGroup = eagleyeUserGroupDao.createOrUpdate(userGroup);
            }
        }
        //更新redis预警规则
        this.alertManageService.updateRuleListToCache();
        return userGroup;
    }


    /**
     * 根据groupId获取userGroup列表
     *
     * @param groupId
     * @return list
     */
    public List<EagleyeUserGroup> getListByGroupId(String groupId) throws Exception {
        List<EagleyeUserGroup> userGroupList = null;
        if (groupId != null) {
            EagleyeUserGroup eagleyeUserGroup = new EagleyeUserGroup();
            userGroupList = this.eagleyeUserGroupDao.queryList(eagleyeUserGroup);
        }
        return userGroupList;
    }


    /**
     * 删除
     *
     * @param id 待删除的用户对象的id
     */
    public int delete(String id) throws Exception {
        int resultCount = eagleyeUserGroupDao.delete(id);
        //更新redis预警规则
        this.alertManageService.updateRuleListToCache();
        return resultCount;
    }


    /**
     * 删除多个
     *
     * @param ids 待删除的数据的id
     */
    public int deleteMultiple(String ids) throws Exception {
        if (ids != null && !ids.trim().equals("")) {
            //不物理删除
            if (ids.contains(",")) {
                String[] idsArray = ids.split(",");
                return this.eagleyeUserGroupDao.deleteMultiple(idsArray);
            } else {
                return this.eagleyeUserGroupDao.delete(ids);
            }
        }
        return 0;
    }


    /**
     * 删除某个groupId所有的usergroup数据
     *
     * @param groupId
     * @return
     * @throws Exception
     */
    public int deleteByGroupId(String groupId) throws Exception {
        List<EagleyeUserGroup> userGroupList = this.getListByGroupId(groupId);
        String ids = "0";
        if (userGroupList != null && userGroupList.size() != 0) {
            for (EagleyeUserGroup userGroup : userGroupList) {
                ids += "," + userGroup.getId();
            }
        }
        //更新redis预警规则
        this.alertManageService.updateRuleListToCache();
        return this.deleteMultiple(ids);
    }


    /**
     * 简单模糊检索
     *
     * @param DtPageBean pageBean , String sSearch, String groupId 所属用户组
     * @return pageBean
     * @throws Exception
     */
    public DtPageBean<EagleyeUserGroup> querySimpleUserGroupListByGroupId(DtPageBean<EagleyeUserGroup> pageBean, String sSearch, String groupId) throws Exception {
        pageBean = this.eagleyeUserGroupDao.querySimplePageByGroupId(pageBean, sSearch, groupId);
        return pageBean;
    }

    /**
     * 更新用户组的用户信息
     *
     * @param userId
     * @param userAccount
     * @param userPhone
     */
    public void updateUserInfo(Integer userId, String userAccount, String userPhone) throws Exception {
        Integer res = eagleyeUserGroupDao.updateUserInfo(userId, userAccount, userPhone);
        //更新预警规则
        if (res > 0) {
            this.alertManageService.updateRuleListToCache();
        }
    }
}