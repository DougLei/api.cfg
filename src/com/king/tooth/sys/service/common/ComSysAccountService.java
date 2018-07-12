package com.king.tooth.sys.service.common;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.cache.SysConfig;
import com.king.tooth.cache.TokenRefProjectIdMapping;
import com.king.tooth.constants.LoginConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.entity.common.ComSysAccount;
import com.king.tooth.sys.entity.common.ComSysAccountOnlineStatus;
import com.king.tooth.sys.entity.common.ComUser;
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
		String hql = "from ComSysAccount where "+ResourceNameConstants.ID+" = '"+accountId+"'";
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
	
	
	//-----------------------------------------------------------------------------------------------
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
		
		String queryAccountHql = "from ComSysAccount where loginName = ? or tel = ? or email = ? and projectId = ?";
		ComSysAccount loginAccount = HibernateUtil.extendExecuteUniqueQueryByHqlArr(ComSysAccount.class, queryAccountHql, accountName, accountName, accountName, CurrentThreadContext.getProjectId());
		
		if(loginAccount == null){
			accountOnlineStatus.setMessage("账号或密码错误，请重新输入");
			return accountOnlineStatus;
		}
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
		
		processOnlineStatusBasicData(accountOnlineStatus, loginAccount, accountName);
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
	 * 处理账户在线状态对象的基础数据
	 * <p>包括当前账户id，当前用户id等等基础信息</p>
	 * @param accountOnlineStatus
	 * @param loginAccount
	 * @param accountName
	 */
	private void processOnlineStatusBasicData(ComSysAccountOnlineStatus accountOnlineStatus, ComSysAccount loginAccount, String accountName) {
		accountOnlineStatus.setCurrentCustomerId("unknow");
		accountOnlineStatus.setCurrentProjectId("unknow");
		accountOnlineStatus.setAccountId(loginAccount.getId());
		accountOnlineStatus.setIsAdministrator(loginAccount.getAccountType());
		
		if(SysConfig.isConfSys){
			accountOnlineStatus.setAccountName(accountName);
		}else{
			ComUser loginUser = HibernateUtil.extendExecuteUniqueQueryByHqlArr(ComUser.class, "from ComUser where accountId = ?", loginAccount.getId());
			accountOnlineStatus.setAccountName(getCurrentAccountName(loginUser, accountName));
			accountOnlineStatus.setCurrentUserId(loginUser.getId());
			accountOnlineStatus.setCurrentPositionId(loginUser.getPositionId());
			accountOnlineStatus.setCurrentDeptId(loginUser.getDeptId());
			accountOnlineStatus.setCurrentOrgId(loginUser.getOrgId());
		}
	}

	/**
	 * 获取当前登录的账户名
	 * @param loginAccount
	 * @param loginUser
	 * @param accountName
	 * @return
	 */
	private String getCurrentAccountName(ComUser loginUser, String accountName) {
		if(loginUser == null){
			return accountName;
		}
		String currentAccountName = loginUser.getName();
		if(currentAccountName == null){
			return accountName;
		}
		return currentAccountName;
	}

	//-----------------------------------------------------------------------------------------------
	
	/**
	 * 退出
	 * @param token
	 */
	public void loginOut(String token) {
		// 删除对应的ComSysAccountOnlineStatus数据
		HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "delete ComSysAccountOnlineStatus where token = ? ", token);
		// 移除传递的token和对应项目id的映射缓存
		TokenRefProjectIdMapping.removeMapping(token);
	}
	
	//-----------------------------------------------------------------------------------------------
	
	/**
	 * 修改账户密码
	 * @param accountId
	 * @param newLoginPwd
	 * @return
	 */
	public Object uploadAccounLoginPwd(String userId, String accountId, String newLoginPwd){
		ComSysAccount account = getObjectById(accountId, ComSysAccount.class);
		String newPwd = CryptographyUtil.encodeMd5(newLoginPwd, account.getLoginPwdKey());
		if(newPwd.equals(account.getLoginPwd())){
			return "新密码不能和旧密码相同";
		}
		HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.UPDATE, "update ComSysAccount set loginPwd=? where "+ ResourceNameConstants.ID +"=?", newPwd, accountId);
		
		JSONObject json = new JSONObject(2);
		if(StrUtils.notEmpty(userId)){
			json.put(ResourceNameConstants.ID, userId);
		}else if(StrUtils.notEmpty(accountId)){
			json.put(ResourceNameConstants.ID, accountId);
		}
		json.put("password", newPwd);
		return json;
	}
	
	//-----------------------------------------------------------

	/**
	 * 验证登录名是否已经存在
	 * @param loginName
	 * @return 
	 */
	private String validWorkNoIsExists(String loginName) {
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourceNameConstants.ID+") from ComSysAccount where loginName=? and projectId=?", loginName, CurrentThreadContext.getProjectId());
		if(count > 0){
			return "系统已经存在登录名为["+loginName+"]的账户";
		}
		return null;
	}
	
	/**
	 * 验证email邮箱是否已经存在
	 * @param email
	 * @return 
	 */
	private String validEmailIsExists(String email) {
		if(StrUtils.isEmpty(email)){
			return null;
		}
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourceNameConstants.ID+") from ComSysAccount where email=? and projectId=?", email, CurrentThreadContext.getProjectId());
		if(count > 0){
			return "系统已经存在邮箱为["+email+"]的账户";
		}
		return null;
	}
	
	/**
	 * 验证tel手机号是否已经存在
	 * @param tel
	 * @return 
	 */
	private String validTelIsExists(String tel) {
		if(StrUtils.isEmpty(tel)){
			return null;
		}
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourceNameConstants.ID+") from ComSysAccount where tel=? and projectId=?", tel, CurrentThreadContext.getProjectId());
		if(count > 0){
			return "系统已经存在手机号为["+tel+"]的账户";
		}
		return null;
	}
	
	/**
	 * 添加账户
	 * @param comSysAccount
	 * @return
	 */
	public Object saveAccount(ComSysAccount account) {
		String result = validWorkNoIsExists(account.getLoginName());
		if(result == null){
			result = validEmailIsExists(account.getEmail());
		}
		if(result == null){
			result = validTelIsExists(account.getTel());
		}
		if(result == null){
			JSONObject accountJsonObject = HibernateUtil.saveObject(account, null);
			return accountJsonObject;
		}
		return result;
	}

	/**
	 * 修改账户
	 * @param comSysAccount
	 * @return
	 */
	public Object updateAccount(ComSysAccount account) {
		ComSysAccount oldAccount = getObjectById(account.getId(), ComSysAccount.class);
		String result = null;
		if(!oldAccount.getLoginName().equals(account.getLoginName())){
			result = validWorkNoIsExists(account.getLoginName());
		}
		if(result == null && (StrUtils.notEmpty(oldAccount.getEmail()) && !oldAccount.getEmail().equals(account.getEmail())) || (StrUtils.isEmpty(oldAccount.getEmail()) && StrUtils.notEmpty(account.getEmail()))){
			result = validEmailIsExists(account.getEmail());
		}
		if(result == null && (StrUtils.notEmpty(oldAccount.getTel()) && !oldAccount.getTel().equals(account.getTel())) || (StrUtils.isEmpty(oldAccount.getTel()) && StrUtils.notEmpty(account.getTel()))){
			result = validTelIsExists(account.getTel());
		}
		if(result == null){
			return HibernateUtil.updateObjectByHql(account, null);
		}
		return result;
	}

	/**
	 * 删除账户
	 * @param accountId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object deleteAccount(String accountId) {
		HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "delete ComSysAccount where " + ResourceNameConstants.ID+"=?", accountId);
		HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "update ComUser set accountId =null  where accountId=? and projectId=?", accountId, CurrentThreadContext.getProjectId());
		
		List<Object> tokens = HibernateUtil.executeListQueryByHqlArr("select token from ComSysAccountOnlineStatus where accountId=? and projectId=?", accountId, CurrentThreadContext.getProjectId());
		HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "delete ComSysAccountOnlineStatus where accountId=? and projectId=?", accountId, CurrentThreadContext.getProjectId());
		
		// 移除传递的token和对应项目id的映射缓存
		for (Object token : tokens) {
			TokenRefProjectIdMapping.removeMapping(token+"");
		}
		return null;
	}
}
