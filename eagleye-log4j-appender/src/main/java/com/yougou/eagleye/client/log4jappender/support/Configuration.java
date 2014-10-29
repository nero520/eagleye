package com.yougou.eagleye.client.log4jappender.support;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;



public class Configuration {

	private static Logger logger = Logger.getLogger(Configuration.class);
	
	public static BlockingQueue<String> alertQueue = new LinkedBlockingQueue<String>(2000);
	
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
		} catch (FileNotFoundException e) {
			//如果该文件没找到,则默认加载项目中的配置文件
			rb = ResourceBundle.getBundle("config");
		} catch (IOException e){
			logger.error("eagleye-alert-client loading redis properties error", e);
		}
		
		if(rb!=null){
			try {
				this.maxidle = Integer.parseInt(rb.getString("eagleye.client.alert.redis.pool.maxIdle"));
				logger.info("eagleye-alert-client loading redis maxidle : " + this.maxidle);
			} catch (Exception e) {
				logger.error("eagleye-alert-client loading redis maxidle error",e);
			}
			
			try {
				this.maxwait = new Long(rb.getString("eagleye.client.alert.redis.pool.maxWait"));
				logger.info("eagleye-alert-client loading redis maxWait : " + this.maxwait);
			} catch (Exception e) {
				logger.error("eagleye-alert-client loading redis maxWait error",e);
			}
			
			try {
				this.timeout = Integer.parseInt(rb.getString("eagleye.client.alert.redis.pool.timeout"));
				logger.info("eagleye-alert-client loading redis timeout : " + this.timeout);
			} catch (Exception e) {
				logger.error("eagleye-alert-client loading redis timeout error",e);
			}
			
			try {
				this.retryNum = Integer.parseInt(rb.getString("eagleye.client.alert.redis.retryNum"));
				logger.info("eagleye-alert-client loading redis retryNum : " + this.retryNum);
			} catch (Exception e) {
				logger.error("eagleye-alert-client loading redis retryNum error",e);
			}
			
			try {
				this.ip = rb.getString("eagleye.client.alert.redis.ip");
				logger.info("eagleye-alert-client loading redis ip : " + this.ip);
			} catch (Exception e) {
				logger.error("eagleye-alert-client loading redis ip error",e);
			}
			
			try {
				this.port = Integer.parseInt(rb.getString("eagleye.client.alert.redis.port"));
				logger.info("eagleye-alert-client loading redis port : " + this.port);
			} catch (Exception e) {
				logger.error("eagleye-alert-client loading redis port error",e);
			}
			
			try {
				this.isOpen = Boolean.valueOf(rb.getString("eagleye.client.alert.isOpen"));
				logger.info("eagleye-alert-client loading isOpen : " + this.isOpen);
			} catch (Exception e) {
				logger.error("eagleye-alert-client loading isOpen",e);
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
	
	public static String getLocalIP() {
		String ipStr = "0.0.0.0";
        try {
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface
					.getNetworkInterfaces();
			InetAddress ip = null;
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces
						.nextElement();
				Enumeration<InetAddress> addresses = netInterface
						.getInetAddresses();
				while (addresses.hasMoreElements()) {
					ip = (InetAddress) addresses.nextElement();
					if (ip != null && ip instanceof Inet4Address) {
						String tempIp = ip.getHostAddress();
						if (!"127.0.0.1".equals(tempIp)
								&& !"0.0.0.0".equals(tempIp)) {
							ipStr = tempIp;
						}
					}
				}
				netInterface = null;
				addresses = null;
			}
			allNetInterfaces = null;
		} catch (Exception e) {
			logger.debug("eagleye log4j appender get local ip error " + e.getLocalizedMessage());
		}
        logger.info("eagleye-alert-client get local ip : " + ipStr);
		return ipStr;
    }
	
}
