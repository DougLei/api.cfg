package com.api.sys.controller.sys;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.api.annotation.Controller;
import com.api.annotation.RequestMapping;
import com.api.constants.OperDataTypeConstants;
import com.api.constants.ResourcePropNameConstants;
import com.api.plugins.ijson.IJson;
import com.api.sys.builtin.data.BuiltinParameterKeys;
import com.api.sys.builtin.data.BuiltinResourceInstance;
import com.api.sys.controller.AController;
import com.api.sys.entity.sys.SysUser;
import com.api.sys.service.sys.SysUserService;
import com.api.util.StrUtils;

/**
 * 人员信息表Controller
 * @author DougLei
 */
@Controller
public class SysUserController extends AController{

	/**
	 * 添加用户
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object add(HttpServletRequest request, IJson ijson){
		List<SysUser> users = getDataInstanceList(ijson, SysUser.class, true);
		analysisResourceProp(users, false);
		if(analysisResult == null){
			for (SysUser user : users) {
				resultObject = BuiltinResourceInstance.getInstance("SysUserService", SysUserService.class).saveUser(user);
				if(resultObject instanceof String){
					index++;
					resultObject = "第"+index+"个SysUser对象，" + resultObject;
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(users, null);
	}
	
	/**
	 * 修改用户
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping
	public Object update(HttpServletRequest request, IJson ijson){
		List<SysUser> users = getDataInstanceList(ijson, SysUser.class, true);
		analysisResourceProp(users, true);
		if(analysisResult == null){
			for (SysUser user : users) {
				resultObject = BuiltinResourceInstance.getInstance("SysUserService", SysUserService.class).updateUser(user);
				if(resultObject instanceof String){
					index++;
					resultObject = "第"+index+"个SysUser对象，" + resultObject;
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(users, null);
	}
	
	/**
	 * 删除用户
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping
	public Object delete(HttpServletRequest request, IJson ijson){
		String userIds = request.getParameter(BuiltinParameterKeys._IDS);
		if(StrUtils.isEmpty(userIds)){
			return "要删除的用户id不能为空";
		}
		
		String[] userIdArr = userIds.split(",");
		for (String userId : userIdArr) {
			resultObject = BuiltinResourceInstance.getInstance("SysUserService", SysUserService.class).deleteUser(userId);
			if(resultObject != null){
				break;
			}
		}
		processResultObject(BuiltinParameterKeys._IDS, userIds);
		return getResultObject(null, null);
	}
	
	/**
	 * 物理删除用户
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping
	public Object physicalDelete(HttpServletRequest request, IJson ijson){
//		if(!CurrentThreadContext.getCurrentAccountOnlineStatus().isUserAdministrator()){
//			return "只有用户管理账户，才能进行物理删除用户的操作";
//		}
		String userIds = request.getParameter(BuiltinParameterKeys._IDS);
		if(StrUtils.isEmpty(userIds)){
			return "要删除的用户id不能为空";
		}
		
		String[] userIdArr = userIds.split(",");
		for (String userId : userIdArr) {
			resultObject = BuiltinResourceInstance.getInstance("SysUserService", SysUserService.class).physicalDelete(userId);
			if(resultObject != null){
				break;
			}
		}
		processResultObject(BuiltinParameterKeys._IDS, userIds);
		return getResultObject(null, null);
	}
	
	/**
	 * 开通账户
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object openAccount(HttpServletRequest request, IJson ijson){
		List<SysUser> users = getDataInstanceList(ijson, SysUser.class, true);
		for (SysUser user : users) {
			resultObject = BuiltinResourceInstance.getInstance("SysUserService", SysUserService.class).openAccount(user);
			if(resultObject instanceof String){
				index++;
				resultObject = "第"+index+"个SysUser对象，" + resultObject;
				break;
			}
			resultJsonArray.add(resultObject);
		}
		return getResultObject(users, OperDataTypeConstants.EDIT);
	}
	
	/**
	 * 关闭账户
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object closeAccount(HttpServletRequest request, IJson ijson){
		List<SysUser> users = getDataInstanceList(ijson, SysUser.class, true);
		for (SysUser user : users) {
			resultObject = BuiltinResourceInstance.getInstance("SysUserService", SysUserService.class).closeAccount(user);
			if(resultObject instanceof String){
				index++;
				resultObject = "第"+index+"个SysUser对象，" + resultObject;
				break;
			}
			resultJsonArray.add(resultObject);
		}
		return getResultObject(users, OperDataTypeConstants.EDIT);
	}
	
	/**
	 * 重置用户关联账户的信息，即将用户的工号、手机号、邮箱三个字段的值，重新更新到账户的帐号、手机号、邮箱三个字段的值，同时重置账户的登陆密码为初始密码
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping
	public Object resetAccount(HttpServletRequest request, IJson ijson){
		JSONObject jsonObject = getJSONObject(ijson);
		if(StrUtils.isEmpty(jsonObject.getString(ResourcePropNameConstants.ID))){
			return "重置用户的账户信息时，传入的用户id不能为空";
		}
		resultObject = BuiltinResourceInstance.getInstance("SysUserService", SysUserService.class).resetAccount(jsonObject.getString(ResourcePropNameConstants.ID));
		return getResultObject(null, OperDataTypeConstants.EDIT);
	}
	
	/**
	 * 修改用户关联账户的登录密码
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping
	public Object updatePassword(HttpServletRequest request, IJson ijson){
		JSONObject jsonObject = getJSONObject(ijson);
		resultObject = BuiltinResourceInstance.getInstance("SysUserService", SysUserService.class).updatePassword(jsonObject);
		return getResultObject(null, OperDataTypeConstants.EDIT);
	}
	
	/**
	 * 修改当前用户关联账户的登录密码
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping
	public Object updatePasswordSelf(HttpServletRequest request, IJson ijson){
		JSONObject jsonObject = getJSONObject(ijson);
		resultObject = BuiltinResourceInstance.getInstance("SysUserService", SysUserService.class).updatePasswordSelf(request.getHeader("_token"), jsonObject);
		return getResultObject(null, OperDataTypeConstants.EDIT);
	}
	
	/**
	 * 不登录修改用户关联账户的登录密码
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping
	public Object updatePasswordSelfNoLogin(HttpServletRequest request, IJson ijson){
		JSONObject jsonObject = getJSONObject(ijson);
		resultObject = BuiltinResourceInstance.getInstance("SysUserService", SysUserService.class).updatePasswordSelfNoLogin(jsonObject);
		return getResultObject(null, OperDataTypeConstants.EDIT);
	}
	
	/**
	 * 重置用户关联账户的登陆密码
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping
	public Object resetPassword(HttpServletRequest request, IJson ijson){
		JSONObject jsonObject = getJSONObject(ijson);
		if(StrUtils.isEmpty(jsonObject.getString(ResourcePropNameConstants.ID))){
			return "要重置密码的用户id不能为空";
		}
		resultObject = BuiltinResourceInstance.getInstance("SysUserService", SysUserService.class).resetPassword(jsonObject.getString(ResourcePropNameConstants.ID));
		return getResultObject(null, OperDataTypeConstants.EDIT);
	}
	
	/**
	 * 更新指定用户的面部特征
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	public Object updateFaceFeature(HttpServletRequest request, IJson ijson){
		JSONObject jsonObject = getJSONObject(ijson);
		resultObject = BuiltinResourceInstance.getInstance("SysUserService", SysUserService.class).updateFaceFeature(jsonObject.getString("userId"));
		return getResultObject(null, OperDataTypeConstants.EDIT);
	}
}
