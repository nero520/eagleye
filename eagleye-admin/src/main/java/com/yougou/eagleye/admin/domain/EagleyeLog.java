package com.yougou.eagleye.admin.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;

 


/**
 * 应用日志对象
 * @author panda
 *
 */
@Document(indexName = "applogs",type = "log" , shards = 1, replicas = 1, indexStoreType = "fs", refreshInterval = "-1")
public class EagleyeLog implements java.io.Serializable{

	private static final long serialVersionUID = 7664382600656918528L;
	
	@Id
	private String id;
	
	private String appGroup;
	
	



	private String appName;
	
	private String body;
	
	@Version
	private Long version;
	
	private String timestamp;
	
	public String getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}


	public String getAppGroup() {
		return appGroup;
	}


	public void setAppGroup(String appGroup) {
		this.appGroup = appGroup;
	}
	
	
	/**
	 * 构造函数必须要有
	 */
	public EagleyeLog(){}


	public Long getVersion() {
		return version;
	}


	public void setVersion(Long version) {
		this.version = version;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}


	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	
	
	public static EagleyeLog msg2AlertLog(String msg, String id){
		EagleyeLog el = null;
		if(msg!=null){
			el = new EagleyeLog();
			String[] strs = msg.split("#");
			el.setId(id);
			el.setAppGroup(strs[2].replaceAll("-", "_"));
			el.setAppName(strs[3].replaceAll("-", "_"));
			el.setTimestamp(strs[4]);
			el.setVersion(System.currentTimeMillis());
			el.setBody(strs[strs.length-1]);
		}
		return el;
	}
	
}
