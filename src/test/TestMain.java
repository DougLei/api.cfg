package test;

import java.io.IOException;

import com.king.tooth.sys.entity.sys.SysUser;


public class TestMain {
	public static void main(String[] args) throws CloneNotSupportedException, IOException, ClassNotFoundException {
		SysUser u = new SysUser();
		u.setIsSyncLoginName(1);
		operAccount(u, true);
		
	}
	
	/**
	 * 操作账户
	 * @param originUser
	 * @param isLoadUser
	 * @return
	 */
	private static void operAccount(SysUser originUser, boolean isLoadUser) {
		SysUser user = originUser;
		
		if(isLoadUser){
			System.out.println(user);
			System.out.println(originUser);
			
			user = new SysUser();
			user.setIsSyncLoginName(originUser.getIsSyncLoginName());
			
			System.out.println(user);
			System.out.println(originUser);
		}
		
	}
}
