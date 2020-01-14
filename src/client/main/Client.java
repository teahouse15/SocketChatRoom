package client.main;

import client.service.Connector;
import client.service.Receiver;
import utlis.TimeManager;

public class Client {

    public static Thread connector;
    public static Thread receiver;


    public static void main(String[] args) {
        new GUI().initFrame();

        GUI.receiveArea.append("[" + TimeManager.nowTime() + "][INFO][Client] 欢迎使用ChatWithMe-Client V1.0.0!\r\n");

        connector = new Thread(new Connector());
        connector.run();

        receiver = new Thread(new Receiver());
        receiver.start();
    }
}
