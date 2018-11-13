package com.king.tooth.web.entity.request.valid.data.util.entity;

import java.io.Serializable;
import java.util.List;

import com.king.tooth.sys.entity.cfg.CfgSql;
import com.king.tooth.sys.entity.cfg.CfgSqlParameter;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.prop.code.rule.PropCodeRuleUtil;
import com.king.tooth.web.entity.request.ResourcePropCodeRule;

/**
 * sql参数的set实际值
 * @author DougLei
 */
@SuppressWarnings("serial")
public class SqlParamSetActualValueEntity implements Serializable{

	/**
	 * 获取简单的sql参数值
	 * <p>目前就是对值加上''</p>
	 * @param ssp
	 * @param sqlParameterValue
	 * @return
	 */
	protected String getSimpleSqlParameterValue(CfgSqlParameter ssp, Object sqlParameterValue){
		if(sqlParameterValue == null){
			Log4jUtil.warn(CfgSqlParameter.class, "getSimpleSqlParameterValue", "在获取简单的sql参数值时，传入的sqlParameterValue参数值为null【目前就是对值加上''】");
			return "''";
		}
		return ssp.getValuePackStart()+sqlParameterValue.toString()+ssp.getValuePackEnd();
	}
	
	// ------------------------------------------------------------------------
	/**
	 * 给sql对象中的参数，赋予最终的编码值
	 * @param sql
	 * @param resourcePropCodeRule
	 */
	public void setFinalCodeVals(CfgSql sql, ResourcePropCodeRule resourcePropCodeRule){
		List<List<CfgSqlParameter>> sqlParamsList = sql.getSqlParamsList();
		if(sqlParamsList != null && sqlParamsList.size() > 0){
			Object finalCodeVal = null;
			for (List<CfgSqlParameter> sqlParams : sqlParamsList) {
				if(sqlParams != null && sqlParams.size() > 0){
					for (CfgSqlParameter ssp : sqlParams) {
						if(ssp.getValueFrom() == CfgSqlParameter.AUTO_CODE){
							finalCodeVal = PropCodeRuleUtil.getSqlResourceFinalCodeVal(ssp.getName(), codeValIndex++, resourcePropCodeRule);
							if(finalCodeVal == null){
								throw new NullPointerException("操作sql资源["+sql.getResourceName()+"]时，获取自动编码参数["+ssp.getName()+"]的值为空，请联系后端系统开发人员");
							}
							if(ssp.getIsPlaceholder() == 0){
								finalCodeVal = getSimpleSqlParameterValue(ssp, finalCodeVal);
							}
							ssp.setActualInValue(finalCodeVal);
						}
					}
					codeValIndex = 0;
				}
			}
		}
	}
	/** 标识自动编码的下标 */
	private int codeValIndex;
}
