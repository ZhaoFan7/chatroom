import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Author:zhaofan
 * Created:2019/3/1
 */

/**
 * 客户端发送消息线程
 */
public class WriteThread implements Runnable {

    private Socket socket;

    public WriteThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        Scanner message = new Scanner(System.in);
        try {
            OutputStream clientOutput = socket.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(clientOutput);
            while (true) {
                System.out.println("请输入要发送的信息：");
                String clientData = null;
                if (message.hasNext()) {
                    clientData =message.next();
                    writer.write(clientData+"\n");
                    writer.flush();
                    System.out.println("信息以发送");
                }
                if (clientData.equals("bye")) {
                    message.close();
                    writer.close();
                    socket.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
