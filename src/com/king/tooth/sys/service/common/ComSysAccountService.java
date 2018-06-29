package com.king.tooth.sys.service.common;

import java.util.Date;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.cache.TokenRefProjectIdMapping;
import com.king.tooth.constants.LoginConstants;
import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.entity.common.ComSysAccount;
import com.king.tooth.sys.entity.common.ComSysAccountOnlineStatus;
import com.king.tooth.sys.service.AbstractService;
import com.king.tooth.util.CryptographyUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 系统账户资源服务处理器
 * @author DougLei
 */
public class ComSysAccountService extends AbstractService{

	/**
	 * 验证账户的状态
	 * @param accountId
	 */
	public ComSysAccount validAccountOfStatus(String accountId) {
		// 再验证帐号状态是否正常
		String hql = "from ComSysAccount where id = '"+accountId+"'";
		ComSysAccount account = HibernateUtil.extendExecuteUniqueQueryByHqlArr(ComSysAccount.class, hql);
		if(account.getAccountStatus() == 2){
			account.setMessage("您的账号已被禁用，请联系管理员");
			return account;
		}
		if((account.getValidDate().getTime() - System.currentTimeMillis()) < 0){
			account.setMessage("您的账号已过期，请联系管理员");
			return account;
		}
		return account;
	}
	
	//-----------------------------------------------------------
	
	/**
	 * 登录
	 * @param loginIp
	 * @param accountName
	 * @param password
	 * @return
	 */
	public ComSysAccountOnlineStatus login(String loginIp, String accountName, String password){
		return modifyAccountOfOnLineStatus(loginIp, accountName, password);
	}
	
	/**
	 * 创建或修改一个账户在线状态对象
	 * <p>登录</p>
	 * @param loginIp
	 * @param accountName
	 * @param password
	 * @return
	 */
	private ComSysAccountOnlineStatus modifyAccountOfOnLineStatus(String loginIp, String accountName, String password){
		ComSysAccountOnlineStatus accountOnlineStatus = getAccountOfOnLineStatus(loginIp, accountName, password);
		CurrentThreadContext.setCurrentAccountOnlineStatus(accountOnlineStatus);// 记录当前账户在线对象到当前线程中
		
		if(accountOnlineStatus.getIsSave()){
			HibernateUtil.saveObject(accountOnlineStatus, accountOnlineStatus.getLoginIp() + ":请求登录");
		}else{
			HibernateUtil.updateObject(accountOnlineStatus, accountOnlineStatus.getLoginIp() + ":请求登录");
		}
		return accountOnlineStatus;
	}
	
	/**
	 * 获取指定账户的在线状态对象信息
	 * @param accountName
	 * @param password
	 * @return
	 */
	private ComSysAccountOnlineStatus getAccountOfOnLineStatus(String loginIp, String accountName, String password){
		ComSysAccountOnlineStatus accountOnlineStatus = findAccountOnlineStatus(loginIp, accountName);
		
		if(accountOnlineStatus.getTryLoginTimes() > LoginConstants.tryLoginTimes){
			long lastOperDateDuration = System.currentTimeMillis() - accountOnlineStatus.getLastOperDate().getTime();
			if(lastOperDateDuration < LoginConstants.overLoginfailTimesDateDuration){
				accountOnlineStatus.setMessage("您尝试登录次数大于系统限制的"+LoginConstants.tryLoginTimes+"次，请"+((LoginConstants.overLoginfailTimesDateDuration-lastOperDateDuration)/60000)+"分钟后再试");
				return accountOnlineStatus;
			}
		}
		
		if(StrUtils.isEmpty(accountName) || StrUtils.isEmpty(password)){
			accountOnlineStatus.setMessage("帐号或密码不能为空");
			return accountOnlineStatus;
		}
		
		String queryAccountHql = "from ComSysAccount where loginName = ? or tel = ? or emails = ? and projectId = ?";
		ComSysAccount loginAccount = HibernateUtil.extendExecuteUniqueQueryByHqlArr(ComSysAccount.class, queryAccountHql, accountName, accountName, accountName, CurrentThreadContext.getProjectId());
		
		if(loginAccount == null){
			accountOnlineStatus.setMessage("账号或密码错误，请重新输入");
			return accountOnlineStatus;
		}
		accountOnlineStatus.setAccountId(loginAccount.getId());
		
		if(loginAccount.getAccountStatus() == 2){
			accountOnlineStatus.setMessage("您的账号已被禁用，请联系管理员");
			return accountOnlineStatus;
		}
		if((loginAccount.getValidDate().getTime() - System.currentTimeMillis()) < 0){
			accountOnlineStatus.setMessage("您的账号已过期，请联系管理员");
			return accountOnlineStatus;
		}
		
		if(!loginAccount.getLoginPwd().equals(CryptographyUtil.encodeMd5AccountPassword(password, loginAccount.getLoginPwdKey()))){
			accountOnlineStatus.setMessage("帐号或密码错误，请重新输入");
			return accountOnlineStatus;
		}
		
//		accountOnlineStatus.setAccount(loginAccount);
		accountOnlineStatus.setAccountId(loginAccount.getId());
		accountOnlineStatus.setAccountName(loginAccount.getLoginName());
		accountOnlineStatus.setToken(ResourceHandlerUtil.getToken());
		accountOnlineStatus.setLoginDate(new Date());
		accountOnlineStatus.setTryLoginTimes(0);
		accountOnlineStatus.setIsError(0);// 都没有错误，修改标识的值

		if(SysConfig.isConfSys){
			accountOnlineStatus.setConfProjectId("7fe971700f21d3a796d2017398812dcd");// 这里先写成固定值
		}
		return accountOnlineStatus;
	}
	
