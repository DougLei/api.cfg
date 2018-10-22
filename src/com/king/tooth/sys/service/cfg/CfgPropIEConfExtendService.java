package com.king.tooth.sys.service.cfg;

import com.king.tooth.annotation.Service;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.entity.cfg.CfgPropIEConfExtend;
import com.king.tooth.sys.service.AService;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 属性导入导出配置扩展表Service
 * @author DougLei
 */
@Service
public class CfgPropIEConfExtendService extends AService{

	/**
	 * 验证属性导入导出的扩展配置关联的属性是否存在
	 * @param project
	 * @return operResult
	 */
	private String validCfgPropIEConfExtendRefPropIsExists(CfgPropIEConfExtend propIEConfExtend) {
		String hql = null;
		String desc = null;
		if(propIEConfExtend.getRefPropType() == CfgPropIEConfExtend.REF_PROP_TYPE_COLUMN){
			desc = "CfgColumn";
			hql = "select count("+ResourcePropNameConstants.ID+") from CfgColumn where "+ResourcePropNameConstants.ID+"=? ";
		}else if(propIEConfExtend.getRefPropType() == CfgPropIEConfExtend.REF_PROP_TYPE_SQL_RESULTSET){
			desc = "CfgSqlResult";
			hql = "select count("+ResourcePropNameConstants.ID+") from CfgSqlResultset where "+ResourcePropNameConstants.ID+"=? ";
		}
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr(hql, propIEConfExtend.getRefPropId());
		if(count == 0){
			return desc + "资源中，不存在id为["+propIEConfExtend.getRefPropId()+"]的数据";
		}
		return null;
	}
	
	/**
	 * 验证属性导入导出的扩展配置是否存在
	 * @param propIEConfExtend
	 * @return
	 */
	private String validCfgPropIEConfExtendIsExists(CfgPropIEConfExtend propIEConfExtend){
		String hql = "select count("+ResourcePropNameConstants.ID+") from CfgPropIEConfExtend where refPropId=? and confType=?";
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr(hql, propIEConfExtend.getRefPropId(), propIEConfExtend.getConfType());
		if(count > 0){
			return "id为["+propIEConfExtend.getRefPropId()+"]的属性，已经配置了"+(propIEConfExtend.getConfType()==CfgPropIEConfExtend.CONF_TYPE_IMPORT?"导入":"导出")+"的扩展信息";
		}
		return null;
	}
	
	/**
	 * 保存属性导入导出的扩展配置
	 * @param propIEConfExtend
	 * @return
	 */
	public Object savePropIEConfExtend(CfgPropIEConfExtend propIEConfExtend) {
		String operResult = validCfgPropIEConfExtendRefPropIsExists(propIEConfExtend);
		if(operResult == null){
			operResult = validCfgPropIEConfExtendIsExists(propIEConfExtend);
		}
		if(operResult == null){
			return HibernateUtil.saveObject(propIEConfExtend, null);
		}
		return operResult;
	}

	/**
	 * 修改属性导入导出的扩展配置
	 * @param propIEConfExtend
	 * @return
	 */
	public Object updatePropIEConfExtend(CfgPropIEConfExtend propIEConfExtend) {
		String operResult = validCfgPropIEConfExtendRefPropIsExists(propIEConfExtend);
		if(operResult == null){
			CfgPropIEConfExtend oldPropIEConfExtend = getObjectById(propIEConfExtend.getId(), CfgPropIEConfExtend.class);
			if(!oldPropIEConfExtend.getRefPropId().equals(propIEConfExtend.getRefPropId()) 
					|| oldPropIEConfExtend.getRefPropType() != propIEConfExtend.getRefPropType()){
				operResult = validCfgPropIEConfExtendIsExists(propIEConfExtend);
			}
		}
		if(operResult == null){
			return HibernateUtil.updateObject(propIEConfExtend, null);
		}
		return operResult;
	}

	/**
	 * 删除属性导入导出的扩展配置
	 * @param propIEConfExtendIds
	 * @return
	 */
	public String deletePropIEConfExtend(String propIEConfExtendIds) {
		return deleteDataById("CfgPropIEConfExtend", propIEConfExtendIds);
	}
}
