package com.yougou.eagleye.dubbo.client;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.remoting.TimeoutException;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcInvocation;
import com.yougou.eagleye.trace.domain.BinaryAnnotation;
import com.yougou.eagleye.trace.domain.Span;
import com.yougou.eagleye.dubbo.client.support.AsyncTransfer;
import com.yougou.eagleye.dubbo.client.support.Configuration;
import com.yougou.eagleye.dubbo.client.support.Tracer;
import com.yougou.eagleye.dubbo.client.support.TracerUtils;

/**
 * 
 */
@Activate(group = { Constants.PROVIDER, Constants.CONSUMER })
public class EagleyeDubboFilter implements Filter {
	
	private static Logger logger = LoggerFactory.getLogger(EagleyeDubboFilter.class);

    private Tracer tracer = Tracer.getInstance();
    
    private static Configuration configuration = Configuration.getInstance();
    
//    public static BlockingQueue<Span> spanQueue = new LinkedBlockingQueue<Span>(10000);
    

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
//    	long st1 = System.currentTimeMillis();
    	
        //是否是消费者
        Span span = null;
        String ip = null;
        RpcContext context = RpcContext.getContext();
        boolean isConsumerSide = context.isConsumerSide();//需要预先取到该值, 否则在服务调用之后该值会变化
        try {
        	if(configuration.isOpen()){//是否开启跟踪
	            //*************************************具体业务调用之前的处理 start *******************************
	        	try{
	    	    	
	    	    	//获取 interface name
	    	    	String serviceName = context.getUrl().getServiceInterface();
	    	    	String appName = context.getUrl().getParameter("application");
	    	    	//起始时间
	    	        long startTime = System.currentTimeMillis();
	    	        ip = context.getLocalAddressString();
	    	        
	    	        if (context.isConsumerSide()) { //消费者
	    	            Span parentSpan = tracer.getParentSpan();
	    	            if (parentSpan == null) { //为rootSpan
	    	                span = tracer.newSpan(context.getMethodName(), ip, serviceName,appName);//生成root Span
	    	            } else {
	    	                span = tracer.genSpan(parentSpan.getTraceId(), parentSpan.getId(), tracer.genLongId(), context.getMethodName(), parentSpan.isSample(), serviceName,appName);
	    	            }
	    	            
	    	            if(span.isSample()){
	    	            	tracer.clientSendRecord(span, ip, startTime);
	    	            }
	    	            
	    	        } else if (context.isProviderSide()) {//提供者
	    	            Long traceId, parentId, spanId;
	    	            traceId = TracerUtils.getAttachmentLong(invocation.getAttachment(TracerUtils.TID));
	    	            parentId = TracerUtils.getAttachmentLong(invocation.getAttachment(TracerUtils.PID));
	    	            spanId = TracerUtils.getAttachmentLong(invocation.getAttachment(TracerUtils.SID));
	    	            boolean isSample = (traceId != null);
	    	            span = tracer.genSpan(traceId, parentId, spanId, context.getMethodName(), isSample, serviceName,appName);
	    	            
	    	            if(span.isSample()){
	    	            	tracer.serverReceiveRecord(span, ip, startTime);
	    	            }
	    	            
	    	            tracer.setParentSpan(span);
	    	        }
	    	        
	    	        //设置需要向下游传递的参数
	    	        RpcInvocation invocationSink = (RpcInvocation) invocation;
	    	        if (span!=null) {
	    	        	invocationSink.setAttachment(TracerUtils.PID, (span.getParentId() != null ? String.valueOf(span.getParentId()) : "0"));
	    	        	invocationSink.setAttachment(TracerUtils.SID, (span.getId() != null ? String.valueOf(span.getId()) : null));
	    	        	invocationSink.setAttachment(TracerUtils.TID, (span.getTraceId() != null ? String.valueOf(span.getTraceId()) : null));
	    	        }
	        	} catch (Exception ex){
	        		logger.debug("eagleye dubbo client invoker before error [" + ex.getMessage() + "]");
	        	}
	        	//*************************************具体业务调用之前的处理 end *******************************
        	}
        	
//        	logger.debug("***********************spend invoke before : " + (System.currentTimeMillis() - st1) + "ms*****************************");
        	
            //************************************具体的业务调用 start*************************
//        	long st2 = System.currentTimeMillis();
            Result result = invoker.invoke(invocation);
//            logger.debug("***********************spend invoke : " + (System.currentTimeMillis() - st2) + "ms*****************************");
            //************************************具体的业务调用 end*************************
            
            if (result.getException() != null && configuration.isOpen()){
                catchException(result.getException(), ip, "invokeError");
            }
            return result;
        }catch (RpcException e) {
        	if(configuration.isOpen()){
	            if (e.getCause() != null && e.getCause() instanceof TimeoutException){
	            	catchException(e, ip, "invokeTimeout");
	            }else {
	            	catchException(e, ip, "invokeError");
	            }
        	}
            throw e;
        }finally {
//        	long st3 = System.currentTimeMillis();
            if (span != null && span.isSample()) {
                //************************************调用后记录annotation start ******************************
                long endTime = System.currentTimeMillis();
                if (isConsumerSide) {
                    tracer.clientReceiveRecord(span, ip, endTime);
                } else {
                    tracer.serverSendRecord(span, ip, endTime);
                    tracer.removeParentSpan();
                }
//                tracer.removeParentSpan();
                //************************************调用后记录annotation end ******************************
            }
//            logger.debug("***********************spend invoke after: " + (System.currentTimeMillis() - st3) + "ms*****************************");
        }
    }

    
    

    /**
     * 记录业务异常
     * @param e
     * @param endpoint
     * @param exType
     */
    private void catchException(Throwable e, String ip, String exType) {
        BinaryAnnotation exAnnotation = new BinaryAnnotation();
        exAnnotation.setKey(TracerUtils.EXCEPTION);
        exAnnotation.setValue(e.getMessage());
        exAnnotation.setType(exType);
        exAnnotation.setIp(ip);
        tracer.addBinaryAnntation(exAnnotation);
    }

    
    //加载filter时, 创建发送redis的守护线程
    static{
    	Thread sendThread = new Thread(new AsyncTransfer());
    	sendThread.setDaemon(true);
    	sendThread.setName("DubboTraceAsyncSendLogThread");
    	sendThread.start();
    	logger.info("=========Daemon thread: DubboTraceAsyncSendLogThread init successfully!============");
    }

	
}
