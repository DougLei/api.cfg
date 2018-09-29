package com.king.tooth.plugins.jdbc.database.impl.oracle;

import com.king.tooth.plugins.jdbc.database.impl.ADatabaseHandler;
import com.king.tooth.sys.entity.cfg.CfgDatabase;
import com.king.tooth.util.StrUtils;

/**
 * oracle创建表操作的实现类
 * @author DougLei
 */
public class DatabaseImpl extends ADatabaseHandler{
	
	public String installCreateDatabaseSql(CfgDatabase database) {
		operDatabaseSql.setLength(0);
		// 如果有，先创建临时表空间
		if(database.getTmpLogFile() != null && StrUtils.notEmpty(database.getTmpLogFile().getName())){
			operDatabaseSql.append("create temporary tablespace ").append(database.getTmpLogFile().getName())
			  			   .append(" tempfile '").append(database.getTmpLogFile().getFilepath()).append(database.getTmpLogFile().getName()).append(".dbf'")
			  			   .append(" size ").append(database.getTmpLogFile().getTmpSize()).append("m")
			  			   .append(" autoextend on next ").append(database.getTmpLogFile().getTmpFileGrowth()).append("m");
			if(database.getTmpLogFile().getTmpMaxSize() > 0){
				operDatabaseSql.append(" maxsize ").append(database.getTmpLogFile().getTmpMaxSize()).append("m");
			}
			operDatabaseSql.append(" extent management local;");
		}
		// 创建表空间
		operDatabaseSql.append("create tablespace ").append(database.getMainFile().getName())
					   .append(" logging datafile '").append(database.getMainFile().getFilepath()).append(database.getMainFile().getName()).append(".dbf'")
					   .append(" size ").append(database.getMainFile().getSize()).append("m")
					   .append(" autoextend on next ").append(database.getMainFile().getFileGrowth()).append("m");
		if(database.getMainFile().getTmpMaxSize() > 0){
			operDatabaseSql.append(" maxsize ").append(database.getMainFile().getMaxSize()).append("m");
		}
		operDatabaseSql.append(" extent management local;");
		
		// 创建用户
		operDatabaseSql.append("create user ").append(database.getLoginUserName()).append(" identified by \"").append(database.getLoginPassword()).append("\" ")
					   .append(" default tablespace ").append(database.getMainFile().getName());
		if(database.getTmpLogFile() != null && StrUtils.notEmpty(database.getTmpLogFile().getName())){
			operDatabaseSql.append(" temporary tablespace ").append(database.getTmpLogFile().getName());
		}
		operDatabaseSql.append(";");
		// 给用户赋权
		operDatabaseSql.append("grant connect,resource,dba to ").append(database.getLoginUserName());
		return operDatabaseSql.toString();
	}

	public String installDropDatabaseSql(CfgDatabase database) {
		// 删除用户
		operDatabaseSql.append("drop user ").append(database.getLoginUserName()).append(" cascade;");
		// 删除表空间
		operDatabaseSql.append("drop tablespace ").append(database.getMainFile().getName()).append(" including contents and datafiles");
		// 如果有，再删除临时表空间
		if(database.getTmpLogFile() != null && StrUtils.notEmpty(database.getTmpLogFile().getName())){
			operDatabaseSql.append(";drop tablespace ").append(database.getTmpLogFile().getName()).append(" including contents and datafiles");
		}
		return operDatabaseSql.toString();
	}
}
