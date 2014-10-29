package com.yougou.eagleye.admin.services;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.FilterBuilders.rangeFilter;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.filteredQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.facet.request.StatisticalFacetRequest;
import org.springframework.data.elasticsearch.core.facet.request.TermFacetRequestBuilder;
import org.springframework.data.elasticsearch.core.facet.result.Term;
import org.springframework.data.elasticsearch.core.facet.result.TermResult;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.admin.dao.EagleyeElasticsearchTemplate;
import com.yougou.eagleye.admin.dao.EagleyeTraceLogDao;
import com.yougou.eagleye.admin.domain.EagleyeTraceLog;
import com.yougou.eagleye.admin.util.DtPageBean;
import com.yougou.eagleye.admin.util.EagleyeAdminUtil;
import com.yougou.eagleye.admin.vo.TraceLinkVo;
import com.yougou.eagleye.admin.vo.TraceLogStatisticsVo;
import com.yougou.eagleye.core.utils.DateUtils;

@Service("dubboFilterLogManageService")
public class TraceLogManageService{
	
	private final static Logger logger = LoggerFactory.getLogger(TraceLogManageService.class);

	@Autowired
    private EagleyeTraceLogDao eagleyeTraceLogDao;
	
	@Autowired
	private RedisTemplate<String, String> traceRedisTemplate;
	
	@Autowired
	private EagleyeElasticsearchTemplate eagleyeElasticsearchTemplate;
	
	@Autowired
	private AppConstants appConstants;
	
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	//用来存储统计数据, 过期时间为600s
	public static Cache<String, List<TraceLogStatisticsVo>> statisticsCache = CacheBuilder.newBuilder()
																	.maximumSize(100000)
																	.expireAfterWrite(600, TimeUnit.SECONDS)
																	.build();
	
	public static String statisticsStatus = "";
	

	//根据serviceName(接口名), spanName(方法名), traceId, spanId, 开始时间, 结束时间
	
	public DtPageBean<EagleyeTraceLog> queryTraceLog(String ip, String serviceName,String spanName, String traceId,
			String spanId, String date,String invokeType, String spendTime,String exceptionNum, String exceptions,DtPageBean<EagleyeTraceLog> pageBean) throws Exception{
		
		ip = this.formatStringParam(ip);
		serviceName = this.formatStringParam(serviceName);
		spanName = this.formatStringParam(spanName);
		traceId = this.formatStringParam(traceId);
		spanId = this.formatStringParam(spanId);
		Long from = null;
		Long to = null;
		//如果两个时间区间其中为空串,则今天的起始时间或末尾时间
		if(date==null || "".equals(date)){//如果没有设置查询时间, 则默认查找当天的
			from = DateUtils.getDateFromString(DateUtils.getSystemDate() + " 00:00:00" , "yyyy-MM-dd HH:mm:ss").getTime();
			to = DateUtils.getDateFromString(DateUtils.getSystemDate() + " 23:59:59" , "yyyy-MM-dd HH:mm:ss").getTime();
		}else{
			from = DateUtils.getDateFromString(date + " 00:00:00" , "yyyy-MM-dd HH:mm:ss").getTime();
			to = DateUtils.getDateFromString(date + " 23:59:59" , "yyyy-MM-dd HH:mm:ss").getTime();
		}
		
		invokeType = this.formatStringParam(invokeType);
		
		
		if(spendTime!=null && !spendTime.equals("")){
			spendTime = spendTime.replaceAll(" ", "");
			NumberFormat formatter = new DecimalFormat("00000000");
			spendTime = formatter.format(Long.parseLong(spendTime));
		}else{
			spendTime = "00000000";
		}
		exceptions = this.formatStringParam(exceptions);
		
		
		Page<EagleyeTraceLog> logs = this.eagleyeTraceLogDao
				.findByIpAndServiceNameAndSpanNameAndTraceIdAndSpanIdAndVersionBetweenAndInvokeTypeAndSpendTimeAfterAndExceptionsOrderByVersionDesc(
						ip,serviceName, spanName, traceId, spanId, from, to,invokeType,spendTime,exceptions,
						pageBean.getPageable());

		pageBean.setResult(logs.getContent());
		pageBean.setTotalCount(Integer.parseInt(String.valueOf(logs.getTotalElements())));
		pageBean.setTotalPages(logs.getTotalPages());
		
		return pageBean;
	}
	
	
	
