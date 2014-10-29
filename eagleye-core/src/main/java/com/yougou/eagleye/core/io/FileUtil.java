/** 
 * jar名 :  eagleye-core.jar
 * 文件名 ：  FileUtil.java
 *       (C) Copyright eagleye Corporation 2011
 *           All Rights Reserved.
 * *****************************************************************************
 *    注意： 本内容仅限于优购公司内部使用，禁止转发
 ******************************************************************************/
package com.yougou.eagleye.core.io;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.util.FileCopyUtils;


/**
 * <PRE>
 * 作用:
 *       文件操作工具类
 * 限制:
 *       无.
 * 注意事项:
 *       无.
 * 修改历史:
 * -----------------------------------------------------------------------------
 *         VERSION       DATE                BY              CHANGE/COMMENT
 * -----------------------------------------------------------------------------
 *          1.0        2011-07-15           null              create
 * -----------------------------------------------------------------------------
 * </PRE>
 */
public class FileUtil {
	
	/**
	 * 写文件
	 * @param filePathAndName 文件路径与名称
	 * @param fileContent  需要写入的文件内容
	 * @param characterSet 字符集编码
	 */
	public static void writeFile(String filePathAndName, String fileContent,String characterSet) {
		try {
			File file = new File(filePathAndName);
			if (!file.exists()) {
				file.createNewFile();
			}
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file), characterSet);
			BufferedWriter writer = new BufferedWriter(write);
			writer.write(fileContent);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取文件返回集合
	 * @param filePathAndName 文件路径和名称
	 * @param characterSet 字符集编码
	 * @return 以行内容为单位的集合
	 */
	public static List<String> readFile(String filePathAndName,String characterSet){
		File file = new File(filePathAndName);
		List<String> list = new ArrayList<String>();
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			InputStreamReader read = new InputStreamReader(new FileInputStream(
					file), characterSet);
			BufferedReader reader = new BufferedReader(read);
			String line = "";
			while ((line = reader.readLine()) != null) {
				list.add(line);
			}
			read.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 追加文件：使用FileWriter
	 * @param filePathAndName 文件路径和名称
	 * @param content  需要追加的内容
	 */
	public static boolean appendWriter(String filePathAndName, String content) {
		try {
			File file = new File(filePathAndName);
			if (!file.exists()) {// 判断是否存在
				file.createNewFile();
			}
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			FileWriter writer = new FileWriter(filePathAndName, true);
			writer.write(content+"\n");
			writer.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 创建文件夹
	 * @param folderPath 文件夹路径和名称
	 */
	public static void createFolder(String folderPathAndName) {
		try {
			File newFolder = new File(folderPathAndName);
			if (!newFolder.exists()) {
				newFolder.mkdir();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件
	 * @param filePathAndName 操作文件的路径和名称
	 */
	public static void delFile(String filePathAndName) {
		try {
			File delFile = new File(filePathAndName);
			delFile.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件夹
	 * @param file
	 */
	public static void deleteFileFolder(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					FileUtil.deleteFileFolder(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
			file.delete();
		} else {
			System.out.println("this filePath is not exist！" + '\n');
		}
	}
	

	/**
	 * 复制文件
	 * @param rawFile 原始文件
	 * @param newFilePathAndName 需要复制到的路径和名称
	 */
	public static void copyFile(File rawFile, String newFilePathAndName) {
		try {
			FileCopyUtils.copy(rawFile, new File(newFilePathAndName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 复制文件夹与文件夹下的文件
	 * @param rawFolderPathAndName 原始文件夹路径和名称
	 * @param newFolderPathAndName 新文件夹和名称
	 */
	public static void copyFolder(String rawFolderPathAndName, String newFolderPathAndName) {
		try {
			(new File(newFolderPathAndName)).mkdirs();
			File a = new File(rawFolderPathAndName);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (rawFolderPathAndName.endsWith(File.separator)) {
					temp = new File(rawFolderPathAndName + file[i]);
				} else {
					temp = new File(rawFolderPathAndName + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newFolderPathAndName
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {
					copyFolder(rawFolderPathAndName + "/" + file[i], newFolderPathAndName + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	/**
	 * 将文件转换为二进制数组
	 * @param file 需要转换的文件
	 * @return 二进制数组
	 */
	public static byte[] file2Bytes(File file){
		byte[] fileData = null;
		try {
			fileData = FileCopyUtils.copyToByteArray(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileData;
	}
	
	/**
	 * 提取文件内容为字符串
	 * @param file 文件
	 * @return 文件内容
	 */
	public static String file2Str(File file){
		String fileStr = null;
		try {
			fileStr = FileCopyUtils.copyToString(new FileReader(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileStr;
	}
	
	/**
	 * 将从url中获取的数据直接写入到指定目录下的fileName文件中
	 * @param url 获取外部数据的url
	 * @param fileName 将获取的外部数据写入的文件名
	 * @param encoding 生成文件的编码
	 * @date 2011-07-06
	 * */
	public void captureHtml2File(String url,String fileNameAndPath,String encoding) throws Exception{

        File file = new File(fileNameAndPath);			//判断文件夹是否存在(以及建立) 
        if (!(file.exists())){//判断文件是否存在，不存在建立
        	file.createNewFile();
        }
		String sCurrentLine="";
		InputStream is; 
		FileWriter fw;
			try {
				String sTotalString=""; 
				URL l_url = new URL(url);
				java.net.HttpURLConnection l_connection = (java.net.HttpURLConnection) l_url.openConnection();
				l_connection.connect();
				is = l_connection.getInputStream();
				java.io.InputStreamReader read = new InputStreamReader(is,encoding);
				java.io.BufferedReader l_reader = new java.io.BufferedReader(read);
				
				while ((sCurrentLine = l_reader.readLine()) != null){
					sTotalString += sCurrentLine+"\r\n";
				}
				Date d = new Date();
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
				sTotalString+="<!-- html create time is : "+df.format(d)+"-->";
	        	//写入数据到文本文件
				fw = new FileWriter(fileNameAndPath); 
				// 将字串写入文件
				fw.write(sTotalString);
				fw.close();
				is.close();
				l_reader.close();
				read.close();
			} catch (Exception e) {
				e.printStackTrace();
			} 
	}
	
	
	
	/**
	 * 将流保存为文件,用来进行文件上传操作
	 * 
	 * 前台controller可能会这样使用
	 * public ModelAndView upload(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request; // TODO
		// 获得文件：
		MultipartFile file = multipartRequest.getFile("tempFile");
		String fileName = file.getOriginalFilename();
		if (file != null) {
			this.SaveFileFromInputStream(file.getInputStream(),"e://",fileName);
		}
		
		// 避免浏览器弹出保存对话框,不是浏览器默认识别类型可能会弹出下载对话框
		response.setContentType("text/html");
		return null;
		}
	 * 
	 * 
	 * 可以最大上传36M的文件
	 * @param stream
	 * @param path
	 * @param filename
	 * @param bufferSize 1024*1024*36 上传文件的缓存大小 整型
	 * @throws IOException
	 */
	public void saveFileFromInputStream(InputStream stream, String path,
			String filename, int bufferSize){
		FileOutputStream fs = null;
		try {
			fs = new FileOutputStream(path + "/" + filename);
//			byte[] buffer = new byte[1024 * 1024 * 36];//可以最大上传36M的文件
			byte[] buffer = new byte[bufferSize];
			int bytesum = 0;
			int byteread = 0;
			while ((byteread = stream.read(buffer)) != -1) {
				bytesum += byteread;
				fs.write(buffer, 0, byteread);
				fs.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				fs.close();
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	/**
	 * 将指定的对象写入指定的文件
	 * 
	 * @param file
	 *            指定写入的文件
	 * @param objs
	 *            要写入的对象
	 */
	public static void doObjToFile(String file, Object[] objs) {
		ObjectOutputStream oos = null;
		try {
			FileOutputStream fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			for (int i = 0; i < objs.length; i++) {
				oos.writeObject(objs[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 返回在文件中指定位置的对象
	 * 
	 * @param file
	 *            指定的文件
	 * @param i
	 *            从1开始
	 * @return
	 */
	public static Object getObjFromFile(String file, int i) {
		ObjectInputStream ois = null;
		Object obj = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			for (int j = 0; j < i; j++) {
				obj = ois.readObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return obj;
	}
	
	
	/**
	 * 返回在文件中指定位置的对象
	 * 
	 * @param file
	 *            指定的文件
	 * @param i
	 *            从1开始
	 * @return
	 */
	public static Object getObjFromFileStream(InputStream fileStream, int i) {
		ObjectInputStream ois = null;
		Object obj = null;
		try {
			ois = new ObjectInputStream(fileStream);
			for (int j = 0; j < i; j++) {
				obj = ois.readObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return obj;
	}
	

	/**
	 * 文件转化为字节数组
	 */
	public static byte[] getBytesFromFile(String filePath) {
		File f = new File(filePath);
		try {
			FileInputStream stream = new FileInputStream(f);
			ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = stream.read(b)) != -1)
				out.write(b, 0, n);
			stream.close();
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 文件流转化为字节数组
	 */
	public static byte[] getBytesFromFileStream(InputStream fileStream) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fileStream.read(b)) != -1)
				out.write(b, 0, n);
			fileStream.close();
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	/**
	 * 把字节数组保存为一个文件
	 */
	public static File getFileFromBytes(byte[] b, String outputFile) {
		BufferedOutputStream stream = null;
		File file = null;
		try {
			file = new File(outputFile);
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return file;
	}
	
	
	
	/**
	 * 从字节数组获取对象
	 */
	public static Object getObjectFromBytes(byte[] objBytes) {
		if (objBytes == null || objBytes.length == 0) {
			return null;
		}
		Object obj = null;
		ByteArrayInputStream bi = new ByteArrayInputStream(objBytes);

		ObjectInputStream oi = null;
		try {
			oi = new ObjectInputStream(bi);
			obj = oi.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (oi != null) {
					oi.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return obj;
	}

	/**
	 * 从对象获取一个字节数组
	 */
	public static byte[] getBytesFromObject(Serializable obj) {
		if (obj == null) {
			return null;
		}
		byte[] bytes = null;
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream oo = null;
		try {
			oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);
			bytes = bo.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (oo != null) {
					oo.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bytes;
	}
	
	
	public static byte[] getByte(Object obj) {
		byte[] bt = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream objos = null;
		try {
			if (obj != null) {
				objos = new ObjectOutputStream(baos);
				objos.writeObject(obj);
				bt = baos.toByteArray();
			}
		} catch (Exception e) {
			bt = (byte[]) null;
			e.printStackTrace();
		}finally{
			if(objos!=null){
				try {
					objos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return bt;
	}

	
	public static void main(String[] args){
		FileUtil.copyFolder("D:/javaserver/tomcat6.0.32/webapps/eln", "c:/test/elnfile20120704150800.bak");
//		FileUtil.createFolder("d:\\test1");
	}
	
}