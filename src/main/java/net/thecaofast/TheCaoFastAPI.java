package net.thecaofast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.lxc.thecaofast.TheCaoFast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static me.lxc.thecaofast.TheCaoFast.pluginDebug;

public class TheCaoFastAPI {

    private TheCaoFastAPI() {
    }

    private static final String API_SERVER = "https://thecaofast.net";
    private static final String CHARGING_ENDPOINT = "API/v4/?method=charging";
    private static final String CHECK_ENDPOINT = "API/v4/?method=check";

    public static JsonObject sendCard(String apiKey, String apiSecret, String cardType, int cardAmount, String serial, String pin) {
        final String url = MessageFormat.format(
                "{0}/{1}&APIKey={2}&APISecret={3}&CardType={4}&CardAmount={5}&CardSerial={6}&CardPin={7}",
                API_SERVER, CHARGING_ENDPOINT, apiKey, apiSecret, cardType, String.valueOf(cardAmount), serial, pin);
        pluginDebug.debug("Send card URL: " + url);
        return sendRequest(url);
    }

    public static JsonObject checkCard(String apiKey, String apiSecret, String transactionID) {
        final String url = MessageFormat.format("{0}/{1}&APIkey={2}&APISecret={3}&Transaction_ID={4}",
                API_SERVER, CHECK_ENDPOINT, apiKey, apiSecret, transactionID);
        pluginDebug.debug("Check card URL: " + url);
        return sendRequest(url);
    }

    private static JsonObject sendRequest(String url) {
        try {
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
            final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            final String response = reader.lines().collect(Collectors.joining());
            return new JsonParser().parse(response).getAsJsonObject();
        } catch (SocketTimeoutException e) {
            TheCaoFast.getInstance().getLogger().log(Level.SEVERE, "Read timed out, may API server go down");
            TheCaoFast.getInstance().getLogger().log(Level.SEVERE, "Check your self: " + url);
            TheCaoFast.getInstance().getLogger().log(Level.WARNING, "Attempting to try again...");
            return sendRequest(url);
        } catch (IOException e){
            TheCaoFast.getInstance().getLogger().log(Level.SEVERE, "An error occurred ", e);
            return null;
        }

    }
}
