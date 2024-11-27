package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;

public class BatFileRunner {
    private static final String  startBatPath = PropertyUtil.getStartBatPath();
    public static void serverStart() {
        CompletableFuture.runAsync(() -> {
            try {
                ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", startBatPath);
                builder.redirectErrorStream(true); // 標準エラーを標準出力にリダイレクト
                Process process = builder.start();

                // サーバーのログ出力を非同期で読み取る
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("[サーバー]: " + line);
                    }
                }

                int exitCode = process.waitFor(); // サーバー終了時まで待機
                System.out.println("サーバー終了コード: " + exitCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).thenRun(() -> System.out.println("サーバーの起動処理が非同期で完了しました。"));
    }
}

