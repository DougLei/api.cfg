package com.king.tooth.web.entity.resulttype;

import java.io.Serializable;
import java.util.List;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 分页结果实体类
 * @author DougLei
 */
@SuppressWarnings("serial")
public class PageResultEntity implements Serializable {
	
	/**
	 * 该属性后面指定资源标识
	 */
	private String[] focusedId;
	
	/**
	 * 一页显示的数量
	 */
	private int pageSize;
	
	/**
	 * 当前第一条数据的位置
	 * 计数是从0开始
	 */
	@JSONField(name = "pos")
	private int firstDataIndex;
	
	/**
	 * 数据总数量
	 */
	@JSONField(name = "total")
	private long totalCount;
	
	/**
	 * 当前页数
	 * 即第几页
	 */
	@JSONField(name = "page")
	private int pageNum;
	
	/**
	 * 总页数
	 */
	@JSONField(name = "pageCount")
	private int pageTotalCount;

	/**
	 * 结果集合
	 */
	@JSONField(name = "rows")
	private List<? extends Object> resultDatas;
	
	/**
	 * 是否最后一页
	 * @return
	 */
	public boolean getIsLastPage() {
		return pageNum == pageTotalCount;
	}
	
	/**
	 * 是否数据溢出
	 * <p>即只有两页数据，查询第三页数据的时候，是不应该查询的</p>
	 * <p>这时要将查询的最后一页该为最大页数</p>
	 * @return
	 */
	private boolean overflowData() {
		return pageNum > pageTotalCount;
	}
	
	public String[] getFocusedId() {
		return focusedId;
	}
	public void setFocusedId(String[] focusedId) {
		this.focusedId = focusedId;
	}
	public List<? extends Object> getResultDatas() {
		return resultDatas;
	}
	public void setResultDatas(List<? extends Object> resultDatas) {
		this.resultDatas = resultDatas;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getFirstDataIndex() {
		return firstDataIndex;
	}
	public void setFirstDataIndex(int firstDataIndex) {
		if(overflowData()){
			this.pageNum = pageTotalCount;
			this.firstDataIndex = (pageTotalCount-1)*pageSize;
		}else{
			this.firstDataIndex = firstDataIndex;
		}
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getPageTotalCount() {
		return pageTotalCount;
	}
	public void setPageTotalCount(int pageTotalCount) {
		this.pageTotalCount = pageTotalCount;
	}
}