package com.king.tooth.plugins.builtin.params;

import java.util.Date;

import com.king.tooth.plugins.thread.CurrentThreadContext;

/**
 * 系统内置的查询参数
 * @author DougLei
 */
public class BuiltinQueryParameters {
	
	/**
	 * 当前时间
	 */
	private static final String currentDate = "_currentDate";
	/**
	 * 当前租户id
	 */
	private static final String currentCustomerId = "_currentCustomerId";
	/**
	 * 当前的项目id
	 */
	private static final  String currentProjectId = "_currentProjectId";
	/**
	 * 当前的账户id
	 */
	private static final String accountId = "_accountId";
	/**
	 * 当前账户名
	 */
	private static final String accountName = "_accountName";
	/**
	 * 当前用户id
	 */
	private static final String currentUserId = "_currentUserId";
	/**
	 * 当前用户所属组织id
	 */
	private static final String currentOrgId = "_currentOrgId";
	/**
	 * 当前用户所属部门id
	 */
	private static final String currentDeptId = "_currentDeptId";
	/**
	 * 当前用户所属岗位id
	 */
	private static final String currentPositionId = "_currentPositionId";
	
	/**
	 * 判断参数是否是系统内置参数
	 * @param parameterName
	 * @return
	 */
	public static boolean isBuiltinQueryParams(String parameterName) {
		if(currentDate.equalsIgnoreCase(parameterName)
				|| currentCustomerId.equalsIgnoreCase(parameterName) 
				|| currentProjectId.equalsIgnoreCase(parameterName)
				|| accountId.equalsIgnoreCase(parameterName)
				|| accountName.equalsIgnoreCase(parameterName)
				|| currentUserId.equalsIgnoreCase(parameterName)
				|| currentOrgId.equalsIgnoreCase(parameterName)
				|| currentDeptId.equalsIgnoreCase(parameterName)
				|| currentPositionId.equalsIgnoreCase(parameterName)){
			return true;
		}
		return false;
	}
	
	/**
	 * 获取内置参数对应的值
	 * @param parameterName
	 * @return
	 */
	public static Object getBuiltinQueryParamValue(String parameterName) {
		if(currentDate.equalsIgnoreCase(parameterName)){
			return new Date();
		}
		if(currentCustomerId.equalsIgnoreCase(parameterName)){
			return CurrentThreadContext.getCurrentAccountOnlineStatus().getCurrentCustomerId();
		}
		if(currentProjectId.equalsIgnoreCase(parameterName)){
			return CurrentThreadContext.getCurrentAccountOnlineStatus().getCurrentProjectId();
		}
		if(accountId.equalsIgnoreCase(parameterName)){
			return CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId();
		}
		if(accountName.equalsIgnoreCase(parameterName)){
			return CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountName();
		}
		if(currentUserId.equalsIgnoreCase(parameterName)){
			return CurrentThreadContext.getCurrentAccountOnlineStatus().getCurrentUserId();
		}
		if(currentOrgId.equalsIgnoreCase(parameterName)){
			return CurrentThreadContext.getCurrentAccountOnlineStatus().getCurrentOrgId();
		}
		if(currentDeptId.equalsIgnoreCase(parameterName)){
			return CurrentThreadContext.getCurrentAccountOnlineStatus().getCurrentDeptId();
		}
		if(currentPositionId.equalsIgnoreCase(parameterName)){
			return CurrentThreadContext.getCurrentAccountOnlineStatus().getCurrentPositionId();
		}
		throw new IllegalArgumentException("没有匹配到内置参数["+parameterName+"]");
	}
}
