package me.lxc.nap1s.tasks;

import com.google.gson.JsonObject;
import com.nap1s.Nap1sAPI;
import com.nap1s.data.CardInfo;
import me.lxc.artxeapi.utils.ArtxeCommands;
import me.lxc.nap1s.Nap1S;
import me.lxc.nap1s.internal.Messages;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CardCheckTask extends BukkitRunnable {

    public CardCheckTask(Nap1S instance) {
        this.runTaskTimer(instance, 0L, instance.getSettings().cardCheckPeriod);
    }

    @Override
    public void run() {
        checkAll();
    }

    public static void checkAll() {
        new BukkitRunnable(){
            @Override
            public void run() {
                Nap1S.pluginDebug.debug("Checking in progress...");
                for(Map.Entry<Player, List<CardInfo>> playerCards : Nap1S.getInstance().queue.entrySet()){
                    Player player = playerCards.getKey();
                    List<CardInfo> cards = playerCards.getValue();
                    List<CardInfo> removeQueue = new ArrayList<>();
                    for(CardInfo card : cards){
                        checkOne(player, card, removeQueue);
                    }
                    cards.removeAll(removeQueue);
                    Nap1S.getInstance().queue.replace(player, cards);
                }
            }
        }.runTaskAsynchronously(Nap1S.getInstance());
    }

    public static boolean checkOne(final Player player, final CardInfo card, List<CardInfo> removeQueue) {
        final Messages messages = Nap1S.getInstance().getMessages();
        final boolean isOnline = player.isOnline();
        String notes;
        JsonObject checkCard = Nap1sAPI.checkCard(Nap1S.getInstance().getSettings().iNap1sKey, Nap1S.getInstance().getSettings().iNap1sSecret, card.transactionID);
        Nap1S.pluginDebug.debug("Response: " + (checkCard != null ? checkCard.toString() : "NULL"));
        if (checkCard == null || !checkCard.has("result")) {
            if (isOnline) player.sendMessage(messages.systemError);
            return false;
        }
        int status = checkCard.get("result").getAsJsonObject().get("status").getAsInt();
        switch (status) {
            case 200:
                if(isOnline) player.sendMessage(messages.success.replaceAll("(?ium)[{]Amount[}]", String.valueOf(card.amount)));
                successAction(player, card.amount);
                notes = ChatColor.stripColor(messages.success);
                Nap1S.getInstance().getDonorLog().writeLog(player, card.serial, card.pin, card.type, card.amount, true, notes);
                if (removeQueue != null) removeQueue.add(card);
                break;
            case 201:
                if(isOnline) player.sendMessage(messages.awaitingApproval);
                return true;
            default:
                if(isOnline) {
                    player.sendMessage(messages.fail);
                    player.sendMessage(checkCard.get("result").getAsJsonObject().get("msg").getAsString());
                }
                notes = checkCard.get("result").getAsJsonObject().get("msg").getAsString();
                if (removeQueue != null) removeQueue.add(card);
                Nap1S.getInstance().getDonorLog().writeLog(player, card.serial, card.pin, card.type, card.amount, false, notes);
        }
        return false;
    }

    private static void successAction(Player player, int amount) {
        List<String> commands = Nap1S.getInstance().getSettings().yaml().getConfig().getStringList("Card-Reward." + amount);
        for (String command : commands) {
            ArtxeCommands.dispatchCommand(player, command);
        }
    }
}
