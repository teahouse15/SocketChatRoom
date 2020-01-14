package client.service;

import client.main.GUI;
import utlis.TimeManager;

import java.io.IOException;

public class Sender implements Runnable {

    public String message;


    public void setMessage(String message) {
        this.message = message;
    }


    public void sendMessage() {
        try {
            Connector.dataOutputStream.writeUTF(message);
            if (!message.equals("")) {
                GUI.receiveArea.append("[" + TimeManager.nowTime() + "][CHAT][Client] " + message + "\r\n");
            }
        } catch (IOException e) {
            System.out.println("发送失败");
            e.printStackTrace();
        }
    }



    @Override
    public void run() {}

    public static void sendMessage(String message) {
        try {
            Connector.dataOutputStream.writeUTF(message);
            if (!message.equals("")) {
                GUI.receiveArea.append("[" + TimeManager.nowTime() + "][CHAT][Client] " + message + "\r\n");
                GUI.receiveArea.selectAll();
            }
        } catch (IOException e) {
            System.out.println("发送失败");
            e.printStackTrace();
        }
    }
}
