import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Author:zhaofan
 * Created:2019/3/1
 */
/**
 * 客户端接受消息线程
 */
public class ReadThread implements Runnable {
    private Socket socket;

    public ReadThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run(){

        try{
            Scanner scanner = new Scanner(socket.getInputStream());
            while(true){
                if(scanner.hasNext()){
                    System.out.println(scanner.nextLine());
                }
                if(socket.isClosed()){
                    break;
                }
            }
            scanner.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
