
package com.yougou.eagleye.core.dao.page;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;

/**
 * <PRE>
 * 作用:
 *     与具体ORM实现无关的分页参数及查询结果封装.
 *     注意所有序号从1开始.
 * 限制:
 *       无。
 * 注意事项:
 *       无。
 * 修改历史:
 * -----------------------------------------------------------------------------
 *         VERSION       DATE                BY              CHANGE/COMMENT
 * -----------------------------------------------------------------------------
 *          1.0        2011-07-12           null              create
 * -----------------------------------------------------------------------------
 * </PRE>
 */
@XmlRootElement
public class PageBean<T> {
	
	// 正序排列标识
	public static final String ASC = "asc";
	//倒序排列标识
	public static final String DESC = "desc";

	//当前页数
	protected Integer pageNow = 1;
	//每页多少条
	protected Integer pageSize = 10;
	//排序字段
	protected String orderBy = null;
	//排序方向
	protected String order = null;
	
	//返回结果 //
	protected List<T> result = Collections.emptyList();
	//总记录数
	protected Integer totalCount = -1;
	
	protected Integer totalPages = -1;
	
	//扩展属性，设定当前显示的翻页列表中的最小编号
	private Integer rangeMin;
	
	//扩展属性，设定当前显示的翻页列表中的最大编号
	private Integer rangeMax;
	
	//第一页
	private Integer firstPage;
	
	//最后一页
	private Integer lastPage;
	
	//前一页
	private Integer prePage;
	
	//下一页
	private Integer nextPage;
	


	public Integer getFirstPage() {
		return firstPage;
	}


	public void setFirstPage(Integer firstPage) {
		this.firstPage = firstPage;
	}


	public Integer getLastPage() {
		return lastPage;
	}


	public void setLastPage(Integer lastPage) {
		this.lastPage = lastPage;
	}


	public void setPrePage(Integer prePage) {
		this.prePage = prePage;
	}


	public void setNextPage(Integer nextPage) {
		this.nextPage = nextPage;
	}


	public Integer getRangeMin() {
		return rangeMin;
	}


	public void setRangeMin(Integer rangeMin) {
		this.rangeMin = rangeMin;
	}


	public Integer getRangeMax() {
		return rangeMax;
	}


	public void setRangeMax(Integer rangeMax) {
		this.rangeMax = rangeMax;
	}


	// 构造函数 //
	public PageBean() {
	}

	
	public PageBean(final Integer pageSize) {
		setPageSize(pageSize);
	}

	public PageBean(final Integer pageSize, final Boolean autoCount) {
		setPageSize(pageSize);
	}

	// 查询参数访问函数 //

	/**
	 * 获得当前页的页号,序号从1开始,默认为1.
	 */
	public Integer getPageNow() {
		return pageNow;
	}

	/**
	 * 设置当前页的页号,序号从1开始,低于1时自动调整为1.
	 */
	public void setPageNow(final Integer pageNow) {
		this.pageNow = pageNow;
		if (pageNow < 1) {
			this.pageNow = 1;
		}
	}

	/**
	 * 获得每页的记录数量,默认为1.
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * 设置每页的记录数量,低于1时自动调整为1.
	 */
	public void setPageSize(final Integer pageSize) {
		this.pageSize = pageSize;
		if (pageSize < 1) {
			this.pageSize = 1;
		}
	}

	/**
	 * 根据pageNow和pageSize计算当前页第一条记录在总结果集中的位置,序号从1开始.
	 */
	public Integer getFirst() {
		return ((pageNow - 1) * pageSize) + 1;
	}

	/**
	 * 获得排序字段,无默认值.多个排序字段时用','分隔.
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * 设置排序字段,多个排序字段时用','分隔.
	 */
	public void setOrderBy(final String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * 是否已设置排序字段,无默认值.
	 */
	public Boolean isOrderBySetted() {
		return (StringUtils.isNotBlank(orderBy) && StringUtils.isNotBlank(order));
	}

	/**
	 * 获得排序方向.
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * 设置排序方式向.
	 * 
	 * @param order 可选值为desc或asc,多个排序字段时用','分隔.
	 */
	public void setOrder(final String order) {
		//检查order字符串的合法值
		String[] orders = StringUtils.split(StringUtils.lowerCase(order), ',');
		for (String orderStr : orders) {
			if (!StringUtils.equals(DESC, orderStr) && !StringUtils.equals(ASC, orderStr))
				throw new IllegalArgumentException("排序方向" + orderStr + "不是合法值");
		}
		this.order = StringUtils.lowerCase(order);
	}


	// 访问查询结果函数 //

	/**
	 * 取得页内的记录列表.
	 */
	public List<T> getResult() {
		return result;
	}

	public void setResult(final List<T> result) {
		this.result = result;
	}

	/**
	 * 取得总记录数, 默认值为-1.
	 */
	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(final Integer totalCount) {
		this.totalCount = totalCount;
		this.iTotalRecords = totalCount;
		this.iTotalDisplayRecords = totalCount;
	}

	/**
	 * 根据pageSize与totalCount计算总页数, 默认值为-1.
	 */
	public Integer getTotalPages() {
		if (totalCount < 0){
			return -1;
		}
		Integer count = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			count++;
		}
		return count;
	}
	
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	/**
	 * 是否还有下一页.
	 */
	public Boolean isHasNext() {
		return (pageNow + 1 <= getTotalPages());
	}

