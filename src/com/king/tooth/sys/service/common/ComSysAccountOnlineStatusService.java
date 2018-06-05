package com.king.tooth.sys.service.common;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.LoginConstants;
import com.king.tooth.sys.entity.common.ComSysAccountOnlineStatus;
import com.king.tooth.sys.service.AbstractResourceService;
import com.king.tooth.util.HttpHelperUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * [通用的]系统账户在线状态资源服务处理器
 * @author DougLei
 */
public class ComSysAccountOnlineStatusService extends AbstractResourceService{
	
	/**
	 * 根据token值，验证账户的在线状态
	 * @param request
	 * @param token
	 * @return
	 */
	public ComSysAccountOnlineStatus validAccountOfOnLineStatus(HttpServletRequest request, String token){
		String hql = "from ComSysAccountOnlineStatus where token = ? ";
		ComSysAccountOnlineStatus onlineStatus = HibernateUtil.extendExecuteUniqueQueryByHqlArr(ComSysAccountOnlineStatus.class, hql, token);
		if(onlineStatus == null){
			onlineStatus = new ComSysAccountOnlineStatus();
			onlineStatus.setMessage("请先登录");
			return onlineStatus;
		}
		
		if(!onlineStatus.getToken().equals(token)){
			onlineStatus.setMessage("登录验证失败，请重新登录");
			return onlineStatus;
		}
		
		// (最后操作时间-登录时间)	或	 (当前时间-最后操作时间)		只要大于了登录超时时限，就提示登录超时
		if(((onlineStatus.getLastOperDate().getTime() - onlineStatus.getLoginDate().getTime()) > LoginConstants.loginTimeoutDatelimit)
				|| ((System.currentTimeMillis() - onlineStatus.getLastOperDate().getTime()) > LoginConstants.loginTimeoutDatelimit)){
			onlineStatus.setMessage("登录超时(超过"+SysConfig.getSystemConfig("login.timeout.datelimit")+"分钟)，请重新登录");
			return onlineStatus;
		}
		
		if(!onlineStatus.getLoginIp().equals(HttpHelperUtil.getClientIp(request))){
			onlineStatus.setMessage("当前请求的ip地址与登录时的ip地址不符，请在当前机器上重新登录");
			return onlineStatus;
		}
		return onlineStatus;
	}
	
}
