package hots.chapter16;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppServer extends Thread {


    private final int port;

    private static final int DEFAULT_PORT = 12722;

    private volatile boolean start = true;

    private final List<ClientHandler> clientHandlers = new LinkedList<>();

    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    private ServerSocket serverSocket;

    public AppServer() {
        this.port = DEFAULT_PORT;
    }

    public AppServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(port);
            System.out.println("端口：" + port + "，正在等待客户端连接.....");
            while (start) {
                // 获取客户端链接
                Socket client = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(client);
                clientHandlers.add(clientHandler);
                // 提交任务处理
                executor.submit(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.dispose();
        }
    }

    /**
     * 清理线程程池资源
     */
    private void dispose() {
        System.out.println("清理线程程池资源.....");
        clientHandlers.stream().forEach(e -> e.shutDown());
        executor.shutdown();
    }

    /**
     * 关闭服务端
     */
    public void shutDown() {
        System.out.println("关闭服务端.....");
        this.start = false;
        this.interrupt();
        try {
            serverSocket.close();
            System.out.println("服务端正常关闭");
        } catch (IOException e) {
            System.out.println("服务端关闭失败 : " + e.getMessage());
        }
    }
}
