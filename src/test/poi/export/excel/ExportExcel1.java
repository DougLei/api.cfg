package test.poi.export.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.king.tooth.util.PoiExcelUtil;

public class ExportExcel1 {
	public static void main(String[] args) throws IOException {
		
		Workbook wb = (Workbook) PoiExcelUtil.getWriteWorkBookInstance("xlsx");
		Sheet sheet = wb.createSheet();
		
		sheet.setColumnHidden(2, true);
//		CellStyle cs = wb.createCellStyle();
//		cs.setHidden(true);
		
		Row row = sheet.createRow(0);
		Cell cell1 = row.createCell(0);
		cell1.setCellValue("第一个单元格");
		
		
		Cell cell2 = row.createCell(1);
		cell2.setCellValue("第二个单元格");
		
		
		Cell cell3 = row.createCell(2);
		cell3.setCellValue("第三个单元格");
		
		
		
		
		File f = new File("C:\\Users\\StoneKing\\Desktop\\test.xlsx");
		if(f.exists()){
			f.delete();
		}
		FileOutputStream stream = new FileOutputStream(f);
		wb.write(stream);
	}
}
