package client.service;

import client.main.Definer;
import client.main.GUI;
import utlis.TimeManager;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Connector implements Runnable{

    public static DataInputStream dataInputStream;
    public static DataOutputStream dataOutputStream;

    @Override
    public void run() {
        try {
            // 初始化客户端
            Socket socket = new Socket();

            GUI.receiveArea.append("[" + TimeManager.nowTime() + "][INFO][Client] 正在连接服务器" + Definer.ip + ":" + Definer.port + "\r\n");

            // 连接服务端
            socket.connect(new InetSocketAddress(Definer.ip, Definer.port), 5000);
            GUI.receiveArea.append("[" + TimeManager.nowTime() + "][INFO][Client] 服务器连接成功!\r\n");
            GUI.label.setText("当前连接服务器 " + Definer.ip + ":" + Definer.port);


            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());

        } catch (IOException e) {
            GUI.receiveArea.append("[" + TimeManager.nowTime() + "][WARN][Client] 服务器连接失败(/connect IP:Port重新连接服务器)\r\n");
            GUI.label.setText("/connect IP:Port");
            e.printStackTrace();
        }
    }
}
