package com.king.tooth.sys.service.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.annotation.Service;
import com.king.tooth.cache.SysContext;
import com.king.tooth.cache.TokenRefProjectIdMapping;
import com.king.tooth.constants.LoginConstants;
import com.king.tooth.constants.PermissionConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.constants.SqlStatementTypeConstants;
import com.king.tooth.sys.builtin.data.BuiltinObjectInstance;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.entity.cfg.projectmodule.ProjectModuleExtend;
import com.king.tooth.sys.entity.sys.SysAccount;
import com.king.tooth.sys.entity.sys.SysAccountCard;
import com.king.tooth.sys.entity.sys.SysAccountOnlineStatus;
import com.king.tooth.sys.entity.sys.SysUser;
import com.king.tooth.sys.entity.sys.SysUserPermissionCache;
import com.king.tooth.sys.entity.sys.permission.SysPermissionExtend;
import com.king.tooth.sys.service.AService;
import com.king.tooth.sys.service.cfg.CfgProjectModuleService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.CryptographyUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.util.websocket.pushmessage.PushMessageUtil;

/**
 * 账户表Service
 * @author DougLei
 */
@Service
public class SysAccountService extends AService{
	
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
	 * 用户名密码登录
	 * <p>创建或修改一个账户在线状态对象</p>
	 * @param loginIp
	 * @param accountName
	 * @param password
	 * @return
	 */
	public SysAccountOnlineStatus loginByUsernameAndPwd(String loginIp, String accountName, String password){
		if(password.equals(loingByCardOfPassword)){
			return new SysAccountOnlineStatus("用户名密码登陆时，禁止调用刷卡登陆的密码");
		}
		return commonLogin(loginIp, accountName, password);
	}
	
	/**
	 * 刷卡登录
	 * <p>创建或修改一个账户在线状态对象</p>
	 * @param loginIp
	 * @param accountName
	 * @return
	 */
	public SysAccountOnlineStatus loginByCard(String loginIp, String accountName){
		if(StrUtils.isEmpty(accountName)){
			return new SysAccountOnlineStatus("刷卡登陆时，获取账户名为空");
		}
		return commonLogin(loginIp, accountName, loingByCardOfPassword);
	}
	private static final String loingByCardOfPassword = "ad791d1940bd788b0fdada53bad6bc74";
	
	/**
	 * 通用的登陆
	 * @param loginIp
	 * @param accountName
	 * @param password
	 * @return
	 */
	private SysAccountOnlineStatus commonLogin(String loginIp, String accountName, String password){
		SysAccountOnlineStatus accountOnlineStatus = getAccountOfOnLineStatus(loginIp, accountName, password);
		accountOnlineStatus.setIsDoLogin(true);
		CurrentThreadContext.setCurrentAccountOnlineStatus(accountOnlineStatus);// 记录当前账户在线对象到当前线程中
		
		setLoginType(accountOnlineStatus, password);
		if(accountOnlineStatus.getIsSave()){
			HibernateUtil.saveObject(accountOnlineStatus, accountOnlineStatus.getLoginIp() + ":请求登录");
		}else{
			HibernateUtil.updateEntityObject(accountOnlineStatus, accountOnlineStatus.getLoginIp() + ":请求登录");
		}
		return accountOnlineStatus;
	}
	
