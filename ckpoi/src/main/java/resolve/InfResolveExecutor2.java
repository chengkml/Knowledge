package resolve;

import base.CommonLogger;
import base.PoiDocReader;
import doc.DocReader;
import doc.DocxReader;
import org.apache.log4j.Level;
import xls.XlsWriter;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title: InfResolveExecutor
 * @Description: 接口解析器2
 * @Author: Chengkai
 * @Date: 2019/7/19 15:00
 * @Version: 1.0
 */
public class InfResolveExecutor2 extends CommonLogger {

    /**
     * 解析接口文件目录
     *
     * @param dir
     * @throws IOException
     */
    public static void resolveDir(String dir, JProgressBar progressBar1) {
        File dirFile = new File(dir);
        String[] fileNames = dirFile.list((dirF, name) -> PoiDocReader.docx(name) || PoiDocReader.doc(name));
        String[] dirNames = dirFile.list((dirF, name) -> new File(dirF, name).isDirectory());
        if (fileNames != null) {
            logger.info("目录下发现word文档" + fileNames.length + "个...");
            for (String name : fileNames) {
                logger.info("解析接口文件“" + name + "”...");
                int tempCount = progressBar1.getValue();
                if (resolve(dir + File.separator + name, progressBar1) == 0) {
                    logger.info("文档“"+name+"”未解析到接口信息，尝试采用格式2进行解析...");
                    progressBar1.setValue(tempCount);
                    InfResolveExecutor.resolve(dir + File.separator + name, progressBar1);
                }
            }
        }
        if (dirNames != null && dirNames.length > 0) {
            logger.info("目录“" + dir + "”下发现子目录" + dirNames.length + "个...");
            for (String dirS : dirNames) {
                logger.info("解析目录“" + dirS + "”下的接口文件...");
                resolveDir(dir + File.separator + dirS, progressBar1);
            }
        }
    }

    /**
     * 解析单个接口文件
     *
     * @param path
     */
    public static int resolve(String path, JProgressBar progressBar1) {
        PoiDocReader reader = null;
        if (PoiDocReader.docx(path)) {
            try {
                reader = new DocxReader(path);
            } catch (FileNotFoundException e) {
                logger.error("未找到文件“" + path + "”异常!", e);
            } catch (IOException ie) {
                logger.error(ie);
            }
        } else if (PoiDocReader.doc(path)) {
            try {
                reader = new DocReader(path);
            } catch (FileNotFoundException e) {
                logger.error("未找到文件“" + path + "”异常!", e);
            } catch (IOException ie) {
                logger.error(ie);
            }
        }
        if (reader == null) {
            logger.error("“" + path + "”非word文件！");
            return 0;
        }
        String prefix = path.substring(0, path.lastIndexOf(File.separator) + 1);
        String name = path.substring(path.lastIndexOf(File.separator) + 1, path.lastIndexOf("."));
        XlsWriter writer = new XlsWriter();
        int infRowCount = 1;
        int infPropRowCount = 1;
        writer.createXls();
        writer.createSheet("接口基础信息");
        writer.createSheet("接口属性信息");
        String[] infHeader = new String[]{"接口分类", "接口编码", "接口单元名称", "接口单元说明", "抽取方式（增量/全量）", "周期（日/月/周）", "接口数据文件名", "接口校验文件名"};
        writer.addSheetData("接口基础信息", new String[][]{infHeader}, 0);
        infHeader = new String[]{"接口分类", "接口编码", "属性编码", "属性名称", "属性描述", "属性类型", "备注"};
        writer.addSheetData("接口属性信息", new String[][]{infHeader}, 0);

        InfSummary summary = new InfSummary();
        summary.setInfDetails(new ArrayList<>());
        List<String[][]> allTabs = reader.getAllTab();
        logger.info("文档中发现表格数量" + allTabs.size() + "个");
        for (int j = 0; j < allTabs.size(); j++) {
            String[][] tab = allTabs.get(j);
            if (tab.length > 6 && tab[0].length > 0 && tab[0][0].contains("接口名称") && tab[1].length > 0 && tab[1][0].contains("接口编码") && tab[2].length > 0 && tab[2][0].contains("接口说明")) {
                logger.info("解析第" + (j + 1) + "个表格中的接口信息:");
                InfDetail detail = new InfDetail();
                detail.setInfData(new String[7]);
                detail.getInfData()[1] = tab[0][1];
                logger.info("接口名称：" + tab[0][1]);
                detail.getInfData()[0] = tab[1][1];
                logger.info("接口编码：" + tab[1][1]);
                detail.getInfData()[2] = tab[2][1];
                logger.info("接口说明：" + tab[2][1]);
                if (tab[4].length > 1) {
                    if (tab[4][1].contains("增量") || tab[4][1].contains("新增")) {
                        detail.getInfData()[3] = "增量";
                    } else if (tab[4][1].contains("全量")) {
                        detail.getInfData()[3] = "全量";
                    }
                    if (tab[4][1].contains("日")) {
                        detail.getInfData()[4] = "日";
                    } else if (tab[4][1].contains("月")) {
                        detail.getInfData()[4] = "月";
                    } else if (tab[4][1].contains("周")) {
                        detail.getInfData()[4] = "周";
                    }
                }
                logger.info("接口抽取方式：" + detail.getInfData()[3]);
                logger.info("周期（日/月/周）：" + detail.getInfData()[4]);
                if (tab[5].length > 2) {
                    detail.getInfData()[5] = tab[5][2];
                }
                logger.info("接口数据文件名：" + detail.getInfData()[5]);
                if (tab[6].length > 2) {
                    detail.getInfData()[6] = tab[6][2];
                }
                logger.info("接口校验文件名：" + detail.getInfData()[6]);
                String[][] props = new String[tab.length - 8][];
                System.arraycopy(tab, 8, props, 0, tab.length - 8);
                detail.setIndProps(props);
                logger.info("接口属性信息：");
                for(int i = 0;i<props.length;i++){
                    if(i+1>=props.length){
                        StringBuilder sb = new StringBuilder();
                        for(int ii = 0;ii<props[i].length;ii++){
                            sb.append(props[i][ii].replaceAll("\n","")).append("-");
                        }
                        while(sb.toString().endsWith("-")){
                            sb.deleteCharAt(sb.length()-1);
                        }
                        logger.info(sb+"\n");
                    }else{
                        StringBuilder sb = new StringBuilder();
                        for(int ii = 0;ii<props[i].length;ii++){
                            sb.append(props[i][ii].replaceAll("\n","")).append("-");
                        }
                        while(sb.toString().endsWith("-")){
                            sb.deleteCharAt(sb.length()-1);
                        }
                        logger.info(sb);
                    }
                }
                summary.getInfDetails().add(detail);
            }
            progressBar1.setValue(progressBar1.getValue() + 1);
        }
        List<InfDetail> infDetails = summary.getInfDetails();
        for (InfDetail detail : infDetails) {
            String[] data = detail.getInfData();
            String[] dd = new String[data.length + 1];
            System.arraycopy(data, 0, dd, 1, data.length);
            writer.addSheetData("接口基础信息", new String[][]{dd}, infRowCount);
            infRowCount++;
            String[][] propData = detail.getIndProps();
            List<String[]> dData = new ArrayList<>();
            for (int i = 0; i < propData.length; i++) {
                if (propData[i].length > 0 && propData[i][0] != null) {
                    String[] propRow = propData[i];
                    dd = new String[data.length + 2];
                    System.arraycopy(propRow, 0, dd, 2, propRow.length);
                    dd[1] = data[0];
                    dData.add(dd);
                }
            }
            String[][] toWrite = new String[dData.size()][];
            for (int i = 0; i < dData.size(); i++) {
                toWrite[i] = dData.get(i);
            }
            writer.addSheetData("接口属性信息", toWrite, infPropRowCount);
            infPropRowCount += toWrite.length;
        }
        writer.saveXls(prefix + name + ".xls");
        return infDetails.size();
    }

