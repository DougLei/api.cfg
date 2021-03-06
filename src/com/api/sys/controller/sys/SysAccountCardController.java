package com.api.sys.controller.sys;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.api.annotation.Controller;
import com.api.annotation.RequestMapping;
import com.api.cache.TokenRefProjectIdMapping;
import com.api.plugins.ijson.IJson;
import com.api.sys.builtin.data.BuiltinParameterKeys;
import com.api.sys.builtin.data.BuiltinResourceInstance;
import com.api.sys.controller.AController;
import com.api.sys.entity.sys.SysAccountCard;
import com.api.sys.entity.sys.SysAccountOnlineStatus;
import com.api.sys.entity.sys.SysReqLog;
import com.api.sys.entity.sys.accountcard.AccountCardAndUserRelation;
import com.api.sys.service.sys.SysAccountCardService;
import com.api.thread.current.CurrentThreadContext;
import com.api.util.JsonUtil;
import com.api.util.StrUtils;

/**
 * 账户卡表Controller
 * @author DougLei
 */
@Controller
public class SysAccountCardController extends AController{
	
	/**
	 * 登录
	 * <p>请求方式：POST</p>
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping
	public Object loginByCert(HttpServletRequest request, IJson ijson){
		CurrentThreadContext.getReqLogData().getReqLog().setType(SysReqLog.LOGIN);// 标识为登陆日志
		
		String cert = ijson.get(0).getString("cert");
		SysAccountOnlineStatus accountOnlineStatus = BuiltinResourceInstance.getInstance("SysAccountCardService", SysAccountCardService.class).loginByCert(request.getAttribute(BuiltinParameterKeys._CLIENT_IP).toString(), cert);
		
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
	 * 登录
	 * <p>请求方式：POST</p>
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping
	public Object loginById(HttpServletRequest request, IJson ijson){
		CurrentThreadContext.getReqLogData().getReqLog().setType(SysReqLog.LOGIN);// 标识为登陆日志
		
		SysAccountCard accountCard = JsonUtil.toJavaObject(ijson.get(0), SysAccountCard.class);
		SysAccountOnlineStatus accountOnlineStatus = BuiltinResourceInstance.getInstance("SysAccountCardService", SysAccountCardService.class).loginById(request.getAttribute(BuiltinParameterKeys._CLIENT_IP).toString(), accountCard.getId());
		
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
	 * 登录
	 * <p>请求方式：POST</p>
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping
	public Object login(HttpServletRequest request, IJson ijson){
		CurrentThreadContext.getReqLogData().getReqLog().setType(SysReqLog.LOGIN);// 标识为登陆日志
		
		SysAccountCard accountCard = JsonUtil.toJavaObject(ijson.get(0), SysAccountCard.class);
		SysAccountOnlineStatus accountOnlineStatus = BuiltinResourceInstance.getInstance("SysAccountCardService", SysAccountCardService.class).loginByCard(request.getAttribute(BuiltinParameterKeys._CLIENT_IP).toString(), accountCard);
		
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
	
	// ---------------------------------------------------------------------------------
	/**
	 * 添加账户卡
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object add(HttpServletRequest request, IJson ijson){
		List<SysAccountCard> accountCards = getDataInstanceList(ijson, SysAccountCard.class, true);
		analysisResourceProp(accountCards, false);
		if(analysisResult == null){
			for (SysAccountCard accountCard : accountCards) {
				resultObject = BuiltinResourceInstance.getInstance("SysAccountCardService", SysAccountCardService.class).saveAccountCard(accountCard);
				if(resultObject instanceof String){
					index++;
					resultObject = "第"+index+"个SysAccountCard对象，" + resultObject;
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(accountCards, null);
	}
	
	/**
	 * 修改账户卡
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping
	public Object update(HttpServletRequest request, IJson ijson){
		List<SysAccountCard> accountCards = getDataInstanceList(ijson, SysAccountCard.class, true);
		analysisResourceProp(accountCards, true);
		if(analysisResult == null){
			for (SysAccountCard accountCard : accountCards) {
				resultObject = BuiltinResourceInstance.getInstance("SysAccountCardService", SysAccountCardService.class).updateAccountCard(accountCard);
				if(resultObject instanceof String){
					index++;
					resultObject = "第"+index+"个SysAccountCard对象，" + resultObject;
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(accountCards, null);
	}
	
	/**
	 * 删除账户卡
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping
	public Object delete(HttpServletRequest request, IJson ijson){
		String accountCardIds = request.getParameter(BuiltinParameterKeys._IDS);
		if(StrUtils.isEmpty(accountCardIds)){
			return "要删除的账户卡id不能为空";
		}
		resultObject = BuiltinResourceInstance.getInstance("SysAccountCardService", SysAccountCardService.class).deleteAccountCard(accountCardIds);
		processResultObject(BuiltinParameterKeys._IDS, accountCardIds);
		return getResultObject(null, null);
	}
	
	/**
	 * 关联账户卡和用户的关系
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object addCardAndUserRelation(HttpServletRequest request, IJson ijson){
		List<AccountCardAndUserRelation> acurs = getDataInstanceList(ijson, AccountCardAndUserRelation.class, true);
		analysisResourceProp(acurs, false);
		if(analysisResult == null){
			for (AccountCardAndUserRelation acur : acurs) {
				resultObject = BuiltinResourceInstance.getInstance("SysAccountCardService", SysAccountCardService.class).addCardAndUserRelation(acur);
				if(resultObject instanceof String){
					index++;
					resultObject = "第"+index+"个AccountCardAndUserRelation对象，" + resultObject;
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(acurs, null);
	}
	
	/**
	 * 取消关联账户卡和用户的关系
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object cancelCardAndUserRelation(HttpServletRequest request, IJson ijson){
		List<AccountCardAndUserRelation> acurs = getDataInstanceList(ijson, AccountCardAndUserRelation.class, true);
		analysisResourceProp(acurs, false);
		if(analysisResult == null){
			for (AccountCardAndUserRelation acur : acurs) {
				resultObject = BuiltinResourceInstance.getInstance("SysAccountCardService", SysAccountCardService.class).cancelCardAndUserRelation(acur);
				if(resultObject instanceof String){
					index++;
					resultObject = "第"+index+"个AccountCardAndUserRelation对象，" + resultObject;
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(acurs, null);
	}
}
