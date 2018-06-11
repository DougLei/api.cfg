package com.king.tooth.constants;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.king.tooth.plugins.orm.hibernate.hbm.HibernateHbmHandler;
import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.sys.entity.common.ComHibernateHbm;
import com.king.tooth.sys.entity.common.ComProject;
import com.king.tooth.sys.entity.common.ComProjectModule;
import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.sys.entity.common.ComTabledata;

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
	public static List<ComTabledata> getCoreTables(String dbType){
		List<ComTabledata> coreTables = new ArrayList<ComTabledata>(5);
		coreTables.add(new ComDatabase().toCreateTable(dbType));
		coreTables.add(new ComProject().toCreateTable(dbType));
		coreTables.add(new ComProjectModule().toCreateTable(dbType));
		coreTables.add(new ComHibernateHbm().toCreateTable(dbType));
		coreTables.add(new ComSqlScript().toCreateTable(dbType));
		return coreTables;
	}
	
	/**
	 * 核心表资源映射的InputStreams
	 * <p>
	 * 	例如：ComDatabase,ComSqlScript，当发布资源时，连接远程的数据库，并为其在本系统创建dataSource和sessionFactory，将这些资源的映射加入到sessionFactory中，方便数据交互
	 * </p>
	 */
	private transient static final List<InputStream> coreTableResourceMappingInputStreams = new ArrayList<InputStream>(5);
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
}
