package com.yougou.eagleye.admin.domain;
 

/**
 * 预警对象, 用来在redis中存储预警信息
 * @author panda
 *
 */
public class EagleyeAlert implements java.io.Serializable{

	private static final long serialVersionUID = 7664382600656918528L;
	
	
	/**
	 * 用户组名称
	 */
	private String appName;
	
	/**
	 * 预警关键字
	 */
	private String keyword;
	
	/**
	 * 预警邮件列表, 用 ";" 分割
	 */
	private String emails;
	
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getEmails() {
		return emails;
	}

	public void setEmails(String emails) {
		this.emails = emails;
	}

	public String getMobiles() {
		return mobiles;
	}

	public void setMobiles(String mobiles) {
		this.mobiles = mobiles;
	}

	/**
	 * 预警手机号列表, 用";"分割
	 */
	private String mobiles;
	
	
	
	
	
	
}
