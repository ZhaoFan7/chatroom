/**
 * Author:zhaofan
 * Created:2019/3/1
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.*;

/**
 * 执行器
 * 功能:执行服务端发布的任务
 *   1、注册
 *   2、群聊
 *   3、私聊
 *   4、退出
 */
public class ExecutorServer implements Runnable {
    private static final Map<String,Socket> clientMap  = new HashMap<String,Socket>();

    private final Socket client;

    public ExecutorServer(Socket client){
        this.client = client;
    }

    @Override
    public void run() {
        //给服务端发送使用说明书
        send(this.client);
        //接收数据
        try {
            InputStream receiveClient = this.client.getInputStream();
            Scanner scanner = new Scanner(receiveClient);
            while(true){
                if(scanner.hasNext()){
                    String clientDate = scanner.nextLine();
                    if(clientDate.startsWith("userName:")){
                        String userName = clientDate.split(":")[1];
                        this.userName(userName,this.client);
                        continue;
                    }

                    if(clientDate.startsWith("group:")){
                        String message = clientDate.split(":")[1];
                        this.group(message);
                        continue;
                    }

                    if(clientDate.startsWith("private:")){
                        String friend = clientDate.split(":")[1];
                        String message = clientDate.split(":")[2];
                        this.privates(friend,message);
                        continue;
                    }

                    if(clientDate.equals("bye")){
                        this.bye(this.client);
                        break;
                    }

                    if((!clientDate.startsWith("userName:"))||(!clientDate.startsWith("group:"))||(!clientDate.startsWith("private:"))||(!clientDate.equals("bey"))){
                        this.send(client,"输入错误，请按照规定格式重新输入！");
                        continue;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void send(Socket socket) {
        try {
            //发送数据
            OutputStream clientOutput = socket.getOutputStream();
            //将字节流转换为字符流
            OutputStreamWriter writer = new OutputStreamWriter(clientOutput);
            writer.write("你好，欢迎连接服务器!\n"+"如果想注册，请输入“userName:***”\n"+"群聊输入：“group:****”\n"+"私聊输入：“private:***(用户名):****”\n"+"退出请输入：“bye”  !\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void send(Socket socket,String s) {
        try {
            //发送数据
            OutputStream clientOutput = socket.getOutputStream();;
            //将字节流转换为字符流
            OutputStreamWriter writer = new OutputStreamWriter(clientOutput);
            writer.write(s+"\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void bye(Socket socket) {
        this.send(socket,"bye!");
        String name = null;
        for (String getKey:clientMap.keySet()){
            if(clientMap.get(getKey).equals(socket)){
                name = getKey;
            }
        }
        System.out.println("用户"+name+"下线了！"+"当前在线人数"+clientMap.size()+"人");
        clientMap.remove(name);
    }

    private void privates(String friend,String message) {
        Socket socket = clientMap.get(friend);
        String name = null;
        this.send(socket,message);
    }

    private void group(String message) {
        Set<Map.Entry<String,Socket>> entrySet = clientMap.entrySet();
        Iterator<Map.Entry<String,Socket>> iterator = entrySet.iterator();

        for(Map.Entry<String,Socket> StringSocketEntry :entrySet){
            Socket socket = StringSocketEntry.getValue();
            this.send(socket,message);
        }
    }

    private void userName(String userName,Socket socket) {
        clientMap.put(userName,socket);
        System.out.println("用户"+userName+"上线啦！ "+socket.getLocalPort()+"   当前在线人数"+clientMap.size()+"人");
        this.group("用户"+userName+"上线啦！当前在线人数"+clientMap.size()+"人");
    }
}
