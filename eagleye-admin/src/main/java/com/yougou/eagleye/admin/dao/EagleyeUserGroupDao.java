package com.yougou.eagleye.admin.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.admin.domain.EagleyeUserGroup;
import com.yougou.eagleye.admin.util.DtPageBean;
import com.yougou.eagleye.core.dao.hibernate3.HibernateBaseDao;
import com.yougou.eagleye.core.utils.NumUtils;

@Repository("eagleyeUserGroupDao")
public class EagleyeUserGroupDao extends HibernateBaseDao<EagleyeUserGroup> {


    /**
     * 简单模糊检索查询
     *
     * @param  pageBean , String sSearch
     * @return pageBean
     * @throws Exception
     */
    public DtPageBean<EagleyeUserGroup> querySimplePageByGroupId(DtPageBean<EagleyeUserGroup> pageBean,
                                                                 String sSearch, String groupId) throws Exception {
        if (pageBean != null) {
            StringBuilder hql = new StringBuilder();
            hql.append("from EagleyeUserGroup u where u.dataStatus =:dataStatus");
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("dataStatus", AppConstants.DATASTATUS_VALID);
            if (groupId != null) {
                hql.append(" and u.groupId =:groupId");
                map.put("groupId", Integer.parseInt(groupId));
            }

            if (sSearch != null && !"".equals(sSearch)) {
                hql.append(" and CONCAT('");
                hql.append(" [',case when u.userAccount is null then '' else u.userAccount end,']");
                hql.append(" [',case when u.userPhone is null then '' else u.userPhone end,']");
                hql.append(" [',case when u.description is null then '' else u.description end,']");
                hql.append(" ') ");
                hql.append(" like :sSearch");
                map.put("sSearch", "%" + sSearch + "%");
            }
            hql.append(pageBean.getSortStr());
            this.queryPageByHql(hql.toString(), pageBean, map, "简单模糊检索");
        }
        return pageBean;
    }


    /**
     * 不带分页的查询
     *
     * @param eagleyeUserGroup
     * @return list
     */
    public List<EagleyeUserGroup> queryList(EagleyeUserGroup eagleyeUserGroup) {
        StringBuilder hql = new StringBuilder();
        hql.append("from EagleyeUserGroup u where u.dataStatus =:dataStatus");
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("dataStatus", AppConstants.DATASTATUS_VALID);
        if (eagleyeUserGroup != null) {
            //用户组Id
            Integer groupId = eagleyeUserGroup.getGroupId();
            if (groupId != null) {
                hql.append(" and u.groupId=:groupId");
                map.put("groupId", groupId);
            }

        }
        hql.append(" order by u.id desc");
        List<EagleyeUserGroup> list = this.queryByHql(hql.toString(), map, "不带分页的检索", 100);
        return list;
    }

    /**
     * 通过groupId的集合查询usergroup对象集合
     *
     * @param groupIds
     * @return list
     */
    public List<EagleyeUserGroup> queryListByGroupIds(Object[] groupIds) {
        StringBuilder hql = new StringBuilder();
        List<EagleyeUserGroup> list = null;
        if (groupIds != null) {
            hql.append("from EagleyeUserGroup u where u.dataStatus =:dataStatus");
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("dataStatus", AppConstants.DATASTATUS_VALID);
            hql.append(" and u.groupId in (:groupIds)");
            map.put("groupIds", groupIds);
            list = this.queryByHql(hql.toString(), map, "通过groupId的集合查询usergroup对象集合", 1000);
        }
        return list;
    }


    /**
     * 根据ID获取对象
     *
     * @param id
     * @return
     */
    public EagleyeUserGroup getEagleyeUserGroupById(String id) {
        EagleyeUserGroup userGroup = null;
        if (id != null && !id.trim().equals("")) {
            //查询单个对象
            String hql = "from EagleyeUserGroup t where t.dataStatus =:dataStatus and t.id=:userGroupId order by t.id asc";
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("dataStatus", AppConstants.DATASTATUS_VALID);
            map.put("userGroupId", Integer.parseInt(id));
            userGroup = this.uniqueQueryByHql(hql, map, "根据ID查询对象");
        }
        return userGroup;
    }


