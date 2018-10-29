package test.poi.export.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.king.tooth.util.PoiExcelUtil;

public class ExportExcel2 {
	public static void main(String[] args) throws IOException {
		String suffix = "xlsx";
		
		Workbook wb = (Workbook) PoiExcelUtil.getWriteWorkBookInstance(suffix);
		Sheet sheet = wb.createSheet();
		
		Row row = sheet.createRow(0);
		Cell cell = row.createCell(10);
		cell.setCellValue("哈哈");
		
		row = sheet.createRow(1);
		cell = row.createCell(10);
		cell.setCellValue("呵呵");
		
		row = sheet.createRow(2);
		cell = row.createCell(10);
		cell.setCellValue("嘿嘿");
		
		// 设置数据有效性，默认10列就够了，剩下的用户去修改excel即可
		PoiExcelUtil.setDataValidation(suffix, sheet, -1, 10, 0, 0, "K", 1, "K", 3);
		
		File f = new File("C:\\Users\\StoneKing\\Desktop\\test."+suffix);
		if(f.exists()){
			f.delete();
		}
		FileOutputStream stream = new FileOutputStream(f);
		wb.write(stream);
		System.out.println("ok");
	}
}