	/**记录登陆类型*/
	private void setLoginType(SysAccountOnlineStatus accountOnlineStatus, String password) {
		if("ad791d1940bd788b0fdada53bad6bc74".equals(password)){
			accountOnlineStatus.loginByCard();
		}else{
			accountOnlineStatus.loginByUserNameAndPassword();
		}
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
		
		String queryAccountHql = "from SysAccount where (loginName = ? or tel = ? or email = ? or workNo = ?) and customerId = ? and isDelete=0";
		SysAccount loginAccount = HibernateUtil.extendExecuteUniqueQueryByHqlArr(SysAccount.class, queryAccountHql, accountName, accountName, accountName, accountName, CurrentThreadContext.getCustomerId());
		
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
		if(!"ad791d1940bd788b0fdada53bad6bc74".equals(password)
				&& !loginAccount.getLoginPwd().equals(CryptographyUtil.encodeMd5(password, loginAccount.getLoginPwdKey()))){
			accountOnlineStatus.setMessage("帐号或密码错误，请重新输入");
			return accountOnlineStatus;
		}
		accountOnlineStatus.setAccountType(loginAccount.getType());
		
		// 处理账户和用户的关系
		processAccountAndUserRelation(accountOnlineStatus, loginAccount.getId(), accountName);
		
		// 处理权限
		if(processPermission(accountOnlineStatus)){
			// 处理基本信息
			processOnlineStatusBasicData(accountOnlineStatus, loginAccount);
			
			// TODO 在配置系统中，要配置的项目id，这里暂时写成固定值
			accountOnlineStatus.setConfProjectId("7fe971700f21d3a796d2017398812dcd");
			
			accountOnlineStatus.setIsError(0);// 都没有错误，修改标识的值
		}
		return accountOnlineStatus;
	}
	
	/**
	 * 处理账户和用户的关系
	 * <p>获取账户对应的用户的信息，包括用户id，用户所属的角色、职务、部门、组织机构id，并保存到账户在线状态对象中</p>
	 * @param accountOnlineStatus
	 * @param accountId
	 * @param accountName
	 */
	private void processAccountAndUserRelation(SysAccountOnlineStatus accountOnlineStatus, String accountId, String accountName) {
		accountOnlineStatus.setAccountId(accountId);
		SysUser loginUser = HibernateUtil.extendExecuteUniqueQueryByHqlArr(SysUser.class, "from SysUser where "+ResourcePropNameConstants.ID+" = ? and customerId=? and isDelete=0", accountId, CurrentThreadContext.getCustomerId());
		
		if(loginUser == null){
			accountOnlineStatus.setAccountName(accountName);
			accountOnlineStatus.setUserId("unknow");
			accountOnlineStatus.setPositionId("unknow");
			accountOnlineStatus.setDeptId("unknow");
			accountOnlineStatus.setOrgId("unknow");
		}else{
			accountOnlineStatus.setIsExistsUserObj(true);
			String userId = loginUser.getId();
			accountOnlineStatus.setAccountName(getCurrentAccountName(loginUser, accountName));
			accountOnlineStatus.setUserId(userId);
			accountOnlineStatus.setUserSecretLevel(loginUser.getSecretLevel());
			
			StringBuilder idBuffer = new StringBuilder();
			
			// 获取用户所有有效的角色id
			List<Object> roleIds = new ArrayList<Object>(10);
			accountOnlineStatus.setRoleId(getIds(idBuffer, roleIds, queryUserOfRoleIdsHql, userId));
			accountOnlineStatus.setRoleIds(roleIds);
			
			// 获取用户所属的职务id
			List<Object> positionIds = new ArrayList<Object>(10);
			accountOnlineStatus.setPositionId(getIds(idBuffer, positionIds, queryUserOfPositionIdsHql, userId));
			accountOnlineStatus.setPositionIds(positionIds);
			
			// 获取用户所属的部门id
			List<Object> deptIds = new ArrayList<Object>(10);
			accountOnlineStatus.setDeptId(getIds(idBuffer, deptIds, queryUserOfDeptIdsHql, userId));
			accountOnlineStatus.setDeptIds(deptIds);
			
			// 获取用户所有的用户组id
			List<Object> userGroupIds = new ArrayList<Object>(10);
			accountOnlineStatus.setUserGroupId(getIds(idBuffer, userGroupIds, queryUserOfUserGroupIdsHql, userId));
			accountOnlineStatus.setUserGroupIds(userGroupIds);
			
			// 获取用户所属的组织id
//			List<Object> orgIds = new ArrayList<Object>(10);
			accountOnlineStatus.setOrgId("暂不支持");
//			accountOnlineStatus.setOrgIds(orgIds);
		}
	}
	
