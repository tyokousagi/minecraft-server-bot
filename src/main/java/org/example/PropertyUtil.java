package org.example;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertyUtil {
    private static final String FILE_PATH = "D:\\MOD\\Administration_BOT\\src\\main\\java\\org\\example\\config.properties";
    private static final Properties properties;
    private PropertyUtil() {

    }
    static {
        properties = new Properties();
        try {
            properties.load(Files.newBufferedReader(Paths.get(FILE_PATH), StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("ファイルの読み込みに失敗しました");
        }
    }

    public static String getToken() {
        return properties.getProperty("token");
    }
    public static String getMinecraftVersion() {
        return properties.getProperty("minecraft_version");
    }
    public static String getBotVersion() {
        return properties.getProperty("bot_version");
    }
    public static String getGuildId() {
        return properties.getProperty("guild_id");
    }
    public static String getChannelId() {
        return properties.getProperty("channel_id");
    }
    public static String getRconPort() {
        return properties.getProperty("rcon_port");
    }
    public static String getRconPassword() {
        return properties.getProperty("rcon_password");
    }
    public static String getRconHost() {
        return properties.getProperty("rcon_host");
    }
    public static String getStartBatPath() {
        return properties.getProperty("start_bat_path");
    }
    public static String getBackupScriptPath() {
        return properties.getProperty("backup_script_path");
    }

}
