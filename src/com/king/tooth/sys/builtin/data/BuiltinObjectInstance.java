package com.king.tooth.sys.builtin.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.king.tooth.cache.SysContext;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.PermissionConstants;
import com.king.tooth.constants.ResourceInfoConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.cfg.CfgDatabase;
import com.king.tooth.sys.entity.cfg.CfgProject;
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
		currentSysBuiltinDatabaseInstance.setId(SysContext.getSystemConfig("current.sys.database.id"));
		currentSysBuiltinDatabaseInstance.setType(SysContext.getSystemConfig("jdbc.dbType"));
		currentSysBuiltinDatabaseInstance.setInstanceName(SysContext.getSystemConfig("db.default.instancename"));
		currentSysBuiltinDatabaseInstance.setLoginUserName(SysContext.getSystemConfig("jdbc.username"));
		currentSysBuiltinDatabaseInstance.setLoginPassword(SysContext.getSystemConfig("jdbc.password"));
		currentSysBuiltinDatabaseInstance.setIp(SysContext.getSystemConfig("db.default.ip"));
		currentSysBuiltinDatabaseInstance.setPort(Integer.valueOf(SysContext.getSystemConfig("db.default.port")));
//		currentSysBuiltinDatabaseInstance.setPort(1433);
	}
	
	/**
	 * 当前系统项目对象实例
	 */
	public transient static final CfgProject currentSysBuiltinProjectInstance = new CfgProject(); 
	static{
		currentSysBuiltinProjectInstance.setId(SysContext.getSystemConfig("current.sys.project.id"));
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
	public static final CfgColumn idColumn50 = new CfgColumn(ResourcePropNameConstants.ID, DataTypeConstants.STRING, 50);
	public static final CfgColumn customerIdColumn = new CfgColumn("customer_id", DataTypeConstants.STRING, 32);
	public static final CfgColumn projectIdColumn = new CfgColumn("project_id", DataTypeConstants.STRING, 32);
	public static final CfgColumn createDateColumn = new CfgColumn("create_date", DataTypeConstants.DATE, 0);
	public static final CfgColumn lastUpdateDateColumn = new CfgColumn("last_update_date", DataTypeConstants.DATE, 0);
	public static final CfgColumn createUserIdColumn = new CfgColumn("create_user_id", DataTypeConstants.STRING, 32);
	public static final CfgColumn lastUpdateUserIdColumn = new CfgColumn("last_update_user_id", DataTypeConstants.STRING, 32);
	static{
		idColumn32.setPropName(ResourcePropNameConstants.ID);
		idColumn32.setIsPrimaryKey(1);
		idColumn32.setIsNullabled(0);
		idColumn32.setName("主键");
		idColumn32.setComments("主键");
		idColumn32.setIsIgnoreValid(1);// 内置主键不做验证
		
		idColumn50.setPropName(ResourcePropNameConstants.ID);
		idColumn50.setIsPrimaryKey(1);
		idColumn50.setIsNullabled(0);
		idColumn50.setName("主键");
		idColumn50.setComments("主键");
		idColumn50.setIsIgnoreValid(1);// 内置主键不做验证
		
		customerIdColumn.setName("所属租户主键");
		customerIdColumn.setComments("所属租户主键");
		customerIdColumn.setIsIgnoreValid(1);
		
		projectIdColumn.setName("所属项目主键");
		projectIdColumn.setComments("所属项目主键");
		projectIdColumn.setIsIgnoreValid(1);
		
		createDateColumn.setName("创建时间");
		createDateColumn.setComments("创建时间");
		createDateColumn.setIsIgnoreValid(1);
		
		lastUpdateDateColumn.setComments("最后修改时间");
		lastUpdateDateColumn.setName("最后修改时间");
		lastUpdateDateColumn.setIsIgnoreValid(1);
		
		createUserIdColumn.setComments("创建人主键");
		createUserIdColumn.setName("创建人主键");
		createUserIdColumn.setIsIgnoreValid(1);
		
		lastUpdateUserIdColumn.setComments("最后修改人主键");
		lastUpdateUserIdColumn.setName("最后修改人主键");
		lastUpdateUserIdColumn.setIsIgnoreValid(1);
	}
	
	// -------------------------------------------------------
	/** 资源字段对象 */
	public static final CfgColumn nameColumn = new CfgColumn("name", DataTypeConstants.STRING, 100);
	public static final CfgColumn resourceNameColumn = new CfgColumn("resource_name", DataTypeConstants.STRING, 60);
	public static final CfgColumn isCreatedColumn = new CfgColumn("is_created", DataTypeConstants.INTEGER, 1);
	public static final CfgColumn isEnabledColumn = new CfgColumn("is_enabled", DataTypeConstants.INTEGER, 1);
	public static final CfgColumn requestMethodColumn = new CfgColumn("request_method", DataTypeConstants.STRING, 30);
	public static final CfgColumn remarkColumn = new CfgColumn("remark", DataTypeConstants.STRING, 200);
	static{
		nameColumn.setName("汉字描述名称");
		nameColumn.setComments("汉字描述名称");
		
		resourceNameColumn.setName("资源名");
		resourceNameColumn.setComments("资源名");
		
		isCreatedColumn.setName("是否被创建");
		isCreatedColumn.setComments("默认值为0，[针对CfgTable资源描述]该字段在建模时，值改为1，后续修改字段信息等，该值均不变，只有在取消建模时，才会改为0");
		isCreatedColumn.setDefaultValue("0");
		
		isEnabledColumn.setName("是否有效");
		isEnabledColumn.setComments("默认值为1");
		isEnabledColumn.setDefaultValue("1");
		
		requestMethodColumn.setName("请求资源的方法");
		requestMethodColumn.setComments("默认值：all，get/put/post/delete/all/none，多个可用,隔开；all表示支持全部，表示都不支持");
		requestMethodColumn.setDefaultValue(ResourceInfoConstants.ALL);
		
		remarkColumn.setName("备注");
		remarkColumn.setComments("备注");
	}
}
