package resolvepo;

import xls.XlsWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Title: FileResolver
 * @Description: 文件解析器
 * @Author: Chengkai
 * @Date: 2019/9/26 17:26
 * @Version: 1.0
 */
public class FileResolver {

    public static List<File> getSubFiles(String fileName){
        File root = new File(fileName);
        List<File> fileList = new ArrayList<>();
        fileList.add(root);
        addSubFile(root,fileList);
        return fileList;
    }

    public static void addSubFile(File parent, List<File> fileList){
        if(parent.isDirectory()){
            File[] temp = parent.listFiles();
            fileList.addAll(Arrays.asList(temp));
            for(File file : temp){
                addSubFile(file,fileList);
            }
        }
    }

    public static void main(String[] args){
        XlsWriter writer = new XlsWriter();
        writer.createXls();
        List<File> fileList = getSubFiles("D:\\idea_repo\\guangdong");
        String[] targets = new String[]{"MetaTablePo.java","MetaTableAlterPo.java","MetaTableHisPo.java","MetaTabFieldPo.java","MetaTabFieldAlterPo.java","MetaTabFieldHisPo.java","MetaMemberTabPo.java","MetaTablePrivPo.java","MetaTableFieldPrivPo.java","MetaTableOpenPo.java"};
        List<String> names = Arrays.asList(targets);
        for(File file : fileList){
            if(names.contains(file.getName())){
                System.out.println(file);
                try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))){
                    writer.createSheet(file.getName());
                    String temp = br.readLine();
                    List<List<String>> data = new ArrayList<>();
                    List<String> row = new ArrayList<>();
                    String lastLen = null;
                    boolean start = true;
                    boolean lastNull = false;
                    while (temp!=null) {
                        if(temp.contains("private")&&!temp.contains("List")){
                            String type = temp.substring(temp.indexOf("private ")+8,temp.lastIndexOf(" "));
                            if(type.trim().contains("String")){
                                row.add("varchar");
                            }else if(type.trim().contains("Date")){
                                row.add("datetime");
                            }else if(type.trim().contains("Integer")){
                                row.add("int");
                            }
                            row.add(lastLen);
                            row.add("");
                            row.add(lastNull?"NO":"YES");
                            lastNull = false;
                        }
                        if(temp.contains("@Column(name=")||temp.contains("@Column(name = ")){
                            if(start){
                                start = false;
                            }else{
                                data.add(row);
                            }
                            row = new ArrayList<>();
                            row.add(temp.substring(temp.indexOf("\"")+1,temp.lastIndexOf("\"")));
                        }
                        if(temp.contains("nullable=false")){
                            lastNull = true;
                        }
                        if(temp.contains("length=")){
                            if(temp.indexOf(",",temp.indexOf("length="))!=-1){
                                lastLen = temp.substring(temp.indexOf("length=")+7,temp.indexOf(",",temp.indexOf("length=")));
                            }else if(temp.indexOf(")",temp.indexOf("length="))!=-1){
                                lastLen = temp.substring(temp.indexOf("length=")+7,temp.indexOf(")",temp.indexOf("length=")));
                            }
                        }
                        temp = br.readLine();
                    }
                    String[][] toWrite = new String[data.size()][];
                    for(int i = 0;i<data.size();i++){
                        Object[] tempA = data.get(i).toArray();
                        String[] r = new String[tempA.length];
                        for(int j = 0;j<tempA.length;j++){
                            r[j] = (String) tempA[j];
                        }
                        toWrite[i] = r;
                    }
                    writer.addSheetData(file.getName(),toWrite,0);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        writer.saveXls("C:\\Users\\m1816\\Desktop\\potest.xls");
    }
}
