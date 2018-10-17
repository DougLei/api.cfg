package test.poi.imports.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.king.tooth.util.PoiExcelUtil;

public class ImportExcel1 {
	public static void main(String[] args) {
	      Workbook workbook = null;
          workbook = (Workbook) PoiExcelUtil.getReadWorkBookInstance("D:\\file\\import\\2018-10-16\\83beb3eee4bd413f95966d1b842dc572.xls", null);
          
          //工作表对象
          Sheet sheet = workbook.getSheetAt(0);
          
          Cell cell = sheet.getRow(0).getCell(0);
          
          System.out.println(cell.getCellType() == Cell.CELL_TYPE_NUMERIC);
          System.out.println(DateUtil.isCellDateFormatted(cell));
          System.out.println(cell.getDateCellValue());
	}
}
