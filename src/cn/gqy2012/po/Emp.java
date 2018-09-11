package cn.gqy2012.po;

import java.sql.*;
import java.util.*;

public class Emp{

	private String empname;
	private java.sql.Date birthday;
	private Integer deptid;
	private Integer id;
	private Double salary;
	private Integer age;
	public void setEmpname(String empname){
		this.empname = empname;
	}
	public void setBirthday(java.sql.Date birthday){
		this.birthday = birthday;
	}
	public void setDeptid(Integer deptid){
		this.deptid = deptid;
	}
	public void setId(Integer id){
		this.id = id;
	}
	public void setSalary(Double salary){
		this.salary = salary;
	}
	public void setAge(Integer age){
		this.age = age;
	}
	public String getEmpname(){
		return empname;
	}
	public java.sql.Date getBirthday(){
		return birthday;
	}
	public Integer getDeptid(){
		return deptid;
	}
	public Integer getId(){
		return id;
	}
	public Double getSalary(){
		return salary;
	}
	public Integer getAge(){
		return age;
	}
}
