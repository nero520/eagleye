package com.yougou.eagleye.admin.services.support;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.admin.domain.EagleyeAlert;
import com.yougou.eagleye.admin.domain.EagleyeLog;
import com.yougou.eagleye.admin.services.ConsumeAlertLogService;
import com.yougou.eagleye.admin.services.LogManageService;
import com.yougou.eagleye.admin.util.EagleyeAdminUtil;
import com.yougou.eagleye.core.security.encoder.Md5PwdEncoder;


public class AlertLogHandler implements Runnable{
	
	private String msg;
	
	private String id;
	

	

	private final static Logger logger = LoggerFactory.getLogger(AlertLogHandler.class);

	@Autowired
    private LogManageService logManageService;
	

	private static ObjectMapper objectMapper = new ObjectMapper();
	

	@Autowired
	private RedisTemplate<String, String> alertRedisTemplate;
	
	
	@Autowired
	private AppConstants appConstants;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(msg!=null && id!=null){
			
			EagleyeLog log = null;
			//存储原始跟踪信息
			try {
				log = EagleyeLog.msg2AlertLog(msg, id);
				if(log!=null){
					this.logManageService.saveAlertLog(log);
				}
			} catch (Exception e) {
				logger.error("save alertlog to elasticsearch error :" + e.getMessage());
			}
			
			
			//预警操作, 
			try {
				if (log != null) {
					//预警
					this.logAlert(log);
				}
			} catch (Exception e) {
				logger.error("alertlog alert error :" + e.getMessage());
			}
			id = null;
			msg = null;
		}
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	/**
	 * 预警
	 * @param span
	 * @param id
	 * @return
	 */
	private void logAlert(EagleyeLog alertLog){
		try {
			// 获取redis中的预警规则
			String redisAppKey = AppConstants.KEY_PREFIX + alertLog.getAppGroup();
			// 获取规则json串,并转换为规则对象list
			String ruleStr = alertRedisTemplate.opsForValue().get(redisAppKey);
			
			
			logger.debug("############ redis rule key and ruleStr : "+ redisAppKey +"############"+ruleStr);
			List<EagleyeAlert> alertList = null;
			if(ruleStr != null){
				JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, EagleyeAlert.class);
				alertList = (List<EagleyeAlert>) objectMapper.readValue(ruleStr, javaType);
			}
			
			if (alertList != null) {
				
				for (EagleyeAlert alert : alertList) {
					try {
						String keyword = alert.getKeyword();
						String body = alertLog.getBody();
						if (body != null && keyword != null) {
							int position = body.indexOf(keyword);
							// 如果包含该关键字, 则需要预警
							if (this.marchKeyword(body, keyword)) {
								// 根据headMsg产生md5值
								// 短信内容和邮件title
								String headMsg = alertLog.getAppGroup() + " " + alertLog.getAppName() + " " + keyword;
								String md5Tag = Md5PwdEncoder.encryptPasswordNoDepend(headMsg, "");
								logger.debug("&&&&&&&&&&&&&&&&&&&" + md5Tag + "&&&&&&&&&&&&&&&" + headMsg + "&&&&&&&&"+ ConsumeAlertLogService.md5Cache.getIfPresent(md5Tag));
								// 从cache中获取md5值进行比对, 如果没有, 则直接发送预警
								if (judgeMd5Tag(md5Tag)) {
									int after = position + 20;
									if (after >= body.length() - 1) {
										after = body.length() - 1;
									}
									int before = position - 10;
									if(before<0){
										before = 0;
									}
									headMsg += " " + EagleyeAdminUtil.long2DateStr(alertLog.getTimestamp()) + " " + body.substring(before, after);// 取关键字前10个,和之后20个字符,作为标题.

									// 获取发送邮件的列表
									String emails = alert.getEmails();
									// 发送邮件预警
									if (emails != null && !emails.trim().equals("")) {
//										System.out.println("发送邮件" + md5Tag + "********" + headMsg);
										
										String alertMsg = "<font color='blue'><b>应用预警日志说明: <br/>";
										alertMsg += "每个应用如果有ERROR级别的预警,则会通过后台设定的预警规则进行过滤判断,然后预警.<br/>";
										alertMsg += "针对符合预警的日志,会实时预警,在十分钟内不会重复发送相同的预警信息.<br/>";
										alertMsg += "短信预警只截取关键的第10~20个字符;邮件预警则会发送全部预警信息,不会截取.<br/>";
										alertMsg += "</b></font><br/><br/>";
										
										alertMsg += body;
										
										EagleyeAdminUtil.sendEmail(headMsg, alertMsg, emails, appConstants.getSendEmailUrl());
									}

									//获取发送短信的列表
									String mobiles = alert.getMobiles();
									// 发送短信预警
									if (mobiles != null && !mobiles.trim().equals("")) {
//										System.out.println("发送短信" + md5Tag + "********" + headMsg);
										EagleyeAdminUtil.sendMobile(headMsg, mobiles,appConstants.getSendSmsUrl());
									}
								}
							}
						}
					} catch (Exception e) {
						logger.error("发送预警邮件或短信异常", e);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("日志分析异常", e);
		}
	}
	
	
	/**
	 * 判断是否有发送状态码,如果有, 则不需要发送, 该方法是同步方法, 解决分布式问题
	 * @param md5Tag
	 * @return
	 */
	private synchronized boolean judgeMd5Tag(String md5Tag){
		boolean isAlert = false;
		if(ConsumeAlertLogService.md5Cache.getIfPresent(md5Tag) == null){
			// 在缓存中埋下识别代码, 过期时间为10分钟
			ConsumeAlertLogService.md5Cache.put(md5Tag, md5Tag);
			isAlert = true;
		}
		return isAlert;
	}
	
	/**
	 * 判断是否包含关键字, 如果包含,则进行预警
	 * @param msg
	 * @param keyword
	 * @return
	 */
	private boolean marchKeyword(String msg, String keyword){
		boolean isMarch = false;
		if(msg!=null && keyword!=null){
			if(msg.indexOf(keyword)>-1){
				isMarch = true;
			}
		}
		return isMarch;
	}
	
}
