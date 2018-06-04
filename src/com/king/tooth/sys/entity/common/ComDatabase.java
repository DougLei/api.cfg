package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.DynamicDataConstants;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgTabledata;
import com.king.tooth.sys.entity.common.database.DBFile;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.StrUtils;

/**
 * [通用的]数据库数据信息资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComDatabase extends AbstractSysResource implements ITable{
	
	/**
	 * 数字库名
	 */
	private String dbDisplayName;
	/**
	 * 数据库类型
	 */
	private String dbType;
	/**
	 * 数据库实例名
	 * <p>oracle中是实例名、sqlserver中就是数据库名</p>
	 */
	private String dbInstanceName;
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
	private String dbIp;
	/**
	 * 数据库端口
	 */
	private int dbPort;
	/**
	 * 数据库文件配置内容(json串)
	 */
	private String cfgMainFileContent;
	private DBFile mainFile;
	/**
	 * 数据库临时/日志文件配置内容(json串)
	 */
	private String cfgTmplogFileContent;
	private DBFile tmpLogFile;
	
	//------------------------------------------------------------------------
	
	public ComDatabase() {
	}
	/**
	 * 构造函数
	 * <p>其他参数赋值，可以通过调用setXXX方法赋值[除去构造函数中参数对应的属性不需要调用setXXX方法]</p>
	 * @param dbDisplayName
	 * @param dbInstanceName
	 * @param loginUserName
	 * @param loginPassword
	 */
	public ComDatabase(String dbDisplayName, String dbInstanceName, String loginUserName, String loginPassword) {
		this();
		this.dbDisplayName = dbDisplayName;
		setDbInstanceName(dbInstanceName);
		this.loginUserName = loginUserName;
		this.loginPassword = loginPassword;
		setDBFile();
	}
	/**
	 * 设置数据库的文件的属性
	 * @param dbInstanceName
	 */
	public void setDBFile(){
		this.mainFile = new DBFile("MAIN_" + getLoginUserName());
		this.tmpLogFile = new DBFile("TMPLOG_" + getLoginUserName());
	}
	
	public String getCfgMainFileContent() {
		if(mainFile != null){
			cfgMainFileContent = mainFile.toJsonString();
		}
		return cfgMainFileContent;
	}
	public String getCfgTmplogFileContent() {
		if(tmpLogFile != null){
			cfgTmplogFileContent = tmpLogFile.toJsonString();
		}
		return cfgTmplogFileContent;
	}
	public String getDbType() {
		if(StrUtils.isEmpty(dbType)){
			dbType = SysConfig.getSystemConfig("jdbc.dbType");
		}
		return dbType;
	}
	public String getDbIp() {
		if(StrUtils.isEmpty(dbIp)){
			dbIp = SysConfig.getSystemConfig("db.default.ip");
		}
		return dbIp;
	}
	public int getDbPort() {
		if(dbPort < 1){
			dbPort = Integer.valueOf(SysConfig.getSystemConfig("db.default.port"));
		}
		return dbPort;
	}
	public String getLoginPassword() {
		if(StrUtils.isEmpty(loginPassword)){
			loginPassword = SysConfig.getSystemConfig("db.default.password");
		}
		return loginPassword;
	}
	
	public String getDbInstanceName() {
		return dbInstanceName;
	}
	public void setCfgMainFileContent(String cfgMainFileContent) {
		this.cfgMainFileContent = cfgMainFileContent;
		this.mainFile = JsonUtil.parseObject(cfgMainFileContent, DBFile.class);
	}
	public void setCfgTmplogFileContent(String cfgTmplogFileContent) {
		this.cfgTmplogFileContent = cfgTmplogFileContent;
		this.tmpLogFile = JsonUtil.parseObject(cfgTmplogFileContent, DBFile.class);
	}
	
	public String getDbDisplayName() {
		if(StrUtils.isEmpty(dbDisplayName)){
			dbDisplayName = dbInstanceName;
		}
		return dbDisplayName;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public String getLoginUserName() {
		return loginUserName;
	}
	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	public void setDbIp(String dbIp) {
		this.dbIp = dbIp;
	}
	public String getId() {
		return id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public String getLastUpdatedUserId() {
		return lastUpdatedUserId;
	}
	public void setDbPort(int dbPort) {
		this.dbPort = dbPort;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public void setLastUpdatedUserId(String lastUpdatedUserId) {
		this.lastUpdatedUserId = lastUpdatedUserId;
	}
	public void setDbDisplayName(String dbDisplayName) {
		this.dbDisplayName = dbDisplayName;
	}
	public void setDbInstanceName(String dbInstanceName) {
		// 如果数据库类型是oracle数据库
		if(DynamicDataConstants.DB_TYPE_ORACLE.equals(getDbType())){
			// 如果和我oracle配置库的ip和端口一样，就说明是使用的我们的库，oracle实例名，要用我们的
			if(SysConfig.getSystemConfig("db.default.ip").equals(getDbIp()) 
					&& SysConfig.getSystemConfig("db.default.port").equals(getDbPort()+"")){
				this.dbInstanceName = SysConfig.getSystemConfig("db.default.instancename");
			}
			// 否则，说明是客户自己买的数据库服务器，程序要连接过去，这里直接记录实例名即可
			else{
				this.dbInstanceName = dbInstanceName;
			}
			return;
		}
		
		// 目前，否则就是sqlserver数据库
		if(StrUtils.isEmpty(dbInstanceName)){
			throw new IllegalArgumentException("sqlserver的数据库名不能为空！");
		}
		this.dbInstanceName = dbInstanceName;
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
	public int getIsDeploymentRun() {
		return isDeploymentRun;
	}
	public void setIsDeploymentRun(int isDeploymentRun) {
		this.isDeploymentRun = isDeploymentRun;
	}
	
	/**
	 * 获取数据库的连接url
	 * @return
	 */
	public String getUrl() {
		return DynamicDataConstants.getDataBaseLinkUrl(getDbType(), getDbIp(), getDbPort(), getDbInstanceName());
	}
	/**
	 * 获取数据库驱动
	 * @return
	 */
	public String getDriverClass() {
		return DynamicDataConstants.getDataBaseDriver(getDbType());
	}
	/**
	 * 获取数据库连接方言
	 * @return
	 */
	public String getDialect(){
		return DynamicDataConstants.getDataBaseDialect(getDbType());
	}
	
	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "COM_DATABASE");
		table.setName("[通用的]数据库数据信息资源对象表");
		table.setComments("[通用的]数据库数据信息资源对象表");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(16);
		
		CfgColumndata dbDisplayNameColumn = new CfgColumndata("db_display_name");
		dbDisplayNameColumn.setName("数字库名");
		dbDisplayNameColumn.setComments("数字库名");
		dbDisplayNameColumn.setColumnType(DataTypeConstants.STRING);
		dbDisplayNameColumn.setLength(100);
		dbDisplayNameColumn.setOrderCode(2);
		columns.add(dbDisplayNameColumn);
		
		CfgColumndata dbTypeColumn = new CfgColumndata("db_type");
		dbTypeColumn.setName("数据库类型");
		dbTypeColumn.setComments("数据库类型");
		dbTypeColumn.setColumnType(DataTypeConstants.STRING);
		dbTypeColumn.setLength(10);
		dbTypeColumn.setOrderCode(3);
		columns.add(dbTypeColumn);
		
		CfgColumndata dbInstanceNameColumn = new CfgColumndata("db_instance_name");
		dbInstanceNameColumn.setName("数据库实例名");
		dbInstanceNameColumn.setComments("数据库实例名：oracle中是实例名、sqlserver中就是数据库名");
		dbInstanceNameColumn.setColumnType(DataTypeConstants.STRING);
		dbInstanceNameColumn.setLength(20);
		dbInstanceNameColumn.setOrderCode(4);
		columns.add(dbInstanceNameColumn);
		
		CfgColumndata loginUserNameColumn = new CfgColumndata("login_user_name");
		loginUserNameColumn.setName("数据库登录名");
		loginUserNameColumn.setComments("数据库登录名");
		loginUserNameColumn.setColumnType(DataTypeConstants.STRING);
		loginUserNameColumn.setLength(20);
		loginUserNameColumn.setOrderCode(5);
		columns.add(loginUserNameColumn);
		
		CfgColumndata loginPasswordColumn = new CfgColumndata("login_password");
		loginPasswordColumn.setName("数据库登录密码");
		loginPasswordColumn.setComments("数据库登录密码");
		loginPasswordColumn.setColumnType(DataTypeConstants.STRING);
		loginPasswordColumn.setLength(20);
		loginPasswordColumn.setOrderCode(6);
		columns.add(loginPasswordColumn);
		
		CfgColumndata dbIpColumn = new CfgColumndata("db_ip");
		dbIpColumn.setName("数据库ip");
		dbIpColumn.setComments("数据库ip");
		dbIpColumn.setColumnType(DataTypeConstants.STRING);
		dbIpColumn.setLength(20);
		dbIpColumn.setOrderCode(7);
		columns.add(dbIpColumn);
		
		CfgColumndata dbPortColumn = new CfgColumndata("db_port");
		dbPortColumn.setName("数据库端口");
		dbPortColumn.setComments("数据库端口");
		dbPortColumn.setColumnType(DataTypeConstants.INTEGER);
		dbPortColumn.setLength(5);
		dbPortColumn.setOrderCode(8);
		columns.add(dbPortColumn);
		
		CfgColumndata cfgMainFileContentColumn = new CfgColumndata("cfg_main_file_content");
		cfgMainFileContentColumn.setName("数据库文件配置内容");
		cfgMainFileContentColumn.setComments("数据库文件配置内容(json串)");
		cfgMainFileContentColumn.setColumnType(DataTypeConstants.STRING);
		cfgMainFileContentColumn.setLength(700);
		cfgMainFileContentColumn.setOrderCode(9);
		columns.add(cfgMainFileContentColumn);
		
		CfgColumndata cfgTmplogFileContentColumn = new CfgColumndata("cfg_tmplog_file_content");
		cfgTmplogFileContentColumn.setName("数据库临时/日志文件配置内容");
		cfgTmplogFileContentColumn.setComments("数据库临时/日志文件配置内容(json串)");
		cfgTmplogFileContentColumn.setColumnType(DataTypeConstants.STRING);
		cfgTmplogFileContentColumn.setLength(700);
		cfgTmplogFileContentColumn.setOrderCode(10);
		columns.add(cfgTmplogFileContentColumn);
		
		CfgColumndata isDeploymentRunColumn = new CfgColumndata("is_deployment_run");
		isDeploymentRunColumn.setName("是否部署到正式环境");
		isDeploymentRunColumn.setComments("是否部署到正式环境");
		isDeploymentRunColumn.setColumnType(DataTypeConstants.INTEGER);
		isDeploymentRunColumn.setLength(1);
		isDeploymentRunColumn.setOrderCode(11);
		columns.add(isDeploymentRunColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_DATABASE";
	}
	
	public int getResourceType() {
		return DATABASE;
	}
	public String getResourceName() {
		return getDbDisplayName();
	}
	public String getResourceId() {
		return getId();
	}
}
