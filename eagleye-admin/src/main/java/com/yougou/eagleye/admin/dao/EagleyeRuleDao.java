package com.yougou.eagleye.admin.dao;
 
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.admin.domain.EagleyeRule;
import com.yougou.eagleye.admin.util.DtPageBean;
import com.yougou.eagleye.core.dao.hibernate3.HibernateBaseDao;

@Repository("eagleyeRuleDao")
public class EagleyeRuleDao extends HibernateBaseDao<EagleyeRule>{
	
	
	/**
	 * 简单模糊检索查询
	 * @param DtPageBean pageBean , String sSearch
	 * @return pageBean
	 * @throws Exception
	 */
	public DtPageBean<EagleyeRule> querySimplePage(DtPageBean<EagleyeRule> pageBean,
			String sSearch) throws Exception {
		if(pageBean!=null){
			StringBuilder hql = new StringBuilder();
			hql.append("from EagleyeRule u where u.dataStatus =:dataStatus");
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("dataStatus", AppConstants.DATASTATUS_VALID);
			if(sSearch!=null && !"".equals(sSearch)){
				hql.append(" and CONCAT('");
				hql.append(" [',case when u.ruleKeyword is null then '' else u.ruleKeyword end,']");
				hql.append(" [',case when u.description is null then '' else u.description end,']");
				hql.append(" ') ");
				hql.append(" like :sSearch");
				map.put("sSearch", "%" + sSearch + "%");
			}
			hql.append(pageBean.getSortStr());
			this.queryPageByHql(hql.toString(), pageBean, map, "简单模糊检索");
		}
		return pageBean;
	}
	
	
	
	/**
	 * 高级检索
	 * @param PageBean pageBean , EagleyeRule eagleyeRule
	 * @return pageBean
	 * @throws Exception
	 */
	public DtPageBean<EagleyeRule> queryPage(DtPageBean<EagleyeRule> pageBean,
			EagleyeRule eagleyeRule) throws Exception {
		if(pageBean!=null){
			StringBuilder hql = new StringBuilder();
			hql.append("from EagleyeRule u where u.dataStatus =:dataStatus");
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("dataStatus", AppConstants.DATASTATUS_VALID);
			if(eagleyeRule!=null){
				 	//用户组名称
			 		String ruleKeyword = eagleyeRule.getRuleKeyword();
			 		if(ruleKeyword!=null){
			 			hql.append(" and u.ruleKeyword like :ruleKeyword");
			 			map.put("ruleKeyword","%" + ruleKeyword + "%");
			 		}
			 		
			}
			hql.append(pageBean.getSortStr());
			this.queryPageByHql(hql.toString(), pageBean, map, "高级检索用户");
		}
		return pageBean;
	}
	
	
	/**
	 * 不带分页的查询
	 * @param null
	 * @return list
	 */
	public List<EagleyeRule> queryList(EagleyeRule eagleyeRule){
		StringBuilder hql = new StringBuilder();
		hql.append("from EagleyeRule u where u.dataStatus =:dataStatus");
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("dataStatus", AppConstants.DATASTATUS_VALID);
		if(eagleyeRule!=null){
			 	//关键字
		 		String ruleKeyword = eagleyeRule.getRuleKeyword();
		 		if(ruleKeyword!=null){
		 			hql.append(" and u.ruleKeyword like :ruleKeyword");
		 			map.put("ruleKeyword","%" + ruleKeyword + "%");
		 		}
		 		
		 		String appIds = eagleyeRule.getAppIds();
		 		if(appIds!=null){
		 			hql.append(" and u.appIds like :appIds");
		 			map.put("appIds","%" + appIds + "%");
		 		}
		 		
		}
		hql.append(" order by u.id desc");
		List<EagleyeRule> list = this.queryByHql(hql.toString() , map, "不带分页的检索" , 100);
		return list;
	}
	
	/**
	 * 验证数据库中是否有重复的数据
	 * @param eagleyeRule
	 * @return false 没有重复 , 
	 * @throws Exception
	 */
	public boolean verifyRepeat(EagleyeRule eagleyeRule) throws Exception{
		List<EagleyeRule> list = null;
		if(eagleyeRule!=null){
			StringBuilder hql = new StringBuilder();
			hql.append("from EagleyeRule u where u.dataStatus =:dataStatus");
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("dataStatus", AppConstants.DATASTATUS_VALID);
			
	 		String ruleKeyword = eagleyeRule.getRuleKeyword();
	 		if(ruleKeyword!=null){
	 			hql.append(" and u.ruleKeyword=:ruleKeyword");
	 			map.put("ruleKeyword", ruleKeyword);
	 		}else{
	 			hql.append(" and 1=2");
	 		}
	 		
	 		Integer id = eagleyeRule.getId();
	 		if(id!=null){
	 			hql.append(" and u.id<>:id");
	 			map.put("id", id);
	 		}
	 		
	 		list = this.queryByHql(hql.toString() , map, "不带分页的检索" , 1);	
		}
		
		if(list == null || list.size() == 0){//没有重复的
			return false;
		}else{
			return true;
		}
	}
	

	/**
	 * 根据ID获取对象
	 * @param id
	 * @return
	 */
	public EagleyeRule getEagleyeRuleById(String id){
		EagleyeRule rule = null;
	 	if(id!=null && !id.trim().equals("")){
			//查询单个对象
			String hql = "from EagleyeRule t where t.dataStatus =:dataStatus and t.id=:ruleId order by t.id asc";
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("dataStatus", AppConstants.DATASTATUS_VALID);
			map.put("ruleId", Integer.parseInt(id));
			rule = this.uniqueQueryByHql(hql, map, "根据ID查询对象");
		}		
		return rule;
	}
	
	
	
	
	
	 
	 
	/**
	 * 添加或修改一个对象
	 * @param EagleyeRule
	 * @return
	 */
	public EagleyeRule createOrUpdate(EagleyeRule eagleyeRule){
		if(eagleyeRule!=null){
			this.save(eagleyeRule);
		}
		return eagleyeRule;
	}
 
 	/**
	 * 删除一个对象 逻辑删除 非物理删除
	 * @param id
	 * @return
	 */
	public int delete(String id){
		if(id!=null && !id.trim().equals("")){
			String hql="update EagleyeRule u set u.dataStatus=:dataStatus where u.id=:id";
			HashMap<String , Object> map=new HashMap<String, Object>();
			map.put("dataStatus", AppConstants.DATASTATUS_DEL);
			map.put("id", Integer.parseInt(id));
			return this.excuteByHql(hql, map, "逻辑删除");
		}
		return 0;
	}
	
}