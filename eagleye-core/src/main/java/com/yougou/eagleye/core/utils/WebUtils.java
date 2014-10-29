/** 
 * jar名 :  eagleye-core.jar
 * 文件名 ：  WebUtils.java
 *       (C) Copyright eagleye Corporation 2011
 *           All Rights Reserved.
 * *****************************************************************************
 *    注意： 本内容仅限于优购公司内部使用，禁止转发
 ******************************************************************************/
package com.yougou.eagleye.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.yougou.eagleye.core.reflection.ReflectionUtils;
import com.yougou.eagleye.core.system.SystemExecute;




public class WebUtils {
	
	private static final long serialVersionUID = -7886659017820810560L;

	public WebUtils() {
	}

	/**
	 * 重载Spring WebUtils中的函数,作用如函数名所示 加入泛型转换,改变输入参数为request 而不是session
	 * @param name  session中变量名称
	 * @param clazz session中变量的类型
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getOrCreateSessionAttribute(HttpServletRequest request, String name, Class<T> clazz) {
		return (T) org.springframework.web.util.WebUtils.getOrCreateSessionAttribute(request.getSession(), name, clazz);
	}
	
	
	/**
	 * 获取真实客户端ip地址,
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		//可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串Ｉｐ值
		//是取X-Forwarded-For中第一个非unknown的有效IP字符串
		String[] str = ip.split(",");
		if(str!=null && str.length>1){
			ip = str[0];
		}
		return ip;
	}
	
	

	/**
	 * 获得项目跟目录
	 * @param request
	 * @return
	 */
	public static String getBasePath(HttpServletRequest request){
    	String path = request.getContextPath();
    	String basePath = "";
		if (!"80".equals(request.getServerPort()+"")) {
			basePath = request.getScheme() + "://" + request.getServerName()
			+ ":" + request.getServerPort() + path;
		}else{
			basePath = request.getScheme() + "://" + request.getServerName() + path ;
		}
		return basePath;
    }
	
	/**
	 * 根据cookie的名称获得该cookie对象
	 * @param request
	 * @return
	 */
	public static Cookie getCookieByName(HttpServletRequest request,String cookieName){
		Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(int i = 0; i < cookies.length; i++) {
                if(cookieName.equals(cookies[i].getName()) && !"".equals(cookies[i].getValue())) {
                    return cookies[i];
                }
            }
        }
        return null;
	}
	
	
	/**
	 * 读取url流转换为字符串,可以用来生成静态网页
	 * @param url
	 * @param characterSet
	 * @return
	 */
	public static final String readHtml(final String url,String characterSet) {
		if (StringUtils.isBlank(url)) {
			return null;
		}
		Pattern pattern = Pattern
				.compile("(http://|https://){1}[\\w\\.\\-/:]+");
		Matcher matcher = pattern.matcher(url);
		if (!matcher.find()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		try {
			URL _url = new URL(url);
			URLConnection urlConnection = _url.openConnection();
			InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream(), characterSet);
			BufferedReader in = new BufferedReader(isr);

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	
	/**
	 * 获得printwriter对象,用来进行out输出,和ajax配合使用
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public static PrintWriter getPrintWriter(HttpServletResponse response,String characterSet)throws Exception{
		PrintWriter out = null;
		if(response!=null){
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.setCharacterEncoding(characterSet);
			out = response.getWriter();
		}
		return out;
	}
	
	
	
	/**
	 * 获取form表单中的对象,如果是一个数组想转换为 sdfsd,sdfd,dfsdf,dfdf
	 * @param request
	 * @param clazz
	 * @return
	 */
	public static Object getFormObj(HttpServletRequest request,Object obj){

		return obj;
	}
	
	
	/**
	 * 获取应该创建的form对象的数量
	 * @param request
	 * @param clazz
	 * @return
	 */
	public static int getFormObjNum(HttpServletRequest request,Class<?> clazz){
		Object obj = WebUtils.getFormObj(request, clazz);
		int maxObjNum = 0;
		for(Field field : clazz.getDeclaredFields()){
			String fieldValue = ReflectionUtils.getFieldValue(obj, field.getName()).toString();
			if(fieldValue.indexOf(",")>-1){
				if(fieldValue.split(",").length>maxObjNum){
					maxObjNum = fieldValue.split(",").length;
				}
			}
		}
		return maxObjNum;
	}
	
	
	/**
	 * 获取系统的mac地址
	 * 目前只支持linux,windows,uinux
	 * @return
	 */
	public static String getSystemMacAddress(){
		//根据系统分隔符判断系统类型 ,如果是 : 则为linux  如果是 ; 则是windows
		String osFlag = System.getProperty("path.separator");
		
		//windows获取mac地址
		String macStr = "";
		try {
			if (osFlag != null && ";".equals(osFlag)) {//windows系统
				List<String> strList = SystemExecute
						.runCmd("cmd /c ipconfig /all");
				for (String str : strList) {
					if (str.indexOf("Physical Address") > -1
							&& str.indexOf(":") > -1) {
						macStr = str.split(":")[1];
						break;
					}
				}
			}
			//linux获取mac地址
			if (osFlag != null && ":".equals(osFlag)) {//linux系统
				List<String> strList = SystemExecute.runShell("ifconfig");
				if (strList != null) {
					macStr = strList.get(0);
					if (macStr != null && macStr.length() >= 17) {//mac地址长度为17,固定不变
						macStr = macStr.trim();
						macStr = macStr.substring(macStr.length() - 17, macStr
								.length());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return macStr.trim();
	}
	
	
	public static void main(String[] args)throws Exception{
	}
	
}
