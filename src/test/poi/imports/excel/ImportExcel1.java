package test.poi.imports.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.king.tooth.util.PoiExcelUtil;

public class ImportExcel1 {
	public static void main(String[] args) {
	      Workbook workbook = null;
          workbook = (Workbook) PoiExcelUtil.getWorkBookInstance("C:\\Users\\Administrator\\Desktop\\import.xlsx", null);
          
          //工作表对象
          Sheet sheet = workbook.getSheetAt(0);
          
          Cell cell = sheet.getRow(0).getCell(0);
          
          System.out.println(cell.getCellType() == Cell.CELL_TYPE_NUMERIC);
          System.out.println(DateUtil.isCellDateFormatted(cell));
          System.out.println(cell.getDateCellValue());
	}
}
