import java.awt.Desktop;
import java.net.URI;

public class HelloWorld {
    public static void main(String[] args) throws Exception {
        // 打开 index.html
        Desktop.getDesktop().browse(new URI("file:///C:/Users/35719/Desktop/1/index.html"));
        System.out.println("已打开 index.html");
    }
}