package com.yougou.eagleye.admin.services;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yougou.eagleye.admin.domain.EagleyeGroup;
import com.yougou.eagleye.admin.domain.EagleyeUserGroup;
import com.yougou.eagleye.admin.util.DtPageBean;
import com.yougou.eagleye.core.junit.SpringBaseTest;

public class UserGroupManageServiceTest extends SpringBaseTest{
	
	@Autowired
	private GroupManageService groupManageService;
	
	@Autowired
	private UserGroupManageService userGroupManageService;
	
	@Test
	public void createUserGroup() throws Exception{
//		EagleyeGroup eagleyeGroup = this.groupManageService.getGroupById("2");
//		if(eagleyeGroup!=null){
//			EagleyeUserGroup userGroup = new EagleyeUserGroup();
//			userGroup.setEagleyeGroup(eagleyeGroup);
//			this.userGroupManageService.createUserGroup(userGroup);
//			System.out.println(userGroup.getFlash());
//		}
		
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
