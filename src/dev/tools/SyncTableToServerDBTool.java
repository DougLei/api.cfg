package dev.tools;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.king.tooth.constants.ResourceInfoConstants;
import com.king.tooth.plugins.jdbc.DBLink;
import com.king.tooth.plugins.jdbc.table.DBTableHandler;
import com.king.tooth.sys.entity.cfg.CfgDatabase;
import com.king.tooth.sys.entity.cfg.CfgHibernateHbm;
import com.king.tooth.sys.entity.cfg.CfgResource;
import com.king.tooth.sys.entity.cfg.CfgTable;
import com.king.tooth.sys.entity.sys.SysAccountCard;
import com.king.tooth.sys.entity.sys.SysAccountOnlineStatus;
import com.king.tooth.sys.service.AService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.CloseUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.hibernate.HibernateHbmUtil;

/**
 * 同步表到服务器数据库的工具类
 * @author DougLei
 */
public final class SyncTableToServerDBTool extends AService{
	
	public static void main(String[] args) {
		syncTablesToService(
				new SysAccountCard().toCreateTable(),
				new SysAccountOnlineStatus().toCreateTable()
				);
	}
	
	/**
	 * 同步表到服务器
	 * <p>解决每次开发新加的表，后续还要手工到服务器数据库中建表、加建模数据</p>
	 * @param tables
	 */
	private static void syncTablesToService(CfgTable... tables){
		if(tables == null || tables.length == 0){
			return;
		}
		// 服务器数据库对象
		CfgDatabase serviceDatabaseInstance = new CfgDatabase();
		// 服务器数据库连接信息
		serviceDatabaseInstance.setType("sqlserver");
		serviceDatabaseInstance.setInstanceName("SmartOneCfg");
		serviceDatabaseInstance.setLoginUserName("sa");
		serviceDatabaseInstance.setLoginPassword("123_abc");
		serviceDatabaseInstance.setIp("192.168.1.111");
		serviceDatabaseInstance.setPort(1433);
		
		// 数据库表处理对象【根据服务器数据库对象获取】
		DBTableHandler dbTableHandler = new DBTableHandler(serviceDatabaseInstance);
		// 数据库数据处理对象【根据服务器数据库对象获取】
		DBLink dblink = new DBLink(serviceDatabaseInstance);
		
		Connection conn = null;
		Statement st = null;
		PreparedStatement pst = null;
		try {
			conn = dblink.getConnection();
			st = conn.createStatement();
			pst = conn.prepareStatement("update cfg_hibernate_hbm set content = ? where id = ?");
			
			String hbmId;
			CfgHibernateHbm hbm;
			CfgResource resource;
			for (CfgTable table : tables) {
				if(dbTableHandler.filterTable(true, table.getTableName()).size() == 1){
					System.err.println("表名为["+table.getTableName()+"]的表，在服务器数据库中已经存在，不能能再进行操作");
					continue;
				}
				
				// 1、建表
				dbTableHandler.createTable(table, true);
				
				// 2、获取hbm文件， 组装hbm对象，获取插入hbm数据的sql，并执行
				hbm = new CfgHibernateHbm(table);
				hbm.setRefDatabaseId(CurrentThreadContext.getDatabaseId());
				hbm.setContent(HibernateHbmUtil.createHbmMappingContent(table, false, "C:\\devlopment\\api\\projects\\api.cfg\\resources\\hibernateMapping\\template\\hibernate.hbm.xml.ftl"));
				hbmId = executeInsertCfgHibernateHbmSql(st, hbm, table.getResourceName());
				
				// 3、获取插入资源数据的sql，并执行
				resource = table.turnToResource();
				executeInsertCfgResourceSql(st, resource);
				
				// 5.将hbm的内容也保存到对应数据的hbmContent字段中
				pst.setClob(1, new StringReader(hbm.getContent()));
				pst.setString(2, hbmId);
				pst.executeUpdate();
				
				// 6.清除不用的数据
				table.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			CloseUtil.closeDBConn(pst, st, conn);
		}
		System.out.println("******同步表到服务器数据库完成******");
	}
	
	/**
	 * 获取插入CfgHibernateHbm表的sql语句，并执行
	 * @param st
	 * @param hbm
	 * @param resourceName 
	 * @return
	 * @throws SQLException 
	 */
	private static String executeInsertCfgHibernateHbmSql(Statement st, CfgHibernateHbm hbm, String resourceName) throws SQLException {
		String id = ResourceHandlerUtil.getIdentity();
		// 先尝试删除之前的数据，再添加新的数据
		st.executeUpdate("delete cfg_hibernate_hbm where resource_name = '"+resourceName+"'");
		st.executeUpdate("insert into cfg_hibernate_hbm(ref_database_id, ref_table_id, resource_name, id, customer_id, project_id, create_date, last_update_date, create_user_id, last_update_user_id) values('5k7f1ef02728y7018f9df0e9edcr8d37','"+ResourceInfoConstants.BUILTIN_RESOURCE+"', '"+hbm.getResourceName()+"','"+id+"','unknow','90621e37b806o6fe8538c5eb782901bb', getdate(), getdate(), '16ed21bd7a7a41f5bea2ebaa258908cf', '16ed21bd7a7a41f5bea2ebaa258908cf')");
		return id;
	}
	
	/**
	 * 获取插入CfgResource表的sql语句，并执行
	 * @param st
	 * @param resource
	 * @return
	 * @throws SQLException 
	 */
	private static String executeInsertCfgResourceSql(Statement st, CfgResource resource) throws SQLException {
		String id = ResourceHandlerUtil.getIdentity();
		// 先尝试删除之前的数据，再添加新的数据
		st.executeUpdate("delete cfg_resource where resource_name = '"+resource.getResourceName()+"'");
		st.executeUpdate("insert into cfg_resource(ref_resource_id, resource_name, resource_type, id, customer_id, project_id, is_enabled, request_method, create_date, last_update_date, create_user_id, last_update_user_id) values('"+ResourceInfoConstants.BUILTIN_RESOURCE+"','"+resource.getResourceName()+"',1,'"+id+"','unknow','90621e37b806o6fe8538c5eb782901bb',1, 'all', getdate(), getdate(), '16ed21bd7a7a41f5bea2ebaa258908cf', '16ed21bd7a7a41f5bea2ebaa258908cf')");
		return id;
	}
}