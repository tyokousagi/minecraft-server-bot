package org.example;

import java.nio.charset.Charset;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("file.encoding", "UTF-8");
        System.out.println(Charset.defaultCharset().displayName());
        BOT.startBot();
    }
}