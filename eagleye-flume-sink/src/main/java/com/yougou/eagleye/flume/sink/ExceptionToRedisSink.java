package com.yougou.eagleye.flume.sink;

import org.apache.flume.Channel;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Sink;
import org.apache.flume.Transaction;
import org.apache.flume.event.EventHelper;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

/**
 * <p>
 * A {@link Sink} implementation that logs all events received at the INFO level
 * to the <tt>org.apache.flume.sink.LoggerSink</tt> logger.
 * </p>
 * <p>
 * <b>WARNING:</b> Logging events can potentially introduce performance
 * degradation.
 * </p>
 * <p>
 * <b>Configuration options</b>
 * </p>
 * <p>
 * <i>This sink has no configuration parameters.</i>
 * </p>
 * <p>
 * <b>Metrics</b>
 * </p>
 * <p>
 * TODO
 * </p>
 */
public class ExceptionToRedisSink extends AbstractSink {

	private static final Logger logger = LoggerFactory
			.getLogger(ExceptionToRedisSink.class);

	public Status process() throws EventDeliveryException {
		Status status = null;

		// Start transaction
		Channel ch = getChannel();
		Transaction txn = ch.getTransaction();
		txn.begin();
		try {
			// This try clause includes whatever Channel operations you want to
			// do

			Event event = ch.take();

			// Send the Event to the external repository.
			// storeSomeData(e);
			if (event != null) {
				//this.sendToRedis(EventHelper.dumpEvent(event));
				//'{"app_name":"cms","ip":"192.168.211.4","body":"cms_mysql connection error"}'
				String bodyStr = new String(event.getBody());
				bodyStr = bodyStr.replaceAll("\"", "'");
				String jsonStr = "{"
							   + "\"appGroup\":\"" + event.getHeaders().get("appGroup") + "\","
							   + "\"hostname\":\"" + event.getHeaders().get("hostname") + "\","
							   + "\"date\":\"" + event.getHeaders().get("date") + "\","
							   + "\"body\":\"" + bodyStr + "\""
							   + "}";
				this.sendToRedis(jsonStr);
			}

			txn.commit();
			status = Status.READY;
		} catch (Throwable t) {
			txn.rollback();

			// Log exception, handle individual exceptions as needed

			status = Status.BACKOFF;

			// re-throw all Errors
			if (t instanceof Error) {
				throw (Error) t;
			}
		} finally {
			txn.close();
		}
		return status;
	}
	
	
	
	/**
	 * 发送日志到redis
	 * @param msg
	 * @throws Exception
	 */
	private void sendToRedis(String msg) throws Exception{
		JedisUtil jedisUtil = JedisUtil.getInstance();
		Jedis jedis = jedisUtil.getJedis();
		logger.info("==========publised "+ msg + "===========");
		jedis.publish("eagleyechannel", msg);
		jedisUtil.closeJedis(jedis);
	}
	
	public static void main(String[] args){
		String str = "title:\"cms\"";
		str = str.replaceAll("\\\"", "'");
		System.out.println(str);
	}

}
