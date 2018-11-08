package com.king.tooth.web.builtin.method.common.create.exportfile;

import java.util.ArrayList;
import java.util.List;

import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.cfg.CfgResource;
import com.king.tooth.sys.service.cfg.CfgResourceService;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.web.builtin.method.BuiltinMethodProcesserType;
import com.king.tooth.web.builtin.method.common.AbstractBuiltinCommonMethod;

/**
 * 内置创建导出文件的函数处理器
 * <p>可以配合BuiltinPagerMethodProcesser中的，_rows或_limit参数使用，这两个中的任意一个参数指定一次导出的数据数量，提高系统性能</p>
 * <p>_rows或_limit参数的搭配参数(_page或_start)可以随便传值，但是必须传值，建议传值都为0即可</p>
 * @author DougLei
 */
public class BuiltinCreateExportFileMethodProcesser extends AbstractBuiltinCommonMethod{
	
	/**
	 * 请求的资源名
	 */
	private String resourceName;
	private String parentResourceName;
	/**
	 * 是否创建导出文件
	 */
	private boolean isCreateExport;
	/**
	 * 导出文件的后缀
	 */
	private String exportFileSuffix;
	/**
	 * 导出文件中的标题
	 * <p>如果为空，则和资源名一样</p>
	 */
	private String exportTitle;
	/**
	 * 要导出的基础字段属性名，多个用,隔开
	 * <p>值包括：Id,customerId,projectId,createDate,lastUpdateDate,createUserId,lastUpdateUserId</p>
	 */
	private String exportBasicPropNames;
	
	/**
	 * 要生成导出文件的资源对象
	 */
	private CfgResource resource;
	
	public BuiltinCreateExportFileMethodProcesser() {
		Log4jUtil.debug("此次请求，没有使用到BuiltinCreateExportFileMethodProcesser内置方法处理器");
	}
	public BuiltinCreateExportFileMethodProcesser(String resourceName, String parentResourceName, String isCreateExport, String exportFileSuffix, String exportTitle, String exportBasicPropNames) {
		if(!"true".equals(isCreateExport)){
			Log4jUtil.debug("此次请求，没有使用到BuiltinCreateExportFileMethodProcesser内置方法处理器");
			return;
		}
		this.isUsed = true;
		this.resourceName = resourceName;
		this.parentResourceName = parentResourceName;
		this.isCreateExport = true;
		this.exportFileSuffix = exportFileSuffix;
		this.exportBasicPropNames = exportBasicPropNames;
		
		if(StrUtils.isEmpty(exportTitle)){
			exportTitle = resourceName;
		}
		this.exportTitle = exportTitle;
	}

	/**
	 * 获取生成导出文件时，查询数据的属性名，多个用,隔开
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getExportSelectPropNames() {
		if(isUsed){
			resource = BuiltinResourceInstance.getInstance("CfgResourceService", CfgResourceService.class).findResourceByResourceName(resourceName);
			List<String> propNameList = null;
			
			if(resource.isBuiltinResource()){
				ITable itable = BuiltinResourceInstance.getInstance(resource.getResourceName(), ITable.class);
				List<CfgColumn> columns = itable.getColumnList();
				propNameList = new ArrayList<String>(columns.size());
				for (CfgColumn column : columns) {
					if(column.getIsExport() == 1){
						propNameList.add(column.getPropName());
					}
				}
				columns.clear();
			}else{
				String hql;
				if(resource.isTableResource()){
					hql = queryTableExportPropNamesHql;
				}else if(resource.isSqlResource()){
					hql = querySqlExportPropNamesHql;
				}else{
					throw new IllegalArgumentException("系统目前只支持[表资源/sql资源]的数据导出");
				}
				propNameList = HibernateUtil.executeListQueryByHqlArr(null, null, hql, resource.getRefResourceId());
			}
			if(propNameList == null || propNameList.size() == 0){
				throw new NullPointerException("没有查询到名为["+resourceName+"]资源的导出属性名信息集合，请检查配置，或联系后端系统开发人员");
			}
			
			StringBuilder propNameBuilder = new StringBuilder();
			for (Object propName : propNameList) {
				propNameBuilder.append(propName).append(",");
			}
			propNameList.clear();
			
			// 如果是导出表资源数据，则判断是否传入了要导出的基础字段
			if(resource.isTableResource() && StrUtils.notEmpty(exportBasicPropNames)){
				propNameBuilder.append(exportBasicPropNames);
			}else{
				propNameBuilder.setLength(propNameBuilder.length()-1);
			}
			return propNameBuilder.toString();
		}
		return null;
	}
	/** 查询表资源，要导出的属性名集合hql */
	private static final String queryTableExportPropNamesHql = "select propName from CfgColumn where tableId=? and operStatus="+CfgColumn.CREATED + " and isExport=1 order by exportOrderCode asc";
	/** 查询sql资源，要导出的属性名集合hql */
	private static final String querySqlExportPropNamesHql = "select propName from CfgSqlResultset where sqlScriptId=? and isExport=1 order by exportOrderCode asc";
	
	public int getProcesserType() {
		return BuiltinMethodProcesserType.EXPORT_FILE;
	}
	
	public boolean getIsCreateExport() {
		return isCreateExport;
	}
	public String getExportFileSuffix() {
		return exportFileSuffix;
	}
	public String getResourceName() {
		return resourceName;
	}
	public String getParentResourceName() {
		return parentResourceName;
	}
	public CfgResource getResource() {
		return resource;
	}
	public String getExportTitle() {
		return exportTitle;
	}
	public String getExportBasicPropNames() {
		return exportBasicPropNames;
	}
}
