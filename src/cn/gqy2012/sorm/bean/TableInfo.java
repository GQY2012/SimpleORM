package cn.gqy2012.sorm.bean;

import java.util.List;
import java.util.Map;

/**
 * 存储表信息
 * @author gqy2012
 *
 */
public class TableInfo {
	/**
	 * 表名
	 */
	private String tname;
	/**
	 * 所有字段信息
	 */
	private Map<String,ColumnInfo> columns;
	/**
	 * 主键字段(不支持联合主键)
	 */
	private ColumnInfo PriKey;
	
	private List<ColumnInfo> PriKeys;
	
	public List<ColumnInfo> getPrikeys() {
		return PriKeys;
	}
	public void setPrikeys(List<ColumnInfo> PriKeys) {
		this.PriKeys = PriKeys;
	}
	public String getTname() {
		return tname;
	}
	public void setTname(String tname) {
		this.tname = tname;
	}
	public Map<String, ColumnInfo> getColumns() {
		return columns;
	}
	public void setColumns(Map<String, ColumnInfo> columns) {
		this.columns = columns;
	}
	public ColumnInfo getPriKey() {
		return PriKey;
	}
	public void setPriKey(ColumnInfo PriKey) {
		this.PriKey = PriKey;
	}
	public TableInfo(String tname, Map<String, ColumnInfo> columns, ColumnInfo priKey) {
		this.tname = tname;
		this.columns = columns;
		PriKey = priKey;
	}
	public TableInfo(String tname, List<ColumnInfo> PriKeys, Map<String, ColumnInfo> columns) {
		this.tname = tname;
		this.columns = columns;
		this.PriKeys = PriKeys;
	}
	public TableInfo() {
	}
	
}
