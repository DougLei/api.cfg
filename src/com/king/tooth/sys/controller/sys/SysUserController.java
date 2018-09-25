package com.king.tooth.sys.controller.sys;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.controller.AbstractController;
import com.king.tooth.sys.entity.sys.SysUser;
import com.king.tooth.sys.service.sys.SysUserService;
import com.king.tooth.util.StrUtils;

/**
 * 人员信息表Controller
 * @author DougLei
 */
@Controller
public class SysUserController extends AbstractController{

	/**
	 * 添加用户
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object add(HttpServletRequest request, IJson ijson){
		List<SysUser> users = getDataInstanceList(ijson, SysUser.class, true);
		analysisResourceProp(users);
		if(analysisResult == null){
			if(users.size() == 1){
				resultObject = BuiltinResourceInstance.getInstance("SysUserService", SysUserService.class).saveUser(users.get(0));
			}else{
				for (SysUser user : users) {
					resultObject = BuiltinResourceInstance.getInstance("SysUserService", SysUserService.class).saveUser(user);
					if(resultObject instanceof String){
						break;
					}
					resultJsonArray.add((JSONObject) resultObject);
				}
			}
			users.clear();
		}
		return getResultObject();
	}
	
	/**
	 * 修改用户
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping
	public Object update(HttpServletRequest request, IJson ijson){
		List<SysUser> users = getDataInstanceList(ijson, SysUser.class, true);
		analysisResourceProp(users);
		if(analysisResult == null){
			if(users.size() == 1){
				resultObject = BuiltinResourceInstance.getInstance("SysUserService", SysUserService.class).updateUser(users.get(0));
			}else{
				for (SysUser user : users) {
					resultObject = BuiltinResourceInstance.getInstance("SysUserService", SysUserService.class).updateUser(user);
					if(resultObject instanceof String){
						break;
					}
					resultJsonArray.add((JSONObject) resultObject);
				}
			}
			users.clear();
		}
		return getResultObject();
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
		return getResultObject();
	}
	
	/**
	 * 开通账户
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object openAccount(HttpServletRequest request, IJson ijson){
		List<SysUser> users = getDataInstanceList(ijson, SysUser.class, true);
		if(users.size() == 1){
			resultObject = BuiltinResourceInstance.getInstance("SysUserService", SysUserService.class).openAccount(users.get(0));
		}else{
			for (SysUser user : users) {
				resultObject = BuiltinResourceInstance.getInstance("SysUserService", SysUserService.class).openAccount(user);
				if(resultObject instanceof String){
					break;
				}
				resultJsonArray.add((JSONObject) resultObject);
			}
		}
		users.clear();
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
			return "要修改密码的用户id不能为空";
		}
		if(StrUtils.isEmpty(jsonObject.getString("password"))){
			return "新密码不能为空";
		}
		resultObject = BuiltinResourceInstance.getInstance("SysUserService", SysUserService.class).uploadUserLoginPwd(jsonObject.getString(ResourcePropNameConstants.ID), jsonObject.getString("password"));
		return getResultObject();
	}
}
