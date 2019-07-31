package doc;

import base.PoiDocReader;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class DocxReaderTest {

    @Test
    public void getAllTab() throws IOException {
        DocxReader reader = new DocxReader("C:\\Users\\m1816\\Desktop\\test.docx");
        System.out.println(reader.getAllTab().get(0)[1][1]);
    }

    @Test
    public void filterTabByHeader() throws IOException {
        DocxReader reader = new DocxReader("C:\\Users\\m1816\\Desktop\\test.docx");
        assertEquals(reader.getAllTab().size(),2);
        assertEquals(1,reader.filterTabByHeader(new String[]{"分类","内容"}).size());
        assertEquals(1,reader.filterTabByHeader(new String[]{"实体表"}).size());
    }

    @Test
    public void filterDataByHeader() throws IOException {
        DocxReader reader = new DocxReader("C:\\Users\\m1816\\Desktop\\test.docx");
        assertEquals(5,reader.filterDataByHeader(new String[]{"分类","内容"}).get(0).length);
        assertEquals(3,reader.filterDataByHeader(new String[]{"实体表"}).get(0).length);
    }

    @Test
    public void filterOneCellTab() throws IOException {
        DocxReader reader = new DocxReader("C:\\Users\\m1816\\Desktop\\test.docx");
        assertEquals(1,reader.filterOneCellTab().size());
    }
}