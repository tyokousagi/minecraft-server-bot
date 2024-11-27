package org.example;

import java.io.IOException;
import java.net.Socket;

public class MinecraftServerChecker {
    public static boolean isServerRunning(String host,int port) {
        try (Socket socket = new Socket(host, port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
