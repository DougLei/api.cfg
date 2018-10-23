package com.king.tooth.sys.entity.tools.resource.metadatainfo.ie;

import com.king.tooth.sys.entity.tools.resource.metadatainfo.ResourceMetadataInfo;

/**
 * 资源导入导出的元数据信息对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class IEResourceMetadataInfo extends ResourceMetadataInfo{

	/** 这三个字段，处理某个列的值，引用某张表中某个字段的值。导入时，key列用来显示值，提交的时候，提交value列的值 */
	/** 关联的表id */
	protected String refTableId;
	/** 关联表中，关联的key列id */
	protected String refKeyColumnId;
	/** 关联表中，关联的value列id */
	protected String refValueColumnId;
	
	/** 这三个字段，同上，用来处理内置表，因为内置表和属性没有id */
	/** 关联的表资源名 */
	protected String refTableResourceName;
	/** 关联表中，关联的key列属性名 */
	protected String refKeyColumnPropName;
	/** 关联表中，关联的value列属性名 */
	protected String refValueColumnPropName;
	
	
	public IEResourceMetadataInfo() {
	}
	public IEResourceMetadataInfo(String columnName, String dataType, Integer length, Integer precision, Integer isUnique, Integer isNullabled, Integer isIgnoreValid) {
		super(columnName, dataType, length, precision, isUnique, isNullabled, isIgnoreValid);
	}
	public IEResourceMetadataInfo(String propName) {
		super(propName);
	}
	
	
	public String getRefTableId() {
		return refTableId;
	}
	public void setRefTableId(String refTableId) {
		this.refTableId = refTableId;
	}
	public String getRefKeyColumnId() {
		return refKeyColumnId;
	}
	public void setRefKeyColumnId(String refKeyColumnId) {
		this.refKeyColumnId = refKeyColumnId;
	}
	public String getRefValueColumnId() {
		return refValueColumnId;
	}
	public void setRefValueColumnId(String refValueColumnId) {
		this.refValueColumnId = refValueColumnId;
	}
	public String getRefTableResourceName() {
		return refTableResourceName;
	}
	public void setRefTableResourceName(String refTableResourceName) {
		this.refTableResourceName = refTableResourceName;
	}
	public String getRefKeyColumnPropName() {
		return refKeyColumnPropName;
	}
	public void setRefKeyColumnPropName(String refKeyColumnPropName) {
		this.refKeyColumnPropName = refKeyColumnPropName;
	}
	public String getRefValueColumnPropName() {
		return refValueColumnPropName;
	}
	public void setRefValueColumnPropName(String refValueColumnPropName) {
		this.refValueColumnPropName = refValueColumnPropName;
	}
}
