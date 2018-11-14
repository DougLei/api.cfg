package com.king.tooth.sys.service.cfg;

import com.king.tooth.annotation.Service;
import com.king.tooth.sys.entity.cfg.CfgBusiModelResRelations;
import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.cfg.CfgSql;
import com.king.tooth.sys.entity.cfg.CfgSqlParameter;
import com.king.tooth.sys.entity.cfg.CfgTable;
import com.king.tooth.sys.service.AService;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

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
	private String validRefDataIsValid(CfgBusiModelResRelations busiModelResRelations){
		if(busiModelResRelations.getRefResourceType() == CfgBusiModelResRelations.REF_RESOURCE_TYPE_CFG_TABLE){
			try {
				CfgTable table = getObjectById(busiModelResRelations.getRefResourceId(), CfgTable.class);
				if(table.getIsBuildModel() == 0){
					return "业务模型资源关系中，关联的表资源["+table.getName()+"]还未建模";
				}
			} catch (NullPointerException e) {
				return "业务模型资源关系中，不存在id为["+busiModelResRelations.getRefResourceId()+"]的表资源";
			}
			try {
				CfgColumn column = getObjectById(busiModelResRelations.getRefParentResourcePropId(), CfgColumn.class);
				if(column.getOperStatus() != CfgColumn.CREATED){
					return "业务模型资源关系中，关联父资源的属性列["+column.getName()+"]为未创建状态，无法实现关联";
				}
			} catch (NullPointerException e) {
				return "业务模型资源关系中，不存在id为["+busiModelResRelations.getRefParentResourcePropId()+"]的列属性";
			}
		}else if(busiModelResRelations.getRefResourceType() == CfgBusiModelResRelations.REF_RESOURCE_TYPE_CFG_SQL){
			try {
				getObjectById(busiModelResRelations.getRefResourceId(), CfgSql.class);
			} catch (NullPointerException e) {
				return "业务模型资源关系中，不存在id为["+busiModelResRelations.getRefResourceId()+"]的sql资源";
			}
			try {
				getObjectById(busiModelResRelations.getRefParentResourcePropId(), CfgSqlParameter.class);
			} catch (NullPointerException e) {
				return "业务模型资源关系中，不存在id为["+busiModelResRelations.getRefParentResourcePropId()+"]的sql参数属性";
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
