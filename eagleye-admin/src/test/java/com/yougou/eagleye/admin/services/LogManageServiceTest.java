package com.yougou.eagleye.admin.services;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yougou.eagleye.admin.dao.EagleyeLogDao;
import com.yougou.eagleye.admin.domain.EagleyeLog;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring/*Context.xml")
public class LogManageServiceTest {
	
	@Autowired
	private LogManageService logManageService;
	
	@Autowired
	private EagleyeLogDao eagleyeLogDao;
	
	
	
	
	@Before
	public void emptyData(){
		 eagleyeLogDao.deleteAll();
	}
	
	@Test
	public void query() throws Exception{
//		EagleyeLog log1 = new EagleyeLog();
//		log1.setAppGroup("cms");
//		log1.setAppName("cms153");
//		log1.setBody("java NullPointException");
//		log1.setDate("2014-05-29 10:26:30");
//		log1.setHostname("10.10.10.153");
//		log1.setId("12345");
//		
//		EagleyeLog log2 = new EagleyeLog();
//		log2.setAppGroup("promotion");
//		log2.setAppName("promotion186");
//		log2.setBody("java NoSuchMethod");
//		log2.setDate("2014-04-21 12:26:30");
//		log2.setHostname("10.10.10.186");
//		log2.setId("456443");
//		eagleyeLogDao.save(Arrays.asList(log1, log2));
		
		Page<EagleyeLog> logPage = this.eagleyeLogDao.findByBody("java",new PageRequest(0, 10));
//		
		System.out.println(logPage.getContent().size());
	}
}
