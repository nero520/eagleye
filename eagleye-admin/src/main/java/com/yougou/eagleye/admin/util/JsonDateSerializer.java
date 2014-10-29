package com.yougou.eagleye.admin.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


//import org.codehaus.jackson.JsonGenerator;
//import org.codehaus.jackson.JsonProcessingException;
//import org.codehaus.jackson.map.JsonSerializer;
//import org.codehaus.jackson.map.SerializerProvider;

/**
 * 通过hibernate标注时需要通过该方法对时间属性进行格式化
 * 
 * eg: @Temporal(TemporalType.TIMESTAMP)
 *	   @JsonSerialize(using=JsonDateSerializer.class)
 *	   @Column(name="CREATE_TIME",length=19)
 *	   public Date getCreateTime() {
 *		   return createTime;
 *	   }
 * @author Administrator
 *
 */
public class JsonDateSerializer extends JsonSerializer<Date> {
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void serialize(Date date, JsonGenerator gen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		String formatDate = dateFormat.format(date);
		gen.writeString(formatDate);
	}

}
