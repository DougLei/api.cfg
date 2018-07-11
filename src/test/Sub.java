package test;

import java.util.ArrayList;
import java.util.List;

import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.util.JsonUtil;

public class Sub extends Parent{
	private String name;
	public String getName(){
		return name;
	}
	
	public static void main(String[] args) {
		
		List<String> a = new ArrayList<String>();
		a.add("a_1111");
		a.add("a_22222");
//		System.out.println(a.toString().replace("[", "").replace("]", ""));
		
		
		String b = "{\"sqlScriptContent\":\"SELECT\n  id,\n  name,\n  parent_id,\n  code,\n  url,\n  icon,\n  module_body,\n  is_enabled,\n  CASE is_enabled WHEN '1' THEN '启用' WHEN '0' THEN '禁用'  END AS enabled_text,\n  CASE is_need_deploy WHEN '1' THEN '发布' WHEN '0' THEN '未发布'  END AS deploy_text,\n  CASE belong_platform_type WHEN '0' THEN '配置平台' WHEN '1' THEN '解析平台' WHEN '2' THEN '通用' END AS platform_text\nFROM COM_PROJECT_MODULE\",\"sqlScriptCaption\":\"Case1\",\"sqlScriptResourceName\":\"Case1\",\"isEnabled\":\"1\",\"isNeedDeploy\":\"1\",\"belongPlatformType\":\"1\"}";
		ComSqlScript sql = JsonUtil.parseObject(b, ComSqlScript.class);
		System.out.println(sql.getSqlScriptContent());
	}
}
