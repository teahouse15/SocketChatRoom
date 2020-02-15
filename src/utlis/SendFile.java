package utlis;

import client.service.Connector;
import server.service.Networker;


import java.io.*;

public class SendFile extends Thread {

    public static FileInputStream fileInputStream;      // 文件读取流

    public static File file;

    public static void selectFile(String who) {
        if (who.equals("server")) {
            server.main.GUI.fileChooser.showOpenDialog(null);
            file = server.main.GUI.fileChooser.getSelectedFile();

            for (DataOutputStream o : Networker.startSending.outputList) {
                try {
                    o.writeUTF("*!!!FILE!!!*");
                    o.writeUTF(file.getName());
                    o.writeLong(file.length());
                } catch (IOException e) {
                }
            }
        } else {
            // 客户端发送文件界面
            client.main.GUI.fileChooser.showOpenDialog(null);
            file = client.main.GUI.fileChooser.getSelectedFile();

            try {
                Connector.dataOutputStream.writeUTF("*!!!FILE!!!*");
                Connector.dataOutputStream.writeUTF(file.getName());
                Connector.dataOutputStream.writeLong(file.length());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    /**
     *
     * 使用FileInputStream读取文件内容
     * 通过dataOutputStream将数据传入到对端
     * @param file 要发送的文件
     */
    public static void sendFile(DataOutputStream dataOutputStream, File file) {
        int len = 0;
        try {
            fileInputStream = new FileInputStream(file);
            byte[] bytes = new byte[8 * 1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                System.out.println(len);
                dataOutputStream.write(bytes, 0, len);
                dataOutputStream.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {}
}
