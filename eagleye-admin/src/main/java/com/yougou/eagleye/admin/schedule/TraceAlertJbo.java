package com.yougou.eagleye.admin.schedule;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.admin.services.LogManageService;
import com.yougou.eagleye.admin.services.TraceLogManageService;
import com.yougou.eagleye.admin.util.EagleyeAdminUtil;
import com.yougou.eagleye.admin.util.SpringContextHolder;
import com.yougou.eagleye.core.utils.DateUtils;

@Component
public class TraceAlertJbo extends QuartzJobBean{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TraceLogManageService traceLogManageService;
	
	
	@Autowired
	private AppConstants appConstants;
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		
		
		
		//每隔两小时预警一次
		try {
			if ( traceLogManageService == null ){
				traceLogManageService = SpringContextHolder.getBean(TraceLogManageService.class);
			}
			
			traceLogManageService.traceAlert();
			
			logger.info("trace alert run " + DateUtils.getSystemDateAndTime());
			
		} catch (Exception e) {
			logger.info("Delete "+ DateUtils.addDay(DateUtils.getSystemDate(), -7) +" tracelog at 7 days ago error ", e);
		}
		
		
	}


		

}
