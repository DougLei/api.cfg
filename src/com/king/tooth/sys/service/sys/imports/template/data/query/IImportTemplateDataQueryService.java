package com.king.tooth.sys.service.sys.imports.template.data.query;

import java.util.List;

import com.king.tooth.sys.entity.cfg.propextend.query.data.param.QueryPropExtendConfDataCondition;

/**
 * 导入模版文件中的数据查询
 * @author DougLei
 */
public interface IImportTemplateDataQueryService {

	/**
	 * 查询数据列表
	 * @param rows
	 * @param pageNo
	 * @param conditions
	 * @return
	 */
	public List<Object[]> queryDataList(String rows, String pageNo, List<QueryPropExtendConfDataCondition> conditions);
}
