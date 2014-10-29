/*******************************************************************************
 * jar名 :  eagleye-core.jar
 * 文件名 ： HibernateBaseDao.java
 *       (C) Copyright eagleye Corporation 2011
 *           All Rights Reserved.
 * *****************************************************************************
 *    注意： 本内容仅限于优购公司内部使用，禁止转发
 ******************************************************************************/
package com.yougou.eagleye.core.dao.hibernate3;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import com.yougou.eagleye.core.dao.page.PageBean;


/**
 * <PRE>
 * 作用: 
 *      1,hibernate底层Dao实现
 * 限制:
 *       建议通过service层调用dao层,将事务控制放在service层上。 
 * 注意事项:
 *       该基类是建立在TransactionInterceptor的基础上,
 *       所以通过junit测试时直接调用底层dao的方法需要手动去释放session,
 *       如果剥离事务控制,需要对session进行额外的控制。
 * 修改历史:
 * -----------------------------------------------------------------------------
 *         VERSION       DATE                BY              CHANGE/COMMENT
 * -----------------------------------------------------------------------------
 *          1.0        2011-07-12           null              create
 * -----------------------------------------------------------------------------
 * </PRE>
 */
public abstract class HibernateBaseDao<T> extends HibernateDaoSupport{

	private static Logger logger = Logger.getLogger(HibernateBaseDao.class);
	
	private static int fetchSize = 20000;
	
	private static int timeout = 30;
	

	/**
	 * 查询限制超时时长
	 * @param timeout (单位:s)
	 */
	public static void setTimeout(int timeout) {
		HibernateBaseDao.timeout = timeout;
	}

	/**
	 * 设置批量查询的数量,避免大数据量查询并且不分页时造成的内存溢出
	 * 默认是500条
	 * @param fetchSize 批量查询的数量
	 * @data 2011-7-14
	 */
	public static void setFetchSize(int fetchSize) {
		HibernateBaseDao.fetchSize = fetchSize;
	}
	
	
	/****************************************基于hql的操作start**************************************************/
	
	/*************************查询单个对象***************************/
	/**
	 * 用map传参以查询单个对象
	 * @param hql
	 * @param paramMap 需要传递的参数,参数支持new Object[]{"1","2"},
	 *                 如果通过id批量查询可以在hql或sql中写 id in (:ids)其中ids必须是一个object数组,如果是字符串则不行
	 * @param comment  注释
	 * @data 2011-7-14
	 */
	@SuppressWarnings("unchecked")
	public final T uniqueQueryByHql(String hql, Map<String, Object> paramMap,String comment){
//		logger.info("HibernateBaseDao:[uniqueQueryByHql]");
		Query q = this.getSession(false).createQuery(hql).setTimeout(timeout);
		if(comment!=null){//添加注释
			q.setComment(comment);
		}
		String[] keys = q.getNamedParameters();
		if (keys == null || paramMap == null) {
			return (T) q.uniqueResult();
		}
		for (String key : keys) {
			Object obj = paramMap.get(key);
			if(obj!=null && obj.getClass().isArray()){//如果是数组
				q.setParameterList(key, (Object[])obj);
			}else{//如果不是数组则走单个的setParam
				q.setParameter(key, obj);
			}
		}
//		releaseSession(session);//释放session,避免占用资源
		return (T) q.uniqueResult();
	}
	/**
	 * 用map传参以查询单个对象
	 * @param hql
	 * @param paramMap 需要传递的参数,参数支持new Object[]{"1","2"},
	 *                 如果通过id批量查询可以在hql或sql中写 id in (:ids)其中ids必须是一个object数组,如果是字符串则不行
	 * @param comment  注释
	 * @param cacheable 是否开启二级缓存
	 * @data 2011-7-14
	 */
	@SuppressWarnings("unchecked")
	public final T uniqueQueryByHql(String hql, Map<String, Object> paramMap,String comment,boolean cacheable){
//		logger.info("HibernateBaseDao:[uniqueQueryByHql]");
		Query q = this.getSession(false).createQuery(hql).setTimeout(timeout);
		q.setCacheable(cacheable);
		if(comment!=null){//添加注释
			q.setComment(comment);
		}
		String[] keys = q.getNamedParameters();
		if (keys == null || paramMap == null) {
			return (T) q.uniqueResult();
		}
		for (String key : keys) {
			Object obj = paramMap.get(key);
			if(obj!=null && obj.getClass().isArray()){//如果是数组
				q.setParameterList(key, (Object[])obj);
			}else{//如果不是数组则走单个的setParam
				q.setParameter(key, obj);
			}
		}
//		releaseSession(session);//释放session,避免占用资源
		return (T) q.uniqueResult();
	}
	
