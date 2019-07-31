package xls;

import base.PoiWriter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;

public class XlsWriter extends PoiWriter {

    private Workbook xls;

    public void createXls(){
        xls = new HSSFWorkbook();
    }

    public Sheet createSheet(String sheetName){
        return xls.createSheet(sheetName);
    }

    public void saveXls(String path){
        try(FileOutputStream outputStream = new FileOutputStream(path)){
            xls.write(outputStream);
            outputStream.flush();
        }catch(Exception e){
            logger.error("保存xls文件异常！", e);
        }
    }

    public void writeSheetData(String sheetName, String[][] data){
        Sheet sheet = xls.createSheet(sheetName);
        int rowIndex = 0;
        for(String[] rowData : data){
            Row row = sheet.createRow(rowIndex);
            int cellIndex = 0;
            if(rowData!=null){
                for(String cellData:rowData){
                    Cell cell = row.createCell(cellIndex);
                    cell.setCellValue(cellData);
                    cellIndex++;
                }
            }
            rowIndex++;
        }
    }

    public void addSheetData(String sheetName, String[][] data, int rowStart){
        Sheet sheet = xls.getSheet(sheetName);
        int rowIndex = rowStart;
        for(String[] rowData : data){
            Row row = sheet.createRow(rowIndex);
            int cellIndex = 0;
            if(rowData!=null){
                for(String cellData:rowData){
                    Cell cell = row.createCell(cellIndex);
                    cell.setCellValue(cellData);
                    cellIndex++;
                }
            }
            rowIndex++;
        }
    }

    public void addSheetData(String sheetName, String[][] data, int rowStart, int colStart){
        Sheet sheet = xls.getSheet(sheetName);
        int rowIndex = rowStart;
        for(String[] rowData : data){
            Row row = sheet.createRow(rowIndex);
            int cellIndex = colStart;
            if(rowData!=null){
                for(String cellData:rowData){
                    Cell cell = row.createCell(cellIndex);
                    cell.setCellValue(cellData);
                    cellIndex++;
                }
            }
            rowIndex++;
        }
    }
}
