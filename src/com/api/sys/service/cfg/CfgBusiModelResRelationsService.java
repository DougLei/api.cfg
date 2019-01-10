package com.api.sys.service.cfg;

import java.util.List;

import com.api.annotation.Service;
import com.api.constants.SqlStatementTypeConstants;
import com.api.sys.entity.cfg.CfgBusiModel;
import com.api.sys.entity.cfg.CfgBusiModelResRelations;
import com.api.sys.entity.cfg.CfgColumn;
import com.api.sys.entity.cfg.CfgSql;
import com.api.sys.entity.cfg.CfgSqlParameter;
import com.api.sys.entity.cfg.CfgSqlResultset;
import com.api.sys.entity.cfg.CfgTable;
import com.api.sys.service.AService;
import com.api.util.StrUtils;
import com.api.util.hibernate.HibernateUtil;

/**
 * 业务模型资源关系表Service
 * @author DougLei
 */
@Service
public class CfgBusiModelResRelationsService extends AService{

	/**
	 * 验证业务模型资源关系中关联的数据是否有效
	 * <p>目前refResourceId、refParentResourcePropId</p>
	 * @param busiModelResRelations
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	private String validRefDataIsValid(CfgBusiModelResRelations busiModelResRelations){
		try {
			getObjectById(busiModelResRelations.getRefBusiModelId(), CfgBusiModel.class);
		} catch (Exception e) {
			return "不存在id为["+busiModelResRelations.getRefBusiModelId()+"]的业务模型资源";
		}
		
		if(busiModelResRelations.getRefResourceType() == CfgBusiModelResRelations.REF_RESOURCE_TYPE_CFG_TABLE){
			try {
				CfgTable table = getObjectById(busiModelResRelations.getRefResourceId(), CfgTable.class);
				if(table.getIsBuildModel() == 0){
					return "业务模型资源关系中，关联的表资源["+table.getName()+"]还未建模";
				}
				if(StrUtils.isEmpty(busiModelResRelations.getRefResourceKeyName())){
					busiModelResRelations.setRefResourceKeyName(table.getResourceName());
				}
			} catch (NullPointerException e) {
				return "业务模型资源关系中，不存在id为["+busiModelResRelations.getRefResourceId()+"]的表资源";
			}
			
			if(StrUtils.notEmpty(busiModelResRelations.getParentId())){
				try {
					CfgColumn column = getObjectById(busiModelResRelations.getRefParentResourcePropId(), CfgColumn.class);
					if(column.getOperStatus() != CfgColumn.CREATED){
						return "业务模型资源关系中，关联父资源的属性列["+column.getName()+"]为未创建状态，无法实现关联";
					}
				} catch (NullPointerException e) {
					return "业务模型资源关系中，不存在关联父资源的属性列，id为["+busiModelResRelations.getRefParentResourcePropId()+"]";
				}
			}
		}else if(busiModelResRelations.getRefResourceType() == CfgBusiModelResRelations.REF_RESOURCE_TYPE_CFG_SQL){
			CfgSql sql = null;
			try {
				sql = getObjectById(busiModelResRelations.getRefResourceId(), CfgSql.class);
				if(StrUtils.isEmpty(busiModelResRelations.getRefResourceKeyName())){
					busiModelResRelations.setRefResourceKeyName(sql.getResourceName());
				}
			} catch (NullPointerException e) {
				return "业务模型资源关系中，不存在id为["+busiModelResRelations.getRefResourceId()+"]的sql资源";
			}
			
			int[] flag = new int[2];// 是select的sql，修改第一个值；非select的sql，修改第二个值
			List<Object> sqlConfTypes = HibernateUtil.executeListQueryBySqlArr("select conf_type from cfg_sql cs left join cfg_busi_model_res_relations cbmrr on (cs.id = cbmrr.ref_resource_id) where cbmrr.ref_busi_model_id = ? and cbmrr.ref_resource_type=2", busiModelResRelations.getRefBusiModelId());
			if(sqlConfTypes != null && sqlConfTypes.size() > 0){
				sqlConfTypes.add(sql.getConfType());
				for (Object sqlConfType : sqlConfTypes) {
					if(SqlStatementTypeConstants.SELECT.equals(sqlConfType)){
						flag[0] = 1;
					}else{
						flag[1] = 2;
					}
				}
				sqlConfTypes.clear();
				if(flag[0]==1 && flag[1]==2){
					throw new IllegalArgumentException("在一个业务模型中，系统目前不支持同时执行[增删改]类型和[查询]类型的sql语句");
				}
			}
			
			if(StrUtils.notEmpty(busiModelResRelations.getParentId())){
				if(sql.isSelectSql()){
					try {
						getObjectById(busiModelResRelations.getRefParentResourcePropId(), CfgSqlResultset.class);
					} catch (NullPointerException e) {
						return "业务模型资源关系中，不存在id为["+busiModelResRelations.getRefParentResourcePropId()+"]的sql结果集属性";
					}
				}else{
					try {
						getObjectById(busiModelResRelations.getRefParentResourcePropId(), CfgSqlParameter.class);
					} catch (NullPointerException e) {
						return "业务模型资源关系中，不存在id为["+busiModelResRelations.getRefParentResourcePropId()+"]的sql参数属性";
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 验证业务模型资源关系是否是递归关系
	 * @param busiModelResRelations
	 * @return 
	 */
	private String validIsRecursiveRelationOfBusiModelResRelations(CfgBusiModelResRelations busiModelResRelations) {
		if(StrUtils.notEmpty(busiModelResRelations.getParentId())){
			if(getObjectById(busiModelResRelations.getParentId(), CfgBusiModelResRelations.class).getRefResourceId().equals(busiModelResRelations.getRefResourceId())){
				return "在创建业务模型资源时，系统目前不支持构建递归层级的关系";
			}
		}
		return null;
	}
	
	/**
	 * 保存业务模型资源关系
	 * @param busiModelResRelations
	 * @return
	 */
	public Object saveBusiModelResRelations(CfgBusiModelResRelations busiModelResRelations) {
		String operResult = validRefDataIsValid(busiModelResRelations);
		if(operResult == null){
			operResult = validIsRecursiveRelationOfBusiModelResRelations(busiModelResRelations);
			if(operResult == null){
				return HibernateUtil.saveObject(busiModelResRelations, null);
			}
		}
		return operResult;
	}

	/**
	 * 修改业务模型资源关系
	 * @param busiModelResRelations
	 * @return
	 */
	public Object updateBusiModelResRelations(CfgBusiModelResRelations busiModelResRelations) {
		getObjectById(busiModelResRelations.getId(), CfgBusiModelResRelations.class);
		String operResult = validRefDataIsValid(busiModelResRelations);
		if(operResult == null){
			operResult = validIsRecursiveRelationOfBusiModelResRelations(busiModelResRelations);
			if(operResult == null){
				return HibernateUtil.updateEntityObject(busiModelResRelations, null);
			}
		}
		return operResult;
	}
	
	/**
	 * 删除业务模型资源关系
	 * @param busiModelResRelationsIds
	 * @return
	 */
	public Object deleteBusiModelResRelations(String busiModelResRelationsIds) {
		return deleteDataById("CfgBusiModelResRelations", busiModelResRelationsIds);
	}
}
