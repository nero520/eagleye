package com.yougou.eagleye.admin.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.yougou.eagleye.admin.domain.EagleyeTraceLog;

public interface EagleyeTraceLogDao extends
		ElasticsearchRepository<EagleyeTraceLog, String> {

	//根据serviceName(接口名), spanName(方法名), traceId, spanId, 开始时间, 结束时间
	
	Page<EagleyeTraceLog> findByIpAndServiceNameAndSpanNameAndTraceIdAndSpanIdAndVersionBetweenAndInvokeTypeAndSpendTimeAfterAndExceptionsOrderByVersionDesc(String ip,String serviceName,String spanName, String traceId,
			String spanId, Long from, Long to, String invokeType, String spendTime,String exceptions,Pageable pageable);
	
	
	
	Page<EagleyeTraceLog> findBySpendTimeAfterAndIpAndServiceNameAndSpanNameAndTraceIdAndSpanIdAndVersionBetweenAndInvokeTypeAndExceptionsOrderByVersionDesc(String spendTime,  String ip,String serviceName,String spanName, String traceId,
			String spanId, Long from, Long to, String invokeType,String exceptions, Pageable pageable);
	
	
	Page<EagleyeTraceLog> findByIpAndServiceNameAndSpanNameAndTraceIdAndSpanIdAndVersionBetweenAndInvokeTypeAndSpendTimeAfterAndExceptionsAndExceptionNumNotOrderByVersionDesc(String ip,String serviceName,String spanName, String traceId,
			String spanId, Long from, Long to, String invokeType, String spendTime,String exceptions,String exceptionNum, Pageable pageable);
	
//	Page<EagleyeTraceLog> findByServiceNameAndSpanNameAndTraceIdAndSpanIdAndVersionBetweenAndInvokeTypeAndSpendTimeAfterOrderByVersionDesc(String serviceName,String spanName, String traceId,
//			String spanId, Long from, Long to, String invokeType, Long spendTime,Pageable pageable);
	
	
	Page<EagleyeTraceLog> findByTraceId(String traceId, Pageable pageable);
	
	Page<EagleyeTraceLog> findByServiceNameAndSpanNameAndVersionBetweenAndInvokeType(String serviceName,String spanName, Long from, Long to, String invokeType, Pageable pageable);

	
	
}