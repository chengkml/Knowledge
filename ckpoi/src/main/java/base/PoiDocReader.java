package base;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public abstract class PoiDocReader extends CommonLogger {

    public PoiDocReader(String path) throws IOException {
        loadDoc(path);
    }

    protected XWPFDocument docx;

    protected HWPFDocument doc;

    public static boolean docx(String path) {
        return path.toLowerCase().endsWith("docx");
    }

    public static boolean doc(String path) {
        return path.toLowerCase().endsWith("doc");
    }

    private void loadDoc(String path) throws IOException {
        FileInputStream in = new FileInputStream(path);
        if (docx(path)) {
            docx = new XWPFDocument(in);
        } else if(doc(path)){
            POIFSFileSystem pfs = new POIFSFileSystem(in);
            doc = new HWPFDocument(pfs);
        }
    }

    /**
     * 解析文档中所有表格
     *
     * @return 二维数组集合
     */
    public abstract List<String[][]> getAllTab();

    /**
     * 按指定表头过滤表格
     *
     * @param headers 表格标题
     * @return 过滤结果
     */
    public abstract List<String[][]> filterTabByHeader(String[] headers);

    /**
     * 按指定表头过滤表格数据
     *
     * @param headers
     * @return
     */
    public abstract List<String[][]> filterDataByHeader(String[] headers);

    /**
     * 过滤单个单元格的表格
     *
     * @return
     */
    public abstract List<String[][]> filterOneCellTab();

    /**
     * 获取所有段落中的文本
     * @return
     */
    public abstract List<String> getAllParaText();
}
