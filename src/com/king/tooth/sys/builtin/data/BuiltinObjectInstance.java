package com.king.tooth.sys.builtin.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.PermissionConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.cfg.CfgDatabase;
import com.king.tooth.sys.entity.cfg.ComProject;
import com.king.tooth.sys.entity.sys.SysPermissionPriority;
import com.king.tooth.sys.entity.sys.permission.SysPermissionExtend;
import com.king.tooth.util.DateUtil;

/**
 * 系统内置的对象实例
 * @author DougLei
 */
public class BuiltinObjectInstance {
	
	/**
	 * 数据的有效期
	 */
	public transient static final Date validDate = DateUtil.parseDate("2099-12-31 23:59:59");
	
	// -------------------------------------------------------
	/**
	 * 当前系统的数据库对象实例
	 */
	public transient static final CfgDatabase currentSysBuiltinDatabaseInstance = new CfgDatabase();
	static{
		currentSysBuiltinDatabaseInstance.setId(SysConfig.getSystemConfig("current.sys.database.id"));
		currentSysBuiltinDatabaseInstance.setType(SysConfig.getSystemConfig("jdbc.dbType"));
		currentSysBuiltinDatabaseInstance.setInstanceName(SysConfig.getSystemConfig("db.default.instancename"));
		currentSysBuiltinDatabaseInstance.setLoginUserName(SysConfig.getSystemConfig("jdbc.username"));
		currentSysBuiltinDatabaseInstance.setLoginPassword(SysConfig.getSystemConfig("jdbc.password"));
		currentSysBuiltinDatabaseInstance.setIp(SysConfig.getSystemConfig("db.default.ip"));
		currentSysBuiltinDatabaseInstance.setPort(Integer.valueOf(SysConfig.getSystemConfig("db.default.port")));
	}
	
	/**
	 * 当前系统项目对象实例
	 */
	public transient static final ComProject currentSysBuiltinProjectInstance = new ComProject(); 
	static{
		currentSysBuiltinProjectInstance.setId(SysConfig.getSystemConfig("current.sys.project.id"));
	}
	
	// -------------------------------------------------------
	/**
	 * 权限优先级集合
	 */
	public static final List<SysPermissionPriority> permissionPriorities = new ArrayList<SysPermissionPriority>(6); 
	static{
		permissionPriorities.add(new SysPermissionPriority(PermissionConstants.OBJ_TYPE_USER, 1));
		permissionPriorities.add(new SysPermissionPriority(PermissionConstants.OBJ_TYPE_ACCOUNT, 2));
		permissionPriorities.add(new SysPermissionPriority(PermissionConstants.OBJ_TYPE_ROLE, 3));
		permissionPriorities.add(new SysPermissionPriority(PermissionConstants.OBJ_TYPE_DEPT, 4));
		permissionPriorities.add(new SysPermissionPriority(PermissionConstants.OBJ_TYPE_POSITION, 5));
		permissionPriorities.add(new SysPermissionPriority(PermissionConstants.OBJ_TYPE_USERGROUP, 6));
	}
	
	/**
	 * 所有权限的对象实例
	 */
	public static final SysPermissionExtend allPermission = new SysPermissionExtend();
	static{
		allPermission.setRefResourceCode("ALL");
		allPermission.setRefResourceId("ALL");
		allPermission.setRefResourceType("ALL");
	}
	
	// -------------------------------------------------------
	/** 基础字段对象 */
	public static final CfgColumn idColumn32 = new CfgColumn(ResourcePropNameConstants.ID, DataTypeConstants.STRING, 32);
	public static final CfgColumn idColumn50 = new CfgColumn(ResourcePropNameConstants.ID, DataTypeConstants.STRING, 32);
	public static final CfgColumn customerIdColumn = new CfgColumn("customer_id", DataTypeConstants.STRING, 32);
	public static final CfgColumn projectIdColumn = new CfgColumn("project_id", DataTypeConstants.STRING, 32);
	public static final CfgColumn createDateColumn = new CfgColumn("create_date", DataTypeConstants.DATE, 0);
	public static final CfgColumn lastUpdateDateColumn = new CfgColumn("last_update_date", DataTypeConstants.DATE, 0);
	public static final CfgColumn createUserIdColumn = new CfgColumn("create_user_id", DataTypeConstants.STRING, 32);
	public static final CfgColumn lastUpdateUserIdColumn = new CfgColumn("last_update_user_id", DataTypeConstants.STRING, 32);
	static{
		idColumn32.setIsPrimaryKey(1);
		idColumn32.setIsNullabled(0);
		idColumn32.setName("主键");
		idColumn32.setComments("主键");
		
		idColumn50.setIsPrimaryKey(1);
		idColumn50.setIsNullabled(0);
		idColumn50.setName("主键");
		idColumn50.setComments("主键");
		
		customerIdColumn.setName("所属租户主键");
		customerIdColumn.setComments("所属租户主键");
		
		projectIdColumn.setName("所属项目主键");
		projectIdColumn.setComments("所属项目主键");
		
		createDateColumn.setName("创建时间");
		createDateColumn.setComments("创建时间");
		
		lastUpdateDateColumn.setComments("最后修改时间");
		lastUpdateDateColumn.setName("最后修改时间");
		
		createUserIdColumn.setComments("创建人主键");
		createUserIdColumn.setName("创建人主键");
		
		lastUpdateUserIdColumn.setComments("最后修改人主键");
		lastUpdateUserIdColumn.setName("最后修改人主键");
	}
}
