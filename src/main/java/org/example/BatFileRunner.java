package org.example;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;

public class BatFileRunner {
    private static final String  startBatPath = PropertyUtil.getStartBatPath();
    private static final String backupScriptPath = PropertyUtil.getBackupScriptPath();
    private static final String channelID = PropertyUtil.getChannelId();
    private static final String guildID = PropertyUtil.getGuildId();
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
                        if(line.contains("Done")) {
                            BOT.sendEmbedMessageToChannel(guildID,channelID,"サーバーが起動しました", Color.GREEN);
                        }
                    }
                }

                int exitCode = process.waitFor(); // サーバー終了時まで待機
                System.out.println("サーバー終了コード: " + exitCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).thenRun(() -> System.out.println("サーバーの起動処理が非同期で完了しました。"));
    }

    public static void backupStart() {
        CompletableFuture.runAsync(() -> {
            try {
                ProcessBuilder builder = new ProcessBuilder(
                        "powershell.exe", "-NoProfile", "-ExecutionPolicy","Bypass", "-File", backupScriptPath,"-Encoding", "UTF8"
                );
                builder.environment().put("JAVA_TOOL_OPTIONS", "-Dfile.encoding=UTF-8");
                builder.redirectErrorStream(true);
                Process process = builder.start();

                //実行結果を読み取る
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                boolean ok = false;
                while ((line = reader.readLine()) != null) {
                    if(line.contains("RCONコマンドが失敗しました")) {
                        BOT.sendEmbedMessageToChannel(guildID,channelID,"バックアップが失敗しました",Color.RED);
                    }
                    if(!ok) {
                        System.out.println(line);
                    }
                    if(line.contains("正常に完了")) {
                        BOT.sendEmbedMessageToChannel(guildID,channelID,"バックアップのコピーが完了しました",Color.GREEN);
                    }
                    if(line.contains("バックアップファイルを圧縮しています")) {
                        BOT.sendEmbedMessageToChannel(guildID,channelID,"バックアップが完了しました", Color.GREEN);
                        ok = true;
                    }
                }
                int exitCode = process.waitFor();
                System.out.println("PowerShellスクリプトの終了コード:" + exitCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).thenRun(() -> System.out.println("バックアップの処理が終了しました"));
    }
}

