package com.api.thread.current;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.api.sys.entity.sys.SysAccountOnlineStatus;
import com.api.sys.entity.sys.reqlog.ReqLogData;
import com.api.util.CloseUtil;
import com.api.util.ExceptionUtil;
import com.api.util.StrUtils;
import com.api.util.database.DynamicDBUtil;

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
	/**
	 * 当前线程中使用到的connection集合
	 */
	private List<Connection> connections;
	
	/**
	 * 获取当前线程Connection实例，由调用方管理关闭，或由CurrentThreadContext.clearCurrentThreadData()统一处理
	 * @return
	 * @throws SQLException 
	 */
	public Connection getConnectionInstance() throws SQLException {
		if(connections == null){
			connections = new ArrayList<Connection>(4);
		}
		Connection connection = DynamicDBUtil.getDataSource(databaseId).getConnection();
		connections.add(connection);
		return connection;
	}
	
	/**
	 * 关闭connection集合
	 */
	public void closeConnections() {
		if(connections != null && connections.size() > 0){
			try {
				for (Connection connection : connections) {
					if(!connection.isClosed()){
						CloseUtil.closeDBConn(connection);
					}
				}
			} catch (SQLException e) {
				throw new IllegalArgumentException("关闭连接时出现异常：" + ExceptionUtil.getErrMsg(e));
			}
		}
	}

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
