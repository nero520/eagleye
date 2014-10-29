package com.yougou.eagleye.admin.util;

import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;


public class HttpClientUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	
	private static PoolingHttpClientConnectionManager connManager = null;
	private static CloseableHttpClient httpclient = null;
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	private final static int connectTimeout = 5000;
	
	static {
		try {
			
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
		            .register("http", PlainConnectionSocketFactory.INSTANCE)
		            .build();
			
			connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			httpclient = HttpClients.custom().setConnectionManager(connManager).build();
			// Create socket configuration
			SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
			connManager.setDefaultSocketConfig(socketConfig);
			// Create message constraints
	        MessageConstraints messageConstraints = MessageConstraints.custom()
	            .setMaxHeaderCount(200)
	            .setMaxLineLength(2000)
	            .build();
	        // Create connection configuration
	        ConnectionConfig connectionConfig = ConnectionConfig.custom()
	            .setMalformedInputAction(CodingErrorAction.IGNORE)
	            .setUnmappableInputAction(CodingErrorAction.IGNORE)
	            .setCharset(Consts.UTF_8)
	            .setMessageConstraints(messageConstraints)
	            .build();
	        connManager.setDefaultConnectionConfig(connectionConfig);
			connManager.setMaxTotal(200);
			connManager.setDefaultMaxPerRoute(20);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public static String postJsonBody(String url, String jsonStr, String encoding){
		HttpPost post = new HttpPost(url);
		try {
		    post.setHeader("Content-type", "application/json");
		    RequestConfig requestConfig = RequestConfig.custom()
		    		.setSocketTimeout(connectTimeout)
		    		.setConnectTimeout(connectTimeout)
		    		.setConnectionRequestTimeout(connectTimeout)
		    		.setExpectContinueEnabled(false).build();
		    post.setConfig(requestConfig);
		    
		    
		    post.setEntity(new StringEntity(jsonStr, encoding));
		    logger.debug("[HttpUtils Post] begin invoke url:" + url + " , params:"+jsonStr);
		    CloseableHttpResponse response = httpclient.execute(post);
		    try {
			    HttpEntity entity = response.getEntity();
			    try {
					if(entity != null){
						String str = EntityUtils.toString(entity, encoding);
						logger.debug("[HttpUtils Post]Debug response, url :" + url + " , response string :"+str);
						return str;
					}
				} finally {
					if(entity != null){
						entity.getContent().close();
					}
				}
		    } finally {
				if(response != null){
					response.close();
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			post.releaseConnection();
		}
		return "";
	}
	
	
	
	
	public static String post(String url, Map<String , Object> map , String encoding){
		HttpPost post = new HttpPost(url);
		try {
//		    post.setHeader("Content-type", "application/json");
		    RequestConfig requestConfig = RequestConfig.custom()
		    		.setSocketTimeout(connectTimeout)
		    		.setConnectTimeout(connectTimeout)
		    		.setConnectionRequestTimeout(connectTimeout)
		    		.setExpectContinueEnabled(false).build();
		    post.setConfig(requestConfig);
		    
		    if(map!=null){
		    	List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		    	for(String key : map.keySet()){
		    		nvps.add(new BasicNameValuePair(key, map.get(key).toString()) );
		    	}
		    	post.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
		    }
		    
		    
		    CloseableHttpResponse response = httpclient.execute(post);
		    try {
			    HttpEntity entity = response.getEntity();
			    try {
					if(entity != null){
						String str = EntityUtils.toString(entity, encoding);
						logger.debug("[HttpUtils Post]Debug response, url :" + url + " , response string :"+str);
						return str;
					}
				} finally {
					if(entity != null){
						entity.getContent().close();
					}
				}
		    } finally {
				if(response != null){
					response.close();
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			post.releaseConnection();
		}
		return "";
	}
	
	
	
	public static void main(String[] args){
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("email", "liu.hw@yougou.com");
			map.put("title", "test");
			map.put("content", "testtest");
			map.put("type", "93");
			String paramStr =  objectMapper.writeValueAsString(map);
			
			
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("param", paramStr);
			System.out.println(paramStr);
			String str = HttpClientUtil.post(
					"http://10.10.10.30:8849/messenger/sendEmailForSystem.do",
					paramMap, "UTF-8");
			System.out.println(str);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	

}