package com.fsb.xls.xlsimport;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import org.apache.poi.hssf.OldExcelFormatException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author Fede
 */
public final class XLSImport {

    private final Workbook workbook;
    private Sheet selectedSheet;
    private Row selectedRow;

    public XLSImport(String filepath) throws IOException, InvalidFormatException, OldExcelFormatException {
        FileInputStream fis = null;
        try {
            File file = new File(filepath);
            fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            workbook = WorkbookFactory.create(bis);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ex) {
            }
        }
    }

    public Sheet selectSheet(int index) {
        this.selectedSheet = workbook.getSheetAt(index);
        return this.selectedSheet;
    }

    public Row selectRow(int index) {
        if (selectedSheet == null) {
            selectedSheet = workbook.getSheetAt(0);
        }
        this.selectedRow = selectedSheet.getRow(index);
        return this.selectedRow;
    }

    public Cell getCell(int index) {
        if (selectedRow == null) {
            selectedRow = selectedSheet.getRow(0);
        }
        return selectedRow.getCell(index);
    }

    public Object getValue(int cellIndex) {
        Cell cell = getCell(cellIndex);
        if (cell == null) {
            return "";
        }
        Object o = null;
        int type = cell.getCellType();
        if (type == Cell.CELL_TYPE_FORMULA) {
            type = cell.getCachedFormulaResultType();
        }
        switch (type) {
            case Cell.CELL_TYPE_BLANK:
                o = "";
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                o = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_ERROR:
                o = cell.getErrorCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    o = cell.getDateCellValue();
                } else {
                    o = cell.getNumericCellValue();
                }
                break;
            case Cell.CELL_TYPE_STRING:
                o = cell.getRichStringCellValue().getString();
                break;
        }
        return o;
    }

    public String getString(int cellIndex) {
        return getValue(cellIndex).toString().trim();
    }

    public double getDouble(int cellIndex) {
        return (Double) getValue(cellIndex);
    }

    public int getInt(int cellIndex) {
        double doubleValue = getDouble(cellIndex);
        return (int) doubleValue;
    }

    public boolean getBoolean(int cellIndex) {
        return (Boolean) getValue(cellIndex);
    }

    public Date getDate(int cellIndex) {
        return (Date) getValue(cellIndex);
    }
}
