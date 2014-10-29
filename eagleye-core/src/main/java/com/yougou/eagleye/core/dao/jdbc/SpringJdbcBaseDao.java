package com.yougou.eagleye.core.dao.jdbc;

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.support.JdbcDaoSupport;


/**
<bean id="transactionManager"
		class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="dataSource" />
	</bean>

    <!--利用了拦截器的原理。-->   
   <bean id="transactionInterceptor"  
         class="org.springframework.transaction.interceptor.TransactionInterceptor">   
        <property name="transactionManager">    
                  <ref bean="transactionManager" />   
        </property>   
    <!-- 配置事务属性 -->
   <property name="transactionAttributes">   
        <props>   
            <prop key="delete*">PROPAGATION_REQUIRED</prop>
            <prop key="operate*">PROPAGATION_REQUIRED,-Exception</prop>   
            <prop key="insert*">PROPAGATION_REQUIRED,-Exception</prop>   
            <prop key="update*">PROPAGATION_REQUIRED,-Exception</prop>   
            <prop key="save*">PROPAGATION_REQUIRED</prop>   
            <prop key="find*">PROPAGATION_REQUIRED,readOnly</prop>   
       </props>   
   </property>   
   </bean>   
   <bean id="txProxy"  
         class="org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator">   
        <property name="beanNames">   
          <list>   
             <value>*Services</value>
          </list>   
        </property>   
        <property name="interceptorNames">   
          <list>   
             <value>transactionInterceptor</value>   
          </list>   
        </property>   
   </bean>  
 * @author liuxiaobei
 * 
 * @param <T>
 */
public class SpringJdbcBaseDao extends JdbcDaoSupport{
	
	
    /**
     * 执行建表
     * @param sql
     */
	public final void execute(final String sql) {
		getJdbcTemplate().execute(sql);
	}

	
	public final List queryForList(final String sql) {
		return getJdbcTemplate().queryForList(sql);
	}
	
	/**
	 * 查询集合
	 * @param sql
	 * @param paramList
	 * @return
	 */
	public final List queryForList(final String sql,List<Object>  paramList) {
		Object[] objs = null;
		if(paramList!=null && paramList.size()>0){
			objs = new Object[paramList.size()];
			for(int i=0;i<paramList.size();i++){
				objs[i] = paramList.get(i);
			}
		}
		return getJdbcTemplate().queryForList(sql,objs);
	}
	
	
	public final List queryForPage(final String sql, final int startIndex, final int lastIndex) {
		StringBuilder pageSQL = new StringBuilder(" SELECT * FROM ( ");
		pageSQL.append(" SELECT temp.* ,ROWNUM num FROM ( ");
		pageSQL.append(sql);
		pageSQL.append("　) temp where ROWNUM <= " + lastIndex);
		pageSQL.append(" ) WHERE　num > " + startIndex);
		return getJdbcTemplate().queryForList(pageSQL.toString());
	}

	public final Object[] queryForArray(final String sql) {
		return (Object[])getJdbcTemplate().queryForList(sql).toArray();
	}
	
	public final Map<String, Object> queryForMap(final String sql) {
		return getJdbcTemplate().queryForMap(sql);
	}
	
	/**
	 * 添加或修改
	 * @param sql
	 * @param objs
	 * @return
	 * @throws Exception
	 */
	public final int update(final String sql, final Object[] objs) throws Exception {
            int effectCount = getJdbcTemplate().update(sql, objs);
            return effectCount;
    }
	
	
	/**
	 * 添加或修改,和删除
	 * @param sql
	 * @param paramList
	 * @return
	 * @throws Exception
	 */
	public final int updateOrSaveOrDel(final String sql, List<Object>  paramList) throws Exception {
		Object[] objs = null;
		if(paramList!=null && paramList.size()>0){
			objs = new Object[paramList.size()];
			for(int i=0;i<paramList.size();i++){
				objs[i] = paramList.get(i);
			}
		}
        int effectCount = getJdbcTemplate().update(sql, objs);
        return effectCount;
	}
	
}
