/** 
 * jar名 :  eagleye-core.jar
 * 文件名 ：  VerifyCode.java
 *       (C) Copyright eagleye Corporation 2011
 *           All Rights Reserved.
 * *****************************************************************************
 *    注意： 本内容仅限于优购公司内部使用，禁止转发
 ******************************************************************************/
package com.yougou.eagleye.core.security.verify;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



/**
 * <PRE>
 * 作用:
 *       生成IMG验证码
 * 限制:
 *       无.
 * 注意事项:
 *       1, 先在web.xml配置servlet
 *		  	<servlet>
 *	        <servlet-name>getImg</servlet-name>
 *		      <servlet-class>com.yougou.eagleye.core.security.verify.VerifyCode</servlet-class>
 *		  	</servlet>
 *		  	<servlet-mapping>
 *		  	  <servlet-name>getImg</servlet-name>
 *		  	  <url-pattern>/getImg</url-pattern>
 *		  	</servlet-mapping>
 *		  2, 在html页面上写 <img src="getImg">
 * 修改历史: 注意,如果下载验证码和页面图片下载冲突,则需要在html中的body中预先加载如:<div style="display: none;"><img id="tempVerifyCode" src="<%=basePath%>getImg"></img></div>
 * -----------------------------------------------------------------------------
 *         VERSION       DATE                BY              CHANGE/COMMENT
 * -----------------------------------------------------------------------------
 *          1.0        2011-07-14           null              create
 * -----------------------------------------------------------------------------
 * </PRE>
 */
public class VerifyCode extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.generateImg(request, response);
//		request.setAttribute("verifyCode", str);
//		System.out.println(str);
	}

	/**
	 * 存储验证码的session名称
	 */
	public static final String SESSION_VERIFY_CODE = "VERIFY_CODE";
	
	/**
	 * 生成验证码的数量
	 */
	public static final int CODE_NUM = 4;
	
	//随机字符范围
    private final static char[] CHAR_RANGE = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J',
                    'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T',
                    'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
                    'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm', 'n',
                    'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
                    'y', 'z', '2', '3', '4', '5', '6', '7',
                    '8', '9'
    }; 
	
//	public static Random random = new Random();
	
//	/**
//	 * 生成随机数
//	 * @return
//	 */
//	public static String generateRandomCode(){
//		int index = random.nextInt(CHAR_RANGE.length);
//		String randomCode = CHAR_RANGE[index]+"";
//		return randomCode;
//	}
	
	
	/**
	 * 生成随机验证码图片并输出,存储进session
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String generateImg(HttpServletRequest request,
			HttpServletResponse response)throws ServletException, IOException{
		// 禁止图像缓存。
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		
		Random random = new Random();
		
		// 验证码图片的宽度。
		int width = 60;
		// 验证码图片的高度。
		int height = 20;
		BufferedImage buffImg = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
		
		Graphics2D g = buffImg.createGraphics();
		// 设定图像背景色(因为是做背景，所以偏淡)
		g.setColor(getRandColor(80, 350));
		g.fillRect(0, 0, width, height);
		// 创建字体，字体的大小应该根据图片的高度来定。
		// Font font=new Font("Times New Roman",Font.PLAIN,18);
		g.setFont(new Font("宋体", Font.BOLD, 18));
		// 画边框。
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width - 1, height - 1);
		// 随机产生160条干扰线，使图象中的认证码不易被其它程序探测到。
		g.setColor(Color.GRAY);
//		random.setSeed(System.currentTimeMillis());
		for (int i = 0; i < 50; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}
		// randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
		StringBuilder verifyCode = new StringBuilder();
		// 随机产生4位数字的验证码。
		for (int i = 0; i < CODE_NUM; i++) {
			int index = random.nextInt(CHAR_RANGE.length);
			String randomCode = CHAR_RANGE[index]+"";
//		    String randomCode = generateRandomCode();
			g.setColor(getRandColor(1, 100));
			g.drawString(randomCode, 13 * i + 6, 15);
			// 将产生的四个随机数组合在一起。
			verifyCode.append(randomCode);
		}
		
		//将四位数字的验证码保存到Session中。
//		HttpSession session=request.getSession();
//		session.setAttribute(SESSION_VERIFY_CODE,verifyCode.toString());
		HttpSession session = request.getSession();
		session.setAttribute(SESSION_VERIFY_CODE,verifyCode.toString());
		  
		
		// 将图像输出到Servlet输出流中。
		ServletOutputStream sos = response.getOutputStream();
		ImageIO.write(buffImg, "jpeg", sos);
		buffImg.flush();
		sos.flush();
		sos.close();
		
		return verifyCode.toString();
	}
	

	/**
	 * 获得随机颜色
	 * @param fc
	 * @param bc
	 * @return
	 */
	public static Color getRandColor(int fc, int bc) {// 给定范围获得随机颜色
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
}
