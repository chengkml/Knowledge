package com.ck.ddl;

import javafx.scene.control.Tab;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PoGenerator {

    public static String generatePoStr(Table table) {
        StringBuilder sb = new StringBuilder();
        addImports(sb);
        addAuthorInfo(sb);
        addClassExplain(sb,table);
        addClassDetails(sb,table);
        return sb.toString();
    }

    private static void addClassDetails(StringBuilder sb, Table table) {
        sb.append("public class ").append(toCamel(table.getName(),true)).append(" implements Serializable {").append("\n");
        sb.append("\n");
        table.getColumns().forEach(col->{
            if(col.isPk()){
                sb.append("\t@Id").append("\n");
            }
            if(col.getLength()==4000||(col.getDataType()!=null&&col.getDataType().equalsIgnoreCase("text"))){
                sb.append("\t@Lob").append("\n");
            }
            sb.append("\t@Column(name = \"").append(col.getName()).append("\"");
            if(col.getLength()>0&&col.getLength()<4000){
                sb.append(", length = ").append(col.getLength());
            }
            if(!col.isNullable()){
                sb.append(", nullable = false");
            }
            sb.append(")").append("\n");
            if(StringUtils.isNotBlank(col.getComment())){
                sb.append("\t@ColumnExt(columnComment = \"").append(col.getComment()).append("\")").append("\n");
            }
            sb.append("\tprotected String ").append(toCamel(col.getName(),false)).append(";").append("\n");
            sb.append("\n");
        });
        sb.append("}").append("\n");
    }

    private static String toCamel(String name,boolean headUpper) {
        StringBuilder sb = new StringBuilder();
        char[] arr = name.toCharArray();
        for(int i = 0;i<arr.length;i++){
            if(headUpper&&i==0){
                sb.append((arr[i]+"").toUpperCase());
            }else if("_".equals(arr[i]+"")){
                continue;
            }else if(i>1&&"_".equals(arr[i-1]+"")){
                sb.append((arr[i]+"").toUpperCase());
            }else{
                sb.append(arr[i]);
            }
        }
        return sb.toString();
    }

    private static void addClassExplain(StringBuilder sb,Table table) {
        sb.append("@Data").append("\n");
        sb.append("@Entity").append("\n");
        sb.append("@NoArgsConstructor").append("\n");
        sb.append("@AllArgsConstructor").append("\n");
        sb.append("@TableExt(x=1.1,y=1.2)").append("\n");
        sb.append("@Table(name = \"").append(table.getName()).append("\")").append("\n");
    }

    private static void addAuthorInfo(StringBuilder sb) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        sb.append("/**").append("\n");
        sb.append(" *").append("\n");
        sb.append(" * @auth ChengKai").append("\n");
        sb.append(" * @date ").append(sdf.format(new Date())).append("\n");
        sb.append(" */").append("\n");
    }

    private static void addImports(StringBuilder sb) {
        sb.append("import com.asiainfo.dacp.context.jpa.annotation.ColumnExt;").append("\n");
        sb.append("import com.asiainfo.dacp.context.jpa.annotation.TableExt;").append("\n");
        sb.append("import lombok.AllArgsConstructor;").append("\n");
        sb.append("import lombok.Data;").append("\n");
        sb.append("import lombok.NoArgsConstructor;").append("\n");
        sb.append("import javax.persistence.*;").append("\n");
        sb.append("import java.io.Serializable;").append("\n");
        sb.append("import java.util.Date;").append("\n");
    }

    public static List<String> batchGeneratePoStr(List<Table> tables) {
        return tables.stream().map(table->generatePoStr(table)).collect(Collectors.toList());
    }

    public static void batchGeneratePoFiles(String dir, List<Table> tables) throws IOException {
        File dirFile = new File(dir);
        for(Table table : tables){
            File file = new File(dirFile,toCamel(table.getName(),true)+".java");
            file.createNewFile();
            try(BufferedWriter bw = new BufferedWriter((new FileWriter(file)))){
                bw.write(generatePoStr(table));
            }catch(Exception e){
                throw new RuntimeException();
            }
        }
    }
}
