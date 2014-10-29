package com.yougou.eagleye.admin.domain;
 
 import java.util.Date;
 


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.admin.util.JsonDateSerializer;
import com.yougou.eagleye.core.utils.StrUtils;

/**
 * 应用对象
 * @author panda
 *
 */
@Entity
@Table(name="eagleye_app")
public class EagleyeApp implements java.io.Serializable{

	private static final long serialVersionUID = 7664382600656918528L;
	
	/**
	 * 主键ID
	 */
	private Integer id;

	
	/**
	 * 用户组名称
	 */
	private String appName;
	
	/**
	 * 对应的用户组[1][2][3]
	 */
	private String groupIds;
	

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
	

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="app_name",length=100)
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	
	@Column(name="group_ids",length=2000)
	public String getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
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
	
	/**
	 * 传递对应用户组成员
	 */
	private String[] groups;
	
	
	@Transient
	public String[] getGroups() {
		return groups;
	}

	public void setGroups(String[] groups) {
		this.groupIds = "";
		if(groups!=null){
			for(String group : groups){
				this.groupIds += "[" + group + "]";
			}
		}
		this.groups = groups;
	}

	
	/**
	 * 对应的group map,用来页面的select选择
	 */
	private Map<String,String> groupMap;

	@Transient
	public Map<String, String> getGroupMap() {
		
		return groupMap;
	}

	public void setGroupMap() {
		this.groupMap = new HashMap<String,String>(); 
		if(this.groupIds!=null){
			List<String> groupIdList = StrUtils.getBetweenArray("\\[","\\]",this.groupIds);
			for(String groupId : groupIdList){
				groupMap.put(groupId, groupId);
			}
		}
	}

	
	
	
}