	/**
	 * 获取异常跟踪
	 * @param ip
	 * @param serviceName
	 * @param spanName
	 * @param traceId
	 * @param spanId
	 * @param date
	 * @param invokeType
	 * @param spendTime
	 * @param exceptionNum
	 * @param exceptions
	 * @param pageBean
	 * @return
	 * @throws Exception
	 */
	public DtPageBean<EagleyeTraceLog> queryErrorTraceLog(String ip, String serviceName,String spanName, String traceId,
			String spanId, String fromTime,String toTime,String invokeType,String isException,DtPageBean<EagleyeTraceLog> pageBean) throws Exception{
		ip = this.formatStringParam(ip);
		serviceName = this.formatStringParam(serviceName);
		spanName = this.formatStringParam(spanName);
		traceId = this.formatStringParam(traceId);
		spanId = this.formatStringParam(spanId);
		Long from = null;
		Long to = null;
		
		if(fromTime==null || "".equals(fromTime)){//如果没有设置查询时间, 则默认查找当天的
			from = DateUtils.getDateFromString(DateUtils.getSystemDate() + " 00:00:00" , "yyyy-MM-dd HH:mm:ss").getTime();
		}else{
			from = DateUtils.getDateFromString(fromTime , "yyyy-MM-dd HH:mm:ss").getTime();
		}
		
		if(toTime==null || "".equals(toTime)){//如果没有设置查询时间, 则默认查找当天的
			to = DateUtils.getDateFromString(DateUtils.getSystemDate() + " 23:59:59" , "yyyy-MM-dd HH:mm:ss").getTime();
		}else{
			to = DateUtils.getDateFromString(toTime , "yyyy-MM-dd HH:mm:ss").getTime();
		}
		
		invokeType = this.formatStringParam(invokeType);
		
		
		NumberFormat formatter = new DecimalFormat("00000000");
		String spendTime = formatter.format(Long.parseLong(AppConstants.SPEND_TIME_THRESHOLD + ""));
		Page<EagleyeTraceLog> logs = null;
		if(isException==null || "".equals(isException) || "0".equals(isException)){
			logs = this.eagleyeTraceLogDao
					.findBySpendTimeAfterAndIpAndServiceNameAndSpanNameAndTraceIdAndSpanIdAndVersionBetweenAndInvokeTypeAndExceptionsOrderByVersionDesc(
							spendTime,ip,serviceName, spanName, traceId, spanId, from, to,invokeType ,null,
							pageBean.getPageable());
		}else{//需要包含有异常的log
			logs = this.eagleyeTraceLogDao
					.findByIpAndServiceNameAndSpanNameAndTraceIdAndSpanIdAndVersionBetweenAndInvokeTypeAndSpendTimeAfterAndExceptionsAndExceptionNumNotOrderByVersionDesc(
							ip,serviceName, spanName, traceId, spanId, from, to,invokeType,"00000000" ,null,"0",
							pageBean.getPageable());
		}
		
		
		

		pageBean.setResult(logs.getContent());
		pageBean.setTotalCount(Integer.parseInt(String.valueOf(logs.getTotalElements())));
		pageBean.setTotalPages(logs.getTotalPages());
		
		return pageBean;
	}
	
	
	/**
	 * 根据traceId获取整个调用链
	 */
	public List<TraceLinkVo> getTraceLogListByTraceId(String traceId) throws Exception{
		List<TraceLinkVo> logList = new ArrayList<TraceLinkVo>();
		if(traceId!=null && !traceId.equals("")){
			traceId = "\"" + traceId + "\"";
			DtPageBean<EagleyeTraceLog> pageBean = new DtPageBean<EagleyeTraceLog>();
			pageBean.setPageSize(1000);
			pageBean.setPageNow(1);
			Page<EagleyeTraceLog> logs = this.eagleyeTraceLogDao.findByTraceId(traceId, pageBean.getPageable());
			
			//合并consumer与provider
			Map<String, TraceLinkVo> mergeMap = new HashMap<String, TraceLinkVo>();
			if(logs!=null && logs.getContent()!=null){
				for(EagleyeTraceLog log : logs.getContent()){
					String spanId = log.getSpanId();
					if(mergeMap.get(spanId)==null){
						mergeMap.put(spanId, TraceLinkVo.traceLog2LinkVo(log, new TraceLinkVo()));
					}else{
						TraceLinkVo tlv = mergeMap.get(spanId);
						mergeMap.put(spanId, TraceLinkVo.traceLog2LinkVo(log, tlv));
					}
				}
			
				//按照startTime进行正序排序
				Map<String, TraceLinkVo> tempMap = new TreeMap<String,TraceLinkVo>();
			
				for(Map.Entry<String, TraceLinkVo> entry : mergeMap.entrySet()){
					String startTime = entry.getValue().getCst();
					if(startTime==null && "".equals(startTime)){//保证该值有效
						startTime = entry.getValue().getPst();
					}
					tempMap.put(startTime, entry.getValue());
				}
				for(Map.Entry<String, TraceLinkVo> entry : tempMap.entrySet()){
					logList.add(entry.getValue());
				}
			}
			
		}
		return logList;
	}
	
	
	
