package com.king.tooth.sys.service.cfg;

import com.king.tooth.annotation.Service;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.constants.SqlStatementTypeConstants;
import com.king.tooth.sys.entity.cfg.CfgDatabase;
import com.king.tooth.sys.service.AService;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 数据库信息表Service
 * @author DougLei
 */
@Service
public class CfgDatabaseService extends AService {
	
	/**
	 * 验证数据库数据是否存在
	 * @param database
	 * @return operResult
	 */
	private String validDatabaseDataIsExists(CfgDatabase database) {
		String hql = "select count("+ResourcePropNameConstants.ID+") from CfgDatabase where dbType=? and dbInstanceName=? and loginUserName=? and loginPassword=? and dbIp=? and dbPort=?";
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr(hql, database.getType(), database.getInstanceName(), database.getLoginUserName(), database.getLoginPassword(), database.getIp(), database.getPort()+"");
		if(count > 0){
			return "[dbType="+database.getType()+" ， dbInstanceName="+database.getInstanceName()+" ， loginUserName="+database.getLoginUserName()+" ， loginPassword="+database.getLoginPassword()+" ， dbIp="+database.getIp()+" ， dbPort="+database.getPort()+"]的数据库连接信息已存在";
		}
		return null;
	}
	
	/**
	 * 保存数据库
	 * @param database
	 * @return operResult
	 */
	public Object saveDatabase(CfgDatabase database) {
		String operResult = validDatabaseDataIsExists(database);
		if(operResult == null){
			return HibernateUtil.saveObject(database, null);
		}
		return operResult;
	}

	/**
	 * 修改数据库
	 * @param database
	 * @return operResult
	 */
	public Object updateDatabase(CfgDatabase database) {
		CfgDatabase oldDatabase = getObjectById(database.getId(), CfgDatabase.class);
		
		String operResult = null;
		boolean databaseLinkInfoIsSame = oldDatabase.compareLinkInfoIsSame(database);
		if(!databaseLinkInfoIsSame){// 如果修改了连接信息
			operResult = validDatabaseDataIsExists(database);
		}
		if(operResult == null){
			return HibernateUtil.updateObject(database, null);
		}
		return operResult;
	}
	
	/**
	 * 删除数据库
	 * @param databaseId
	 * @return
	 */
	public String deleteDatabase(String databaseId) {
		getObjectById(databaseId, CfgDatabase.class);
		
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from CfgProject where refDatabaseId = ?", databaseId);
		if(count > 0){
			return "该数据库下还存在项目，无法删除，请先删除相关项目";
		}
		HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.DELETE, "delete CfgDatabase where "+ResourcePropNameConstants.ID+" = '"+databaseId+"'");
		return null;
	}
	
	/**
	 * 测试数据库连接
	 * @param databaseId
	 */
	public String databaseLinkTest(String databaseId) {
		CfgDatabase database = getObjectById(databaseId, CfgDatabase.class);
		return database.testDbLink();
	}
}
