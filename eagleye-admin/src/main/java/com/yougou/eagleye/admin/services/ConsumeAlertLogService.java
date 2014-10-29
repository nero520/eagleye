package com.yougou.eagleye.admin.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.admin.domain.EagleyeAlert;
import com.yougou.eagleye.admin.services.support.AlertLogHandler;
import com.yougou.eagleye.admin.util.EagleyeAdminUtil;
import com.yougou.eagleye.admin.util.HttpClientUtil;
import com.yougou.eagleye.core.security.encoder.Md5PwdEncoder;

public class ConsumeAlertLogService {
	
	private final static Logger logger = LoggerFactory.getLogger(ConsumeAlertLogService.class);

	
	private static long expireTime = 600;
	
	
	@Autowired
	private AppConstants appConstants;
	
	@Autowired
	private ThreadPoolTaskExecutor alertLogHandlerTaskExecutor;
	
	//用来存储md5缓存, 过期时间为600s
	public static Cache<String, String> md5Cache = CacheBuilder.newBuilder()
																.maximumSize(100000)
																.expireAfterWrite(expireTime, TimeUnit.SECONDS)
																.build();
	/**
	 * 重复信息预警间隔时间
	 * @param expireTime
	 */
	public ConsumeAlertLogService(long expireTime){
		this.expireTime = expireTime;
	}
	
	
	
	// pass the channel/pattern as well
	public void handleMessage(Serializable message, String channel) {
		String msgStr = message.toString();
		
		this.run(msgStr);

	}
	
	
	
	private void run(String msgStr){
		logger.debug("***********trace log redis message : " + msgStr );
		if(appConstants.isStorageStatus()){//是否开启存储
			try {
				//根据msgStr生成md5值作为该span在elasticsearch中的唯一标识, 如果有多个客户端进行存储操作, 安装es的默认操作统一id会进行覆盖,不会出现重复数据
				String id = Md5PwdEncoder.encryptPasswordNoDepend(msgStr, "eagleye");
				if(id!=null){
					
					//采用多线程进行处理
					AlertLogHandler alh = (AlertLogHandler) EagleyeAdminUtil.getDynamicBean("com.yougou.eagleye.admin.services.support.AlertLogHandler", "prototype");
					alh.setId(id);
					alh.setMsg(msgStr);
					this.alertLogHandlerTaskExecutor.execute(alh);
					alh = null;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("dubbo trace log consumer error :" + e.getMessage());
			}
		}else{
			logger.debug("alert log storage closed! ");
		}
	}

}
