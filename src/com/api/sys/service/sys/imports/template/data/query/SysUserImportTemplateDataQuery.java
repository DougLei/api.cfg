package com.api.sys.service.sys.imports.template.data.query;

import java.util.List;

import com.api.constants.ResourcePropNameConstants;

/**
 * SysUser资源：导入模版文件中的数据查询
 * @author DougLei
 */
public class SysUserImportTemplateDataQuery extends AImportTemplateDataQueryService{

	protected void installQueryHql(List<Object> conditionValues, String valueColumnPropName, String conditionHql, String orderByColumnPropName, String orderBy){
		if(queryHql == null){
			StringBuilder hql = new StringBuilder();
			hql.append("select p.")
			   .append(valueColumnPropName)
			   .append(", p.realName||'-'||d.name from SysUser p, SysDept d ")
			   .append("where p.deptId=d.").append(ResourcePropNameConstants.ID)
			   .append(" and p.isDelete=0 and p.customerId=?");
			if(conditionHql != null){
				hql.append(" and ").append(conditionHql);
			}
			if(orderByColumnPropName != null){
				hql.append(" order by p.").append(orderByColumnPropName).append(" ").append(orderBy);
			}
			
			queryHql = hql.toString();
			hql.setLength(0);
		}
	}
}
