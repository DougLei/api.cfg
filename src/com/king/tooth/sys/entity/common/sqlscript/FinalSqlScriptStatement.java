package com.king.tooth.sys.entity.common.sqlscript;

import java.io.Serializable;

import com.king.tooth.util.StrUtils;

/**
 * 最终的sql脚本对象
 * @see ComSqlScript使用到
 * @author DougLei
 */
@SuppressWarnings("serial")
public class FinalSqlScriptStatement implements Serializable{
	/**
	 * 是否是修改sql脚本
	 */
	private boolean isUpdateSqlScript;
	/**
	 * 是否是添加sql脚本
	 */
	private boolean isInsertSqlScript;
	/**
	 * 是否是删除sql脚本
	 */
	private boolean isDeleteSqlScript;
	/**
	 * 是否是查询sql脚本
	 */
	private boolean isSelectSqlScript;
	/**
	 * 是否是存储过程
	 */
	private boolean isProcedure;
	
	
	//select sql专用属性---------------------------------------------------------------------
	/**
	 * 最终的select sql脚本语句
	 */
	private String finalSelectSqlScript;
	/**
	 * 处理过的，最终的with脚本语句
	 * <p>select类型的脚本语句专用属性</p>
	 */
	private String finalCteSql;
	//------------------------------------------------------------------------------------

	
	
	//insert/update/delete sql专用属性---------------------------------------------------------------------
	/**
	 * 最终的修改sql脚本语句数组
	 */
	private String[] finalModifySqlArr;
	//------------------------------------------------------------------------------------
	
	
	public void setFinalSelectSqlScript(String finalSelectSqlScript) {
		this.finalSelectSqlScript = finalSelectSqlScript;
	}
	public String getFinalSelectSqlScript() {
		return finalSelectSqlScript;
	}
	public String getFinalCteSql() {
		if(StrUtils.isEmpty(finalCteSql)){
			return "";
		}
		return finalCteSql;
	}
	public void setFinalCteSql(String finalCteSql) {
		this.finalCteSql = finalCteSql;
	}
	
	public boolean getIsSelectSqlScript() {
		return isSelectSqlScript;
	}
	public void setIsSelectSqlScript(boolean isSelectSqlScript) {
		this.isSelectSqlScript = isSelectSqlScript;
	}
	public boolean getIsUpdateSqlScript() {
		return isUpdateSqlScript;
	}
	public void setIsUpdateSqlScript(boolean isUpdateSqlScript) {
		this.isUpdateSqlScript = isUpdateSqlScript;
	}
	public boolean getIsInsertSqlScript() {
		return isInsertSqlScript;
	}
	public void setIsInsertSqlScript(boolean isInsertSqlScript) {
		this.isInsertSqlScript = isInsertSqlScript;
	}
	public boolean setIsDeleteSqlScript() {
		return isDeleteSqlScript;
	}
	public void setIsDeleteSqlScript(boolean isDeleteSqlScript) {
		this.isDeleteSqlScript = isDeleteSqlScript;
	}
	public boolean getIsProcedure() {
		return isProcedure;
	}
	public void setIsProcedure(boolean isProcedure) {
		this.isProcedure = isProcedure;
	}
	public String[] getFinalModifySqlArr() {
		return finalModifySqlArr;
	}
	public void setFinalModifySqlArr(String[] finalModifySqlArr) {
		this.finalModifySqlArr = finalModifySqlArr;
	}
}
