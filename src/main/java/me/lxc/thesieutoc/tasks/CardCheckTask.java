package me.lxc.thesieutoc.tasks;

import com.google.gson.JsonObject;
import me.lxc.artxeapi.utils.ArtxeCommands;
import me.lxc.thesieutoc.TheSieuToc;
import me.lxc.thesieutoc.internal.Messages;
import net.thesieutoc.TheSieuTocAPI;
import net.thesieutoc.data.CardInfo;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CardCheckTask extends BukkitRunnable {
    private TheSieuToc instance;

    public CardCheckTask(TheSieuToc instance) {
        this.instance = instance;
        this.runTaskTimer(instance, 0L, instance.getSettings().cardCheckPeriod);
    }

    @Override
    public void run() {
        check(this.instance);
    }

    public static void check(TheSieuToc instance) {
        final Messages messages = instance.getMessages();
        for(Map.Entry<Player, List<CardInfo>> playerCards: instance.queue.entrySet()){
            Player player = playerCards.getKey();
            List<CardInfo> cards = playerCards.getValue();
            List<CardInfo> removeQueue = new ArrayList<>();
            for(CardInfo card : cards){
                boolean success;
                String notes;
                String transactionID = card.transactionID;
                String type = card.type;
                String serial = card.serial;
                String pin = card.pin;
                int amount = card.amount;
                JsonObject response = TheSieuTocAPI.checkCard(TheSieuToc.getInstance().getSettings().iTheSieuTocKey, TheSieuToc.getInstance().getSettings().iTheSieuTocSecret, transactionID);
                int status = response.get("status").getAsInt();
                boolean isOnline = player.isOnline();
                switch (status) {
                    case 0:
                        successAction(isOnline, player, messages, amount);
                        notes = "";
                        success = true;
                        removeQueue.add(card);
                        break;
                    case -9:
                        if(isOnline) player.sendMessage(messages.awaitingApproval);
                        return;
                    default:
                        if(isOnline) {
                            player.sendMessage(messages.fail);
                            player.sendMessage(response.get("msg").getAsString());
                        }
                        success = false;
                        notes = response.get("msg").getAsString();
                        removeQueue.add(card);
                        break;
                }
                instance.getDonorLog().writeLog(player, serial, pin, type, amount, success, notes);
            }
            cards.removeAll(removeQueue);
            instance.queue.replace(player, cards);
        }
    }

    private static void successAction(boolean isOnline, Player player, Messages messages, int amount) {
        if(isOnline) player.sendMessage(messages.success.replaceAll("(?ium)[{]Amount[}]", String.valueOf(amount)));
        List<String> commands = TheSieuToc.getInstance().getSettings().yaml().getConfig().getStringList("Card." + amount);
        for(String command : commands){
            ArtxeCommands.dispatchCommand(player, command);
        }
    }
}
