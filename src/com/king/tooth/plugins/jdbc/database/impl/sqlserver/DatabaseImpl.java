package com.king.tooth.plugins.jdbc.database.impl.sqlserver;

import com.king.tooth.plugins.jdbc.database.AbstractDatabaseHandler;
import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.util.StrUtils;

/**
 * sqlserver创建表操作的实现类
 * @author DougLei
 */
public class DatabaseImpl extends AbstractDatabaseHandler{

	public String installCreateDatabaseSql(ComDatabase database) {
		// 创建数据库文件
		operDatabaseSql.append("create database ").append(database.getDbInstanceName())
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
		operDatabaseSql.append(";use ").append(database.getDbInstanceName()).append(";");
		// 创建登录账户
		operDatabaseSql.append("create login ")
		               .append(database.getLoginUserName())
		               .append(" with password='").append(database.getLoginPassword()).append("', default_database=")
		               .append(database.getDbInstanceName()).append(";");
		// 创建数据库用户
		operDatabaseSql.append("create user ")
					   .append("u_").append(database.getLoginUserName()).append(" for login ")
					   .append(database.getLoginUserName()).append(" with default_schema=dbo;");
		// 给用户授权
		operDatabaseSql.append("exec sp_addrolemember 'db_owner', '")
				       .append("u_").append(database.getLoginUserName()).append("'");
		return operDatabaseSql.toString();
	}

	public String installDropDatabaseSql(ComDatabase database) {
		// 删除用户
		operDatabaseSql.append("drop user ").append("u_").append(database.getLoginUserName()).append(";");
		// 删除登录账户
		operDatabaseSql.append("drop login ").append(database.getLoginUserName()).append(";");
		// 删除数据库
		operDatabaseSql.append("drop database").append(database.getDbInstanceName());
		return operDatabaseSql.toString();
	}
}
