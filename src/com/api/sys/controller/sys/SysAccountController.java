package com.api.sys.controller.sys;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.codec.Base64;

import com.alibaba.fastjson.JSONObject;
import com.api.annotation.Controller;
import com.api.annotation.RequestMapping;
import com.api.cache.TokenRefProjectIdMapping;
import com.api.plugins.ijson.IJson;
import com.api.sys.builtin.data.BuiltinParameterKeys;
import com.api.sys.builtin.data.BuiltinResourceInstance;
import com.api.sys.controller.AController;
import com.api.sys.entity.sys.SysAccount;
import com.api.sys.entity.sys.SysAccountOnlineStatus;
import com.api.sys.entity.sys.SysReqLog;
import com.api.sys.service.sys.SysAccountService;
import com.api.thread.current.CurrentThreadContext;
import com.api.util.JsonUtil;
import com.api.util.StrUtils;

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
		CurrentThreadContext.getReqLogData().getReqLog().setType(SysReqLog.LOGIN);// 标识为登陆日志
		
		SysAccount account = JsonUtil.toJavaObject(ijson.get(0), SysAccount.class);
		SysAccountOnlineStatus accountOnlineStatus = BuiltinResourceInstance.getInstance("SysAccountService", SysAccountService.class).loginByUsernameAndPwd(request.getAttribute(BuiltinParameterKeys._CLIENT_IP).toString(), account.getLoginName(), Base64.decodeToString(account.getLoginPwd()));
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
		return getResultObject(null, null);
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
		CurrentThreadContext.getReqLogData().getReqLog().setType(SysReqLog.LOGIN_OUT);// 标识为退出登陆日志
		
		String token = request.getHeader("_token");
		BuiltinResourceInstance.getInstance("SysAccountService", SysAccountService.class).loginOut(token);
		
		JSONObject jsonObject = new JSONObject(1);
		jsonObject.put("_token", token);
		resultObject = jsonObject;
		return getResultObject(null, null);
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
		analysisResourceProp(accounts, false);
		if(analysisResult == null){
			for (SysAccount account : accounts) {
				resultObject = BuiltinResourceInstance.getInstance("SysAccountService", SysAccountService.class).saveAccount(account);
				if(resultObject instanceof String){
					index++;
					resultObject = "第"+index+"个SysAccount对象，" + resultObject;
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(accounts, null);
	}
	
	/**
	 * 修改账户
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping
	public Object update(HttpServletRequest request, IJson ijson){
		List<SysAccount> accounts = getDataInstanceList(ijson, SysAccount.class, true);
		analysisResourceProp(accounts, true);
		if(analysisResult == null){
			for (SysAccount account : accounts) {
				resultObject = BuiltinResourceInstance.getInstance("SysAccountService", SysAccountService.class).updateAccount(account);
				if(resultObject instanceof String){
					index++;
					resultObject = "第"+index+"个SysAccount对象，" + resultObject;
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(accounts, null);
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
		return getResultObject(null, null);
	}
	
	/**
	 * 物理删除账户
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping
	public Object physicalDelete(HttpServletRequest request, IJson ijson){
		if(!CurrentThreadContext.getCurrentAccountOnlineStatus().isUserAdministrator()){
			return "只有用户管理账户，才能进行物理删除账户的操作";
		}
		String accountIds = request.getParameter(BuiltinParameterKeys._IDS);
		if(StrUtils.isEmpty(accountIds)){
			return "要删除的账户id不能为空";
		}
		
		String[] accountIdArr = accountIds.split(",");
		for (String accountId : accountIdArr) {
			resultObject = BuiltinResourceInstance.getInstance("SysAccountService", SysAccountService.class).physicalDeleteAccount(accountId);
			if(resultObject != null){
				break;
			}
		}
		processResultObject(BuiltinParameterKeys._IDS, accountIds);
		return getResultObject(null, null);
	}
}
