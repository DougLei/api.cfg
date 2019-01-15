package test;

import java.util.ArrayList;
import java.util.List;

import com.api.sys.entity.sys.SysUser;



public class TestMain {
	public static void main(String[] args) throws Exception {

		List<Object> s = new ArrayList<Object>();
		s.add(new SysUser());
		s.add(new SysUser());
		s.add(new SysUser());
		s.add(new SysUser());
		s.add(new SysUser());
		
		for (int i=0;i<s.size();i++) {
			System.out.println(i);
			s.remove(i);
			s.add(i, (i+1));
		}
		System.out.println(s);
		
	}
}