	/**
	 * 用map传参数执行update,delete,insert的hql语句
	 * @param hql
	 * @param paramMap 需要传递的参数,参数支持new Object[]{"1","2"},
	 *                 如果通过id批量查询可以在hql或sql中写 id in (:ids)其中ids必须是一个object数组,如果是字符串则不行
	 * @param comment  注释
	 * @data 2011-7-14
	 */
	@SuppressWarnings("unchecked")
	public final int excuteByHql(String hql, Map<String, Object> paramMap,String comment){
//		logger.info("HibernateBaseDao:[excuteByHql]");
		Query q = this.getSession(false).createQuery(hql).setTimeout(timeout);
		if(comment!=null){//添加注释
			q.setComment(comment);
		}
		String[] keys = q.getNamedParameters();   
		if (keys == null || paramMap == null) {
			return q.executeUpdate();
		}
		for (String key : keys) {
			Object obj = paramMap.get(key);
			if(obj!=null && obj.getClass().isArray()){//如果是数组
				q.setParameterList(key, (Object[])obj);
			}else{//如果不是数组则走单个的setParam
				q.setParameter(key, obj);
			}
		}
//		releaseSession(session);//释放session,避免占用资源
		return q.executeUpdate();
	}
	
	
	/**********************查询多个对象****************************/
	/**
	 * 不分页用map传参查询集合
	 * @param hql
	 * @param paramMap 需要传递的参数,参数支持new Object[]{"1","2"},
	 *                 如果通过id批量查询可以在hql或sql中写 id in (:ids)其中ids必须是一个object数组,如果是字符串则不行
	 * @param comment  注释
	 * @data 2011-7-14
	 */
	@SuppressWarnings("unchecked")
	public final List<T> queryByHql(String hql, Map<String, Object> paramMap,String comment){
//		logger.debug("HibernateBaseDao:[queryByHql]");
		return this.queryByHql(hql, paramMap, comment,false);
	}
	
	/**
	 * 不分页用map传参查询集合,可以控制是否使用二级缓存
	 * @param hql
	 * @param paramMap 需要传递的参数,参数支持new Object[]{"1","2"},
	 *                 如果通过id批量查询可以在hql或sql中写 id in (:ids)其中ids必须是一个object数组,如果是字符串则不行
	 * @param comment  注释
	 * @param cacheable 是否开启二级缓存
	 * @data 2011-7-14
	 */
	@SuppressWarnings("unchecked")
	public final List<T> queryByHql(String hql, Map<String, Object> paramMap,String comment,boolean cacheable){
//		logger.debug("HibernateBaseDao:[queryByHql]");
		Query q = this.getSession(false).createQuery(hql).setTimeout(timeout);
		q.setCacheable(cacheable);//开启二级缓存
		if(comment!=null){//添加注释
			q.setComment(comment);
		}
		String[] keys = q.getNamedParameters();
		if (keys == null || paramMap == null) {
			return q.setMaxResults(fetchSize).list();
		}
		for (String key : keys) {
			Object obj = paramMap.get(key);
			if(obj!=null && obj.getClass().isArray()){//如果是数组
				q.setParameterList(key, (Object[])obj);
			}else{//如果不是数组则走单个的setParam
				q.setParameter(key, obj);
			}
		}
		return q.setMaxResults(fetchSize).list();
	}
	
	/**
	 * 不分页用map传参查询集合
	 * @param hql
	 * @param paramMap 需要传递的参数,参数支持new Object[]{"1","2"},
	 *                 如果通过id批量查询可以在hql或sql中写 id in (:ids)其中ids必须是一个object数组,如果是字符串则不行
	 * @param comment  注释
	 * @data 2011-01-16 12:52
	 */
	@SuppressWarnings("unchecked")
	public final List<T> queryByHql(String hql, Map<String, Object> paramMap,String comment,int iRows){
//		logger.debug("HibernateBaseDao:[queryByHql]");
		return queryByHql(hql, paramMap,comment,iRows,false);
	}
	
