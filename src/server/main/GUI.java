package server.main;

import server.service.Networker;
import utlis.SendFile;
import utlis.ServerUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JFrame {

    private Toolkit toolkit = Toolkit.getDefaultToolkit();
    private Dimension screenSize = toolkit.getScreenSize();

    JMenuBar menuBar =new JMenuBar();
    JMenu menuMode = new JMenu("模式(M)");
    JMenu menuFunction = new JMenu("功能(F)");
    JMenu menuHelp = new JMenu("帮助(H)");

    // menuMode
    JMenuItem modeSolo = new JMenuItem("私聊");
    JMenuItem modeGroupChat = new JMenuItem("群聊");

    // menuFunction
    JMenuItem functionFileTransmit = new JMenuItem("发送文件");

    // menuHelp
    JMenuItem helpGetHelp = new JMenuItem("获取帮助");
    JMenuItem helpAbout = new JMenuItem("关于");
    JMenuItem helpVersion = new JMenuItem("(Server) 版本: 1.2");




    // 对话窗口
    JTextField message = new JTextField();
    // 发送按钮
    JButton send = new JButton("发送");
    public static JTextArea receiveArea = new JTextArea();
    JScrollPane jsp = new JScrollPane();

    // 文件选择
    public static JFileChooser fileChooser = new JFileChooser();
    public static JOptionPane jOptionPane = new JOptionPane();


    // 初始化窗口
    public void initFrame() {

        this.setTitle("Server");
        this.setLayout(null);
        // 设置窗口大小
        this.setSize(625, 490);
        this.setLocation(screenSize.width/2 - 625/2, screenSize.height/2 - 430/2);
        // 窗口居中


        // 元素设置
        message.setFont(new Font("黑体", Font.PLAIN, 25));
        message.setBounds(5, 5, 500, 50);

        send.setBounds(510, 5, 90, 50);

        receiveArea.setBounds(5, 60, 595, 350);
        receiveArea.setFont(new Font("宋体", Font.PLAIN, 15));
        receiveArea.setEditable(false);
        receiveArea.setLineWrap(true);

        jsp.setBounds(5, 60, 595, 350);
        jsp.setViewportView(receiveArea);

        modeSolo.addActionListener(new ItemListener());
        modeGroupChat.addActionListener(new ItemListener());
        functionFileTransmit.addActionListener(new ItemListener());
        helpGetHelp.addActionListener(new ItemListener());
        helpAbout.addActionListener(new ItemListener());
        helpVersion.addActionListener(new ItemListener());

        menuMode.add(modeSolo);
        menuMode.add(modeGroupChat);

        menuFunction.add(functionFileTransmit);

        menuHelp.add(helpGetHelp);
        menuHelp.add(helpAbout);
        menuHelp.add(helpVersion);

        menuBar.add(menuMode);
        menuBar.add(menuFunction);
        menuBar.add(menuHelp);


        this.add(message);
        this.add(send);
        this.add(jsp);
        this.setJMenuBar(menuBar);




        String port = JOptionPane.showInputDialog("请输入端口号(默认为9999)");
        try {
            int intPort = Integer.parseInt(port);
            if (!port.equals("") && intPort >= 1 && intPort <= 65535) {
                Networker.port = Integer.parseInt(port);
            } else {
                System.out.println("端口不合格，将使用默认端口");
            }
        } catch (NumberFormatException e) {
            System.out.println("端口不合格，将使用默认端口");
        }
        send.addActionListener(new Listener());
        message.addKeyListener(new Listener());


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }



    private class Listener implements ActionListener, KeyListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Networker.startSending.printer(message.getText());
            message.setText("");
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 10) {
                Networker.startSending.printer(message.getText());
                message.setText("");
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }


    private class ItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem menuItem = (JMenuItem) e.getSource();
            switch (menuItem.getText()) {
                case "私聊":
                    ServerUtils.solo();
                    break;
                case "群聊":
                    ServerUtils.groupChat();
                    break;
                case "发送文件":
                    if (client.service.Receiver.flag) {
                        System.out.println("客户端正在发送文件");
                    } else {
                        SendFile.selectFile("server");
                    }
                    break;
                case "获取帮助":
                    ServerUtils.getHelp();
                    break;
                case "关于":
                    ServerUtils.about();
                    break;
            }
        }
    }
}
