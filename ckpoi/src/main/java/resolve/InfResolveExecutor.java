package resolve;

import base.CommonLogger;
import base.PoiDocReader;
import doc.DocReader;
import doc.DocxReader;
import xls.XlsWriter;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title: InfResolveExecutor
 * @Description: 接口解析器
 * @Author: Chengkai
 * @Date: 2019/7/19 15:00
 * @Version: 1.0
 */
public class InfResolveExecutor extends CommonLogger {

    /**
     * 解析接口文件目录
     *
     * @param dir
     * @throws IOException
     */
    public static void resolveDir(String dir, JProgressBar progressBar1) {
        File dirFile = new File(dir);
        String[] fileNames = dirFile.list((dirF, name) -> PoiDocReader.docx(name) || PoiDocReader.doc(name));
        String[] dirNames = dirFile.list((dirF, name) -> dirF.isDirectory());
        if (fileNames != null) {
            for (String name : fileNames) {
                resolve(dir + File.separator + name, progressBar1);
            }
        }
        if (dirNames != null) {
            for (String dirS : dirNames) {
                resolveDir(dir + File.separator + dirS, progressBar1);
            }
        }
    }

    /**
     * 解析单个接口文件
     *
     * @param path
     */
    public static void resolve(String path, JProgressBar progressBar1) {
        PoiDocReader reader = null;
        if (PoiDocReader.docx(path)) {
            try {
                reader = new DocxReader(path);
            } catch (IOException e) {
                logger.error(e);
            }
        } else if (PoiDocReader.doc(path)) {
            try {
                reader = new DocReader(path);
            } catch (IOException e) {
                logger.error(e);
            }
        }
        if (reader == null) {
            logger.warn("非word文件！");
            return;
        }
        String prefix = path.substring(0, path.lastIndexOf(File.separator) + 1);
        String name = path.substring(path.lastIndexOf(File.separator) + 1, path.lastIndexOf("."));
        XlsWriter writer = new XlsWriter();
        int infRowCount = 1;
        int infPropRowCount = 1;
        writer.createXls();
        writer.createSheet("接口汇总信息");
        writer.createSheet("接口基础信息");
        writer.createSheet("接口属性信息");
        String[] infHeader = new String[]{"接口分类", "接口编码", "接口单元名称", "接口单元说明", "抽取方式（增量/全量）", "周期（日/月/周）", "接口数据文件名", "接口校验文件名"};
        writer.addSheetData("接口基础信息", new String[][]{infHeader}, 0);
        infHeader = new String[]{"接口分类", "接口编码", "属性编码", "属性名称", "属性描述", "属性类型", "备注"};
        writer.addSheetData("接口属性信息", new String[][]{infHeader}, 0);
        InfSummary summary = new InfSummary();
        summary.setInfDetails(new ArrayList<>());
        List<String[][]> allTabs = reader.getAllTab();
        List<String[]> subData = new ArrayList<>();
        subData.add(new String[]{"接口单元编码", "接口单元名称", "接口单元说明"});
        logger.info("开始查找接口汇总信息...");
        int tabCount = 1;
        for (String[][] tab : allTabs) {
            if (tab[0] != null && tab[0].length >= 3 && tab[0][0].contains("接口单元编码") && tab[0][1].contains("接口单元名称") && tab[0][2].contains("接口单元说明")) {
                logger.info("表格"+tabCount+"中发现接口汇总信息:");
                for (int i = 1; i < tab.length; i++) {
                    logger.info("接口单元编码："+tab[i][0]);
                    logger.info("接口单元名称："+tab[i][1]);
                    logger.info("接口单元说明："+tab[i][2]);
                    subData.add(tab[i]);
                }
            }
            tabCount++;
        }
        String[][] sdd = new String[subData.size()][];
        for (int i = 0; i < subData.size(); i++) {
            sdd[i] = subData.get(i);
        }
        writer.addSheetData("接口汇总信息", sdd, 0);
        List<String> allParas = reader.getAllParaText();
        logger.info("查找接口存放接口基本信息的单格表格...");
        for (int j = 0; j < allTabs.size(); j++) {
            String[][] tab = allTabs.get(j);
            if (tab.length == 1 && tab[0].length == 1) {
                logger.info("表格"+j+"中发现接口基本信息：");
                InfDetail detail = new InfDetail();
                detail.setInfData(new String[7]);
                String[] parts = tab[0][0].split("\n");
                for (String part : parts) {
                    if (part.contains("接口单元编码")) {
                        detail.getInfData()[0] = part.replaceAll("接口单元编码：", "").trim();
                        logger.info("接口单元编码："+detail.getInfData()[0]);
                    } else if (part.contains("接口编号")) {
                        detail.getInfData()[0] = part.replaceAll("接口编号：", "").trim();
                        logger.info("接口编号："+detail.getInfData()[0]);
                    } else if (part.contains("接口单元名称")) {
                        detail.getInfData()[1] = part.replaceAll("接口单元名称：", "").trim();
                        logger.info("接口单元名称："+detail.getInfData()[1]);
                    } else if (part.contains("接口名称")) {
                        detail.getInfData()[1] = part.replaceAll("接口名称：", "").trim();
                        logger.info("接口名称："+detail.getInfData()[1]);
                    } else if (part.contains("元名称")) {
                        detail.getInfData()[1] = part.substring(part.indexOf("元名称"), part.length()).trim();
                        logger.info("元名称："+detail.getInfData()[1]);
                    } else if (part.contains("接口单元说明")) {
                        detail.getInfData()[2] = tab[0][0].substring(tab[0][0].indexOf("接口单元说明："), tab[0][0].length()).replaceAll("接口单元说明：", "").trim();
                        logger.info("接口单元说明："+detail.getInfData()[2]);
                        break;
                    } else if (part.contains("接口说明")) {
                        detail.getInfData()[2] = tab[0][0].substring(tab[0][0].indexOf("接口说明："), tab[0][0].length()).replaceAll("接口说明：", "").trim();
                        logger.info("接口说明："+detail.getInfData()[2]);
                        break;
                    }
                }
                String[][] propTab = allTabs.get(j + 1);
                String[][] props = new String[propTab.length - 1][];
                for (int i = 1; i < propTab.length; i++) {
                    props[i - 1] = propTab[i];
                }
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
                detail.setIndProps(props);
                summary.getInfDetails().add(detail);
                int detailPropCount = 0;
                for (int k = 0; k < allParas.size(); k++) {
                    if (allParas.get(k).contains("接口单元属性列表")) {
                        detailPropCount++;
                    }
                    if (detailPropCount == summary.getInfDetails().size()) {
                        logger.info("查找接口其他基本信息：");
                        if (allParas.get(k).contains("增量") || allParas.get(k).contains("新增")) {
                            detail.getInfData()[3] = "增量";
                        } else if (allParas.get(k).contains("全量")) {
                            detail.getInfData()[3] = "全量";
                        }
                        logger.info("抽取方式："+detail.getInfData()[3]);
                        if (allParas.get(k).contains("日")) {
                            detail.getInfData()[4] = "日";
                        } else if (allParas.get(k).contains("月")) {
                            detail.getInfData()[4] = "月";
                        } else if (allParas.get(k).contains("周")) {
                            detail.getInfData()[4] = "周";
                        }
                        logger.info("抽取周期："+detail.getInfData()[4]);
                        if (allParas.get(k).contains("数据文件")) {
                            detail.getInfData()[5] = allParas.get(k + 1).trim();
                        } else if (allParas.get(k).contains("校验文件")) {
                            detail.getInfData()[6] = allParas.get(k + 1).trim();
                        }
                        logger.info("数据文件："+detail.getInfData()[5]);
                        logger.info("校验文件："+detail.getInfData()[6]);
                    } else if (detailPropCount > summary.getInfDetails().size()) {
                        break;
                    }
                }
            }
            progressBar1.setValue(progressBar1.getValue()+1);
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
    }

}
