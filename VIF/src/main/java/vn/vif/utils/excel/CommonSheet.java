package vn.vif.utils.excel;

import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class CommonSheet {
	HSSFSheet sheet;
	XSSFSheet sheetX;
	public Row getRow(int i) {
		if (sheet != null) {
			return sheet.getRow(i);
		} else if (sheetX != null) {
			return sheetX.getRow(i);
		}
		return null;
	}
	public Iterator<Row> iterator() {
		if (sheet != null) {
			return sheet.iterator();
		} else if (sheetX != null) {
			return sheetX.iterator();
		}
		return null;
	}
}