	/**
	 * 取得下页的页号, 序号从1开始.
	 * 当前页为尾页时仍返回尾页序号.
	 */
	public Integer getNextPage() {
		if (isHasNext()){
			return pageNow + 1;
		}else{
			return pageNow;	
		}
	}

	/**
	 * 是否还有上一页.
	 */
	public Boolean isHasPre() {
		return (pageNow - 1 >= 1);
	}

	/**
	 * 取得上页的页号, 序号从1开始.
	 * 当前页为首页时返回首页序号.
	 */
	public Integer getPrePage() {
		if (isHasPre()){
			return pageNow - 1;
		}else{
			return pageNow;
		}
	}
	
	
	//设定记录条数的时候要做相关页数，以及当前页的设定
	public void setRecordCount(Integer totalCount) {
		// 设定记录总述
		this.totalCount = totalCount;
		//计算页数
		this.totalPages=this.totalCount/pageSize+(this.totalCount%pageSize==0?0:1);
		if(this.pageNow>this.totalPages){
			pageNow=totalPages>=1?totalPages:1;
		}
		//开始设定扩展属性,设定相关显示的范围
		if(this.getPageNow()<=10&&this.getTotalPages()<=10){
			this.rangeMin=1;
			this.rangeMax=this.getTotalPages();
			if(this.rangeMax==0){
				this.rangeMax=1;
			}
		}else if(this.getPageNow()<=10&&this.getTotalPages()>=10){
			this.rangeMin=1;
			this.rangeMax=10;
		}else if(this.getPageNow()>(this.getTotalPages()-10)){
			this.rangeMin=this.getTotalPages()-9;
			this.rangeMax=this.getTotalPages();
		}else{
			this.rangeMin=this.getPageNow()-4;
			this.rangeMax=this.getPageNow()+4;
		}
		
		//开始设置前后页、首尾页面
		this.firstPage= 1;
		this.lastPage=this.getTotalPages();
		this.nextPage=Math.min(getPageNow() + 1, this.getTotalPages());
		this.prePage=Math.max(getPageNow() - 1, 1);
	}
	
	public Integer getRecordCount() {
		return null;
	}
	
	
	
	
	/*************************兼容data tables 插件的分页参数 ******************/
	/**
	 * 当前页从第几条开始
	 */
	private int iDisplayStart = 0;
	
	/**
	 * 一页显示多少条
	 */
	private int iDisplayLength = 0;

	/**
	 * 总记录数
	 */
	private int iTotalRecords = 0;
	
	private int iTotalDisplayRecords = 0;
	
	/**用于传递次数**/
	private int sEcho = 0;


	public int getITotalRecords() {
		return iTotalRecords;
	}


	public void setITotalRecords(int totalRecords) {
		iTotalRecords = totalRecords;
	}


	public int getIDisplayStart() {
		return iDisplayStart;
	}


	public void setIDisplayStart(int displayStart) {
		iDisplayStart = displayStart+1;
		if(this.iDisplayLength>0){
			this.pageNow = this.iDisplayStart/this.iDisplayLength +1;
		}
		if(this.iDisplayLength==-1){//如果为-1则默认全部数据,就分一页
			this.pageNow = 1;
		}
	}


	public int getIDisplayLength() {
		return iDisplayLength;
	}


	public void setIDisplayLength(int displayLength) {
		iDisplayLength = displayLength;
		
		if(this.iDisplayLength>0){
			this.pageSize = displayLength;
			this.pageNow = this.iDisplayStart/this.iDisplayLength +1;
		}
		if(this.iDisplayLength==-1){//如果为-1则默认全部数据,就分一页,最多取20000条数据
			this.pageNow = 1;
			this.pageSize = 20000;
		}
	}


	public int getSEcho() {
		return sEcho;
	}


	public void setSEcho(int sEcho) {
		this.sEcho = sEcho;
	}


	public int getITotalDisplayRecords() {
		return iTotalDisplayRecords;
	}


	public void setITotalDisplayRecords(int totalDisplayRecords) {
		iTotalDisplayRecords = totalDisplayRecords;
	}


	
}
