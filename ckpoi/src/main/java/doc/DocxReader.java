package doc;

import base.PoiDocReader;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title: DocxReader
 * @Author: Chengkai
 * @Date: 2019/7/19 12:38
 * @Version: 1.0
 */
public class DocxReader extends PoiDocReader {

    public DocxReader(String path) throws IOException {
        super(path);
    }

    @Override
    public List<String[][]> getAllTab() {
        List<String[][]> data = new ArrayList<>();
        List<XWPFTable> tables = docx.getTables();
        for (XWPFTable table : tables) {
            List<XWPFTableRow> rows = table.getRows();
            int rowNum = rows.size();
            String[][] tabData = new String[rowNum][];
            data.add(tabData);
            for (int i = 0; i < rowNum; i++) {
                XWPFTableRow row = rows.get(i);
                List<XWPFTableCell> cells = row.getTableCells();
                int cellNum = cells.size();
                String[] rowData = new String[cellNum];
                tabData[i] = rowData;
                for (int j = 0; j < cellNum; j++) {
                    XWPFTableCell cell = cells.get(j);
                    List<XWPFParagraph> parts = cell.getParagraphs();
                    int cellParNums = parts.size();
                    StringBuilder sb = new StringBuilder();
                    logger.debug("读单元格开始...");
                    for (int k = 0; k < cellParNums; k++) {
                        XWPFParagraph para = parts.get(k);
                        String textPart = para.getText();
                        sb.append(textPart.trim());
                        if(k+1<cellParNums){
                            sb.append("\n");
                        }
                        logger.debug("单元格中的行：" + textPart);
                    }
                    logger.debug("读单元格结束，单元格内容：" + sb.toString());
                    rowData[j] = sb.toString();
                }
            }
        }
        return data;
    }

    @Override
    public List<String[][]> filterTabByHeader(String[] headers) {
        List<String[][]> data = new ArrayList<>();
        List<XWPFTable> tables = docx.getTables();
        for (XWPFTable table : tables) {
            List<XWPFTableRow> rows = table.getRows();
            if (testHeader(rows.get(0), headers)) {
                int rowNum = rows.size();
                String[][] tabData = new String[rowNum][];
                data.add(tabData);
                for (int i = 0; i < rowNum; i++) {
                    XWPFTableRow row = rows.get(i);
                    List<XWPFTableCell> cells = row.getTableCells();
                    int cellNum = cells.size();
                    String[] rowData = new String[cellNum];
                    tabData[i] = rowData;
                    for (int j = 0; j < cellNum; j++) {
                        XWPFTableCell cell = cells.get(j);
                        rowData[j] = cell.getText();
                    }
                }
            } else {
                logger.debug("表格表头不符合要求，忽略。");
            }
        }
        return data;
    }

    @Override
    public List<String[][]> filterDataByHeader(String[] headers) {
        List<String[][]> data = new ArrayList<>();
        List<XWPFTable> tables = docx.getTables();
        for (XWPFTable table : tables) {
            List<XWPFTableRow> rows = table.getRows();
            int rowNum = rows.size();
            if(rowNum<=1){
                logger.debug("忽略只有一行的表格！");
                continue;
            }
            if (testHeader(rows.get(0), headers)) {
                String[][] tabData = new String[rowNum-1][];
                data.add(tabData);
                for (int i = 1; i < rowNum; i++) {
                    XWPFTableRow row = rows.get(i);
                    List<XWPFTableCell> cells = row.getTableCells();
                    int cellNum = cells.size();
                    String[] rowData = new String[cellNum];
                    tabData[i-1] = rowData;
                    for (int j = 0; j < cellNum; j++) {
                        XWPFTableCell cell = cells.get(j);
                        rowData[j] = cell.getText();
                    }
                }
            } else {
                logger.debug("表格表头不符合要求，忽略。");
            }
        }
        return data;
    }

    @Override
    public List<String[][]> filterOneCellTab() {
        List<String[][]> data = new ArrayList<>();
        List<XWPFTable> tables = docx.getTables();
        for (XWPFTable table : tables) {
            List<XWPFTableRow> rows = table.getRows();
            if (testOneCell(rows)) {
                int rowNum = rows.size();
                String[][] tabData = new String[rowNum][];
                data.add(tabData);
                for (int i = 0; i < rowNum; i++) {
                    XWPFTableRow row = rows.get(i);
                    List<XWPFTableCell> cells = row.getTableCells();
                    int cellNum = cells.size();
                    String[] rowData = new String[cellNum];
                    tabData[i] = rowData;
                    for (int j = 0; j < cellNum; j++) {
                        XWPFTableCell cell = cells.get(j);
                        rowData[j] = cell.getText();
                    }
                }
            } else {
                logger.debug("表格表头不符合要求，忽略。");
            }
        }
        return data;
    }

    @Override
    public List<String> getAllParaText() {
        List<String> texts = new ArrayList<>();
        List<XWPFParagraph> paras = docx.getParagraphs();
        for(XWPFParagraph para : paras){
            texts.add(para.getText());
        }
        return texts;
    }

    private boolean testOneCell(List<XWPFTableRow> rows) {
        if(rows.size()!=1){
            return false;
        }
        return rows.get(0).getTableCells().size()==1;
    }

    private boolean testHeader(XWPFTableRow row, String[] headers) {
        List<XWPFTableCell> cells = row.getTableCells();
        if (cells.size() < headers.length) {
            return false;
        }
        int testMax = cells.size()<headers.length?cells.size():headers.length;
        for (int i = 0; i < testMax; i++) {
            if(!cells.get(i).getText().contains(headers[i])){
                return false;
            }
        }
        return true;
    }
}
