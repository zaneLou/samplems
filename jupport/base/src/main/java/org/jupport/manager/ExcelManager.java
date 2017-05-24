package org.jupport.manager;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;

public interface ExcelManager {

	public String encodeFilename(String filename, HttpServletRequest request);
	public void exportListMap(OutputStream os, List<String> head, List<String> keys, List<Map> datas ) throws Exception;
	public void createHSSFWorkbookHead(HSSFSheet sheet, List<String> head, HSSFCellStyle style, int width) throws Exception ;
}
