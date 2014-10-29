package com.yougou.eagleye.core.dom;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * <PRE>
 * 作用:
 *      使用Jaxb2.0持久化XML的Binder.
 * 		JAXB（Java Architecture for XML Binding) 是一个业界的标准，
 * 		是一项可以根据XML Schema产生Java类的技术。该过程中，
 * 		JAXB也提供了将XML实例文档反向生成Java对象树的方法，
 * 		并能将Java对象树的内容重新写到 XML实例文档。
 * 		从另一方面来讲，JAXB提供了快速而简便的方法将XML模式绑定到Java表示，
 * 		从而使得Java开发者在Java应用程序中能方便地结合XML数据和处理函数
 * 限制:
 *       无。
 * 注意事项:
 *       无。
 * 修改历史:
 * -----------------------------------------------------------------------------
 *         VERSION       DATE                BY              CHANGE/COMMENT
 * -----------------------------------------------------------------------------
 *          1.0        2010-07-12           刘宏伟              create
 * -----------------------------------------------------------------------------
 * </PRE>
 */
public class JaxbBinder{
	
	private Marshaller marshaller;
	
	private Unmarshaller unmarshaller;

	/**
	 * 构造函数
	 * @param types 所有需要序列化的Root对象的类型.
	 */
	public JaxbBinder(Class<?>... types) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(types);
			marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);
			unmarshaller = jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Java->Xml.
	 * 被转换的对象类中需要加上标注:"@XmlRootElement"
	 * @param root 需要转换的对象
	 * @return 转换后的xml字符串
	 */
	public String toXml(Object root) {
		try {
			StringWriter writer = new StringWriter();
			marshaller.marshal(root, writer);
			return writer.toString();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Xml->Java.
	 * 将要转换成的java对象类需要加上标注:"@XmlRootElement"
	 * @param xml
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public <T> T fromXml(String xml) {
		try {
			StringReader reader = new StringReader(xml);
			return (T) unmarshaller.unmarshal(reader);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	public Marshaller getMarshaller() {
		return marshaller;
	}

	public Unmarshaller getUnmarshaller() {
		return unmarshaller;
	}
	
	public static void main(String[] args){
//		JaxbBinder jb = new JaxbBinder(eagleyeLicense.class);
//		eagleyeLicense svl = new eagleyeLicense();
//		svl.setAuthor("panda");
//		svl.setCreationDate("2011-09-19 11:20");
//		svl.setExpiresDate("2011-12-20");
//		svl.setLicenseID("1");
//		svl.setNum("20");
//		svl.setSignature("sdfdfsd$sdf%sdf");
//		svl.setSystemIuid("eln");
//		svl.setVersion("1.0");
//		
//		String str = jb.toXml(svl);
////		String xml = "<pageBean><autoCount>true</autoCount>"
////                   +"<pageNow>1</pageNow>"
////                   +"<pageSize>1</pageSize>"
////                   +"<totalCount>-1</totalCount>"
////                   +"</pageBean>";
////		PageBean pb = jb.fromXml(xml);
//		System.out.println(str);
	}
}
