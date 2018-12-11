package com.king.tooth.web.entity.request.valid.data.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.cfg.CfgResource;
import com.king.tooth.sys.entity.tools.resource.metadatainfo.ResourceMetadataInfo;
import com.king.tooth.sys.entity.tools.resource.metadatainfo.TableResourceMetadataInfo;
import com.king.tooth.sys.service.cfg.CfgResourceService;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.build.model.DynamicBasicColumnUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 表资源验证的工具类
 * @author DougLei
 */
public class TableResourceValidUtil {

	/**
	 * 获取表资源的元数据信息集合
	 * @param tableResourceName
	 * @return
	 */
	public static List<ResourceMetadataInfo> getTableResourceMetadataInfos(String tableResourceName){
		CfgResource resource = BuiltinResourceInstance.getInstance("CfgResourceService", CfgResourceService.class).findResourceByResourceName(tableResourceName);
		return getTableResourceMetadataInfos(resource);
	}
	
	/**
	 * 获取表资源的元数据信息集合
	 * @param resource
	 * @return
	 */
	public static List<ResourceMetadataInfo> getTableResourceMetadataInfos(CfgResource resource){
		List<ResourceMetadataInfo> resourceMetadataInfos = null;
		String resourceId = resource.getRefResourceId();
		String resourceName = resource.getResourceName();
		
		if(resource.isBuiltinResource()){
			resourceMetadataInfos = getBuiltinTableResourceMetadataInfos(resourceName);
		}else{
			resourceMetadataInfos = HibernateUtil.extendExecuteListQueryByHqlArr(ResourceMetadataInfo.class, null, null, queryTableMetadataInfosHql, resourceId);
			if(resourceMetadataInfos == null || resourceMetadataInfos.size() == 0){
				throw new NullPointerException("没有查询到表资源["+resourceName+"]的元数据信息，请检查配置，或联系后台系统开发人员");
			}
			DynamicBasicColumnUtil.initBasicMetadataInfos(0, resourceName, resourceMetadataInfos);
		}
		return resourceMetadataInfos;
	}
	/** 查询表资源元数据信息集合的hql头 */
	private static final String queryTableMetadataInfosHqlHead = "select new map(columnName as columnName,propName as propName,columnType as dataType,length as length,precision as precision,isUnique as isUnique,isNullabled as isNullabled, name as descName, isIgnoreValid as isIgnoreValid) from CfgColumn where tableId=? and operStatus="+CfgColumn.CREATED;
	/** 查询表资源元数据信息集合的hql */
	private static final String queryTableMetadataInfosHql = queryTableMetadataInfosHqlHead + " order by orderCode asc";
	
	/**
	 * 获取内置表资源的元数据信息集合
	 * @param tableResourceName
	 * @return
	 */
	private static List<ResourceMetadataInfo> getBuiltinTableResourceMetadataInfos(String tableResourceName){
		ITable itable = BuiltinResourceInstance.getInstance(tableResourceName, ITable.class);
		List<CfgColumn> columns = itable.getColumnList();
		List<ResourceMetadataInfo> metadataInfos = new ArrayList<ResourceMetadataInfo>(columns.size());
		for (CfgColumn column : columns) {
			metadataInfos.add(new TableResourceMetadataInfo(
					column.getColumnName(),
					column.getColumnType(),
					column.getLength(),
					column.getPrecision(),
					column.getIsUnique(), 
					column.getIsNullabled(),
					column.getIsIgnoreValid(),
					column.getPropName(),
					column.getName()));
		}
		DynamicBasicColumnUtil.initBasicMetadataInfos(1, tableResourceName, metadataInfos);
		columns.clear();
		return metadataInfos;
	}
	
