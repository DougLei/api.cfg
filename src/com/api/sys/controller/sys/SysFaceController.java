package com.api.sys.controller.sys;

import java.io.ByteArrayInputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.codec.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.api.cache.FaceEngineContext;
import com.api.cache.TokenRefProjectIdMapping;
import com.api.sys.builtin.data.BuiltinParameterKeys;
import com.api.sys.builtin.data.BuiltinResourceInstance;
import com.api.sys.controller.AController;
import com.api.sys.entity.sys.SysAccountOnlineStatus;
import com.api.sys.entity.sys.SysReqLog;
import com.api.sys.service.sys.SysAccountCardService;
import com.api.thread.current.CurrentThreadContext;
import com.api.util.HttpHelperUtil;
import com.api.util.JsonUtil;
import com.arcsoft.face.FaceFeature;

/**
 * 账户表Controller
 * @author DougLei
 */
public class SysFaceController extends AController{
	private static final Logger logger = LoggerFactory.getLogger(SysFaceController.class);
	
	/**
	 * 业务流程: 根据传入的流, 与所有已存在的照片进行匹配, 找到匹配的照片的code, 去数据库中查询, 获取对应的用户id, 然后通过id进行登录
	 * @param request
	 * @param ijson
	 * @return
	 */
	public Object loginByFace(HttpServletRequest request){
		CurrentThreadContext.getReqLogData().getReqLog().setType(SysReqLog.LOGIN);// 标识为登陆日志
		
		FaceFeature requestFaceFeature = getRequestFaceFeature(request);
		String targetCode = FaceEngineContext.compare(requestFaceFeature);
		if(targetCode == null)
			return "您的面部与当前面部库中的数据不匹配";
		
		SysAccountOnlineStatus accountOnlineStatus = BuiltinResourceInstance.getInstance("SysAccountCardService", SysAccountCardService.class).loginById(request.getAttribute(BuiltinParameterKeys._CLIENT_IP).toString(), targetCode);
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

	// 获取请求的face特征
	private FaceFeature getRequestFaceFeature(HttpServletRequest request) {
		try {
			String imageString = HttpHelperUtil.analysisFormData(request).toString();
			return FaceEngineContext.extractFaceFeature(new ByteArrayInputStream(Base64.decode(imageString.substring(imageString.indexOf(",")+1))));
		} catch (Exception e) {
			logger.error("获取请求的面部特性时出现异常: {}", e);
			return null;
		}
	}
}
