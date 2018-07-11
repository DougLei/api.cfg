package com.king.tooth.sys.controller.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.sys.controller.AbstractController;
import com.king.tooth.sys.entity.common.ComUser;
import com.king.tooth.sys.service.common.ComUserService;

/**
 * 人员资源对象控制器
 * @author DougLei
 */
public class ComUserController extends AbstractController{
	private ComUserService userService = new ComUserService();
	
	/**
	 * 添加用户
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object add(HttpServletRequest request, String json){
		List<ComUser> users = getDataInstanceList(json, ComUser.class);
		analysisResourceProp(users);
		if(analysisResult == null){
			if(users.size() == 1){
				resultObject = userService.saveUser(users.get(0));
			}else{
				for (ComUser user : users) {
					resultObject = userService.saveUser(user);
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
	public Object update(HttpServletRequest request, String json){
		List<ComUser> users = getDataInstanceList(json, ComUser.class);
		analysisResourceProp(users);
		if(analysisResult == null){
			if(users.size() == 1){
				resultObject = userService.updateUser(users.get(0));
			}else{
				for (ComUser user : users) {
					resultObject = userService.updateUser(user);
					if(resultObject instanceof String){
						break;
					}
					resultJsonArray.add((JSONObject) resultObject);
				}
			}
		}
		return getResultObject();
	}
}
