package com.yougou.eagleye.admin.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yougou.eagleye.core.utils.DateUtils;


public class EagleyeAdminUtil {
	
	
	private final static Logger logger = LoggerFactory.getLogger(EagleyeAdminUtil.class);
	
	private static ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 将时间字符串转换为long型
	 * @param dateStr
	 * @param format yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String dateStr2Long(String dateStr, String format){
		Date date = DateUtils.getDateFromString(dateStr, format);
		String dateLong = date.getTime() + "";
		return dateLong;
	}
	
	
	/**
	 * 将long型时间转换为需要的日期格式
	 * @param timestamp
	 * @return
	 */
	public static String long2DateStr(String timestamp){
		Timestamp ts = new Timestamp(Long.parseLong(timestamp));
		String dateStr = null;
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			dateStr = sdf.format(ts);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateStr;
	}
	
	
	/**
	 * 动态加载spring类
	 * @param className 类全路径
	 * @param scope "prototype" 多例  "singleton" 单例
	 * @return
	 */
	public static Object getDynamicBean(String className,String scope) {  
	     ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext) SpringContextHolder.getApplicationContext();  
	     DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext  
	            .getBeanFactory();  
	     if (!beanFactory.containsBean(className)) {  
	         BeanDefinitionBuilder bdb = BeanDefinitionBuilder  
	                .rootBeanDefinition(className);  
	         bdb.setScope(scope);//"prototype" 多例
	         beanFactory.registerBeanDefinition(className,  
	                bdb.getBeanDefinition());  
	     }  
	     return beanFactory.getBean(className);  
	}
	
	
	
	/**
	 * 发送邮件
	 * @param title
	 * @param content
	 * @param emails
	 * @return
	 * @throws Exception
	 */
	public static String sendEmail(String title, String content, String emails, String sendUrl) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("email", emails);
		map.put("title", title);
		map.put("content", content);
		map.put("type", "93");
		String paramStr =  objectMapper.writeValueAsString(map);
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("param", paramStr);
		String returnStr = HttpClientUtil.post(sendUrl, paramMap, "UTF-8");
		logger.info("+++++++++++++++++sendEmail  " + returnStr + " " + paramStr);
		return returnStr;
	}

	/**
	 * 发送短信
	 * @param content
	 * @param mobiles
	 * @return
	 * @throws Exception
	 */
	public static String sendMobile(String content, String mobiles, String sendUrl)throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phone", mobiles);
		map.put("content", content);
		map.put("type", "93");
		String paramStr =  objectMapper.writeValueAsString(map);
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("param", paramStr);
		String returnStr = HttpClientUtil.post(sendUrl, paramMap, "UTF-8");
		logger.info("-----------------sendMobile  " + returnStr + " " + paramStr);
		return returnStr;
	}
	
	
	public static String getLocalIP() {
		String ipStr = "0.0.0.0";
        try {
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface
					.getNetworkInterfaces();
			InetAddress ip = null;
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces
						.nextElement();
				Enumeration<InetAddress> addresses = netInterface
						.getInetAddresses();
				while (addresses.hasMoreElements()) {
					ip = (InetAddress) addresses.nextElement();
					if (ip != null && ip instanceof Inet4Address) {
						String tempIp = ip.getHostAddress();
						if (!"127.0.0.1".equals(tempIp)
								&& !"0.0.0.0".equals(tempIp)) {
							ipStr = tempIp;
						}
					}
				}
				netInterface = null;
				addresses = null;
			}
			allNetInterfaces = null;
		} catch (Exception e) {
			logger.debug("eagleye log4j appender get local ip error " + e.getLocalizedMessage());
		}
		return ipStr;
    }
	
	
	public static void main(String[] args){
//		String content = "";
		System.out.println("111");
	}
	
}
