import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws Exception {


        FileTool ft = new FileTool();


        String filePath = FileTool.chooseTXTPath();
        ft.readTxtList(filePath);
        String folderPath = FileTool.chooseFolderPath("请选择ZIP文件所在的路径");
        ft.readFolderList(folderPath);
        String newFolderPath = FileTool.chooseFolderPath("请选择ZIP文件需要移动到的路径");
        ft.CreatNewFolder(folderPath,newFolderPath);
    }
}
