package org.jupport.sample.thread;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.apache.poi.hssf.OldExcelFormatException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.jupport.sample.model.FileInfo;
import org.jupport.sample.model.Product;
import org.springframework.util.StringUtils;

import com.fsb.xls.xlsimport.XLSImport;

public class ImportProductsRunnable extends Thread {

	protected String filePath;
	//protected ProductManager productManager;
	protected int prefixLength;
	
	public ImportProductsRunnable(String filePath)//, ProductManager productManager)
	{
		super();
		this.filePath = filePath;
		//this.productManager = productManager;
		
		setName("ImportProductsRunnable");
	}
	
	// iconv -f cp936 -t utf-8 p1.xls > p1_8.xls 
	// iconv -f cp936 -t utf-8 p2.xls > p2_8.xls 
	// iconv -f cp936 -t utf-8 p3.xls > p3_8.xls 
	
	@Override
	public void run() 
	{
		//importCSVFile(filePath);
		importXLSFile(filePath);
	}
	
	public void importXLSFile(String filePath) 
	{
		prefixLength =  10; //SYConstantsManager.Product_ImagePrefix.length();
		try {
			XLSImport importer = new XLSImport(filePath);
			Sheet selectedSheet = importer.selectSheet(0);
			Iterator<Row> it = selectedSheet.iterator();
			int errorCount = 0;
			//int rightCount = 0;
			while(it.hasNext())
			{
				if(!parseXLSLine(it.next()))
					errorCount++;
				//else 
				//	rightCount ++;
				
				//if(rightCount>100)
				//	break;
			}
			System.out.println("[errorCount]" + errorCount);
		} catch (OldExcelFormatException | InvalidFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public boolean parseXLSLine(Row row) 
	{
		/*
		// 编号,药品列表名称,ATY,图片1,图片2,图片3,图片4,图片5,图片6,列表说明,二维码,药品单位,TY,药品名称,药品成份,药品性状,功能主治,药品规格,用法用量,不良反应,药品禁忌,注意事项,相互作用,药品储藏,包装,有效期,批准文号,生产企业,SC
		protected String listName;
		protected Category category;
		protected String statement; //列表说明
		protected String barcode;//二维码
		protected String medicineUnit;//药品单位
		protected String ty;//TY
		protected String medicineName;//药品名称
		protected String composition;//药品成份
		protected String medicineTraits;//药品性状
		protected String function;//功能主治
		protected String specification;//药品规格
		protected String usage;//用法用量
		protected String reaction;//不良反应
		protected String taboo;//药品禁忌
		protected String attention;//注意事项
		protected String interaction;//相互作用
		protected String store;//药品储藏
		protected String packing;//包装
		protected String validityPeriod;//有效期
		protected String approvalNumber;//批准文号
		protected String enterprise;//生产企业
		protected String sc;//SC
		*/
		Iterator<Cell> it = row.iterator();
		int i = 0;
		Product product = null;
		while(it.hasNext())
		{
			Cell cell = it.next();
			i = cell.getColumnIndex();
			//System.out.println("[" + i +"]" + cell);
			switch (i) {
			case 0:
				int pid = 0;
				if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
				{
					pid = (int) cell.getNumericCellValue();
					
				}
				else
				{	
					if(cell.getStringCellValue().equals("编号"))
					{
						return true;
					}
						 
					pid = Integer.parseInt(cell.getStringCellValue());
				}
				try {
					System.out.println("[编号]" + pid);
					//product = productManager.getProductRepository().getProductByPid(pid);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(product == null)
				{
					product = new Product();
					product.setPid(pid);
				}
				else
					//productManager.cleanProductFileInfos(product);
				break;
			case 1:
				product.setListName(cell.getStringCellValue().trim());
				break;
			case 2:
				if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
				{
					//Category category = productManager.getCategoryRepository().getCategoryByUid((int) cell.getNumericCellValue());
					//product.setCategory(category);
				}
				else
				{
					//Category category = productManager.getCategoryRepository().getCategoryByUid(Integer.parseInt(cell.getStringCellValue().trim()));
					//product.setCategory(category);
				}
				break;
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
				String path = cell.toString();
				if(StringUtils.hasLength(path) && path.length()>=prefixLength)
				{
					path = path.substring(prefixLength);
					path = path.substring(path.lastIndexOf("/")+1);
					FileInfo fileInfo = new FileInfo(i==3?FileInfo.Image_ProductMain:FileInfo.Image_Product, product.getPid(), path.trim(), i-3);
					//productManager.getFileInfoRepository().saveOrUpdateFileInfo(fileInfo);
					if(i==3)
						product.setMainImage(fileInfo);
				}
				break;
			case 9:
				product.setStatement(cell.getStringCellValue().trim());
				break;
			case 10:
				/**/
				product.setBarcode(cell.toString().trim());
				if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
				{
					product.setBarcode(""+cell.getNumericCellValue());
				}
				else
				{
					product.setBarcode(cell.getStringCellValue().trim());
				}
				
				break;
			case 11:
				product.setMedicineUnit(cell.toString().trim());
				break;
			case 12:
				if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
				{
					product.setTy(""+cell.getNumericCellValue());
				}
				else
				{
					product.setTy(cell.getStringCellValue().trim());
				}
				break;
			case 13:
				product.setMedicineName(cell.toString().trim());
				break;
			case 14:
				product.setComposition(cell.toString().trim());
				break;
			case 15:
				product.setMedicineTraits(cell.toString().trim());
				break;
			case 16:
				product.setFunction(cell.toString().trim());
				break;
			case 17:
				product.setSpecification(cell.toString().trim());
				break;
			case 18:
				product.setUsage(cell.toString().trim());
				break;
			case 19:
				product.setReaction(cell.getStringCellValue().trim());
				break;
			case 20:
				product.setTaboo(cell.getStringCellValue().trim());
				break;
			case 21:
				product.setAttention(cell.getStringCellValue().trim());
				break;
			case 22:
				product.setInteraction(cell.getStringCellValue().trim());
				break;
			case 23:
				product.setStore(cell.getStringCellValue().trim());
				break;
			case 24:
				product.setPacking(cell.toString().trim());
				break;
			case 25:
				product.setValidityPeriod(cell.toString().trim());
				break;
			case 26:
				product.setApprovalNumber(cell.toString().trim());
				break;
			case 27:
				product.setEnterprise(cell.getStringCellValue().trim());
				break;
			case 28:
				if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
				{
					product.setSc(""+cell.getNumericCellValue());
				}
				else
				{
					product.setSc(cell.getStringCellValue().trim());
				}
				break;
			case 29:
				int test = (int) cell.getNumericCellValue();
				if(test!=24)
				{
					System.out.println("**********************************[pid]" + product.getPid() +  "[test]" + cell.getNumericCellValue());
					return false;
				}
				break;
			default:
				break;
			}
		}
			
		//save
		//productManager.getProductRepository().saveOrUpdateProduct(product);
		return true;
	}
	
	/*
	 * CSV
	 */

	public void importCSVFile(String filePath) 
	{
		BufferedReader br = null;
		String line = null;
	    try {
	    	int i = 0;
	        // = "/Users/zhengliu/data2/Java/workspace3/DrugStore-doc/yao1_8.csv";
	        InputStreamReader isr = new InputStreamReader(new FileInputStream(filePath), "UTF-8"); 
			br = new BufferedReader(isr);
	        while ((line = br.readLine()) != null) 
	        {
	        	line = StringUtils.trimWhitespace(line);
	        	if(!StringUtils.hasLength(line))
	        		continue;
	        	
	        	i++;
	        	if(i%10==0)
	        		System.out.println("[line]" + i);
	        	else
	        		System.out.print("[line]" + i);
	        	parseCSVLine(line);
	        	
	        }
	    }
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (br != null) {
	            try {
	                br.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}
	
	public void parseCSVLine(String line) 
	{
		/*
		// 编号,药品列表名称,ATY,图片1,图片2,图片3,图片4,图片5,图片6,列表说明,二维码,药品单位,TY,药品名称,药品成份,药品性状,功能主治,药品规格,用法用量,不良反应,药品禁忌,注意事项,相互作用,药品储藏,包装,有效期,批准文号,生产企业,SC
		protected String listName;
		protected Category category;
		protected String statement; //列表说明
		protected String barcode;//二维码
		protected String medicineUnit;//药品单位
		protected String ty;//TY
		protected String medicineName;//药品名称
		protected String composition;//药品成份
		protected String medicineTraits;//药品性状
		protected String function;//功能主治
		protected String specification;//药品规格
		protected String usage;//用法用量
		protected String reaction;//不良反应
		protected String taboo;//药品禁忌
		protected String attention;//注意事项
		protected String interaction;//相互作用
		protected String store;//药品储藏
		protected String packing;//包装
		protected String validityPeriod;//有效期
		protected String approvalNumber;//批准文号
		protected String enterprise;//生产企业
		protected String sc;//SC
		*/
		String values[] = line.split(",");
		if(values[0].equals("编号"))
			return;
		
		int pid = Integer.parseInt(values[0]);
		Product product = null;
		try {
			//product = productManager.getProductRepository().getProductByPid(pid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(product == null)
		{
			product = new Product();
		}
		else
			//productManager.cleanProductFileInfos(product);
		
		product.setPid(pid);
		product.setListName(values[1]);
		//Category category = productManager.getCategoryRepository().getCategoryByUid(Integer.parseInt(values[2]));
		//product.setCategory(category);
		
		//Image
		for (int i = 0; i < 6; i++) 
		{
			if(StringUtils.hasLength(values[3+i]))
			{
				//String path = values[3+i].substring(SYConstantsManager.Product_ImagePrefix.length());
				//FileInfo fileInfo = new FileInfo(i==0?FileInfo.Image_ProductMain:FileInfo.Image_Product, pid, path, i);
				//productManager.getFileInfoRepository().saveOrUpdateFileInfo(fileInfo);
			}
		}
		
		int i = 9;
		//protperty
		product.setStatement(values[i++]);
		product.setBarcode(values[i++]);
		product.setMedicineUnit(values[i++]);
		product.setTy(values[i++]);
		product.setMedicineName(values[i++]);
		product.setComposition(values[i++]);
		product.setMedicineTraits(values[i++]);
		product.setFunction(values[i++]);
		product.setSpecification(values[i++]);
		product.setUsage(values[i++]);
		product.setReaction(values[i++]);
		product.setTaboo(values[i++]);
		product.setAttention(values[i++]);
		product.setInteraction(values[i++]);
		product.setStore(values[i++]);
		product.setPacking(values[i++]);
		product.setValidityPeriod(values[i++]);
		product.setApprovalNumber(values[i++]);
		product.setEnterprise(values[i++]);
		product.setSc(values[i++]);
		
		System.out.println("[pid]" + product.getPid() + "[ty]" + product.getTy() + "[sc]" + product.getSc());
		//save
		//productManager.getProductRepository().saveOrUpdateProduct(product);
		
	}
}
