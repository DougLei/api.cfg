package test.poi.export.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.king.tooth.util.PoiExcelUtil;

public class ExportExcel1 {
	public static void main(String[] args) throws IOException {
		
		Workbook wb = (Workbook) PoiExcelUtil.getWriteWorkBookInstance("xlsx");
		Sheet sheet = wb.createSheet();
		Row row = sheet.createRow(0);
		
		CellStyle cs = wb.createCellStyle();
		cs.setAlignment(CellStyle.ALIGN_CENTER);// 水平对齐
		cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直对齐
		// 修改背景颜色
		cs.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.index);
		cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// 设置字体
		Font font = wb.createFont();
		font.setFontName("Arial");//设置字体名称
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);// 加粗
		cs.setFont(font);

		
		
		Cell cell = row.createCell(1);
		cell.setCellStyle(cs);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue("1111解决xlsx");
		
		
		
		File f = new File("C:\\Users\\StoneKing\\Desktop\\test.xlsx");
		if(f.exists()){
			f.delete();
		}
		FileOutputStream stream = new FileOutputStream(f);
		wb.write(stream);
	}
}