	/**
	 * 不分页用map传参查询集合
	 * @param hql
	 * @param paramMap 需要传递的参数,参数支持new Object[]{"1","2"},
	 *                 如果通过id批量查询可以在hql或sql中写 id in (:ids)其中ids必须是一个object数组,如果是字符串则不行
	 * @param comment  注释
	 * @param iRows 一共取多少数据
	 * @param cacheable 是否开启二级缓存
	 * @data 2011-01-16 12:52
	 */
	public final List<T> queryByHql(String hql, Map<String, Object> paramMap,String comment,int iRows,boolean cacheable){
//		logger.debug("HibernateBaseDao:[queryByHql]");
		Query q = this.getSession(false).createQuery(hql).setTimeout(timeout);
		q.setCacheable(cacheable);
		q.setFirstResult(0);
		//q.setMaxResults(iRows);
		
		if(comment!=null){//添加注释
			q.setComment(comment);
		}
		String[] keys = q.getNamedParameters();
		if (keys == null || paramMap == null) {
			return q.setMaxResults(iRows).list();
		}
		for (String key : keys) {
			Object obj = paramMap.get(key);
			if(obj!=null && obj.getClass().isArray()){//如果是数组
				q.setParameterList(key, (Object[])obj);
			}else{//如果不是数组则走单个的setParam
				q.setParameter(key, obj);
			}
		}
		return q.setMaxResults(iRows).list();
	}
	
	
	/**********************分页查询**************************/
	/**
	 * 分页查询,通过hql,用Map传参,
	 * @param hql
	 * @param countHql 获取结果数的hql
	 * @param pageBean 分页信息,如果该信息为null则直接是查询方法
	 * @param paramMap 需要传递的参数,参数支持new Object[]{"1","2"},
	 *                 如果通过id批量查询可以在hql或sql中写 id in (:ids)其中ids必须是一个object数组,如果是字符串则不行
	 * @param comment  注释
	 * @data 2011-7-14
	 */
	@SuppressWarnings("unchecked")
	public final PageBean<T> queryPageByHql(String hql,PageBean<T> pageBean,Map<String, Object> paramMap,String comment) {
//		logger.debug("HibernateBaseDao:[queryPageByHql:PageBean<T>]");
		return this.queryPageByHql(hql, pageBean, paramMap, comment,false);
	}
	
	
	
	/**
	 * 分页查询,通过hql,用Map传参,
	 * @param hql
	 * @param countHql 获取结果数的hql,此方法主要用来对待含有distinct关键词的查询语句
	 * @param pageBean 分页信息,如果该信息为null则直接是查询方法
	 * @param paramMap 需要传递的参数,参数支持new Object[]{"1","2"},
	 *                 如果通过id批量查询可以在hql或sql中写 id in (:ids)其中ids必须是一个object数组,如果是字符串则不行
	 * @param comment  注释
	 * @data 2011-7-14
	 */
	@SuppressWarnings("unchecked")
	public final PageBean<T> queryPageByHql(String hql,String countHql,PageBean<T> pageBean,Map<String, Object> paramMap,String comment,boolean cacheable) {
//		logger.debug("HibernateBaseDao:[queryPageByHql:PageBean<T>]");
		if (pageBean != null) {
			Query q = this.getSession(false).createQuery(hql).setTimeout(timeout);
			q.setCacheable(cacheable);//开启二级缓存
			if(comment!=null){//添加注释
				q.setComment(comment);
			}
			q.setFirstResult((pageBean.getPageNow()-1)*pageBean.getPageSize());
			q.setMaxResults(pageBean.getPageSize());
			String[] keys = q.getNamedParameters();
			if (keys != null && paramMap!=null) {
				for (String key : keys) {
					Object obj = paramMap.get(key);
					if(obj!=null && obj.getClass().isArray()){//如果是数组
						q.setParameterList(key, (Object[])obj);
					}else{//如果不是数组则走单个的setParam
						q.setParameter(key, obj);
					}
				}
			}
			pageBean.setResult(q.list());
			
			//查询结果集数量
			if(countHql!=null && !"".equals(countHql)){
				int totalCount = Integer.parseInt(this.uniqueQueryByHql(countHql, paramMap,null).toString()) ;
				pageBean.setTotalCount(totalCount);
				pageBean.setRecordCount(totalCount);
			}
			return pageBean;
		}else{
			PageBean<T> pb = new PageBean<T>();
			List<T> list = this.queryByHql(hql, paramMap, comment,cacheable);
			pb.setResult(list);
			return pb;
		}
	}
	
	
	
