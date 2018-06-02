package test.sqlparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * 读取sql文件，获取sql语句
 * @author DougLei
 */
public class SQLFileReader {
	public static void main(String[] args) throws Exception {
		File file = new File("C:\\devlopment\\GitRepository\\api_platform\\src\\test\\sqlparser\\sql.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		StringBuilder sql = new StringBuilder();
		sql.append("String sqls = \"\";");
		while(br.ready()){
			sql.append("sqls += \"  ").append(br.readLine()).append("\\n\"; \n");
		}
		System.out.println(sql);
		
	}
}
