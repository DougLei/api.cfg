package com.api.web.builtin.method.sqlresource.sqlscript;

import java.util.List;

import com.api.sys.entity.cfg.CfgPropCodeRule;
import com.api.sys.entity.cfg.CfgSql;
import com.api.util.Log4jUtil;
import com.api.web.builtin.method.BuiltinMethodProcesserType;
import com.api.web.builtin.method.sqlresource.AbstractSqlResourceBuiltinMethodProcesser;

/**
 * 内置sql脚本处理器
 * @author DougLei
 */
public class BuiltinSqlMethodProcesser extends AbstractSqlResourceBuiltinMethodProcesser{

	/**
	 * sql脚本资源
	 */
	private CfgSql reqSql;

	public BuiltinSqlMethodProcesser(CfgSql reqSql) {
		super.isUsed = true;
		this.reqSql = reqSql;
	}

	public BuiltinSqlMethodProcesser() {
		Log4jUtil.debug("此次请求，没有使用到BuiltinSqlScriptMethodProcesser内置方法处理器");
	}
	
	protected void execAnalysisParam() {
		reqSql.analysisFinalSqlScript(sqlParameterValues);
	}

	/**
	 * 获取sql脚本资源
	 * <p>非get请求方式</p>
	 * @return
	 */
	public CfgSql getReqSql(List<CfgPropCodeRule> rules) {
		reqSql.setRules(rules);
		return getReqSql();
	}
	
	/**
	 * 获取sql脚本资源
	 * <p>get请求方式</p>
	 * @return
	 */
	public CfgSql getReqSql() {
		execAnalysisParams();
		return reqSql;
	}

	public int getProcesserType() {
		return BuiltinMethodProcesserType.SQL_SCRIPT;
	}

	public StringBuilder getSql() {
		execAnalysisParams();
		return sql;
	}
	
	public void clearInvalidMemory() {
		if(reqSql != null){
			reqSql.clear();
		}
	}
}
