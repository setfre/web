package psn.pojo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pagination<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer currentIndex;	//当前页下标
	
	private Integer recordShowSize;	//页面显示最大数量
	
	private Integer pageSize;		//页数
	
	private Integer dbRecordSize;	//数据库记录总数量
	
	private String message;			//分页情况消息
	
	private List<T> bean;			//分页显示的数据
	
	private Map<String, Object> range = new HashMap<String, Object>();

	public Pagination(){}

	public List<T> getBean() {
		return bean;
	}

	public void setBean(List<T> bean) {
		this.bean = bean;
	}

	public Integer getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(Integer currentIndex) {
		this.currentIndex = currentIndex<1?1:currentIndex>pageSize?pageSize:currentIndex;
	}

	public Integer getRecordShowSize() {
		return recordShowSize;
	}

	public void setRecordShowSize(Integer recordShowSize) {
		this.recordShowSize = recordShowSize;
		int t1 = dbRecordSize % this.recordShowSize;//21 % 5 = 1;
		int t2 = dbRecordSize / this.recordShowSize;//21 / 5 = 4;
		this.pageSize = t1==0?t2:t2+1;
	}

 
	public Integer getPageSize() {
		return pageSize;
	}
	/*
	 * public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
	 */
	public Integer getDbRecordSize() {
		return dbRecordSize;
	}
	
	public void setDbRecordSize(Integer dbRecordSize) {
		this.dbRecordSize = dbRecordSize;
	}

	public Map<String, Object> getRange() {
		System.out.println("currentIndex:"+currentIndex);
		System.out.println("recordShowSize:"+recordShowSize);
		System.out.println("stat:"+(currentIndex - 1) * recordShowSize);
		range.put("start", (currentIndex - 1) * recordShowSize);//3*** (3 - 1) * 5 = 10
		range.put("end", currentIndex * recordShowSize);		//		3 * 5 = 15
		System.out.println(range);
		return range;
	}
	/*
	 * public void setIndex(Map<String, Integer> range) { this.range = range; }
	 */

	@Override
	public String toString() {
		return "Pagination [currentIndex=" + currentIndex + ", recordShowSize=" + recordShowSize + ", pageSize="
				+ pageSize + ", dbRecordSize=" + dbRecordSize + ", message=" + message + ", bean=" + bean + ", range="
				+ range + "]";
	}
	
}
