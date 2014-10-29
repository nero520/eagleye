package com.yougou.eagleye.admin.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yougou.eagleye.core.utils.StrUtils;

/**
 * 是否对js,css等不经常改变的文件进行缓存
 * 需要在web.xml里面进行如下配置
 *  <!-- 所有.htm结尾的请求全部不缓存 -->
 *	<filter>
 *       <filter-name>NoCache</filter-name>
 *       <filter-class>com.tty.ws.filter.ResponseHeaderFilter</filter-class>
 *       <init-param>
 *           <param-name>Cache-Control</param-name>
 *           <param-value>no-cache,must-revalidate</param-value>
 *       </init-param>
 *   </filter>
 *   <filter-mapping>
 *       <filter-name>NoCache</filter-name>
 *       <url-pattern>*.htm</url-pattern>
 *   </filter-mapping>
 *   <!-- 对图片,js,css等不经常变更的数据进行缓存 -->
 *   <filter>
 *       <filter-name>CacheForWeek</filter-name>
 *       <filter-class>com.tty.ws.filter.ResponseHeaderFilter</filter-class>
 *       <init-param>
 *           <param-name>Cache-Control</param-name>
 *           <!-- 单位是秒 -->
 *           <param-value>max-age=1, public</param-value>
 *       </init-param>
 *   </filter>
 *   <filter-mapping>
 *       <filter-name>CacheForWeek</filter-name>
 *       <url-pattern>/images/*</url-pattern>
 *   </filter-mapping>
 *   <filter-mapping>
 *       <filter-name>CacheForWeek</filter-name>
 *       <url-pattern>*.js</url-pattern>
 *   </filter-mapping>
 *   <filter-mapping>
 *       <filter-name>CacheForWeek</filter-name>
 *       <url-pattern>*.css</url-pattern>
 *   </filter-mapping> 
 * @author liuxiaobei
 *
 */
public class ResponseHeaderFilter implements Filter {
	
    FilterConfig fc; 

    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest)req;
        // set the provided HTTP response parameters
        for (Enumeration e = fc.getInitParameterNames(); e.hasMoreElements();) {
            String headerName = (String) e.nextElement();
            response.addHeader(headerName, fc.getInitParameter(headerName));
        }
        // pass the request/response on
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        
        
        String requestUrl = request.getRequestURL().toString()+"";
        String urlSuffix = StrUtils.convertUpper2Low(requestUrl.substring(requestUrl.lastIndexOf(".")+1,requestUrl.length()));
        //////////////////////////////////////正式环境下需要将下列代码注释打开/////////////////////////////////////////////////////
//        if(!"html".equals(urlSuffix)){
//        	//缓存失效
//        	if(this.checkHeaderCache(3600, request.getDateHeader("Last-Modified"), request, response)){
//        		this.setRespHeaderCache(3600, request, response);
//        	}
//        	System.out.println("*******************"+urlSuffix+"****************"+requestUrl);
//        }else{
//        	response.addHeader("Cache-Control", "no-cache,must-revalidate");
//        }
        ////////////////////////////////////////////////end/////////////////////////////////////////////////////////////////////
        chain.doFilter(request, response);
    } 

    
    /**
     * 判断是否需要重新请求服务器
     * @param adddays
     * @param modelLastModifiedDate
     * @param request
     * @param response
     * @return
     */
    public boolean checkHeaderCache(long adddays, long modelLastModifiedDate, HttpServletRequest request, HttpServletResponse response) {
		// com.jdon.jivejdon.presentation.filter.ExpiresFilter
		request.setAttribute("myExpire", adddays);

		// convert seconds to ms.
		long adddaysM = adddays * 1000;
		long header = request.getDateHeader("If-Modified-Since");
		long now = System.currentTimeMillis();
		if (header > 0 && adddaysM > 0) {
			if (modelLastModifiedDate > header) {
				// adddays = 0; // reset
				response.setStatus(HttpServletResponse.SC_OK);
				return true;
			}
			if (header + adddaysM > now) {
				// during the period happend modified
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				return false;
			}
		}

		// if over expire data, see the Etags;
		// ETags if ETags no any modified
		String previousToken = request.getHeader("If-None-Match");
		if (previousToken != null && previousToken.equals(Long.toString(modelLastModifiedDate))) {
			// not modified
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return false;
		}
		// if th model has modified , setup the new modified date
		response.setHeader("ETag", Long.toString(modelLastModifiedDate));
		setRespHeaderCache(adddays, request, response);
		return true;

	}

    /**
     * 重新请求服务器
     * @param adddays
     * @param request
     * @param response
     */
	public void setRespHeaderCache(long adddays, HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("myExpire", adddays);
		long adddaysM = adddays * 1000;
		String maxAgeDirective = "max-age=" + adddays;
		response.setHeader("Cache-Control", maxAgeDirective);
		response.setStatus(HttpServletResponse.SC_OK);
		response.addDateHeader("Last-Modified", System.currentTimeMillis());
		response.addDateHeader("Expires", System.currentTimeMillis() + adddaysM);
	}
    
    
    public void init(FilterConfig filterConfig) {
        this.fc = filterConfig;
    } 

    public void destroy() {
        this.fc = null;
    } 

    
    public static void main(String[] args){
    	String str = "sdfsdf.html";
    	System.out.println(str.substring(str.lastIndexOf(".")+1,str.length()));
    }
}

