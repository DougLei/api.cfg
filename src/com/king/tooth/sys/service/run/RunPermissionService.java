package com.king.tooth.sys.service.run;

import java.util.List;

import com.king.tooth.sys.entity.common.ComRole;
import com.king.tooth.sys.service.AbstractResourceService;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * [运行系统]权限处理器
 * @author DougLei
 */
@SuppressWarnings("unchecked")
public class RunPermissionService extends AbstractResourceService{
	
	/**
	 * 获取指定账户的所有权限
	 * @param accountId
	 * @return
	 */
	public List<ComRole> findPermissionsByAccountId(String accountId){
		String hql = "select new ComRole(id,name,code) from ComRole where isEnabled = 1 and id in (select rightId from CfgCustomerCfgDatabaseLinks where leftId = '"+accountId+"') order by orderCode asc";
		List<ComRole> roles = HibernateUtil.executeListQueryByHql(hql, null);
		if(roles!=null && roles.size()>0){
			hql = "from ComPermission where id in (select rightId from ComRoleComPermissionLinks where leftId = ?)";
			for (ComRole role : roles) {
				role.setPermissions(HibernateUtil.executeListQueryByHqlArr(hql, role.getId()));
			}
		}
		return roles;
	}
	
	
	public void addPermissionByRoleId(String roleId){
		
	}
}
