 
package com.yougou.eagleye.admin.controller;

import javax.servlet.http.HttpServletRequest;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yougou.eagleye.admin.domain.EagleyeTraceLog;
import com.yougou.eagleye.admin.domain.EagleyeGroup;
import com.yougou.eagleye.admin.services.TraceLogManageService;
import com.yougou.eagleye.admin.util.DtPageBean;
import com.yougou.eagleye.admin.util.EagleyeAdminUtil;
import com.yougou.eagleye.admin.vo.TraceLinkVo;
import com.yougou.eagleye.admin.vo.TraceLogStatisticsVo;

@Controller
@RequestMapping("/trace")
public class TraceLogManageController extends BaseController{

	@Autowired
	private TraceLogManageService traceLogManageService;
	
	/**
	 * 打开管理页面
	 */
	@RequestMapping(value = "/index")
	public ModelAndView index(HttpServletRequest request)throws Exception{
		return new ModelAndView("/trace/index");
	}
	
	
	
	/**
	 * 进入检索调用链的页面
	 * @param request
	 * @param eagleyeDubboFilterLog
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/errorinvoke")
	public ModelAndView invokelink(HttpServletRequest request)throws Exception{
		return new ModelAndView("/trace/errorinvoke");
	}
	
	
	/**
	 * 进入统计页面
	 * @param request
	 * @param eagleyeDubboFilterLog
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/statistics")
	public ModelAndView statistics(HttpServletRequest request)throws Exception{
		
		return new ModelAndView("/trace/statistics");
	}
	
	
	/**
	 * 简单查询
	 * 
	 * @param pageBean
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/query")
	@ResponseBody
	public Map<String,Object> query(HttpServletRequest request,DtPageBean<EagleyeTraceLog> pageBean,String ip,String serviceName,String spanName, String sSearch,
			String spanId,String date,String invokeTypeSelect,String spendTime,String exceptionNum,String exceptions) throws Exception{
		Map<String, Object> modelMap = new HashMap<String, Object>();
		this.traceLogManageService.queryTraceLog(ip,serviceName, spanName, sSearch, spanId, date, invokeTypeSelect, spendTime, exceptionNum,exceptions, pageBean);
		this.formatDtPageBean(modelMap, pageBean);
		return modelMap;
	}
	
	
	
	
	/**
	 * 异常跟踪检索
	 * 
	 * @param pageBean
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/queryerrorinvoke")
	@ResponseBody
	public Map<String,Object> queryerrorinvoke(HttpServletRequest request,DtPageBean<EagleyeTraceLog> pageBean,String ip,String serviceName,String spanName, String sSearch,
			String spanId,String fromTime,String toTime,String invokeTypeSelect,String isException) throws Exception{
		Map<String, Object> modelMap = new HashMap<String, Object>();
		this.traceLogManageService.queryErrorTraceLog(ip, serviceName, spanName, sSearch, spanId, fromTime, toTime, invokeTypeSelect, isException, pageBean);
		this.formatDtPageBean(modelMap, pageBean);
		return modelMap;
	}
	
	
	
	/**
	 * 查询统计结果
	 * 
	 * @param pageBean
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/querystatistics")
	@ResponseBody
	public Map<String,Object> querystatistics(HttpServletRequest request,DtPageBean<TraceLogStatisticsVo> pageBean,String sSearch, String serviceName, String spanName, String date) throws Exception{
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//按照方法统计
//		if(serviceName!=null && !serviceName.equals("") && spanName!=null && !spanName.equals("")){
//			pageBean = this.traceLogManageService.countTraceLogByServiceNameAndSpanName(serviceName, spanName, date,pageBean);
//		}else{//统计所有接口的调用
		if(!date.equals("undefined")){
			pageBean = this.traceLogManageService.getAllServiceInvokeCountByDate(sSearch,date,pageBean);
		}
//		}
		
		this.formatDtPageBean(modelMap, pageBean);
		return modelMap;
	}
	
	
	/**
	 * 根据traceId获取跟踪日志列表
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/linkshow/{traceId}") 
	public ModelAndView linkshow(HttpServletRequest request,@PathVariable("traceId")String traceId) throws Exception{
		List<TraceLinkVo> traceLinkVoList = this.traceLogManageService.getTraceLogListByTraceId(traceId);
		request.setAttribute("traceLinkVoList", traceLinkVoList);
		request.setAttribute("traceId", traceId);
		return new ModelAndView("/trace/linkshow");
	}
	
	
	/**
	 * 根据traceId获取跟踪日志列表
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/errorshow/{id}") 
	public ModelAndView errorshow(HttpServletRequest request,@PathVariable("id")String id) throws Exception{
		EagleyeTraceLog log = this.traceLogManageService.getTraceLogById(id);
		String errorMsg = "";
		if(log!=null && log.getExceptions()!=null){
			String[] strs = log.getExceptions().split("#\\$\\@");
			for(String str : strs){
				errorMsg = str + "<br/><br/>";
			}
			
		}
		request.setAttribute("errorMsg", errorMsg);
		request.setAttribute("id", id);
		return new ModelAndView("/trace/errorshow");
	}
	
	
}