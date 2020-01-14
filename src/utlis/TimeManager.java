package utlis;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeManager {

    public static Date date;
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");



    public static String nowTime() {
        date = new Date();
        String now = simpleDateFormat.format(date);

        return now;
    }
}
