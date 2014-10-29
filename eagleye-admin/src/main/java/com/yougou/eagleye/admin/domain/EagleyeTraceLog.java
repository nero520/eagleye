package com.yougou.eagleye.admin.domain;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;

import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.trace.domain.Annotation;
import com.yougou.eagleye.trace.domain.BinaryAnnotation;
import com.yougou.eagleye.trace.domain.Span;
 


/**
 * 应用日志对象
 * @author panda
 *
 */
@Document(indexName = "tracelogs*",type = "log" , shards = 1, replicas = 1, indexStoreType = "fs", refreshInterval = "-1")
public class EagleyeTraceLog implements java.io.Serializable{

private static final long serialVersionUID = 7664382600656918528L;
	
//	@Id
	private String id;
	
	private String _id;
	
	

	private String traceId;
    
    private String spanId;
    
    private String parentId = "0"; //optional
    
    //method name
    private String spanName;
    
    // interface name
    private String serviceName;
    
    private String appName;
    
    private String startTime;
    
    private String spendTime = "00000000";
    
    private String ip;
    
    private String invokeType;
    
    private String exceptions;
    
    private String exceptionNum = "0";
    
    private String statistic = "";
    
    @Version
	private Long version;
	 
	public String getStartTime() {
		return startTime;
	}


	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}


	public String getSpendTime() {
		return spendTime;
	}


	public void setSpendTime(String spendTime) {
		this.spendTime = spendTime;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public String getInvokeType() {
		return invokeType;
	}


	public void setInvokeType(String invokeType) {
		this.invokeType = invokeType;
	}

	public String get_id() {
		return _id;
	}


	public void set_id(String _id) {
		this._id = _id;
	}
	
	
	public String getExceptions() {
		return exceptions;
	}


	public void setExceptions(String exceptions) {
		this.exceptions = exceptions;
	}


	public String getSpanId() {
		return spanId;
	}


	public void setSpanId(String spanId) {
		this.spanId = spanId;
	}
	
	
	public String getTraceId() {
		return traceId;
	}


	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}


	public String getParentId() {
		return parentId;
	}


	public void setParentId(String parentId) {
		this.parentId = parentId;
	}


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


	public String getExceptionNum() {
		return exceptionNum;
	}


	public void setExceptionNum(String exceptionNum) {
		this.exceptionNum = exceptionNum;
	}
	
	


	public String getAppName() {
		return appName;
	}


	public void setAppName(String appName) {
		this.appName = appName;
	}


	/**
	 * 构造函数必须要有
	 */
	public EagleyeTraceLog(){}


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
	
	
	public String getStatistic() {
		return statistic;
	}


	public void setStatistic(String statistic) {
		this.statistic = statistic;
	}


	public static EagleyeTraceLog span2TraceLog(Span span, String id){
		EagleyeTraceLog etl = null;
		if(span!=null){
			String invokeType = AppConstants.DUBBO_INVOKE_TYPE_CONSUMER; //默认是消费方
			String ip = "";
			long startTime = 0;
			long spendTime = 0;
			List<Annotation> annotations = span.getAnnotations();
			if(annotations!=null && annotations.size()==2){
				Annotation a0 = annotations.get(0);
				//如果包含c则是消费端,不包含为提供方
				if(a0.getValue().indexOf("c") == -1){
					invokeType = AppConstants.DUBBO_INVOKE_TYPE_PROVIDER;
				}
				ip = a0.getIp();
				startTime = a0.getTimestamp();
				spendTime = annotations.get(1).getTimestamp() - startTime;  
			}
			List<BinaryAnnotation> binaryAnnotations = span.getBinaryAnnotations();
			etl = new EagleyeTraceLog();
			etl.setId(id);
			etl.setSpanId(span.getId() + "");
			etl.setParentId(span.getParentId()+"");
			etl.setServiceName(span.getServiceName());
			etl.setSpanName(span.getSpanName());
			etl.setAppName(span.getAppName());
			etl.setTraceId(span.getTraceId() + "");
			etl.setVersion(System.currentTimeMillis());
			etl.setInvokeType(invokeType);
			etl.setIp(ip);
			etl.setStartTime(startTime+"");
			NumberFormat formatter = new DecimalFormat("00000000");
			etl.setSpendTime(formatter.format(spendTime));
			formatter = null;
			String exceptions = "";
			if(binaryAnnotations!=null){
				for(BinaryAnnotation ba : binaryAnnotations){
					exceptions += "#$@" + ba.getValue() + "#$@";
				}
				etl.setExceptionNum(binaryAnnotations.size()+"");
			}
			etl.setExceptions(exceptions);
			etl.setStatistic(span.getAppName()==null?"":span.getAppName().replaceAll("-", "_")+":"+span.getServiceName()+":"+span.getSpanName());
			annotations = null;
			binaryAnnotations = null;
		}
		return etl;
	}
	
	public static XContentBuilder span2Esdoc(Span span , String id){
		XContentBuilder doc = null;
		if(span!=null){
			String invokeType = AppConstants.DUBBO_INVOKE_TYPE_CONSUMER; //默认是消费方
			String ip = "";
			long startTime = 0;
			long spendTime = 0;
			List<Annotation> annotations = span.getAnnotations();
			if(annotations!=null && annotations.size()==2){
				Annotation a0 = annotations.get(0);
				//如果包含c则是消费端,不包含为提供方
				if(a0.getValue().indexOf("c") == -1){
					invokeType = AppConstants.DUBBO_INVOKE_TYPE_PROVIDER;
				}
				ip = a0.getIp();
				startTime = a0.getTimestamp();
				spendTime = annotations.get(1).getTimestamp() - startTime;  
			}
			NumberFormat formatter = new DecimalFormat("00000000");
			
			List<BinaryAnnotation> binaryAnnotations = span.getBinaryAnnotations();
			int exceptionNum = 0;
			String exceptions = "";
			if(binaryAnnotations!=null){
				for(BinaryAnnotation ba : binaryAnnotations){
					exceptions += "#$@" + ba.getValue() + "#$@";
				}
				exceptionNum = binaryAnnotations.size();
			}
			
			try {
				doc = jsonBuilder()
						.startObject()
						.field("id", id)
						.field("spanId", span.getId() + "")
						.field("parentId", span.getParentId() + "")
						.field("serviceName", span.getServiceName())
						.field("spanName", span.getSpanName())
						.field("appName", span.getAppName())
						.field("traceId", span.getTraceId() + "")
						.field("version", System.currentTimeMillis())
						.field("invokeType", invokeType)
						.field("ip", ip)
						.field("startTime", startTime + "")
						.field("spendTime", formatter.format(spendTime))
						.field("exceptionNum", exceptionNum + "")
						.field("exceptions", exceptions)
						.field("statistic", span.getAppName() == null ? "" : span.getAppName().replaceAll("-", "_") + ":" + span.getServiceName() + ":" + span.getSpanName()).endObject();
			} catch (Exception e) {
				System.out.println("======== span2Esdoc error : " + e.getLocalizedMessage());
			}
			formatter = null;
			
			annotations = null;
			binaryAnnotations = null;
		}
		return doc;
	}
	
}
