package client.service;

import client.main.GUI;
import server.service.Networker;
import utlis.ReceiveFile;
import utlis.SendFile;
import utlis.TimeManager;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class Receiver implements Runnable{

    // 是否正在发送文件
    public static boolean flag;


    @Override
    public void run() {
        while (true) {
            try {
                  String message = Connector.dataInputStream.readUTF();

                if (message.equals("*!!!FILE!!!*")) {
                    String filename = Connector.dataInputStream.readUTF();
                    Long length = Connector.dataInputStream.readLong();
                    int i = GUI.jOptionPane.showConfirmDialog(null,"服务器向您发送了文件 " + filename + " ，长度为 " + length + "\r\n是否接收?","文件传输",GUI.jOptionPane.YES_NO_OPTION);
                    // 客户端同意接收
                    if (i == 0) {
                        Connector.dataOutputStream.writeUTF("*!!!YES!!!*");
                        ReceiveFile.receiveFile(Connector.dataInputStream, new File(filename), length);
                    }
                } else if(message.equals("*!!!YES!!!*")) {
                    System.out.println("服务器已同意接收文件，可以发送文件了");
                    flag = true;
                    SendFile.sendFile(Connector.dataOutputStream, SendFile.file);
                    flag = false;
                } else {
                    GUI.receiveArea.append("[" + TimeManager.nowTime() + "][CHAT][Server] " + message + "\r\n");
                    GUI.receiveArea.selectAll();
                    if (GUI.receiveArea.getSelectedText() != null) {
                        GUI.receiveArea.setCaretPosition(GUI.receiveArea.getSelectedText().length());
                        GUI.receiveArea.requestFocus();
                    }
                }
            } catch (IOException e) {
                GUI.receiveArea.append("[" + TimeManager.nowTime() + "][WARN][Server] 服务器已关闭");
                e.printStackTrace();
                break;
            }
        }
    }
}
