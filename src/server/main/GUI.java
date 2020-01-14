package server.main;

import server.service.Networker;

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
    JButton send = new JButton("发送");
    public static JTextArea receiveArea = new JTextArea();
    JScrollPane jsp = new JScrollPane();

    // 初始化窗口
    public void initFrame() {

        this.setTitle("Server");
        this.setLayout(null);
        // 设置窗口大小
        this.setSize(625, 470);
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

        this.add(message);
        this.add(send);
        this.add(jsp);




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



    class Listener implements ActionListener, KeyListener {

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
}
