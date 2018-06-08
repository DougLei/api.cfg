package com.king.tooth.sys.entity.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.DynamicDataConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.common.database.DBFile;
import com.king.tooth.util.CloseUtil;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.StrUtils;

/**
 * 数据库数据信息资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComDatabase extends AbstractSysResource implements ITable, IEntity, IEntityPropAnalysis{
	
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
		return dbType;
	}
	public String getDbIp() {
		return dbIp;
	}
	public int getDbPort() {
		return dbPort;
	}
	public String getLoginPassword() {
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
	public void setDbPort(int dbPort) {
		this.dbPort = dbPort;
	}
	public void setDbDisplayName(String dbDisplayName) {
		this.dbDisplayName = dbDisplayName;
	}
	public void setDbInstanceName(String dbInstanceName) {
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
	
	public ComTabledata toCreateTable(String dbType) {
		ComTabledata table = new ComTabledata(dbType, "COM_DATABASE", 0);
		table.setIsResource(1);
		table.setName("数据库数据信息资源对象表");
		table.setComments("数据库数据信息资源对象表");
		table.setIsBuiltin(1);
		
		table.setIsNeedDeploy(1);
		table.setReqResourceMethod(GET+","+DELETE);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(21);
		
		ComColumndata dbDisplayNameColumn = new ComColumndata("db_display_name");
		dbDisplayNameColumn.setName("数字库名");
		dbDisplayNameColumn.setComments("数字库名");
		dbDisplayNameColumn.setColumnType(DataTypeConstants.STRING);
		dbDisplayNameColumn.setLength(100);
		dbDisplayNameColumn.setOrderCode(2);
		columns.add(dbDisplayNameColumn);
		
		ComColumndata dbTypeColumn = new ComColumndata("db_type");
		dbTypeColumn.setName("数据库类型");
		dbTypeColumn.setComments("数据库类型");
		dbTypeColumn.setColumnType(DataTypeConstants.STRING);
		dbTypeColumn.setLength(10);
		dbTypeColumn.setOrderCode(3);
		columns.add(dbTypeColumn);
		
		ComColumndata dbInstanceNameColumn = new ComColumndata("db_instance_name");
		dbInstanceNameColumn.setName("数据库实例名");
		dbInstanceNameColumn.setComments("数据库实例名：oracle中是实例名、sqlserver中就是数据库名");
		dbInstanceNameColumn.setColumnType(DataTypeConstants.STRING);
		dbInstanceNameColumn.setLength(20);
		dbInstanceNameColumn.setOrderCode(4);
		columns.add(dbInstanceNameColumn);
		
		ComColumndata loginUserNameColumn = new ComColumndata("login_user_name");
		loginUserNameColumn.setName("数据库登录名");
		loginUserNameColumn.setComments("数据库登录名");
		loginUserNameColumn.setColumnType(DataTypeConstants.STRING);
		loginUserNameColumn.setLength(20);
		loginUserNameColumn.setOrderCode(5);
		columns.add(loginUserNameColumn);
		
		ComColumndata loginPasswordColumn = new ComColumndata("login_password");
		loginPasswordColumn.setName("数据库登录密码");
		loginPasswordColumn.setComments("数据库登录密码");
		loginPasswordColumn.setColumnType(DataTypeConstants.STRING);
		loginPasswordColumn.setLength(20);
		loginPasswordColumn.setOrderCode(6);
		columns.add(loginPasswordColumn);
		
		ComColumndata dbIpColumn = new ComColumndata("db_ip");
		dbIpColumn.setName("数据库ip");
		dbIpColumn.setComments("数据库ip");
		dbIpColumn.setColumnType(DataTypeConstants.STRING);
		dbIpColumn.setLength(20);
		dbIpColumn.setOrderCode(7);
		columns.add(dbIpColumn);
		
		ComColumndata dbPortColumn = new ComColumndata("db_port");
		dbPortColumn.setName("数据库端口");
		dbPortColumn.setComments("数据库端口");
		dbPortColumn.setColumnType(DataTypeConstants.INTEGER);
		dbPortColumn.setLength(5);
		dbPortColumn.setOrderCode(8);
		columns.add(dbPortColumn);
		
		ComColumndata cfgMainFileContentColumn = new ComColumndata("cfg_main_file_content");
		cfgMainFileContentColumn.setName("数据库文件配置内容");
		cfgMainFileContentColumn.setComments("数据库文件配置内容(json串)");
		cfgMainFileContentColumn.setColumnType(DataTypeConstants.STRING);
		cfgMainFileContentColumn.setLength(700);
		cfgMainFileContentColumn.setOrderCode(9);
		columns.add(cfgMainFileContentColumn);
		
		ComColumndata cfgTmplogFileContentColumn = new ComColumndata("cfg_tmplog_file_content");
		cfgTmplogFileContentColumn.setName("数据库临时/日志文件配置内容");
		cfgTmplogFileContentColumn.setComments("数据库临时/日志文件配置内容(json串)");
		cfgTmplogFileContentColumn.setColumnType(DataTypeConstants.STRING);
		cfgTmplogFileContentColumn.setLength(700);
		cfgTmplogFileContentColumn.setOrderCode(10);
		columns.add(cfgTmplogFileContentColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_DATABASE";
	}
	
	public String getEntityName() {
		return "ComDatabase";
	}
	
	public JSONObject toEntity() {
		JSONObject json = JsonUtil.toJsonObject(this);
		json.put("dbPort", dbPort+"");
		json.put("isEnabled", isEnabled+"");
		json.put("validDate", validDate);
		json.put("isBuiltin", isBuiltin+"");
		json.put("isNeedDeploy", isNeedDeploy+"");
		json.put("isDeployed", isDeployed+"");
		json.put(ResourceNameConstants.CREATE_TIME, this.createTime);
		return json;
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
	
	/**
	 * 测试数据库连接
	 */
	private void testDbLink(){
		Connection conn = null;
		try {
			Class.forName(getDriverClass());
			conn = DriverManager.getConnection(getUrl(), getLoginUserName(), getLoginPassword());
		} catch (Exception e) {
			throw new IllegalArgumentException("数据库连接失败，请检查您的配置是否正确，以及要连接的数据库是否可以正常连接：["+ExceptionUtil.getErrMsg(e)+"]");
		} finally{
			CloseUtil.closeDBConn(conn);
		}
	}
	
	public void validNotNullProps() {
		if(!isValidNotNullProps){
			if(StrUtils.isEmpty(dbType)){
				throw new NullPointerException("数据库类型不能为空！");
			}
			if(StrUtils.isEmpty(dbInstanceName)){
				throw new NullPointerException("数据库名不能为空！");
			}
			if(StrUtils.isEmpty(loginUserName)){
				throw new NullPointerException("数据库登录名不能为空！");
			}
			if(StrUtils.isEmpty(loginPassword)){
				throw new NullPointerException("数据库登录密码不能为空！");
			}
			if(StrUtils.isEmpty(dbIp)){
				throw new NullPointerException("数据库ip不能为空！");
			}
			if(dbPort < 1){
				throw new NullPointerException("数据库端口不能为空！");
			}
			isValidNotNullProps = true;
		}
	}
	
	public void analysisResourceProp() {
		validNotNullProps();
		
		// 验证数据库实例名
		if(DynamicDataConstants.DB_TYPE_ORACLE.equals(dbType)
				&& SysConfig.getSystemConfig("db.default.ip").equals(dbIp)
				&& SysConfig.getSystemConfig("db.default.port").equals(getDbPort()+"")){
			// 如果数据库类型是oracle数据库
			// 如果和jdbc中配置的ip和端口一样，就说明是使用的是当前库，则使用jdbc中配置的oracle数据库实例名
			this.dbInstanceName = SysConfig.getSystemConfig("db.default.instancename");
		}
		
		// 创建数据库文件对象
		this.mainFile = new DBFile("MAIN_" + loginUserName);
		this.tmpLogFile = new DBFile("TMPLOG_" + loginUserName);
		
		testDbLink();
	}
	
	public ComSysResource turnToResource() {
		analysisResourceProp();
		ComSysResource resource = super.turnToResource();
		resource.setRefResourceId(id);
		resource.setResourceType(DATABASE);
		resource.setResourceName(dbInstanceName);
		return resource;
	}
	
	/**
	 * 比较数据库的连接信息是否一致
	 * @param database
	 * @return
	 */
	public boolean compareLinkInfoIsSame(ComDatabase database){
		if(StrUtils.compareIsSame(dbInstanceName, database.getDbInstanceName())
				&& StrUtils.compareIsSame(loginUserName, database.getLoginUserName())
				&& StrUtils.compareIsSame(loginPassword, database.getLoginPassword())
				&& StrUtils.compareIsSame(dbIp, database.getDbIp())
				&& dbPort == database.getDbPort()){
			return true;
		}
		return false;
	}
}
