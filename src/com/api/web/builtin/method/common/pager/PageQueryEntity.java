package com.api.web.builtin.method.common.pager;

import java.io.Serializable;

import com.api.util.StrUtils;
import com.api.util.datatype.DataTypeValidUtil;

/**
 * 分页查询的参数对象
 * 
 * <p>limit 和   start是一组，简称A组</p>
 * <p>rows 和   page是一组，简称B组</p>
 * <p>其中，A组的优先级高于B组，即若同时配置了A组和B组的请求参数，则优先使用A组的配置信息</p>
 * @author DougLei
 */
@SuppressWarnings("serial")
public class PageQueryEntity implements Serializable{
	
	/**
	 * 限制一次查询的数量
	 * 即一页显示的数量
	 */
	private int limit = -1;
	/**
	 * 开始查询的数据位置
	 * 即从数据库查询到的结果集中，第几个位置下标的数据开始查询
	 * 也即当前第一条数据的位置
	 */
	private int start = -1;
	
	//*********************************************************
	
	/**
	 * 一页显示的数量
	 */
	private int rows = -1;
	/**
	 * 页数，即第几页
	 * 从1开始
	 */
	private int page = -1;
	
	/**
	 * 是否使用limit和start的参数值进行分页查询
	 */
	private boolean useLimitStart;
	
	/**
	 * 是否使用rows和page的参数值进行分页查询
	 */
	private boolean useRowsPage;
	
	
	public PageQueryEntity(String limit, String start, String rows, String page) {
		setLimit(limit);
		setStart(start);
		setPage(page);
		setRows(rows);
	}
	private void setLimit(String limit) {
		if(StrUtils.notEmpty(limit) && DataTypeValidUtil.isInteger(limit)){
			this.limit = Math.abs(Integer.parseInt(limit.trim()));
			if(this.limit == 0){
				this.limit = 1;
			}
		}
	}
	private void setStart(String start) {
		if(StrUtils.notEmpty(start) && DataTypeValidUtil.isInteger(start)){
			this.start = Integer.parseInt(start.trim());
			if(this.start < 0){
				this.start = 0;
			}
		}
	}
	private void setRows(String rows) {
		if(StrUtils.notEmpty(rows) && DataTypeValidUtil.isInteger(rows)){
			this.rows = Math.abs(Integer.parseInt(rows.trim()));
			if(this.rows == 0){
				this.rows = 1;
			}
		}
	}
	private void setPage(String page) {
		if(StrUtils.notEmpty(page) && DataTypeValidUtil.isInteger(page)){
			this.page = Math.abs(Integer.parseInt(page.trim()));
			if(this.page == 0){
				this.page = 1;
			}
		}
	}
	
	/**
	 * 判断否使用limit和start的参数值进行分页查询
	 * @return
	 */
	private void checkUseLimitStart(){
		if(limit > 0 && start >= 0){
			if(start%limit != 0){
				/** @see getPageNum() */
				throw new IllegalArgumentException("分页查询时，_start参数值，必须能整除_limit的参数值");
			}
			this.useLimitStart = true;
		}
	}
	
	/**
	 * 判断是否使用rows和page的参数值进行分页查询
	 * @return
	 */
	private void checkUseRowsPage(){
		// limit和start参数值的优先级高于rows和page，所以，如果useLimitStart=true，则不用在考虑rows和page的参数值
		if(!this.useLimitStart){
			if(rows > 0 && page > 0){
				this.useRowsPage = true;
			}
		}
	}
	
	/**
	 * 开始解析分页查询的参数
	 */
	public void execAnalysisPageQueryParams() {
		checkUseLimitStart();
		checkUseRowsPage();
	}
	
	/**
	 * 获取起始查询的位置  queryStartIndex
	 * 从0开始
	 * <p>hibernate query.setFirstResult(firstResult)</p>
	 * @return
	 */
	public int getFirstResult(){
		return getFirstDataIndex();
	}
	
	/**
	 * 获取查询的数据量 queryDataLength
	 * 即一页显示的数量
	 * <p>hibernate query.setMaxResults(maxResults)</p>
	 * @return
	 */
	public int getMaxResults(){
		return getPageSize();
	}
	
	/**
	 * 获取一页显示的数量
	 */
	public int getPageSize(){
		if(useLimitStart){
			return this.limit;
		}else if(useRowsPage){
			return this.rows;
		}
		return -1;
	}
	
	/**
	 * 获取当前第一条数据的位置
	 * 计数是从0开始
	 */
	public int getFirstDataIndex(){
		if(useLimitStart){
			return this.start;
		}else if(useRowsPage){
			return (this.page-1)*this.rows;
		}
		return -1;
	}
	
	/**
	 * 获取当前页数
	 * 即第几页
	 */
	public int getPageNum(){
		if(useLimitStart){
			return this.start/this.limit + 1;
		}else if(useRowsPage){
			return this.page;
		}
		return -1;
	}

	/**
	 * 获取总页数
	 * @param totalCount 总数量
	 */
	public int getPageTotalCount(long totalCount){
		int pageTotalCount =  -1;
		if(useLimitStart){
			pageTotalCount = calcPageTotalCount(totalCount, limit);
		}else if(useRowsPage){
			pageTotalCount = calcPageTotalCount(totalCount, rows);
		}
		return pageTotalCount;
	}
	/**
	 * 计算总页数
	 * @param totalCount
	 * @param pageSize
	 * @return
	 */
	private int calcPageTotalCount(long totalCount, int pageSize){
		int pageTotalCount = (int)(totalCount/pageSize);
		if((totalCount%pageSize) != 0){
			pageTotalCount++;
		}
		return pageTotalCount;
	}
}