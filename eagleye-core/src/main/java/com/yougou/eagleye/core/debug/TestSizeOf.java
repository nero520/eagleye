package com.yougou.eagleye.core.debug;

public class TestSizeOf extends SizeOf {

	@Override
	protected Object newInstance() {
		StringBuilder sb = new StringBuilder();
		sb.append("dffffffffffffdddddddddddddddddddddddsdasdasddddddddddddddddd");
		return sb;
	}
	
	
	public static void main(String[] args)throws Exception{
		SizeOf sizeOf = new TestSizeOf();
		System.out.println("所占内存：" + sizeOf.size() + "字节");   
	}

}
