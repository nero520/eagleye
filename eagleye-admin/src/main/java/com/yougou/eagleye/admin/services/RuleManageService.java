package com.yougou.eagleye.admin.services;


import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.admin.dao.EagleyeRuleDao;
import com.yougou.eagleye.admin.domain.EagleyeRule;
import com.yougou.eagleye.admin.util.DtPageBean;
import com.yougou.eagleye.core.reflection.ReflectionUtils;

@Service("ruleManageService")
@Transactional
public class RuleManageService{
	
	private final static Logger logger = LoggerFactory.getLogger(UserGroupManageService.class);

	@Autowired
	private EagleyeRuleDao eagleyeRuleDao;
	
	@Autowired
	private AlertManageService alertManageService;
	
	/**
	 * 根据id获得Rule
	 * @param id
	 * @return 返回查询到的EagleyeRule对象
	 */
	public EagleyeRule getRuleById(String id) throws Exception{
		EagleyeRule rule = eagleyeRuleDao.getEagleyeRuleById(id);
		rule.setAppMap();
		return rule;
	}
	
	/**
	 * 更新
	 * @param 页面传入的对象
	 * @return 返回更新后的用户对象
	 */
	public EagleyeRule updateRule(EagleyeRule pageParam) throws Exception{
		EagleyeRule rule = null;
		if(pageParam!=null){
			//name需要排重
			if(eagleyeRuleDao.verifyRepeat(pageParam)){//有重复数据
				pageParam.setFlash(AppConstants.FLASH_REPEAT);
				rule = pageParam;
			}else{//没有重复的
				rule = eagleyeRuleDao.getEagleyeRuleById(pageParam.getId()+"");
				ReflectionUtils.mergeObject(rule, pageParam);
				rule.setUpdateTime(new Date());
				
				rule = eagleyeRuleDao.createOrUpdate(rule);
			}
		}
//		更新redis预警规则
		this.alertManageService.updateRuleListToCache();
		return rule;
	}
	
	/**
	 * 新增
	 * @param eagleyeRule 待更新的对象
	 * @return 返回更新后的对象
	 */
	public EagleyeRule createRule(EagleyeRule eagleyeRule) throws Exception{
		if(eagleyeRule!=null){
			eagleyeRule.setDataStatus(AppConstants.DATASTATUS_VALID);
			eagleyeRule.setCreateTime(new Date());
			eagleyeRule.setUpdateTime(new Date());
			//name需要排重
			if(eagleyeRuleDao.verifyRepeat(eagleyeRule)){//有重复数据
				eagleyeRule.setFlash(AppConstants.FLASH_REPEAT);
			}else{//没有重复的
				if(eagleyeRule.getRuleKeyword()==null){//用户名不能为空
					eagleyeRule.setFlash(AppConstants.FLASH_ERROR);
				}else{
					eagleyeRule = eagleyeRuleDao.createOrUpdate(eagleyeRule);
				}
			}
		}
//		更新redis预警规则
		this.alertManageService.updateRuleListToCache();
		return eagleyeRule;
	}
	
	
	
	
	
	/**
	 * 删除
	 * @param id 待删除的用户对象的id
	 */
	public int delete(String id) throws Exception{
		int resultCount = eagleyeRuleDao.delete(id);
//		更新redis预警规则
		this.alertManageService.updateRuleListToCache();
		return resultCount;
	}
	
	
	
	/**
	 * 简单模糊检索
	 * @param DtPageBean pageBean , String sSearch
	 * @return pageBean
	 * @throws Exception
	 */
	public DtPageBean<EagleyeRule> querySimpleRuleList(DtPageBean<EagleyeRule> pageBean,String sSearch) throws Exception{
		pageBean = this.eagleyeRuleDao.querySimplePage(pageBean, sSearch);
		return pageBean;
	}
	
	/**
	 * 高级检索
	 * @param DtPageBean pageBean , EagleyeRule eagleyeRule
	 * @return pageBean
	 * @throws Exception
	 */
	public DtPageBean<EagleyeRule> queryRuleList(DtPageBean<EagleyeRule> pageBean,EagleyeRule eagleyeRule) throws Exception{
		pageBean=this.eagleyeRuleDao.queryPage(pageBean, eagleyeRule);
		return pageBean;
	}
	
	
	
	
	
	
}