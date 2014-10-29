package com.yougou.eagleye.admin.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.admin.dao.EagleyeAppDao;
import com.yougou.eagleye.admin.dao.EagleyeRuleDao;
import com.yougou.eagleye.admin.dao.EagleyeUserGroupDao;
import com.yougou.eagleye.admin.domain.EagleyeAlert;
import com.yougou.eagleye.admin.domain.EagleyeApp;
import com.yougou.eagleye.admin.domain.EagleyeRule;
import com.yougou.eagleye.admin.domain.EagleyeUserGroup;
import com.yougou.eagleye.core.utils.NumUtils;
import com.yougou.eagleye.core.utils.StrUtils;



@Service("alertManageService")
@Transactional
public class AlertManageService{
	
	private final static Logger logger = LoggerFactory.getLogger(AlertManageService.class);
	
	
	@Autowired
	private EagleyeAppDao eagleyeAppDao;
	
	@Autowired
	private EagleyeUserGroupDao eagleyeUserGroupDao;
	
	@Autowired
	private EagleyeRuleDao eagleyeRuleDao;
	
	@Autowired
	private RedisTemplate<String, String> alertRedisTemplate;

	public List<EagleyeAlert> getAlertInfoByAppName (EagleyeApp app)throws Exception{
		List<EagleyeAlert> alertList = null;
		//根据appName 获取app对象
		String emails = "";
		String mobiles = "";
		if(app!=null){
			String appName = app.getAppName();
			//根据appName 获取 成员列表
			List<String> groupIdList = StrUtils.getBetweenArray("\\[","\\]",app.getGroupIds());
			
			List<EagleyeUserGroup> userGroupList = null;
			if(groupIdList!=null && groupIdList.size()!=0){
				userGroupList = this.eagleyeUserGroupDao.queryListByGroupIds(NumUtils.strsToInts(groupIdList.toArray()));
			}
			if(userGroupList!=null){
				Set<String> tempEmails = new HashSet<String>();//临时存储emails列表用来排重
				Set<String> tempMobiles = new HashSet<String>(); // 临时存储 mobiles列表用来排重
				for(EagleyeUserGroup userGroup : userGroupList){
					String alertType = userGroup.getWarnType(); //预警类型
					if(alertType!=null && alertType.indexOf(AppConstants.ALERT_TYPE_EMAIL)>-1){//需要发送邮件的用户
						tempEmails.add(userGroup.getUserAccount());
					}
					if(alertType!=null && alertType.indexOf(AppConstants.ALERT_TYPE_MOBILE)>-1){//需要发送手机短信的用户
						tempMobiles.add(userGroup.getUserPhone());
					}
				}
				if(tempEmails.size()!=0){
					emails = StringUtils.join(tempEmails.toArray(),";");
				}
				if(tempMobiles.size()!=0){
					mobiles = StringUtils.join(tempMobiles.toArray(),";");
				}
			}
			
			//根据appName 获取预警规则
			EagleyeRule queryParam = new EagleyeRule();
			queryParam.setAppIds("["+app.getId()+"]");
			List<EagleyeRule> ruleList = this.eagleyeRuleDao.queryList(queryParam);
			//对该应用下的关键字进行排重
			Set<String> keywords = new HashSet<String>();
			if(ruleList!=null){
				for(EagleyeRule rule : ruleList){
					keywords.add(rule.getRuleKeyword());
				}
			}
			
			//开始拼装alert对象,并放入集合中
			alertList = new ArrayList<EagleyeAlert>();
			for(String keyword : keywords){
				EagleyeAlert alert = new EagleyeAlert();
				alert.setAppName(appName);
				alert.setEmails(emails);
				alert.setKeyword(keyword);
				alert.setMobiles(mobiles);
				alertList.add(alert);
			}
		}
		return alertList;
	}
	
	
	/**
	 * list转换为json
	 * @param alertList
	 * @return
	 */
	private String ruleList2String(List<EagleyeAlert> alertList){
		String str = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			str = mapper.writeValueAsString(alertList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
	
	/**
	 * json 转换为 list
	 * @param str
	 * @return
	 */
	private List<EagleyeAlert> string2RuleList(String str){
		List<EagleyeAlert> alertList = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class,EagleyeAlert.class);
			alertList = (List<EagleyeAlert>) mapper.readValue(str, javaType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return alertList;
	}
	
	
	
	
	/**
	 * 更新所有redis中的预警规则
	 * @throws Exception
	 */
	public void updateRuleListToCache() throws Exception{
		//获取所有appName列表
		List<EagleyeApp> appList = this.eagleyeAppDao.queryList(null);
		if(appList!=null){
			for(EagleyeApp app : appList){
				String redisAppKey = AppConstants.KEY_PREFIX + app.getAppName();
				List<EagleyeAlert> alertList = this.getAlertInfoByAppName(app);
				if(alertList!=null){
					String ruleJson = this.ruleList2String(alertList);
					alertRedisTemplate.opsForValue().set(redisAppKey, ruleJson);
					logger.info("**************update object and reload cache*******"+redisAppKey+"**********");
//					ruleCache.put(redisAppKey, ruleJson);
				}
			}
		}
	}
	
	
	
}