package com.king.tooth.sys.controller.sys;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinObjectInstance;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.controller.AbstractController;
import com.king.tooth.sys.entity.sys.SysUser;
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
	public Object add(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		List<SysUser> users = getDataInstanceList(ijson, SysUser.class);
		analysisResourceProp(users);
		if(analysisResult == null){
			if(users.size() == 1){
				resultObject = BuiltinObjectInstance.userService.saveUser(users.get(0));
			}else{
				for (SysUser user : users) {
					resultObject = BuiltinObjectInstance.userService.saveUser(user);
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
	 * 修改用户
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping
	public Object update(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		List<SysUser> users = getDataInstanceList(ijson, SysUser.class);
		analysisResourceProp(users);
		if(analysisResult == null){
			if(users.size() == 1){
				resultObject = BuiltinObjectInstance.userService.updateUser(users.get(0));
			}else{
				for (SysUser user : users) {
					resultObject = BuiltinObjectInstance.userService.updateUser(user);
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
	 * 删除用户
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping
	public Object delete(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		String userIds = request.getParameter(BuiltinParameterKeys._IDS);
		if(StrUtils.isEmpty(userIds)){
			return "要删除的用户id不能为空";
		}
		
		String[] userIdArr = userIds.split(",");
		for (String userId : userIdArr) {
			resultObject = BuiltinObjectInstance.userService.deleteUser(userId);
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
	public Object openAccount(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		List<SysUser> users = getDataInstanceList(ijson, SysUser.class);
		if(users.size() == 1){
			resultObject = BuiltinObjectInstance.userService.openAccount(users.get(0));
		}else{
			for (SysUser user : users) {
				resultObject = BuiltinObjectInstance.userService.openAccount(user);
				if(resultObject instanceof String){
					break;
				}
				resultJsonArray.add((JSONObject) resultObject);
			}
		}
		return getResultObject();
	}
	
	/**
	 * 修改密码
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object updatePassword(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		JSONObject jsonObject = getJSONObject(ijson);
		if(StrUtils.isEmpty(jsonObject.getString(ResourcePropNameConstants.ID))){
			return "要修改密码的用户id不能为空";
		}
		if(StrUtils.isEmpty(jsonObject.getString("password"))){
			return "新密码不能为空";
		}
		resultObject = BuiltinObjectInstance.userService.uploadUserLoginPwd(jsonObject.getString(ResourcePropNameConstants.ID), jsonObject.getString("password"));
		return getResultObject();
	}
}
