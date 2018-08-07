package com.king.tooth.plugins.thread;

import java.io.Serializable;

import org.hibernate.Session;

import com.king.tooth.plugins.thread.log.ReqLogData;
import com.king.tooth.sys.entity.sys.SysAccountOnlineStatus;
import com.king.tooth.util.StrUtils;

/**
 * 当前线程的数据对象
 * @author DougLei
 */
@SuppressWarnings("serial")
class CurrentThreadData implements Serializable{
	
	/**
	 * 进行配置的项目id
	 * <p>配置系统专用</p>
	 */
	private String confProjectId;
	/**
	 * 当前线程的租户id
	 * <p>记录每个线程请求的租户主键</p>
	 * <p>和SysAccountOnlineStatus中的currentCustomerId值一致</p>
	 */
	private String customerId;
	/**
	 * 当前线程的项目id
	 * <p>记录每个线程请求的项目主键</p>
	 * <p>和SysAccountOnlineStatus中的currentProjectId值一致</p>
	 */
	private String projectId;
	/**
	 * 当前线程的数据库id
     * <p>通过不同的数据库id，获取不同的dataSource和sessionFactory对象</p>
	 */
	private String databaseId;
	/**
	 * 当前线程的hibernate session对象
	 */
	private Session currentSession;
	/**
	 * 当前线程的账户在线状态
	 */
	private SysAccountOnlineStatus currentAccountOnlineStatus;
	/**
	 * 当前线程的请求日志数据对象
	 */
	private ReqLogData reqLogData;
	
	public String getCustomerId() {
		if(StrUtils.isEmpty(customerId)){
			return "unknow";
		}
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getDatabaseId() {
		return databaseId;
	}
	public void setDatabaseId(String databaseId) {
		this.databaseId = databaseId;
	}
	public Session getCurrentSession() {
		return currentSession;
	}
	public void setCurrentSession(Session currentSession) {
		this.currentSession = currentSession;
	}
	public SysAccountOnlineStatus getCurrentAccountOnlineStatus() {
		return currentAccountOnlineStatus;
	}
	public void setCurrentAccountOnlineStatus(SysAccountOnlineStatus currentAccountOnlineStatus) {
		this.currentAccountOnlineStatus = currentAccountOnlineStatus;
	}
	public String getConfProjectId() {
		return confProjectId;
	}
	public void setConfProjectId(String confProjectId) {
		this.confProjectId = confProjectId;
	}
	public ReqLogData getReqLogData() {
		if(reqLogData == null){
			reqLogData = new ReqLogData();
		}
		return reqLogData;
	}
}
