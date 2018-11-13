package com.king.tooth.sys.service.cfg;

import com.king.tooth.annotation.Service;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.entity.cfg.CfgPropExtendConf;
import com.king.tooth.sys.service.AService;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 属性扩展配置信息表Service
 * @author DougLei
 */
@Service
public class CfgPropExtendConfService extends AService{

	/**
	 * 验证属性的扩展配置关联的属性是否存在
	 * @param propExtendConf
	 * @return 
	 */
	private String validPropExtendConfRefPropIsExists(CfgPropExtendConf propExtendConf) {
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from CfgColumn where "+ResourcePropNameConstants.ID+"=? ", propExtendConf.getRefPropId());
		if(count == 0){
			return "CfgColumn资源中，不存在id为["+propExtendConf.getRefPropId()+"]的数据";
		}
		return null;
	}
	
	/**
	 * 验证属性的扩展配置是否存在
	 * @param propExtendConf
	 * @return
	 */
	private String validPropExtendConfIsExists(CfgPropExtendConf propExtendConf){
		String hql = "select count("+ResourcePropNameConstants.ID+") from CfgPropExtendConf where refPropId=? ";
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr(hql, propExtendConf.getRefPropId());
		if(count > 0){
			return "id为["+propExtendConf.getRefPropId()+"]的属性，已经配置了扩展信息";
		}
		return null;
	}
	
	/**
	 * 保存属性的扩展配置
	 * @param propExtendConf
	 * @return
	 */
	public Object savePropExtendConf(CfgPropExtendConf propExtendConf) {
		String operResult = validPropExtendConfRefPropIsExists(propExtendConf);
		if(operResult == null){
			operResult = validPropExtendConfIsExists(propExtendConf);
		}
		if(operResult == null){
			return HibernateUtil.saveObject(propExtendConf, null);
		}
		return operResult;
	}

	/**
	 * 修改属性的扩展配置
	 * @param propExtendConf
	 * @return
	 */
	public Object updatePropExtendConf(CfgPropExtendConf propExtendConf) {
		String operResult = validPropExtendConfRefPropIsExists(propExtendConf);
		if(operResult == null){
			CfgPropExtendConf oldPropExtendConf = getObjectById(propExtendConf.getId(), CfgPropExtendConf.class);
			if(!oldPropExtendConf.getRefPropId().equals(propExtendConf.getRefPropId())){
				operResult = validPropExtendConfIsExists(propExtendConf);
			}
		}
		if(operResult == null){
			return HibernateUtil.updateEntityObject(propExtendConf, null);
		}
		return operResult;
	}

	/**
	 * 删除属性的扩展配置
	 * @param propExtendConfIds
	 * @return
	 */
	public String deletePropExtendConfExtend(String propExtendConfIds) {
		return deleteDataById("CfgPropExtendConf", propExtendConfIds);
	}
}
