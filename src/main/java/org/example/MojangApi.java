package org.example;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MojangApi {
    private static final String MOJANG_API_URL = "https://api.mojang.com/users/profiles/minecraft/";

    public static String getUUID(String username) throws  Exception {
        URL url = new URL(MOJANG_API_URL + username);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if(responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());

            String uuidWithoutHyphens = jsonResponse.getString("id");

            return formatUUID(uuidWithoutHyphens);
        }else {
            throw new Exception("Failed to get UUID. Response Code: " + responseCode);
        }
    }

    private static String formatUUID(String uuidWithoutHyphens) {
        return uuidWithoutHyphens.substring(0, 8) + "-" +
                uuidWithoutHyphens.substring(8, 12) + "-" +
                uuidWithoutHyphens.substring(12, 16) + "-" +
                uuidWithoutHyphens.substring(16, 20) + "-" +
                uuidWithoutHyphens.substring(20);
    }
}
