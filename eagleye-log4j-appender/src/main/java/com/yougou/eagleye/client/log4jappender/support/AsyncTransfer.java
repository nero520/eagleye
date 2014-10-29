package com.yougou.eagleye.client.log4jappender.support;


import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;


/**
 * 异步发送实现类
 */
public class AsyncTransfer implements Runnable{

    private static Logger logger = Logger.getLogger(AsyncTransfer.class);

	
 
    public void run() {
        try {
            while (true) {
                String alertStr = Configuration.alertQueue.take();
                if (null != alertStr) {
            		this.sendToRedis(alertStr);
                } 
            }
        } catch (Exception e) {
        	//生产上默认是不打印该日志的.
            logger.debug(" send alertStr to redis error :[" + e.getMessage() +"]");
        }
    }
 
    
    /**
	 * 发送日志到redis
	 * @param msg
	 * @throws Exception
	 */
	private void sendToRedis(String msg){
		try {
			JedisUtil jedisUtil = JedisUtil.getInstance();
			Jedis jedis = jedisUtil.getJedis();
			logger.debug("========alert log==publised " + msg + "===========");
			jedis.publish("eagleyealertchannel", msg);
			jedisUtil.closeJedis(jedis);
		} catch (Exception e) {
			//生产上默认是不打印该日志的.
            logger.debug(" send alert log to redis error :[" + e.getMessage() +"]");
		}
	}

}
