package vn.vif.report.models;

import java.util.HashMap;
import java.util.Map;

import vn.vif.report.services.ReportsService;

public class ReportFilter {
	private String exportType = ReportsService.EXTENSION_TYPE_EXCEL;
	private String template;
	private String fileName;
	private Map<String, Object> paramsMap = new HashMap<String, Object>();
	
	public Map<String, Object> getParamsMap() {
		return paramsMap;
	}
	public void putParamsMap(String field, Object value) {
		this.paramsMap.put(field, value);
	}
	public String getExportType() {
		return exportType;
	}
	public void setExportType(String exportType) {
		this.exportType = exportType;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
