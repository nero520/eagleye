package com.yougou.eagleye.admin.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.yougou.eagleye.admin.constants.AppConstants;
import com.yougou.eagleye.admin.domain.EagleyeAlert;
import com.yougou.eagleye.admin.domain.EagleyeSpan;
import com.yougou.eagleye.admin.domain.EagleyeTraceLog;
import com.yougou.eagleye.admin.services.support.TraceLogHandler;
import com.yougou.eagleye.admin.util.EagleyeAdminUtil;
import com.yougou.eagleye.admin.util.HttpClientUtil;
import com.yougou.eagleye.core.security.encoder.Md5PwdEncoder;
import com.yougou.eagleye.trace.domain.Span;

public class ConsumeTraceLogService {
	
	private final static Logger logger = LoggerFactory.getLogger(ConsumeTraceLogService.class);

	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	public static ThreadLocal<AtomicLong> count = new ThreadLocal<AtomicLong>();
	
	public static ThreadLocal<Long> lastTime = new ThreadLocal<Long>();
	
	public static ThreadLocal<List<XContentBuilder>> esdocList = new ThreadLocal<List<XContentBuilder>>();
	
	

	@Autowired
	private RedisTemplate<String, String> traceRedisTemplate;
	
	
	@Autowired
	private AppConstants appConstants;
	
//	@Autowired
//	private ThreadPoolTaskExecutor traceLogHandlerTaskExecutor;
	
	@Autowired
    private TraceLogManageService traceLogManageService;
	
	
	
	// pass the channel/pattern as well
	public void handleMessage(Serializable message, String channel) {
		String msgStr = message.toString();
		this.run(msgStr);
	}
	
	
	
	private void run(String msgStr){
		logger.debug("***********trace log redis message : " + msgStr );
		if(appConstants.isStorageStatus()){//是否开启存储
			try {
				//根据msgStr生成md5值作为该span在elasticsearch中的唯一标识, 如果有多个客户端进行存储操作, 安装es的默认操作统一id会进行覆盖,不会出现重复数据
				String id = Md5PwdEncoder.encryptPasswordNoDepend(msgStr, "eagleye");
				Span span = objectMapper.readValue(msgStr, Span.class);
				if(span!=null && id!=null){
					EagleyeSpan eagleyeSpan = new EagleyeSpan();
					eagleyeSpan.setUuid(id);
					eagleyeSpan.setSpan(span);
					if(this.esdocList.get()==null){
						this.esdocList.set(new ArrayList<XContentBuilder>());
					}
					this.esdocList.get().add(EagleyeTraceLog.span2Esdoc(span,id));
					//采用多线程进行页面生成
					if(isSave()){
						//存储原始跟踪信息
						try {
							this.traceLogManageService.rawMultipleSaveTraceLog(esdocList.get());
						} catch (Exception e) {
							logger.error("save tracelog to elasticsearch error :" + e.getMessage());
						}
						this.esdocList.set(new ArrayList<XContentBuilder>());
						
//						TraceLogHandler tlh = (TraceLogHandler) EagleyeAdminUtil.getDynamicBean("com.yougou.eagleye.admin.services.support.TraceLogHandler", "prototype");
//						
//						tlh.setEagleyeSpanList(this.eagleyeSpanList.get());
//						this.traceLogHandlerTaskExecutor.execute(tlh);
//						tlh = null;
//						this.eagleyeSpanList.set(new ArrayList<EagleyeSpan>());
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("dubbo trace log consumer error :" + e.getMessage(),e);
			}
		}else{
			logger.debug("trace log storage closed! ");
		}
	}
	
	private boolean isSave(){
		boolean status = false;
		if(this.count.get() == null){
			this.count.set(new AtomicLong());
		}
		
		if(this.lastTime.get() == null){
			this.lastTime.set(-1L);
		}
		if(this.count.get().incrementAndGet()%100 == 0){//每100个交给一个线程进行批量保存
			status = true;
		}
		if(System.currentTimeMillis() - this.lastTime.get() > 5000){//或者五秒钟保存一次
			status = true;
		}
		if(status){
			this.lastTime.set(System.currentTimeMillis());
			this.count.get().getAndSet(0);
		}
		return status;
	}

}