    /**
     * 添加或修改一个对象
     *
     * @param eagleyeUserGroup
     * @return
     */
    public EagleyeUserGroup createOrUpdate(EagleyeUserGroup eagleyeUserGroup) {
        if (eagleyeUserGroup != null) {
            this.save(eagleyeUserGroup);
        }
        return eagleyeUserGroup;
    }

    /**
     * 删除一个对象 物理删除
     *
     * @param id
     * @return
     */
    public int delete(String id) {
        if (id != null && !id.trim().equals("")) {
            String hql = "update EagleyeUserGroup u set u.dataStatus=:dataStatus where u.id=:id";
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("dataStatus", AppConstants.DATASTATUS_DEL);
            map.put("id", Integer.parseInt(id));
            return this.excuteByHql(hql, map, "逻辑删除");
        }
        return 0;
    }

    /**
     * 删除多个对象
     *
     * @param ids
     * @return
     */
    public int deleteMultiple(String[] ids) {
        if (ids != null && ids.length > 0) {
            String hql = "delete EagleyeUserGroup u where u.id in (:ids)";
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ids", NumUtils.strsToInts(ids));
            return this.excuteByHql(hql, map, "物理批量删除");
        }
        return 0;
    }


    /**
     * 验证数据库中是否有重复的数据
     *
     * @param eagleyeUserGroup
     * @return false 没有重复 ,
     * @throws Exception
     */
    public boolean verifyRepeat(EagleyeUserGroup eagleyeUserGroup) throws Exception {
        List<EagleyeUserGroup> list = null;
        if (eagleyeUserGroup != null) {
            StringBuilder hql = new StringBuilder();
            hql.append("from EagleyeUserGroup u where u.dataStatus =:dataStatus");
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("dataStatus", AppConstants.DATASTATUS_VALID);

            if (eagleyeUserGroup.getGroupId() != null) {
                hql.append(" and u.groupId=:groupId");
                map.put("groupId", eagleyeUserGroup.getGroupId());
            }

//	 		String userAccount = eagleyeUserGroup.getUserAccount();
//	 		if(userAccount!=null){
//	 			hql.append(" and u.userAccount=:userAccount");
//	 			map.put("userAccount", userAccount);
//	 		}else{
//	 			hql.append(" and 1=2");
//	 		}
//切换成从数据库查询
            String userId = eagleyeUserGroup.getUserId();
            if (StringUtils.isNotBlank(userId)) {
                hql.append(" and u.userId=:userId");
                map.put("userId", userId);
            } else {
                hql.append(" and 1=2");
            }

            Integer id = eagleyeUserGroup.getId();
            if (id != null) {
                hql.append(" and u.id<>:id");
                map.put("id", id);
            }

            list = this.queryByHql(hql.toString(), map, "不带分页的检索", 1);
        }

        if (list == null || list.size() == 0) {//没有重复的
            return false;
        } else {
            return true;
        }
    }

    /**
     * 更新用户信息
     *
     * @param userId
     * @param userAccount
     * @param userPhone
     * @return
     */
    public Integer updateUserInfo(Integer userId, String userAccount, String userPhone) {
        if (userId == null || StringUtils.isBlank(userAccount) || StringUtils.isBlank(userPhone)) {
            return 0;
        }
        String hql = "update EagleyeUserGroup u set u.userAccount=:userAccount,u.userPhone=:userPhone" +
                " where u.userId=:userId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("userAccount", userAccount);
        map.put("userPhone", userPhone);
        map.put("userId", userId.toString());
        return this.excuteByHql(hql, map, "更新用户信息");
    }
}