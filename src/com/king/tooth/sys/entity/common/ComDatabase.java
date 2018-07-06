package com.king.tooth.sys.entity.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.builtin.data.BuiltinCodeDataType;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.EntityJson;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.IPublish;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComPublishInfo;
import com.king.tooth.sys.entity.cfg.ComTabledata;
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
public class ComDatabase extends AbstractSysResource implements ITable, IEntityPropAnalysis, IPublish{
	
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
	private Integer dbPort;
	/**
	 * 数据库文件配置内容(json串)
	 */
	private String cfgMainFileContent;
	@JSONField(serialize = false)
	private DBFile mainFile;
	/**
	 * 数据库临时/日志文件配置内容(json串)
	 */
	private String cfgTmplogFileContent;
	@JSONField(serialize = false)
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
	public Integer getDbPort() {
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
	public void setDbPort(Integer dbPort) {
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
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("COM_DATABASE", 0);
		table.setName("数据库数据信息资源对象表");
		table.setComments("数据库数据信息资源对象表");
		table.setIsResource(1);
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(COMMON_PLATFORM);
		table.setIsCore(1);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(24);
		
		ComColumndata dbDisplayNameColumn = new ComColumndata("db_display_name", BuiltinCodeDataType.STRING, 100);
		dbDisplayNameColumn.setName("数字库名");
		dbDisplayNameColumn.setComments("数字库名");
		dbDisplayNameColumn.setOrderCode(2);
		columns.add(dbDisplayNameColumn);
		
		ComColumndata dbTypeColumn = new ComColumndata("db_type", BuiltinCodeDataType.STRING, 10);
		dbTypeColumn.setName("数据库类型");
		dbTypeColumn.setComments("数据库类型");
		dbTypeColumn.setIsNullabled(0);
		dbTypeColumn.setOrderCode(3);
		columns.add(dbTypeColumn);
		
		ComColumndata dbInstanceNameColumn = new ComColumndata("db_instance_name", BuiltinCodeDataType.STRING, 20);
		dbInstanceNameColumn.setName("数据库实例名");
		dbInstanceNameColumn.setComments("数据库实例名：oracle中是实例名、sqlserver中就是数据库名");
		dbInstanceNameColumn.setIsNullabled(0);
		dbInstanceNameColumn.setOrderCode(4);
		columns.add(dbInstanceNameColumn);
		
		ComColumndata loginUserNameColumn = new ComColumndata("login_user_name", BuiltinCodeDataType.STRING, 20);
		loginUserNameColumn.setName("数据库登录名");
		loginUserNameColumn.setComments("数据库登录名");
		loginUserNameColumn.setIsNullabled(0);
		loginUserNameColumn.setOrderCode(5);
		columns.add(loginUserNameColumn);
		
		ComColumndata loginPasswordColumn = new ComColumndata("login_password", BuiltinCodeDataType.STRING, 32);
		loginPasswordColumn.setName("数据库登录密码");
		loginPasswordColumn.setComments("数据库登录密码");
		loginPasswordColumn.setIsNullabled(0);
		loginPasswordColumn.setOrderCode(6);
		columns.add(loginPasswordColumn);
		
		ComColumndata dbIpColumn = new ComColumndata("db_ip", BuiltinCodeDataType.STRING, 20);
		dbIpColumn.setName("数据库ip");
		dbIpColumn.setComments("数据库ip");
		dbIpColumn.setIsNullabled(0);
		dbIpColumn.setOrderCode(7);
		columns.add(dbIpColumn);
		
		ComColumndata dbPortColumn = new ComColumndata("db_port", BuiltinCodeDataType.INTEGER, 5);
		dbPortColumn.setName("数据库端口");
		dbPortColumn.setComments("数据库端口");
		dbPortColumn.setIsNullabled(0);
		dbPortColumn.setOrderCode(8);
		columns.add(dbPortColumn);
		
		ComColumndata cfgMainFileContentColumn = new ComColumndata("cfg_main_file_content", BuiltinCodeDataType.STRING, 800);
		cfgMainFileContentColumn.setName("数据库文件配置内容");
		cfgMainFileContentColumn.setComments("数据库文件配置内容(json串)");
		cfgMainFileContentColumn.setOrderCode(9);
		columns.add(cfgMainFileContentColumn);
		
		ComColumndata cfgTmplogFileContentColumn = new ComColumndata("cfg_tmplog_file_content", BuiltinCodeDataType.STRING, 800);
		cfgTmplogFileContentColumn.setName("数据库临时/日志文件配置内容");
		cfgTmplogFileContentColumn.setComments("数据库临时/日志文件配置内容(json串)");
		cfgTmplogFileContentColumn.setOrderCode(10);
		columns.add(cfgTmplogFileContentColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_DATABASE";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "ComDatabase";
	}
	
	public JSONObject toEntityJson() {
		EntityJson entityJson = new EntityJson(JsonUtil.toJsonObject(this));
		entityJson.put("dbPort", dbPort);
		super.processSysResourceProps(entityJson);
		return entityJson.getEntityJson();
	}
	
	/**
	 * 获取数据库的连接url
	 * @return
	 */
	@JSONField(serialize = false)
	public String getUrl() {
		return BuiltinDatabaseData.getDataBaseLinkUrl(getDbType(), getDbIp(), getDbPort(), getDbInstanceName());
	}
	/**
	 * 获取数据库驱动
	 * @return
	 */
	@JSONField(serialize = false)
	public String getDriverClass() {
		return BuiltinDatabaseData.getDataBaseDriver(getDbType());
	}
	/**
	 * 获取数据库连接方言
	 * @return
	 */
	@JSONField(serialize = false)
	public String getDialect(){
		return BuiltinDatabaseData.getDataBaseDialect(getDbType());
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
			return "ok:连接成功，耗时["+connectSeconds+"]秒";
		} catch (Exception e) {
			return "err:测试数据库连接失败，系统在[2秒]内无法连接到数据库，请检查您的配置是否正确，以及要连接的数据库是否可以正常连接，或联系管理员：["+ExceptionUtil.getErrMsg(e)+"]";
		} finally{
			CloseUtil.closeDBConn(conn);
		}
	}
	
	public String validNotNullProps() {
		if(StrUtils.isEmpty(dbType)){
			return "数据库类型不能为空！";
		}
		if(!dbType.equals(BuiltinDatabaseData.DB_TYPE_ORACLE) && !dbType.equals(BuiltinDatabaseData.DB_TYPE_SQLSERVER)){
			return "系统目前只支持oracle和sqlserver数据库！";
		}
		if(StrUtils.isEmpty(dbInstanceName)){
			return "数据库实例名不能为空！";
		}
		if(StrUtils.isEmpty(loginUserName)){
			return "数据库登录名不能为空！";
		}
		if(StrUtils.isEmpty(loginPassword)){
			return "数据库登录密码不能为空！";
		}
		if(StrUtils.isEmpty(dbIp)){
			return "数据库ip不能为空！";
		}
		if(dbPort == null || dbPort < 1){
			return "数据库端口不能为空！";
		}
		return null;
	}
	
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
			// 验证数据库实例名
			if(BuiltinDatabaseData.DB_TYPE_ORACLE.equals(dbType)
					&& SysConfig.getSystemConfig("db.default.ip").equals(dbIp)
					&& SysConfig.getSystemConfig("db.default.port").equals(getDbPort()+"")){
				// 如果数据库类型是oracle数据库
				// 如果和jdbc中配置的ip和端口一样，就说明是使用的是当前库，则使用jdbc中配置的oracle数据库实例名
				this.dbInstanceName = SysConfig.getSystemConfig("db.default.instancename");
			}
			
			// 创建数据库文件对象
			this.mainFile = new DBFile("MAIN_" + loginUserName);
			this.tmpLogFile = new DBFile("TMPLOG_" + loginUserName);
		}
		return result;
	}
	
	public ComSysResource turnToResource() {
		throw new IllegalArgumentException("该资源目前不支持turnToResource功能");
	}
	
	public ComSysResource turnToPublishResource(String projectId, String refResourceId) {
		throw new IllegalArgumentException("该资源目前不支持turnToPublishResource功能");
	}
	
	/**
	 * 比较数据库的连接信息是否一致
	 * 用在保存/修改数据库信息的时候，要确保数据库连接信息的唯一性
	 * @param database
	 * @return
	 */
	public boolean compareLinkInfoIsSame(ComDatabase database){
		if(StrUtils.compareIsSame(dbInstanceName, database.getDbInstanceName())
				&& StrUtils.compareIsSame(loginUserName, database.getLoginUserName())
				&& StrUtils.compareIsSame(loginPassword, database.getLoginPassword())
				&& StrUtils.compareIsSame(dbIp, database.getDbIp())
				&& dbPort.equals(database.getDbPort())){
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
	public boolean compareIsSameDatabase(ComDatabase database){
		if(database.getDbType().equals(BuiltinDatabaseData.DB_TYPE_ORACLE)){
			if(StrUtils.compareIsSameIgnoreCase(dbInstanceName, database.getDbInstanceName())
					&& StrUtils.compareIsSame(dbIp, database.getDbIp())
					&& dbPort.equals(database.getDbPort())){
				return true;
			}
		}else if(database.getDbType().equals(BuiltinDatabaseData.DB_TYPE_SQLSERVER)){
			if(StrUtils.compareIsSame(dbIp, database.getDbIp()) && dbPort.equals(database.getDbPort())){
				return true;
			}
		}
		return false;
	}

	@JSONField(serialize = false)
	public Integer getResourceType() {
		return 1;
	}
	
	public ComPublishInfo turnToPublish() {
		ComPublishInfo publish = new ComPublishInfo();
		publish.setPublishDatabaseId(id);
		publish.setPublishResourceId(id);
		publish.setPublishResourceName(dbInstanceName);
		publish.setResourceType(DATABASE);
		return publish;
	}
	
	public JSONObject toPublishEntityJson(String projectId) {
		JSONObject json = toEntityJson();
		json.put("refDataId", json.getString(ResourceNameConstants.ID));
		processPublishEntityJson(json);
		return json;
	}
}