	/**
	 * 分页查询,通过hql,用Map传参,可以控制是否使用二级缓存
	 * @param hql
	 * @param countHql 获取结果数的hql
	 * @param pageBean 分页信息,如果该信息为null则直接是查询方法
	 * @param paramMap 需要传递的参数,参数支持new Object[]{"1","2"},
	 *                 如果通过id批量查询可以在hql或sql中写 id in (:ids)其中ids必须是一个object数组,如果是字符串则不行
	 * @param comment  注释
	 * @param cacheable 是否开启二级缓存
	 * @data 2011-7-14
	 */
	@SuppressWarnings("unchecked")
	public final PageBean<T> queryPageByHql(String hql,PageBean<T> pageBean,Map<String, Object> paramMap,String comment,boolean cacheable) {
//		logger.debug("HibernateBaseDao:[queryPageByHql:PageBean<T>]");
		if (pageBean != null) {
			Query q = this.getSession(false).createQuery(hql).setTimeout(timeout);
			q.setCacheable(cacheable);//开启二级缓存
			if(comment!=null){//添加注释
				q.setComment(comment);
			}
			q.setFirstResult((pageBean.getPageNow()-1)*pageBean.getPageSize());
			q.setMaxResults(pageBean.getPageSize());
			String[] keys = q.getNamedParameters();
			if (keys != null && paramMap!=null) {
				for (String key : keys) {
					Object obj = paramMap.get(key);
					if(obj!=null && obj.getClass().isArray()){//如果是数组
						q.setParameterList(key, (Object[])obj);
					}else{//如果不是数组则走单个的setParam
						q.setParameter(key, obj);
					}
				}
			}
			pageBean.setResult(q.list());
			
			//查询结果集数量
			String countHql = " select count(*) " + removeSelect(removeOrders(hql));
			if(countHql!=null && !"".equals(countHql)){
				int totalCount = Integer.parseInt(this.uniqueQueryByHql(countHql, paramMap,null).toString()) ;
				pageBean.setTotalCount(totalCount);
				pageBean.setRecordCount(totalCount);
			}
			return pageBean;
		}else{
			PageBean<T> pb = new PageBean<T>();
			List<T> list = this.queryByHql(hql, paramMap, comment,cacheable);
			pb.setResult(list);
			return pb;
		}
	}
	
	
	/**********************分页查询**************************/
	/**
	 * 分页查询,通过hql,用Map传参, 没有泛型
	 * @param hql
	 * @param countHql 获取结果数的hql
	 * @param pageBean 分页信息,如果该信息为null则直接是查询方法
	 * @param paramMap 需要传递的参数,参数支持new Object[]{"1","2"},
	 *                 如果通过id批量查询可以在hql或sql中写 id in (:ids)其中ids必须是一个object数组,如果是字符串则不行
	 * @param comment  注释
	 * @data 2011-7-14
	 */
	@SuppressWarnings("unchecked")
	public final PageBean queryPageByHqlNoGenerics(String hql,PageBean pageBean,Map<String, Object> paramMap,String comment) {
//		logger.debug("HibernateBaseDao:[queryPageByHql:PageBean]");
		return this.queryPageByHqlNoGenerics(hql,pageBean,paramMap,comment,false);
	}
	
	
	/**
	 * 分页查询,通过hql,用Map传参, 没有泛型,可以控制是否开启二级缓存
	 * @param hql
	 * @param countHql 获取结果数的hql
	 * @param pageBean 分页信息,如果该信息为null则直接是查询方法
	 * @param paramMap 需要传递的参数,参数支持new Object[]{"1","2"},
	 *                 如果通过id批量查询可以在hql或sql中写 id in (:ids)其中ids必须是一个object数组,如果是字符串则不行
	 * @param comment  注释
	 * @param cacheable 是否可以开启二级缓存
	 * @data 2011-7-14
	 */
	@SuppressWarnings("unchecked")
	public final PageBean queryPageByHqlNoGenerics(String hql,PageBean pageBean,Map<String, Object> paramMap,String comment,boolean cacheable) {
//		logger.debug("HibernateBaseDao:[queryPageByHql:PageBean]");
		if (pageBean != null) {
			Query q = this.getSession(false).createQuery(hql).setTimeout(timeout);
			q.setCacheable(cacheable);
			if(comment!=null){//添加注释
				q.setComment(comment);
			}
			q.setFirstResult((pageBean.getPageNow()-1)*pageBean.getPageSize());
			q.setMaxResults(pageBean.getPageSize());
			String[] keys = q.getNamedParameters();
			if (keys != null && paramMap!=null) {
				for (String key : keys) {
					Object obj = paramMap.get(key);
					if(obj!=null && obj.getClass().isArray()){//如果是数组
						q.setParameterList(key, (Object[])obj);
					}else{//如果不是数组则走单个的setParam
						q.setParameter(key, obj);
					}
				}
			}
			pageBean.setResult(q.list());
			//查询结果集数量
			String countHql = " select count(*) " + removeSelect(removeOrders(hql));
			if(countHql!=null && !"".equals(countHql)){
				int totalCount = Integer.parseInt(this.uniqueQueryByHql(countHql, paramMap,null).toString()) ;
				pageBean.setTotalCount(totalCount);
				pageBean.setRecordCount(totalCount);
			}
			return pageBean;
		}else{
			PageBean pb = new PageBean();
			List list = this.queryByHql(hql, paramMap, comment,cacheable);
			pb.setResult(list);
			return pb;
		}
	}
	
	
	/**
	 * 分页查询,通过hql,用Map传参,
	 * @param hql
	 * @param countHql 获取结果数的hql,此方法主要用来对待含有distinct关键词的查询语句
	 * @param pageBean 分页信息,如果该信息为null则直接是查询方法
	 * @param paramMap 需要传递的参数,参数支持new Object[]{"1","2"},
	 *                 如果通过id批量查询可以在hql或sql中写 id in (:ids)其中ids必须是一个object数组,如果是字符串则不行
	 * @param comment  注释
	 * @data 2011-7-14
	 */
	@SuppressWarnings("unchecked")
	public final PageBean queryPageByHqlNoGenerics(String hql,String countHql,PageBean pageBean,Map<String, Object> paramMap,String comment,boolean cacheable) {
//		logger.debug("HibernateBaseDao:[queryPageByHql:PageBean<T>]");
		if (pageBean != null) {
			Query q = this.getSession(false).createQuery(hql).setTimeout(timeout);
			q.setCacheable(cacheable);//开启二级缓存
			if(comment!=null){//添加注释
				q.setComment(comment);
			}
			q.setFirstResult((pageBean.getPageNow()-1)*pageBean.getPageSize());
			q.setMaxResults(pageBean.getPageSize());
			String[] keys = q.getNamedParameters();
			if (keys != null && paramMap!=null) {
				for (String key : keys) {
					Object obj = paramMap.get(key);
					if(obj!=null && obj.getClass().isArray()){//如果是数组
						q.setParameterList(key, (Object[])obj);
					}else{//如果不是数组则走单个的setParam
						q.setParameter(key, obj);
					}
				}
			}
			pageBean.setResult(q.list());
			
			//查询结果集数量
			if(countHql!=null && !"".equals(countHql)){
				int totalCount = Integer.parseInt(this.uniqueQueryByHql(countHql, paramMap,null).toString()) ;
				pageBean.setTotalCount(totalCount);
				pageBean.setRecordCount(totalCount);
			}
			return pageBean;
		}else{
			PageBean pb = new PageBean();
			List list = this.queryByHql(hql, paramMap, comment,cacheable);
			pb.setResult(list);
			return pb;
		}
	}
	
	
	
