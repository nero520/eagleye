/** 
 * jar名 :  eagleye-core.jar
 * 文件名 ：  StrUtils.java
 *       (C) Copyright eagleye Corporation 2011
 *           All Rights Reserved.
 * *****************************************************************************
 *    注意： 本内容仅限于优购公司内部使用，禁止转发
 ******************************************************************************/
package com.yougou.eagleye.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.JavaScriptUtils;

/**
 * <PRE>
 * 作用:
 *       字符串操作工具类
 * 限制:
 *       无.
 * 注意事项:
 *       无.
 * 修改历史:
 * -----------------------------------------------------------------------------
 *         VERSION       DATE                BY              CHANGE/COMMENT
 * -----------------------------------------------------------------------------
 *          1.0        2011-07-14           null              create
 * -----------------------------------------------------------------------------
 * </PRE>
 */
public class StrUtils {

	/**
	 * 验证url,如果url前没有http:// 则添加http:// eg: www.eagleye.cn
	 * 将转化为http://www.eagleye.com
	 * 
	 * @param url
	 * @return
	 */
	public static String handelUrl(String url) {
		if (url == null) {
			return null;
		}
		url = url.trim();
		if ((url.equals("")) || (url.startsWith("http://"))
				|| (url.startsWith("https://"))) {
			return url;
		}
		return "http://" + url.trim();
	}

	/**
	 * 将html代码进行过滤转换,避免html注入
	 * 
	 * @param htmlContent
	 *            html代码
	 * @return
	 */
	public static String html2Text(String htmlContent) {
		String txtContent = HtmlUtils.htmlEscape(htmlContent);
		return txtContent;
	}

	/**
	 * 将txt转换为html代码
	 * 
	 * @param textContent
	 *            文本格式代码
	 * @return
	 */
	public static String text2Html(String textContent) {
		String htmlContent = HtmlUtils.htmlUnescape(textContent);
		return htmlContent;
	}
	

	/**
	 * 对sql参数进行过滤,避免sql注入 注意: 只对用户输入的注入脚本进行过滤,而不是整个后台拼接以后的sql语句
	 * 
	 * @param sqlParam
	 * @return
	 */
	public static String escapeSql(String sqlParam) {
		sqlParam = StringEscapeUtils.escapeSql(sqlParam);
		return sqlParam;
	}

