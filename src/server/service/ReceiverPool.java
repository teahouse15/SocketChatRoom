package server.service;

import client.service.Connector;
import server.main.GUI;
import utlis.ReceiveFile;
import utlis.SendFile;
import utlis.TimeManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class ReceiverPool implements Runnable {

    public DataInputStream dataInputStream;
    public DataOutputStream dataOutputStream;
    public Socket socket;

    // 是否正在发送文件
    public static boolean flag;

    public ReceiverPool(DataOutputStream dataOutputStream, DataInputStream dataInputStream, Socket socket) {
        this.dataOutputStream = dataOutputStream;
        this.dataInputStream = dataInputStream;
        this.socket = socket;
    }



    @Override
    public void run() {

        while (true) {
            try {
                String message = dataInputStream.readUTF();
                if (message.equals("*!!!YES!!!*")) {
                    System.out.println("客户端已同意接收文件，可以发送文件了");
                    for (DataOutputStream dos : Networker.startSending.outputList) {
                        flag = true;
                        SendFile.sendFile(dos, SendFile.file);
                    }
                    flag = false;
                    message = dataInputStream.readUTF();
                }
                if (message.equals("*!!!FILE!!!*")) {
                    String filename = dataInputStream.readUTF();
                    Long length = dataInputStream.readLong();
                    int i = server.main.GUI.jOptionPane.showConfirmDialog(null,"客户端向您发送了文件 " + filename + " ，长度为 " + length + "\r\n是否接收?","文件传输", client.main.GUI.jOptionPane.YES_NO_OPTION);
                    // 服务端同意接收
                    if (i == 0) {
                        System.out.println(filename + ":" +length);
                        dataOutputStream.writeUTF("*!!!YES!!!*");
                        ReceiveFile.receiveFile(dataInputStream, new File(filename), length);
                    }
                    message = dataInputStream.readUTF();
                }
                if (!message.equals("")) {
                    GUI.receiveArea.append("[" + TimeManager.nowTime() + "][CHAT][Client] " + message + "\r\n");
                    GUI.receiveArea.selectAll();
                }

            } catch (IOException e) {
                // 当出现异常的时候，证明客户端出现了问题，可以直接把dataInputStream从list中删除
                StartSending.inputList.remove(dataInputStream);

                GUI.receiveArea.append("[" + TimeManager.nowTime()+ "][INFO][Client] " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + " is Disconnected\r\n");
                Networker.address.remove(socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
                // break相当于关闭此线程
                break;
            }
        }
    }
}
