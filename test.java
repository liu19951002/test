import javax.swing.JFileChooser;

public class test{
    public static void main(String[] args) {
        System.out.println("Hello world");
        System.out.println(Math.min(0, 2));
        String s="da";
        JFileChooser jFile=new JFileChooser();
        jFile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        s.length();
    }
}