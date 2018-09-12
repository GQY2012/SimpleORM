package cn.gqy2012.sorm.core;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sun.corba.se.impl.ior.NewObjectKeyTemplateBase;

import cn.gqy2012.sorm.bean.ColumnInfo;
import cn.gqy2012.sorm.bean.TableInfo;
import cn.gqy2012.sorm.utils.JDBCUtils;
import cn.gqy2012.sorm.utils.ReflectUtils;

/**
 * 负责查询
 * @author gqy2012
 *
 */
@SuppressWarnings("all")
public abstract class Query implements Cloneable{
	/**
	 * Execute a DML statement directly
	 * @param sql 查询语句
	 * @param params sql语句的参数
	 * @return 执行后影响的表的行数
	 */
	public int executeDML(String sql, Object[] params) {
		Connection conn = DBManager.getConn();
		int count = 0;
		
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(sql);
			//给sql设参
			JDBCUtils.handleParams(ps, params);
			
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBManager.close(null, ps, conn);
		}
		
		return count;
	}
	/**
	 * Insert an object into database
	 * 只插入不为null的属性，数字为null置为0
	 * @param obj 要插入的对象
	 */
	public void insert(Object obj) {
		//obj-->表中  insert into 表名 (id,name,pwd) values(?,?,?)
		Class c = obj.getClass();
		//存储参数
		List<Object> params = new ArrayList<>();
		TableInfo tableInfo = TableContext.poClassTableMap.get(c);
		StringBuilder sql = new StringBuilder("insert into "+tableInfo.getTname()+"(");
		
		Field[] fs = c.getDeclaredFields();
		for(Field f:fs) {
			String fieldName = f.getName();
			Object fieldValue = ReflectUtils.invokeGet(fieldName, obj);
			//只插入不为null的值
			if(fieldValue != null) {
				sql.append(fieldName+",");
				params.add(fieldValue);
			}
		}
		
		sql.setCharAt(sql.length()-1, ')');
		sql.append(" values (");
		for(int i = 0;i < params.size();i++) {
			sql.append("?,");
		}
		sql.setCharAt(sql.length()-1,')');
		
		executeDML(sql.toString(), params.toArray());
		
	}
	/**
	 * 删除clazz表示类对应表中的记录
	 * @param clazz	表对应类的Class对象
	 * @param id 主键的值
	 */
	public void delete(Class clazz, Object id) {
		//Emp.class,2-->delete from Emp where id=2;
		//通过Class对象找TableInfo
		TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
		//主键
		ColumnInfo Prikey = tableInfo.getPriKey();
		
		String sql = "delete from "+tableInfo.getTname()
			+" where "+Prikey.getName()+"=?";
		
		executeDML(sql, new Object[] {id});
	}
	/**
	 * 删除对象在数据库中对应的记录
	 * @param obj
	 */
	public void delete(Object obj) {
		Class c = obj.getClass();
		//通过Class对象找TableInfo
		TableInfo tableinfo = TableContext.poClassTableMap.get(c);
		//主键
		ColumnInfo Prikey = tableinfo.getPriKey();
		
		//通过反射机制，调用属性对应的get、set方法
		Object PriKeyValue = ReflectUtils.invokeGet(Prikey.getName(), obj);
		delete(c,PriKeyValue);
	}
	/**
	 * 更新对象对应的记录，并且只能只更新指定的字段的值
	 * @param obj 要更新的对象
	 * @param fieldNames 更新的属性列表
	 * @return 执行后影响的表的行数
	 */
	public int update(Object obj, String[] fieldNames) {
		//obj{"uname","pwd"}-->update 表名 set uname = ?,pwd = ? where id = ?
		Class c = obj.getClass();
		//存储参数
		List<Object> params = new ArrayList<>();
		TableInfo tableInfo = TableContext.poClassTableMap.get(c);
		ColumnInfo priKey = tableInfo.getPriKey();
		StringBuilder sql = new StringBuilder("update "+tableInfo.getTname()+" set ");
		
		for(String fieldName:fieldNames) {
			Object fieldvalue = ReflectUtils.invokeGet(fieldName, obj);
			params.add(fieldvalue);
			sql.append(fieldName+"=?,");
		}
		sql.setCharAt(sql.length()-1, ' ');
		sql.append(" where ");
		sql.append(priKey.getName()+" =? ");
		//加入最后的主键
		params.add(ReflectUtils.invokeGet(priKey.getName(), obj));
		
		return executeDML(sql.toString(), params.toArray());
	}
	/**
	 * 查询返回多行记录，并将每行记录封装到Clazz对象指定的类对象中
	 * @param sql 查询语句
	 * @param clazz 封装数据 的Javabean类的Class对象
	 * @param params sql语句的参数
	 * @return
	 */
	public List queryRows(String sql, Class clazz, Object[] params) {
		Connection conn = DBManager.getConn();
		List list = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(sql);
			//给sql设参
			JDBCUtils.handleParams(ps, params);
			System.out.println(ps);
			rs = ps.executeQuery();
			
			ResultSetMetaData metaData = rs.getMetaData();
			//多行
			while(rs.next()) {
				if(list == null) {
					list = new ArrayList<>();
				}
				Object rowObj = null;
				try {//相当于调用JavaBean的无参构造器
					rowObj = clazz.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
				//多列
				for(int i = 0;i <metaData.getColumnCount();i++) {
					//列名
					String columnName = metaData.getColumnLabel(i+1);//从1开始
					//列值
					Object columnValue = rs.getObject(i+1);
					//调用rowObj对象的set方法
					ReflectUtils.invokeSet(rowObj, columnName, columnValue);
				}
				list.add(rowObj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBManager.close(null, ps, conn);
		}
		
		return list;
	}
	/**
	 * 查询返回一行记录，并将每行记录封装到Clazz对象指定的类对象中
	 * @param sql 查询语句
	 * @param clazz 封装数据 的Javabean类的Class对象
	 * @param params sql语句的参数
	 * @return
	 */
	public Object queryUniqueRows(String sql, Class clazz, Object[] params) {
		List list = queryRows(sql, clazz, params);
		return (list!=null || list.size()>0)?list.get(0):null;
	}
	/**
	 * 查询返回一个值(一行一列)，并将每行记录封装到Clazz对象指定的类对象中
	 * @param sql 查询语句
	 * @param clazz 封装数据 的Javabean类的Class对象
	 * @param params sql语句的参数
	 * @return
	 */
	public Object queryValue(String sql, Class clazz, Object[] params) {
		Connection conn = DBManager.getConn();
		Object value = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(sql);
			//给sql设参
			JDBCUtils.handleParams(ps, params);
			System.out.println(ps);
			rs = ps.executeQuery();
			
			//多行
			while(rs.next()) {
				value = rs.getObject(1);
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBManager.close(null, ps, conn);
		}
		
		return value;
	}
	/**
	 * 查询返回一个数字(一行一列)，并将每行记录封装到Clazz对象指定的类对象中
	 * @param sql 查询语句
	 * @param clazz 封装数据 的Javabean类的Class对象
	 * @param params sql语句的参数
	 * @return
	 */
	public Number queryNumber(String sql, Class clazz, Object[] params) {
		return (Number) queryValue(sql, clazz, params);
	}
	/**
	 * 根据主键的值直接查找对象
	 * @param pageNum
	 * @param size
	 * @return
	 */
	public Object queryById(Class clazz,Object id) {
		TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
		
		ColumnInfo priKeValue = tableInfo.getPriKey();
		
		String sql = "select * from "+tableInfo.getTname()
			+" where "+priKeValue.getName()+" =?";
		
		return queryUniqueRows(sql, clazz, new Object[] {id});
	}
	/**
	 * 分页查询
	 * @param pageNum 页号
	 * @param size 
	 * @return
	 */

	public abstract Object queryPagenate(int pageNum,int size);
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
