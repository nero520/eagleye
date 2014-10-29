package com.yougou.eagleye.admin.schedule;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.yougou.eagleye.admin.services.LogManageService;
import com.yougou.eagleye.admin.services.TraceLogManageService;
import com.yougou.eagleye.admin.util.EagleyeAdminUtil;
import com.yougou.eagleye.admin.util.SpringContextHolder;
import com.yougou.eagleye.core.utils.DateUtils;

@Component
public class RemoveTraceLogJbo extends QuartzJobBean{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TraceLogManageService traceLogManageService;
	
	@Autowired
	private LogManageService logManageService;
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		
//		String date = EagleyeAdminUtil.dateStr2Long(
//				DateUtils.addDay(DateUtils.getSystemDate(), -7)
//						+ " 00:00:00", "yyyy-MM-dd HH:mm:ss");
		
		String date = DateUtils.addDay(DateUtils.getSystemDate(), -7);
		
		//删除七天之前的跟踪日志数据
		try {
			if ( traceLogManageService == null ){
				traceLogManageService = SpringContextHolder.getBean(TraceLogManageService.class);
			}
			
			traceLogManageService.deleteTraceLogByDateBefore(date);
			logger.info("========Delete "+ DateUtils.addDay(DateUtils.getSystemDate(), -7) + ": " + date +" tracelog at 7 days ago successful ! ===========");
		} catch (Exception e) {
			logger.info("Delete "+ DateUtils.addDay(DateUtils.getSystemDate(), -7) +" tracelog at 7 days ago error ", e);
		}
		
		
		//删除七天之前的error数据
		try {
			
			if ( logManageService == null ){
				logManageService = SpringContextHolder.getBean(LogManageService.class);
			}
			
			
			logManageService.deleteErrorLogByDateBefore(EagleyeAdminUtil.dateStr2Long(DateUtils.addDay(DateUtils.getSystemDate(), -7) + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
			logger.info("========Delete "+ DateUtils.addDay(DateUtils.getSystemDate(), -7) + ": " + date +" errorlog at 7 days ago successful ! ===========");
		} catch (Exception e) {
			logger.info("Delete "+ DateUtils.addDay(DateUtils.getSystemDate(), -7) +" errorlog at 7 days ago error ", e);
		}
		
	}


		

}
