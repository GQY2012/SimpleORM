package cn.gqy2012.sorm.core;

import java.util.List;

/**
 * 负责查询
 * @author gqy2012
 *
 */
@SuppressWarnings("all")
public interface Query {
	/**
	 * Execute a DML statement directly
	 * @param sql 查询语句
	 * @param params sql语句的参数
	 * @return 执行后影响的表的行数
	 */
	public int executeDML(String sql,Object[] params);
	/**
	 * Insert an object into database
	 * 只插入不为null的属性，数字为null置为0
	 * @param obj 要插入的对象
	 */
	public void insert(Object obj);
	/**
	 * 删除clazz表示类对应表中的记录
	 * @param clazz	表对应类的Class对象
	 * @param id 主键的值
	 */
	public void delete(Class clazz,Object id);
	/**
	 * 删除对象在数据库中对应的记录
	 * @param obj
	 */
	public void delete(Object obj);
	/**
	 * 更新对象对应的记录，并且只能只更新指定的字段的值
	 * @param obj 要更新的对象
	 * @param fieldNames 更新的属性列表
	 * @return 执行后影响的表的行数
	 */
	public int update(Object obj,String[] fieldNames);
	/**
	 * 查询返回多行记录，并将每行记录封装到Clazz对象指定的类对象中
	 * @param sql 查询语句
	 * @param clazz 封装数据 的Javabean类的Class对象
	 * @param params sql语句的参数
	 * @return
	 */
	public List queryRows(String sql,Class clazz,Object[] params);
	/**
	 * 查询返回一行记录，并将每行记录封装到Clazz对象指定的类对象中
	 * @param sql 查询语句
	 * @param clazz 封装数据 的Javabean类的Class对象
	 * @param params sql语句的参数
	 * @return
	 */
	public Object queryUniqueRows(String sql,Class clazz,Object[] params);
	/**
	 * 查询返回一个值(一行一列)，并将每行记录封装到Clazz对象指定的类对象中
	 * @param sql 查询语句
	 * @param clazz 封装数据 的Javabean类的Class对象
	 * @param params sql语句的参数
	 * @return
	 */
	public Object queryValue(String sql,Class clazz,Object[] params);
	/**
	 * 查询返回一个数字(一行一列)，并将每行记录封装到Clazz对象指定的类对象中
	 * @param sql 查询语句
	 * @param clazz 封装数据 的Javabean类的Class对象
	 * @param params sql语句的参数
	 * @return
	 */
	public Number queryNumber(String sql,Class clazz,Object[] params);
}
