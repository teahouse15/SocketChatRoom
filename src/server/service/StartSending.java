package server.service;

import server.main.GUI;
import utlis.TimeManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class StartSending extends Thread{

    public static List<DataInputStream> inputList = new ArrayList<>();
    public static List<DataOutputStream> outputList = new ArrayList<>();

    public ReceiverPool receiverPool;




    public void addClient(Socket socket) {
        try {
            // 改一下
//            inputList.add(new DataInputStream(socket.getInputStream()));
            receiverPool = new ReceiverPool(new DataInputStream(socket.getInputStream()), socket);
            inputList.add(receiverPool.dataInputStream);

            new Thread(receiverPool).start();
            outputList.add(new DataOutputStream(socket.getOutputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void run() {
        System.out.println("StartSending start working------");
    }


    // 发送信息 和 断开连接合并
    public void printer(String message) {
        if (!message.equals("")) {
            GUI.receiveArea.append("[" + TimeManager.nowTime() + "][CHAT][Server] " + message + "\r\n");
            List<DataOutputStream> closedStreamList = new ArrayList<>();
            for (DataOutputStream o : outputList) {
                try {
                    o.writeUTF(message);
                } catch (IOException e) {
                    closedStreamList.add(o);
                }
            }
            outputList.removeAll(closedStreamList);
        } else {
//            GUI.receiveArea.append("[Notice][Server] 空内容无法发送\r\n");
        }
    }
}
