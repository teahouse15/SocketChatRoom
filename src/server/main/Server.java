package server.main;

import server.service.Networker;
import utlis.TimeManager;

public class Server {

    public static void main(String[] args) {


        new GUI().initFrame();

        GUI.receiveArea.append("[" + TimeManager.nowTime() + "][INFO][Server] 欢迎使用ChatWithMe-Server V1.0.0!\r\n");

        Networker networker = new Networker();
        networker.run();
    }
}
