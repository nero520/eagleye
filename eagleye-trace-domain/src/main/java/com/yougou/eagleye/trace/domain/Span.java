package com.yougou.eagleye.trace.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 13-3-18
 * Time: 下午3:29
 */
public class Span implements Serializable {
	
    private Long traceId;
    
    private Long id;
    
    private Long parentId = 0L; //optional
    
    //method name
    private String spanName;
    
    // interface name
    private String serviceName;
    
    private String appName;
    
    public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	private List<Annotation> annotations;
    private List<BinaryAnnotation> binaryAnnotations;
    private boolean isSample;
    

	public boolean isSample() {
        return isSample;
    }

    public void setSample(boolean sample) {
        isSample = sample;
    }

    public Span(){
        annotations = new ArrayList<Annotation>();
        binaryAnnotations = new ArrayList<BinaryAnnotation>();
    }


    public void addAnnotation(Annotation a){
        annotations.add(a);
    }

    public void addBinaryAnnotation(BinaryAnnotation a){
        binaryAnnotations.add(a);
    }


    public Long getTraceId() {
        return traceId;
    }

    public void setTraceId(Long traceId) {
        this.traceId = traceId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
    	if(parentId == null){
    		parentId = 0L;
    	}
        this.parentId = parentId;
    }

    public String getSpanName() {
        return spanName;
    }

    public void setSpanName(String spanName) {
        this.spanName = spanName;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    public List<BinaryAnnotation> getBinaryAnnotations() {
        return binaryAnnotations;
    }

    public void setBinaryAnnotations(List<BinaryAnnotation> binaryAnnotations) {
        this.binaryAnnotations = binaryAnnotations;
    }

//    @Override
//    public String toString() {
//        return "Span{" +
//                "traceId=" + traceId +
//                ", id=" + id +
//                ", parentId=" + parentId +
//                ", serviceName=" + serviceName +
//                ", spanName='" + spanName + '\'' +
//                ", annotations=" + annotations +
//                ", binaryAnnotations=" + binaryAnnotations +
//                ", isSample=" + isSample +
//                '}';
//    }

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

    
}
