package test.poi.export.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.king.tooth.util.PoiExcelUtil;

public class ExportExcel1 {
	public static void main(String[] args) throws IOException {
		
		Workbook wb = (Workbook) PoiExcelUtil.getWriteWorkBookInstance("xlsx");
		Sheet sheet = wb.createSheet();
		
		int i = sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
		sheet.createRow(0);
		
		
		
		
		
		
		File f = new File("C:\\Users\\StoneKing\\Desktop\\test.xlsx");
		if(f.exists()){
			f.delete();
		}
		FileOutputStream stream = new FileOutputStream(f);
		wb.write(stream);
		sheet.removeMergedRegion(i-1);
	}
}
