package cn.gqy2012.sorm.vo;
/**
 * 对于复杂查询，直接封装一个JavaBean类来存储查询结果
 * @author gqy2012
 *
 */
public class EmpVO {
	private Integer id;
	private String empname;
	private Integer age;
	private String deptName;
	private String deptAddr;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEmpname() {
		return empname;
	}
	public void setEmpname(String empname) {
		this.empname = empname;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDeptAddress() {
		return deptAddr;
	}
	public void setDeptAddress(String deptAddress) {
		this.deptAddr = deptAddress;
	}
	
	public String getDeptAddr() {
		return deptAddr;
	}
	public void setDeptAddr(String deptAddr) {
		this.deptAddr = deptAddr;
	}
	public EmpVO() {
	}
	public EmpVO(Integer id, String empname, Integer age, String deptName, String deptAddr) {
		this.id = id;
		this.empname = empname;
		this.age = age;
		this.deptName = deptName;
		this.deptAddr = deptAddr;
	}
	
}