	// --------------------------------------------------------------------------------
	/**
	 * 验证表资源的元数据
	 * @param desc
	 * @param resourceName
	 * @param resourceMetadataInfos
	 * @param ijson
	 * @param isUpdate 是否是修改，如果是修改，则要验证id属性为空
	 * @param isValidUniqueInDb 是否在数据库中验证值唯一(如果不是正常的表类型，比如sqlserver的表类型，则无法去验证唯一性)
	 * @return
	 */
	public static String validTableResourceMetadata(String desc, String resourceName, List<ResourceMetadataInfo> resourceMetadataInfos, IJson ijson, boolean isUpdate, boolean isValidUniqueInDb){
		int size = ijson.size();
		
		Set<ResourceMetadataInfo> uniqueConstraintProps = new HashSet<ResourceMetadataInfo>(resourceMetadataInfos.size());
		JSONObject data = null;
		Object dataIdValue = null;
		boolean dataValueIsNull;
		Set<String> propKeys = null;
		List<String> unExistsPropNames = null;
		Object dataValue = null;
		String validDataIsLegalResult = null;
		
		for(int i=0;i<size;i++){
			data = ijson.get(i);
			dataIdValue = data.get(ResourcePropNameConstants.ID);
			if(isUpdate && StrUtils.isEmpty(dataIdValue)){
				return desc + "第"+(i+1)+"个对象，"+ResourcePropNameConstants.ID+"(主键)属性值不能为空";
			}
			
			unExistsPropNames = new ArrayList<String>(data.size());
			// 验证每个对象的属性，是否存在
			propKeys = data.keySet();
			for (String propName : propKeys) {
				if(validPropUnExists(false, propName, resourceMetadataInfos)){
//					return desc + "第"+(i+1)+"个对象，不存在名为["+propName+"]的属性";
					unExistsPropNames.add(propName);
				}
			}
			if(unExistsPropNames.size() > 0){
				for (String propName : unExistsPropNames) {
					data.remove(propName);
				}
				unExistsPropNames.clear();
			}
			
			for (ResourceMetadataInfo rmi : resourceMetadataInfos) {
				if(rmi.getIsIgnoreValid() == 1){
					continue;
				}
				dataValue = data.get(rmi.getPropName());
				dataValueIsNull = StrUtils.isEmpty(dataValue);
				
				// 验证不能为空
				if(rmi.getIsNullabled() == 0 && dataValueIsNull){
					return desc + "第"+(i+1)+"个对象，["+rmi.getDescName()+"] 的值不能为空";
				}
				
				if(!dataValueIsNull){
					// 两个大字段类型不用检查
					if(DataTypeConstants.CLOB.equals(rmi.getDataType()) || DataTypeConstants.BLOB.equals(rmi.getDataType())){
						continue;
					}
					validDataIsLegalResult = ResourceHandlerUtil.validDataIsLegal(dataValue, rmi);
					if(validDataIsLegalResult != null){
						return desc + "第"+(i+1)+"个对象，" + validDataIsLegalResult;
					}
					
					// 验证唯一约束
					if(!rmi.isUnUnique()){
						uniqueConstraintProps.add(rmi);
						if(isValidUniqueInDb && validDataIsExists(resourceName, rmi, dataValue, isUpdate, dataIdValue)){
							return desc + "第"+(i+1)+"个对象，["+rmi.getDescName()+"] 的值["+dataValue+"]已经存在，不能重复添加";
						}
					}
				}
			}
		}
		
		// 验证一次提交的数组中，是否有重复的值，违反了唯一约束
		if(size > 1 && uniqueConstraintProps.size()>0){
			for (ResourceMetadataInfo uniqueConstraintProp : uniqueConstraintProps) {
				for(int i=0;i<size-1;i++){
					dataValue = ijson.get(i).get(uniqueConstraintProp.getPropName());
					if(StrUtils.notEmpty(dataValue)){
						for(int j=i+1;j<size;j++){
							if(dataValue.equals(ijson.get(j).get(uniqueConstraintProp.getPropName()))){
								return desc + "第"+(i+1)+"个对象和第"+(j+1)+"个对象，["+uniqueConstraintProp.getDescName()+"] 的值重复，操作失败";
							}
						}
					}
				}
			}
		}
		
		if(uniqueConstraintProps.size() > 0){
			uniqueConstraintProps.clear();
		}
		return null;
	}
	
	/**
	 * 验证数据是否已经存在
	 * @param resourceName
	 * @param rmi
	 * @param dataValue
	 * @param isUpdate 是否是修改
	 * @param originDataIdValue 源数据id值，用来在修改的时候判断唯一性，如果操作的是同一个数据，则不用做重复校验
	 * @return
	 */
	private static boolean validDataIsExists(String resourceName, ResourceMetadataInfo rmi, Object dataValue, boolean isUpdate, Object originDataIdValue) {
		Object id = ResourceHandlerUtil.getUniqueDataId(resourceName, rmi, dataValue);
		if(isUpdate){
			// 如果是修改的话，要先判断是否能查询到数据，如果查询不到数据，则证明不存在
			if(id == null){
				return false;
			}
			// 如果查询到数据，再判断查询到的数据id是否和当前操作的数据id一致，如果一致，忽略唯一性验证
			if(id.toString().equals(originDataIdValue.toString())){
				return false;
			}
			// 否则就是数据出现重复
			return true;
		}else{
			// 如果是添加的话，则只要查询到数据，就是已存在
			return (id != null);
		}
	}
	
	// --------------------------------------------------------------------------------
	/**
	 * 验证属性是否不存在
	 * @param validBuiltinParams 是否验证内置参数，是get请求的时候才需要为true，其他请求都是false
	 * @param propName
	 * @param resourceMetadataInfos
	 * @return
	 */
	public static boolean validPropUnExists(boolean validBuiltinParams, String propName, List<ResourceMetadataInfo> resourceMetadataInfos){
		if(propName.startsWith("$") && propName.endsWith("$")){
			return false;
		}
		if(validBuiltinParams){
			for (String builtinParams : BuiltinParameterKeys.BUILTIN_PARAMS) { // 内置的参数不做是否存在的验证，因为肯定不存在，是后台使用的一些参数
				if(propName.equals(builtinParams)){
					return false;
				}
			}
		}
		for (ResourceMetadataInfo rmi : resourceMetadataInfos) {
			if(propName.equals(rmi.getPropName())){
				return false;
			}
		}
		return true;
	}
}
