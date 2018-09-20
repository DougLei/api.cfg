package com.king.tooth.sys.entity.cfg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.IPublish;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.database.DBFile;
import com.king.tooth.sys.entity.dm.DmPublishInfo;
import com.king.tooth.sys.entity.sys.SysResource;
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
public class CfgDatabase extends AbstractSysResource implements ITable, IEntityPropAnalysis, IPublish{
	
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
	public void setCfgMainFileContent(String cfgMainFileContent) {
		this.cfgMainFileContent = cfgMainFileContent;
		this.mainFile = JsonUtil.parseObject(cfgMainFileContent, DBFile.class);
	}
	public void setCfgTmplogFileContent(String cfgTmplogFileContent) {
		this.cfgTmplogFileContent = cfgTmplogFileContent;
		this.tmpLogFile = JsonUtil.parseObject(cfgTmplogFileContent, DBFile.class);
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
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("CFG_DATABASE", 0);
		table.setName("数据库信息表");
		table.setComments("数据库信息表");
		table.setIsResource(1);
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(COMMON_PLATFORM);
		table.setIsCore(1);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(24);
		
		ComColumndata displayNameColumn = new ComColumndata("display_name", BuiltinDataType.STRING, 100);
		displayNameColumn.setName("数字库名");
		displayNameColumn.setComments("数字库名");
		displayNameColumn.setOrderCode(2);
		columns.add(displayNameColumn);
		
		ComColumndata typeColumn = new ComColumndata("type", BuiltinDataType.STRING, 10);
		typeColumn.setName("数据库类型");
		typeColumn.setComments("数据库类型");
		typeColumn.setIsNullabled(0);
		typeColumn.setOrderCode(3);
		columns.add(typeColumn);
		
		ComColumndata instanceNameColumn = new ComColumndata("instance_name", BuiltinDataType.STRING, 20);
		instanceNameColumn.setName("数据库实例名");
		instanceNameColumn.setComments("数据库实例名：oracle中是实例名、sqlserver中就是数据库名");
		instanceNameColumn.setIsNullabled(0);
		instanceNameColumn.setOrderCode(4);
		columns.add(instanceNameColumn);
		
		ComColumndata loginUserNameColumn = new ComColumndata("login_user_name", BuiltinDataType.STRING, 20);
		loginUserNameColumn.setName("数据库登录名");
		loginUserNameColumn.setComments("数据库登录名");
		loginUserNameColumn.setIsNullabled(0);
		loginUserNameColumn.setOrderCode(5);
		columns.add(loginUserNameColumn);
		
		ComColumndata loginPasswordColumn = new ComColumndata("login_password", BuiltinDataType.STRING, 32);
		loginPasswordColumn.setName("数据库登录密码");
		loginPasswordColumn.setComments("数据库登录密码");
		loginPasswordColumn.setIsNullabled(0);
		loginPasswordColumn.setOrderCode(6);
		columns.add(loginPasswordColumn);
		
		ComColumndata ipColumn = new ComColumndata("ip", BuiltinDataType.STRING, 20);
		ipColumn.setName("数据库ip");
		ipColumn.setComments("数据库ip");
		ipColumn.setIsNullabled(0);
		ipColumn.setOrderCode(7);
		columns.add(ipColumn);
		
		ComColumndata portColumn = new ComColumndata("port", BuiltinDataType.INTEGER, 5);
		portColumn.setName("数据库端口");
		portColumn.setComments("数据库端口");
		portColumn.setIsNullabled(0);
		portColumn.setOrderCode(8);
		columns.add(portColumn);
		
		ComColumndata cfgMainFileContentColumn = new ComColumndata("cfg_main_file_content", BuiltinDataType.STRING, 800);
		cfgMainFileContentColumn.setName("数据库文件配置内容");
		cfgMainFileContentColumn.setComments("数据库文件配置内容(json串)");
		cfgMainFileContentColumn.setOrderCode(9);
		columns.add(cfgMainFileContentColumn);
		
		ComColumndata cfgTmplogFileContentColumn = new ComColumndata("cfg_tmplog_file_content", BuiltinDataType.STRING, 800);
		cfgTmplogFileContentColumn.setName("数据库临时/日志文件配置内容");
		cfgTmplogFileContentColumn.setComments("数据库临时/日志文件配置内容(json串)");
		cfgTmplogFileContentColumn.setOrderCode(10);
		columns.add(cfgTmplogFileContentColumn);
		
		table.setColumns(columns);
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
			return "ok:连接成功，耗时["+connectSeconds+"]秒";
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
	
	public SysResource turnToResource() {
		throw new IllegalArgumentException("该资源目前不支持turnToResource功能");
	}
	
	public SysResource turnToPublishResource(String projectId, String refResourceId) {
		throw new IllegalArgumentException("该资源目前不支持turnToPublishResource功能");
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

	@JSONField(serialize = false)
	public Integer getResourceType() {
		return DATABASE;
	}
	
	public DmPublishInfo turnToPublish() {
		DmPublishInfo publish = new DmPublishInfo();
		publish.setPublishDatabaseId(id);
		publish.setPublishResourceId(id);
		publish.setPublishResourceName(instanceName);
		publish.setResourceType(DATABASE);
		return publish;
	}
	
	public JSONObject toPublishEntityJson(String projectId) {
		JSONObject json = toEntityJson();
		json.put("refDataId", json.getString(ResourcePropNameConstants.ID));
		processPublishEntityJson(json);
		return json;
	}
}
