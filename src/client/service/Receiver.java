package client.service;

import client.main.GUI;
import utlis.TimeManager;

import java.io.IOException;

public class Receiver implements Runnable{

    @Override
    public void run() {
        while (true) {
            try {
                String message = Connector.dataInputStream.readUTF();
                GUI.receiveArea.append("[" + TimeManager.nowTime() + "][CHAT][Server] " + message + "\r\n");
                GUI.receiveArea.selectAll();
            } catch (IOException e) {
                GUI.receiveArea.append("[" + TimeManager.nowTime() + "][WARN][Server] 服务器已关闭");
                break;
            }
        }
    }
}
