package com.king.tooth.sys.entity.cfg.busi.model.resource.data;

import java.io.Serializable;

import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.entity.cfg.CfgSql;
import com.king.tooth.sys.entity.cfg.CfgTable;
import com.king.tooth.web.entity.request.valid.data.util.SqlResourceValidUtil;
import com.king.tooth.web.entity.request.valid.data.util.TableResourceValidUtil;

/**
 * 业务模型的资源数据
 * <p>和每个业务模型资源关系(CfgBusiModelResRelations)对应</p>
 * <p>@see CfgBusiModelResRelations</p>
 * @author DougLei
 */
@SuppressWarnings("serial")
public class BusiModelResourceData implements Serializable{
	
	/**
	 * 数据的父级id
	 * <p>如果是根，则该值为null</p>
	 */
	private String dataParentId;
	/**
	 * 实际的数据
	 */
	private IJson data;

	// -----------------------------------------------------------
	public BusiModelResourceData() {
	}
	public BusiModelResourceData(Object dataParentId, IJson ijsonData) {
		this.data = ijsonData;
		this.dataParentId = dataParentId.toString();
	}
	
	// -----------------------------------------------------------
	public String getDataParentId() {
		return dataParentId;
	}
	public void setDataParentId(String dataParentId) {
		this.dataParentId = dataParentId;
	}
	public IJson getData() {
		return data;
	}
	public void setData(IJson data) {
		this.data = data;
	}
	
	/**
	 * 进行业务资源数据验证
	 * @param table
	 * @param sql
	 * @return
	 */
	public String doBusiResourceDataValid(CfgTable table, CfgSql sql) {
		if(table != null){
			String tableResourceName = table.getResourceName();
			return TableResourceValidUtil.validTableResourceMetadata("操作表资源["+tableResourceName+"]时，", tableResourceName, TableResourceValidUtil.getTableResourceMetadataInfos(tableResourceName), data, false, true);
		}else if(sql != null){
			return SqlResourceValidUtil.doValidAndSetActualParams(sql, 
						false, 
						SqlResourceValidUtil.initActualParamsList(null, data), 
						SqlResourceValidUtil.getSqlResourceParamsMetadataInfos(sql), 
						SqlResourceValidUtil.getSqlInResultSetMetadataInfoList(sql), 
						requestBody.getResourcePropCodeRule());
		}
		throw new NullPointerException("进行业务资源数据验证时，传入的对象都为空，请联系后端系统开发人员");
	}
}
