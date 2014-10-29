package com.yougou.eagleye.admin.vo;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.admin.util.EagleyeAdminUtil;
import com.yougou.eagleye.core.security.encoder.Md5PwdEncoder;
import com.yougou.eagleye.trace.domain.Annotation;
import com.yougou.eagleye.trace.domain.BinaryAnnotation;
import com.yougou.eagleye.trace.domain.Span;
 


/**
 * 日志统计汇总
 * @author panda
 *
 */
public class TraceLogStatisticsVo implements java.io.Serializable{

	
	
	private static final long serialVersionUID = 7664382600656918528L;
    
    
    //method name
    private String spanName;
    
    // interface name
    private String serviceName;
    
    private String appName;
    
    private String traceId;
    
    /**
     * 消耗的总时间
     */
    private Long totleSpendTime = 0L;
    
    private String ip = "";
    
    /**
     * 调用次数
     */
    private Integer invokeCount = 0;

	public String getSpanName() {
		return spanName;
	}

	public void setSpanName(String spanName) {
		this.spanName = spanName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Long getTotleSpendTime() {
		return totleSpendTime;
	}

	public void setTotleSpendTime(Long totleSpendTime) {
		this.totleSpendTime = totleSpendTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getInvokeCount() {
		return invokeCount;
	}

	public void setInvokeCount(Integer invokeCount) {
		this.invokeCount = invokeCount;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}
    
    
	
}
