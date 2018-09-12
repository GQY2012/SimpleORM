package cn.gqy2012.sorm.core;

import java.sql.Date;

import cn.gqy2012.po.Emp;
/**
 * 负责对Mysql数据库的查询
 * @author gqy2012
 *
 */
public class MySqlQuery extends Query{

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
	public Object queryPagenate(int pageNum, int size) {
		// TODO 自动生成的方法存根
		return null;
	}
	
}
