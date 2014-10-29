package com.yougou.eagleye.admin.dao;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.admin.domain.EagleyeGroup;
import com.yougou.eagleye.admin.util.DtPageBean;
import com.yougou.eagleye.core.dao.hibernate3.HibernateBaseDao;
import com.yougou.eagleye.core.dao.page.PageBean;

@Repository("eagleyeGroupDao")
public class EagleyeGroupDao extends HibernateBaseDao<EagleyeGroup>{
	
	
	/**
	 * 简单模糊检索查询
	 * @param DtPageBean pageBean , String sSearch
	 * @return pageBean
	 * @throws Exception
	 */
	public DtPageBean<EagleyeGroup> querySimplePage(DtPageBean<EagleyeGroup> pageBean,
			String sSearch) throws Exception {
		if(pageBean!=null){
			StringBuilder hql = new StringBuilder();
			hql.append("from EagleyeGroup u where u.dataStatus =:dataStatus");
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("dataStatus", AppConstants.DATASTATUS_VALID);
			if(sSearch!=null && !"".equals(sSearch)){
				hql.append(" and CONCAT('");
				hql.append(" [',case when u.groupName is null then '' else u.groupName end,']");
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
	 * @param PageBean pageBean , EagleyeGroup eagleyeGroup
	 * @return pageBean
	 * @throws Exception
	 */
	public DtPageBean<EagleyeGroup> queryPage(DtPageBean<EagleyeGroup> pageBean,
			EagleyeGroup eagleyeGroup) throws Exception {
		if(pageBean!=null){
			StringBuilder hql = new StringBuilder();
			hql.append("from EagleyeGroup u where u.dataStatus =:dataStatus");
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("dataStatus", AppConstants.DATASTATUS_VALID);
			if(eagleyeGroup!=null){
				 	//用户组名称
			 		String groupName = eagleyeGroup.getGroupName();
			 		if(groupName!=null){
			 			hql.append(" and u.groupName like :groupName");
			 			map.put("groupName","%" + groupName + "%");
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
	public List<EagleyeGroup> queryList(EagleyeGroup eagleyeGroup){
		StringBuilder hql = new StringBuilder();
		hql.append("from EagleyeGroup u where u.dataStatus =:dataStatus");
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("dataStatus", AppConstants.DATASTATUS_VALID);
		if(eagleyeGroup!=null){
			 	//用户组名称
		 		String groupName = eagleyeGroup.getGroupName();
		 		if(groupName!=null){
		 			hql.append(" and u.groupName like :groupName");
		 			map.put("groupName","%" + groupName + "%");
		 		}
		 		
		}
		hql.append(" order by u.id desc");
		List<EagleyeGroup> list = this.queryByHql(hql.toString() , map, "不带分页的检索" , 100);
		return list;
	}
	
	/**
	 * 验证数据库中是否有重复的数据
	 * @param eagleyeGroup
	 * @return false 没有重复 , 
	 * @throws Exception
	 */
	public boolean verifyRepeat(EagleyeGroup eagleyeGroup) throws Exception{
		List<EagleyeGroup> list = null;
		if(eagleyeGroup!=null){
			StringBuilder hql = new StringBuilder();
			hql.append("from EagleyeGroup u where u.dataStatus =:dataStatus");
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("dataStatus", AppConstants.DATASTATUS_VALID);
			
	 		String groupName = eagleyeGroup.getGroupName();
	 		if(groupName!=null){
	 			hql.append(" and u.groupName=:groupName");
	 			map.put("groupName", groupName);
	 		}else{
	 			hql.append(" and 1=2");
	 		}
	 		
	 		Integer id = eagleyeGroup.getId();
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
	public EagleyeGroup getEagleyeGroupById(String id){
		EagleyeGroup group = null;
	 	if(id!=null && !id.trim().equals("")){
			//查询单个对象
			String hql = "from EagleyeGroup t where t.dataStatus =:dataStatus and t.id=:groupId order by t.id asc";
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("dataStatus", AppConstants.DATASTATUS_VALID);
			map.put("groupId", Integer.parseInt(id));
			group = this.uniqueQueryByHql(hql, map, "根据ID查询对象");
		}		
		return group;
	}
	
	
	
	
	
	 
	 
	/**
	 * 添加或修改一个对象
	 * @param EagleyeGroup
	 * @return
	 */
	public EagleyeGroup createOrUpdate(EagleyeGroup eagleyeGroup){
		if(eagleyeGroup!=null){
			this.save(eagleyeGroup);
		}
		return eagleyeGroup;
	}
 
 	/**
	 * 删除一个对象 逻辑删除 非物理删除
	 * @param id
	 * @return
	 */
	public int delete(String id){
		if(id!=null && !id.trim().equals("")){
			String hql="update EagleyeGroup u set u.dataStatus=:dataStatus where u.id=:id";
			HashMap<String , Object> map=new HashMap<String, Object>();
			map.put("dataStatus", AppConstants.DATASTATUS_DEL);
			map.put("id", Integer.parseInt(id));
			return this.excuteByHql(hql, map, "逻辑删除");
		}
		return 0;
	}
	
}