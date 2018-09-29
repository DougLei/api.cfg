package com.king.tooth.sys.controller.sys;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.cache.TokenRefProjectIdMapping;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.controller.AController;
import com.king.tooth.sys.entity.sys.SysAccount;
import com.king.tooth.sys.entity.sys.SysAccountOnlineStatus;
import com.king.tooth.sys.service.sys.SysAccountService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.StrUtils;

/**
 * 账户表Controller
 * @author DougLei
 */
@Controller
public class SysAccountController extends AController{
	
	/**
	 * 登录
	 * <p>请求方式：POST</p>
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping
	public Object login(HttpServletRequest request, IJson ijson){
		CurrentThreadContext.getReqLogData().getReqLog().setType(1);// 标识为登陆日志
		
		SysAccount account = JsonUtil.toJavaObject(ijson.get(0), SysAccount.class);
		SysAccountOnlineStatus accountOnlineStatus = BuiltinResourceInstance.getInstance("SysAccountService", SysAccountService.class).login(request.getAttribute(BuiltinParameterKeys._CLIENT_IP).toString(), account.getLoginName(), account.getLoginPwd());
		if(accountOnlineStatus.getIsError() == 1){
			resultObject = accountOnlineStatus.getMessage();
		}else{
			// 登录成功时，记录token和项目id的关系
			TokenRefProjectIdMapping.setTokenRefProjMapping(accountOnlineStatus.getToken(), CurrentThreadContext.getProjectId());
			
			// 将(模块)权限信息组装到结果json中
			JSONObject json = JsonUtil.toJsonObject(accountOnlineStatus);
			json.put("modules", accountOnlineStatus.getProjectModules());
			resultObject = json;
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
	@RequestMapping
	public Object loginOut(HttpServletRequest request, IJson ijson){
		CurrentThreadContext.getReqLogData().getReqLog().setType(2);// 标识为退出登陆日志
		
		String token = request.getHeader("_token");
		BuiltinResourceInstance.getInstance("SysAccountService", SysAccountService.class).loginOut(token);
		
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
	@RequestMapping
	public Object add(HttpServletRequest request, IJson ijson){
		List<SysAccount> accounts = getDataInstanceList(ijson, SysAccount.class, true);
		analysisResourceProp(accounts);
		if(analysisResult == null){
			if(accounts.size() == 1){
				resultObject = BuiltinResourceInstance.getInstance("SysAccountService", SysAccountService.class).saveAccount(accounts.get(0));
			}else{
				for (SysAccount account : accounts) {
					resultObject = BuiltinResourceInstance.getInstance("SysAccountService", SysAccountService.class).saveAccount(account);
					if(resultObject instanceof String){
						break;
					}
					resultJsonArray.add((JSONObject) resultObject);
				}
			}
			accounts.clear();
		}
		return getResultObject();
	}
	
	/**
	 * 修改账户
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping
	public Object update(HttpServletRequest request, IJson ijson){
		List<SysAccount> accounts = getDataInstanceList(ijson, SysAccount.class, true);
		analysisResourceProp(accounts);
		if(analysisResult == null){
			if(accounts.size() == 1){
				resultObject = BuiltinResourceInstance.getInstance("SysAccountService", SysAccountService.class).updateAccount(accounts.get(0));
			}else{
				for (SysAccount account : accounts) {
					resultObject = BuiltinResourceInstance.getInstance("SysAccountService", SysAccountService.class).updateAccount(account);
					if(resultObject instanceof String){
						break;
					}
					resultJsonArray.add((JSONObject) resultObject);
				}
			}
			accounts.clear();
		}
		return getResultObject();
	}
	
	/**
	 * 删除账户
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping
	public Object delete(HttpServletRequest request, IJson ijson){
		String accountIds = request.getParameter(BuiltinParameterKeys._IDS);
		if(StrUtils.isEmpty(accountIds)){
			return "要删除的账户id不能为空";
		}
		
		String[] accountIdArr = accountIds.split(",");
		for (String accountId : accountIdArr) {
			resultObject = BuiltinResourceInstance.getInstance("SysAccountService", SysAccountService.class).deleteAccount(accountId);
			if(resultObject != null){
				break;
			}
		}
		processResultObject(BuiltinParameterKeys._IDS, accountIds);
		return getResultObject();
	}
	
	/**
	 * 修改密码
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object updatePassword(HttpServletRequest request, IJson ijson){
		JSONObject jsonObject = getJSONObject(ijson);
		if(StrUtils.isEmpty(jsonObject.getString(ResourcePropNameConstants.ID))){
			return "要修改密码的账户id不能为空";
		}
		if(StrUtils.isEmpty(jsonObject.getString("password"))){
			return "新密码不能为空";
		}
		resultObject = BuiltinResourceInstance.getInstance("SysAccountService", SysAccountService.class).uploadAccounLoginPwd(jsonObject.getString(ResourcePropNameConstants.ID), jsonObject.getString("password"));
		return getResultObject();
	}
}
