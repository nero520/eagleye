package com.yougou.eagleye.admin.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.yougou.eagleye.admin.domain.EagleyeLog;

public interface EagleyeLogDao extends
		ElasticsearchRepository<EagleyeLog, String> {


	/**
	 * 根据body查询
	 * @param body
	 * @param pageable
	 * @return
	 */
	Page<EagleyeLog> findByBody(String body, Pageable pageable);
	
	Page<EagleyeLog> findByAppNameAndBodyAndTimestampBetweenOrderByVersionDesc(String appName,String body, Long from, Long to, Pageable pageable);
	

}