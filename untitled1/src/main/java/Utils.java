import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    static String regEx = "[\u4e00-\u9fa5]";
    static Pattern pat = Pattern.compile(regEx);
    public static boolean isContainsChinese(String str)
    {
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find())  {
            flg = true;
        }
        return flg;
    }
    public static boolean isInt(double a)
    {
        double b = a;
        int b1 = (int)a;
        if(b % b1 == 0)
            return true;
        else
            return false;
    }
    public static String chooseExcelPath(){
        JFileChooser fileChooser = new JFileChooser();
        //fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter(".xlsx", "xlsx"));
        fileChooser.setDialogTitle("请选择需要处理的文件");
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }return null;
    }

    public static String chooseFolderPath(){
        int result = 0;
        String path = null;
        JFileChooser fileChooser = new JFileChooser();
        FileSystemView fsv = FileSystemView.getFileSystemView();  //注意了，这里重要的一句
        //System.out.println(fsv.getHomeDirectory());                //得到桌面路径
        fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
        fileChooser.setDialogTitle("请选择结果的存放路径");
        fileChooser.setApproveButtonText("确定");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        result = fileChooser.showOpenDialog(null);
        if (JFileChooser.APPROVE_OPTION == result) {
            path=fileChooser.getSelectedFile().getPath();
            return path;
        }return null;
    }

}
