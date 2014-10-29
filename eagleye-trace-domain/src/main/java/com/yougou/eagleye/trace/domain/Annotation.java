package com.yougou.eagleye.trace.domain;

import java.io.Serializable;

/**
 * Date: 13-3-18
 * Time: 下午3:36
 */
public class Annotation implements Serializable {
    public static final String CLIENT_SEND = "cs";
    public static final String CLIENT_RECEIVE = "cr";
    public static final String SERVER_SEND = "ss";
    public static final String SERVER_RECEIVE = "sr";
    private Long timestamp;
    private String value;
    private String ip;
    private Integer duration = 0;

    public Annotation(){

    }
    
    public Annotation(Long timestamp, String value, String ip) {
        this.timestamp = timestamp;
        this.value = value;
        this.ip = ip;
    }
    
    
    public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
    	if(duration == null){
    		duration = 0;
    	}
        this.duration = duration;
    }

//    @Override
//    public String toString() {
//        return "Annotation{" +
//                "timestamp=" + timestamp +
//                ", value='" + value + '\'' +
//                ", host=" + host +
//                ", duration=" + duration +
//                '}';
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Annotation)) return false;

        Annotation that = (Annotation) o;

        if (duration!=null&&!duration.equals(that.duration)) return false;
        if (!timestamp.equals(that.timestamp)) return false;
        if (!ip.equals(that.ip)) return false;
        if (!value.equals(that.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = timestamp.hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + ip.hashCode();
        result = 31 * result + duration.hashCode();
        return result;
    }
}
