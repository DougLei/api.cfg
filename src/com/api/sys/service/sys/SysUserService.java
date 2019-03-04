package com.api.sys.service.sys;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.api.annotation.Service;
import com.api.cache.SysContext;
import com.api.constants.ResourcePropNameConstants;
import com.api.constants.SqlStatementTypeConstants;
import com.api.sys.builtin.data.BuiltinResourceInstance;
import com.api.sys.entity.sys.SysAccount;
import com.api.sys.entity.sys.SysAccountCard;
import com.api.sys.entity.sys.SysUser;
import com.api.sys.service.AService;
import com.api.thread.current.CurrentThreadContext;
import com.api.util.CryptographyUtil;
import com.api.util.ResourceHandlerUtil;
import com.api.util.StrUtils;
import com.api.util.hibernate.HibernateUtil;

/**
 * 人员信息表Service
 * @author DougLei
 */
@Service
public class SysUserService extends AService{
	
	private static final String sysUserDeptLinks = "SysUserDeptLinks";
	private static final String sysUserPositionLinks = "SysUserPositionLinks";
	private static final String sysUserRoleLinks = "SysUserRoleLinks";
	
	/**
	 * 账户是否存在
	 * @param id
	 * @return
	 */
	private boolean accountIsExists(String id){
		if(StrUtils.isEmpty(id)){
			return false;
		}
		Object obj = HibernateUtil.executeUniqueQueryByHqlArr("select "+ResourcePropNameConstants.ID+" from SysAccount where " + ResourcePropNameConstants.ID +"=?", id);
		if(obj == null){
			return false;
		}
		return true;
	}
	
	/**
	 * 重置用户的账户信息，即将用户的工号、手机号、邮箱三个字段的值，重新更新到账户的帐号、手机号、邮箱三个字段的值，同时重置账户的登陆密码为初始密码
	 * @param userId
	 * @return
	 */
	public Object resetAccount(String userId) {
		if(!accountIsExists(userId)){
			return "该用户不存在账户信息，无法重置，或先创建关联的账户信息";
		}
		SysUser user = getObjectById(userId, SysUser.class);
		SysAccount account = getObjectById(userId, SysAccount.class);
		account.setLoginName(user.getWorkNo());
		account.setTel(user.getTel());
		account.setEmail(user.getEmail());
		account.setLoginPwd(CryptographyUtil.encodeMd5(SysContext.getSystemConfig("account.default.pwd"), account.getLoginPwdKey()));
		HibernateUtil.updateEntityObject(account, null);
		
		JSONObject json = new JSONObject(2);
		json.put(ResourcePropNameConstants.ID, userId);
		return json;
	}
	
	/**
	 * 修改用户关联账户的登录密码
	 * @param jsonObject
	 * @return
	 */
	public Object updatePassword(JSONObject jsonObject){
		Object userId = jsonObject.get(ResourcePropNameConstants.ID);
		if(StrUtils.isEmpty(userId)){
			return "要修改密码的用户id不能为空";
		}
		Object oldPassword = jsonObject.get("oldPassword");
		if(StrUtils.isEmpty(oldPassword)){
			return "旧密码不能为空";
		}
		Object password = jsonObject.get("password");
		if(StrUtils.isEmpty(password)){
			return "新密码不能为空";
		}
		Object confirmPassword = jsonObject.get("confirmPassword");
		if(StrUtils.isEmpty(confirmPassword)){
			return "确认密码不能为空";
		}
		if(!password.equals(confirmPassword)){
			return "两次新密码不一致";
		}
		
		String userIdStr = userId.toString();
		if(!accountIsExists(userIdStr)){
			return "该用户不存在账户信息，无法修改密码，或先创建关联的账户信息";
		}
		
		SysAccount account = getObjectById(userIdStr, SysAccount.class);
		if(!CryptographyUtil.encodeMd5(oldPassword.toString(), account.getLoginPwdKey()).equals(account.getLoginPwd())){
			return "旧密码错误";
		}
		
		String newPassword = CryptographyUtil.encodeMd5(password.toString(), account.getLoginPwdKey());
		if(newPassword.equals(account.getLoginPwd())){
			return "新密码不能和旧密码相同";
		}
		HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.UPDATE, "update SysAccount set loginPwd=? where "+ ResourcePropNameConstants.ID +"=?", newPassword, userIdStr);
		
