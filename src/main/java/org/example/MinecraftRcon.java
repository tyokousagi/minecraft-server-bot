package org.example;

import net.kronos.rkon.core.Rcon;
import net.kronos.rkon.core.ex.AuthenticationException;

import java.io.IOException;

public class MinecraftRcon {
    public static boolean isConnected = false;
    public  static Rcon rcon = null;
    private static final String HOST = PropertyUtil.getRconHost();
    private static final int PORT = Integer.parseInt(PropertyUtil.getRconPort());
    private static final byte PASSWORD = Byte.parseByte(PropertyUtil.getRconPassword());

    public static void build() {
        try {
            rcon = new Rcon(HOST,PORT, new byte[]{PASSWORD});
        }catch (AuthenticationException e) {
            e.printStackTrace();
            System.out.println("Rconへのログインに失敗しました");
            System.out.println("パスワードが間違っている可能性があります");
            return;
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Rconへの接続に失敗しました");
            return;
        }

        isConnected = true;
        System.out.println("Rconに接続しました");
    }

    public static String sendCommand(String command) {
        String result = null;

        try {
            result = rcon.command(command);
        }
        catch (IOException | NullPointerException | NegativeArraySizeException e) {
            build();
        }
        return result;
    }
}
