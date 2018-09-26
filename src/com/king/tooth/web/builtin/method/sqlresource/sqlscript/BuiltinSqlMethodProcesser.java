package com.king.tooth.web.builtin.method.sqlresource.sqlscript;

import com.king.tooth.sys.entity.cfg.ComSqlScript;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.web.builtin.method.BuiltinMethodProcesserType;
import com.king.tooth.web.builtin.method.sqlresource.AbstractSqlResourceBuiltinMethodProcesser;

/**
 * 内置sql脚本处理器
 * @author DougLei
 */
public class BuiltinSqlMethodProcesser extends AbstractSqlResourceBuiltinMethodProcesser{

	/**
	 * sql脚本资源
	 */
	private ComSqlScript sqlScriptResource;

	public BuiltinSqlMethodProcesser(ComSqlScript reqSqlScriptResource) {
		super.isUsed = true;
		this.sqlScriptResource = reqSqlScriptResource;
	}

	public BuiltinSqlMethodProcesser() {
		Log4jUtil.debug("此次请求，没有使用到BuiltinSqlScriptMethodProcesser内置方法处理器");
	}
	
	protected void execAnalysisParam() {
		sqlScriptResource.analysisFinalSqlScript(sqlScriptResource, sqlParameterValues);
	}

	/**
	 * 获取sql脚本资源
	 * @return
	 */
	public ComSqlScript getSqlScriptResource() {
		execAnalysisParams();
		return sqlScriptResource;
	}

	public int getProcesserType() {
		return BuiltinMethodProcesserType.SQL_SCRIPT;
	}

	public StringBuilder getSql() {
		execAnalysisParams();
		return sql;
	}
	
	public void clearInvalidMemory() {
		if(sqlScriptResource != null){
			sqlScriptResource.clear();
		}
	}
}
