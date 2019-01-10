package test.poi.export.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.api.util.PoiExcelUtil;

public class ExportExcel3 {
	public static void main(String[] args) throws IOException {
		String suffix = "xls";
		
		Workbook wb = (Workbook) PoiExcelUtil.getWriteWorkBookInstance(suffix);
		Sheet sheet = wb.createSheet();

		Row titleRow = sheet.createRow(0);
		Cell titleCell = titleRow.createCell(0);
		PoiExcelUtil.setTitleCellStyle(wb, sheet, titleCell, (short)0, (short)0, (short)0, (short)13);
		titleCell.setCellValue("辅料明细信息");
		
		Row headRow = sheet.createRow(1);
		Cell headCell = headRow.createCell(0);
		PoiExcelUtil.setHeadCellStyle(wb, headCell);
		headCell.setCellValue("辅料种类");
		
		
		Row valueRow = sheet.createRow(2);
		Cell valueCell = valueRow.createCell(0);
		PoiExcelUtil.setValueCellStyle(wb, valueCell);
		valueCell.setCellValue("油料");
		
		
		
		headCell = headRow.createCell(1);
		PoiExcelUtil.setHeadCellStyle(wb, headCell);
		headCell.setCellValue("辅料名称");
		
		
		
		valueCell = valueRow.createCell(1);
		PoiExcelUtil.setValueCellStyle(wb, valueCell);
		valueCell.setCellValue("昆仑EC435低烟雾极压切削油(0.17吨/桶)昆仑EC435低烟雾极压切削油(0.17吨/桶)昆仑EC435低烟雾极压切削油(0.17吨/桶)");
		
		sheet.autoSizeColumn(1, true);
		valueRow = sheet.createRow(3);
		valueCell = valueRow.createCell(1);
		PoiExcelUtil.setValueCellStyle(wb, valueCell);
		valueCell.setCellValue("昆仑EC435低烟雾极压切削油(0.17吨/桶)昆仑EC435低烟雾极压切削油(0.17吨/桶)");
		
		
		
		
		File f = new File("C:\\Users\\StoneKing\\Desktop\\test."+suffix);
		if(f.exists()){
			f.delete();
		}
		FileOutputStream stream = new FileOutputStream(f);
		wb.write(stream);
		System.out.println("ok");
	}
}
