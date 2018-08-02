package com.king.tooth.sys.controller.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinInstance;
import com.king.tooth.sys.controller.AbstractController;
import com.king.tooth.sys.entity.common.ComUser;
import com.king.tooth.util.StrUtils;

/**
 * 人员资源对象控制器
 * @author DougLei
 */
public class ComUserController extends AbstractController{

	/**
	 * 添加用户
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object add(HttpServletRequest request, IJson ijson){
		List<ComUser> users = getDataInstanceList(ijson, ComUser.class);
		analysisResourceProp(users);
		if(analysisResult == null){
			if(users.size() == 1){
				resultObject = BuiltinInstance.userService.saveUser(users.get(0));
			}else{
				for (ComUser user : users) {
					resultObject = BuiltinInstance.userService.saveUser(user);
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
	public Object update(HttpServletRequest request, IJson ijson){
		List<ComUser> users = getDataInstanceList(ijson, ComUser.class);
		analysisResourceProp(users);
		if(analysisResult == null){
			if(users.size() == 1){
				resultObject = BuiltinInstance.userService.updateUser(users.get(0));
			}else{
				for (ComUser user : users) {
					resultObject = BuiltinInstance.userService.updateUser(user);
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
	public Object delete(HttpServletRequest request, IJson ijson){
		String userIds = request.getParameter(ResourceNameConstants.IDS);
		if(StrUtils.isEmpty(userIds)){
			return "要删除的用户id不能为空";
		}
		
		String[] userIdArr = userIds.split(",");
		for (String userId : userIdArr) {
			resultObject = BuiltinInstance.userService.deleteUser(userId);
			if(resultObject != null){
				break;
			}
		}
		processResultObject(ResourceNameConstants.IDS, userIds);
		return getResultObject();
	}
	
	/**
	 * 开通账户
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object openAccount(HttpServletRequest request, IJson ijson){
		List<ComUser> users = getDataInstanceList(ijson, ComUser.class);
		if(users.size() == 1){
			resultObject = BuiltinInstance.userService.openAccount(users.get(0));
		}else{
			for (ComUser user : users) {
				resultObject = BuiltinInstance.userService.openAccount(user);
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
	public Object updatePassword(HttpServletRequest request, IJson ijson){
		JSONObject jsonObject = getJSONObject(ijson);
		if(StrUtils.isEmpty(jsonObject.getString(ResourceNameConstants.ID))){
			return "要修改密码的用户id不能为空";
		}
		if(StrUtils.isEmpty(jsonObject.getString("password"))){
			return "新密码不能为空";
		}
		resultObject = BuiltinInstance.userService.uploadUserLoginPwd(jsonObject.getString(ResourceNameConstants.ID), jsonObject.getString("password"));
		return getResultObject();
	}
}
