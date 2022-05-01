package hots.chapter7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PreventDuplicated {
    private static final String LOCK_PATH = "E:\\Downloads";

    private static final String LOCK_FILE = ".lock";

    private static final String PERMISSIONS = "rw-------";

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // 程序退出，删除lock文件
            System.out.println("deal program shutdown...");
            getLockFile().toFile().delete();
        }));

        // 判断Lock文件，存在，抛重复执行异常，不存在继续
        checkLockFile();
        //程序执行
        try {
            Thread.sleep(20_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void checkLockFile() {
        Path path = getLockFile();
        if (path.toFile().exists()) {
            throw new RuntimeException("The program already running.");
        }
        try {
            Files.createFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Path getLockFile() {
        return Paths.get(LOCK_PATH, LOCK_FILE);
    }
}
