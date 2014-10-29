package com.yougou.eagleye.admin.services;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yougou.eagleye.admin.domain.EagleyeGroup;
import com.yougou.eagleye.admin.util.DtPageBean;
import com.yougou.eagleye.core.junit.SpringBaseTest;

public class GroupManageServiceTest extends SpringBaseTest{
	
	@Autowired
	private GroupManageService groupManageService;
	
	
	@Test
	public void createGroup() throws Exception{
//		for(int i=0;i<20;i++){
//			EagleyeGroup eagleyeGroup = new EagleyeGroup();
//			eagleyeGroup.setGroupName("cms000"+i);
//			EagleyeGroup group = this.groupManageService.createGroup(eagleyeGroup);
//			System.out.println(group.getFlash());
//		}
		
	}
	
	
	
	
	/**
	 * 根据id获得Group
	 * @param id
	 * @return 返回查询到的EagleyeGroup对象
	 */
	//@Test
	public void getGroupById() throws Exception{
		EagleyeGroup group = this.groupManageService.getGroupById("2");
		System.out.println(group.getGroupName());
	}
	
	
	/**
	 * 更新
	 */
	//@Test
	public void updateGroup() throws Exception{
		EagleyeGroup pageParam = new EagleyeGroup();
		pageParam.setId(1);
		pageParam.setDescription("update first data");
		EagleyeGroup group = this.groupManageService.updateGroup(pageParam);
		System.out.println(group.getFlash());
		
	}
	
	/**
	 * 简单模糊检索
	 * @param DtPageBean pageBean , String sSearch
	 * @return pageBean
	 * @throws Exception
	 */
	//@Test
	public void querySimpleGroupList() throws Exception{
		DtPageBean<EagleyeGroup> pageBean = this.groupManageService.querySimpleGroupList(new DtPageBean<EagleyeGroup>(), "002");
		System.out.println(pageBean.getResult().size());
	}
	
	
	/**
	 * 高级检索
	 * @param DtPageBean pageBean , EagleyeGroup eagleyeGroup
	 * @return pageBean
	 * @throws Exception
	 */
	//@Test
	public void queryGroupList() throws Exception{
		EagleyeGroup group = new EagleyeGroup();
		group.setGroupName("00");
		DtPageBean<EagleyeGroup> pageBean = this.groupManageService.queryGroupList(new DtPageBean<EagleyeGroup>(), group);
		System.out.println(pageBean.getResult().size());
	}
	
	
	/**
	 * 删除
	 * @param id 待删除的用户对象的id
	 */
	//@Test
	public void delete() throws Exception{
		int resultCount = this.groupManageService.delete("1");
		System.out.println(resultCount);
	}
	
	
	
	
	

}