	/**
	 * 根据ID获取对象. 实际调用Hibernate的session.load()方法返回实体或其proxy对象. 如果对象不存在，抛出异常.
	 * @param entityClass 需要查询的实体
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final T getSingle(Class<T> entityClass, Serializable id) {
		Object obj = this.getHibernateTemplate().load(entityClass, id);
		return (T) obj;
	}
	
	
	/**
	 * 获取全部对象集合.
	 * @param entityClass 需要查询的实体
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final List<T> getAll(Class<T> entityClass) {
		List<T> list = getHibernateTemplate().loadAll(entityClass);
		return list;
	}
	
	
	/****************************增删改操作*********************************/
	/**
	 * 修改或新增一个对象
	 * @param obj
	 * @data 2011-7-14
	 */
	public final T save(T o) {
//		logger.debug("HibernateBaseDao:[save]");
		getHibernateTemplate().saveOrUpdate(o);
		return o;
	}
	
	
	/**
	 * 批量添加或更新
	 * @param list
	 * @data 2011-7-14
	 */
	public final void saveMutiple(List<T> list) {
//		logger.debug("HibernateBaseDao:[saveMutiple]");
		getHibernateTemplate().saveOrUpdateAll(list);
	}
	
	/**
	 * 删除一个对象
	 * @param o
	 * @data 2011-7-14
	 */
	public final void delete(T o) {
//		logger.debug("HibernateBaseDao:[delete]");
		getHibernateTemplate().delete(o);
	}
	
