package test.poi.imports.excel;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.king.tooth.util.PoiExcelUtil;

public class ImportExcel1 {
	public static void main(String[] args) {
	      Workbook workbook = null;
          workbook = (Workbook) PoiExcelUtil.getWorkBookInstance("C:\\Users\\StoneKing\\Desktop\\import.xlsx", null);
          
          //工作表对象
          Sheet sheet = workbook.getSheetAt(0);
          
          System.out.println(sheet.getLastRowNum());
          System.out.println(sheet.getRow(0).getLastCellNum());
          System.out.println(sheet.getRow(0).getCell(1));
	}
}
