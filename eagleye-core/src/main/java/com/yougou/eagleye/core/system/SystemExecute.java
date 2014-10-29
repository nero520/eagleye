package com.yougou.eagleye.core.system;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

public class SystemExecute {

	
	
	/**
	 * 运行shell
	 * 
	 * @param shStr
	 *            需要执行的shell
	 * @return
	 * @throws IOException
	 */
	public static List<String> runShell(String shStr) throws Exception {
		List<String> strList = new ArrayList<String>();
		Process process = null;
		//注:如果sh中含有awk,一定要按new String[]{"/bin/sh","-c",shStr}写,才可以获得流
		process = Runtime.getRuntime().exec(shStr);
		InputStreamReader ir = new InputStreamReader(process.getInputStream());
		LineNumberReader input = new LineNumberReader(ir);
		String line;
		while ((line = input.readLine()) != null){
		    strList.add(line);
		}
		process.waitFor();
		process.destroy();
		input.close();
		ir.close();
		return strList;
	}
	
	
	
	/**
	 * 运行cmd命令
	 * 
	 * @param cmdStr
	 *            需要执行的cmd命令
	 * @return
	 * @throws IOException
	 */
	public static List<String> runCmd(String cmdStr) throws Exception {
		List<String> strList = new ArrayList<String>();
		Process process = null;
		process = Runtime.getRuntime().exec(cmdStr);
		InputStreamReader ir = new InputStreamReader(process.getInputStream());
		LineNumberReader input = new LineNumberReader(ir);
		String line;
		while ((line = input.readLine()) != null){
		    strList.add(line);
		}
		process.waitFor();
		process.destroy();
		input.close();
		ir.close();
		return strList;
	}
}
