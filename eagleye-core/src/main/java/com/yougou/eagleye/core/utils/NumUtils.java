/** 
 * jar名 :  eagleye-core.jar
 * 文件名 ：  NumUtils.java
 *       (C) Copyright eagleye Corporation 2011
 *           All Rights Reserved.
 * *****************************************************************************
 *    注意： 本内容仅限于优购公司内部使用，禁止转发
 ******************************************************************************/
package com.yougou.eagleye.core.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;


/**
 * <PRE>
 * 作用:
 *       数字操作工具类
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
public class NumUtils {

	/**
	 * 保留小数后面的位数*
	 * @param rawValue 初始值，
	 * @param digitCount 要保留的小数点后的位数
	 * @return String 处理后的数字
	 */
	public static String getDigits(double rawValue,int digitCount){
		String baseStr = "#." ;
        if(digitCount>0){
        	for(int i=0; i<digitCount; i++){
        		baseStr+="#" ;
        	}
        	DecimalFormat df = new DecimalFormat(baseStr);   
            return df.format(rawValue); 
        }else{
        	DecimalFormat df = new DecimalFormat(baseStr);   
            return df.format(rawValue).substring(0,df.format(rawValue).lastIndexOf(".")); 
        }
	}
	
	/**
	 * 精确的加法运算.
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.add(b2).doubleValue();
	}
	
	/**
	 * 
	 * 精确的减法运算.
	 * 
	 * @param v1 被减数
	 * @param v2 减数
	 */
	public static double subtract(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}
	
	/**
	 * 提供精确的乘法运算.
	 */
	public static double multiply(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.multiply(b2).doubleValue();
	}
	
	/**
	 * 提供精确的乘法运算，并对运算结果截位.
	 * 
	 * @param scale 运算结果小数后精确的位数
	 */
	public static double multiply(double v1, double v2,int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.multiply(b2).setScale(scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	}


	/**
	 * 提供（相对）精确的除法运算.
	 * 
	 * @see #divide(double, double, int)
	 */
	public static double divide(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.divide(b2).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算.
	 * 由scale参数指定精度，以后的数字四舍五入.
	 * 
	 * @param v1 被除数
	 * @param v2 除数
	 * @param scale 表示表示需要精确到小数点以后几位
	 */
	public static double divide(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}

		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 提供精确的小数位四舍五入处理.
	 * 
	 * @param v 需要四舍五入的数字
	 * @param scale 小数点后保留几位
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(v);
		return b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 验证是否只是数字
	 * @param str
	 * @return
	 */
	public static Boolean isNumber(String str){
		try {
			Double.parseDouble(str);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	
	
	/**
	 * 将字符串数组转换为integer数组
	 * @param strs
	 * @return
	 */
	public static Integer[] strsToInts(Object[] strs) {
		Integer[] ints = null;
		if(strs!=null){
			ints = new Integer[strs.length];
			for(int i=0;i<strs.length;i++){
				ints [i]=Integer.parseInt(strs[i].toString());
			}  
		}
		return ints;
    }
	
	public static void main(String[] args){
		String[] strs = {"12","23","45"};
		Integer[] ints = NumUtils.strsToInts(strs);
		for(int i : ints){
			System.out.println(i);
		}
	}
	
}