		JSONObject json = new JSONObject(3);
		json.put(ResourcePropNameConstants.ID, userIdStr);
		json.put("password", newPassword);
		return json;
	}
	
	/**
	 * 重置用户关联账户的登陆密码
	 * @param userId
	 * @return
	 */
	public Object resetPassword(String userId) {
		if(!accountIsExists(userId)){
			return "该用户不存在账户信息，无法修改密码，或先创建关联的账户信息";
		}
		SysAccount account = getObjectById(userId, SysAccount.class);
		String defaultPassword = CryptographyUtil.encodeMd5(SysContext.getSystemConfig("account.default.pwd"), account.getLoginPwdKey());
		HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.UPDATE, "update SysAccount set loginPwd=? where "+ ResourcePropNameConstants.ID +"=?", defaultPassword, userId);
		
		JSONObject json = new JSONObject(2);
		json.put(ResourcePropNameConstants.ID, userId);
		return json;
	}
	
	/**
	 * 验证工号是否已经存在
	 * @param user
	 * @return 
	 */
	private String validWorkNoIsExists(SysUser user) {
		String workNo = user.getWorkNo();
		String currentCustomerId = CurrentThreadContext.getCurrentAccountOnlineStatus().getCustomerId();
		
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from SysUser where workNo=? and customerId=?", workNo, currentCustomerId);
		if(count > 0){
			return "系统已经存在工号为["+workNo+"]的用户";
		}
		
		// 如果同时创建账户，则要去账户表中去判断，是否有重名的loginName,workNo
		if(user.getIsCreateAccount() == 1){
			count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from SysAccount where (loginName=? or workNo=?) and customerId=?", workNo, workNo, currentCustomerId);
			if(count > 0){
				return "系统已经存在[登录名/工号]为["+workNo+"]的账户";
			}
		}
		return null;
	}
	
	/**
	 * 验证email邮箱是否已经存在
	 * @param user
	 * @return 
	 */
	private String validEmailIsExists(SysUser user) {
		String email = user.getEmail();
		if(StrUtils.isEmpty(email)){
			return null;
		}
		String currentCustomerId = CurrentThreadContext.getCurrentAccountOnlineStatus().getCustomerId();
		
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from SysUser where email=? and customerId=?", email, currentCustomerId);
		if(count > 0){
			return "系统已经存在邮箱为["+email+"]的用户";
		}
		
		// 如果同时创建账户，则要去账户表中去判断，是否有重名的email
		if(user.getIsCreateAccount() == 1){
			count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from SysAccount where email=? and customerId=?", email, currentCustomerId);
			if(count > 0){
				return "系统已经存在邮箱为["+email+"]的账户";
			}
		}
		return null;
	}
	
	/**
	 * 验证tel手机号是否已经存在
	 * @param user
	 * @return 
	 */
	private String validTelIsExists(SysUser user) {
		String tel = user.getTel();
		if(StrUtils.isEmpty(tel)){
			return null;
		}
		String currentCustomerId = CurrentThreadContext.getCurrentAccountOnlineStatus().getCustomerId();
		
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from SysUser where tel=? and customerId=?", tel, currentCustomerId);
		if(count > 0){
			return "系统已经存在手机号为["+tel+"]的用户";
		}
		
		// 如果同时创建账户，则要去账户表中去判断，是否有重名的tel
		if(user.getIsCreateAccount() == 1){
			count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from SysAccount where tel=? and customerId=?", tel, currentCustomerId);
			if(count > 0){
				return "系统已经存在手机号为["+tel+"]的账户";
			}
		}
		return null;
	}
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	public Object saveUser(SysUser user){
		String ba = CurrentThreadContext.getCurrentAccountOnlineStatus().isBuiltinAccount(user.getId());
		if(ba != null){
			return "不能对内置用户["+ba+"]进行添加操作";
		}
		
		String result = validWorkNoIsExists(user);
		if(result == null){
			result = validEmailIsExists(user);
		}
		if(result == null){
			result = validTelIsExists(user);
		}
		if(result == null){
			if(user.getIsCreateAccount() == 1){
				JSONObject account = (JSONObject)operAccount(user, false);
				user.setId(account.getString(ResourcePropNameConstants.ID));
			}
			String userId = ResourceHandlerUtil.getIdentity();
			
			// 保存部门
			if(StrUtils.isEmpty(user.getDeptId())){
				user.setDeptId("NONE");
			}else{
				JSONObject udLink = ResourceHandlerUtil.getDataLinksObject(userId, user.getDeptId(), 1, null, null);
				udLink.put("isMain", "1");
				HibernateUtil.saveObject(sysUserDeptLinks, udLink, null);
			}
			
			// 保存岗位
			if(StrUtils.isEmpty(user.getPositionId())){
				user.setPositionId("NONE");
			}else{
				JSONObject upLink = ResourceHandlerUtil.getDataLinksObject(userId, user.getPositionId(), 1, null, null);
				upLink.put("isMain", "1");
				HibernateUtil.saveObject(sysUserPositionLinks, upLink, null);
			}
			
			// 保存角色
			if(StrUtils.isEmpty(user.getRoleId())){
				user.setRoleId("NONE");
			}else{
				JSONObject urLink = ResourceHandlerUtil.getDataLinksObject(userId, user.getRoleId(), 1, null, null);
				urLink.put("isMain", "1");
				HibernateUtil.saveObject(sysUserRoleLinks, urLink, null);
			}
			
			user.setId(userId);
			JSONObject userJsonObject = HibernateUtil.saveObject(user, null);
			return userJsonObject;
		}
		return result;
	}
	
	/**
	 * 修改用户
	 * @param user
	 * @return
	 */
	public Object updateUser(SysUser user){
		String ba = CurrentThreadContext.getCurrentAccountOnlineStatus().isBuiltinAccount(user.getId());
		if(ba != null){
			return "不能对内置用户["+ba+"]进行修改操作";
		}
		
		SysUser oldUser = getObjectById(user.getId(), SysUser.class);
		
		String result = null;
		if(!oldUser.getWorkNo().equals(user.getWorkNo())){
			result = validWorkNoIsExists(user);
		}
		if(result == null && (StrUtils.notEmpty(oldUser.getEmail()) && !oldUser.getEmail().equals(user.getEmail())) || (StrUtils.isEmpty(oldUser.getEmail()) && StrUtils.notEmpty(user.getEmail()))){
			result = validEmailIsExists(user);
		}
		if(result == null && (StrUtils.notEmpty(oldUser.getTel()) && !oldUser.getTel().equals(user.getTel())) || (StrUtils.isEmpty(oldUser.getTel()) && StrUtils.notEmpty(user.getTel()))){
			result = validTelIsExists(user);
		}
		if(result == null){
			if(user.getIsCreateAccount()==1){
				operAccount(user, false);
			}
			
			if(user.getUserStatus() != oldUser.getUserStatus()){
				BuiltinResourceInstance.getInstance("SysAccountCardService", SysAccountCardService.class).updateAccountCardStatus(new SysAccountCard(user.getId(), user.getUserStatus()));
			}
			
			JSONObject userJsonObject = HibernateUtil.updateEntityObject(user, null);
			String userId = oldUser.getId();
			
			// 可能修改部门
			if(StrUtils.isEmpty(user.getDeptId())){
				user.setDeptId("NONE");
			}
			if(!oldUser.getDeptId().equals(user.getDeptId())){
				if(!oldUser.getDeptId().equals("NONE")){
					HibernateUtil.deleteDataLinks(sysUserDeptLinks, userId, oldUser.getDeptId());
				}
				if(!user.getDeptId().equals("NONE")){
					JSONObject upLink = ResourceHandlerUtil.getDataLinksObject(userId, user.getDeptId(), 1, null, null);
					upLink.put("isMain", "1");
					HibernateUtil.saveObject(sysUserDeptLinks, upLink, null);
				}
			}
			
			// 可能修改岗位
			if(StrUtils.isEmpty(user.getPositionId())){
				user.setPositionId("NONE");
			}
			if(!oldUser.getPositionId().equals(user.getPositionId())){
				if(!oldUser.getPositionId().equals("NONE")){
					HibernateUtil.deleteDataLinks(sysUserPositionLinks, userId, oldUser.getPositionId());
				}
				if(!user.getPositionId().equals("NONE")){
					JSONObject upLink = ResourceHandlerUtil.getDataLinksObject(userId, user.getPositionId(), 1, null, null);
					upLink.put("isMain", "1");
					HibernateUtil.saveObject(sysUserPositionLinks, upLink, null);
				}
			}
			
			// 可能修改角色
			if(StrUtils.isEmpty(user.getRoleId())){
				user.setRoleId("NONE");
			}
			if(!oldUser.getRoleId().equals(user.getRoleId())){
				if(!oldUser.getRoleId().equals("NONE")){
					HibernateUtil.deleteDataLinks(sysUserRoleLinks, userId, oldUser.getRoleId());
				}
				if(!user.getRoleId().equals("NONE")){
					JSONObject urLink = ResourceHandlerUtil.getDataLinksObject(userId, user.getRoleId(), 1, null, null);
					urLink.put("isMain", "1");
					HibernateUtil.saveObject(sysUserRoleLinks, urLink, null);
				}
			}
			return userJsonObject;
		}
		return result;
	}

	/**
	 * 开通账户
	 * @param user
	 * @param isLoadUser
	 * @return
	 */
	public Object openAccount(SysUser user) {
		Object object = operAccount(user, true);
		if(object instanceof String){
			return object;
		}
		JSONObject jsonObject = new JSONObject(2);
		jsonObject.put(ResourcePropNameConstants.ID, user.getId());
		return jsonObject;
	}
	
	/**
	 * 操作账户
	 * @param originUser
	 * @param isLoadUser
	 * @return
	 */
	private Object operAccount(SysUser originUser, boolean isLoadUser) {
		SysUser user = originUser;
		if(isLoadUser){
			user = getObjectById(originUser.getId(), SysUser.class);
			user.setIsSyncLoginName(originUser.getIsSyncLoginName());
		}
		
		String ba = CurrentThreadContext.getCurrentAccountOnlineStatus().isBuiltinAccount(user.getId());
		if(ba != null){
			return "不能对内置用户["+ba+"]进行操作";
		}
		
		if(accountIsExists(user.getId())){
			SysAccount account = getObjectById(user.getId(), SysAccount.class);
			
			if(isLoadUser && account.getIsDelete() == 0){
				return "用户["+user.getName()+"]已经存在账户，禁止重复开通账户";
			}else{
				// 之前被删除了账户，这里要重置密码
				if(isLoadUser && account.getIsDelete() == 1){
					account.setIsDelete(0);
					account.setLoginPwd(CryptographyUtil.encodeMd5(SysContext.getSystemConfig("account.default.pwd"), account.getLoginPwdKey()));
				}
				
				if(user.getIsSyncLoginName() == 1){
					account.setLoginName(user.getWorkNo());
				}
				account.setTel(user.getTel());
				account.setEmail(user.getEmail());
				account.setWorkNo(user.getWorkNo());
				return BuiltinResourceInstance.getInstance("SysAccountService", SysAccountService.class).updateAccount(account);
			}
		}else{
			SysAccount account = new SysAccount();
			account.setId(user.getId());
			// ----
			account.setLoginName(user.getWorkNo());
			account.setTel(user.getTel());
			account.setEmail(user.getEmail());
			account.setWorkNo(user.getWorkNo());
			return BuiltinResourceInstance.getInstance("SysAccountService", SysAccountService.class).saveAccount(account);
		}
	}
	
	/**
	 * 关闭账户
	 * @param user
	 * @return
	 */
	public Object closeAccount(SysUser user) {
		String ba = CurrentThreadContext.getCurrentAccountOnlineStatus().isBuiltinAccount(user.getId());
		if(ba != null){
			return "不能对内置用户["+ba+"]进行操作";
		}
		
		user = getObjectById(user.getId(), SysUser.class);
		
		if(accountIsExists(user.getId())){
			SysAccount account = getObjectById(user.getId(), SysAccount.class);
			if(account.getIsDelete() == 0){
				return "用户["+user.getName()+"]不存在账户，无法关闭";
			}else if(account.getIsDelete() == 0){
				account.setIsDelete(1);
				HibernateUtil.updateEntityObject(account, null);
			}
		}else{
			return "用户["+user.getName()+"]不存在账户，无法关闭";
		}
		
		JSONObject jsonObject = new JSONObject(2);
		jsonObject.put(ResourcePropNameConstants.ID, user.getId());
		return jsonObject;
	}
	
	/**
	 * 删除用户
	 * @param userId
	 * @return
	 */
	public String deleteUser(String userId) {
		String ba = CurrentThreadContext.getCurrentAccountOnlineStatus().isBuiltinAccount(userId);
		if(ba != null){
			return "不能对内置用户["+ba+"]进行删除操作";
		}
		
		SysUser user = getObjectById(userId, SysUser.class);
		// 删除用户
		HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.UPDATE, "update SysUser set isDelete=1, lastUpdateDate=? where "+ResourcePropNameConstants.ID+" = ?", new Date(), userId);
		
		// 删除账户
		if(accountIsExists(userId)){
			BuiltinResourceInstance.getInstance("SysAccountService", SysAccountService.class).deleteAccount(userId);
		}
		
		commonDeleteOper(user, userId);
		return null;
	}
	
	/**
	 * 物理删除用户
	 * @param userId
	 * @return
	 */
	public String physicalDelete(String userId) {
		String ba = CurrentThreadContext.getCurrentAccountOnlineStatus().isBuiltinAccount(userId);
		if(ba != null){
			return "不能对内置用户["+ba+"]进行删除操作";
		}
		
		SysUser user = getObjectById(userId, SysUser.class);
		// 删除用户
		HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.DELETE, "delete SysUser where "+ResourcePropNameConstants.ID+" = ?", userId);
		
		// 删除账户
		if(accountIsExists(userId)){
			BuiltinResourceInstance.getInstance("SysAccountService", SysAccountService.class).physicalDeleteAccount(userId);
		}
		
		commonDeleteOper(user, userId);
		return null;
	}
	
	/**
	 * 通用的删除
	 * @param user
	 * @param userId
	 */
	private void commonDeleteOper(SysUser user, String userId){
		// 删除所属的部门
		if(StrUtils.notEmpty(user.getDeptId())){
			HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.DELETE, "delete SysUserDeptLinks where leftId = ?", userId);
		}
		// 删除所属的职务
		if(StrUtils.notEmpty(user.getPositionId())){
			HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.DELETE, "delete SysUserPositionLinks where leftId = ?", userId);
		}
		// 删除所属的角色
		HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.DELETE, "delete SysUserRoleLinks where leftId = ?", userId);
		// 删除所属的用户组
		HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.DELETE, "delete SysUserGroupDetail where userId = ?", userId);
	}
}
