package com.king.tooth.sys.service.sys;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.cache.SysConfig;
import com.king.tooth.cache.TokenRefProjectIdMapping;
import com.king.tooth.constants.LoginConstants;
import com.king.tooth.constants.PermissionConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.builtin.data.BuiltinObjectInstance;
import com.king.tooth.sys.entity.sys.SysAccount;
import com.king.tooth.sys.entity.sys.SysAccountOnlineStatus;
import com.king.tooth.sys.entity.sys.SysAccountPermissionCache;
import com.king.tooth.sys.entity.sys.SysUser;
import com.king.tooth.sys.entity.sys.permission.SysPermissionExtend;
import com.king.tooth.sys.service.AbstractService;
import com.king.tooth.thread.CurrentThreadContext;
import com.king.tooth.util.CryptographyUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 账户表Service
 * @author DougLei
 */
public class SysAccountService extends AbstractService{
	
	/**
	 * 验证账户的状态
	 * @param accountId
	 */
	public SysAccount validAccountOfStatus(String accountId) {
		// 再验证帐号状态是否正常
		String hql = "from SysAccount where "+ResourcePropNameConstants.ID+" = ?";
		SysAccount account = HibernateUtil.extendExecuteUniqueQueryByHqlArr(SysAccount.class, hql, accountId);
		if(account.getStatus() == 2){
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
	 * 创建或修改一个账户在线状态对象
	 * <p>登录</p>
	 * @param loginIp
	 * @param accountName
	 * @param password
	 * @return
	 */
	public SysAccountOnlineStatus modifyAccountOfOnLineStatus(String loginIp, String accountName, String password){
		SysAccountOnlineStatus accountOnlineStatus = getAccountOfOnLineStatus(loginIp, accountName, password);
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
	private SysAccountOnlineStatus getAccountOfOnLineStatus(String loginIp, String accountName, String password){
		SysAccountOnlineStatus accountOnlineStatus = findAccountOnlineStatus(loginIp, accountName);
		
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
		
		String queryAccountHql = "from SysAccount where (loginName = ? or tel = ? or email = ?) and customerId = ?";
		SysAccount loginAccount = HibernateUtil.extendExecuteUniqueQueryByHqlArr(SysAccount.class, queryAccountHql, accountName, accountName, accountName, CurrentThreadContext.getCustomerId());
		
		if(loginAccount == null){
			accountOnlineStatus.setMessage("账号或密码错误，请重新输入");
			return accountOnlineStatus;
		}
		if(loginAccount.getStatus() == 2){
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
		
		accountOnlineStatus.setAccountType(loginAccount.getType());
		
		// 处理权限
		if(processPermission(loginAccount.getId(), loginAccount.getType(), accountOnlineStatus)){
			// 处理基本信息
			processOnlineStatusBasicData(accountOnlineStatus, loginAccount, accountName);
			if(SysConfig.isConfSys){
				// TODO 这里暂时写成固定值
				accountOnlineStatus.setConfProjectId("7fe971700f21d3a796d2017398812dcd");
			}
			accountOnlineStatus.setIsError(0);// 都没有错误，修改标识的值
		}
		return accountOnlineStatus;
	}
	
	/**
	 * 登陆时处理权限
	 * @param accountId
	 * @param accountType
	 * @param accountOnlineStatus
	 * @return 是否有权限，如果没有权限，则也属于登陆失败
	 */
	public boolean processPermission(String accountId, Integer accountType, SysAccountOnlineStatus accountOnlineStatus) {
		// 管理员或系统开发人员，不做权限控制，返回ALL，标识可以访问所有功能
		if(accountOnlineStatus.isAdministrator() || accountOnlineStatus.isDeveloper()){
			accountOnlineStatus.setPermission(BuiltinObjectInstance.allPermission);
			return true;
		}
		
		SysAccountPermissionCache sapc = BuiltinObjectInstance.permissionService.getSysAccountPermissionCache(accountId);
		SysPermissionExtend permission = sapc.getPermissionObject();
		
		if((accountOnlineStatus.isNormal())
				&& (permission == null || permission.getChildren() == null || permission.getChildren().size() == 0)){
			accountOnlineStatus.setMessage("您还未分配系统功能权限，请联系系统管理员");
			return false;
		}
		
		BuiltinObjectInstance.permissionService.filterPermission(permission, PermissionConstants.RT_MODULE);
		accountOnlineStatus.setPermission(permission);
		return true;
	}
	
	/**
	 * 处理账户在线状态对象的基础数据
	 * <p>包括当前账户id，当前用户id等等基础信息</p>
	 * @param accountOnlineStatus
	 * @param loginAccount
	 * @param accountName
	 */
	private void processOnlineStatusBasicData(SysAccountOnlineStatus accountOnlineStatus, SysAccount loginAccount, String accountName) {
		StringBuilder idBuffer = new StringBuilder();
		String accountId = loginAccount.getId();
		
		accountOnlineStatus.setLastOperDate(new Date());
		accountOnlineStatus.setToken(ResourceHandlerUtil.getToken());
		accountOnlineStatus.setLoginDate(new Date());
		accountOnlineStatus.setTryLoginTimes(0);
		accountOnlineStatus.setAccountId(accountId);
		accountOnlineStatus.setRoleId(getIds(idBuffer, queryRoleId, accountId));
		
		SysUser loginUser = HibernateUtil.extendExecuteUniqueQueryByHqlArr(SysUser.class, "from SysUser where accountId = ? and customerId=?", loginAccount.getId(), CurrentThreadContext.getCustomerId());
		if(loginUser == null){
			accountOnlineStatus.setAccountName(accountName);
			accountOnlineStatus.setUserId("unknow");
			accountOnlineStatus.setPositionId("unknow");
			accountOnlineStatus.setDeptId("unknow");
			accountOnlineStatus.setOrgId("unknow");
		}else{
			String userId = loginUser.getId();
			accountOnlineStatus.setAccountName(getCurrentAccountName(loginUser, accountName));
			accountOnlineStatus.setUserId(userId);
			accountOnlineStatus.setPositionId(getIds(idBuffer, queryPositionId, userId));
			accountOnlineStatus.setDeptId(getIds(idBuffer, queryDeptId, userId));
			accountOnlineStatus.setOrgId("暂不支持");
		}
	}
	
	// 查询账户所有有效的角色信息
	private static final String queryRoleId = "select l.rightId from SysAccountRoleLinks l, SysRole r where l.rightId=r."+ResourcePropNameConstants.ID+" and l.leftId=? and r.isEnabled=1 order by r.orderCode asc";
	// 查询用户所有的职务信息
	private static final String queryPositionId = "select l.rightId from SysUserPositionLinks l, SysPosition p where l.rightId=p."+ResourcePropNameConstants.ID+" and l.isMain=0 and l.leftId=? order by p.orderCode asc, l.isMain desc";
	// 查询用户所有的部门信息
	private static final String queryDeptId = "select l.rightId from SysUserDeptLinks l, SysDept d where l.rightId=d."+ResourcePropNameConstants.ID+" and l.isMain=0 and l.leftId=? order by d.orderCode asc, l.isMain desc";
	/**
	 * 获取hql执行查询后的结果id
	 * @param idBuffer
	 * @param hql
	 * @param paramValues
	 */
	@SuppressWarnings("unchecked")
	private String getIds(StringBuilder idBuffer, String hql, Object...paramValues){
		idBuffer.setLength(0);
		try {
			List<Object> ids = HibernateUtil.executeListQueryByHqlArr(null, null, hql, paramValues);
			if(ids != null && ids.size() > 0){
				for (Object id : ids) {
					idBuffer.append(id).append(",");
				}
				ids.clear();
				idBuffer.setLength(idBuffer.length()-1);
				return idBuffer.toString();
			}
			return null;
		} finally{
			if(idBuffer.length() > 0){
				idBuffer.setLength(0);
			}
		}
	}
	
	/**
	 * 根据账户名，获取账户的在线状态对象
	 * @param loginIp 防止客户端故意输入不存在的账户密码，不停的发起请求
	 * @param accountName
	 * @return
	 */
	private SysAccountOnlineStatus findAccountOnlineStatus(String loginIp, String accountName) {
		// TODO 暂时屏蔽
//		String queryAccountStatusHql = "from SysAccountOnlineStatus where (loginIp = ? or accountName = ?) and customerId = ?";
//		SysAccountOnlineStatus onlineStatus = HibernateUtil.extendExecuteUniqueQueryByHqlArr(SysAccountOnlineStatus.class, queryAccountStatusHql, loginIp, accountName, CurrentThreadContext.getCustomerId());
		
		String queryAccountStatusHql = "from SysAccountOnlineStatus where loginIp = ? and accountName = ? and customerId = ?";
		SysAccountOnlineStatus accountOnlineStatus = HibernateUtil.extendExecuteUniqueQueryByHqlArr(SysAccountOnlineStatus.class, queryAccountStatusHql, loginIp, accountName, CurrentThreadContext.getCustomerId());
		if(accountOnlineStatus == null){
			accountOnlineStatus = new SysAccountOnlineStatus();
			accountOnlineStatus.setTryLoginTimes(1);
			accountOnlineStatus.setIsSave(true);
			accountOnlineStatus.setLastOperDate(new Date());
		}else{
			// 判断上一次操作的时间至当前时间，是否超过了login.timeout.datelimit的时间，如果超过了，则将tryLoginTimes归为1
			long duration = System.currentTimeMillis() - accountOnlineStatus.getLastOperDate().getTime();
			if(LoginConstants.loginTimeoutDatelimit < duration){
				accountOnlineStatus.setTryLoginTimes(1);
				accountOnlineStatus.setLastOperDate(new Date());
			}else{
				accountOnlineStatus.setTryLoginTimes(accountOnlineStatus.getTryLoginTimes() + 1);	
			}
		}
		
		accountOnlineStatus.setLoginIp(loginIp);
		accountOnlineStatus.setAccountName(accountName);
		accountOnlineStatus.setIsError(1);// 一开始标识为有错误
		return accountOnlineStatus;
	}
	
	/**
	 * 获取当前登录的账户名
	 * @param loginAccount
	 * @param loginUser
	 * @param accountName
	 * @return
	 */
	private String getCurrentAccountName(SysUser loginUser, String accountName) {
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
		// 删除对应的SysAccountOnlineStatus数据
		HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "delete SysAccountOnlineStatus where token = ? ", token);
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
		SysAccount account = getObjectById(accountId, SysAccount.class);
		String newPwd = CryptographyUtil.encodeMd5(newLoginPwd, account.getLoginPwdKey());
		if(newPwd.equals(account.getLoginPwd())){
			return "新密码不能和旧密码相同";
		}
		HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.UPDATE, "update SysAccount set loginPwd=? where "+ ResourcePropNameConstants.ID +"=?", newPwd, accountId);
		
		JSONObject json = new JSONObject(2);
		if(StrUtils.notEmpty(userId)){
			json.put(ResourcePropNameConstants.ID, userId);
		}else if(StrUtils.notEmpty(accountId)){
			json.put(ResourcePropNameConstants.ID, accountId);
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
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from SysAccount where loginName=? and customerId=?", loginName, CurrentThreadContext.getCurrentAccountOnlineStatus().getCustomerId());
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
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from SysAccount where email=? and customerId=?", email, CurrentThreadContext.getCurrentAccountOnlineStatus().getCustomerId());
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
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from SysAccount where tel=? and customerId=?", tel, CurrentThreadContext.getCurrentAccountOnlineStatus().getCustomerId());
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
	public Object saveAccount(SysAccount account) {
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
	public Object updateAccount(SysAccount account) {
		SysAccount oldAccount = getObjectById(account.getId(), SysAccount.class);
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
			return HibernateUtil.updateObject(account, null);
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
		HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "delete SysAccount where " + ResourcePropNameConstants.ID+"=? and customerId=?", accountId, CurrentThreadContext.getCustomerId());
		HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.UPDATE, "update SysUser set accountId =null  where accountId=? and customerId=?", accountId, CurrentThreadContext.getCustomerId());
		
		List<Object> tokens = HibernateUtil.executeListQueryByHqlArr(null, null, "select token from SysAccountOnlineStatus where accountId=? and customerId=?", accountId, CurrentThreadContext.getCustomerId());
		if(tokens != null && tokens.size() > 0){
			HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "delete SysAccountOnlineStatus where accountId=? and customerId=?", accountId, CurrentThreadContext.getCustomerId());
			// 移除传递的token和对应项目id的映射缓存
			for (Object token : tokens) {
				TokenRefProjectIdMapping.removeMapping(token+"");
			}
			tokens.clear();
		}
		return null;
	}
}
