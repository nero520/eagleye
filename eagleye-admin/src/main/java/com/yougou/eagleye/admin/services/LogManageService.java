package com.yougou.eagleye.admin.services;





import static org.elasticsearch.index.query.FilterBuilders.rangeFilter;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.filteredQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;





import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.elasticsearch.index.query.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.facet.request.TermFacetRequestBuilder;
import org.springframework.data.elasticsearch.core.facet.result.Term;
import org.springframework.data.elasticsearch.core.facet.result.TermResult;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.yougou.eagleye.admin.dao.EagleyeElasticsearchTemplate;
import com.yougou.eagleye.admin.dao.EagleyeLogDao;
import com.yougou.eagleye.admin.domain.EagleyeLog;
import com.yougou.eagleye.admin.util.DtPageBean;
import com.yougou.eagleye.core.utils.DateUtils;


@Service("logManageService")
public class LogManageService{
	
	private final static Logger logger = LoggerFactory.getLogger(LogManageService.class);

	@Autowired
    private EagleyeLogDao eagleyeLogDao;
	
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	
	
	
	//用来存储统计数据, 过期时间为600s
	public static Cache<String, List<String>> statisticsCache = CacheBuilder.newBuilder()
																	.maximumSize(100000)
																	.expireAfterWrite(3600, TimeUnit.SECONDS)
																	.build();
	
	public static String statisticsStatus = "";

	/**
	 * 根据app名称, body查询
	 */
	public DtPageBean<EagleyeLog> findByDateAndAppNameAndBody(String date,String appName,String sSearch, DtPageBean<EagleyeLog> pageBean)throws Exception{
		
		appName = "".equals(appName)?null:appName;
		sSearch = "".equals(sSearch)?null:"\"" + sSearch + "\"";
		
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
		
		
		Page<EagleyeLog> logs = this.eagleyeLogDao.findByAppNameAndBodyAndTimestampBetweenOrderByVersionDesc(appName, sSearch, from, to, pageBean.getPageable());
		pageBean.setResult(logs.getContent());
		pageBean.setTotalCount(Integer.parseInt(String.valueOf(logs.getTotalElements())));
		pageBean.setTotalPages(logs.getTotalPages());
		
		return pageBean;
	}
	
	
	
	
	/**
	 * 获取某一天所有的服务名称
	 * @param serviceName
	 * @param spanName
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public List<String> getAllAppNameByDate(String date) throws Exception{
		List<String> appNameList = new ArrayList<String>();
		String cacheKey = "getAllAppNameByDate:"+ date;
		if(this.statisticsCache.getIfPresent(cacheKey) == null){//加上本地缓存, 1小时失效,失效后重新统计
			synchronized(this.statisticsStatus){
				if(!"running".equals(this.statisticsStatus)){//如果多个人同时点击,在第一次统计没有完成之前是不会改变状态的.
					this.statisticsStatus = "running";
					long startTime = System.currentTimeMillis();
					logger.info(" getAllServiceByDate staring , param : " + date);
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
			        QueryBuilder tempqb = boolQuery().mustNot(termQuery("id", null));
			        
					QueryBuilder qb = filteredQuery(tempqb,rangeFilter("version") 
																	.from(from)
																	.to(to)
												                    .includeLower(true)
												                    .includeUpper(true));
			        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(qb)
			                .withFacet(new TermFacetRequestBuilder(statisticFacet).fields("appName").descCount().size(50).build())
			                .build();
			        // when
 			        searchQuery.setPageable(new PageRequest(0, 1));
			        FacetedPage<EagleyeLog> result = elasticsearchTemplate.queryForPage(searchQuery, EagleyeLog.class);
			        
				    TermResult numberFacet = (TermResult) result.getFacet(statisticFacet);
				    for(Term term : numberFacet.getTerms()){
				    	logger.info("====Statistics=====" + term.getTerm());
				    	String appName = term.getTerm();
				    	appNameList.add(appName);
				    }
					this.statisticsCache.put(cacheKey, appNameList);
					this.statisticsStatus = "";
					
					long endTime = System.currentTimeMillis();
					logger.info(" getAllAppNameByDate end spend time : "+ (endTime - startTime) +"ms , param : " + date);
				}
			}
		}else{
			appNameList = this.statisticsCache.getIfPresent(cacheKey);
			this.statisticsStatus = "";
		}
		
		return appNameList;
	}

	
	public void saveAlertLog(EagleyeLog log) throws Exception{
		if(log!=null){
			this.eagleyeLogDao.save(log);
		}
	}
	
		
	/**
	 * 删除某个时间点以前的数据
	 * @param date
	 * @throws Exception
	 */
	public void deleteErrorLogByDateBefore(String date) throws Exception{
		DeleteQuery dq = new DeleteQuery();
		QueryBuilder tempqb = boolQuery().mustNot(termQuery("id", null));
		QueryBuilder qb = filteredQuery(tempqb,rangeFilter("timestamp") 
														.to(Long.parseLong(date)) 
									                    .includeLower(true)
									                    .includeUpper(true));
		dq.setQuery(qb);
		dq.setIndex("applogs");
		dq.setType("log");
		this.elasticsearchTemplate.delete(dq);
	}
	
}