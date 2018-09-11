package cn.gqy2012.po;

import java.sql.*;
import java.util.*;

public class T_user{

	private java.sql.Date regTime;
	private java.sql.Clob myInfo;
	private Integer pwd;
	private String username;
	public void setRegTime(java.sql.Date regTime){
		this.regTime = regTime;
	}
	public void setMyInfo(java.sql.Clob myInfo){
		this.myInfo = myInfo;
	}
	public void setPwd(Integer pwd){
		this.pwd = pwd;
	}
	public void setUsername(String username){
		this.username = username;
	}
	public java.sql.Date getRegTime(){
		return regTime;
	}
	public java.sql.Clob getMyInfo(){
		return myInfo;
	}
	public Integer getPwd(){
		return pwd;
	}
	public String getUsername(){
		return username;
	}
}