	/**
	 * 根据id获取跟踪日志对象
	 */
	public EagleyeTraceLog getTraceLogById(String id) throws Exception{
		EagleyeTraceLog traceLog = new EagleyeTraceLog();
		if(id!=null && !"".equals(id)){
			
			QueryBuilder qb = boolQuery().must(termQuery("_id", id));
			SearchResponse searchResponse = eagleyeElasticsearchTemplate.getClient().prepareSearch("tracelogs*") 
			        .setQuery(qb) 
			        .setFrom(0).setSize(1).setExplain(true) 
			        .execute() 
			        .actionGet();	
			SearchHits hits = searchResponse.getHits();
	        for (int i = 0; i < 1; i++) {
	        	traceLog.setId(id);
	        	traceLog.setExceptions(hits.getAt(i).getSource().get("exceptions").toString());
	        }
		}
		return traceLog;
	}
	
	
//	/**
//	 * 统计某一个方法一天之内被哪些服务调用情况
//	 * @param serviceName
//	 * @param spanName
//	 * @param date
//	 * @return
//	 * @throws Exception
//	 */
//	public DtPageBean<TraceLogStatisticsVo> countTraceLogByServiceNameAndSpanName(String serviceName, String spanName, String date,DtPageBean<TraceLogStatisticsVo> resultPageBean) throws Exception{
//		serviceName = this.formatStringParam(serviceName);
//		spanName = this.formatStringParam(spanName);
//		Long from = null;
//		Long to = null;
//		//如果两个时间区间其中为空串,则今天的起始时间或末尾时间
//		if(date==null || "".equals(date)){//如果没有设置查询时间, 则默认查找当天的
//			from = DateUtils.getDateFromString(DateUtils.getSystemDate() + " 00:00:00" , "yyyy-MM-dd HH:mm:ss").getTime();
//			to = DateUtils.getDateFromString(DateUtils.getSystemDate() + " 23:59:59" , "yyyy-MM-dd HH:mm:ss").getTime();
//		}else{
//			from = DateUtils.getDateFromString(date + " 00:00:00" , "yyyy-MM-dd HH:mm:ss").getTime();
//			to = DateUtils.getDateFromString(date + " 23:59:59" , "yyyy-MM-dd HH:mm:ss").getTime();
//		}
//		
//		DtPageBean<EagleyeTraceLog> pageBean = new DtPageBean<EagleyeTraceLog>();
//		pageBean.setPageSize(1000000);
//		pageBean.setPageNow(1);
//		
//		Page<EagleyeTraceLog> logs = this.eagleyeTraceLogDao.findByServiceNameAndSpanNameAndVersionBetweenAndInvokeType(serviceName, spanName, from, to,AppConstants.DUBBO_INVOKE_TYPE_CONSUMER, pageBean.getPageable());
//		logger.info("=====dubbo inferface: " + serviceName + ":" + spanName +" on "+ date + " is invoked ["+ logs.getTotalElements() + "]");
//		List<TraceLogStatisticsVo> tlsvList = new ArrayList<TraceLogStatisticsVo>();
//		if(logs!=null && logs.getContent()!=null){
//			Map<String, String> map = new HashMap<String, String>();
//			for(EagleyeTraceLog log : logs.getContent()){
//				String logKey = log.getAppName() + "#" + log.getIp();
//				String logValue = map.get(logKey); // value的格式为 eg: 12#2445
//				if(logValue!= null){
//					String[] strs = logValue.split("#");
//					if(strs.length==2){
//						Integer invokeCount = Integer.parseInt(strs[0]) + 1;
//						Long totleSpendTime = Long.parseLong(strs[1]) + Long.parseLong(log.getSpendTime());
//						map.put(logKey, invokeCount+"#"+totleSpendTime);
//					}
//					
//				}else{
//					map.put(logKey, 1 + "#" + Long.parseLong(log.getSpendTime()));
//				}
//			}
//			
//			for(Map.Entry<String, String> entry : map.entrySet()){
//				String[] keyStrs = entry.getKey().split("#");
//				String[] valueStrs = entry.getValue().split("#");
//				if(keyStrs.length==2 && valueStrs.length==2){
//					TraceLogStatisticsVo tlsv = new TraceLogStatisticsVo();
//					tlsv.setAppName(keyStrs[0]);
//					tlsv.setIp(keyStrs[1]);
//					tlsv.setServiceName(serviceName);
//					tlsv.setSpanName(spanName);
//					tlsv.setInvokeCount(Integer.parseInt(valueStrs[0]));
//					tlsv.setTotleSpendTime(Long.parseLong(valueStrs[1])/Long.parseLong(valueStrs[0]));
//					
//					tlsvList.add(tlsv);
//				}
//			}
//		}
//		
//		resultPageBean.setResult(tlsvList);
//		resultPageBean.setTotalCount(tlsvList.size());
//		resultPageBean.setTotalPages(1);
//		
//		return resultPageBean;
//	}
	
	
	/**
	 * 统计所有方法在一天内的调用次数, 并从多到少进行排序
	 * @param serviceName
	 * @param spanName
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public DtPageBean<TraceLogStatisticsVo> getAllServiceInvokeCountByDate(String sSearch,String date,DtPageBean<TraceLogStatisticsVo> resultPageBean) throws Exception{
		List<TraceLogStatisticsVo> tlsvList = new ArrayList<TraceLogStatisticsVo>();
		String cacheKey = "getAllServiceInvokeCountByDate:"+ date + ":"+sSearch;
		if(this.statisticsCache.getIfPresent(cacheKey) == null){//加上本地缓存, 五分钟失效,失效后重新统计
			synchronized(this.statisticsStatus){
				if(!"running".equals(this.statisticsStatus)){//如果多个人同时点击,在第一次统计没有完成之前是不会改变状态的.
					this.statisticsStatus = "running";
					long startTime = System.currentTimeMillis();
					logger.info(" statistic staring , param : " + date + " " + sSearch);
					Long from = null;
					Long to = null;
					//如果两个时间区间其中为空串,则今天的起始时间或末尾时间
					if(date==null || "".equals(date)){//如果没有设置查询时间, 则默认查找当天的
						from = DateUtils.getDateFromString(DateUtils.getSystemDate() + " 00:00:00" , "yyyy-MM-dd HH:mm:ss").getTime();
						to = DateUtils.getDateFromString(DateUtils.getSystemDate() + " 23:59:59" , "yyyy-MM-dd HH:mm:ss").getTime();
					}else{
						from = DateUtils.getDateFromString(date + " 00:00:00" , "yyyy-MM-dd HH:mm:ss").getTime();
						to = DateUtils.getDateFromString(date + " 23:59:59" , "yyyy-MM-dd HH:mm:ss").getTime();
					}
			        String statisticFacet = "statisticFacet";
			        QueryBuilder tempqb = boolQuery().mustNot(termQuery("id", null)).must(termQuery("invokeType", AppConstants.DUBBO_INVOKE_TYPE_CONSUMER));
			        
					QueryBuilder qb = filteredQuery(tempqb,rangeFilter("version") 
																	.from(from)
																	.to(to)
												                    .includeLower(true)
												                    .includeUpper(true));
			        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(qb)
			                .withFacet(new TermFacetRequestBuilder(statisticFacet).fields("statistic").descCount().size(1000).build())
			                .build();
			        // when
 			        searchQuery.setPageable(new PageRequest(0, 1));
			        FacetedPage<EagleyeTraceLog> result = eagleyeElasticsearchTemplate.queryForPage(searchQuery, EagleyeTraceLog.class);
			        
				    TermResult numberFacet = (TermResult) result.getFacet(statisticFacet);
				    for(Term term : numberFacet.getTerms()){
				    	boolean isSearch = false;
				    	logger.info("====Statistics=====" + term.getTerm());
				    	if(sSearch != null && !"".equals(sSearch.trim()) && term.getTerm().indexOf(sSearch.trim().toLowerCase())>-1){
				    		isSearch = true;
				        }
				    	if(sSearch == null || "".equals(sSearch.trim())){
				    		isSearch = true;
				    	}
				    	if(isSearch){
					    	String[] statisticNames = term.getTerm().split(":");
					    	if(statisticNames.length==3){
					    		TraceLogStatisticsVo tsv = new TraceLogStatisticsVo();
					    		tsv.setAppName(statisticNames[0]);
					    		tsv.setServiceName(statisticNames[1]);
					    		tsv.setSpanName(statisticNames[2]);
					    		tsv.setInvokeCount(term.getCount());
					    		tlsvList.add(tsv);
					    	}
				    	}
				    }
					this.statisticsCache.put(cacheKey, tlsvList);
					this.statisticsStatus = "";
					
					long endTime = System.currentTimeMillis();
					logger.info(" statistic end spend time : "+ (endTime - startTime) +"ms , param : " + date + " " + sSearch);
				}
			}
		}else{
			tlsvList = this.statisticsCache.getIfPresent(cacheKey);
			this.statisticsStatus = "";
		}
		
		resultPageBean.setResult(tlsvList);
		resultPageBean.setTotalCount(tlsvList.size());
		resultPageBean.setTotalPages(1);
		return resultPageBean;
	}
	
	
	
	
	/**
	 * 根据接口和方法名获取当天的调用次数
	 */
	public long getTraceLogCountByServiceNameAndSpanName(String serviceName, String spanName, String date) throws Exception{
		long count = 0;
		serviceName = this.formatStringParam(serviceName);
		spanName = this.formatStringParam(spanName);
		Long from = null;
		Long to = null;
		//如果两个时间区间其中为空串,则今天的起始时间或末尾时间
		if(date==null || "".equals(date)){//如果没有设置查询时间, 则默认查找当天的
			from = DateUtils.getDateFromString(DateUtils.getSystemDate() + " 00:00:00" , "yyyy-MM-dd HH:mm:ss").getTime();
			to = DateUtils.getDateFromString(DateUtils.getSystemDate() + " 23:59:59" , "yyyy-MM-dd HH:mm:ss").getTime();
		}else{
			from = DateUtils.getDateFromString(date + " 00:00:00" , "yyyy-MM-dd HH:mm:ss").getTime();
			to = DateUtils.getDateFromString(date + " 23:59:59" , "yyyy-MM-dd HH:mm:ss").getTime();
		}
		DtPageBean<EagleyeTraceLog> pageBean = new DtPageBean<EagleyeTraceLog>();
		pageBean.setPageSize(1);
		pageBean.setPageNow(1);
		Page<EagleyeTraceLog> logs = this.eagleyeTraceLogDao.findByServiceNameAndSpanNameAndVersionBetweenAndInvokeType(serviceName, spanName, from, to, AppConstants.DUBBO_INVOKE_TYPE_CONSUMER, pageBean.getPageable());
		if(logs!=null && logs.getContent()!=null){
			count = logs.getTotalElements();
		}
		logs = null;
		return count;
	}
	
	
	/**
	 * 获取最近半个小时内的预警信息, 进行预警
	 * @throws Exception
	 */
	public void traceAlert()throws Exception{
		if(appConstants.isTraceAlertStatus() && !appConstants.isStorageStatus()){//不开启存储的节点, 才进行预警
			DtPageBean<EagleyeTraceLog> pageBean = new DtPageBean<EagleyeTraceLog>();
			pageBean.setPageSize(1000);
			pageBean.setPageNow(1);
			
			//获取最近半个小时之内的预警信息
			Long before = System.currentTimeMillis() - 1800000;
			NumberFormat formatter = new DecimalFormat("00000000");
			String spendTime = formatter.format(Long.parseLong(AppConstants.SPEND_TIME_THRESHOLD + ""));
			
			//所有超时
			Page<EagleyeTraceLog> logPageBean1 = this.eagleyeTraceLogDao
			.findBySpendTimeAfterAndIpAndServiceNameAndSpanNameAndTraceIdAndSpanIdAndVersionBetweenAndInvokeTypeAndExceptionsOrderByVersionDesc(
					spendTime,null,null,null, null, null, before, System.currentTimeMillis() ,null ,null, pageBean.getPageable());
			
			//所有异常
			Page<EagleyeTraceLog> logPageBean2 = this.eagleyeTraceLogDao
					.findByIpAndServiceNameAndSpanNameAndTraceIdAndSpanIdAndVersionBetweenAndInvokeTypeAndSpendTimeAfterAndExceptionsAndExceptionNumNotOrderByVersionDesc(
							null,null, null, null, null, before, System.currentTimeMillis(),null,"00000000" ,null,"0",
							pageBean.getPageable());
			
			//排重
			Map<String,EagleyeTraceLog> totalMap = new HashMap<String,EagleyeTraceLog>();
			if(logPageBean1!=null){
				for(EagleyeTraceLog tempLog : logPageBean1.getContent()){
					totalMap.put(tempLog.getSpanId(), tempLog);
				}
			}
			if(logPageBean2!=null){
				for(EagleyeTraceLog tempLog : logPageBean2.getContent()){
					totalMap.put(tempLog.getSpanId(), tempLog);
				}
			}
			
			if(totalMap.size()!=0){
				String alertMsg = "";
				String alertTitle = "Dubbo alert in ";
				Map<String, List<EagleyeTraceLog>> tempMap = new HashMap<String, List<EagleyeTraceLog>>();
				
				for(Map.Entry<String, EagleyeTraceLog> entry : totalMap.entrySet()){
						EagleyeTraceLog log = entry.getValue();
						String tempKey = log.getServiceName() + ":" + log.getSpanName();
						List<EagleyeTraceLog> tempValue = null;
						if(tempMap.get(tempKey)==null){
							tempValue = new ArrayList<EagleyeTraceLog>();
						}else{
							tempValue = tempMap.get(tempKey);
						}
						tempValue.add(log);
						tempMap.put(tempKey, tempValue);
				}
				
				int count = 0;
				
				alertMsg += "<font color='blue'><b>RPC预警日志说明: <br/>第一行会显示在最近半个小时内请求时间超过"+AppConstants.SPEND_TIME_THRESHOLD/1000+"秒,或者有异常出现的dubbo接口数量<br/>";
				alertMsg += "接下来列出了是最近半小时内预警的dubbo接口详细信息,蓝色加粗部分为具体的接口名称,预警的次数,平均耗时.<br/>";
				alertMsg += "针对每一个dubbo接口下面会有详细的调用信息,用中括号标注.<br/>";
				alertMsg += "内容依次为:<br/>";
				alertMsg += "1. 日志类型,consumer是消费方,provider是提供方.<br/>";
				alertMsg += "2. 如果是consumer,则是提供方应用名; 如果是provider,则是提供方应用名<br/>";
				alertMsg += "3. 应用所在的IP.<br/>";
				alertMsg += "4. 如果是consumer,则是调用时间; 如果是provider,则是提供时间.<br/>";
				alertMsg += "5. 如果是consumer,则是调用时长; 如果是provider,则是提供时长.(单位秒)<br/>";
				alertMsg += "6. 对应日志的SpanId,根据该SpanId可以去平台中查看详细信息.<br/>";
				alertMsg += "7. 产生的异常,只截取前100个字符<br/>";
				alertMsg += "</b></font><br/><br/>";
				alertMsg += "<font color='red'><b>all alert amount: [" + tempMap.size() + "]</b></font><br/><br/>";
				for(Map.Entry<String, List<EagleyeTraceLog>> entry : tempMap.entrySet()){
					count ++;
					if(count <= 100){
						String tempKey = entry.getKey();
						List<EagleyeTraceLog> tempValue = entry.getValue();
						alertTitle += tempKey.split(":")[1] + "; ";
						
						String alertInfo = "";
						long totalSpendTime = 0;
						for(int i=0;i<tempValue.size();i++){
							EagleyeTraceLog tempLog = tempValue.get(i);
							long tempSpendTime = Long.parseLong(tempLog.getSpendTime());
							String tempException = tempLog.getExceptions();
							if(tempException.length()>100){
								tempException = tempException.substring(0, 120);
							}
							alertInfo += "[" + tempLog.getInvokeType()+ "][" +"[" + tempLog.getAppName()+ "][" + tempLog.getIp()+ "][" + EagleyeAdminUtil.long2DateStr(tempLog.getStartTime()) + "][" + tempSpendTime/1000 + "][" + tempLog.getSpanId() + "]["+ tempException + "]<br/>";
							totalSpendTime += tempSpendTime;
						}
						
						alertMsg += "<font color='blue'><b>["+ count +"] Interface : " + tempKey + " [Alert count: "+ tempValue.size() +"][Avg spend time: "+ totalSpendTime/(1000*tempValue.size()) +"]</b></font><br/>";
						alertMsg += alertInfo;
					}
					alertMsg += "<br/><br/>";
				}
				
				EagleyeAdminUtil.sendEmail(alertTitle, alertMsg, "eagleye@yougou.com", appConstants.getSendEmailUrl());
				
				logger.info("========Trace Alert  at "+ DateUtils.getSystemDateAndTime() + ", exception num : "+ totalMap.size() +"===========");
			}
		}
	}


	
	
	
	public void saveTraceLog(EagleyeTraceLog traceLog) throws Exception{
		if(traceLog!=null){
			this.eagleyeTraceLogDao.save(traceLog);
		}
	}
	
	
	/**
	 * 操作原生api进行单个保存
	 * @param traceLog
	 * @throws Exception
	 */
	public void rawSaveTraceLog(XContentBuilder esdoc) throws Exception{
		if(esdoc!=null){
			String index = AppConstants.TRACE_LOG_PREFIX + DateUtils.getSystemDate();
			eagleyeElasticsearchTemplate.getClient().prepareIndex(index, AppConstants.TRACE_LOG_TYPE).setSource(esdoc).execute().actionGet();
		}
	}
	
