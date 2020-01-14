package client.main;

import client.service.Connector;
import client.service.Receiver;
import client.service.Sender;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GUI extends JFrame {



    private Toolkit toolkit = Toolkit.getDefaultToolkit();
    private Dimension screenSize = toolkit.getScreenSize();




    JTextField message = new JTextField();
    JButton connect = new JButton("发送");
    public static JTextArea receiveArea = new JTextArea();
    public static JLabel label = new JLabel();

    JScrollPane jsp = new JScrollPane();

    // 初始化窗口
    public void initFrame() {

        this.setTitle("Client");
        this.setLayout(null);
        // 设置窗口大小
        this.setSize(625, 500);
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

        this.add(label);
        this.add(message);
        this.add(connect);
        this.add(jsp);


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
    class Listener implements ActionListener, KeyListener {


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
        public void keyTyped(KeyEvent e) { }

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
        public void keyReleased(KeyEvent e) { }
    }
}