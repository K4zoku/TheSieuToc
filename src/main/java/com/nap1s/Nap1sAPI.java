package com.nap1s;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.lxc.nap1s.Nap1S;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.text.MessageFormat;
import java.util.logging.Level;

import static me.lxc.nap1s.Nap1S.pluginDebug;

public class Nap1sAPI {

    private Nap1sAPI() {}

    private static final String API_SERVER = "https://nap1s.com";
    private static final String FAST_ENDPOINT = "api/v1/charging/auto";
    private static final String SLOW_ENDPOINT = "api/v1/charging/slow";
    private static final String CHECK_ENDPOINT = "api/v1/check";

    public static JsonObject sendCard(String method, String apiKey, String apiSecret, String cardType, int cardAmount, String serial, String pin) {
        String endpoint;
        switch (method.toLowerCase()) {
            case "auto":
            case "fast":
                endpoint = FAST_ENDPOINT;
                break;
            case "slow":
            default:
                endpoint = SLOW_ENDPOINT;
                break;
        }
        final String url = MessageFormat.format(
                "{0}/{1}",
                API_SERVER, endpoint);
        final String data = MessageFormat.format(
                "APIKey={0}&APISecret={1}&CardPin={2}&CardSerial={3}&CardType={4}&CardAmount="+cardAmount,
                apiKey, apiSecret, pin, serial, cardType
        );
        pluginDebug.debug("Send card URL: " + url);
        pluginDebug.debug("Data: " + data);
        return sendRequest(url, data);
    }

    public static JsonObject checkCard(String apiKey, String apiSecret, String transactionID) {
        final String url = MessageFormat.format("{0}/{1}",
                API_SERVER, CHECK_ENDPOINT);
        final String data = MessageFormat.format(
                "APIKey={0}&APISecret={1}&transaction_id={2}",
                apiKey, apiSecret, transactionID
        );
        pluginDebug.debug("Check card URL: " + url);
        pluginDebug.debug("Data: " + data);
        return sendRequest(url, data);
    }

    private static JsonObject sendRequest(String url, String params) {
        try {
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
            final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(params);
            wr.flush();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            return new JsonParser().parse(response.toString()).getAsJsonObject();
        } catch (SocketTimeoutException e) {
            Nap1S.getInstance().getLogger().log(Level.SEVERE, "Read timed out, may API server go down");
            Nap1S.getInstance().getLogger().log(Level.SEVERE, "Check your self: " + url);
            Nap1S.getInstance().getLogger().log(Level.WARNING, "Attemping to try again...");
            return sendRequest(url, params);
        } catch (IOException e){
            Nap1S.getInstance().getLogger().log(Level.SEVERE, "An error occurred ", e);
            return null;
        }

    }
}
