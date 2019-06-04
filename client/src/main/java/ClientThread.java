import java.io.IOException;
import java.net.Socket;

/**
 * Author:zhaofan
 * Created:2019/3/1
 */
/**
 * 客户端
 */
public class ClientThread {
    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("127.0.0.1",7000);

        Thread readInfoThread = new Thread(new ReadThread(socket));
        Thread writeInfoThread = new Thread(new WriteThread(socket));
        readInfoThread.start();
        writeInfoThread.start();
    }
}
