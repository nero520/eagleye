package com.yougou.eagleye.dubbo.client.support;


import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.yougou.eagleye.trace.domain.Annotation;
import com.yougou.eagleye.trace.domain.BinaryAnnotation;
import com.yougou.eagleye.trace.domain.Span;

/**
 * 系统跟踪类(单例)
 */
public class Tracer {

    private static final Logger logger = LoggerFactory.getLogger(Tracer.class);

    private static final Tracer tracer = new Tracer();
    
    public static BlockingQueue<Span> spanQueue = new LinkedBlockingQueue<Span>(2000);

    private Sampler sampler = new Sampler();

//    private AsyncTransfer transfer = new AsyncTransfer();

    //传递parentSpan
    private ThreadLocal<Span> spanThreadLocal = new ThreadLocal<Span>();


    private Tracer() {
    }
    
    public static Tracer getInstance(){
        return tracer;
    }

    public void removeParentSpan() {
        spanThreadLocal.remove();
    }

    public Span getParentSpan() {
        return spanThreadLocal.get();
    }

    public void setParentSpan(Span span) {
        spanThreadLocal.set(span);
    }

    //构件Span，参数通过上游接口传递过来
    public Span genSpan(Long traceId, Long pid, Long id, String spanName, boolean isSample, String serviceName,String appName) {
        Span span = new Span();
        span.setId(id);
        span.setParentId(pid);
        span.setSpanName(spanName);
        span.setSample(isSample);
        span.setTraceId(traceId);
        span.setServiceName(serviceName);
        span.setAppName(appName);
        return span;
    }

    //构件rootSpan,是否采样
    public Span newSpan(String spanName, String ip, String serviceName,String appName) {
        boolean s = isSample();
        Span span = new Span();
        span.setTraceId(s ? genLongId() : null);
        span.setId(s ? genLongId() : null);
        span.setSpanName(spanName);
        span.setServiceName(serviceName);
        span.setAppName(appName);
        span.setSample(s);
        
        return span;
    }


    public boolean isSample() {
        return sampler.isSample();
    }

    public void addBinaryAnntation(BinaryAnnotation b) {
        Span span = spanThreadLocal.get();
        if (span != null) {
            span.addBinaryAnnotation(b);
        }
    }

    //构件cs annotation
    public void clientSendRecord(Span span, String ip, long start) {
        Annotation annotation = new Annotation();
        annotation.setValue(Annotation.CLIENT_SEND);
        annotation.setTimestamp(start);
        annotation.setIp(ip);
        span.addAnnotation(annotation);
    }


    //构件cr annotation
    public void clientReceiveRecord(Span span, String ip, long end) {
        Annotation annotation = new Annotation();
        annotation.setValue(Annotation.CLIENT_RECEIVE);
        annotation.setIp(ip);
        annotation.setTimestamp(end);
        span.addAnnotation(annotation);
        if(!Tracer.spanQueue.offer(span)){
        	logger.debug("================ spanQueue overflow =================");
        }
    }

    //构件sr annotation
    public void serverReceiveRecord(Span span, String ip, long start) {
        Annotation annotation = new Annotation();
        annotation.setValue(Annotation.SERVER_RECEIVE);
        annotation.setIp(ip);
        annotation.setTimestamp(start);
        span.addAnnotation(annotation);
        spanThreadLocal.set(span);
    }

    //构件 ss annotation
    public void serverSendRecord(Span span, String ip, long end) {
        Annotation annotation = new Annotation();
        annotation.setTimestamp(end);
        annotation.setIp(ip);
        annotation.setValue(Annotation.SERVER_SEND);
        span.addAnnotation(annotation);
        if(!Tracer.spanQueue.offer(span)){
        	logger.debug("================ spanQueue overflow =================");
        }
    }


    public Long genLongId() {
    	long id = UUID.randomUUID().getMostSignificantBits();
        return Math.abs(id);
    }
    
    

}

