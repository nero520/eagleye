package com.yougou.eagleye.admin.services;



import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yougou.eagleye.admin.domain.EagleyeAlert;
import com.yougou.eagleye.core.junit.SpringBaseTest;

public class AlertManageServiceTest extends SpringBaseTest{

	@Autowired
	private AlertManageService alertManageService;
	
	@Test
	public void getAlertInfoByAppName(){
		
	}
	
	
	@Test
	public void updateRuleListToRedis(){
		try {
			this.alertManageService.updateRuleListToCache();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
