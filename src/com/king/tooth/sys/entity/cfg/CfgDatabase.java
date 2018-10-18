package com.king.tooth.sys.entity.cfg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.database.DBFile;
import com.king.tooth.util.CloseUtil;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.StrUtils;

/**
 * 数据库信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class CfgDatabase extends BasicEntity implements ITable, IEntityPropAnalysis, IEntity{
	
	/**
	 * 数字库名
	 */
	private String displayName;
	/**
	 * 数据库类型
	 */
	private String type;
	/**
	 * 数据库实例名
	 * <p>oracle中是实例名、sqlserver中就是数据库名</p>
	 */
	private String instanceName;
	/**
	 * 数据库登录名
	 */
	private String loginUserName;
	/**
	 * 数据库登录密码
	 */
	private String loginPassword;
	/**
	 * 数据库ip
	 */
	private String ip;
	/**
	 * 数据库端口
	 */
	private Integer port;
	/**
	 * 数据库文件配置内容(json串)
	 */
	private String mainFileContent;
	@JSONField(serialize = false)
	private DBFile mainFile;
	/**
	 * 数据库临时/日志文件配置内容(json串)
	 */
	private String tmplogFileContent;
	@JSONField(serialize = false)
	private DBFile tmpLogFile;
	
	//------------------------------------------------------------------------
	
	public String getMainFileContent() {
		if(mainFile != null){
			mainFileContent = mainFile.toJsonString();
		}
		return mainFileContent;
	}
	public String getTmplogFileContent() {
		if(tmpLogFile != null){
			tmplogFileContent = tmpLogFile.toJsonString();
		}
		return tmplogFileContent;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getInstanceName() {
		return instanceName;
	}
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getLoginPassword() {
		return loginPassword;
	}
	public void setMainFileContent(String mainFileContent) {
		this.mainFileContent = mainFileContent;
		this.mainFile = JsonUtil.parseObject(mainFileContent, DBFile.class);
	}
	public void setTmplogFileContent(String tmplogFileContent) {
		this.tmplogFileContent = tmplogFileContent;
		this.tmpLogFile = JsonUtil.parseObject(tmplogFileContent, DBFile.class);
	}
	public String getDisplayName() {
		if(StrUtils.isEmpty(displayName)){
			displayName = instanceName;
		}
		return displayName;
	}
	public String getLoginUserName() {
		return loginUserName;
	}
	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}
	public DBFile getMainFile() {
		return mainFile;
	}
	public DBFile getTmpLogFile() {
		return tmpLogFile;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(9+7);
		
		CfgColumn displayNameColumn = new CfgColumn("display_name", DataTypeConstants.STRING, 100);
		displayNameColumn.setName("数字库名");
		displayNameColumn.setComments("数字库名");
		columns.add(displayNameColumn);
		
		CfgColumn typeColumn = new CfgColumn("type", DataTypeConstants.STRING, 10);
		typeColumn.setName("数据库类型");
		typeColumn.setComments("数据库类型");
		typeColumn.setIsNullabled(0);
		columns.add(typeColumn);
		
		CfgColumn instanceNameColumn = new CfgColumn("instance_name", DataTypeConstants.STRING, 20);
		instanceNameColumn.setName("数据库实例名");
		instanceNameColumn.setComments("数据库实例名：oracle中是实例名、sqlserver中就是数据库名");
		instanceNameColumn.setIsNullabled(0);
		columns.add(instanceNameColumn);
		
		CfgColumn loginUserNameColumn = new CfgColumn("login_user_name", DataTypeConstants.STRING, 20);
		loginUserNameColumn.setName("数据库登录名");
		loginUserNameColumn.setComments("数据库登录名");
		loginUserNameColumn.setIsNullabled(0);
		columns.add(loginUserNameColumn);
		
		CfgColumn loginPasswordColumn = new CfgColumn("login_password", DataTypeConstants.STRING, 32);
		loginPasswordColumn.setName("数据库登录密码");
		loginPasswordColumn.setComments("数据库登录密码");
		loginPasswordColumn.setIsNullabled(0);
		columns.add(loginPasswordColumn);
		
		CfgColumn ipColumn = new CfgColumn("ip", DataTypeConstants.STRING, 20);
		ipColumn.setName("数据库ip");
		ipColumn.setComments("数据库ip");
		ipColumn.setIsNullabled(0);
		columns.add(ipColumn);
		
		CfgColumn portColumn = new CfgColumn("port", DataTypeConstants.INTEGER, 5);
		portColumn.setName("数据库端口");
		portColumn.setComments("数据库端口");
		portColumn.setIsNullabled(0);
		columns.add(portColumn);
		
		CfgColumn mainFileContentColumn = new CfgColumn("main_file_content", DataTypeConstants.STRING, 800);
		mainFileContentColumn.setName("数据库文件配置内容");
		mainFileContentColumn.setComments("数据库文件配置内容(json串)");
		columns.add(mainFileContentColumn);
		
		CfgColumn tmplogFileContentColumn = new CfgColumn("tmplog_file_content", DataTypeConstants.STRING, 800);
		tmplogFileContentColumn.setName("数据库临时/日志文件配置内容");
		tmplogFileContentColumn.setComments("数据库临时/日志文件配置内容(json串)");
		columns.add(tmplogFileContentColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("数据库信息表");
		table.setComments("数据库信息表");
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "CFG_DATABASE";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "CfgDatabase";
	}
	
	/**
	 * 获取数据库的连接url
	 * @return
	 */
	@JSONField(serialize = false)
	public String getUrl() {
		return BuiltinDatabaseData.getDataBaseLinkUrl(getType(), getIp(), getPort(), getInstanceName());
	}
	/**
	 * 获取数据库驱动
	 * @return
	 */
	@JSONField(serialize = false)
	public String getDriverClass() {
		return BuiltinDatabaseData.getDataBaseDriver(getType());
	}
	/**
	 * 获取数据库连接方言
	 * @return
	 */
	@JSONField(serialize = false)
	public String getDialect(){
		return BuiltinDatabaseData.getDataBaseDialect(getType());
	}
	
	/**
	 * 测试数据库连接
	 */
	public String testDbLink(){
		Connection conn = null;
		try {
			long start = System.currentTimeMillis();
			Class.forName(getDriverClass());
			DriverManager.setLoginTimeout(2);
			conn = DriverManager.getConnection(getUrl(), getLoginUserName(), getLoginPassword());
			int connectSeconds = (int) ((System.currentTimeMillis()-start)/1000);
			return "连接成功，耗时["+connectSeconds+"]秒";
		} catch (Exception e) {
			return "测试数据库连接失败，系统在[2秒]内无法连接到数据库，请检查您的配置是否正确，以及要连接的数据库是否可以正常连接，或联系管理员：["+ExceptionUtil.getErrMsg(e)+"]";
		} finally{
			CloseUtil.closeDBConn(conn);
		}
	}
	
	public String validNotNullProps() {
		if(StrUtils.isEmpty(type)){
			return "数据库类型不能为空！";
		}
		if(!type.equals(BuiltinDatabaseData.DB_TYPE_ORACLE) && !type.equals(BuiltinDatabaseData.DB_TYPE_SQLSERVER)){
			return "系统目前只支持oracle和sqlserver数据库！";
		}
		if(StrUtils.isEmpty(instanceName)){
			return "数据库实例名不能为空！";
		}
		if(StrUtils.isEmpty(loginUserName)){
			return "数据库登录名不能为空！";
		}
		if(StrUtils.isEmpty(loginPassword)){
			return "数据库登录密码不能为空！";
		}
		if(StrUtils.isEmpty(ip)){
			return "数据库ip不能为空！";
		}
		if(port == null || port < 1){
			return "数据库端口不能为空！";
		}
		return null;
	}
	
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
			// 验证数据库实例名
			if(BuiltinDatabaseData.DB_TYPE_ORACLE.equals(type)
					&& SysConfig.getSystemConfig("db.default.ip").equals(ip)
					&& SysConfig.getSystemConfig("db.default.port").equals(getPort()+"")){
				// 如果数据库类型是oracle数据库
				// 如果和jdbc中配置的ip和端口一样，就说明是使用的是当前库，则使用jdbc中配置的oracle数据库实例名
				this.instanceName = SysConfig.getSystemConfig("db.default.instancename");
			}
			
			// 创建数据库文件对象
			this.mainFile = new DBFile("MAIN_" + loginUserName);
			this.tmpLogFile = new DBFile("TMPLOG_" + loginUserName);
		}
		return result;
	}
	
	/**
	 * 比较数据库的连接信息是否一致
	 * 用在保存/修改数据库信息的时候，要确保数据库连接信息的唯一性
	 * @param database
	 * @return
	 */
	public boolean compareLinkInfoIsSame(CfgDatabase database){
		if(StrUtils.compareIsSame(instanceName, database.getInstanceName())
				&& StrUtils.compareIsSame(loginUserName, database.getLoginUserName())
				&& StrUtils.compareIsSame(loginPassword, database.getLoginPassword())
				&& StrUtils.compareIsSame(ip, database.getIp())
				&& port.equals(database.getPort())){
			return true;
		}
		return false;
	}
	
	/**
	 * 比较是否是同一个数据库
	 * 用在创建数据库的时候，如果和本系统的数据库是一个，则可以实现创建数据库的操作
	 * @param database
	 * @return
	 */
	public boolean compareIsSameDatabase(CfgDatabase database){
		if(database.getType().equals(BuiltinDatabaseData.DB_TYPE_ORACLE)){
			if(StrUtils.compareIsSameIgnoreCase(instanceName, database.getInstanceName())
					&& StrUtils.compareIsSame(ip, database.getIp())
					&& port.equals(database.getPort())){
				return true;
			}
		}else if(database.getType().equals(BuiltinDatabaseData.DB_TYPE_SQLSERVER)){
			if(StrUtils.compareIsSame(ip, database.getIp()) && port.equals(database.getPort())){
				return true;
			}
		}
		return false;
	}
}
