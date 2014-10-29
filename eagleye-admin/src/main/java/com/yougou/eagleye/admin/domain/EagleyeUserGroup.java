package com.yougou.eagleye.admin.domain;
 
 import java.util.Date;
 





import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.admin.util.JsonDateSerializer;

/**
 * 用户组对象
 * @author panda
 *
 */
@Entity
@Table(name="eagleye_user_group")
public class EagleyeUserGroup implements java.io.Serializable{

	private static final long serialVersionUID = 7664382600656918528L;
	
	/**
	 * 主键ID
	 */
	private Integer id;
	
	private Integer groupId;
	
	private String userId;
	
	/**
	 * 预警类型, n:不发送 m:手机发送 e:邮件发送  me:手机,邮件都发送
	 */
	private String warnType;
	
	/**
	 * 用户账号
	 */
	private String userAccount;
	
	/**
	 * 用户手机
	 */
	private String userPhone;
	

	/**
	 * 描述
	 */
	private String description;
	
	/**
	 * 数据状态: 0:删除 1:生效  2:无效
	 */
	private String dataStatus;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 最后修改时间
	 */
	private Date updateTime;
	
	private EagleyeGroup eagleyeGroup;
	
	@ManyToOne
	@JoinColumn(name="group_id",insertable=false,updatable=false)
	public EagleyeGroup getEagleyeGroup() {
		return eagleyeGroup;
	}

	public void setEagleyeGroup(EagleyeGroup eagleyeGroup) {
		this.eagleyeGroup = eagleyeGroup;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	@Column(name="group_id",length=500)
	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	@Column(name="user_id",length=500)
	public String getUserId() {
		return userId;
	}

	/**
	 * 对应user的uuid
	 * @param userId
	 */
	@Column(name="user_id",length=100)
	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name="warn_type",length=10)
	public String getWarnType() {
		return warnType;
	}

	
	public void setWarnType(String warnType) {
		this.warnType = warnType;
	}

	@Column(name="user_account",length=100)
	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	
	@Column(name="user_phone",length=20)
	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	@Column(name="description",length=500)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	

	/**
	 * 数据状态: 0:删除 1:生效  2:无效
	 */
	@Column(name="data_status",length=10)
	public String getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}

	/**
	 * 创建时间
	 * @return
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using=JsonDateSerializer.class)
	@Column(name="create_time",length=19)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	
	/**
	 * 最后修改时间
	 * @return
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using=JsonDateSerializer.class)
	@Column(name="update_time",length=19)
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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
