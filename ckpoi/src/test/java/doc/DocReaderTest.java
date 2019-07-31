package doc;

import base.CommonLogger;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class DocReaderTest {

    private static final String[] INF_HEADERS1 = new String[]{"接口单元编码","接口单元名称","接口单元说明"};

    private static final String[] INF_HEADERS2 = new String[]{"接口单元编码","接口模型实体","实体说明"};
    @Test
    public void testgetTabIterator() throws IOException {
        DocReader reader = new DocReader("C:\\Users\\m1816\\Desktop\\inf.doc");
        assertNotNull(reader.getTabIterator());
    }

    @Test
    public void testGetAllTabDatas() throws IOException {
        DocReader reader = new DocReader("C:\\Users\\m1816\\Desktop\\inf.doc");
        CommonLogger.logger.info(reader.getAllTab().get(0)[0][0]);
    }

    @Test
    public void testFilterTabByHeaders() throws IOException {
        DocReader reader = new DocReader("C:\\Users\\m1816\\Desktop\\11.doc");
        CommonLogger.logger.info(reader.filterTabByHeader(new String[]{"接口单元编码","接口单元名称"}));
    }

    @Test
    public void testFilterDataByHeaders() throws IOException {
        DocReader reader = new DocReader("C:\\Users\\m1816\\Desktop\\22.doc");
        CommonLogger.logger.info(reader.filterDataByHeader(new String[]{"属性编码","属性名称"}));
    }

    @Test
    public void testFilterOneCellTab() throws IOException {
        DocReader reader = new DocReader("C:\\Users\\m1816\\Desktop\\inf.doc");
        CommonLogger.logger.info(reader.filterOneCellTab());
    }

    @Test
    public void testGetAllParagraphs() throws IOException {
        DocReader reader = new DocReader("C:\\Users\\m1816\\Desktop\\11.doc");
        List<Paragraph> pars = reader.getAllParagraphs();
        for(Paragraph par : pars){
            CharacterRun run = par.getCharacterRun(0);
            CommonLogger.logger.info(par.text());
            CommonLogger.logger.info(par.getLvl());
            CommonLogger.logger.info(run.getFontSize());
        }
    }

    @Test
    public void testGetAllParagraphs2() throws IOException {
        DocReader reader = new DocReader("C:\\Users\\m1816\\Desktop\\inf.doc");
        List<Paragraph> pars = reader.getAllParagraphs();
        Iterator<Paragraph> it = pars.iterator();
        List<String[]> attachProps = new ArrayList<>();
        int count = 0;
        while(it.hasNext()){
            Paragraph par = it.next();
            if(StringUtils.isNotBlank(par.text())&&par.text().contains("接口单元文件名称")&&par.text().length()<10){
                count++;
                String[] temp = new String[4];
                Paragraph p = it.next();
                if(StringUtils.isNotBlank(p.text())){
                    temp[0] = p.text().contains("增量")||p.text().contains("新增")?"增量":(p.text().contains("全量")?"全量":"");
                }else{
                    temp[0] = "";
                }
                if(StringUtils.isNotBlank(p.text())){
                    temp[1] = p.text().contains("日")?"日":(p.text().contains("月")?"月":(p.text().contains("周")?"周":""));
                }else{
                    temp[1] = "";
                }
                it.next();
                p = it.next();
                if(StringUtils.isNotBlank(p.text())){
                    CharacterRun run = p.getCharacterRun(0);
                    if(run.getFontSize()==21){
                        temp[2] = p.text();
                    }
                }else{
                    temp[2] = "";
                }
                it.next();
                p = it.next();
                if(StringUtils.isNotBlank(p.text())){
                    CharacterRun run = p.getCharacterRun(0);
                    if(run.getFontSize()==21){
                        temp[3] = p.text();
                    }
                }else{
                    temp[3] = "";
                }
                if(StringUtils.isNotBlank(temp[0])){
                    attachProps.add(temp);
                }
            }
        }
        System.out.println(count);
    }

    @Test
    public void testFilterParagraphsByLvl() throws IOException {
        DocReader reader = new DocReader("C:\\Users\\m1816\\Desktop\\inf.doc");
        List<Paragraph> pars = reader.filterParagraphByLvl(0);
        for(Paragraph par : pars){
            CommonLogger.logger.info(par.text());
        }
        CommonLogger.logger.info(pars.size());
    }

    @Test
    public void testFilterParagraphsByFontSize() throws IOException {
        DocReader reader = new DocReader("C:\\Users\\m1816\\Desktop\\inf.doc");
        List<Paragraph> pars = reader.filterParagraphByFontSize(32);
        for(Paragraph par : pars){
            CharacterRun run = par.getCharacterRun(0);
            CommonLogger.logger.info(par.text());
        }
    }

}
