package com.yougou.eagleye.core.dao.hibernate3;

import java.sql.Types;

import org.hibernate.dialect.MySQLDialect;

/**
 * mysql操作text类型时需要注册该类型,通过该类注册
 * @describe 在spring配置文件中需要做如下操作
 * <property name="hibernateProperties">
 * <props>
 *      <prop key="hibernate.show_sql">false</prop>
 *       <prop key="hibernate.dialect">com.yougou.eagleye.core.dao.MySqlDialectExtra</prop>
 *   </props>
 * </property>
 * @author panda
 *
 */
public class MySqlDialectExtra extends MySQLDialect{

	 public MySqlDialectExtra() {
	        super();
	        registerHibernateType(Types.LONGVARCHAR, "text");
	    }
}