    public static void resolveWithLog(String filePath, TextAreaLogAppender appender, JProgressBar progressBar1, JButton resolveBtn) {
        logger.setLevel(Level.INFO);
        logger.addAppender(appender);
        logger.info("开始解析接口文档...");
        List<String[][]> allTabs = new ArrayList<>();
        countDirTabs(filePath, allTabs);
        progressBar1.setMaximum(allTabs.size());
        progressBar1.setValue(0);
        File file = new File(filePath);
        if (file.isDirectory()) {
            logger.info("解析目录“" + filePath + "”下的接口文件...");
            resolveDir(filePath, progressBar1);
        } else {
            logger.info("解析接口文件“" + filePath + "”...");
            if (resolve(filePath, progressBar1) == 0) {
                logger.info("未解析到接口信息，尝试采用格式2进行解析...");
                InfResolveExecutor.resolve(filePath, progressBar1);
            }
        }
        resolveBtn.setEnabled(true);
        logger.info("接口文档解析完毕。");
    }

    private static void countDirTabs(String dir, List<String[][]> allTabs) {
        int count = 0;
        File dirFile = new File(dir);
        String[] fileNames = dirFile.list((dirF, name) -> PoiDocReader.docx(name) || PoiDocReader.doc(name));
        String[] dirNames = dirFile.list((dirF, name) -> new File(dirF, name).isDirectory());
        if (fileNames != null) {
            for (String name : fileNames) {
                countFileTabs(dir + File.separator + name, allTabs);
            }
        }
        if (dirNames != null && dirNames.length > 0) {
            for (String dirS : dirNames) {
                countDirTabs(dir + File.separator + dirS, allTabs);
            }
        }
    }

    private static void countFileTabs(String path, List<String[][]> allTabs) {
        PoiDocReader reader = null;
        if (PoiDocReader.docx(path)) {
            try {
                reader = new DocxReader(path);
            } catch (FileNotFoundException e) {
                logger.error("未找到文件“" + path + "”异常!", e);
            } catch (IOException ie) {
                logger.error(ie);
            }
        } else if (PoiDocReader.doc(path)) {
            try {
                reader = new DocReader(path);
            } catch (FileNotFoundException e) {
                logger.error("未找到文件“" + path + "”异常!", e);
            } catch (IOException ie) {
                logger.error(ie);
            }
        }
        if (reader == null) {
            logger.error("“" + path + "”非word文件！");
            return;
        }
        allTabs.addAll(reader.getAllTab());
    }
}
