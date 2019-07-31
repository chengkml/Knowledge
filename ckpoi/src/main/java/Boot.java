import org.apache.log4j.Logger;

import java.io.IOException;

public class Boot {

    public static Logger logger = Logger.getLogger(Boot.class);

    private static final String[] PROP_HEADERS = new String[]{"属性编码", "属性名称", "属性描述", "属性类型", "备注"};

    public static void main(String[] args) throws IOException {
        /**String fileName = "中国移动一级经营分析系统与一级业务系统数据接 口规范(内容计费)1.9.0";
         DocReader reader = new DocReader("C:\\Users\\m1816\\Desktop\\一级系统接口文档0711\\"+fileName+".doc");
         XlsWriter writer = createInfXls();
         TableIterator it = reader.getTabIterator();
         int infCount = 0;
         int propRowCount = 1;
         while(true){
         Table detailTab = reader.filterOneCellTab(it);
         if(detailTab==null){
         logger.warn("未找到更多接口详情表格，结束该主题！");
         break;
         }
         infCount++;
         //记录接口详情单个表格
         TableCell cell =detailTab.getRow(0).getCell(0);
         int detailCellNum = cell.numParagraphs();
         String[][] oneInf = new String[1][8];
         if(cell.getParagraph(0).text().contains("接口单元名称")){
         oneInf[0][2] = cell.getParagraph(0).text().replaceAll("接口单元名称：","");
         }else if(cell.getParagraph(0).text().contains("元名称")){
         oneInf[0][2] = cell.getParagraph(0).text().substring(cell.getParagraph(0).text().indexOf("元名称"),cell.getParagraph(0).text().length());
         }
         String infCode = cell.getParagraph(1).text().replaceAll("接口单元编码：","");
         oneInf[0][1] = infCode;
         oneInf[0][0] = positionInfClass(reader, "接口单元编码："+infCode);
         StringBuilder sb = new StringBuilder();
         for(int i = 2;i<detailCellNum;i++){
         sb.append(cell.getParagraph(i).text());
         }
         oneInf[0][3] = sb.toString().replaceAll("接口单元说明：","");
         //从接口详情表格开始往下找抽取周期、方式等信息
         Iterator<Paragraph> tempIt = reader.getAllParagraphs().iterator();
         while(tempIt.hasNext()){
         Paragraph p = tempIt.next();
         //先找到“接口单元名称：....”
         if(cell.getParagraph(0).text().contains("元名称：")&&p.text().contains(cell.getParagraph(0).text())){
         //再往下找“接口单元文件名称”
         while(tempIt.hasNext()){
         Paragraph par = tempIt.next();
         if(StringUtils.isNotBlank(par.text())&&(par.text().contains("接口单元文件名称")||par.text().contains("下发方式及周期"))){
         if(par.text().contains("接口单元文件名称")){
         //判断抽取方式是增量还是全量
         Paragraph ssp = reader.nextValidParagraph(tempIt);
         if(StringUtils.isNotBlank(ssp.text())){
         oneInf[0][4] = ssp.text().contains("增量")||ssp.text().contains("新增")?"增量":(ssp.text().contains("全量")?"全量":"");
         }else{
         oneInf[0][4] = "";
         }
         //判断抽取周期
         if(StringUtils.isNotBlank(ssp.text())){
         oneInf[0][5] = ssp.text().contains("日")?"日":(ssp.text().contains("月")?"月":(ssp.text().contains("周")?"周":""));
         }else{
         oneInf[0][5] = "";
         }
         //获取接口数据文件名
         reader.nextValidParagraph(tempIt);
         ssp = reader.nextValidParagraph(tempIt);
         if(StringUtils.isNotBlank(ssp.text())){
         CharacterRun run = ssp.getCharacterRun(0);
         oneInf[0][6] = ssp.text();
         }else{
         oneInf[0][6] = "";
         }
         //获取接口校验文件名
         reader.nextValidParagraph(tempIt);
         ssp = reader.nextValidParagraph(tempIt);
         if(StringUtils.isNotBlank(ssp.text())){
         CharacterRun run = ssp.getCharacterRun(0);
         oneInf[0][7] = ssp.text();
         }else{
         oneInf[0][7] = "";
         }
         break;
         }else{
         //判断抽取方式是增量还是全量
         Paragraph ssp = reader.nextValidParagraph(tempIt);
         if(StringUtils.isNotBlank(ssp.text())){
         oneInf[0][4] = ssp.text().contains("增量")||ssp.text().contains("新增")?"增量":(ssp.text().contains("全量")?"全量":"");
         }else{
         oneInf[0][4] = "";
         }
         //判断抽取周期
         if(StringUtils.isNotBlank(ssp.text())){
         oneInf[0][5] = ssp.text().contains("日")?"日":(ssp.text().contains("月")?"月":(ssp.text().contains("周")?"周":""));
         }else{
         oneInf[0][5] = "";
         }
         //获取接口数据文件名
         reader.nextValidParagraph(tempIt);
         ssp = reader.nextValidParagraph(tempIt, "数据文件：");
         if(StringUtils.isNotBlank(ssp.text())){
         CharacterRun run = ssp.getCharacterRun(0);
         oneInf[0][6] = ssp.text().replaceAll("数据文件：", "");
         }else{
         oneInf[0][6] = "";
         }
         break;
         }
         }
         }
         break;
         }
         }
         writer.addSheetData("接口基础信息",oneInf,infCount,0);
         Table propTab = reader.filterNextTabByHeaders(it,PROP_HEADERS);
         int propNum = propTab.numRows()-1;
         String[][] propData = new String[propNum][];
         if(propNum<1){
         logger.info("存在只有表头信息的接口属性表格");
         continue;
         }
         reader.readRows(propTab,propData,1);
         for(int k = 0;k<propNum;k++){
         if(propData[k].length>0){
         String[] temp = new String[propData[k].length+2];
         System.arraycopy(propData[k],0,temp,2,propData[k].length-1);
         temp[0] = oneInf[0][0];
         temp[1] = infCode;
         propData[k] = temp;
         }
         }
         writer.addSheetData("接口属性信息",propData,propRowCount,0);
         propRowCount+=propData.length;
         }
         logger.info("接口编码数："+infCount);
         writer.saveXls("C:\\Users\\m1816\\Desktop\\一级系统接口文档0711\\"+fileName+".xls");
         }

         private static XlsWriter createInfXls() {
         XlsWriter writer = new XlsWriter();
         writer.createXls();
         //创建表单
         writer.createSheet("接口基础信息");
         writer.createSheet("接口属性信息");
         //添加标题
         writer.addSheetData("接口基础信息",new String[][]{new String[]{"接口分类","接口单元","接口单元名称","接口单元说明","抽取方式（增量/全量）","周期（日/月/周）","接口数据文件名","接口校验文件名"}},0);
         writer.addSheetData("接口属性信息",new String[][]{new String[]{"接口分类","接口编码","属性编码","属性名称","属性描述","属性类型","备注"}},0);
         return writer;
         }

         private static String positionInfClass(DocReader reader, String s) {
         List<Paragraph> paras = reader.getAllParagraphs();
         List<Paragraph> temp = new ArrayList<>();
         for(Paragraph p : paras){
         if(p.getLvl()==1||p.text().contains(s)){
         temp.add(p);
         }
         }
         int index = 0;
         for(Paragraph p : temp){
         if(p.text().contains(s)){
         break;
         }
         index++;
         }
         return temp.get(index-1).text();*/
    }

}