	/**
	 * 根据ID删除对象.
	 */
	public final void deleteById(Class<T> entityClass, Serializable id) {
		delete(this.getSingle(entityClass, id));
	}


	/**
	 * 批量删除对象
	 * @param list 对象列表
	 * @data 2011-7-14
	 */
	public final void deleteMutiple(List<T> list) {
//		logger.debug("HibernateBaseDao:[deleteMutiple]");
		getHibernateTemplate().deleteAll(list);
	}
	
	
	/**
	 * 刷新hibernate缓存
	 */
	public void flush() {
		getHibernateTemplate().flush();
	}

	/**
	 * 清除缓存,可以是session中的对象状态变为游离态
	 */
	public void clear() {
		getHibernateTemplate().clear();
	}
	
	/****************************************基于hql的操作end****************************************************/
	
	
	
	/****************************************基于sql的操作start**************************************************/
	
	/*************************查询返回结果集***********************/
	/**
	 * 直接用sql进行查询的语句
	 * @param sql
	 * @param paramMap 需要传递的参数,参数支持new Object[]{"1","2"},
	 *                 如果通过id批量查询可以在hql或sql中写 id in (:ids)其中ids必须是一个object数组,如果是字符串则不行
	 * @param comment  注释
	 * @return
	 * @data 2011-7-14
	 */
	@SuppressWarnings("unchecked")
	public final List queryBySql(String sql,Map<String, Object> paramMap,String comment){
//		logger.debug("HibernateBaseDao:[queryBySql]");
		Query q = this.getSession(false).createSQLQuery(sql).setTimeout(timeout);
		if(comment!=null){//添加注释
			q.setComment(comment);
		}
		String[] keys = q.getNamedParameters();
		if (keys == null || paramMap == null) {
			List list = q.list();
			return list;
		}
		for (String key : keys) {
			Object obj = paramMap.get(key);
			if(obj!=null && obj.getClass().isArray()){//如果是数组
				q.setParameterList(key, (Object[])obj);
			}else{//如果不是数组则走单个的setParam
				q.setParameter(key, obj);
			}
		}
		
		List list = q.setFetchSize(fetchSize).list();
		return list;
	}
	
	
	
