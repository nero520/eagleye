package com.yougou.eagleye.core.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

/**
 * <PRE>
 * 作用:
 * 		 反射的Utils函数集合
 *       提供访问私有变量,获取泛型类型Class,提取集合中元素的属性等Utils函数.
 * 限制:
 *       无.
 * 注意事项:
 *       无.
 * 修改历史:
 * -----------------------------------------------------------------------------
 *         VERSION       DATE                BY              CHANGE/COMMENT
 * -----------------------------------------------------------------------------
 *          1.0        2011-07-14           null              create
 * -----------------------------------------------------------------------------
 * </PRE>
 */
public class ReflectionUtils {

	private static Logger logger = Logger.getLogger(ReflectionUtils.class);

	/**
	 * 直接读取对象属性值,无视private/protected修饰符,不经过getter函数.
	 */
	public static Object getFieldValue(final Object object, final String fieldName) {
		Field field = getDeclaredField(object, fieldName);

		if (field == null)
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");

		makeAccessible(field);

		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常:" + e.getMessage());
		}
		return result;
	}

	/**
	 * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.
	 */
	@SuppressWarnings("unchecked")
	public static void setFieldValue(final Object object, final String fieldName, final Object value) {
		if(fieldName.indexOf(".")!=-1){
			String[] names=fieldName.split("\\.");
			Object object2=getFieldValue(object, names[0]);
			if(object2==null){
				Field field2=getDeclaredField(object, names[0]);
				try {
					object2=field2.getType().newInstance();
					setFieldValue(object, names[0], object2);
					
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			setFieldValue(object2, fieldName.substring(fieldName.indexOf(".")+1), value);
			return ;
		}
		Field field = getDeclaredField(object, fieldName);
		if (field == null)
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");

		makeAccessible(field);

		try {
			Class clazz=field.getType();
			if(clazz.isEnum()){
				field.set(object, Enum.valueOf(clazz, value.toString()));
			}else{
				field.set(object, convertValue(value,clazz));
			}
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常:" + e.getMessage());
		}
	}

	/**
	 * 直接调用对象方法,无视private/protected修饰符.
	 */
	public static Object invokeMethod(final Object object, final String methodName, final Class<?>[] parameterTypes,
			final Object[] parameters) throws InvocationTargetException {
		Method method = getDeclaredMethod(object, methodName, parameterTypes);
		if (method == null)
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");

		method.setAccessible(true);

		try {
			return method.invoke(object, parameters);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常:" + e.getMessage());
		}

		return null;
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 */
	protected static Field getDeclaredField(final Object object, final String fieldName) {
		Assert.notNull(object, "object不能为空");
		Assert.hasText(fieldName, "fieldName");
		for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				// Field不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 */
	protected static void makeAccessible(final Field field) {
		if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
			field.setAccessible(true);
		}
	}

	/**
	 * 循环向上转型,获取对象的DeclaredMethod.
	 */
	protected static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes) {
		Assert.notNull(object, "object不能为空");

		for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredMethod(methodName, parameterTypes);
			} catch (NoSuchMethodException e) {
				// Method不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 通过反射,获得Class定义中声明的父类的泛型参数的类型.
	 * eg.
	 * public UserDao extends HibernateDao<User>
	 *
	 * @param clazz The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be determined
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射,获得定义Class时声明的父类的泛型参数的类型.
	 * 
	 * 如public UserDao extends HibernateDao<User,Long>
	 *
	 * @param clazz clazz The class to introspect
	 * @param index the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or Object.class if cannot be determined
	 */

	@SuppressWarnings("unchecked")
	public static Class getSuperClassGenricType(final Class clazz, final int index) {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
					+ params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}

		return (Class) params[index];
	}

	/**
	 * 提取集合中的对象的属性(通过getter函数),组合成List.
	 * 
	 * @param collection 来源集合.
	 * @param propertyName 要提取的属性名.
	 */
	@SuppressWarnings("unchecked")
	public static List fetchElementPropertyToList(final Collection collection, final String propertyName) {
		List list = new ArrayList();

		try {
			for (Object obj : collection) {
				list.add(PropertyUtils.getProperty(obj, propertyName));
			}
		} catch (Exception e) {
			convertToUncheckedException(e);
		}

		return list;
	}

	/**
	 * 提取集合中的对象的属性(通过getter函数),组合成由分割符分隔的字符串.
	 * 
	 * @param collection 来源集合.
	 * @param propertyName 要提取的属性名.
	 * @param separator 分隔符.
	 */
	@SuppressWarnings("unchecked")
	public static String fetchElementPropertyToString(final Collection collection, final String propertyName,
			final String separator) {
		List list = fetchElementPropertyToList(collection, propertyName);
		return StringUtils.join(list, separator);
	}

	/**
	 * 转换字符串类型到clazz的property类型的值.
	 * 
	 * @param value 待转换的字符串
	 * @param clazz 提供类型信息的Class
	 * @param propertyName 提供类型信息的Class的属性.
	 */
	public static Object convertValue(Object value, Class<?> toType) {
		try {
			DateConverter dc = new DateConverter();
			dc.setUseLocaleFormat(true);
			dc.setPatterns(new String[] { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss","yyyyMMddHHmmss" });
			ConvertUtils.register(dc, Date.class);
			ConvertUtils.register(dc, java.sql.Date.class);
			return ConvertUtils.convert(value, toType);
		} catch (Exception e) {
			throw convertToUncheckedException(e);
		}
	}

	/**
	 * 将反射时的checked exception转换为unchecked exception.
	 */
	public static IllegalArgumentException convertToUncheckedException(Exception e) {
		if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
				|| e instanceof NoSuchMethodException)
			return new IllegalArgumentException("Refelction Exception.", e);
		else
			return new IllegalArgumentException(e);
	}
	
	
	/**
	 * 合并对象
	 * @param rawObject 原始对象,即将被修改的对象
	 * @param newObject 原始对象中各属性值都将改为该对象中的属性值(非空)
	 * @return 修改后的原始对象,如果返回null则入参两个对象不是同一个class实例化来的
	 */
	public static Object mergeObject(Object rawObject,Object newObject){
		if(rawObject.getClass().getSimpleName().equals(newObject.getClass().getSimpleName())){//通过同一个class进行实例化
			for(Field field : newObject.getClass().getDeclaredFields()){
				if(!Modifier.isFinal(field.getModifiers())){//判断属性的修饰符是否为Final
					Object fieldValue = ReflectionUtils.getFieldValue(newObject, field.getName());
					if(fieldValue!=null){//需要替换的value不为null
						ReflectionUtils.setFieldValue(rawObject, field.getName(), fieldValue);
					}
				}
			}
		}else{//不是从同一个class实例化
			return null;
		}
		return rawObject;
	}
	
	/**
	 * 根据字段名称获取字段类型带.也可以获取
	 * @param clazz
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	public static  Class<?> getFieldType(Class<?> clazz,String fieldName)throws Exception{
		if(fieldName.indexOf(".")!=-1){
			String[] names=fieldName.split("\\.",2);
			return getFieldType(clazz.getDeclaredField(names[0]).getType(), names[1]);
		}else{
			return clazz.getDeclaredField(fieldName).getType();
		}
	}
	
	public static void main(String[] args)throws Exception{
		
	}
}
