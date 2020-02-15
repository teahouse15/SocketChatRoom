package client.main;

import client.service.Connector;
import client.service.Receiver;
import client.service.Sender;
import utlis.ClientUtils;
import utlis.SendFile;
import utlis.ServerUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GUI extends JFrame {



    private Toolkit toolkit = Toolkit.getDefaultToolkit();
    private Dimension screenSize = toolkit.getScreenSize();



    JMenuBar menuBar = new JMenuBar();

    JMenu menuOperation = new JMenu("操作");
    JMenu menuFunction = new JMenu("功能");
    JMenu menuHelp = new JMenu("帮助");

    JMenuItem operationConn = new JMenuItem("连接");
    JMenuItem operationDisConn = new JMenuItem("断开");

    JMenuItem functionFileTransmit = new JMenuItem("发送文件");

    JMenuItem helpGetHelp = new JMenuItem("获取帮助");
    JMenuItem helpAbout = new JMenuItem("关于");
    JMenuItem helpVersion = new JMenuItem("(Client) 版本: 1.2");




    public static JOptionPane jOptionPane = new JOptionPane();
    JTextField message = new JTextField();
    JButton connect = new JButton("发送");
    public static JTextArea receiveArea = new JTextArea();
    public static JLabel label = new JLabel();

    JScrollPane jsp = new JScrollPane();
    // 文件选择
    public static JFileChooser fileChooser = new JFileChooser();

    // 初始化窗口
    public void initFrame() {

        this.setTitle("Client");
        this.setLayout(null);
        // 设置窗口大小
        this.setSize(625, 515);
        this.setLocation(screenSize.width/2 - 625/2, screenSize.height/2 - 430/2);
        // 窗口居中


        // 元素设置
        message.setFont(new Font("黑体", Font.PLAIN, 20));
        message.setBounds(5, 5, 500, 50);
        message.addKeyListener(new Listener());

        connect.setBounds(510, 5, 90, 50);
        connect.addActionListener(new Listener());

        receiveArea.setBounds(5, 60, 595, 350);
        receiveArea.setFont(new Font("宋体", Font.PLAIN, 15));
        receiveArea.setEditable(false);
        receiveArea.setLineWrap(true);

        jsp.setBounds(5, 60, 595, 350);
        jsp.setViewportView(receiveArea);

        label.setFont(new Font("宋体", Font.PLAIN, 15));
        label.setBounds(20, 420, 300, 20);


        operationConn.addActionListener(new ItemListener());
        operationDisConn.addActionListener(new ItemListener());
        functionFileTransmit.addActionListener(new ItemListener());
        helpGetHelp.addActionListener(new ItemListener());
        helpAbout.addActionListener(new ItemListener());
        helpVersion.addActionListener(new ItemListener());




        menuOperation.add(operationConn);
        menuOperation.add(operationDisConn);

        menuFunction.add(functionFileTransmit);

        menuHelp.add(helpGetHelp);
        menuHelp.add(helpAbout);
        menuHelp.add(helpVersion);

        menuBar.add(menuOperation);
        menuBar.add(menuFunction);
        menuBar.add(menuHelp);





        this.add(label);
        this.add(message);
        this.add(connect);
        this.add(jsp);
        this.setJMenuBar(menuBar);


        String port = JOptionPane.showInputDialog("请输入服务器地址(127.0.0.1:9999): ");

        try {
            String[] res = port.split(":");
            int intPort = Integer.parseInt(res[1]);

            if ( !res[0].isEmpty() && !res[1].isEmpty()) {
                if (intPort >=1 && intPort <= 65535) {
                    Definer.port = intPort;
                }

                Definer.ip = res[0];
            }
        } catch (Exception e) {
        }


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }



    public boolean reconnect() {
        if (message.getText().indexOf("/connect ") != -1) {
            String[] address = message.getText().split(" ");

            String ip = address[1].split(":")[0];
            String port = address[1].split(":")[1];


            // 重新定义
            Definer.ip = ip;
            Definer.port = Integer.parseInt(port);

            // 关闭原有的连接
            Client.connector.stop();
            Client.receiver.stop();

            // 打开新的连接
            Client.connector = new Thread(new Connector());
            Client.connector.run();
            Client.receiver = new Thread(new Receiver());
            Client.receiver.start();


            message.setText("");
            
            return false;
        } else {
            return true;
        }
    }



    // 重写事件监听
    private class Listener implements ActionListener, KeyListener {


        @Override
        public void actionPerformed(ActionEvent e) {

            // 当url的内容不是重新连接的时候
            if (reconnect() && !message.getText().equals("")) {
                Sender sender = new Sender();
                sender.setMessage(message.getText());
                sender.sendMessage();

                message.setText("");
                sender.message = "";
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 10) {
                if (reconnect()) {
                    Sender.sendMessage(message.getText());
                    message.setText("");
                } else {
                    reconnect();
                }
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
                case "连接":
                    ClientUtils.connect();
                    break;
                case "断开":
                    ClientUtils.disconnect();
                    break;
                case "发送文件":
                    if (server.service.ReceiverPool.flag) {
                        System.out.println("服务器正在发送文件");
                    } else {
                        SendFile.selectFile("client");
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