package com.king.tooth.sys.entity.sys;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceInfoConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.cfg.CfgTable;

/**
 * 资源信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysResource extends BasicEntity implements ITable, IEntity{
	
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
		
		CfgColumn resourceNameColumn = new CfgColumn("resource_name", DataTypeConstants.STRING, 60);
		resourceNameColumn.setName("资源名");
		resourceNameColumn.setComments("资源名");
		columns.add(resourceNameColumn);
		
		CfgColumn resourceTypeColumn = new CfgColumn("resource_type", DataTypeConstants.INTEGER, 1);
		resourceTypeColumn.setName("资源类型");
		resourceTypeColumn.setComments("资源类型");
		columns.add(resourceTypeColumn);
		
		CfgColumn isEnabledColumn = new CfgColumn("is_enabled", DataTypeConstants.INTEGER, 1);
		isEnabledColumn.setName("是否有效");
		isEnabledColumn.setComments("默认值为1");
		isEnabledColumn.setDefaultValue("1");
		columns.add(isEnabledColumn);
		
		CfgColumn requestMethodColumn = new CfgColumn("request_method", DataTypeConstants.STRING, 30);
		requestMethodColumn.setName("请求资源的方法");
		requestMethodColumn.setComments("默认值：all，get/put/post/delete/all/none，多个可用,隔开；all表示支持全部，none标识都不支持");
		requestMethodColumn.setDefaultValue("all");
		columns.add(requestMethodColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("资源信息表");
		table.setComments("资源信息表");
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "SYS_RESOURCE";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysResource";
	}
	
	/**
	 * 是否是内置资源
	 * @return
	 */
	public boolean isBuiltinResource(){
		return BUILTIN_RESOURCE.equals(refResourceId);
	}
	/**
	 * 内置资源标识
	 * <p>系统内置资源的refResourceId的值为内置资源标识</p>
	 */
	private static final String BUILTIN_RESOURCE = "builtinResource";

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
	 * 获取资源描述
	 * @return
	 */
	public String getResourceTypeDesc() {
		if(ResourceInfoConstants.TABLE == resourceType){
			return "表资源";
		}else if(ResourceInfoConstants.SQL == resourceType){
			return "SQL资源";
		}else if(ResourceInfoConstants.CODE == resourceType){
			return "代码资源";
		}
		return resourceType+"资源";
	}
}
