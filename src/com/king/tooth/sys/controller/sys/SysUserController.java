package com.king.tooth.sys.controller.sys;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.constants.OperDataTypeConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.controller.AController;
import com.king.tooth.sys.entity.sys.SysUser;
import com.king.tooth.sys.service.sys.SysUserService;
import com.king.tooth.util.StrUtils;

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
		return getResultObject(users, OperDataTypeConstants.ADD);
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
		return getResultObject(users, OperDataTypeConstants.EDIT);
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
}
