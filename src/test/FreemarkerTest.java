package test;

import java.io.File;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgTabledata;

import freemarker.template.Template;

@SuppressWarnings({"rawtypes", "unchecked"})
public class FreemarkerTest {
	public static void main(String[] args) throws Exception {
//		Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
//		// 设置模版文件夹的位置
//		cfg.setDirectoryForTemplateLoading(new File("C:\\devlopment\\GitRepository\\api_platform\\resources"));
		
		Template t = new Template(null, new FileReader(new File("C:\\devlopment\\GitRepository\\api_platform\\resources\\hibernateMapping\\template\\hibernate.hbm.xml.ftl")), null);
		
//		Template t = cfg.getTemplate("C:\\devlopment\\GitRepository\\api_platform\\resources\\hello.ftl");
//		Map dataModel = new HashMap();
//		dataModel.put("classPath", "com.king.tooth");
//		dataModel.put("className", "CfgTabledataController");
//		t.process(dataModel , new OutputStreamWriter(System.out));
		
		Map dataModel = new HashMap();
		CfgTabledata table = new CfgTabledata();
//		table.setTableName("App_User");
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>();
		CfgColumndata c1 = new CfgColumndata();
//		c1.setColumnName("User_Type");
		CfgColumndata c2 = new CfgColumndata();
//		c2.setColumnName("Login_Status");
		CfgColumndata c3 = new CfgColumndata();
//		c3.setColumnName("Name");
		columns.add(c1);
		columns.add(c2);
		columns.add(c3);
		
		CfgColumndata c4 = new CfgColumndata();
//		c4.setColumnName("CreateTime");
		columns.add(c4);
		
		
		dataModel.put("table", table);
		dataModel.put("columns", columns);
		t.process(dataModel, new OutputStreamWriter(System.out));
	}
}
