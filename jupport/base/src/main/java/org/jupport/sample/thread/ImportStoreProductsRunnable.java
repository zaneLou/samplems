package org.jupport.sample.thread;

import java.io.IOException;
import java.util.Iterator;
import org.apache.poi.hssf.OldExcelFormatException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.jupport.sample.model.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.fsb.xls.xlsimport.XLSImport;

public class ImportStoreProductsRunnable extends Thread {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected String filePath;
	//protected StoreManager storeManager;
	//protected ProductManager productManager;
	protected int storeUid;
	protected int format;
	protected Store store;
	/*
	public ImportStoreProductsRunnable(int storeUid, StoreManager storeManager, String filePath, ProductManager productManager, int format)
	{
		super();
		this.storeUid = storeUid;
		this.storeManager = storeManager;
		this.filePath = filePath;
		this.productManager = productManager;
		this.format = format;
		
		setName("ImportStoreProductsRunnable");
	}
	*/
	@Override
	public void run() 
	{
		//store = storeManager.getStoreRepository().getStoreByUid(storeUid);
		String wrongIndex = "";
		try {
			XLSImport importer = new XLSImport(filePath);
			Sheet selectedSheet = importer.selectSheet(0);
			Iterator<Row> it = selectedSheet.iterator();
			int errorCount = 0;
			int index = 0;
			int rightCount = 0;
			while(it.hasNext())
			{
				//if(index>10)
				//	break;
				
				//System.out.println("[index]" + index);
				int count = 0;
				if(this.format == 1)
				{
					count = parseXLSLine(it.next());
				}
				else if(this.format == 2)
				{
					count = parseXLSLine2(it.next());
				}
				else if(this.format == 3)
				{
					count = parseXLSLine3(it.next());
				}
				if(count==0)
				{
					wrongIndex += "," + (index+1);
					errorCount++;
				}
				else 
				{
					rightCount += count;
				}
				index ++;
			}
			logger.info("[++++++++++ImportStoreProducts++++++++++]");
			logger.info("[format]" + format);
			logger.info("[wrongIndex]" + wrongIndex + "[index]" + index + "[errorCount]" + errorCount + "[rightCount]" + rightCount);
		} catch (OldExcelFormatException | InvalidFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int parseXLSLine(Row row) 
	{
		int count = 0;
		Iterator<Cell> it = row.iterator();
		int i = 0;
		String barcode = null;
		String stringPrice = null;
		while(it.hasNext())
		{
			Cell cell = it.next();
			i = cell.getColumnIndex();
			
			//if(i==0)
			//logger.info("[cell]:" + cell);
			
			switch (i) {
			case 5:
				barcode = cell.toString().trim();
				break;
			case 6:
				stringPrice = cell.toString().trim();
				break;
			default:
				break;
			}
		}
		
		//logger.info("[row]:" + row);
		if(StringUtils.hasLength(barcode) && StringUtils.hasLength(stringPrice) && !barcode.equals("商品条码") && !barcode.equals("无") && !stringPrice.equals("零售价") )
		{
			float price = 0.f;
			try {
				price = Float.parseFloat(stringPrice);
			} catch (Exception e) {
				e.printStackTrace();
				return count;
			}
			// barcode
			/*
			List<Product> products = productManager.getProductRepository().getProductByBarcode(barcode);
			for (Product product : products) {
				StoreProduct storeProduct = productManager.getStoreProduct(store, product);
				if (storeProduct != null) {
					
					//log
					productManager.addStoreProductLog(storeProduct);
					
					storeProduct.setPrice(price);
					productManager.getStoreProductRepository().saveOrUpdateStoreProduct(storeProduct);
					
					logger.info("[update store ]product:" + product.getUid() + "|barcode:" + barcode + "|price:" + price);
					count++;
				}
			}
			*/
		}
		return count;
	}
	
	public int parseXLSLine2(Row row) 
	{
		return parseXLSLine(row, 6, 8, 7, -1);
	}
	
	public int parseXLSLine3(Row row) 
	{
		return parseXLSLine(row, 5, 6, -1, 7);
	}
	
	public int parseXLSLine(Row row, int barcodeIndex, int priceIndex, int referPriceIndex, int memeberPriceIndex) 
	{
		int count = 0;
		Iterator<Cell> it = row.iterator();
		int i = 0;
		String barcode = null;
		String stringPrice = null;
		String stringReferPrice = null;
		String stringMemberPrice = null;
		while(it.hasNext())
		{
			Cell cell = it.next();
			i = cell.getColumnIndex();
			
			if(barcodeIndex==i)
			{
				barcode = cell.toString().trim();
			}
			else if(priceIndex==i)
			{
				stringPrice =  cell.toString().trim();
			}
			else if(referPriceIndex==i)
			{
				stringReferPrice =  cell.toString().trim();
			}
			else if(memeberPriceIndex==i)
			{
				stringMemberPrice =  cell.toString().trim();
			}
			//if(i==0)
			//logger.info("[cell]:" + cell);
		}
		
		//logger.info("[row]:" + row);
		if(StringUtils.hasLength(barcode) && StringUtils.hasLength(stringPrice) && !barcode.equals("商品条码") && !barcode.equals("无"))
		{
			float price = 0.f;
			float referPrice = 0.f;
			float memberPrice = 0.f;
			try {
				if(stringPrice.contains("*"))
				{
					stringPrice = stringPrice.substring(stringPrice.indexOf("*")+1);
				}
				price = Float.parseFloat(stringPrice);
				if(StringUtils.hasLength(stringReferPrice))
					referPrice = Float.parseFloat(stringReferPrice);
				if(StringUtils.hasLength(stringMemberPrice))
					memberPrice = Float.parseFloat(stringMemberPrice);
			} catch (Exception e) {
				e.printStackTrace();
				return count;
			}
			// barcode
			/*
			List<Product> products = productManager.getProductRepository().getProductByBarcode(barcode);
			for (Product product : products) {
				StoreProduct storeProduct = productManager.getStoreProduct(store, product);
				if (storeProduct != null) {
					
					//log
					productManager.addStoreProductLog(storeProduct);
					
					storeProduct.setPrice(price);
					if(referPrice>0.f)
						storeProduct.setReferPrice(referPrice);
					if(memberPrice>0.f)
						storeProduct.setMemberPrice(memberPrice);
					
					productManager.getStoreProductRepository().saveOrUpdateStoreProduct(storeProduct);
					logger.info("[update store]product:" + product.getUid() + "|barcode:" + barcode + "|price:" + price);
					count++;
				}
			}
			*/
		}
		return count;
	}
	
}