	/**
	 * 操作原生api进行批量保存
	 * @param traceLog
	 * @throws Exception
	 */
	public void rawMultipleSaveTraceLog(List<XContentBuilder> esdocList) throws Exception{
		if(esdocList!=null){
			String index = AppConstants.TRACE_LOG_PREFIX + DateUtils.getSystemDate();
			BulkRequestBuilder bulkRequest = eagleyeElasticsearchTemplate.getClient().prepareBulk();
			for(XContentBuilder esdoc : esdocList){
				bulkRequest.add(eagleyeElasticsearchTemplate.getClient().prepareIndex(index, AppConstants.TRACE_LOG_TYPE).setSource(esdoc));
			}
			BulkResponse bulkResponse = bulkRequest.execute().actionGet();
			if(bulkResponse.hasFailures()){
				logger.error("==== multipleSaveTraceLog error : " + bulkResponse.buildFailureMessage());
			}
		}
	}
	
	
	/**
	 * 删除某个时间点以前的数据
	 * @param date
	 * @throws Exception
	 */
	public void deleteTraceLogByDateBefore(String date) throws Exception{
		DeleteQuery dq = new DeleteQuery();
		QueryBuilder tempqb = boolQuery().mustNot(termQuery("id", null));
//		QueryBuilder qb = filteredQuery(tempqb,rangeFilter("version") 
//														.to(Long.parseLong(date)) 
//									                    .includeLower(true)
//									                    .includeUpper(true));
		dq.setQuery(tempqb);
		dq.setIndex(AppConstants.TRACE_LOG_PREFIX + date);
		dq.setType("log");
		this.eagleyeElasticsearchTemplate.delete(dq);
	}
	
		
	private String formatStringParam(String stringParam){
		String result = null;
		if(stringParam!=null && !"".equals(stringParam)){
			result = "\"" + stringParam + "\"";
		}
		return result;
	}
	
	
	
	
}