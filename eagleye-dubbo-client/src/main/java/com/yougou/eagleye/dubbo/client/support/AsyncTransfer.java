package com.yougou.eagleye.dubbo.client.support;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.yougou.eagleye.trace.domain.Span;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;


/**
 * 异步发送实现类
 */
public class AsyncTransfer implements Runnable{

    private static Logger logger = LoggerFactory.getLogger(AsyncTransfer.class);

	private static ObjectMapper jsonMapper = new ObjectMapper();
	
 
    public void run() {
        while (true) {
        	Span span = null;
            try {
				span = Tracer.spanQueue.take();
				if (null != span) {
					String resultLog = jsonMapper.writeValueAsString(span);
					this.sendToRedis(resultLog);
				} 
			} catch (Exception e) {
				//生产上默认是不打印该日志的.
	            logger.debug(" send span to redis error :[" + e.getMessage() +"]");
			}finally{
				span = null;
			}
        }
    }
 
    
    /**
	 * 发送日志到redis
	 * @param msg
	 * @throws Exception
	 */
	private void sendToRedis(String msg) throws Exception{
		JedisUtil jedisUtil = JedisUtil.getInstance();
		Jedis jedis = jedisUtil.getJedis();
		logger.debug("=======trace log===publised "+ msg + "===========");
		jedis.publish("eagleyetracechannel", msg);
		jedisUtil.closeJedis(jedis);
	}

}
