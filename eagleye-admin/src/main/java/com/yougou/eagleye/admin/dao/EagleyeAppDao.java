package com.yougou.eagleye.admin.dao;
 
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.admin.domain.EagleyeApp;
import com.yougou.eagleye.admin.util.DtPageBean;
import com.yougou.eagleye.core.dao.hibernate3.HibernateBaseDao;

@Repository("eagleyeAppDao")
public class EagleyeAppDao extends HibernateBaseDao<EagleyeApp>{
	
	
	/**
	 * 简单模糊检索查询
	 * @param DtPageBean pageBean , String sSearch
	 * @return pageBean
	 * @throws Exception
	 */
	public DtPageBean<EagleyeApp> querySimplePage(DtPageBean<EagleyeApp> pageBean,
			String sSearch) throws Exception {
		if(pageBean!=null){
			StringBuilder hql = new StringBuilder();
			hql.append("from EagleyeApp u where u.dataStatus =:dataStatus");
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("dataStatus", AppConstants.DATASTATUS_VALID);
			if(sSearch!=null && !"".equals(sSearch)){
				hql.append(" and CONCAT('");
				hql.append(" [',case when u.appName is null then '' else u.appName end,']");
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
	 * @param PageBean pageBean , EagleyeApp eagleyeApp
	 * @return pageBean
	 * @throws Exception
	 */
	public DtPageBean<EagleyeApp> queryPage(DtPageBean<EagleyeApp> pageBean,
			EagleyeApp eagleyeApp) throws Exception {
		if(pageBean!=null){
			StringBuilder hql = new StringBuilder();
			hql.append("from EagleyeApp u where u.dataStatus =:dataStatus");
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("dataStatus", AppConstants.DATASTATUS_VALID);
			if(eagleyeApp!=null){
				 	//用户组名称
			 		String appName = eagleyeApp.getAppName();
			 		if(appName!=null){
			 			hql.append(" and u.appName like :appName");
			 			map.put("appName","%" + appName + "%");
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
	public List<EagleyeApp> queryList(EagleyeApp eagleyeApp){
		StringBuilder hql = new StringBuilder();
		hql.append("from EagleyeApp u where u.dataStatus =:dataStatus");
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("dataStatus", AppConstants.DATASTATUS_VALID);
		if(eagleyeApp!=null){
			 	//用户组名称
		 		String appName = eagleyeApp.getAppName();
		 		if(appName!=null){
		 			hql.append(" and u.appName like :appName");
		 			map.put("appName","%" + appName + "%");
		 		}
		 		
		}
		hql.append(" order by u.id desc");
		List<EagleyeApp> list = this.queryByHql(hql.toString() , map, "不带分页的检索" , 100);
		return list;
	}
	
	/**
	 * 验证数据库中是否有重复的数据
	 * @param eagleyeApp
	 * @return false 没有重复 , 
	 * @throws Exception
	 */
	public boolean verifyRepeat(EagleyeApp eagleyeApp) throws Exception{
		List<EagleyeApp> list = null;
		if(eagleyeApp!=null){
			StringBuilder hql = new StringBuilder();
			hql.append("from EagleyeApp u where u.dataStatus =:dataStatus");
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("dataStatus", AppConstants.DATASTATUS_VALID);
			
	 		String appName = eagleyeApp.getAppName();
	 		if(appName!=null){
	 			hql.append(" and u.appName=:appName");
	 			map.put("appName", appName);
	 		}else{
	 			hql.append(" and 1=2");
	 		}
	 		
	 		Integer id = eagleyeApp.getId();
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
	public EagleyeApp getEagleyeAppById(String id){
		EagleyeApp app = null;
	 	if(id!=null && !id.trim().equals("")){
			//查询单个对象
			String hql = "from EagleyeApp t where t.dataStatus =:dataStatus and t.id=:appId order by t.id asc";
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("dataStatus", AppConstants.DATASTATUS_VALID);
			map.put("appId", Integer.parseInt(id));
			app = this.uniqueQueryByHql(hql, map, "根据ID查询对象");
		}		
		return app;
	}
	
	
	
	/**
	 * 根据appName获取对象
	 * @param appName
	 * @return
	 */
	public EagleyeApp getEagleyeAppByName(String appName){
		EagleyeApp app = null;
	 	if(appName!=null && !appName.trim().equals("")){
			//查询单个对象
			String hql = "from EagleyeApp t where t.dataStatus =:dataStatus and t.appName=:appName";
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("dataStatus", AppConstants.DATASTATUS_VALID);
			map.put("appName", appName);
			List<EagleyeApp> list = this.queryByHql(hql, map, "根据appName查询对象" , 1);
			if(list!=null && list.size()!=0){
				app = list.get(0);
			}
		}		
		return app;
	}
	
	
	
	
	
	 
	 
	/**
	 * 添加或修改一个对象
	 * @param EagleyeAlertVo
	 * @return
	 */
	public EagleyeApp createOrUpdate(EagleyeApp eagleyeApp){
		if(eagleyeApp!=null){
			this.save(eagleyeApp);
		}
		return eagleyeApp;
	}
 
 	/**
	 * 删除一个对象 逻辑删除 非物理删除
	 * @param id
	 * @return
	 */
	public int delete(String id){
		if(id!=null && !id.trim().equals("")){
			String hql="update EagleyeApp u set u.dataStatus=:dataStatus where u.id=:id";
			HashMap<String , Object> map=new HashMap<String, Object>();
			map.put("dataStatus", AppConstants.DATASTATUS_DEL);
			map.put("id", Integer.parseInt(id));
			return this.excuteByHql(hql, map, "逻辑删除");
		}
		return 0;
	}
	
}