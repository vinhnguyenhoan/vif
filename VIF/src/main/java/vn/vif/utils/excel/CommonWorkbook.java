package vn.vif.utils.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CommonWorkbook {
	private HSSFWorkbook workbook;
	private XSSFWorkbook workbookX;
	private FileInputStream in;
	public CommonWorkbook(File file) throws IOException {
		try {
			workbookX = new XSSFWorkbook(in = new FileInputStream(file));
		} catch (Exception e) {
			workbook = new HSSFWorkbook(in = new FileInputStream(file));
		}
	}
	public int getNumberOfSheet() {
		if (workbookX != null) {
			return workbookX.getNumberOfSheets();
		} else if (workbook != null) {
			return workbook.getNumberOfSheets();
		}
		return 0;
	}
	public CommonSheet getSheetAt(int i) {
		CommonSheet s = new CommonSheet();
		if (workbookX != null) {
			s.sheetX = workbookX.getSheetAt(i);
		} else if (workbook != null) {
			s.sheet = workbook.getSheetAt(i);
		}
		return s;
	}
	public void close() throws IOException {
		if (in != null) {
			in.close();
		}
	}
}
