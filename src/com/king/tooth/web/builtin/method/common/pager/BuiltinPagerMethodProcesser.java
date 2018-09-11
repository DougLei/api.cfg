package com.king.tooth.web.builtin.method.common.pager;

import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.web.builtin.method.BuiltinMethodProcesserType;
import com.king.tooth.web.builtin.method.common.AbstractBuiltinCommonMethod;

/**
 * 内置分页函数处理器
 * @author DougLei
 */
public class BuiltinPagerMethodProcesser extends AbstractBuiltinCommonMethod{
	
	/**
	 * 分页查询对象
	 */
	private PageQueryEntity pageQueryEntity;
	
	/**
	 * 是否被解析过
	 */
	private boolean isAnalysis;
	
	/**
	 * 解析分页参数，到分页查询对象
	 */
	private void analysisPageParams() {
		if(isUsed && !isAnalysis){
			pageQueryEntity.execAnalysisPageQueryParams();
			isAnalysis = true;
		}
	}
	
	public BuiltinPagerMethodProcesser(String limit, String start, String rows, String page, String focusedPage) {
		isUsed = true;
		if(focusedPage != null && Integer.valueOf(focusedPage) > 0){
			page = focusedPage;
		}
		pageQueryEntity = new PageQueryEntity(limit, start, rows, page);
	}
	
	public BuiltinPagerMethodProcesser() {
		Log4jUtil.debug("此次请求，没有使用到BuiltinPagerMethodProcesser内置方法处理器");
	}

	public PageQueryEntity getPageQueryEntity() {
		analysisPageParams();
		return pageQueryEntity;
	}
	
	public String getHql() {
		return " select count(" + ResourcePropNameConstants.ID + ") ";
	}

	public String getSql() {
		return "select count(1) ";
	}
	
	public int getProcesserType() {
		return BuiltinMethodProcesserType.PAGER_QUERY;
	}
}
