package com.api.thread.operdb.account.online.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.alibaba.fastjson.JSONObject;
import com.api.sys.entity.sys.SysAccountOnlineStatus;
import com.api.thread.operdb.HibernateOperDBThread;
import com.api.util.ExceptionUtil;
import com.api.util.Log4jUtil;
import com.api.util.ResourceHandlerUtil;
import com.api.util.StrUtils;
import com.api.util.hibernate.HibernateUtil;

/**
 * 修改账户在线状态的线程
 * @author DougLei
 */
public final class UpdateAccountOnlineStatusThread extends HibernateOperDBThread{
	/**
	 * 线程名前缀
	 */
	private static final String threadNamePrefix = "UpdateAccountOnlineStatus_";
	
	/**
	 * 要修改的accountOnlineStatus数据
	 */
	private SysAccountOnlineStatus accountOnlineStatus;
	
	private JSONObject data;
	private List<Object> parameters;

	public UpdateAccountOnlineStatusThread(Session session, SysAccountOnlineStatus accountOnlineStatus, String currentAccountId, String currentUserId, String projectId, String customerId) {
		super(session);
		this.currentAccountId = currentAccountId;
		this.currentUserId = currentUserId;
		this.projectId = projectId;
		this.customerId = customerId;
		this.accountOnlineStatus = accountOnlineStatus;
		setName(threadNamePrefix + ResourceHandlerUtil.getRandom(1000000000));
	}
	
	protected boolean isGoOn() {
		return accountOnlineStatus != null && accountOnlineStatus.getIsUpdate() && StrUtils.notEmpty(accountOnlineStatus.getId());
	}
	
	protected void doRun() {
		accountOnlineStatus.setLastUpdateUserId(currentAccountId);
		accountOnlineStatus.setLastUpdateDate(new Date());
		
		data = accountOnlineStatus.toEntityJson();
		parameters = new ArrayList<Object>(data.size()-1);
		String updateHql = HibernateUtil.installEntityUpdateHql(accountOnlineStatus.getEntityName(), accountOnlineStatus.getId(), data, parameters);
		
		Query query = session.createQuery(updateHql);
		HibernateUtil.setParamters(query, parameters);
		query.executeUpdate();
	}

	protected void doCatch(Exception e) {
		Log4jUtil.warn("修改账户在线状态时出现异常信息：{}", ExceptionUtil.getErrMsg(e));
	}

	protected void doFinally() {
		if(data != null && data.size() > 0){
			data.clear();
		}
		if(parameters != null && parameters.size() > 0){
			parameters.clear();
		}
	}
}