	// 按照orderCode asc，查询用户所有有效的角色id【orderCode越低的，优先级越高】
	private static final String queryUserOfRoleIdsHql = "select l.rightId from SysUserRoleLinks l, SysRole r where l.rightId=r."+ResourcePropNameConstants.ID+" and l.leftId=? and r.isEnabled=1 order by r.orderCode asc";
	// 按照orderCode asc，查询用户所属的职务id【orderCode越低的，优先级越高】
	private static final String queryUserOfPositionIdsHql = "select l.rightId from SysUserPositionLinks l, SysPosition p where l.rightId=p."+ResourcePropNameConstants.ID+" and l.leftId=? order by p.orderCode asc, l.isMain desc";
	// 按照orderCode asc，查询用户所属的部门id【orderCode越低的，优先级越高】
	private static final String queryUserOfDeptIdsHql = "select l.rightId from SysUserDeptLinks l, SysDept d where l.rightId=d."+ResourcePropNameConstants.ID+" and l.leftId=? order by d.orderCode asc, l.isMain desc";
	// 按照orderCode asc，查询用户所有有效的用户组id【orderCode越低的，优先级越高】
	private static final String queryUserOfUserGroupIdsHql = "select "+ResourcePropNameConstants.ID+" from SysUserGroup where isEnabled=1 and "+ResourcePropNameConstants.ID+" in(select distinct userGroupId from SysUserGroupDetail where userId=?) order by orderCode asc";
	/**
	 * 获取hql执行查询后的结果id
	 * @param idBuffer
	 * @param hql
	 * @param paramValues
	 */
	@SuppressWarnings("unchecked")
	private String getIds(StringBuilder idBuffer, List<Object> ids, String hql, Object...paramValues){
		idBuffer.setLength(0);
		try {
			List<Object> tmpIds = HibernateUtil.executeListQueryByHqlArr(null, null, hql, paramValues);
			if(tmpIds != null && tmpIds.size() > 0){
				for (Object id : tmpIds) {
					ids.add(id);
					idBuffer.append(id).append(",");
				}
				tmpIds.clear();
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
	 * 登陆时处理权限
	 * @param accountOnlineStatus
	 * @return 是否有权限，如果没有权限，则也属于登陆失败
	 */
	private boolean processPermission(SysAccountOnlineStatus accountOnlineStatus) {
		// 管理员或系统开发人员，不做权限控制，查询所有有效模块
		if(accountOnlineStatus.isAdministrator() || accountOnlineStatus.isDeveloper()){
			accountOnlineStatus.setProjectModules(processProjectModules(BuiltinObjectInstance.allPermission));
			return true;
		}
		
		if(!accountOnlineStatus.getIsExistsUserObj()){
			accountOnlineStatus.setMessage("您账户关联的用户信息还未完善，请联系系统管理员，协助您创建和完善用户信息，并分配系统功能权限");
			return false;
		}
		
		SysUserPermissionCache sapc = BuiltinResourceInstance.getInstance("SysPermissionService", SysPermissionService.class).getSysUserPermissionCache(accountOnlineStatus);
		SysPermissionExtend permission = sapc.getPermissionObject();
		
		if((accountOnlineStatus.isNormal())
				&& (permission == null || permission.getChildren() == null || permission.getChildren().size() == 0)){
			accountOnlineStatus.setMessage("您还未分配系统功能权限，请联系系统管理员");
			return false;
		}
		
		BuiltinResourceInstance.getInstance("SysPermissionService", SysPermissionService.class).filterPermission(permission, PermissionConstants.RT_MODULE);
		accountOnlineStatus.setProjectModules(processProjectModules(permission));
		return true;
	}
	
	/**
	 * 处理项目模块信息
	 * <p>登陆时先进行权限过滤，筛选出当前用户有哪些模块的权限，查询出来，组成树形结构返回</p>
	 * <p>如果是管理员或系统开发人员，不做权限控制，查询所有模块，组成树形结构返回</p>
	 * @param permission
	 * @return
	 */
	private List<ProjectModuleExtend> processProjectModules(SysPermissionExtend permission) {
		if(permission == BuiltinObjectInstance.allPermission){
			// 证明是管理员或系统开发人员
			return BuiltinResourceInstance.getInstance("CfgProjectModuleService", CfgProjectModuleService.class).getCurrentProjectOfModules();
		}else{
			return BuiltinResourceInstance.getInstance("CfgProjectModuleService", CfgProjectModuleService.class).getProjectModulesByPermission(permission);
		}
	}

	/**
	 * 处理账户在线状态对象的基础数据
	 * @param accountOnlineStatus
	 * @param loginAccount
	 */
	private void processOnlineStatusBasicData(SysAccountOnlineStatus accountOnlineStatus, SysAccount loginAccount) {
		accountOnlineStatus.setLastOperDate(new Date());
		accountOnlineStatus.setToken(ResourceHandlerUtil.getToken());
		accountOnlineStatus.setLoginDate(new Date());
		accountOnlineStatus.setTryLoginTimes(0);
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
		HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.DELETE, "delete SysAccountOnlineStatus where token = ? ", token);
		// 移除传递的token和对应项目id的映射缓存
		TokenRefProjectIdMapping.removeMapping(token);
		
		// 断开与websocket连接
		String userId = CurrentThreadContext.getCurrentAccountOnlineStatus().getUserId();
		String result = PushMessageUtil.closeSession(userId);
		Log4jUtil.info("id为[{}]，名为[{}]的用户，断开与消息推送系统(websocket)的连接结果为:{}", userId, CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountName(), result);
	}
	
	//-----------------------------------------------------------------------------------------------
	
	/**
	 * 验证登录名是否已经存在
	 * @param loginName
	 * @return 
	 */
	private String validWorkNoIsExists(String loginName) {
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from SysAccount where loginName=? and customerId=? and isDelete=0", loginName, CurrentThreadContext.getCurrentAccountOnlineStatus().getCustomerId());
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
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from SysAccount where email=? and customerId=? and isDelete=0", email, CurrentThreadContext.getCurrentAccountOnlineStatus().getCustomerId());
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
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from SysAccount where tel=? and customerId=? and isDelete=0", tel, CurrentThreadContext.getCurrentAccountOnlineStatus().getCustomerId());
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
			if(StrUtils.isEmpty(account.getLoginPwd())){
				account.setLoginPwd(SysContext.getSystemConfig("account.default.pwd"));
			}
			account.setLoginPwdKey(ResourceHandlerUtil.getLoginPwdKey());
			account.setLoginPwd(CryptographyUtil.encodeMd5(account.getLoginPwd(), account.getLoginPwdKey()));
			if(account.getValidDate() == null){
				account.setValidDate(BuiltinObjectInstance.validDate);
			}
			
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
			if(oldAccount.getStatus() != account.getStatus()){
				BuiltinResourceInstance.getInstance("SysAccountCardService", SysAccountCardService.class).updateAccountCardStatus(new SysAccountCard(account.getId(), account.getStatus()));
			}
			return HibernateUtil.updateEntityObject(account, null);
		}
		return result;
	}

	/**
	 * 删除账户
	 * @param accountId
	 * @return
	 */
	public Object deleteAccount(String accountId) {
		HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.UPDATE, "update SysAccount set isDelete=1, lastUpdateDate=? where " + ResourcePropNameConstants.ID+"=? and customerId=?", new Date(), accountId, CurrentThreadContext.getCustomerId());
		BuiltinResourceInstance.getInstance("SysAccountCardService", SysAccountCardService.class).deleteAccountCard(accountId);
		deleteTokenInfoByAccountId(accountId);
		return null;
	}

	/**
	 * 删除指定账户id的token信息
	 * @param accountId
	 */
	@SuppressWarnings("unchecked")
	private void deleteTokenInfoByAccountId(String accountId) {
		List<Object> tokens = HibernateUtil.executeListQueryByHqlArr(null, null, "select token from SysAccountOnlineStatus where accountId=? and customerId=?", accountId, CurrentThreadContext.getCustomerId());
		if(tokens != null && tokens.size() > 0){
			HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.DELETE, "delete SysAccountOnlineStatus where accountId=? and customerId=?", accountId, CurrentThreadContext.getCustomerId());
			// 移除传递的token和对应项目id的映射缓存
			for (Object token : tokens) {
				TokenRefProjectIdMapping.removeMapping(token+"");
			}
			tokens.clear();
		}
	}
}
