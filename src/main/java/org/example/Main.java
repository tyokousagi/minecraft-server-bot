package org.example;

import java.nio.charset.Charset;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("file.encoding", "UTF-8");
        System.out.println(Charset.defaultCharset().displayName());
        BOT.startBot();
        MinecraftRcon.build();
        BOT.serverActivity();
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

        // serverActivityメソッドを定期的に実行
        service.scheduleAtFixedRate(()-> {
            try {
                BOT.serverActivity();
            }catch (Exception e) {
                e.printStackTrace();
            }
        },0,30, TimeUnit.SECONDS);

    }
}