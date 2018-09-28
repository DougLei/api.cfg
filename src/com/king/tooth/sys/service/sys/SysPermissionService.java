package com.king.tooth.sys.service.sys;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.annotation.Service;
import com.king.tooth.constants.PermissionConstants;
import com.king.tooth.sys.builtin.data.BuiltinObjectInstance;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.entity.sys.SysAccountOnlineStatus;
import com.king.tooth.sys.entity.sys.SysPermissionPriority;
import com.king.tooth.sys.entity.sys.SysUserPermissionCache;
import com.king.tooth.sys.entity.sys.permission.SysPermissionExtend;
import com.king.tooth.sys.service.AService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 权限信息表service
 * @author DougLei
 */
@Service
public class SysPermissionService extends AService{
	
	/**
	 * 得到系统权限的优先级集合
	 * @return
	 */
	private List<SysPermissionPriority> getPermissionPriorities(){
		List<SysPermissionPriority> permissionPriorities = HibernateUtil.extendExecuteListQueryByHqlArr(SysPermissionPriority.class, null, null, hql, CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
		if(permissionPriorities == null || permissionPriorities.size() != BuiltinObjectInstance.permissionPriorities.size()){
			permissionPriorities = BuiltinObjectInstance.permissionPriorities;
		}
		return permissionPriorities;
	}
	private static final String hql = "from SysPermissionPriority where projectId=? and customerId=? order by lv asc";
	
	
	// 第一次查询权限信息集合的hql语句
	private static final String queryPermissionHql = "from SysPermission where objId = ? and objType = ? and (refParentResourceId is null or refParentResourceId = '') and projectId = ? and customerId = ?";
	// 后续递归查询权限信息集合的hql语句
	private static final String recursiveQueryPermissionHql = "from SysPermission where objId = ? and objType = ? and (refParentResourceId = ? or refParentResourceCode = ?) and projectId = ? and customerId = ?";
	
	/**
	 * 根据用戶id，获取对应的的权限集合
	 * @param accountOnlineStatus
	 * @return
	 */
	public SysPermissionExtend findAccountOfPermissions(SysAccountOnlineStatus accountOnlineStatus){
		String projectId = CurrentThreadContext.getProjectId();
		String customerId = CurrentThreadContext.getCustomerId();
		
		List<SysPermissionExtend> permissions = new ArrayList<SysPermissionExtend>();
		List<SysPermissionExtend> newPermissions = new ArrayList<SysPermissionExtend>();
		List<SysPermissionExtend> tmpPermissions = null;
		
		List<SysPermissionPriority> permissionPriorities = getPermissionPriorities();
		for (SysPermissionPriority permissionPriority : permissionPriorities) {
			// 用戶
			if(PermissionConstants.OBJ_TYPE_USER.equals(permissionPriority.getPermissionType())){
				tmpPermissions = getRootPermissionsByData(PermissionConstants.OBJ_TYPE_USER, accountOnlineStatus.getUserId(), projectId, customerId);
				setSubPermissionsByData(tmpPermissions, accountOnlineStatus.getUserId(), PermissionConstants.OBJ_TYPE_USER, projectId, customerId);
				mergePermissions(newPermissions, tmpPermissions);
			}
			// 账户
			else if(PermissionConstants.OBJ_TYPE_ACCOUNT.equals(permissionPriority.getPermissionType())){
				tmpPermissions = getRootPermissionsByData(PermissionConstants.OBJ_TYPE_ACCOUNT, accountOnlineStatus.getAccountId(), projectId, customerId);
				setSubPermissionsByData(tmpPermissions, accountOnlineStatus.getAccountId(), PermissionConstants.OBJ_TYPE_ACCOUNT, projectId, customerId);
				mergePermissions(newPermissions, tmpPermissions);
			}
			// 角色
			else if(PermissionConstants.OBJ_TYPE_ROLE.equals(permissionPriority.getPermissionType())){
				List<Object> roleIds = accountOnlineStatus.getRoleIds();
				if(roleIds != null && roleIds.size() > 0){
					roleIds = processSamePermissionTypeLevel(roleIds, permissionPriority.getSamePermissionTypeLv());
					
					for (Object roleId : roleIds) {
						tmpPermissions = getRootPermissionsByData(PermissionConstants.OBJ_TYPE_ROLE, roleId, projectId, customerId);
						setSubPermissionsByData(tmpPermissions, roleId, PermissionConstants.OBJ_TYPE_ROLE, projectId, customerId);
						mergePermissions(newPermissions, tmpPermissions);
					}
					roleIds.clear();
				}
			}
			// 部门
			else if(PermissionConstants.OBJ_TYPE_DEPT.equals(permissionPriority.getPermissionType())){
				List<Object> deptIds = accountOnlineStatus.getDeptIds();
				if(deptIds != null && deptIds.size() > 0){
					deptIds = processSamePermissionTypeLevel(deptIds, permissionPriority.getSamePermissionTypeLv());		
					
					for (Object deptId : deptIds) {
						tmpPermissions = getRootPermissionsByData(PermissionConstants.OBJ_TYPE_DEPT, deptId, projectId, customerId);
						setSubPermissionsByData(newPermissions, deptId, PermissionConstants.OBJ_TYPE_DEPT, projectId, customerId);
						mergePermissions(newPermissions, tmpPermissions);
					}
					deptIds.clear();
				}
			}
			// 岗位
			else if(PermissionConstants.OBJ_TYPE_POSITION.equals(permissionPriority.getPermissionType())){
				List<Object> positionIds = accountOnlineStatus.getPositionIds();
				if(positionIds != null && positionIds.size() > 0){
					positionIds = processSamePermissionTypeLevel(positionIds, permissionPriority.getSamePermissionTypeLv());	
					
					for (Object positionId : positionIds) {
						tmpPermissions = getRootPermissionsByData(PermissionConstants.OBJ_TYPE_POSITION, positionId, projectId, customerId);
						setSubPermissionsByData(newPermissions, positionId, PermissionConstants.OBJ_TYPE_POSITION, projectId, customerId);
						mergePermissions(newPermissions, tmpPermissions);
					}
					positionIds.clear();
				}
			}
			// 用户组
			else if(PermissionConstants.OBJ_TYPE_USERGROUP.equals(permissionPriority.getPermissionType())){
				List<Object> userGroupIds = accountOnlineStatus.getUserGroupIds();
				if(userGroupIds != null && userGroupIds.size() > 0){
					userGroupIds = processSamePermissionTypeLevel(userGroupIds, permissionPriority.getSamePermissionTypeLv());	
					
					for (Object userGroupId : userGroupIds) {
						tmpPermissions = getRootPermissionsByData(PermissionConstants.OBJ_TYPE_USERGROUP, userGroupId, projectId, customerId);
						setSubPermissionsByData(newPermissions, userGroupId, PermissionConstants.OBJ_TYPE_USERGROUP, projectId, customerId);
						mergePermissions(newPermissions, tmpPermissions);
					}
					userGroupIds.clear();
				}
			}
			
			mergePermissions(permissions, newPermissions);
		}
		
		if(permissions.size() > 0){
			SysPermissionExtend permission = new SysPermissionExtend();
			permission.setRefResourceCode("ROOT");
			permission.setRefResourceId("ROOT");
			permission.setRefResourceType("ROOT");
			permission.setChildren(permissions);
			return permission;
		}
		return null;
	}
	
	/**
	 * 处理相同权限类型的优先级：这里可以按照等级顺序，存储多个id，用,分割，越前面的，优先级越高；第一个优先级最高
	 * @param permissionTypeIds
	 * @param samePermissionTypePriorityLevel
	 * @return
	 */
	private List<Object> processSamePermissionTypeLevel(List<Object> permissionTypeIds, String samePermissionTypePriorityLevel){
		if(StrUtils.isEmpty(samePermissionTypePriorityLevel)){
			return permissionTypeIds;
		}
		List<Object> tmpPermissionTypeIds = new ArrayList<Object>(permissionTypeIds.size());
		String[] samePermissionTypePriorityLevelArr = samePermissionTypePriorityLevel.split(",");
		
		for (String sptpl : samePermissionTypePriorityLevelArr) {
			if(permissionTypeIds.contains(sptpl)){
				tmpPermissionTypeIds.add(sptpl);
				permissionTypeIds.remove(sptpl);
			}
		}
		if(permissionTypeIds.size() > 0){
			tmpPermissionTypeIds.addAll(permissionTypeIds);
			permissionTypeIds.clear();
		}
		return tmpPermissionTypeIds;
	}
	
	/**
	 * 获取引用的数据权限根数据集合
	 * @param objType
	 * @param objId
	 * @param projectId
	 * @param customerId
	 * @return
	 */
	private List<SysPermissionExtend> getRootPermissionsByData(String objType, Object objId, String projectId, String customerId){
		return HibernateUtil.extendExecuteListQueryByHqlArr(SysPermissionExtend.class, null, null, queryPermissionHql, 
				objId, objType, projectId, customerId);
	}
	/**
	 * 获取引用的数据权限子数据集合
	 * @param permissions
	 * @param objId
	 * @param objType
	 * @param projectId
	 * @param customerId
	 */
	private void setSubPermissionsByData(List<SysPermissionExtend> permissions, Object objId, String objType, String projectId, String customerId) {
		if(permissions == null || permissions.size() == 0){
			return;
		}
		for (SysPermissionExtend p : permissions) {
			p.setChildren(HibernateUtil.extendExecuteListQueryByHqlArr(SysPermissionExtend.class, null, null, recursiveQueryPermissionHql, 
					objId, objType, p.getRefResourceId(), p.getRefResourceCode(), projectId, customerId));
			setSubPermissionsByData(p.getChildren(), objId, objType, projectId, customerId);
		}
	}

	
	
	
	/**
	 * 将新的权限和原有权限合并，取并集
	 * @param permissions
	 * @param newPermissions
	 * @return
	 */
	private void mergePermissions(List<SysPermissionExtend> permissions, List<SysPermissionExtend> newPermissions){
		if(newPermissions == null || newPermissions.size() == 0){
			return;
		}
		if(permissions.size() == 0){
			permissions.addAll(newPermissions);
			newPermissions.clear();
		}else{
			doMerge(permissions, newPermissions);
		}
	}
	/**
	 * 做合并操作
	 * @param permissions
	 * @param newPermissions
	 */
	private void doMerge(List<SysPermissionExtend> permissions, List<SysPermissionExtend> newPermissions){
		if(newPermissions == null || newPermissions.size() == 0){
			return;
		}
		try {
			if(permissions.size() == 0){
				permissions.addAll(newPermissions);
				return;
			}
			
			for (SysPermissionExtend np : newPermissions) {
				boolean unExists = true;
				for (SysPermissionExtend p : permissions) {
					if(np.getRefResourceId().equals(p.getRefResourceId()) || np.getRefResourceCode().equals(p.getRefResourceCode())){
						unExists = false;
						
						if(!PermissionConstants.PERMISSION_PRIORITY_IS_OPEN){
							// 现在是只要配置了相应的权限，就能访问和操作，没有体现出优先级
							if(np.getIsVisibility()==1){
								p.setIsVisibility(1);
							}
							if(np.getIsOper()==1){
								p.setIsOper(1);
							}
							// 如果注释掉上面两行if代码块，则可以体现出优先级
							// 例如A角色和B角色都对同一个功能C有权限控制，A角色可以操作C，B角色不可以操作C。如果A的优先级高，则最后可以操作功能C；如果B的优先级高，则最终权限不能操作功能C
						}
						
						if(p.getChildren() == null){
							p.setChildren(new ArrayList<SysPermissionExtend>());
						}
						doMerge(p.getChildren(), np.getChildren());
						break;
					}
				}
				if(unExists){
					permissions.add(np);
				}
			}
		} finally{
			newPermissions.clear();
		}
	}
	// ----------------------------------------------------------------------------------------------------

	/**
	 * 过滤出指定资源类型的权限
	 * @param permission
	 * @param refResourceType 过滤到哪种资源类型停止，以下的子权限信息都舍去
	 * @return
	 */
	public void filterPermission(SysPermissionExtend permission, String refResourceType) {
		if(permission != null && permission.getChildren() != null && permission.getChildren().size() > 0){
			recursiveFilterPermission(permission.getChildren(), refResourceType);
		}
	}

	/**
	 * 递归过滤出指定资源类型的权限
	 * @param permissions
	 * @param refResourceType
	 * @return
	 */
	private void recursiveFilterPermission(List<SysPermissionExtend> permissions, String refResourceType) {
		if(permissions != null && permissions.size() > 0){
			for (SysPermissionExtend p : permissions) {
				if(p.getRefResourceType().equals(refResourceType) && !childrenResourceTypeIsSameWithResourceType(p.getChildren(), refResourceType)){
					recursiveClearPermission(p.getChildren());
				}else{
					recursiveFilterPermission(p.getChildren(), refResourceType);
				}
			}
		}
	}
	/**
	 * 子[权限]的资源类型和指定的资源类型是否相同
	 * @param childrenPermissions
	 * @param refResourceType
	 * @return
	 */
	private boolean childrenResourceTypeIsSameWithResourceType(List<SysPermissionExtend> childrenPermissions, String refResourceType) {
		if(childrenPermissions == null || childrenPermissions.size() == 0){
			return false;
		}
		return refResourceType.equals(childrenPermissions.get(0).getRefResourceType());
	}

	/**
	 * 递归清空权限集合
	 * @param permissions
	 */
	private void recursiveClearPermission(List<SysPermissionExtend> permissions) {
		if(permissions != null && permissions.size() > 0){
			for (SysPermissionExtend p : permissions) {
				recursiveClearPermission(p.getChildren());
			}
			permissions.clear();
		}
	}
	// --------------------------------------------------------------------------------------
	private static final String queryUserPermissionCacheHql = "from SysUserPermissionCache where userId = ? and customerId =?";
	/**
	 * 获取用户的权限缓存对象
	 * @param accountOnlineStatus
	 * @return
	 */
	public SysUserPermissionCache getSysUserPermissionCache(SysAccountOnlineStatus accountOnlineStatus){
		SysUserPermissionCache sapc = HibernateUtil.extendExecuteUniqueQueryByHqlArr(SysUserPermissionCache.class, queryUserPermissionCacheHql, accountOnlineStatus.getUserId(), CurrentThreadContext.getCustomerId());
		
		SysPermissionExtend permission = null;
		if(sapc == null){
			permission = BuiltinResourceInstance.getInstance("SysPermissionService", SysPermissionService.class).findAccountOfPermissions(accountOnlineStatus);
			
			sapc = new SysUserPermissionCache();
			sapc.setUserId(accountOnlineStatus.getUserId());
			sapc.setPermission(JsonUtil.toJsonString(permission, false));
			HibernateUtil.saveObject(sapc, null);
		}else if(StrUtils.isEmpty(sapc.getPermission())){
			permission = BuiltinResourceInstance.getInstance("SysPermissionService", SysPermissionService.class).findAccountOfPermissions(accountOnlineStatus);
			
			sapc.setPermission(JsonUtil.toJsonString(permission, false));
			HibernateUtil.updateObject(sapc, null);
		}else{
			permission = JsonUtil.parseObject(sapc.getPermission(), SysPermissionExtend.class);
		}
		
		sapc.setPermissionObject(permission);
		return sapc;
	}
	
	// --------------------------------------------------------------------------------------
	/**
	 * 计算获取当前用户，指定code的功能权限以及子权限集合
	 * @param code
	 * @param recursive
	 * @param deep
	 * @return
	 */
	public Object calcPermissionByCode(String code, boolean recursive, int deep) {
		SysAccountOnlineStatus accountOnlineStatus = CurrentThreadContext.getCurrentAccountOnlineStatus();
		
		// 管理员或系统开发人员，不做权限控制，返回ALL，标识可以访问所有功能
		if(accountOnlineStatus.isAdministrator() || accountOnlineStatus.isDeveloper()){
			return JsonUtil.toJsonObject(BuiltinObjectInstance.allPermission);
		}
		
		SysUserPermissionCache sapc = getSysUserPermissionCache(accountOnlineStatus);
		SysPermissionExtend permission = sapc.getPermissionObject();
		
		if((accountOnlineStatus.isNormal())
				&& (permission == null || permission.getChildren() == null || permission.getChildren().size() == 0)){
			return "您还未分配系统功能权限，请联系系统管理员";
		}
		
		SysPermissionExtend finalPermission = calcPermissionByCode(code, permission.getChildren());
		if(finalPermission == null){
			return "您不具有code为["+code+"]的操作权限，请联系系统管理员";
		}
		
		if(recursive && (deep > 0 || deep == -1)){
			if(deep > 0){
				recursiveClearPermission(finalPermission.getChildren(), deep);
			}
		}else{
			recursiveClearPermission(finalPermission.getChildren());
		}
		JSONObject json = JsonUtil.toJsonObject(finalPermission);
		recursiveClearPermission(permission.getChildren());
		return json;
	}
	
	/**
	 * 计算获取当前用户，指定code的功能权限以及子权限集合
	 * @param code
	 * @param permissions
	 * @return
	 */
	private SysPermissionExtend calcPermissionByCode(String code, List<SysPermissionExtend> permissions) {
		if(permissions != null && permissions.size() > 0){
			for (SysPermissionExtend p : permissions) {
				if(code.startsWith(p.getRefResourceCode())){
					if(code.equals(p.getRefResourceCode())){
						return p;
					}else{
						return calcPermissionByCode(code, p.getChildren());
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 指定递归深度，清空深度外的权限集合
	 * @param permissions
	 * @param deep
	 */
	private void recursiveClearPermission(List<SysPermissionExtend> permissions, int deep) {
		if(permissions != null && permissions.size() > 0){
			if(deep <= 0){
				for (SysPermissionExtend p : permissions) {
					recursiveClearPermission(p.getChildren());
				}
				permissions.clear();
			}else{
				deep--;
				for (SysPermissionExtend p : permissions) {
					recursiveClearPermission(p.getChildren(), deep);
				}
			}
		}
	}
}