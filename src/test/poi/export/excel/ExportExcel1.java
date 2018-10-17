package test.poi.export.excel;

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
		Sheet sheet = wb.createSheet("test");
		Row row = sheet.createRow(0);
		
		Cell cell = row.createCell(1);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue("1111解决xlsx");
		
		FileOutputStream stream = new FileOutputStream("C:\\Users\\StoneKing\\Desktop\\test.xlsx");
		wb.write(stream);
	}
}
