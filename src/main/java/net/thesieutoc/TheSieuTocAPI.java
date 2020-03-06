package net.thesieutoc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.stream.Collectors;

import static me.lxc.thesieutoc.TheSieuToc.artxeDebug;

public class TheSieuTocAPI {

    private TheSieuTocAPI() {}

    private static final String API_SERVER = "https://thesieutoc.net";
    private static final String TRANSACTION = "API/transaction";
    private static final String CARD_CHARGING= "card_charging_api/check-status.html";

    public static JsonObject sendCard(String apiKey, String apiSecret, String pin, String serial, String cardType, int cardAmount) {
        final String url = MessageFormat.format(
                "{0}/{1}?APIkey={2}&APIsecret={3}&mathe={4}&seri={5}&type={6}&menhgia={7}",
                API_SERVER, TRANSACTION, apiKey, apiSecret, pin, serial, cardType, cardAmount);
        return sendRequest(url);
    }

    public static JsonObject checkCard(String apiKey, String apiSecret, String transactionID) {
        final String url = MessageFormat.format("{0}/{1}?APIkey={2}&APIsecret={3}&transaction_id={4}",
                API_SERVER, CARD_CHARGING, apiKey, apiSecret, transactionID);
        return sendRequest(url);
    }

    private static JsonObject sendRequest(String url) {
        try {
            final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setReadTimeout(36 * 10 ^ 5);
            connection.setDoInput(true);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            final String response = reader.lines().collect(Collectors.joining());
            return new JsonParser().parse(response).getAsJsonObject();
        } catch (IOException e){
            artxeDebug.debug("An error occurred: " + e.getCause().getMessage());
            return null;
        }

    }
}
