package org.jupport.manager;

import java.io.FileOutputStream;  
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;  
  

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;  
import org.apache.poi.hssf.usermodel.HSSFCellStyle;  
import org.apache.poi.hssf.usermodel.HSSFDataFormat;  
import org.apache.poi.hssf.usermodel.HSSFFont;  
import org.apache.poi.hssf.usermodel.HSSFHyperlink;  
import org.apache.poi.hssf.usermodel.HSSFRow;  
import org.apache.poi.hssf.usermodel.HSSFSheet;  
import org.apache.poi.hssf.usermodel.HSSFWorkbook;  
import org.apache.poi.hssf.util.HSSFColor;  
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ExcelManagerImpl implements ExcelManager{

	private Logger logger = Logger.getLogger(getClass());
	
	public Logger getLogger() {
		return logger;
	}

	@Override
    public String encodeFilename(String filename, HttpServletRequest request) {    
	      /**  
	       * 获取客户端浏览器和操作系统信息  
	       * 在IE浏览器中得到的是：User-Agent=Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; Maxthon; Alexa Toolbar)  
	       * 在Firefox中得到的是：User-Agent=Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.7.10) Gecko/20050717 Firefox/1.0.6  
	       */    
		String agent = request.getHeader("USER-AGENT");
		try {
			if ((agent != null) && (-1 != agent.indexOf("MSIE"))) {
				String newFileName = URLEncoder.encode(filename, "UTF-8");
				newFileName = StringUtils.replace(newFileName, "+", "%20");
				if (newFileName.length() > 150) {
					newFileName = new String(filename.getBytes("GB2312"), "ISO8859-1");
					newFileName = StringUtils.replace(newFileName, " ", "%20");
				}
				return newFileName;
			}
			if ((agent != null) && (-1 != agent.indexOf("Mozilla")))
				return MimeUtility.encodeText(filename, "UTF-8", "B");

			return filename;
		} catch (Exception ex) {
			return filename;
		}
	} 
	
	public void exportSample(String path) throws Exception 
	{  
	    // 创建Excel的工作书册 Workbook,对应到一个excel文档  
	    HSSFWorkbook wb = new HSSFWorkbook();  
	  
	    // 创建Excel的工作sheet,对应到一个excel文档的tab  
	    HSSFSheet sheet = wb.createSheet("sheet1");  
	  
	    // 设置excel每列宽度  
	    sheet.setColumnWidth(0, 4000);  
	    sheet.setColumnWidth(1, 3500);  
	  
	    // 创建字体样式  
	    HSSFFont font = wb.createFont();  
	    font.setFontName("Verdana");  
	    font.setBoldweight((short) 100);  
	    font.setFontHeight((short) 300);  
	    font.setColor(HSSFColor.BLUE.index);  
	  
	    // 创建单元格样式  
	    HSSFCellStyle style = wb.createCellStyle();  
	    style.setAlignment(CellStyle.ALIGN_CENTER);  
	    style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
	    style.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);  
	    style.setFillPattern(CellStyle.SOLID_FOREGROUND);  
	  
	    // 设置边框  
	    style.setBottomBorderColor(HSSFColor.RED.index);  
	    style.setBorderBottom(CellStyle.BORDER_THIN);  
	    style.setBorderLeft(CellStyle.BORDER_THIN);  
	    style.setBorderRight(CellStyle.BORDER_THIN);  
	    style.setBorderTop(CellStyle.BORDER_THIN);  
	  
	    style.setFont(font);// 设置字体  
	  
	    // 创建Excel的sheet的一行  
	    HSSFRow row = sheet.createRow(0);  
	    row.setHeight((short) 500);// 设定行的高度  
	    // 创建一个Excel的单元格  
	    HSSFCell cell = row.createCell(0);  
	  
	    // 合并单元格(startRow，endRow，startColumn，endColumn)  
	    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));  
	  
	    // 给Excel的单元格设置样式和赋值  
	    cell.setCellStyle(style);  
	    cell.setCellValue("hello world");  
	  
	    // 设置单元格内容格式  
	    HSSFCellStyle style1 = wb.createCellStyle();  
	    style1.setDataFormat(HSSFDataFormat.getBuiltinFormat("h:mm:ss"));  
	  
	    style1.setWrapText(true);// 自动换行  
	  
	    row = sheet.createRow(1);  
	  
	    // 设置单元格的样式格式  
	  
	    cell = row.createCell(0);  
	    cell.setCellStyle(style1);  
	    cell.setCellValue(new Date());  
	  
	    // 创建超链接  
	    HSSFHyperlink link = new HSSFHyperlink(HSSFHyperlink.LINK_URL);  
	    link.setAddress("http://www.baidu.com");  
	    cell = row.createCell(1);  
	    cell.setCellValue("百度");  
	    cell.setHyperlink(link);// 设定单元格的链接  
	  
	    FileOutputStream os = new FileOutputStream(path);  
	    wb.write(os);  
	    os.close();  
	} 
	
	public void initSheet(HSSFWorkbook wb, HSSFSheet sheet)
	{
	    // 设置excel每列宽度  
	    sheet.setColumnWidth(0, 4000);  
	    sheet.setColumnWidth(1, 3500);  
	  
	    // 创建字体样式  
	    HSSFFont font = wb.createFont();  
	    font.setFontName("Verdana");  
	    font.setBoldweight((short) 100);  
	    font.setFontHeight((short) 300);  
	    font.setColor(HSSFColor.BLUE.index);  
	  
	    // 创建单元格样式  
	    HSSFCellStyle style = wb.createCellStyle();  
	    style.setAlignment(CellStyle.ALIGN_CENTER);  
	    style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  
	    style.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);  
	    style.setFillPattern(CellStyle.SOLID_FOREGROUND);  
	  
	    // 设置边框  
	    style.setBottomBorderColor(HSSFColor.RED.index);  
	    style.setBorderBottom(CellStyle.BORDER_THIN);  
	    style.setBorderLeft(CellStyle.BORDER_THIN);  
	    style.setBorderRight(CellStyle.BORDER_THIN);  
	    style.setBorderTop(CellStyle.BORDER_THIN);  
	  
	    style.setFont(font);// 设置字体  
	}
	
	@Override
	public void exportListMap(OutputStream os, List<String> head, List<String> keys, List<Map> datas ) throws Exception 
	{
	    // 创建Excel的工作书册 Workbook,对应到一个excel文档  
	    HSSFWorkbook wb = new HSSFWorkbook();  
	    // 创建Excel的工作sheet,对应到一个excel文档的tab  
	    HSSFSheet sheet = wb.createSheet("sheet1");   
	    HSSFCellStyle style = wb.createCellStyle(); 

	    createHSSFWorkbookHead(sheet, head, style, 4000);
	    
	    HSSFRow row = null;
	    //column width
	    for (int i = 0; i < datas.size(); i++) 
	    {
	    	row = sheet.createRow(i+1);
	    	row.setHeight((short) 400);
	    	Map data = datas.get(i);
	    	for (int j = 0; j < keys.size(); j++)
	    	{
	    		HSSFCell cell = row.createCell(j);
		    	cell.setCellStyle(style);  
		    	Object value = data.get(keys.get(j));
		  	    cell.setCellValue(value==null?"":value.toString());  
			}
		}
	    wb.write(os); 
	}
	
	@Override
	public void createHSSFWorkbookHead(HSSFSheet sheet, List<String> head, HSSFCellStyle style, int width) throws Exception 
	{
	    //column width
	    for (int i = 0; i < head.size(); i++) 
	    {
	    	sheet.setColumnWidth(i, width);
		}
	    
	    //Head
	    HSSFRow row = sheet.createRow(0);  
	    row.setHeight((short) 500);// 设定行的高度
	    for (int i = 0; i < head.size(); i++) 
	    {
	    	HSSFCell cell = row.createCell(i);
	    	cell.setCellStyle(style);  
	  	    cell.setCellValue(head.get(i));  
		}
	}
	
	public static void main(String[] args) throws Exception 
	{
		ExcelManagerImpl excelManager = new ExcelManagerImpl();
		//excelManager.exportSample("/Users/sy1/zane/upload/workbook.xls");
		FileOutputStream os = new FileOutputStream("/Users/zhengliu/data2/upload/workbook.xls");
		List<String> head = new ArrayList<String>();
		head.add("头1");
		head.add("头2");
		List<String> keys = new ArrayList<String>();
		keys.add("head1");
		keys.add("head2");
		List<Map> datas = new ArrayList<Map>();
		Map map = new HashMap<String, Object>();
		map.put("head1", "体A1");
		map.put("head2", "体A2");
		datas.add(map);
		map = new HashMap<String, Object>();
		map.put("head1", "体B2");
		map.put("head2", "体B2");
		datas.add(map);
		excelManager.exportListMap(os, head, keys, datas); 
		os.close();  
		excelManager.getLogger().info("done.");
	}
	
	
	
	
}
