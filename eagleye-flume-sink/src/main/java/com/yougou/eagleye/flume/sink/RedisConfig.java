package com.yougou.eagleye.flume.sink;

import java.util.ResourceBundle;

public class RedisConfig {
	
	private static ResourceBundle rb;
	
	private int maxactive = 300;
	
	private int maxidle = 50;
	
	private long maxwait = 1000;
	
	private int timeout = 5000;
	
	private int retryNum = 3;
	
	private String ip = "192.168.211.246";
	
	private int port = 6379;
	
	public RedisConfig(){
		rb = ResourceBundle.getBundle("config");
		
		try {
			this.maxidle = Integer.parseInt(rb.getString("redis.maxidle"));
		} catch (Exception e) {
		}
		
		try {
			this.maxwait = new Long(rb.getString("redis.maxwait"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		try {
			this.timeout = Integer.parseInt(rb.getString("redis.timeout"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		try {
			this.retryNum = Integer.parseInt(rb.getString("redis.retryNum"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		try {
			this.ip = rb.getString("redis.ip");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		try {
			this.port = Integer.parseInt(rb.getString("redis.port"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}

	public int getMaxactive() {
		return maxactive;
	}

	public void setMaxactive(int maxactive) {
		this.maxactive = maxactive;
	}

	public int getMaxidle() {
		return maxidle;
	}

	public void setMaxidle(int maxidle) {
		this.maxidle = maxidle;
	}

	public long getMaxwait() {
		return maxwait;
	}

	public void setMaxwait(long maxwait) {
		this.maxwait = maxwait;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getRetryNum() {
		return retryNum;
	}

	public void setRetryNum(int retryNum) {
		this.retryNum = retryNum;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	
	
}
