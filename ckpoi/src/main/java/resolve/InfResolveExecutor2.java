package resolve;

import base.CommonLogger;
import base.PoiDocReader;
import doc.DocReader;
import doc.DocxReader;
import xls.XlsWriter;

import java.io.File;
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
    public void resolveDir(String dir) throws IOException {
        File dirFile = new File(dir);
        String[] fileNames = dirFile.list((dirF, name) -> PoiDocReader.docx(name) || PoiDocReader.doc(name));
        String[] dirNames = dirFile.list((dirF, name) -> dirF.isDirectory());
        if (fileNames != null) {
            for (String name : fileNames) {
                resolve(dir + File.separator + name);
            }
        }
        if (dirNames != null) {
            for (String dirS : dirNames) {
                resolveDir(dir + File.separator + dirS);
            }
        }
    }

    /**
     * 解析单个接口文件
     *
     * @param path
     */
    public void resolve(String path) throws IOException {
        PoiDocReader reader = null;
        if (PoiDocReader.docx(path)) {
            reader = new DocxReader(path);
        } else if (PoiDocReader.doc(path)) {
            reader = new DocReader(path);
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
        writer.createSheet("接口基础信息");
        writer.createSheet("接口属性信息");
        String[] infHeader = new String[]{"接口分类", "接口编码", "接口单元名称", "接口单元说明", "抽取方式（增量/全量）", "周期（日/月/周）", "接口数据文件名", "接口校验文件名"};
        writer.addSheetData("接口基础信息", new String[][]{infHeader}, 0);
        infHeader = new String[]{"接口分类", "接口编码", "属性编码", "属性名称", "属性描述", "属性类型", "备注"};
        writer.addSheetData("接口属性信息", new String[][]{infHeader}, 0);
        InfSummary summary = new InfSummary();
        summary.setInfDetails(new ArrayList<>());
        List<String[][]> allTabs = reader.getAllTab();
        for (int j = 0; j < allTabs.size(); j++) {
            String[][] tab = allTabs.get(j);
            if (tab.length>6&&tab[0].length>0&&tab[0][0].contains("接口名称")&&tab[1].length>0&&tab[1][0].contains("接口编码")&&tab[2].length>0&&tab[2][0].contains("接口说明")) {
                InfDetail detail = new InfDetail();
                detail.setInfData(new String[7]);
                detail.getInfData()[0] = tab[1][1];
                detail.getInfData()[1] = tab[0][1];
                detail.getInfData()[2] = tab[2][1];
                if(tab[4].length>1){
                    if(tab[4][1].contains("增量")||tab[4][1].contains("新增")){
                        detail.getInfData()[3] = "增量";
                    }else if(tab[4][1].contains("全量")){
                        detail.getInfData()[3] = "全量";
                    }
                    if(tab[4][1].contains("日")){
                        detail.getInfData()[4] = "日";
                    }else if(tab[4][1].contains("月")){
                        detail.getInfData()[4] = "月";
                    }else if(tab[4][1].contains("周")){
                        detail.getInfData()[4] = "周";
                    }
                }
                if(tab[5].length>2){
                    detail.getInfData()[5] = tab[5][2];
                }
                if(tab[6].length>2){
                    detail.getInfData()[6] = tab[6][2];
                }
                String[][] props = new String[tab.length-8][];
                System.arraycopy(tab,8,props,0,tab.length-8);
                detail.setIndProps(props);
                summary.getInfDetails().add(detail);
            }
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

    public static void main(String[] args) throws IOException {
        InfResolveExecutor2 executor = new InfResolveExecutor2();
        executor.resolveDir("C:\\Users\\m1816\\Desktop\\tochengkai");
    }

}
