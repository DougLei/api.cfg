package com.king.tooth.sys.service.sys;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.annotation.Service;
import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.builtin.data.BuiltinObjectInstance;
import com.king.tooth.sys.entity.sys.SysAccount;
import com.king.tooth.sys.entity.sys.SysUser;
import com.king.tooth.sys.service.AbstractService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.CryptographyUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 人员信息表Service
 * @author DougLei
 */
@Service
public class SysUserService extends AbstractService{
	
	private static final String sysUserDeptLinks = "SysUserDeptLinks";
	private static final String sysUserPositionLinks = "SysUserPositionLinks";
	
	/**
	 * 账户是否存在
	 * @param id
	 * @return
	 */
	private boolean accountIsExists(String id){
		Object obj = HibernateUtil.executeUniqueQueryByHqlArr("select "+ResourcePropNameConstants.ID+" from SysAccount where " + ResourcePropNameConstants.ID +"=?", id);
		if(obj == null){
			return false;
		}
		return true;
	}
	
	/**
	 * 修改用户关联的账户密码
	 * @param userId
	 * @param newLoginPwd
	 * @return
	 */
	public Object uploadUserLoginPwd(String userId, String newLoginPwd){
		if(!accountIsExists(userId)){
			return "该用户不存在账户信息，无法修改密码，或先创建关联的账户信息";
		}
		return BuiltinObjectInstance.accountService.uploadAccounLoginPwd(userId, newLoginPwd);
	}
	
