package resolve;

import base.PoiDocReader;
import xls.XlsWriter;

import java.io.File;

/**
 * @Title: InterCateGoryExecutor
 * @Author: Chengkai
 * @Date: 2019/7/25 11:49
 * @Version: 1.0
 */
public class InterCateGoryExecutor {

    public static void resolveSubCate(String dir){
        File dirFile = new File(dir);
        String[] fileNames = dirFile.list((dirF,name)-> PoiDocReader.docx(name)||PoiDocReader.doc(name));
        XlsWriter writer = new XlsWriter();
        writer.createXls();
        writer.createSheet("新增接口类目");
        String[][] data = new String[fileNames.length+1][];
        data[0] = new String[]{"文件名","架构结点","架构结点","架构结点"};
        for(int i = 0;i<fileNames.length;i++){
            String name = fileNames[i];
            data[i+1] = new String[4];
            data[i+1][0] = name;
            data[i+1][1] ="一级系统（S1）";
            data[i+1][2] ="增值业务综合运营平台（S1-VGOP）";
            int start = name.lastIndexOf("- ");
            if(start!=-1){
                start +=2;
            }else{
                start = name.lastIndexOf("－");
                if(start!=-1){
                    start +=1;
                }else{
                    start = name.lastIndexOf("-");
                    if(start!=-1){
                        start +=1;
                    }
                }
            }
            int end = name.lastIndexOf("(");
            if(end==-1){
                end = name.lastIndexOf("V");
            }else{
                int temp = name.lastIndexOf("V");
                if(temp<end){
                    end = temp;
                }
            }
            if(start>-1&&end>-1){
                data[i+1][3] = name.substring(start,end).trim();
            }
        }
        writer.addSheetData("新增接口类目",data,0);
        writer.saveXls(dir.substring(0,dir.lastIndexOf(File.separator))+File.separator+"category.xls");
    }

    public static void main(String[] args){
        resolveSubCate("C:\\Users\\m1816\\Desktop\\20190719备份\\tochengkai");
    }
}
