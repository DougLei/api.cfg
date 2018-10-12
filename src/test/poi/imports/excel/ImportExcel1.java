package test.poi.imports.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ImportExcel1 {
	public static void main(String[] args) {
		File file = new File("C:\\Users\\StoneKing\\Desktop\\import.xlsx");
		if(file.exists()){
			try {
				XSSFWorkbook xssfWorkBook = new XSSFWorkbook(new FileInputStream(file));
				
				
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("在路径["+"C:\\Users\\StoneKing\\Desktop\\import.xlsx"+"]下，不存在要导入的excel文件，请联系后端系统开发人员");
		}
	}
}
