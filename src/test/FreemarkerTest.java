package test;

import java.io.File;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;

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
//		dataModel.put("className", "ComTabledataController");
//		t.process(dataModel , new OutputStreamWriter(System.out));
		
		Map dataModel = new HashMap();
		ComTabledata table = new ComTabledata();
//		table.setTableName("App_User");
		List<ComColumndata> columns = new ArrayList<ComColumndata>();
		ComColumndata c1 = new ComColumndata();
//		c1.setColumnName("User_Type");
		ComColumndata c2 = new ComColumndata();
//		c2.setColumnName("Login_Status");
		ComColumndata c3 = new ComColumndata();
//		c3.setColumnName("Name");
		columns.add(c1);
		columns.add(c2);
		columns.add(c3);
		
		ComColumndata c4 = new ComColumndata();
//		c4.setColumnName("CreateTime");
		columns.add(c4);
		
		
		dataModel.put("table", table);
		dataModel.put("columns", columns);
		t.process(dataModel, new OutputStreamWriter(System.out));
	}
}
