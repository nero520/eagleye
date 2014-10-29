package com.yougou.eagleye.trace.domain;

import java.io.Serializable;

/**
 * Date: 13-3-18
 * Time: 下午3:36
 */
public class BinaryAnnotation implements Serializable {
    private String key;
    private String value;
    private String type;
    private Integer duration;
    private String ip;

    

//    @Override
//    public String toString() {
//        return "BinaryAnnotation{" +
//                "key='" + key + '\'' +
//                ", value=" + value +
//                ", type='" + type + '\'' +
//                ", duration=" + duration +
//                ", endpoint=" + host +
//                '}';
//    }

    public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
