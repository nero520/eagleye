package com.yougou.eagleye.admin.util;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.yougou.eagleye.admin.domain.EagleyeLog;
import com.yougou.eagleye.core.dao.page.PageBean;


/**
 * jquery插件 DataTables 对应的分页类
 * @param <T>
 */
public class DtPageBean<T> extends PageBean<T>{
	
	
	
	/**
	 * 模糊查询文本
	 */
	private List<String> sSearch;
	
	/**
	 * 各个字段的名称集合
	 */
	private List<String> mDataProp;

	/**
	 * 需要进行排序字段的排序方式,与iSortCol集合一一对应
	 */
	private List<String> sSortDir;
	
	/**
	 * 需要进行排序的字段,与sSortDir集合一一对应
	 */
	private List<String> iSortCol;
	
	/**
	 * 处理过的最终排序字段
	 */
	private String sortStr = " order by id desc";

	public List<String> getSSortDir() {
		return sSortDir;
	}

	public void setSSortDir(List<String> sortDir) {
		sSortDir = sortDir;
	}

	public List<String> getISortCol() {
		return iSortCol;
	}

	public void setISortCol(List<String> sortCol) {
		iSortCol = sortCol;
	}

	public List<String> getSSearch() {
		return sSearch;
	}

	public void setSSearch(List<String> search) {
		sSearch = search;
	}

	public String getSortStr() {
		if(iSortCol!=null && sSortDir!=null && mDataProp!=null && iSortCol.size()!=0 && iSortCol.size() == sSortDir.size() && iSortCol.size()<=mDataProp.size()){
			sortStr =  " order by " + mDataProp.get(Integer.parseInt(iSortCol.get(0))) + " " + sSortDir.get(0);
			for(int i =1;i<sSortDir.size();i++){
				sortStr += "," + mDataProp.get(Integer.parseInt(iSortCol.get(i))) + " " + sSortDir.get(i);
			}
		}
		return sortStr;
	}

	
	
	public String getSortStrNoOrderBy(){
		String sort = null;
		sort =  ","+mDataProp.get(Integer.parseInt(iSortCol.get(0))) + " " + sSortDir.get(0);
		for(int i =1;i<sSortDir.size();i++){
			sortStr += "," + mDataProp.get(Integer.parseInt(iSortCol.get(i))) + " " + sSortDir.get(i);
		}
		return sort;
	}

	public List<String> getMDataProp() {
		return mDataProp;
	}

	public void setMDataProp(List<String> dataProp) {
		mDataProp = dataProp;
	}

	
	/*********************************elasticsearch 使用的分页**************************************/
	
	private Pageable pageable;

	
	public Pageable getPageable() {
		int pageNow = this.getPageNow() - 1;
		int pageSize = this.getPageSize();
		this.pageable = new PageRequest(pageNow, pageSize);
		return pageable;
	}
	
	public void setPageable(Pageable pageable) {
		this.pageable = pageable;
	}
	
	

}
