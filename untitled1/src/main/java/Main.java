import java.util.List;
import java.util.Map;


public class Main {
    public static void main(String[] args) throws Exception {
        String FilePath =Utils.chooseExcelPath();
        //初始化对象,并给Excel中的sheet,titleRow,mapList初始值
        Excel ex = new Excel(FilePath);
        //读取每一行数据,去除含中文的且速度为的,写入list
        List<Map<String, String>> mapList=ex.ReadData();

        System.out.println(mapList.size());
        //以time为关键字重新筛选同一天的数据,写入一个新的键值对Map
        ex.FilterData();

        //用户选择保存文件夹路径,并按照每一天的日期生成excel
        String folderPath=Utils.chooseFolderPath()+"/";
        ex.CreateExcel(folderPath);
    }
}
