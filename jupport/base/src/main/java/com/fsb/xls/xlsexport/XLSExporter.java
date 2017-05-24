/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fsb.xls.xlsexport;

import java.util.List;

/**
 *
 * @author Fede
 */
public interface XLSExporter {

    public String getSheetName();

    public List<String> getTitles();

    public List<Object[]> getCellData() throws Exception;
}
