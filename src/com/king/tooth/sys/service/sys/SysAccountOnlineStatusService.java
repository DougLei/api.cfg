package com.king.tooth.sys.service.sys;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.annotation.Service;
import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.LoginConstants;
import com.king.tooth.sys.entity.sys.SysAccountOnlineStatus;
import com.king.tooth.sys.service.AbstractService;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 账户在线状态信息表Service
 * @author DougLei
 */
@Service
public class SysAccountOnlineStatusService extends AbstractService{
	
	/**
	 * 根据token值，验证账户的在线状态
	 * @param request
	 * @param token
	 * @return
	 */
	public SysAccountOnlineStatus validAccountOfOnLineStatus(HttpServletRequest request, String token){
		String hql = "from SysAccountOnlineStatus where token = ? ";
		SysAccountOnlineStatus onlineStatus = HibernateUtil.extendExecuteUniqueQueryByHqlArr(SysAccountOnlineStatus.class, hql, token);
		if(onlineStatus == null){
			onlineStatus = new SysAccountOnlineStatus();
			onlineStatus.setMessage("请先登录");
			return onlineStatus;
		}
		
		// (当前时间-最后操作时间)		只要大于了登录超时时限，就提示登录超时
		if(((System.currentTimeMillis() - onlineStatus.getLastOperDate().getTime()) > LoginConstants.loginTimeoutDatelimit)){
			onlineStatus.setMessage("登录超时(超过"+SysConfig.getSystemConfig("login.timeout.datelimit")+"分钟)，请重新登录");
			return onlineStatus;
		}
		
		// TODO 暂时屏蔽
//		if(!onlineStatus.getLoginIp().equals(HttpHelperUtil.getClientIp(request))){
//			onlineStatus.setMessage("当前请求的ip地址与登录时的ip地址不符，请在当前机器上重新登录");
//			return onlineStatus;
//		}
		return onlineStatus;
	}
	
	/**
	 * 根据token获取关联的账户id
	 * @param token
	 * @return
	 */
	public String getAccountIdByTokenForLog(String token){
		if(StrUtils.isEmpty(token)){
			return null;
		}
		String hql = "select accountId from SysAccountOnlineStatus where token = ?";
		return (String) HibernateUtil.executeUniqueQueryByHqlArr(hql, token);
	}
}
