package com.yougou.eagleye.admin.vo;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.admin.domain.EagleyeTraceLog;




/**
 * 应用日志对象
 * 
 * @author panda
 * 
 */
public class TraceLinkVo implements java.io.Serializable {

	private static final long serialVersionUID = 7664382600656918528L;
	
	private String traceId;

	private String spanId;

	private String parentId = "0"; // optional

	// method name
	private String spanName;

	// interface name
	private String serviceName;

	/**
	 * consumer app name
	 */
	private String cappName;
	
	/**
	 * provider app name
	 */
	private String pappName;

	/**
	 * consumer start time
	 */
	private String cst = "0";


	/**
	 * provider start time
	 */
	private String pst = "0";


	private String cspendTime = "0";
	
	private String pspendTime = "0";

	private String consumerIp;

	private String providerIp;

	private String cexceptions;
	
	private String pexceptions;

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getSpanId() {
		return spanId;
	}

	public void setSpanId(String spanId) {
		this.spanId = spanId;
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

	

	public String getCappName() {
		return cappName;
	}

	public void setCappName(String cappName) {
		this.cappName = cappName;
	}

	public String getPappName() {
		return pappName;
	}

	public void setPappName(String pappName) {
		this.pappName = pappName;
	}

	public String getCst() {
		return cst;
	}

	public void setCst(String cst) {
		this.cst = cst;
	}


	public String getPst() {
		return pst;
	}

	public void setPst(String pst) {
		this.pst = pst;
	}

	
	

	public String getCexceptions() {
		return cexceptions;
	}

	public void setCexceptions(String cexceptions) {
		this.cexceptions = cexceptions;
	}

	public String getPexceptions() {
		return pexceptions;
	}

	public void setPexceptions(String pexceptions) {
		this.pexceptions = pexceptions;
	}

	public String getCspendTime() {
		return cspendTime;
	}

	public void setCspendTime(String cspendTime) {
		this.cspendTime = cspendTime;
	}

	public String getPspendTime() {
		return pspendTime;
	}

	public void setPspendTime(String pspendTime) {
		this.pspendTime = pspendTime;
	}

	public String getConsumerIp() {
		return consumerIp;
	}

	public void setConsumerIp(String consumerIp) {
		this.consumerIp = consumerIp;
	}

	public String getProviderIp() {
		return providerIp;
	}

	public void setProviderIp(String providerIp) {
		this.providerIp = providerIp;
	}

	

	
	public static TraceLinkVo traceLog2LinkVo(EagleyeTraceLog traceLog, TraceLinkVo tlv){
		if(tlv==null){
			tlv = new TraceLinkVo();
		}
		if(traceLog!=null){
			tlv.setServiceName(traceLog.getServiceName());
			tlv.setSpanName(traceLog.getSpanName());
			tlv.setParentId(traceLog.getParentId());
			tlv.setTraceId(traceLog.getTraceId());
			tlv.setSpanId(traceLog.getSpanId());
			String invokeType = traceLog.getInvokeType();
			if(AppConstants.DUBBO_INVOKE_TYPE_CONSUMER.equals(invokeType)){//消费者
				tlv.setCappName(traceLog.getAppName());
				tlv.setCst(traceLog.getStartTime());
				tlv.setCspendTime(traceLog.getSpendTime());
				tlv.setConsumerIp(traceLog.getIp());
				tlv.setCexceptions(traceLog.getExceptions());
			}else if(AppConstants.DUBBO_INVOKE_TYPE_PROVIDER.equals(invokeType)){//生产者
				tlv.setPappName(traceLog.getAppName());
				tlv.setPst(traceLog.getStartTime());
				tlv.setPspendTime(traceLog.getSpendTime());
				tlv.setProviderIp(traceLog.getIp());
				tlv.setPexceptions(traceLog.getExceptions());
			}
		}
		
		return tlv;
	}
}