	/**
	 * 高效替换字符串的方法 可以替换特殊字符的替换方法,replaceAll只能替换普通字符串,含有特殊字符的不能替换
	 * 
	 * @param strSource
	 *            用户输入的字符串
	 * @param strFrom
	 *            数据库用需要替换的字符
	 * @param strTo
	 *            需要替换的字符替换为该字符串
	 * @return
	 */
	public static String replaceStr(String strSource, String strFrom,
			String strTo) {
		if (strSource == null) {
			return null;
		}
		try {
			int i = 0;
			if ((i = strSource.indexOf(strFrom, i)) >= 0) {
				char[] cSrc = strSource.toCharArray();
				char[] cTo = strTo.toCharArray();
				int len = strFrom.length();
				StringBuilder sb = new StringBuilder();
				sb.append(cSrc, 0, i).append(cTo);
				i += len;
				int j = i;
				while ((i = strSource.indexOf(strFrom, i)) > 0) {
					sb.append(cSrc, j, i - j).append(cTo);
					i += len;
					j = i;
				}
				sb.append(cSrc, j, cSrc.length - j);
				strSource = sb.toString();
				sb = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			strSource = "";
		}
		return strSource;
	}

	/**
	 * 字符串编码转换
	 * 
	 * @param rawStr
	 *            原始字符串
	 * @param characterSet
	 *            需要转换的字符集,例如:UTF-8,GB2312,GBK
	 * @return 转换以后的字符串
	 */
	public static String convertEncode(String rawStr, String characterSet) {
		String str = null;
		try {
			str = new String(rawStr.getBytes(), characterSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 判断字符是否是中文编码
	 * @param c
	 * @return
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否乱码
	 * @param strName
	 * @return
	 */
	public static boolean isMessyCode(String strName) {
//		strName = StrUtils.html2Text(strName);
//		Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
//		Matcher m = p.matcher(strName);
//		String after = m.replaceAll("");
//		String temp = after.replaceAll("\\p{P}", "");
//		char[] ch = temp.trim().toCharArray();
//		float chLength = ch.length;
//		float count = 0;
//		for (int i = 0; i < ch.length; i++) {
//			char c = ch[i];
//			if (!Character.isLetterOrDigit(c)) {
//				if (!isChinese(c)) {
////					count = count + 1;
////					System.out.print(c);
//					return true;
//				}
//			}
//		}
//		float result = count / chLength;
//		if (result > 0.4) {
//			return true;
//		} else {
//			return false;
//		}
		
		String tempStr = " 1234567890-=qwertyuiop[]\\asdfghjkl;'zxcvbnm,./`~!@#$%^&*()_+QWERTYUIOP{}|ASDFGHJKL:\"ZXCVBNM<>?"
            +"！@#￥%……&×（）——+～【】、『』|；‘：“”,./<>? ";
		if(strName!=null){
			for(char c : strName.toCharArray()){
				if(tempStr.indexOf(c)<0){//如果不是字符则将判断是否是中文字符
					if (!isChinese(c)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 检查不区分大小写的扩展名，是否符合允许的扩展名数组中的一个,符合，返回true,否则返回false
	 * 
	 * @param suffix
	 *            要检查的扩展名
	 * @param canUploadSuffix
	 *            允许的扩展名数组,例如:{jpg,img,bmp}
	 * @return
	 */
	public static boolean checkSuffix(String suffix, String[] canUploadSuffix) {
		if (suffix == null || canUploadSuffix == null) {
			return false;
		}
		boolean flag = false;
		for (int i = 0; i < canUploadSuffix.length; i++) {
			if (suffix.equalsIgnoreCase(canUploadSuffix[i])) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * 把字符串中的大写字母全部转换为小写字母
	 * 
	 * @param rawStr
	 *            原始字符串
	 * @return 过滤转换后的字符串
	 */
	public static String convertUpper2Low(String rawStr) {
		StringBuilder lastStr = new StringBuilder();
		if (rawStr != null) {
			for (char ch : rawStr.toCharArray()) {
				if (ch >= 65 && ch <= 90) { // 如果是大写字母就转化成小写字母
					ch = (char) (ch + 32);
				}
				lastStr.append(ch);
			}
		} else {
			return "";
		}
		return lastStr.toString();
	}

	/**
	 * 过滤javascript字符,避免脚本注入
	 * 
	 * @param input
	 * @return
	 */
	public static String javaScriptEscape(String input) {
		return JavaScriptUtils.javaScriptEscape(input);
	}

	/**
	 * 将map转换为url问号后的参数形式
	 * 
	 * @param paramMap
	 * @return
	 */
	public static String prepareParam(Map<String, Object> paramMap) {
		StringBuffer sb = new StringBuffer();
		if (paramMap.isEmpty()) {
			return "";
		} else {
			for (String key : paramMap.keySet()) {
				String value = (String) paramMap.get(key);
				if (sb.length() < 1) {
					sb.append(key).append("=").append(value);
				} else {
					sb.append("&").append(key).append("=").append(value);
				}
			}
			return sb.toString();
		}
	}

	/**
	 * 验证是否只是数字和字母,用来排除非法字符,如果是验证数字则正则为: "[0-9]*"
	 * 
	 * @param str
	 * @return
	 */
	public static Boolean isLetterAndNumber(String str) {
		String reg = "[0-9a-zA-Z]+";
		Pattern p = Pattern.compile(reg);
		return p.matcher(str).matches();
	}

	/**
	 * 验证是否只是数字
	 * 
	 * @param str
	 * @return
	 */
	public static Boolean isNumber(String str) {
		String reg = "[0-9]*";
		Pattern p = Pattern.compile(reg);
		return p.matcher(str).matches();
	}
	
	/**
	 * 验证是否包含数字
	 * @param str
	 * @return
	 */
	public static Boolean isContainNumber(String str){
		String reg = ".*\\d+.*";
		Pattern p = Pattern.compile(reg);
		return p.matcher(str).matches();
	}
	
	public static void main(String[] args) throws Exception{
		String str = "/instrument/instrumentManage/querySimpleInstrumentList/1,2,3,4.html";
		System.out.println(StrUtils.isContainNumber(str));
	}

	/**
	 * 验证日期格式是否正确
	 * 
	 * @param str
	 * @return
	 */
	public static Boolean isDateFormat(String str) {
		String eL = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
		Pattern p = Pattern.compile(eL);
		Matcher m = p.matcher(str);
		boolean b = m.matches();
		return b;
	}

	/**
	 * 方法说明：验证手机号码是否正确
	 * 
	 * @param phoneNum
	 * @return true: 手机号码正确；false：手机号码不正确
	 */
	public static boolean checkPhoneNum(String phoneNum) {

		Pattern p1 = Pattern.compile("^((\\+{0,1}86){0,1})1[0-9]{10}");
		Matcher m1 = p1.matcher(phoneNum);
		if (m1.matches()) {
			Pattern p2 = Pattern.compile("^((\\+{0,1}86){0,1})");
			Matcher m2 = p2.matcher(phoneNum);
			StringBuffer sb = new StringBuffer();
			while (m2.find()) {
				m2.appendReplacement(sb, "");
			}
			m2.appendTail(sb);
			String str = phoneNum.substring(0, 2);
			if ("10".equals(str) || "11".equals(str) || "12".equals(str)
					|| "19".equals(str)) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 验证邮箱地址
	 * 
	 * @param email
	 * @return
	 */
	public static boolean verifyEmail(String email) {
		boolean flag = true;
		// final String pattern1 =
		// "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		final String pattern1 = "^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$";
		final Pattern pattern = Pattern.compile(pattern1);
		final Matcher mat = pattern.matcher(email);
		if (!mat.find()) {
			flag = false;
		}
		return flag;
	}

	/**
	 * Java去除字符串中的空格,回车,换行符,制表符
	 * 
	 * @param strSource
	 * @return
	 */
	public static String replaceBlank(String strSource) {
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		Matcher m = p.matcher(strSource);
		String afterStr = m.replaceAll("");
		return afterStr;
	}

	/**
	 * 替换字符串函数,该方法速度慢,但是不会内存溢出
	 */
	public static String replace(String strSource, String strFrom, String strTo) {
		// 如果要替换的子串为空，则直接返回源串
		if (strFrom == null || strFrom.equals(""))
			return strSource;
		String strDest = "";
		// 要替换的子串长度
		int intFromLen = strFrom.length();
		int intPos;
		// 循环替换字符串
		while ((intPos = strSource.indexOf(strFrom)) != -1) {
			// 获取匹配字符串的左边子串
			strDest = strDest + strSource.substring(0, intPos);
			// 加上替换后的子串
			strDest = strDest + strTo;
			// 修改源串为匹配子串后的子串
			strSource = strSource.substring(intPos + intFromLen);
		}
		// 加上没有匹配的子串
		strDest = strDest + strSource;
		// 返回
		return strDest;
	}

	/**
	 * 获取中间字符的list集合 如: [1,2][3,4][5,6]字符,如果要找[]中间的字符集合则
	 * getBetweenArray("\\[","\\]","[1,2][3,4][5,6]") 则生成一个 1,2 3,4 5,6 的字符串集合
	 * 
	 * @param startStr
	 *            开始字符
	 * @param endStr
	 *            结束字符
	 * @param rawStr
	 *            原生字符
	 * @return
	 */
	public static List<String> getBetweenArray(String startStr, String endStr,
			String rawStr) {
		String patternStr = startStr + "(.+?)" + endStr;
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(rawStr);
		List<String> strList = new ArrayList<String>();
		while (matcher.find()) {
			// 股票代码
			String str = matcher.group(1);
			strList.add(str);
		}
		return strList;
	}

	/**
	 * 将字符串数组转换为字符串 格式为: sdf,123,fgf
	 * 
	 * @param strs
	 * @return
	 */
	public static String arrayToString(String[] strs) {
		String str = "";
		if (strs != null && strs.length != 0) {
			str = strs[0];
			for (int i = 1; i < strs.length; i++) {
				str += "," + strs[i];
			}
		}
		return str;
	}

	/**
	 * 重新封装json串
	 * 
	 * @param rawStr
	 *            通过json包生成的json串
	 * @return
	 */
	public static String convertToJson(String rawStr) {
		rawStr = "{\"results\":" + rawStr + "}";
		rawStr = StrUtils.replace(rawStr, "null", "\"\"");
		return rawStr;
	}
	
	/**
	 * 清楚html标签
	 * @param saltContent
	 * @return
	 */
	public static String clearHtmlTag(String saltContent){
		StringBuilder sb = new StringBuilder();
		saltContent = "<html>" + saltContent + "</html>";
		Parser parser = null;
		try {
			parser = new Parser(saltContent);
			parser.setEncoding("utf-8");
			NodeList nodeList = parser.parse(null);
			for (int i = 0; i <= nodeList.size(); i++) {
				if(nodeList.elementAt(i)!=null){
					sb.append(nodeList.elementAt(i).toPlainTextString());
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}finally{
			if(parser!=null){
				parser.reset();
			}
		}
		return sb.toString();
	}

	
	
	/**
	 * 消除html标签,如果是<p>或<br>则换行,如果是&nbsp;则替换为空格
	 * @param htmlContent 原始内容
	 * @return
	 */
	public static String convertHtmlToText(String htmlContent){
		StringBuilder sb = new StringBuilder();
		htmlContent = "<p>" + htmlContent + "</p>";
		Parser parser = null;
		try {
			parser = new Parser(htmlContent);
			parser.setEncoding("utf-8");
			NodeList nodeList = parser.parse(null);
			for (int i = 0; i <= nodeList.size(); i++) {
				if(nodeList.elementAt(i)!=null){
					System.out.println("+++++++"+nodeList.elementAt(i).toHtml());
					sb.append(StrUtils.replaceStr(nodeList.elementAt(i).toPlainTextString(), "&nbsp;", " "));
					String tempHtmlStr = nodeList.elementAt(i).toHtml();
					if(tempHtmlStr.indexOf("<p>")>0 || tempHtmlStr.indexOf("<P>")>0 ||
							tempHtmlStr.indexOf("</p>")>0 || tempHtmlStr.indexOf("</P>")>0 ||
							tempHtmlStr.indexOf("<br>")>0 || tempHtmlStr.indexOf("<BR>")>0 ||
							tempHtmlStr.indexOf("<br/>")>0 || tempHtmlStr.indexOf("<BR/>")>0){
						sb.append("\r\n");
					}
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}finally{
			if(parser!=null){
				parser.reset();
			}
		}
		return sb.toString();
	}
	
	/**
	 * 数值类型补零
	 * 如果为整数，并且长度小于length,则前面补零
	 * 如果为小数，则把整数部分补零
	 * @param value
	 * @return
	 */
	public static String padZero(Number value,int length) {
		String text = String.valueOf(value);
		
		int dotIndex = text.indexOf('.');
		if (dotIndex == -1)
			dotIndex = text.length();
		if (dotIndex > length)
			throw new IllegalArgumentException("Try to pad on a number too big");
		StringBuilder padded = new StringBuilder();
		for (int padIndex = dotIndex; padIndex < length; padIndex++) {
			padded.append('0');
		}
		return padded.append(text).toString();
	}
}
