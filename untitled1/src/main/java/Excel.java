import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.commons.io.filefilter.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.PortUnreachableException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Excel {
    public String FilePath;
    public XSSFSheet sheet;
    public XSSFRow titleRow;
    public List<Map<String, String>> mapList;
    public Map<String, List<Map<String, String>>> FilteredData;

    public Excel() {
    }
    public Excel(String filePath) throws Exception {
        this.FilePath = filePath;
        XSSFWorkbook xssfWorkbook=new XSSFWorkbook(new FileInputStream(FilePath));
        this.sheet = xssfWorkbook.getSheetAt(0);
        this.titleRow=sheet.getRow(0);
        this.mapList=new ArrayList<>();
    }

    public List<Map<String, String>> ReadData(){
        //List<Map<String, String>> mapList=new ArrayList<>();
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat formatter2=new SimpleDateFormat("yyyy/MM/dd");
        for(int i=1;i<this.sheet.getPhysicalNumberOfRows();i++){
            XSSFRow row=this.sheet.getRow(i);
            Map<String,String> map=new HashMap<>();
            String myTime="time";
            String myValue="";
            for (int index=0;index<row.getPhysicalNumberOfCells();index++ ){
                XSSFCell cell=row.getCell(index);
                XSSFCell titleCell=this.titleRow.getCell(index);
                cell.setCellType(CellType.STRING);
                if(cell.getStringCellValue().equals("")){
                    continue;
                }
                String titleName=titleCell.getStringCellValue();
                String value= "";

                if(titleName.equals("时间")){
                    try {
                        Date date= HSSFDateUtil.getJavaDate(Double.valueOf(cell.getStringCellValue()) );
                        myValue= formatter2.format(date);
                        value= formatter.format(date);
                    }
                    catch (Exception e){
                        myValue= "";
                        value= "";
                    }
                }else {
                    Double value2 = 0.0;
                    try {
                        value2 = Double.parseDouble(cell.getStringCellValue());
                        if(Utils.isInt(value2)){
                            int a=(int)Math.round(value2);
                            value=Integer.toString(a);
                        }
                        else if(value2==0.0){
                            value="0";
                        }
                        else {
                            value=value2.toString();
                        }
                    }
                    catch (Exception e){
                        value="0";
                    }

                }
                map.put(titleName,value);
            }
            map.put(myTime,myValue);
            if(map.get("速度").equals("0")|| Utils.isContainsChinese(map.get("速度").toString())){

            }else {
                this.mapList.add(map);
            }

        }

        return this.mapList;
    }

    public Map<String, List<Map<String, String>>> FilterData(){
        this.FilteredData = this.mapList.stream()
                .collect(Collectors.groupingBy(map2 -> map2.get("time")));

        return this.FilteredData;

    }

    public void CreateExcel(String filterPath){
        Object[] newTitle={"时间","方向","速度","经度","车牌编号","维度"};
        for(Map.Entry<String,List<Map<String, String>>> entry :this.FilteredData.entrySet()){
            String key = entry.getKey();
            String excelPath=key.trim();
            excelPath = excelPath.replace("/", "-");
            excelPath=filterPath+excelPath+".xlsx";
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet2 = workbook.createSheet("sheet1");
            int rowCount = 0;
            Row row = sheet2.createRow(rowCount++);
            int columnCount = 0;
            //写入第一行的数据：行头名：经度维度这些
            for (int i=0;i<newTitle.length;i++) {
                Cell cell = row.createCell(columnCount++);
                cell.setCellValue(newTitle[i].toString());
            }

            List<Map<String, String>> value=entry.getValue();
            for (Map<String, String> subMap:value){
                Row row2 = sheet2.createRow(rowCount++);
                columnCount=0;
                for (Map.Entry<String, String> subEntry : subMap.entrySet()){
                    String subKey = subEntry.getKey();
                    String subValue = subEntry.getValue();
                    if(subKey !="time"){
                        Cell cell = row2.createCell(columnCount++);
                        switch (subKey) {
                            case "车牌编号":
                                cell.setCellValue(subValue);
                            case "速度":
                                cell.setCellValue(subValue);
                            case "方向":
                                cell.setCellValue(subValue);
                            case "经度":
                                cell.setCellValue(subValue);
                            case "维度":
                                cell.setCellValue(subValue);
                            case "时间":
                                cell.setCellValue(subValue);
                        }
                    }

                }
            }
            try (FileOutputStream outputStream = new FileOutputStream(excelPath)) {
                workbook.write(outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
