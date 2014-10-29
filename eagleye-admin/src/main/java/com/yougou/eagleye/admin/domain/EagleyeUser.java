/*
 * 类名 EagleyeUser.java
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
package com.yougou.eagleye.admin.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.admin.util.JsonDateSerializer;

import javax.persistence.*;
import java.util.Date;

/**
 * Class description goes here
 * Created by ylq on 14-10-8 下午4:51.
 */
@Entity
@Table(name = "eagleye_user")
public class EagleyeUser implements java.io.Serializable {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 用户账号，要填写邮箱
     */
    private String userAccount;
    /**
     * 用户手机
     */
    private String userPhone;
    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后修改时间
     */
    private Date updateTime;

    /**
     * 数据状态: 0:删除 1:生效  2:无效
     */
    private String dataStatus;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "user_account", length = 64)
    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    @Column(name = "user_phone", length = 32)
    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    @Column(name = "user_name", length = 32)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @Column(name = "create_time", length = 19)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @Column(name = "update_time", length = 19)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "data_status", length = 4)
    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }


    /*********************************不需要持久化的属性**************************************/
    /**
     * 传递操作状态信息
     */
    private String flash = AppConstants.FLASH_SUCCESS;

    @Transient
    public String getFlash() {
        return flash;
    }

    public void setFlash(String flash) {
        this.flash = flash;
    }
}
