package server.service;

import server.main.GUI;
import utlis.TimeManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Networker implements Runnable{
    public static List<String> address = new ArrayList<>();


    public static int port = 9999;

    public static StartSending startSending;

    @Override
    public void run() {

        try {
            ServerSocket server = new ServerSocket(port);
            GUI.receiveArea.append("[" + TimeManager.nowTime() + "][INFO][Server] 服务器已启动  地址: 127.0.0.1:" + port + "\r\n");
            startSending = new StartSending();
            startSending.start();
            while (true) {
                Socket socket = server.accept();
                GUI.receiveArea.append("[" + TimeManager.nowTime() + "][INFO][Client] " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + " is Connected\r\n");
                address.add(socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
                startSending.addClient(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
