package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.cfg.busi.model.resource.data.BusiModelResourceData;
import com.king.tooth.sys.entity.tools.resource.metadatainfo.ResourceMetadataInfo;
import com.king.tooth.sys.service.cfg.CfgSqlService;
import com.king.tooth.sys.service.cfg.CfgTableService;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.web.entity.request.valid.data.util.SqlResourceValidUtil;
import com.king.tooth.web.entity.request.valid.data.util.TableResourceValidUtil;

/**
 * 业务模型资源关系表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class CfgBusiModelResRelations extends BasicEntity implements IEntityPropAnalysis, IEntity{
	
	/**
	 * 关联的业务模型id
	 */
	private String refBusiModelId;
	/**
	 * 父id
	 * <p>形成树表结构</p>
	 */
	private String parentId;
	/**
	 * 关联的资源id
	 * <p>CfgTable或CfgSql资源的id</p>
	 */
	private String refResourceId;
	/**
	 * 关联资源的主键属性名
	 * <p>默认值为Id，后端取数据主键的时候，根据这个配置值从json对象中取</p>
	 */
	private String refResourceIdPropName;
	/**
	 * 关联的资源类型
	 * <p>1.CfgTable、2.CfgSql</p>
	 */
	private Integer refResourceType;
	
	/**
	 * 主资源中关联子资源的key名
	 * <p>默认值为children，这个是在主资源对象中，用指定的key值存储子资源对象数组</p>
	 */
	private String refSubResourceKeyName;
	/**
	 * 子资源中关联父资源的属性id
	 * <p>指定子资源的哪个属性，存储父资源的id值</p>
	 */
	private String refParentResourcePropId;
	
	/**
	 * 是否级联删除
	 * <p>默认值为0，即删除主表数据时，是否级联删除子表的数据</p>
	 * <p>该字段目前只针对表资源有效</p>
	 */
	private Integer isCascadeDelete;
	/**
	 * 排序值
	 */
	private Integer orderCode;
	
	
	//-------------------------------------------------------------------------
	/**
	 * 业务模型子资源关系集合
	 */
	@JSONField(serialize = false)
	private List<CfgBusiModelResRelations> subBusiModelResRelationsList;
	
	/**
	 * 业务模型的资源数据集合
	 * <p>存储实际传入的数据对象集合</p>
	 */
	@JSONField(serialize = false)
	private List<BusiModelResourceData> resourceDataList;
	
	/**
	 * 子资源中关联父资源的属性名
	 * <p>根据refParentResourcePropId获得</p>
	 */
	@JSONField(serialize = false)
	private String refParentResourcePropName;
	
	public String getParentId() {
		return parentId;
	}
	public String getRefBusiModelId() {
		return refBusiModelId;
	}
	public void setRefBusiModelId(String refBusiModelId) {
		this.refBusiModelId = refBusiModelId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getRefResourceId() {
		return refResourceId;
	}
	public void setRefResourceId(String refResourceId) {
		this.refResourceId = refResourceId;
	}
	public String getRefResourceIdPropName() {
		return refResourceIdPropName;
	}
	public void setRefResourceIdPropName(String refResourceIdPropName) {
		this.refResourceIdPropName = refResourceIdPropName;
	}
	public Integer getRefResourceType() {
		return refResourceType;
	}
	public void setRefResourceType(Integer refResourceType) {
		this.refResourceType = refResourceType;
	}
	public Integer getIsCascadeDelete() {
		return isCascadeDelete;
	}
	public void setIsCascadeDelete(Integer isCascadeDelete) {
		this.isCascadeDelete = isCascadeDelete;
	}
	public Integer getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(Integer orderCode) {
		this.orderCode = orderCode;
	}
	public String getRefSubResourceKeyName() {
		return refSubResourceKeyName;
	}
	public void setRefSubResourceKeyName(String refSubResourceKeyName) {
		this.refSubResourceKeyName = refSubResourceKeyName;
	}
	public void setSubBusiModelResRelationsList(List<CfgBusiModelResRelations> subBusiModelResRelationsList) {
		this.subBusiModelResRelationsList = subBusiModelResRelationsList;
	}
	public String getRefParentResourcePropId() {
		return refParentResourcePropId;
	}
	public void setRefParentResourcePropId(String refParentResourcePropId) {
		this.refParentResourcePropId = refParentResourcePropId;
	}
	public List<CfgBusiModelResRelations> getSubBusiModelResRelationsList() {
		return subBusiModelResRelationsList;
	}
	public List<BusiModelResourceData> getResourceDataList() {
		return resourceDataList;
	}
	
	
	/**
	 * 是否有业务模型的子资源关系集合
	 * @return
	 */
	public boolean haveSubBusiModelResRelationsList(){
		return subBusiModelResRelationsList!=null && subBusiModelResRelationsList.size()>0;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(8+7);
		
		CfgColumn refBusiModelIdColumn = new CfgColumn("ref_busi_model_id", DataTypeConstants.STRING, 32);
		refBusiModelIdColumn.setName("关联的业务模型id");
		refBusiModelIdColumn.setComments("关联的业务模型id");
		columns.add(refBusiModelIdColumn);
		
		CfgColumn parentIdColumn = new CfgColumn("parent_id", DataTypeConstants.STRING, 32);
		parentIdColumn.setName("父id");
		parentIdColumn.setComments("父id");
		columns.add(parentIdColumn);
		
		CfgColumn refResourceIdColumn = new CfgColumn("ref_resource_id", DataTypeConstants.STRING, 32);
		refResourceIdColumn.setName("关联的资源id");
		refResourceIdColumn.setComments("CfgTable或CfgSql资源的id");
		columns.add(refResourceIdColumn);
		
		CfgColumn refResourceIdPropNameColumn = new CfgColumn("ref_resource_id_prop_name", DataTypeConstants.STRING, 20);
		refResourceIdPropNameColumn.setName("关联资源的主键属性名");
		refResourceIdPropNameColumn.setComments("默认值为Id，后端取数据主键的时候，根据这个配置值从json对象中取");
		refResourceIdPropNameColumn.setDefaultValue(ResourcePropNameConstants.ID);
		columns.add(refResourceIdPropNameColumn);
		
		CfgColumn refResourceTypeColumn = new CfgColumn("ref_resource_type", DataTypeConstants.INTEGER, 1);
		refResourceTypeColumn.setName("关联的资源类型");
		refResourceTypeColumn.setComments("1.CfgTable、2.CfgSql");
		columns.add(refResourceTypeColumn);
		
		CfgColumn refSubResourceKeyNameColumn = new CfgColumn("ref_sub_resource_key_name", DataTypeConstants.STRING, 60);
		refSubResourceKeyNameColumn.setName("主资源中关联子资源的key名");
		refSubResourceKeyNameColumn.setComments("默认值为children，这个是在主资源对象中，用指定的key值存储子资源对象数组");
		refSubResourceKeyNameColumn.setDefaultValue("children");
		columns.add(refSubResourceKeyNameColumn);
		
		CfgColumn refParentResourcePropIdColumn = new CfgColumn("ref_parent_resource_prop_id", DataTypeConstants.STRING, 32);
		refParentResourcePropIdColumn.setName("子资源中关联父资源的属性id");
		refParentResourcePropIdColumn.setComments("指定子资源的哪个属性，存储父资源的id值");
		columns.add(refParentResourcePropIdColumn);
		
		CfgColumn isCascadeDeleteColumn = new CfgColumn("is_cascade_delete", DataTypeConstants.INTEGER, 1);
		isCascadeDeleteColumn.setName("是否级联删除");
		isCascadeDeleteColumn.setComments("默认值为0，即删除主表数据时，是否级联删除子表的数据，如果不级联删除，则有约束，有子表数据，则不能删除主表数据，该字段目前只针对表资源有效");
		isCascadeDeleteColumn.setDefaultValue("0");
		columns.add(isCascadeDeleteColumn);
		
		CfgColumn orderCodeColumn = new CfgColumn("order_code", DataTypeConstants.INTEGER, 3);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		columns.add(orderCodeColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("业务模型资源关系表");
		table.setRemark("业务模型资源关系表");
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "CFG_BUSI_MODEL_RES_RELATIONS";
	}
	
	@JSONField(serialize = false)
	public String getEntityName() {
		return "CfgBusiModelResRelations";
	}
	
	public String validNotNullProps() {
		if(StrUtils.isEmpty(refBusiModelId)){
			return "关联的业务模型id(refBusiModelId)值不能为空";
		}
		if(StrUtils.isEmpty(refResourceId)){
			return "关联的资源id(refResourceId)值不能为空";
		}
		if(refResourceType == null || (refResourceType != REF_RESOURCE_TYPE_CFG_TABLE && refResourceType != REF_RESOURCE_TYPE_CFG_SQL)){
			return "关联的资源类型(refResourceType)值不能为空，且值目前只能为[1.CfgTable]或[2.CfgSql]";
		}
		if(StrUtils.notEmpty(parentId) && StrUtils.isEmpty(refParentResourcePropId)){
			return "子资源中关联父资源的属性id(refParentResourcePropId)值不能为空";
		}
		if(orderCode == null || orderCode < 1){
			return "排序值不能为空，且必须大于0！";
		}
		return null;
	}
	
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
		}
		return result;
	}
	
	// --------------------------------------------------------
	/**
	 * 关联的资源类型
	 * <p>1.CfgTable</p>
	 */
	public static final Integer REF_RESOURCE_TYPE_CFG_TABLE = 1;
	/**
	 * 关联的资源类型
	 * <p>2.CfgSql</p>
	 */
	public static final Integer REF_RESOURCE_TYPE_CFG_SQL = 2;
	
	// --------------------------------------------------------
	private void setRefParentResourcePropName(boolean isQueryResource) {
		if(this.refParentResourcePropName == null){
			Object refParentResourcePropName = null;
			if(refResourceType == REF_RESOURCE_TYPE_CFG_TABLE){
				refParentResourcePropName = HibernateUtil.executeUniqueQueryByHqlArr(queryColumnPropNameHql, refParentResourcePropId);
				if(StrUtils.isEmpty(refParentResourcePropName)){
					throw new NullPointerException("在处理业务资源时，关系资源名为["+getRefResourceName()+"]，没有查询到其中有id=["+refParentResourcePropId+"]的列信息");
				}
			}else if(refResourceType == REF_RESOURCE_TYPE_CFG_SQL){
				if(isQueryResource){
					refParentResourcePropName = HibernateUtil.executeUniqueQueryByHqlArr(querySqlResultsetPropNameHql, refParentResourcePropId);
					if(StrUtils.isEmpty(refParentResourcePropName)){
						throw new NullPointerException("在处理业务资源时，关系资源名为["+getRefResourceName()+"]，没有查询到其中有id=["+refParentResourcePropId+"]的sql结果集信息");
					}
				}else{
					refParentResourcePropName = HibernateUtil.executeUniqueQueryByHqlArr(querySqlParamPropNameHql, refParentResourcePropId);
					if(StrUtils.isEmpty(refParentResourcePropName)){
						throw new NullPointerException("在处理业务资源时，关系资源名为["+getRefResourceName()+"]，没有查询到其中有id=["+refParentResourcePropId+"]的sql参数信息");
					}
				}
			}else{
				throw new IllegalArgumentException("关联的资源类型[refResourceType]值目前只能为[1.CfgTable]或[2.CfgSql]");
			}
			this.refParentResourcePropName = refParentResourcePropName.toString();
		}
	}
	private static final String queryColumnPropNameHql = "select propName from CfgColumn where " + ResourcePropNameConstants.ID+"=?";
	private static final String querySqlParamPropNameHql = "select name from CfgSqlParameter where " + ResourcePropNameConstants.ID+"=?";
	private static final String querySqlResultsetPropNameHql = "select propName from CfgSqlResultset where " + ResourcePropNameConstants.ID+"=?";
	
	public String getRefParentResourcePropName(boolean isQueryResource) {
		setRefParentResourcePropName(isQueryResource);
		return refParentResourcePropName;
	}
	
	/**
	 * 添加业务模型资源关系的子关系
	 * @param busiModelResRelations
	 */
	public void addSubBusiModelResRelations(CfgBusiModelResRelations busiModelResRelations) {
		if(subBusiModelResRelationsList == null){
			subBusiModelResRelationsList = new ArrayList<CfgBusiModelResRelations>();
		}
		subBusiModelResRelationsList.add(busiModelResRelations);
	}
	
	/**
	 * 关联的表资源对象
	 */
	@JSONField(serialize = false)
	private CfgTable refTable;
	
	/**
	 * 关联的sql资源对象
	 * <p>每组sql参数，都解析成一个cfgsql对象，他们共享验证的元数据信息</p>
	 */
	@JSONField(serialize = false)
	private List<CfgSql> refSqlList;
	
	/** 资源的元数据信息集合 */
	@JSONField(serialize = false)
	private List<ResourceMetadataInfo> resourceMetadataInfos;
	
	/** sql资源的输入结果集元数据信息集合 */
	@JSONField(serialize = false)
	private List<List<ResourceMetadataInfo>> inSqlResultSetMetadataInfoList;
	
	/** sql资源的参数集合 */
	@JSONField(serialize = false)
	private List<CfgSqlParameter> sqlParams;
	
	public List<ResourceMetadataInfo> getResourceMetadataInfos() {
		return resourceMetadataInfos;
	}
	public List<List<ResourceMetadataInfo>> getInSqlResultSetMetadataInfoList() {
		return inSqlResultSetMetadataInfoList;
	}
	
	/**
	 * 设置引用的资源对象
	 * @return
	 */
	@JSONField(serialize = false)
	private Object setRefResource() {
		if(refResourceType == REF_RESOURCE_TYPE_CFG_TABLE){
			if(refTable == null){
				refTable = BuiltinResourceInstance.getInstance("CfgTableService", CfgTableService.class).findTableResourceById(refResourceId);
			}
			return refTable;
		}else if(refResourceType == REF_RESOURCE_TYPE_CFG_SQL){
			CfgSql refSql = null;
			if(refSqlList == null){
				refSqlList = new ArrayList<CfgSql>();
				refSql = BuiltinResourceInstance.getInstance("CfgSqlService", CfgSqlService.class).findSqlScriptResourceById(refResourceId);
				refSqlList.add(refSql);
			}
			return refSql;
		}
		throw new IllegalArgumentException("关联的资源类型[refResourceType]值目前只能为[1.CfgTable]或[2.CfgSql]");
	}
	/**
	 * 获取引用的资源名
	 * @return
	 */
	@JSONField(serialize = false)
	public String getRefResourceName() {
		setRefResource();
		if(refResourceType == REF_RESOURCE_TYPE_CFG_TABLE){
			return refTable.getResourceName();
		}else{
			return refSqlList.get(0).getResourceName();
		}
	}
	
	public String validResourceData(BusiModelResourceData resourceData) {
		if(resourceDataList == null || resourceDataList.size() == 0){
			resourceDataList = new ArrayList<BusiModelResourceData>();
		}
		resourceDataList.add(resourceData);
		return resourceData.doBusiResourceDataValid(this);
	}
	
	public CfgTable getRefTable() {
		setRefResource();
		if(refTable !=null){
			if(resourceMetadataInfos == null){
				resourceMetadataInfos = TableResourceValidUtil.getTableResourceMetadataInfos(refTable.getResourceName());
			}
			return refTable;
		}
		return null;
	}
	
	/**
	 * 获取引用的sql对象去验证
	 * @return
	 */
	@JSONField(serialize = false)
	public CfgSql getRefSql() {
		setRefResource();
		return refSqlList.get(0);
	}
	
	/**
	 * 获取引用的sql对象去验证
	 * @return
	 */
	@JSONField(serialize = false)
	public CfgSql getRefSqlForValid() {
		setRefResource();
		CfgSql refSql = null;
		if(isValidFirstSql){
			isValidFirstSql = false;
			refSql = refSqlList.get(0);
			if(refSql.isSelectSql()){// 查询语句就不做任何处理
				return refSql;
			}
			
			if(refSql != null && !refSql.getIncludeAllInfo()){
				BuiltinResourceInstance.getInstance("CfgSqlService", CfgSqlService.class).setSqlScriptResourceAllInfo(refSql);
			}
			
			if(refSql.getSqlParams() != null && refSql.getSqlParams().size()>0){
				sqlParams = new ArrayList<CfgSqlParameter>(refSql.getSqlParams().size());
				sqlParams.addAll(refSql.getSqlParams());
			}
			resourceMetadataInfos = SqlResourceValidUtil.getSqlResourceParamsMetadataInfos(refSql);
			inSqlResultSetMetadataInfoList = SqlResourceValidUtil.getSqlInResultSetMetadataInfoList(refSql);
		}else{
			CfgSql basicSql = refSqlList.get(0);
			if(basicSql.isSelectSql()){// 查询语句就不做任何处理
				refSql = basicSql;
			}else{
				if(basicSql.getSqlParams() == null || basicSql.getSqlParams().size() == 0){
					basicSql.setSqlParams(sqlParams);
				}
				try {
					refSql = (CfgSql) basicSql.clone();
				} catch (CloneNotSupportedException e) {
					throw new IllegalArgumentException("克隆CfgSql对象时出现异常："+ExceptionUtil.getErrMsg(e));
				}
			}
			refSqlList.add(refSql);
		}
		return refSql;
	}
	/** 用来标识，是否验证refSqlList的第一个sql对象，后续再做验证，就新创建一个sql对象去做数据验证，并将该对象添加到refSqlList集合中 */
	private boolean isValidFirstSql = true;
	
	/**
	 * 获取引用的sql对象去执行
	 * @return
	 */
	@JSONField(serialize = false)
	public CfgSql getRefSqlForExecute() {
		CfgSql refSql = null;
		if(!(refSql = refSqlList.get(0)).isSelectSql()){// 查询语句就不做任何处理
			refSql = refSqlList.get(sqlForExecuteIndex++);
		}
		return refSql;
	}
	/** 执行sql时，标识依次取refSqlList中的sql去操作 */
	private int sqlForExecuteIndex;
	
	/**
	 * 进行业务数据操作
	 */
	public List<Object> doOperBusiDataList(Object[] pids){
		if(resourceDataList != null && resourceDataList.size()>0){
			List<Object> resultDatasList = null;
			if(pids == null){
				resultDatasList = new ArrayList<Object>(resourceDataList.size());
				for(int i=0;i<resourceDataList.size();i++){
					resultDatasList.add(resourceDataList.get(i).doOperBusiData(null));
					resourceDataList.get(i).clear();
				}
			}else{
				resultDatasList = new ArrayList<Object>(resourceDataList.size() + pids.length);
				for(int i=0;i<resourceDataList.size();i++){
					for(Object pid: pids){
						resultDatasList.add(resourceDataList.get(i).doOperBusiData(pid));
					}
					resourceDataList.get(i).clear();
				}
			}
			return resultDatasList;
		}
		return null;
	}
	
	/**
	 * 清空验证用的元数据信息
	 */
	public void clearValidMetadataDatas(){
		if(resourceMetadataInfos != null && resourceMetadataInfos.size() > 0){
			resourceMetadataInfos.clear();
		}
		if(inSqlResultSetMetadataInfoList != null && inSqlResultSetMetadataInfoList.size() > 0){
			for (List<ResourceMetadataInfo> inSqlResultSetMetadataInfos : inSqlResultSetMetadataInfoList) {
				if(inSqlResultSetMetadataInfos != null && inSqlResultSetMetadataInfos.size() > 0){
					inSqlResultSetMetadataInfos.clear();
				}
			}
			inSqlResultSetMetadataInfoList.clear();
		}
	}
	
	/**
	 * 在最后所有操作完，清除无用的数据信息
	 */
	public void clear(){
		if(sqlParams != null && sqlParams.size()>0){
			sqlParams.clear();
		}
		if(refSqlList != null && refSqlList.size()>0){
			refSqlList.clear();
		}
	}
}
