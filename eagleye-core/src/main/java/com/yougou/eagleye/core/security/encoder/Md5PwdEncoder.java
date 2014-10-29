/** 
 * jar名 :  eagleye-core.jar
 * 文件名 ：  Md5PwdEncoder.java
 *       (C) Copyright eagleye Corporation 2011
 *           All Rights Reserved.
 * *****************************************************************************
 *    注意： 本内容仅限于优购公司内部使用，禁止转发
 ******************************************************************************/
package com.yougou.eagleye.core.security.encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;


/**
 * <PRE>
 * 作用:
 *       MD5算法加密,验证实现类
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
public class Md5PwdEncoder {
	
	private String defaultSalt;

	/**
	 * 对密码进行md5加密
	 * @param rawPass 原生密码
	 * @return md5值
	 * @data 2011-7-14
	 */
	public String encryptPassword(String rawPass) {
		return encryptPassword(rawPass, this.defaultSalt);
	}

	/**
	 * 对密码进行md5加密
	 * @param rawPass 原生密码
	 * @param salt 加密盐
	 * @return md5值
	 * @data 2011-7-14
	 */
	public String encryptPassword(String rawPass, String salt) {
		byte[] digest;
		String saltedPass = mergePasswordAndSalt(rawPass, salt, false);
		MessageDigest messageDigest = getMessageDigest();
		try {
			digest = messageDigest.digest(saltedPass.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("UTF-8 not supported!");
		}
		return new String(Hex.encodeHex(digest));
	}

	/**
	 * 验证md5密码与原生密码是否匹配
	 * @param encPass md5密码
	 * @param rawPass 原生密码
	 * @return
	 * @data 2011-7-14
	 */
	public boolean isPasswordValid(String encPass, String rawPass) {
		return isPasswordValid(encPass, rawPass, this.defaultSalt);
	}

	/**
	 * 验证md5密码与原生密码是否匹配
	 * @param encPass md5密码
	 * @param rawPass 原生密码
	 * @param salt 加密盐
	 * @return
	 * @data 2011-7-14
	 */
	public boolean isPasswordValid(String encPass, String rawPass, String salt) {
		if (encPass == null) {
			return false;
		}
		String pass2 = encryptPassword(rawPass, salt);
		return encPass.equals(pass2);
	}

	/**
	 * 获取MD5算法出处
	 * @return
	 * @data 2011-7-14
	 */
	protected final MessageDigest getMessageDigest() {
		String algorithm = "MD5";
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("No such algorithm ["
					+ algorithm + "]");
		}
	}

	/**
	 * 合并原生密码与加密盐,通常加密盐选用用户名,格式为: 3452{pda}
	 * @param rawPass
	 * @param salt
	 * @param strict
	 * @return
	 * @data 2011-7-14
	 */
	protected String mergePasswordAndSalt(String rawPass, Object salt,
			boolean strict) {
		if (rawPass == null) {
			rawPass = "";
		}
		if ((strict)
				&& (salt != null)
				&& (((salt.toString().lastIndexOf("{") != -1) || (salt
						.toString().lastIndexOf("}") != -1)))) {
			throw new IllegalArgumentException(
					"Cannot use { or } in salt.toString()");
		}
		if ((salt == null) || ("".equals(salt))) {
			return rawPass;
		}
		return rawPass + "{" + salt.toString() + "}";
	}
	
	
	/**
	 * 对密码进行md5加密,不借助任何第三方报,与encryptPassword方法效果一样
	 * @param rawPass 原生密码
	 * @param salt 加密盐
	 * @return md5值
	 * @data 2011-7-14
	 */
	public static String encryptPasswordNoDepend(String rawPass, String salt) {
		String saltedPass = rawPass + "{" + salt.toString() + "}";
		String resultStr = "";
		try {
			byte[] temp = saltedPass.getBytes();
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(temp);
			// resultStr = new String(md5.digest());
			byte[] b = md5.digest();
			for (int i = 0; i < b.length; i++) {
				char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
						'9', 'A', 'B', 'C', 'D', 'E', 'F' };
				char[] ob = new char[2];
				ob[0] = digit[(b[i] >>> 4) & 0X0F];
				ob[1] = digit[b[i] & 0X0F];
				resultStr += new String(ob);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return resultStr;
	}

	public String getDefaultSalt() {
		return this.defaultSalt;
	}

	public void setSefaultSalt(String defaultSalt) {
		this.defaultSalt = defaultSalt;
	}

	//encryptPassword
	
	
	/**
	 * 生成文件的md5校验值
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String getFileMD5String(File file){
		String returnMD5Str = null;
		if(file!=null){
			InputStream fis = null;
		    MessageDigest md5 = null;
		    try {
				fis = new FileInputStream(file);
				byte[] buffer = new byte[1024];
				int numRead = 0;
				md5 = MessageDigest.getInstance("MD5");
				while ((numRead = fis.read(buffer)) > 0) {
					md5.update(buffer, 0, numRead);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(fis!=null){
					try {
						fis.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if(md5!=null){
				returnMD5Str = byte2hex(md5.digest());
			}
		}
		return returnMD5Str;
	}
	
	/**
	 * 二行制转字符串
	 * @param b
	 * @return
	 */
	private static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}
	
	public static void main(String [] args){
		
		Md5PwdEncoder md5=new Md5PwdEncoder();
		System.out.println(md5.encryptPassword("72666331684143960514617634483896797086","eagleye"));
		System.out.print(Md5PwdEncoder.encryptPasswordNoDepend("72666331684143960514617634483896797086","eagleye"));
		
		for(int i=0;i<100000;i++){
			System.out.println(md5.encryptPassword("72666331684143960514617634483896797086"+i,"eagleye"));
			System.out.println(Md5PwdEncoder.encryptPasswordNoDepend("72666331684143960514617634483896797086"+i,"eagleye"));
		}
	}
}
