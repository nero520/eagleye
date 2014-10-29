package com.yougou.eagleye.dubbo.client.support;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Configuration {

	private static Logger logger = LoggerFactory.getLogger(Configuration.class);
	
	private static final Configuration configuration = new Configuration(); 
	
	private static ResourceBundle rb = null;
	
	private int maxactive = 300;
	
	private int maxidle = 50;
	
	private long maxwait = 1000;
	
	private int timeout = 5000;
	
	private int retryNum = 0;
	
	private String ip = "192.168.211.247";
	
	private int port = 6379;
	
	/**
	 * 是否开启跟踪, 默认关闭
	 */
	private boolean isOpen = false;
	
	private Configuration(){
		
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(
					"/etc/yougouconf/eagleye-client/config.properties"));// 生产环境加载该配置文件
			rb = new PropertyResourceBundle(in);
		} catch (Exception e){
			logger.error("eagleye-dubbo-client loading /etc/yougouconf/eagleye-client/config.properties redis properties error");
			try {
				rb = ResourceBundle.getBundle("config");
			} catch (Exception e2) {
				logger.error("eagleye-dubbo-client loading classpath:config.properties redis properties error");
			}
		}
		
		if(rb!=null){
			try {
				this.maxidle = Integer.parseInt(rb.getString("eagleye.client.trace.redis.pool.maxIdle"));
				logger.info("eagleye-dubbo-client loading redis maxidle : " + this.maxidle);
			} catch (Exception e) {
				logger.error("eagleye-dubbo-client loading redis maxidle error",e);
			}
			
			try {
				this.maxwait = new Long(rb.getString("eagleye.client.trace.redis.pool.maxWait"));
				logger.info("eagleye-dubbo-client loading redis maxWait : " + this.maxwait);
			} catch (Exception e) {
				logger.error("eagleye-dubbo-client loading redis maxWait error",e);
			}
			
			try {
				this.timeout = Integer.parseInt(rb.getString("eagleye.client.trace.redis.pool.timeout"));
				logger.info("eagleye-dubbo-client loading redis timeout : " + this.timeout);
			} catch (Exception e) {
				logger.error("eagleye-dubbo-client loading redis timeout error",e);
			}
			
			try {
				this.retryNum = Integer.parseInt(rb.getString("eagleye.client.trace.redis.retryNum"));
				logger.info("eagleye-dubbo-client loading redis retryNum : " + this.retryNum);
			} catch (Exception e) {
				logger.error("eagleye-dubbo-client loading redis retryNum error",e);
			}
			
			try {
				this.ip = rb.getString("eagleye.client.trace.redis.ip");
				logger.info("eagleye-dubbo-client loading redis ip : " + this.ip);
			} catch (Exception e) {
				logger.error("eagleye-dubbo-client loading redis ip error",e);
			}
			
			try {
				this.port = Integer.parseInt(rb.getString("eagleye.client.trace.redis.port"));
				logger.info("eagleye-dubbo-client loading redis port : " + this.port);
			} catch (Exception e) {
				logger.error("eagleye-dubbo-client loading redis port error",e);
			}
			
			try {
				this.isOpen = Boolean.valueOf(rb.getString("eagleye.client.trace.isOpen"));
				logger.info("eagleye-dubbo-client loading isOpen : " + this.isOpen);
			} catch (Exception e) {
				logger.error("eagleye-dubbo-client loading isOpen",e);
			}
		}
		
	}
	
	public static Configuration getInstance(){
		return configuration;
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

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	
	
	
}