	/**
	 * 根据账户名，获取账户的在线状态对象
	 * @param loginIp 防止客户端故意输入不存在的账户密码，不停的发起请求
	 * @param accountName
	 * @return
	 */
	private ComSysAccountOnlineStatus findAccountOnlineStatus(String loginIp, String accountName) {
		// 暂时屏蔽
//		String queryAccountStatusHql = "from ComSysAccountOnlineStatus where (loginIp = ? or accountName = ?) and projectId = ?";
//		ComSysAccountOnlineStatus onlineStatus = HibernateUtil.extendExecuteUniqueQueryByHqlArr(ComSysAccountOnlineStatus.class, queryAccountStatusHql, loginIp, accountName, CurrentThreadContext.getProjectId());
		String queryAccountStatusHql = "from ComSysAccountOnlineStatus where loginIp = ? and accountName = ? and projectId = ?";
		ComSysAccountOnlineStatus onlineStatus = HibernateUtil.extendExecuteUniqueQueryByHqlArr(ComSysAccountOnlineStatus.class, queryAccountStatusHql, loginIp, accountName, CurrentThreadContext.getProjectId());
		if(onlineStatus == null){
			onlineStatus = new ComSysAccountOnlineStatus();
			onlineStatus.setTryLoginTimes(1);
			onlineStatus.setIsSave(true);
			onlineStatus.setLastOperDate(new Date());
		}else{
			// 判断上一次操作的时间至当前时间，是否超过了login.timeout.datelimit的时间，如果超过了，则将tryLoginTimes归为1
			long duration = System.currentTimeMillis() - onlineStatus.getLastOperDate().getTime();
			if(LoginConstants.loginTimeoutDatelimit < duration){
				onlineStatus.setTryLoginTimes(1);
				onlineStatus.setLastOperDate(new Date());
			}else{
				onlineStatus.setTryLoginTimes(onlineStatus.getTryLoginTimes() + 1);	
			}
		}
		onlineStatus.setLoginIp(loginIp);
		onlineStatus.setAccountName(accountName);
		onlineStatus.setIsError(1);// 一开始标识为有错误
		return onlineStatus;
	}

	/**
	 * 退出
	 * @param token
	 * @return
	 */
	public String loginOut(String token) {
		// 删除对应的ComSysAccountOnlineStatus数据
		HibernateUtil.executeUpdateByHqlArr(SqlStatementType.DELETE, "delete ComSysAccountOnlineStatus where token = ? ", token);
		// 移除传递的token和对应项目id的映射缓存
		TokenRefProjectIdMapping.removeMapping(token);
		return null;
	}

	//-----------------------------------------------------------
	
//	/**
//	 * 注册用户
//	 * @param ip
//	 * @param account
//	 */
//	public String register(String ip, ComSysAccount account) {
//		String validResult = validLoginNameIsExists(account.getLoginName());
//		if(validResult != null){
//			return validResult;
//		}
//		
//		ComSysAccount registerAccount = new ComSysAccount();
//		registerAccount.setAccountType(1);
//		registerAccount.setLoginName(account.getLoginName());
//		registerAccount.setLoginPwd(CryptographyUtil.encodeMd5AccountPassword(account.getLoginPwd(), registerAccount.getLoginPwdKey()));
//		registerAccount.setValidDate(DateUtil.parseDate("2099-12-31 23:59:59"));
//		HibernateUtil.saveObject(registerAccount, ip +":注册账户");
//		return "注册成功";
//	}
//	
//	/**
//	 * 验证登录名是否存在
//	 * @param loginName
//	 */
//	public String validLoginNameIsExists(String loginName){
//		int count = Integer.valueOf(HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourceNameConstants.ID+") from ComSysAccount where loginName = ? or tel = ? or emails = ?", loginName, loginName, loginName)+"");
//		if(count > 0){
//			return "用户名["+loginName+"]已存在";
//		}
//		return null;
//	}
}
