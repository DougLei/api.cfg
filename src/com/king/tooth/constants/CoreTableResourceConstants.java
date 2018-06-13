package com.king.tooth.constants;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.king.tooth.plugins.jdbc.util.DynamicBasicDataColumnUtil;
import com.king.tooth.plugins.orm.hibernate.hbm.HibernateHbmHandler;
import com.king.tooth.sys.entity.app.ComRole;
import com.king.tooth.sys.entity.common.ComCode;
import com.king.tooth.sys.entity.common.ComColumndata;
import com.king.tooth.sys.entity.common.ComDataDictionary;
import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.sys.entity.common.ComHibernateHbm;
import com.king.tooth.sys.entity.common.ComOperLog;
import com.king.tooth.sys.entity.common.ComProject;
import com.king.tooth.sys.entity.common.ComProjectModule;
import com.king.tooth.sys.entity.common.ComProjectModuleBody;
import com.king.tooth.sys.entity.common.ComPublishInfo;
import com.king.tooth.sys.entity.common.ComReqLog;
import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.sys.entity.common.ComSysAccount;
import com.king.tooth.sys.entity.common.ComSysAccountOnlineStatus;
import com.king.tooth.sys.entity.common.ComSysResource;
import com.king.tooth.sys.entity.common.ComTabledata;
import com.king.tooth.sys.entity.datalinks.ComDataLinks;
import com.king.tooth.sys.entity.datalinks.ComProjectComSqlScriptLinks;
import com.king.tooth.sys.entity.datalinks.ComProjectComTabledataLinks;

/**
 * 系统核心的表资源
 * @author DougLei
 */
public class CoreTableResourceConstants {

	/**
	 * 获取系统的核心表资源对象集合
	 * @param dbType
	 * @return
	 */
	private static List<ComTabledata> getCoreTables(String dbType){
		List<ComTabledata> coreTables = new ArrayList<ComTabledata>(6);
		coreTables.add(new ComDatabase().toCreateTable(dbType));
		coreTables.add(new ComProject().toCreateTable(dbType));
		coreTables.add(new ComProjectModule().toCreateTable(dbType));
		coreTables.add(new ComHibernateHbm().toCreateTable(dbType));
		coreTables.add(new ComSqlScript().toCreateTable(dbType));
		coreTables.add(new ComCode().toCreateTable(dbType));
		return coreTables;
	}
	
	//-------------------------------------------------------------------------------------------
	
	/**
	 * 配置统的核心表资源对象集合
	 * @param dbType
	 * @return
	 */
	private transient static final List<ComTabledata> configSystemCoreTables = new ArrayList<ComTabledata>(19);
	/**
	 * 初始化配置系统的核心表资源对象集合
	 * @param dbType
	 * @return
	 */
	public static void initConfigSystemCoreTables(String dbType){
		// 添加核心表
		configSystemCoreTables.addAll(getCoreTables(dbType));
		// 添加表
		configSystemCoreTables.add(new ComSysResource().toCreateTable(dbType));
		configSystemCoreTables.add(new ComColumndata().toCreateTable(dbType));
		configSystemCoreTables.add(new ComTabledata().toCreateTable(dbType));
		configSystemCoreTables.add(new ComDataDictionary().toCreateTable(dbType));
		configSystemCoreTables.add(new ComDataLinks().toCreateTable(dbType));
		configSystemCoreTables.add(new ComOperLog().toCreateTable(dbType));
		configSystemCoreTables.add(new ComProjectModuleBody().toCreateTable(dbType));
		configSystemCoreTables.add(new ComReqLog().toCreateTable(dbType));
		configSystemCoreTables.add(new ComSysAccount().toCreateTable(dbType));
		configSystemCoreTables.add(new ComSysAccountOnlineStatus().toCreateTable(dbType));
		configSystemCoreTables.add(new ComPublishInfo().toCreateTable(dbType));
		// 添加关系表
		configSystemCoreTables.add(new ComProjectComSqlScriptLinks().toCreateTable(dbType));
		configSystemCoreTables.add(new ComProjectComTabledataLinks().toCreateTable(dbType));
		// 完善表数据，加入基础列的对象信息
		for (ComTabledata table : configSystemCoreTables) {
			DynamicBasicDataColumnUtil.initBasicColumnToTable(table);
		}
	}
	/**
	 * 获取配置系统的核心表资源对象集合
	 * @param dbType
	 * @return
	 */
	public static List<ComTabledata> getConfigsystemcoretables() {
		return configSystemCoreTables;
	}

	//-------------------------------------------------------------------------------------------
	
	/**
	 * 运行系统的核心表资源对象集合
	 * @param dbType
	 * @return
	 */
	private transient static final List<ComTabledata> appSystemCoreTables = new ArrayList<ComTabledata>(15);
	/**
	 * 初始化运行系统的核心表资源对象集合
	 * @param dbType
	 * @return
	 */
	public static void initAppSystemCoreTables(String dbType){
		// 添加核心表
		appSystemCoreTables.addAll(getCoreTables(dbType));
		// 添加表
		appSystemCoreTables.add(new ComSysResource().toCreateTable(dbType));
		appSystemCoreTables.add(new ComDataDictionary().toCreateTable(dbType));
		appSystemCoreTables.add(new ComDataLinks().toCreateTable(dbType));
		appSystemCoreTables.add(new ComOperLog().toCreateTable(dbType));
		appSystemCoreTables.add(new ComProjectModuleBody().toCreateTable(dbType));
		appSystemCoreTables.add(new ComReqLog().toCreateTable(dbType));
		appSystemCoreTables.add(new ComSysAccount().toCreateTable(dbType));
		appSystemCoreTables.add(new ComSysAccountOnlineStatus().toCreateTable(dbType));
		appSystemCoreTables.add(new ComRole().toCreateTable(dbType));
		// 添加关系表
		
		// 完善表数据，加入基础列的对象信息
		for (ComTabledata table : appSystemCoreTables) {
			DynamicBasicDataColumnUtil.initBasicColumnToTable(table);
		}
	}
	/**
	 * 获取运行系统的核心表资源对象集合
	 * @param dbType
	 * @return
	 */
	public static List<ComTabledata> getAppsystemcoretables() {
		return appSystemCoreTables;
	}
	
	//-------------------------------------------------------------------------------------------
	
	/**
	 * 核心表资源映射的InputStreams
	 * <p>
	 * 	例如：ComDatabase,ComSqlScript，当发布资源时，连接远程的数据库，并为其在本系统创建dataSource和sessionFactory，将这些资源的映射加入到sessionFactory中，方便数据交互
	 * </p>
	 */
	private transient static final List<InputStream> coreTableResourceMappingInputStreams = new ArrayList<InputStream>(6);
	/**
	 * 获得核心表资源映射的InputStreams
	 * @return
	 */
	public static List<InputStream> getCoretableresourcemappinginputstreams() {
		return coreTableResourceMappingInputStreams;
	}
	/**
	 * 初始化核心表资源映射的InputStreams
	 * @param dbType
	 */
	public static void initCoretableresourcemappinginputstreams(String dbType) {
		HibernateHbmHandler hibernateHbmHandler = new HibernateHbmHandler();
		List<ComTabledata> coreTables = getCoreTables(dbType);
		String hbmContent;
		for (ComTabledata table : coreTables) {
			hbmContent = hibernateHbmHandler.createHbmMappingContent(table, true);
			coreTableResourceMappingInputStreams.add(new ByteArrayInputStream(hbmContent.getBytes()));
		}
	}
	
	//-------------------------------------------------------------------------------------------
}