	/**
	 * 直接用sql进行查询的语句,可以转换为泛型对象 具体写法如: this.queryBySql(hql, map,ElnUser.class, "查询列表");
	 * @param sql
	 * @param paramMap 需要传递的参数,参数支持new Object[]{"1","2"},
	 *                 如果通过id批量查询可以在hql或sql中写 id in (:ids)其中ids必须是一个object数组,如果是字符串则不行
	 * @param comment  注释
	 * @return
	 * @data 2011-7-14
	 */
	@SuppressWarnings("unchecked")
	public final List<T> queryBySql(String sql,Map<String, Object> paramMap,Class<T> c ,String comment){
//		logger.debug("HibernateBaseDao:[queryBySql]");
		Query q = this.getSession(false).createSQLQuery(sql).addEntity(c).setTimeout(timeout);
		if(comment!=null){//添加注释
			q.setComment(comment);
		}
		String[] keys = q.getNamedParameters();
		if (keys == null || paramMap == null) {
			List<T> list = q.list();
			return list;
		}
		for (String key : keys) {
			Object obj = paramMap.get(key);
			if(obj!=null && obj.getClass().isArray()){//如果是数组
				q.setParameterList(key, (Object[])obj);
			}else{//如果不是数组则走单个的setParam
				q.setParameter(key, obj);
			}
		}
		
		List<T> list = q.setFetchSize(fetchSize).list();
		return list;
	}
	
	
	/**
	 * 分页查询,通过原生sql,用于复杂条件查询情况下.
	 * @param sql
	 * @param pageBean
	 * @param paramMap 需要传递的参数,参数支持new Object[]{"1","2"},
	 *                 如果通过id批量查询可以在hql或sql中写 id in (:ids)其中ids必须是一个object数组,如果是字符串则不行
	 * @param comment
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final PageBean queryPageBySql(String sql,PageBean pageBean,Map<String, Object> paramMap,String comment) {
//		logger.debug("HibernateBaseDao:[queryPageByHql:PageBean]");
		if (pageBean != null) {
			SQLQuery q = this.getSession(false).createSQLQuery(sql);
			if(comment!=null){//添加注释
				q.setComment(comment);
			}
			q.setFirstResult((pageBean.getPageNow()-1)*pageBean.getPageSize());
			q.setMaxResults(pageBean.getPageSize());
			String[] keys = q.getNamedParameters();
			if (keys != null && paramMap!=null) {
				for (String key : keys) {
					Object obj = paramMap.get(key);
					if(obj!=null && obj.getClass().isArray()){//如果是数组
						q.setParameterList(key, (Object[])obj);
					}else{//如果不是数组则走单个的setParam
						q.setParameter(key, obj);
					}
				}
			}
			pageBean.setResult(q.list());
			
			//查询结果集数量
			String countSql = " select count(*) " + removeSelect(removeOrders(sql));
			int totalCount = Integer.parseInt(this.uniqueQueryBySql(countSql, paramMap,null).toString()) ;
			pageBean.setTotalCount(totalCount);
			pageBean.setRecordCount(totalCount);
			return pageBean;
		}else{
			PageBean pb = new PageBean();
			List list = this.queryBySql(sql, paramMap, comment);
			pb.setResult(list);
			return pb;
		}
	}
	
	
	
	/**
	 * 分页查询,通过原生sql,返回的结果集是hibernate的映射对象,而不是object对象
	 * @param sql
	 * @param pageBean
	 * @param paramMap 需要传递的参数,参数支持new Object[]{"1","2"},
	 *                 如果通过id批量查询可以在hql或sql中写 id in (:ids)其中ids必须是一个object数组,如果是字符串则不行
	 * @param comment
	 * @param T  可以转换为对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final PageBean<T> queryPageBySql(String sql,PageBean<T> pageBean,Map<String, Object> paramMap,Class<T> c,String comment) {
//		logger.debug("HibernateBaseDao:[queryPageByHql:PageBean<T>]");
		if (pageBean != null) {
			SQLQuery q = this.getSession(false).createSQLQuery(sql).addEntity(c);
			if(comment!=null){//添加注释
				q.setComment(comment);
			}
			q.setFirstResult((pageBean.getPageNow()-1)*pageBean.getPageSize());
			q.setMaxResults(pageBean.getPageSize());
			String[] keys = q.getNamedParameters();
			if (keys != null && paramMap!=null) {
				for (String key : keys) {
					Object obj = paramMap.get(key);
					if(obj!=null && obj.getClass().isArray()){//如果是数组
						q.setParameterList(key, (Object[])obj);
					}else{//如果不是数组则走单个的setParam
						q.setParameter(key, obj);
					}
				}
			}
			pageBean.setResult(q.list());
			
			//查询结果集数量
			String countSql = " select count(*) " + removeSelect(removeOrders(sql));
			int totalCount = Integer.parseInt(this.uniqueQueryBySql(countSql, paramMap,null).toString()) ;
			pageBean.setTotalCount(totalCount);
			pageBean.setRecordCount(totalCount);
			return pageBean;
		}else{
			PageBean<T> pb = new PageBean<T>();
			List<T> list = this.queryBySql(sql, paramMap, comment);
			pb.setResult(list);
			return pb;
		}
	}
	
	
	
	
	
	/**
	 * 直接用sql进行查询并返回单个Object对象
	 * @param sql
	 * @param paramMap 需要传递的参数,参数支持new Object[]{"1","2"},
	 *                 如果通过id批量查询可以在hql或sql中写 id in (:ids)其中ids必须是一个object数组,如果是字符串则不行
	 * @param comment  注释
	 * @return
	 * @data 2011-7-14
	 */
	@SuppressWarnings("unchecked")
	public final Object uniqueQueryBySql(String sql,Map<String, Object> paramMap,String comment){
//		logger.debug("HibernateBaseDao:[queryBySql]");
		List list = null;
		Query q = this.getSession(false).createSQLQuery(sql).setTimeout(timeout);
		if(comment!=null){//添加注释
			q.setComment(comment);
		}
		String[] keys = q.getNamedParameters();
		if (keys == null || paramMap == null) {
			list = q.list();
		}else{
			for (String key : keys) {
				Object obj = paramMap.get(key);
				if(obj!=null && obj.getClass().isArray()){//如果是数组
					q.setParameterList(key, (Object[])obj);
				}else{//如果不是数组则走单个的setParam
					q.setParameter(key, obj);
				}
			}
			list = q.setFetchSize(fetchSize).list();
		}
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	
	/**
	 * 直接用sql进行查询并返回单个泛型对象
	 * @param sql
	 * @param paramMap 需要传递的参数,参数支持new Object[]{"1","2"},
	 *                 如果通过id批量查询可以在hql或sql中写 id in (:ids)其中ids必须是一个object数组,如果是字符串则不行
	 * @param comment  注释
	 * @return
	 * @data 2011-7-14
	 */
	@SuppressWarnings("unchecked")
	public final T uniqueQueryBySql(String sql,Map<String, Object> paramMap,Class<T> c , String comment){
//		logger.debug("HibernateBaseDao:[queryBySql]");
		List<T> list = null;
		Query q = this.getSession(false).createSQLQuery(sql).addEntity(c).setTimeout(timeout);
		if(comment!=null){//添加注释
			q.setComment(comment);
		}
		String[] keys = q.getNamedParameters();
		if (keys == null || paramMap == null) {
			list = q.list();
		}else{
			for (String key : keys) {
				Object obj = paramMap.get(key);
				if(obj!=null && obj.getClass().isArray()){//如果是数组
					q.setParameterList(key, (Object[])obj);
				}else{//如果不是数组则走单个的setParam
					q.setParameter(key, obj);
				}
			}
			list = q.setFetchSize(fetchSize).list();
		}
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	
	
	/**
	 * 用sql进行增删改操作
	 * @param sql
	 * @param paramMap 需要传递的参数,参数支持new Object[]{"1","2"},
	 *                 如果通过id批量查询可以在hql或sql中写 id in (:ids)其中ids必须是一个object数组,如果是字符串则不行
	 * @return 受影响的行数
	 * @data 2011-7-14
	 */
	public final int excuteBySql(String sql,Map<String, Object> paramMap) {
//		logger.debug("HibernateBaseDao:[excuteBySql]");
//		Transaction   tx   =   session.beginTransaction(); 
		Query q=this.getSession(false).createSQLQuery(sql).setTimeout(timeout);
		String[] keys = q.getNamedParameters();
		if (keys != null && paramMap != null) {
			for (String key : keys) {
				Object obj = paramMap.get(key);
				if(obj!=null && obj.getClass().isArray()){//如果是数组
					q.setParameterList(key, (Object[])obj);
				}else{//如果不是数组则走单个的setParam
					q.setParameter(key, obj);
				}
			}
		}
		int operateNum = q.executeUpdate();
//		tx.commit();
		return operateNum;
	}
	/****************************************基于sql的操作end**************************************************/
	
	
	/**
	 * 去除hql的select 子句，未考虑union的情况,用于pagedQuery.
	 * 
	 * @see #pagedQuery(String, int, int, Object[])
	 */
	private static String removeSelect(String hql) {
		Assert.hasText(hql);
		int beginPos = hql.toLowerCase().indexOf("from");
		Assert.isTrue(beginPos != -1, " hql : " + hql
				+ " must has a keyword 'from'");
		return hql.substring(beginPos);
	}

	/**
	 * 去除hql的orderby 子句，用于pagedQuery.
	 * 
	 * @see #pagedQuery(String, int, int, Object[])
	 */
	private static String removeOrders(String hql) {
		Assert.hasText(hql);
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer(); 
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();   
	}

}
