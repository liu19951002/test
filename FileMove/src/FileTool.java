import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class FileTool {
    public List<String> TxtList;
    public List<String> FolderList;

    public FileTool() {
        TxtList = new ArrayList<>();
        FolderList = new ArrayList<>();
    }

    public FileTool(List<String> txtList, List<String> folderList) {
        TxtList = new ArrayList<>();
        FolderList = new ArrayList<>();
        this.TxtList=txtList;
        this.FolderList=folderList;
    }

    public static String chooseTXTPath() {
        JFileChooser fileChooser = new JFileChooser();
        //fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter(".txt", "txt"));
        fileChooser.setDialogTitle("请选择需要处理的文件");
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }
        return null;
    }

    public List<String> readTxtList(String filePath) throws Exception {
        FileReader fileReader = new FileReader(filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String str = null;
        while ((str = bufferedReader.readLine()) != null) {
            if (str.trim().length() > 2) {
                TxtList.add(str + ".ZIP");
                //System.out.println(str);
            }
        }
        return TxtList;
    }

    public static String chooseFolderPath(String flag) {
        int result = 0;
        String path = null;
        JFileChooser fileChooser = new JFileChooser();
        FileSystemView fsv = FileSystemView.getFileSystemView();  //注意了，这里重要的一句
        //System.out.println(fsv.getHomeDirectory());                //得到桌面路径
        fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
        fileChooser.setDialogTitle(flag);
        fileChooser.setApproveButtonText("确定");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        result = fileChooser.showOpenDialog(null);
        if (JFileChooser.APPROVE_OPTION == result) {
            path = fileChooser.getSelectedFile().getPath();
            return path;
        }
        return null;
    }

    public List<String> readFolderList(String folderpath) {
        File file = new File(folderpath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                //如果文件是普通文件，则将文件句柄放入集合中
                FolderList.add(f.getName());
            }
        }
        for(int i = 0;i<FolderList.size();i++){
            System.out.println(FolderList.get(i));
        }
        return FolderList;
    }

    public void CreatNewFolder(String oldFolderpath, String newFolderpath) throws IOException {
        try
        {
            System.out.println(newFolderpath);
            File file = new File(newFolderpath);

            if (!file.exists())
            {
                boolean isDirCreated = file.mkdir();
                System.out.println(isDirCreated);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        for(int i = 0;i<FolderList.size();i++){

            for(int j = 0;j<TxtList.size();j++){
                if (FolderList.get(i).equals(TxtList.get(j))){
                    Path pathIn = (Path) Paths.get(oldFolderpath, TxtList.get(j));
                    Path pathOut = (Path) Paths.get(newFolderpath, TxtList.get(j));
                    Files.copy(pathIn,pathOut, StandardCopyOption.REPLACE_EXISTING);
                }

            }
        }
    }


}
