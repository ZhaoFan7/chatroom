/**
 * Author:zhaofan
 * Created:2019/3/1
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多线程聊天室
 * 主要实现功能
 * 1、等待客户端连接
 * 2、将任务发布给执行器执行
 */
public class ServerThread {
    public static void main(String[] args){

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        try {
            //1、创建ServerSocket对象
            ServerSocket serverSocket = new ServerSocket(7000);
            System.out.println("服务器启动！！！" );
            //2、准备接受客户端的信息,等待客户端连接
            System.out.println("等待客户端连接.......\n");
            //3、利用执行器执行任务
            while(true){
                Socket client = serverSocket.accept();
                System.out.println("有新的用户连接:"+client.getInetAddress()+"   "+client.getPort());
                executorService.submit(new ExecutorServer(client));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
