package com.yougou.eagleye.admin.services;


import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.admin.dao.EagleyeGroupDao;
import com.yougou.eagleye.admin.domain.EagleyeGroup;
import com.yougou.eagleye.admin.domain.EagleyeUserGroup;
import com.yougou.eagleye.admin.util.DtPageBean;
import com.yougou.eagleye.core.reflection.ReflectionUtils;

@Service("groupManageService")
@Transactional
public class GroupManageService{
	
	private final static Logger logger = LoggerFactory.getLogger(GroupManageService.class);

	@Autowired
	private EagleyeGroupDao eagleyeGroupDao;
	
	@Autowired
	private UserGroupManageService userGroupService;
	
	@Autowired
	private AlertManageService alertManageService;
	
	/**
	 * 根据id获得Group
	 * @param id
	 * @return 返回查询到的EagleyeGroup对象
	 */
	public EagleyeGroup getGroupById(String id) throws Exception{
		EagleyeGroup group = eagleyeGroupDao.getEagleyeGroupById(id);
		group.getEagleyeUserGroupList();
		return group;
	}
	
	/**
	 * 更新
	 * @param 页面传入的对象
	 * @return 返回更新后的用户对象
	 */
	public EagleyeGroup updateGroup(EagleyeGroup pageParam) throws Exception{
		EagleyeGroup group = null;
		if(pageParam!=null){
			//name需要排重
			if(eagleyeGroupDao.verifyRepeat(pageParam)){//有重复数据
				pageParam.setFlash(AppConstants.FLASH_REPEAT);
				group = pageParam;
			}else{//没有重复的
				group = eagleyeGroupDao.getEagleyeGroupById(pageParam.getId()+"");
				//删除之前的关联usergroup数据
				this.userGroupService.deleteByGroupId(group.getId()+"");
				ReflectionUtils.mergeObject(group, pageParam);
				group.setUpdateTime(new Date());
				//重新添加新的对应成员
				String[] userAccounts = group.getUserAccounts();
				if(userAccounts!=null){
					for(String userAccount : userAccounts){
						EagleyeUserGroup userGroup = new EagleyeUserGroup();
						userGroup.setGroupId(group.getId());
						userGroup.setUserAccount(userAccount);
						this.userGroupService.createUserGroup(userGroup);
					}
				}
				group = eagleyeGroupDao.createOrUpdate(group);
			}
		}
//		更新redis预警规则
		this.alertManageService.updateRuleListToCache();
		return group;
	}
	
	/**
	 * 新增
	 * @param eagleyeGroup 待更新的对象
	 * @return 返回更新后的对象
	 */
	public EagleyeGroup createGroup(EagleyeGroup eagleyeGroup) throws Exception{
		if(eagleyeGroup!=null){
			eagleyeGroup.setDataStatus(AppConstants.DATASTATUS_VALID);
			eagleyeGroup.setCreateTime(new Date());
			eagleyeGroup.setUpdateTime(new Date());
			//name需要排重
			if(eagleyeGroupDao.verifyRepeat(eagleyeGroup)){//有重复数据
				eagleyeGroup.setFlash(AppConstants.FLASH_REPEAT);
			}else{//没有重复的
				if(eagleyeGroup.getGroupName()==null){//用户名不能为空
					eagleyeGroup.setFlash(AppConstants.FLASH_ERROR);
				}else{
					eagleyeGroup = eagleyeGroupDao.createOrUpdate(eagleyeGroup);
					
					if(eagleyeGroup.getUserAccounts()!=null){
						for(String userAccount : eagleyeGroup.getUserAccounts()){
							EagleyeUserGroup userGroup = new EagleyeUserGroup();
							userGroup.setGroupId(eagleyeGroup.getId());
							userGroup.setUserAccount(userAccount);
							this.userGroupService.createUserGroup(userGroup);
						}
					}
				}
				
			}
			
		}
//		更新redis预警规则
		this.alertManageService.updateRuleListToCache();
		return eagleyeGroup;
	}
	
	
	
	
	
	/**
	 * 删除
	 * @param id 待删除的用户对象的id
	 */
	public int delete(String id) throws Exception{
		int resultCount = eagleyeGroupDao.delete(id);
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
	public DtPageBean<EagleyeGroup> querySimpleGroupList(DtPageBean<EagleyeGroup> pageBean,String sSearch) throws Exception{
		pageBean = this.eagleyeGroupDao.querySimplePage(pageBean, sSearch);
		return pageBean;
	}
	
	/**
	 * 高级检索
	 * @param DtPageBean pageBean , EagleyeGroup eagleyeGroup
	 * @return pageBean
	 * @throws Exception
	 */
	public DtPageBean<EagleyeGroup> queryGroupList(DtPageBean<EagleyeGroup> pageBean,EagleyeGroup eagleyeGroup) throws Exception{
		pageBean=this.eagleyeGroupDao.queryPage(pageBean, eagleyeGroup);
		return pageBean;
	}
	
	
	/**
	 * 获取用户组列表
	 * @return
	 * @throws Exception
	 */
	public List<EagleyeGroup> getList() throws Exception{
		List<EagleyeGroup> groupList = this.eagleyeGroupDao.queryList(null);
		return groupList;
	}
	
	
}