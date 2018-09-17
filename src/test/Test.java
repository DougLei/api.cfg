package test;

import com.king.tooth.sys.entity.cfg.projectmodule.ProjectModuleExtend;
import com.king.tooth.util.JsonUtil;


public class Test {
	
	public static void main(String[] args) throws CloneNotSupportedException {
		
		
		String a = "{\"3\", \"0a0e8872555b45229881262f7a5b0c87\", \"3\", \"3\", 1}";
		System.out.println(JsonUtil.parseObject(a, ProjectModuleExtend.class));
	}
}
 