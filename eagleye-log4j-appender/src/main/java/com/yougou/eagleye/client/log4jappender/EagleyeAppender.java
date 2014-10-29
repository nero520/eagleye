/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.yougou.eagleye.client.log4jappender;





import java.net.InetAddress;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;


import com.yougou.eagleye.client.log4jappender.support.AsyncTransfer;
import com.yougou.eagleye.client.log4jappender.support.Configuration;
import com.yougou.eagleye.client.log4jappender.support.Sampler;


public class EagleyeAppender extends AppenderSkeleton {

	private String appGroup = "";
	
	private PatternLayout layout = new org.apache.log4j.PatternLayout("%m");
	
	
	private static Logger logger = Logger.getLogger(EagleyeAppender.class);
	
	private static Configuration configuration = Configuration.getInstance();
	
	private static Sampler sampler = new Sampler();
	
	private static String ip = "";
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean requiresLayout() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void append(LoggingEvent event) {
		try {
			//只发送ERROR级别, 并且是采样的日志
			if (event != null && event.getLevel().equals(Level.ERROR)
					&& sampler.isSample() && configuration.isOpen()) {
				String msg = "#EAGLEYE#";
				msg += this.appGroup + "#";
				String appName = this.appGroup;
				try {
					if("".equals(ip)){
						this.ip = configuration.getLocalIP();
					}
					appName += this.ip.split("\\.")[3];
				} catch (Exception e) {
					logger.debug("eagleye log4j appender get ip error "+ e.getLocalizedMessage());
				}
				msg += appName + "#";
				msg += System.currentTimeMillis() + "#";
				msg += this.layout.format(event) + "\r\n";

				String[] strs = event.getThrowableStrRep();
				if (strs != null && strs.length > 1) {
					msg += event.getThrowableStrRep()[0] + "\r\n";
					for (int i = 1; i < strs.length; i++) {
						if (strs[i].indexOf("com.yougou") > -1) {
							msg += strs[i] + "\r\n";
						}
					}
				}
				
				Configuration.alertQueue.offer(msg);
				strs = null;
				msg = null;
			}
		} catch (Exception e) {
			logger.debug("eagleye log4j appender error "+ e.getLocalizedMessage());
		}
		
	}
	
	 /**
     * Derived appenders should override this method if option structure
     * requires it.
     */
    public void activateOptions() {
    	
    }

	
	
	public String getAppGroup() {
		return appGroup;
	}

	public void setAppGroup(String appGroup) {
		this.appGroup = appGroup;
	}

	
	
	//加载filter时, 创建发送redis的守护线程
    static{
    	Thread sendThread = new Thread(new AsyncTransfer());
    	sendThread.setDaemon(true);
    	sendThread.setName("DubboAlertAsyncSendLogThread");
    	sendThread.start();
    	logger.info("=========Daemon thread: DubboAlertAsyncSendLogThread init successfully!============");
    }

	
  
}

