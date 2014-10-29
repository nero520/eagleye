package com.yougou.eagleye.admin.controller;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.MappedSuperclass;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yougou.eagleye.admin.util.DtPageBean;


@MappedSuperclass  //该标注可以作为所有controller的基类使用,其中包括annotation的继承
public class BaseController extends MultiActionController  {

	
	/**
	 * 针对data tables控件的pagebean 进行map封装.
	 * @param modelMap
	 * @param pageBean
	 * @return
	 */
	protected Map<String,Object> formatDtPageBean(Map<String,Object> modelMap,DtPageBean pageBean) {
		if(pageBean!=null){
			if(modelMap==null){
				modelMap = new HashMap<String,Object>();
			}
			modelMap.put("sEcho", pageBean.getSEcho());
			modelMap.put("result", pageBean.getResult());
			modelMap.put("iTotalRecords", pageBean.getTotalCount());
			modelMap.put("iTotalDisplayRecords", pageBean.getTotalCount());
			modelMap.put("page", pageBean.getPageNow());
			modelMap.put("total", pageBean.getTotalPages());
			modelMap.put("records", pageBean.getTotalCount());
		}
		
//		ObjectMapper mapper = new ObjectMapper();
//		try {
//			String result = mapper.writeValueAsString(pageBean);
//			System.out.println(result);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return modelMap;
	}
	
	
	
	/**
	 * 格式化日期和时间
	 * @param binder
	 */
	@InitBinder
	public void formatDateAndTime(WebDataBinder binder){
		binder.registerCustomEditor(Date.class, new DateEditor());
	}
	
	
	/**
	 * 针对controller传递日期的格式化类进行重写,同时满足多种日期格式的格式化
	 */
	private class DateEditor extends PropertyEditorSupport{

		private SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		private SimpleDateFormat dateMinuteFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		private SimpleDateFormat dateTimeFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		@Override
		public String getAsText() {
			Date date=(Date)getValue();
			String value= dateTimeFormat.format(date);
			value.replace(" 00:00:00", "");
			return value;
		}
		
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			text=text==null?"":text.trim();
			if(text.length()==10){//精确到天
				try {
					setValue(dateFormat.parse(text));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if(text.length()==16){//精确到小时
				try {
					setValue(dateMinuteFormat.parse(text));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if(text.length()==19){//精确到秒
				try {
					setValue(dateTimeFormat.parse(text));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
