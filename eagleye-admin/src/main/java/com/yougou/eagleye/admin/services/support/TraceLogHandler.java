package com.yougou.eagleye.admin.services.support;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.admin.dao.EagleyeTraceLogDao;
import com.yougou.eagleye.admin.domain.EagleyeSpan;
import com.yougou.eagleye.admin.domain.EagleyeTraceLog;
import com.yougou.eagleye.admin.services.TraceLogManageService;
import com.yougou.eagleye.admin.util.EagleyeAdminUtil;
import com.yougou.eagleye.trace.domain.Annotation;
import com.yougou.eagleye.trace.domain.BinaryAnnotation;
import com.yougou.eagleye.trace.domain.Span;


public class TraceLogHandler implements Runnable{
	
	private List<EagleyeSpan> eagleyeSpanList;
	
	
	public List<EagleyeSpan> getEagleyeSpanList() {
		return eagleyeSpanList;
	}

	public void setEagleyeSpanList(List<EagleyeSpan> eagleyeSpanList) {
		this.eagleyeSpanList = eagleyeSpanList;
	}

	private static ObjectMapper objectMapper = new ObjectMapper();

	private final static Logger logger = LoggerFactory.getLogger(TraceLogHandler.class);

	@Autowired
    private TraceLogManageService traceLogManageService;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(eagleyeSpanList!=null){
			List<XContentBuilder> esdocList = new ArrayList<XContentBuilder>();
			for(EagleyeSpan es : eagleyeSpanList){
				
//				//存储原始跟踪信息
//				try {
//					XContentBuilder esdoc = EagleyeTraceLog.span2Esdoc(es);
//					if(esdoc!=null){
//						esdocList.add(esdoc);
//					}
//					this.traceLogManageService.rawMultipleSaveTraceLog(esdocList);
//				} catch (Exception e) {
//					logger.error("save tracelog to elasticsearch error :" + e.getMessage());
//				}
			}
			
		}
	}

	
	
	
	
}
