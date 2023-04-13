import tet.sdad;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Map;


public class Main {
    public static void main(String[] args) throws Exception {
        sdad da=new sdad();
        String xlsxfolder = Utils.chooseFolderPath("选择excel所在的文件夹的路径");
        String sfolder = Utils.chooseFolderPath("选择shp生成的路径");
        da.createPolygon(sfolder,"Route.shp",xlsxfolder);
    }
}
