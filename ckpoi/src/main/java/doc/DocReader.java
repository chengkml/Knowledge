package doc;

import base.PoiDocReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hwpf.usermodel.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DocReader extends PoiDocReader {

    public DocReader(String path) throws IOException {
        super(path);
    }

    @Override
    public List<String[][]> getAllTab() {
        TableIterator it = getTabIterator();
        List<String[][]> datas = new ArrayList<>();
        int total = 0;
        while(it.hasNext()){
            total++;
            Table tab = it.next();
            int rowNum = tab.numRows();
            String[][] tabData = new String[rowNum][];
            List<String> tabStr = new ArrayList<>();
            readTab(datas, tab, rowNum, tabData, tabStr);
        }
        logger.debug("读取到表格总数："+total);
        return datas;
    }

    /**
     * 获取表格迭代器
     * @return
     */
    public TableIterator getTabIterator(){
        Range range = doc.getRange();
        return new TableIterator(range);
    }

    @Override
    public List<String[][]> filterTabByHeader(String[] headers) {
        TableIterator it = getTabIterator();
        List<String[][]> datas = new ArrayList<>();
        int total = 0;
        int validTotal = 0;
        while(it.hasNext()){
            total++;
            Table tab = it.next();
            int rowNum = tab.numRows();
            String[][] tabData = new String[rowNum][];
            List<String> tabStr = new ArrayList<>();
            boolean valid = testHeader(headers, tab);
            if(!valid){
                logger.debug("表格表头不符合要求，忽略。");
                continue;
            }
            validTotal++;
            readTab(datas, tab, rowNum, tabData, tabStr);
        }
        logger.debug("读取到表格总数："+total+"，有效表格总数："+validTotal);
        return datas;
    }

    @Override
    public List<String[][]> filterDataByHeader(String[] headers) {
        TableIterator it = getTabIterator();
        List<String[][]> datas = new ArrayList<>();
        int total = 0;
        int validTotal = 0;
        while(it.hasNext()){
            total++;
            Table tab = it.next();
            int rowNum = tab.numRows();
            String[][] tabData = new String[rowNum][];
            List<String> tabStr = new ArrayList<>();
            boolean valid = testHeader(headers, tab);
            if(!valid){
                logger.debug("表格表头不符合要求，忽略。");
                continue;
            }
            validTotal++;
            readData(datas, tab, rowNum, tabData, tabStr);
        }
        logger.info("读取到表格总数："+total+"，有效表格总数："+validTotal );
        return datas;
    }

    @Override
    public List<String[][]> filterOneCellTab() {
        TableIterator it = getTabIterator();
        List<String[][]> datas = new ArrayList<>();
        int total = 0;
        int validTotal = 0;
        while(it.hasNext()){
            total++;
            Table tab = it.next();
            int rowNum = tab.numRows();
            if(rowNum>1){
                logger.debug("表格不止一行，忽略。");
                continue;
            }
            int cellNum = tab.getRow(0).numCells();
            if(cellNum>1){
                logger.debug("表格不止一列，忽略。");
                continue;
            }
            String[][] tabData = new String[rowNum][];
            List<String> tabStr = new ArrayList<>();
            validTotal++;
            readTab(datas, tab, rowNum, tabData, tabStr);
        }
        logger.info("读取到表格总数："+total+"，有效表格总数："+validTotal );
        return datas;
    }

    @Override
    public List<String> getAllParaText() {
        List<String> paraTexts = new ArrayList<>();
        Range range = doc.getRange();
        int total = range.numParagraphs();
        for(int i = 0;i<total;i++){
            paraTexts.add(range.getParagraph(i).text());
        }
        return paraTexts;
    }

    /**
     * 读表格
     * @param datas
     * @param tab
     * @param rowNum
     * @param tabData
     * @param tabStr
     */
    private void readTab(List<String[][]> datas, Table tab, int rowNum, String[][] tabData, List<String> tabStr) {
        logger.debug("读表格开始...");
        for (int i = 0; i < rowNum; i++) {
            readRow(tab, tabData, tabStr, i);
        }
        logger.debug("读表格结束，表格数据：\t");
        for (String s : tabStr) {
            logger.debug(s);
        }
        datas.add(tabData);
    }

    /**
     * 读表格的数据行
     * @param datas
     * @param tab
     * @param rowNum
     * @param tabData
     * @param tabStr
     */
    private void readData(List<String[][]> datas, Table tab, int rowNum, String[][] tabData, List<String> tabStr) {
        logger.debug("读表格开始...");
        if(rowNum<=1){
            logger.debug("该表格没有数据行！");
            return;
        }
        for (int i = 1; i < rowNum; i++) {
            readRow(tab, tabData, tabStr, i);
        }
        logger.debug("读表格结束，表格数据：\t");
        for (String s : tabStr) {
            logger.debug(s);
        }
        datas.add(tabData);
    }

    /**
     * 读表格的所有行
     * @param tab 表格
     * @param tabData 表格数据输出数组
     * @param tabStr 用于日志打印的表格数据
     * @param i 表格行索引
     */
    private void readRow(Table tab, String[][] tabData, List<String> tabStr, int i) {
        StringBuilder rowStr = doReadRows(tab, tabData, i);
        if (rowStr.length() > 0) {
            logger.debug("读表格行结束，单元格中的一行：" + rowStr.substring(0, rowStr.length() - 1));
            tabStr.add(rowStr.substring(0, rowStr.length() - 1));
        } else {
            logger.debug("读表格行结束，该行数据为空！");
        }
    }

    private StringBuilder doReadRows(Table tab, String[][] tabData, int i) {
        TableRow row = tab.getRow(i);
        int cellNum = row.numCells();
        String[] rowArr = new String[cellNum];
        StringBuilder rowStr = new StringBuilder();
        logger.debug("读表格行开始...");
        for (int j = 0; j < cellNum; j++) {
            TableCell cell = row.getCell(j);
            int cellParNums = cell.numParagraphs();
            StringBuilder sb = new StringBuilder();
            logger.debug("读单元格开始...");
            for (int k = 0; k < cellParNums; k++) {
                Paragraph para = cell.getParagraph(k);
                String textPart = para.text();
                sb.append(textPart.trim());
                if(k+1<cellParNums){
                    sb.append("\n");
                }
                logger.debug("单元格中的行：" + textPart);
            }
            logger.debug("读单元格结束，单元格内容：" + sb.toString());
            rowArr[j] = sb.toString();
            rowStr.append(sb.toString()).append("|");
        }
        tabData[i] = rowArr;
        return rowStr;
    }

    private StringBuilder doReadRows(Table tab, String[][] tabData, int i, int m) {
        TableRow row = tab.getRow(i);
        int cellNum = row.numCells();
        String[] rowArr = new String[cellNum];
        StringBuilder rowStr = new StringBuilder();
        logger.debug("读表格行开始...");
        for (int j = 0; j < cellNum; j++) {
            TableCell cell = row.getCell(j);
            int cellParNums = cell.numParagraphs();
            StringBuilder sb = new StringBuilder();
            logger.debug("读单元格开始...");
            for (int k = 0; k < cellParNums; k++) {
                Paragraph para = cell.getParagraph(k);
                String textPart = para.text();
                sb.append(textPart);
                logger.debug("单元格中的行：" + textPart);
            }
            logger.debug("读单元格结束，单元格内容：" + sb.toString());
            rowArr[j] = sb.toString();
            rowStr.append(sb.toString()).append("|");
        }
        tabData[i-m] = rowArr;
        return rowStr;
    }

    /**
     * 测试表格表头(要考虑单元格内换行问题)
     * @param headers 表头
     * @param tab 表格
     * @return 是否通过测试
     */
    private boolean testHeader(String[] headers, Table tab) {
        TableRow firstRow = tab.getRow(0);
        if(headers.length>firstRow.numCells()){
            return false;
        }
        int firstCells = firstRow.numCells() > headers.length ? headers.length : firstRow.numCells();
        logger.debug("测试表头开始...");
        for (int i = 0; i < firstCells - 1; i++) {
            TableCell cell = firstRow.getCell(i);
            int cellParNums = cell.numParagraphs();
            StringBuilder sb = new StringBuilder();
            for (int k = 0; k < cellParNums; k++) {
                Paragraph para = cell.getParagraph(k);
                String textPart = para.text();
                sb.append(textPart);
            }
            if(!sb.toString().contains(headers[i])){
                return false;
            }
        }
        return true;
    }

    /**
     * 获取doc中所有段落
     * @return
     */
    public List<Paragraph> getAllParagraphs() {
        Range range = doc.getRange();
        List<Paragraph> paras = new ArrayList<>();
        int total = range.numParagraphs();
        for(int i = 0;i<total;i++){
            paras.add(range.getParagraph(i));
        }
        return paras;
    }

    /**
     * 根据字体大小过滤段落
     * @param fontSize
     * @return
     */
    public List<Paragraph> filterParagraphByFontSize(int fontSize) {
        Range range = doc.getRange();
        List<Paragraph> paras = new ArrayList<>();
        int total = range.numParagraphs();
        for(int i = 0;i<total;i++){
            Paragraph par = range.getParagraph(i);
            CharacterRun run = par.getCharacterRun(0);
            if(run.getFontSize()==fontSize&& StringUtils.isNotBlank(par.text())){
                paras.add(par);
            }
        }
        return paras;
    }

    /**
     * 根据标题级别过滤段落
     * @param lvl
     * @return
     */
    public List<Paragraph> filterParagraphByLvl(int lvl) {
        Range range = doc.getRange();
        List<Paragraph> paras = new ArrayList<>();
        int total = range.numParagraphs();
        for(int i = 0;i<total;i++){
            Paragraph par = range.getParagraph(i);
            if(par.getLvl()==lvl&& StringUtils.isNotBlank(par.text())){
                paras.add(par);
            }
        }
        return paras;
    }

    /**
     * 获取下一个有效的段落
     * @param it
     * @return
     */
    public Paragraph nextValidParagraph(Iterator<Paragraph> it){
        while(it.hasNext()){
            Paragraph p = it.next();
            if(p!=null&&StringUtils.isNotBlank(p.text())){
                return p;
            }
        }
        return null;
    }

    /**
     * 根据关键字获取下一个有效的段落
     * @param it
     * @param key
     * @return
     */
    public Paragraph nextValidParagraph(Iterator<Paragraph> it, String key){
        while(it.hasNext()){
            Paragraph p = it.next();
            if(p!=null&&StringUtils.isNotBlank(p.text())&&p.text().contains(key)){
                return p;
            }
        }
        return null;
    }

}
