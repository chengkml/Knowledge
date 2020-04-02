package form;

import lombok.Getter;
import lombok.Setter;
import resolve.InfResolveExecutor2;
import resolve.TextAreaLogAppender;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Title: InfResolverForm
 * @Author: Chengkai
 * @Date: 2020/3/16 22:10
 * @Version: 1.0
 */
@Getter
@Setter
public class InfResolverForm {

    private JScrollPane logScrollPane;
    private JPanel mainPanel;
    private JTextArea logArea;
    private JButton selectPathBtn;
    private JButton clearLogBtn;
    private JPanel toolBarPane;
    private JTextField filePath;
    private JButton resolveBtn;
    private JProgressBar progressBar1;
    private ExecutorService threadPool = Executors.newSingleThreadExecutor();

    public InfResolverForm() {
        selectPathBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JFileChooser fileChooser = new JFileChooser();

                // 设置默认显示的文件夹为当前文件夹
                fileChooser.setCurrentDirectory(new File("."));

                // 设置文件选择的模式（只选文件、只选文件夹、文件和文件均可选）
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

                // 设置是否允许多选
                fileChooser.setMultiSelectionEnabled(false);

                // 设置默认使用的文件过滤器
                fileChooser.setFileFilter(new FileNameExtensionFilter("word(*.doc, *.docx)", "doc", "docx"));

                // 打开文件选择框（线程将被阻塞, 直到选择框被关闭）
                int result = fileChooser.showOpenDialog(mainPanel.getParent());

                if (result == JFileChooser.APPROVE_OPTION) {
                    // 如果点击了"确定", 则获取选择的文件路径
                    File file = fileChooser.getSelectedFile();

                    // 如果允许选择多个文件, 则通过下面方法获取选择的所有文件
                    // File[] files = fileChooser.getSelectedFiles();

                    filePath.setText(file.getAbsolutePath());
                }
            }
        });
        clearLogBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                logArea.setText("");
            }
        });
        resolveBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                threadPool.execute(()->{
                    resolveBtn.setEnabled(false);
                    InfResolveExecutor2.resolveWithLog(filePath.getText(),new TextAreaLogAppender(logArea),progressBar1,resolveBtn);
                });
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("接口文档解析");
        JPanel mainPanel = new InfResolverForm().mainPanel;
        frame.setContentPane(mainPanel);
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.pack();
        frame.setVisible(true);
    }
}
