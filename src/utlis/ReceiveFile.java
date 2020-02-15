package utlis;

import java.io.*;


public class ReceiveFile extends Thread {

    private static FileOutputStream fileOutputStream; //输出流


    public static void receiveFile(DataInputStream dataInputStream, File file, long length) {
        try {
            fileOutputStream = new FileOutputStream(file);

            int len = 0;
            byte[] bytes = new byte[8 * 1024];
            while ((len = dataInputStream.read(bytes)) != -1) {
                if (length == file.length()) {
                    break;
                }
                fileOutputStream.write(bytes, 0, len);
                fileOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
    }
}
