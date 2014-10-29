/*
 * 类名 EagleyeUserDao.java
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
package com.yougou.eagleye.admin.dao;

import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.admin.domain.EagleyeUser;
import com.yougou.eagleye.admin.util.DtPageBean;
import com.yougou.eagleye.core.dao.hibernate3.HibernateBaseDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * Class description goes here
 * Created by ylq on 14-10-8 下午5:12.
 */
@Repository("eagleyeUserDao")
public class EagleyeUserDao extends HibernateBaseDao<EagleyeUser> {

    public DtPageBean<EagleyeUser> querySimpleUserList(DtPageBean<EagleyeUser> pageBean, String sSearch) {
        if (pageBean == null) {
            return pageBean;
        }
        StringBuilder hql = new StringBuilder();
        hql.append("from EagleyeUser u where u.dataStatus =:dataStatus");
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("dataStatus", AppConstants.DATASTATUS_VALID);
        if (StringUtils.isNotBlank(sSearch)) {
            hql.append(" and CONCAT('");
            hql.append(" [',u.userAccount,']");
            hql.append(" [',u.userName,']");
            hql.append(" [',u.userPhone,']");
            hql.append(" ') ");
            hql.append(" like :sSearch");
            map.put("sSearch", "%" + sSearch + "%");
        }
        hql.append(pageBean.getSortStr());
        this.queryPageByHql(hql.toString(), pageBean, map, "简单模糊检索");
        return pageBean;
    }

    /**
     * 更具账号获取用户信息
     *
     * @param userAccount
     * @return
     */
    public EagleyeUser getEagleyeUserByAccount(String userAccount) {
        if (StringUtils.isBlank(userAccount)) {
            return null;
        }
        StringBuilder hql = new StringBuilder();
        hql.append("from EagleyeUser u where u.dataStatus =:dataStatus");
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("dataStatus", AppConstants.DATASTATUS_VALID);
        hql.append(" and u.userAccount=:userAccount");
        map.put("userAccount", userAccount);
        return this.uniqueQueryByHql(hql.toString(), map, "根据账号查询对象");
    }

    /**
     * 逻辑删除用户
     *
     * @param id
     * @return
     */
    public Integer delete(String id) {
        if (StringUtils.isBlank(id)) {
            return 0;
        }
        String hql = "update EagleyeUser u set u.dataStatus=:dataStatus where u.id=:id";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("dataStatus", AppConstants.DATASTATUS_DEL);
        map.put("id", Integer.parseInt(id));
        return this.excuteByHql(hql, map, "逻辑删除");
    }

    /**
     * 根据id获取用户信息
     *
     * @param id
     * @return
     */
    public EagleyeUser getUserById(Integer id) {
        if (id == null) {
            return null;
        }
        StringBuilder hql = new StringBuilder();
        hql.append("from EagleyeUser u where u.dataStatus =:dataStatus");
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("dataStatus", AppConstants.DATASTATUS_VALID);
        hql.append(" and u.id=:id");
        map.put("id", id);
        return this.uniqueQueryByHql(hql.toString(), map, "根据Id查询对象");
    }

    /**
     * 获取所有的可用用户
     *
     * @return
     */
    public List<EagleyeUser> getAllEagleyeUser() {
        StringBuilder hql = new StringBuilder();
        hql.append("from EagleyeUser u where u.dataStatus =:dataStatus");
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("dataStatus", AppConstants.DATASTATUS_VALID);
        hql.append(" order by u.id desc");
        //最多查询200个用户
        return this.queryByHql(hql.toString(), map, "不带分页查询", 200);
    }
}
