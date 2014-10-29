package com.yougou.eagleye.admin.domain;
 
 import java.util.Date;
 


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.admin.util.JsonDateSerializer;

/**
 * 用户组对象
 * @author panda
 *
 */
@Entity
@Table(name="eagleye_group")
public class EagleyeGroup implements java.io.Serializable{

	private static final long serialVersionUID = 7664382600656918528L;
	
	/**
	 * 主键ID
	 */
	private Integer id;

	
	/**
	 * 用户组名称
	 */
	private String groupName;
	

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
	
	private Set<EagleyeUserGroup> eagleyeUserGroupList;
	
	@OneToMany(fetch=FetchType.LAZY,mappedBy="eagleyeGroup",cascade={CascadeType.REMOVE})
	@OrderBy("id asc")
	@JsonIgnore
	public Set<EagleyeUserGroup> getEagleyeUserGroupList() {
		this.userGroupMap = new HashMap<String,String>(); 
		if(eagleyeUserGroupList!=null){
			for(EagleyeUserGroup userGroup : eagleyeUserGroupList){
				if(userGroup!=null){
					this.userGroupMap.put(userGroup.getUserAccount(), userGroup.getUserAccount());
				}
				
			}
		}
		return eagleyeUserGroupList;
	}

	public void setEagleyeUserGroupList(Set<EagleyeUserGroup> eagleyeUserGroupList) {
		this.eagleyeUserGroupList = eagleyeUserGroupList;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	@JsonProperty
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="group_name",length=100)
	@JsonProperty
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Column(name="description",length=500)
	@JsonProperty
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
	@JsonProperty
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
	@JsonProperty
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
	@JsonProperty
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
	
	/**
	 * 传递对应用户成员
	 */
	private String[] userAccounts;

	@Transient
	public String[] getUserAccounts() {
		return userAccounts;
	}
	
	public void setUserAccounts(String[] userAccounts) {
		this.userAccounts = userAccounts;
	}
	
	/**
	 * 对应的userGroup map,用来页面的select选择
	 */
	private Map<String,String> userGroupMap;

	@Transient
	public Map<String, String> getUserGroupMap() {
		return userGroupMap;
	}

	public void setUserGroupMap(Map<String, String> userGroupMap) {
		this.userGroupMap = userGroupMap;
	}
	
}
