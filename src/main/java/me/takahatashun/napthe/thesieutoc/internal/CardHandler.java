package me.takahatashun.napthe.thesieutoc.internal;

import com.google.gson.JsonObject;
import com.sun.istack.internal.NotNull;
import me.takahatashun.napthe.thesieutoc.NapThe;
import me.takahatashun.napthe.thesieutoc.data.CardInfo;
import me.takahatashun.napthe.thesieutoc.data.DonorLog;
import me.takahatashun.napthe.thesieutoc.data.PluginYAML;
import me.takahatashun.napthe.thesieutoc.utils.CommandUtils;
import net.thesieutoc.CardAmount;
import net.thesieutoc.CardType;
import net.thesieutoc.TheSieuTocAPI;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;

public class CardHandler {

    public static void triggerInputCard(@NotNull Player player, String type, int amount, PluginYAML lang) {

        SignGui inpCard = new SignGui(NapThe.getInstance());

        inpCard.newMenu(player,
                Arrays.asList("", "",
                lang.getString("Messages.Input-Serial", "§9Dòng 1: Số Serial"),
                lang.getString("Messages.Input-Pin", "§9Dòng 2: Mã thẻ")))
            .reopenIfFail()
            .response((p, text) -> {
                List<String> signLine = Arrays.asList(text);
                String serial = signLine.get(0);
                String pin = signLine.get(1);
                try {
                    JsonObject response = TheSieuTocAPI.sendCard(
                            NapThe.getInstance().getSettings().TheSieuToc_Key,
                            NapThe.getInstance().getSettings().TheSieuToc_Secret,
                            pin, serial, CardType.valueOf(type.toUpperCase()),
                            Objects.requireNonNull(CardAmount.getCardAmount(amount)));
                    int status = response.get("status").getAsInt();
                    if (status == 0) {
                        NapThe.getInstance().addCardToQueue(player, response.get("transaction_id").getAsString(), type.toUpperCase(), serial, pin, amount);
                        player.sendMessage(lang.getString("Messages.Handling"));
                        return true;
                    } else {
                        player.sendMessage(lang.getString("Messages.Fail"));
                        player.sendMessage(response.get("msg").getAsString());
                        return false;
                    }

                } catch (IOException e) {
                    player.sendMessage(lang.getString("Messages.Sys-Err"));
                    e.printStackTrace();
                    return false;
                }
            }).open();
    }

    public static void cardChecker(HashMap<Player, List<CardInfo>> queue, PluginYAML lang){
        for(Map.Entry<Player, List<CardInfo>> playerCards: queue.entrySet()){
            Player player = playerCards.getKey();
            List<CardInfo> cards = playerCards.getValue();
            List<CardInfo> removeQueue = new ArrayList<>();
            for(CardInfo card : cards){
                try {
                    boolean success;
                    String notes;
                    String transactionID = card.transactionID;
                    String type = card.type;
                    String serial = card.serial;
                    String pin = card.pin;
                    int amount = card.amount;
                    JsonObject response = TheSieuTocAPI.checkCard(
                            NapThe.getInstance().getSettings().TheSieuToc_Key,
                            NapThe.getInstance().getSettings().TheSieuToc_Secret,
                            transactionID);
                    int status = response.get("status").getAsInt();
                    boolean isOnline = player.isOnline();
                    switch (status){
                        case 0: {
                            if(isOnline) player.sendMessage(lang.getString("Messages.Success").replaceAll("(?ium)[{]Amount[}]", String.valueOf(amount)));
                            List<String> commands = NapThe.getInstance().getConfig().getStringList("Card." + amount);
                            for(String command : commands){
                                CommandUtils.dispatchCommand(player, command);
                            }
                            notes = "";
                            success = true;
                            removeQueue.add(card);
                            break;
                        }
                        case -9: {
                            if(isOnline) player.sendMessage(lang.getString("Messages.Awaiting-Approval"));
                            return;
                        }
                        default: {
                            if(isOnline) {
                                player.sendMessage(lang.getString("Messages.Fail"));
                                player.sendMessage(response.get("msg").getAsString());
                            }
                            success = false;
                            notes = response.get("msg").getAsString();
                            removeQueue.add(card);
                            break;
                        }
                    }
                    new DonorLog(player, type, serial, pin, amount, success, notes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            cards.removeAll(removeQueue);
            queue.replace(player, cards);
        }
    }

}
