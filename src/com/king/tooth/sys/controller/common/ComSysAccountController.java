package com.king.tooth.sys.controller.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.cache.TokenRefProjectIdMapping;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.controller.AbstractController;
import com.king.tooth.sys.entity.common.ComSysAccount;
import com.king.tooth.sys.entity.common.ComSysAccountOnlineStatus;
import com.king.tooth.sys.service.common.ComSysAccountService;
import com.king.tooth.util.HttpHelperUtil;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.StrUtils;

/**
 * 系统账户资源对象控制器
 * @author DougLei
 */
public class ComSysAccountController extends AbstractController{
	
	private ComSysAccountService accountService = new ComSysAccountService();
	
	/**
	 * 登录
	 * <p>请求方式：POST</p>
	 * @param request
	 * @param json
	 * @return
	 */
	public Object login(HttpServletRequest request, String json){
		ComSysAccount account = JsonUtil.parseObject(json, ComSysAccount.class);
		ComSysAccountOnlineStatus accountOnlineStatus = accountService.login(HttpHelperUtil.getClientIp(request), account.getLoginName(), account.getLoginPwd());
		if(accountOnlineStatus.getIsError() == 1){
			resultObject = accountOnlineStatus.getMessage();
		}else{
			// 登录成功时，记录token和项目id的关系
			TokenRefProjectIdMapping.setTokenRefProjMapping(accountOnlineStatus.getToken(), CurrentThreadContext.getProjectId());
			resultObject = JsonUtil.toJsonObject(accountOnlineStatus);
		}
		return getResultObject();
	}
	
	/**
	 * 退出
	 * <p>请求方式：POST</p>
	 * @param request
	 * @param json
	 * @return
	 */
	public Object loginOut(HttpServletRequest request, String json){
		String token = request.getHeader("_token");
		accountService.loginOut(token);
		
		JSONObject jsonObject = new JSONObject(1);
		jsonObject.put("_token", token);
		resultObject = jsonObject;
		return getResultObject();
	}
	
	//------------------------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * 添加账户
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object add(HttpServletRequest request, String json){
		List<ComSysAccount> accounts = getDataInstanceList(json, ComSysAccount.class);
		analysisResourceProp(accounts);
		if(analysisResult == null){
			if(accounts.size() == 1){
				resultObject = accountService.saveAccount(accounts.get(0));
			}else{
				for (ComSysAccount account : accounts) {
					resultObject = accountService.saveAccount(account);
					if(resultObject instanceof String){
						break;
					}
					resultJsonArray.add((JSONObject) resultObject);
				}
			}
		}
		return getResultObject();
	}
	
	/**
	 * 修改账户
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	public Object update(HttpServletRequest request, String json){
		List<ComSysAccount> accounts = getDataInstanceList(json, ComSysAccount.class);
		analysisResourceProp(accounts);
		if(analysisResult == null){
			if(accounts.size() == 1){
				resultObject = accountService.updateAccount(accounts.get(0));
			}else{
				for (ComSysAccount account : accounts) {
					resultObject = accountService.updateAccount(account);
					if(resultObject instanceof String){
						break;
					}
					resultJsonArray.add((JSONObject) resultObject);
				}
			}
		}
		return getResultObject();
	}
	
	/**
	 * 删除账户
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	public Object delete(HttpServletRequest request, String json){
		String accountIds = request.getParameter(ResourceNameConstants.IDS);
		if(StrUtils.isEmpty(accountIds)){
			return "要删除的账户id不能为空";
		}
		
		String[] accountIdArr = accountIds.split(",");
		for (String accountId : accountIdArr) {
			resultObject = accountService.deleteAccount(accountId);
			if(resultObject != null){
				break;
			}
		}
		processResultObject(ResourceNameConstants.IDS, accountIds);
		return getResultObject();
	}
	
	/**
	 * 修改密码
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object updatePassword(HttpServletRequest request, String json){
		JSONObject jsonObject = getJSONObject(json);
		if(StrUtils.isEmpty(jsonObject.getString(ResourceNameConstants.ID))){
			return "要修改密码的账户id不能为空";
		}
		if(StrUtils.isEmpty(jsonObject.getString("password"))){
			return "新密码不能为空";
		}
		resultObject = accountService.uploadAccounLoginPwd(null, jsonObject.getString(ResourceNameConstants.ID), jsonObject.getString("password"));
		return getResultObject();
	}
}
