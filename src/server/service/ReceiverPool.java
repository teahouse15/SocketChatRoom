package server.service;

import server.main.GUI;
import utlis.TimeManager;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ReceiverPool implements Runnable {

    public DataInputStream dataInputStream;
    public Socket socket;

    public ReceiverPool(DataInputStream dataInputStream, Socket socket) {
        this.dataInputStream = dataInputStream;
        this.socket = socket;
    }



    @Override
    public void run() {

        while (true) {
            try {
                String message = dataInputStream.readUTF();
                if (!message.equals("")) {
                    GUI.receiveArea.append("[" + TimeManager.nowTime() + "][CHAT][Client] " + message + "\r\n");
                    GUI.receiveArea.selectAll();
                }

            } catch (IOException e) {
                // 当出现异常的时候，证明客户端出现了问题，可以直接把dataInputStream从list中删除
                StartSending.inputList.remove(dataInputStream);

                GUI.receiveArea.append("[" + TimeManager.nowTime()+ "][INFO][Client] " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + " is Disconnected\r\n");

                // break相当于关闭此线程
                break;
            }
        }
    }
}
