package hots.chapter16;

import java.io.*;
import java.net.Socket;

/**
 * 处理客户端请求
 */
public class ClientHandler extends Thread {
    private volatile boolean running = true;

    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
             PrintWriter printWriter = new PrintWriter(outputStream);
        ) {
            while (running) {
                // 读取客户端提供数据
                String message = bufferedReader.readLine();
                if (message == null) {
                    break;
                }
                System.out.println("读取客户端数据 >" + message);
                // 回写数据给客户端
                printWriter.write("echo " + message + "\n");
                printWriter.flush();
            }
            System.out.println("客户端正常关闭");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.shutDown();
        }
    }

    public void shutDown() {
        if (!running) {
            // 客户端已经执行完成：balking设计模式
            return;
        }
        this.running = false;
        try {
            this.socket.close();
            System.out.println("客户端正常关闭");
        } catch (IOException e) {
            System.out.println("客户端关闭失败：" + e.getMessage());
        }
    }
}
