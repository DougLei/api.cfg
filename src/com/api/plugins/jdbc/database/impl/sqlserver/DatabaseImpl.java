package com.api.plugins.jdbc.database.impl.sqlserver;

import com.api.plugins.jdbc.database.impl.ADatabaseHandler;
import com.api.sys.entity.cfg.CfgDatabase;
import com.api.util.StrUtils;

/**
 * sqlserver创建表操作的实现类
 * @author DougLei
 */
public class DatabaseImpl extends ADatabaseHandler{

	public String installCreateDatabaseSql(CfgDatabase database) {
		operDatabaseSql.setLength(0);
		// 创建数据库文件
		operDatabaseSql.append("create database ").append(database.getInstanceName())
				       .append(" on primary (")
				       .append("name='").append(database.getMainFile().getName()).append("',")
				       .append("filename='").append(database.getMainFile().getFilepath()).append(database.getMainFile().getName()).append(".mdf',")
				       .append("size=").append(database.getMainFile().getSize()).append("mb,");
		if(database.getMainFile().getMaxSize() > 0){
			operDatabaseSql.append("maxsize=").append(database.getMainFile().getMaxSize()).append("mb,");
		}
		operDatabaseSql.append("filegrowth=").append(database.getMainFile().getFileGrowth()).append("mb")
				       .append(")");
		
		// 创建数据库日志文件
		if(database.getTmpLogFile() != null && StrUtils.notEmpty(database.getTmpLogFile().getName())){
			operDatabaseSql.append(" log on ( ")
						   .append("name='").append(database.getTmpLogFile().getName()).append("',")
						   .append("filename='").append(database.getTmpLogFile().getFilepath()).append(database.getTmpLogFile().getName()).append(".ldf',")
						   .append("size=").append(database.getTmpLogFile().getTmpSize()).append("mb,");
			if(database.getTmpLogFile().getTmpMaxSize() > 0){
				operDatabaseSql.append("maxsize=").append(database.getTmpLogFile().getTmpMaxSize()).append("mb,");
			}
			operDatabaseSql.append("filegrowth=").append(database.getTmpLogFile().getTmpFileGrowth()).append("mb")
						   .append(")");
		}
		
		// 切换到刚刚创建的数据库
		operDatabaseSql.append(";use ").append(database.getInstanceName()).append(";");
		// 创建登录账户
		operDatabaseSql.append("create login ")
		               .append(database.getLoginUserName())
		               .append(" with password='").append(database.getLoginPassword()).append("', default_database=")
		               .append(database.getInstanceName()).append(";");
		// 创建数据库用户
		operDatabaseSql.append("create user ")
					   .append("u_").append(database.getLoginUserName()).append(" for login ")
					   .append(database.getLoginUserName()).append(" with default_schema=dbo;");
		// 给用户授权
		operDatabaseSql.append("exec sp_addrolemember 'db_owner', '")
				       .append("u_").append(database.getLoginUserName()).append("'");
		return operDatabaseSql.toString();
	}

	public String installDropDatabaseSql(CfgDatabase database) {
		// 删除用户
//		operDatabaseSql.append("drop user ").append("u_").append(database.getLoginUserName()).append(";");
		operDatabaseSql.append("use master;");
		// 删除登录账户：在master数据库下，删除了登录账户和数据库，用户也就被删除了，因为用户是被绑定到数据库中的
		operDatabaseSql.append("drop login ").append(database.getLoginUserName()).append(";");
		// 删除数据库
		operDatabaseSql.append("drop database ").append(database.getInstanceName());
		return operDatabaseSql.toString();
	}
}
