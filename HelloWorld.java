import java.awt.Desktop;
import java.io.File;

public class HelloWorld {
    public static void main(String[] args) throws Exception {
        // 打开当前目录下的 index.html（相对路径，避免硬编码）
        File html = new File("index.html");
        if (!html.exists()) {
            System.out.println("未找到 index.html，请先切换到项目目录运行。");
            return;
        }
        Desktop.getDesktop().browse(html.toURI());
        System.out.println("已打开 index.html");
    }
}
