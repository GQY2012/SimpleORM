package cn.gqy2012.sorm.core;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.gqy2012.po.Emp;
import cn.gqy2012.sorm.bean.ColumnInfo;
import cn.gqy2012.sorm.bean.TableInfo;
import cn.gqy2012.sorm.utils.JDBCUtils;
import cn.gqy2012.sorm.utils.ReflectUtils;
/**
 * 负责对Mysql数据库的查询
 * @author gqy2012
 *
 */
public class MySqlQuery implements Query{

	public static void main(String[] args) {
		Emp e = new Emp();
		e.setId(1);
		e.setEmpname("gqy2012");
		e.setBirthday(new Date(System.currentTimeMillis()));
		e.setAge(33);
		e.setSalary(100.00);
		
		
		//new MySqlQuery().delete(e);
		//new MySqlQuery().insert(e);
		//new MySqlQuery().update(e, new String[] {"empname","age","salary"});
		Object e1 = new MySqlQuery().queryValue("select id from emp where age=? and salary <?"
				, Emp.class, new Object[] {22,5000});
		System.out.println(((Integer) e1));
	}
	
	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
	public Object queryUniqueRows(String sql, Class clazz, Object[] params) {
		List list = queryRows(sql, clazz, params);
		return (list==null&&list.size()>0)?null:list.get(0);
	}

	@Override
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

	@Override
	public Number queryNumber(String sql, Class clazz, Object[] params) {
		return (Number) queryValue(sql, clazz, params);
	}

}
