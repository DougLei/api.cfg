package com.api.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.api.annotation.Table;
import com.api.constants.DataTypeConstants;
import com.api.constants.ResourceInfoConstants;
import com.api.sys.builtin.data.BuiltinObjectInstance;
import com.api.sys.entity.BasicEntity;
import com.api.sys.entity.IEntity;

/**
 * 资源信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class CfgResource extends BasicEntity implements IEntity{
	
	/**
	 * 引用的资源主键
	 */
	private String refResourceId;
	/**
	 * 资源类型
	 */
	private Integer resourceType;
	/**
	 * 资源名
	 */
	private String resourceName;
	/**
	 * 是否有效
	 */
	private Integer isEnabled;
	/**
	 * 请求资源的方法
	 * <p>get/put/post/delete/all/none，多个可用,隔开；all表示支持全部，none标识都不支持</p>
	 * <p>默认值：all</p>
	 */
	private String requestMethod;
	
	//-------------------------------------------------------------------------
	
	public CfgResource() {
	}
	public CfgResource(Integer resourceType) {
		this.resourceType = resourceType;
	}
	
	public String getResourceName() {
		return resourceName;
	}
	public String getRefResourceId() {
		return refResourceId;
	}
	public void setRefResourceId(String refResourceId) {
		this.refResourceId = refResourceId;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public Integer getResourceType() {
		return resourceType;
	}
	public void setResourceType(Integer resourceType) {
		this.resourceType = resourceType;
	}
	public Integer getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}
	public String getRequestMethod() {
		return requestMethod;
	}
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(5+7);
		
		CfgColumn refResourceIdColumn = new CfgColumn("ref_resource_id", DataTypeConstants.STRING, 32);
		refResourceIdColumn.setName("引用的资源主键");
		refResourceIdColumn.setComments("引用的资源主键");
		columns.add(refResourceIdColumn);
		
		columns.add(BuiltinObjectInstance.resourceNameColumn);
		
		CfgColumn resourceTypeColumn = new CfgColumn("resource_type", DataTypeConstants.INTEGER, 1);
		resourceTypeColumn.setName("资源类型");
		resourceTypeColumn.setComments("资源类型");
		columns.add(resourceTypeColumn);
		
		columns.add(BuiltinObjectInstance.isEnabledColumn);
		columns.add(BuiltinObjectInstance.requestMethodColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("资源信息表");
		table.setRemark("资源信息表");
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "CFG_RESOURCE";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "CfgResource";
	}
	
	/**
	 * 是否是内置资源
	 * @return
	 */
	public boolean isBuiltinResource(){
		return ResourceInfoConstants.BUILTIN_RESOURCE.equals(refResourceId);
	}

	/**
	 * 是否是表资源
	 * @return
	 */
	public boolean isTableResource(){
		return resourceType == ResourceInfoConstants.TABLE;
	}
	/**
	 * 是否是sql资源
	 * @return
	 */
	public boolean isSqlResource(){
		return resourceType == ResourceInfoConstants.SQL;
	}
	/**
	 * 是否是代码资源
	 * @return
	 */
	public boolean isCodeResource(){
		return resourceType == ResourceInfoConstants.CODE;
	}
	/**
	 * 是否是业务模型资源
	 * @return
	 */
	public boolean isBusinessModelResource(){
		return resourceType == ResourceInfoConstants.BUSINESS_MODEL;
	}
	
	/**
	 * 获取资源描述
	 * @return
	 */
	public String getResourceTypeDesc() {
		if(ResourceInfoConstants.TABLE == resourceType){
			return "[表]资源";
		}else if(ResourceInfoConstants.SQL == resourceType){
			return "[SQL]资源";
		}else if(ResourceInfoConstants.CODE == resourceType){
			return "[代码]资源";
		}else if(ResourceInfoConstants.BUSINESS_MODEL == resourceType){
			return "[业务模型]资源";
		}
		return resourceType+"资源";
	}
}
