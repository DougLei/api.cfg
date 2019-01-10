package com.api.sys.service.sys;

import com.alibaba.fastjson.JSONObject;
import com.api.annotation.Service;
import com.api.constants.ResourcePropNameConstants;
import com.api.constants.SqlStatementTypeConstants;
import com.api.sys.entity.sys.SysPushMessageInfo;
import com.api.sys.service.AService;
import com.api.util.hibernate.HibernateUtil;

/**
 * 推送消息信息表Service
 * @author DougLei
 */
@Service
public class SysPushMessageInfoService extends AService{

	/**
	 * 阅读消息
	 * @param id
	 * @return
	 */
	public Object readMessage(String id) {
		SysPushMessageInfo spmi = getObjectById(id, SysPushMessageInfo.class);

		// 修改阅读状态为已读
		if(spmi.getIsReaded() == 0){
			HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.UPDATE, "update SysPushMessageInfo set isReaded=1 where " + ResourcePropNameConstants.ID + "=?", id);
		}
		return spmi;
	}

	/**
	 * 修改消息的阅读状态
	 * <p>改为已读或未读</p>
	 * @param sysPushMessageInfo
	 * @return
	 */
	public Object updateMessageReadStatus(SysPushMessageInfo sysPushMessageInfo) {
		SysPushMessageInfo spmi = getObjectById(sysPushMessageInfo.getId(), SysPushMessageInfo.class);
		HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.UPDATE, "update SysPushMessageInfo set isReaded=? where " + ResourcePropNameConstants.ID + "=?", sysPushMessageInfo.getIsReaded(), spmi.getId());
		
		JSONObject json = new JSONObject(3);
		json.put(ResourcePropNameConstants.ID, sysPushMessageInfo.getId());
		json.put("isReaded", sysPushMessageInfo.getIsReaded());
		return json;
	}
}