	/**
	 * 验证工号是否已经存在
	 * @param user
	 * @return 
	 */
	private String validWorkNoIsExists(SysUser user) {
		String workNo = user.getWorkNo();
		String currentCustomerId = CurrentThreadContext.getCurrentAccountOnlineStatus().getCustomerId();
		
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from SysUser where workNo=? and customerId=? and isDelete=0", workNo, currentCustomerId);
		if(count > 0){
			return "系统已经存在工号为["+workNo+"]的用户";
		}
		
		// 如果同时创建账户，则要去账户表中去判断，是否有重名的loginName
		if(user.getIsCreateAccount() == 1){
			count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from SysAccount where loginName=? and customerId=? and isDelete=0", workNo, currentCustomerId);
			if(count > 0){
				return "系统已经存在登录名为["+workNo+"]的账户";
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
		
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from SysUser where email=? and customerId=? and isDelete=0", email, currentCustomerId);
		if(count > 0){
			return "系统已经存在邮箱为["+email+"]的用户";
		}
		
		// 如果同时创建账户，则要去账户表中去判断，是否有重名的email
		if(user.getIsCreateAccount() == 1){
			count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from SysAccount where email=? and customerId=? and isDelete=0", email, currentCustomerId);
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
		
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from SysUser where tel=? and customerId=? and isDelete=0", tel, currentCustomerId);
		if(count > 0){
			return "系统已经存在手机号为["+tel+"]的用户";
		}
		
		// 如果同时创建账户，则要去账户表中去判断，是否有重名的tel
		if(user.getIsCreateAccount() == 1){
			count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from SysAccount where tel=? and customerId=? and isDelete=0", tel, currentCustomerId);
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
		String result = validWorkNoIsExists(user);
		if(result == null){
			result = validEmailIsExists(user);
		}
		if(result == null){
			result = validTelIsExists(user);
		}
		if(result == null){
			if(user.getIsCreateAccount() == 1){
				SysAccount account = new SysAccount();
				account.setLoginName(user.getWorkNo());
				account.setLoginPwdKey(ResourceHandlerUtil.getLoginPwdKey());
				account.setLoginPwd(CryptographyUtil.encodeMd5(SysConfig.getSystemConfig("account.default.pwd"), account.getLoginPwdKey()));
				account.setTel(user.getTel());
				account.setEmail(user.getEmail());
				String accountId = HibernateUtil.saveObject(account, null).getString(ResourcePropNameConstants.ID);
				user.setId(accountId);
			}
			JSONObject userJsonObject = HibernateUtil.saveObject(user, null);
			String userId = userJsonObject.getString(ResourcePropNameConstants.ID);
			
			// 保存部门
			if(StrUtils.notEmpty(user.getDeptId())){
				JSONObject udLink = ResourceHandlerUtil.getDataLinksObject(userId, user.getDeptId(), "1", null, null);
				udLink.put("isMain", "1");
				HibernateUtil.saveObject(sysUserDeptLinks, udLink, null);
			}
			// 保存岗位
			if(StrUtils.notEmpty(user.getPositionId())){
				JSONObject upLink = ResourceHandlerUtil.getDataLinksObject(userId, user.getPositionId(), "1", null, null);
				upLink.put("isMain", "1");
				HibernateUtil.saveObject(sysUserPositionLinks, upLink, null);
			}
			
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
		SysUser oldUser = getObjectById(user.getId(), SysUser.class);
		
		String accountId = user.getId();
		boolean accountIsExists = accountIsExists(accountId);
		boolean modifyAccountInfo = false;// 标识是否修改账户信息
		SysAccount account = null;
		if(accountIsExists){
			account = getObjectById(accountId, SysAccount.class);
		}else if(user.getIsCreateAccount() == 1){
			account = new SysAccount();
		}
		
		String result = null;
		if(!oldUser.getWorkNo().equals(user.getWorkNo())){
			modifyAccountInfo = true;
			result = validWorkNoIsExists(user);
			if(result == null && user.getIsCreateAccount() == 1){
				account.setLoginName(user.getWorkNo());
			}
		}
		if(result == null && (StrUtils.notEmpty(oldUser.getEmail()) && !oldUser.getEmail().equals(user.getEmail())) || (StrUtils.isEmpty(oldUser.getEmail()) && StrUtils.notEmpty(user.getEmail()))){
			modifyAccountInfo = true;
			result = validEmailIsExists(user);
			if(result == null && user.getIsCreateAccount() == 1){
				account.setEmail(user.getEmail());
			}
		}
		if(result == null && (StrUtils.notEmpty(oldUser.getTel()) && !oldUser.getTel().equals(user.getTel())) || (StrUtils.isEmpty(oldUser.getTel()) && StrUtils.notEmpty(user.getTel()))){
			modifyAccountInfo = true;
			result = validTelIsExists(user);
			if(result == null && user.getIsCreateAccount() == 1){
				account.setTel(user.getTel());
			}
		}
		if(result == null){
			if(account != null && modifyAccountInfo){
				if(accountIsExists){
					HibernateUtil.updateObject(account, null);
				}else{
					account.setLoginPwdKey(ResourceHandlerUtil.getLoginPwdKey());
					account.setLoginPwd(CryptographyUtil.encodeMd5(SysConfig.getSystemConfig("account.default.pwd"), account.getLoginPwdKey()));
					HibernateUtil.saveObject(account, null);
				}
			}
			JSONObject userJsonObject = HibernateUtil.updateObject(user, null);
			String userId = oldUser.getId();
			
			// 可能修改部门
			if((StrUtils.notEmpty(oldUser.getDeptId()) && !oldUser.getDeptId().equals(user.getDeptId())) || (StrUtils.isEmpty(oldUser.getDeptId()) && StrUtils.notEmpty(user.getDeptId()))){
				if(StrUtils.notEmpty(oldUser.getDeptId())){
					HibernateUtil.deleteDataLinks(sysUserDeptLinks, userId, oldUser.getDeptId());
				}
				
				if(StrUtils.notEmpty(user.getDeptId())){
					JSONObject upLink = ResourceHandlerUtil.getDataLinksObject(userId, user.getPositionId(), "1", null, null);
					upLink.put("isMain", "1");
					HibernateUtil.saveObject(sysUserDeptLinks, upLink, null);
				}
			}
			
			// 可能修改岗位
			if((StrUtils.notEmpty(oldUser.getPositionId()) && !oldUser.getPositionId().equals(user.getPositionId())) || (StrUtils.isEmpty(oldUser.getPositionId()) && StrUtils.notEmpty(user.getPositionId()))){
				if(StrUtils.notEmpty(oldUser.getPositionId())){
					HibernateUtil.deleteDataLinks(sysUserPositionLinks, userId, oldUser.getPositionId());
				}
				
				if(StrUtils.notEmpty(user.getPositionId())){
					JSONObject upLink = ResourceHandlerUtil.getDataLinksObject(userId, user.getPositionId(), "1", null, null);
					upLink.put("isMain", "1");
					HibernateUtil.saveObject(sysUserPositionLinks, upLink, null);
				}
			}
			return userJsonObject;
		}
		return result;
	}

	/**
	 * 开通账户
	 * @param user
	 * @return
	 */
	public Object openAccount(SysUser user) {
		user = getObjectById(user.getId(), SysUser.class);
		
		if(accountIsExists(user.getId())){
			SysAccount account = getObjectById(user.getId(), SysAccount.class);
			if(account.getIsDelete() == 0){
				return "用户["+user.getName()+"]已经存在账户，禁止重复开通账户";
			}else if(account.getIsDelete() == 1){
				account.setIsDelete(0);
				account.setLastUpdateDate(new Date());
				// ----
				account.setLoginName(user.getWorkNo());
				account.setLoginPwd(CryptographyUtil.encodeMd5(SysConfig.getSystemConfig("account.default.pwd"), account.getLoginPwdKey()));
				account.setTel(user.getTel());
				account.setEmail(user.getEmail());
				HibernateUtil.updateObject(account, null);
			}
		}else{
			SysAccount account = new SysAccount();
			account.setId(user.getId());
			// ----
			account.setLoginName(user.getWorkNo());
			account.setLoginPwdKey(ResourceHandlerUtil.getLoginPwdKey());
			account.setLoginPwd(CryptographyUtil.encodeMd5(SysConfig.getSystemConfig("account.default.pwd"), account.getLoginPwdKey()));
			account.setTel(user.getTel());
			account.setEmail(user.getEmail());
			HibernateUtil.saveObject(account, null);
		}
		
		JSONObject jsonObject = new JSONObject(1);
		jsonObject.put(ResourcePropNameConstants.ID, user.getId());
		return jsonObject;
	}
	
	/**
	 * 删除用户
	 * @param userId
	 * @return
	 */
	public String deleteUser(String userId) {
		SysUser user = getObjectById(userId, SysUser.class);
		// 删除用户
		HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.UPDATE, "update SysUser set isDelete=1, lastUpdateDate=? where "+ResourcePropNameConstants.ID+" = ?", new Date(), userId);
		
		// 删除账户
		if(accountIsExists(userId)){
			HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.UPDATE, "update SysAccount set isDelete=1, lastUpdateDate=? where "+ResourcePropNameConstants.ID+" = ?", new Date(), userId);
			BuiltinObjectInstance.accountService.deleteTokenInfoByAccountId(userId);
		}
		// 删除所属的部门
		if(StrUtils.notEmpty(user.getDeptId())){
			HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "delete SysUserDeptLinks where leftId = ?", userId);
		}
		// 删除所属的职务
		if(StrUtils.notEmpty(user.getPositionId())){
			HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "delete SysUserPositionLinks where leftId = ?", userId);
		}
		// 删除所属的角色
		HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "delete SysUserRoleLinks where leftId = ?", userId);
		// 删除所属的用户组
		HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "delete SysUserGroupDetail where userId = ?", userId);
		
		return null;
	}
}
