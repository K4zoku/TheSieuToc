package me.lxc.thesieutoc.tasks;

import com.google.gson.JsonObject;
import me.lxc.artxeapi.utils.ArtxeCommands;
import me.lxc.thesieutoc.TheSieuToc;
import me.lxc.thesieutoc.internal.Messages;
import net.thesieutoc.TheSieuTocAPI;
import net.thesieutoc.data.CardInfo;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CardCheckTask extends BukkitRunnable {

    public CardCheckTask(TheSieuToc instance) {
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
                TheSieuToc.pluginDebug.debug("Checking in progress...");
                for(Map.Entry<Player, List<CardInfo>> playerCards : TheSieuToc.getInstance().queue.entrySet()){
                    Player player = playerCards.getKey();
                    List<CardInfo> cards = playerCards.getValue();
                    List<CardInfo> removeQueue = new ArrayList<>();
                    for(CardInfo card : cards){
                        checkOne(player, card, removeQueue);
                    }
                    cards.removeAll(removeQueue);
                    TheSieuToc.getInstance().queue.replace(player, cards);
                }
            }
        }.runTaskAsynchronously(TheSieuToc.getInstance());
    }

    public static boolean checkOne(final Player player, final CardInfo card, List<CardInfo> removeQueue) {
        final Messages messages = TheSieuToc.getInstance().getMessages();
        String notes;
        JsonObject checkCard = TheSieuTocAPI.checkCard(TheSieuToc.getInstance().getSettings().iTheSieuTocKey, TheSieuToc.getInstance().getSettings().iTheSieuTocSecret, card.transactionID);
        TheSieuToc.pluginDebug.debug("Response: " + checkCard.toString());
        String status = checkCard.get("status").getAsString();
        boolean isOnline = player.isOnline();
        switch (status) {
            case "00":
                if(isOnline) player.sendMessage(messages.success.replaceAll("(?ium)[{]Amount[}]", String.valueOf(card.amount)));
                successAction(player, card.amount);
                notes = ChatColor.stripColor(messages.success);
                TheSieuToc.getInstance().getDonorLog().writeLog(player, card.serial, card.pin, card.type, card.amount, true, notes);
                if (removeQueue != null) removeQueue.add(card);
                break;
            case "-9":
                if(isOnline) player.sendMessage(messages.awaitingApproval);
                return true;
            default:
                if(isOnline) {
                    player.sendMessage(messages.fail);
                    player.sendMessage(checkCard.get("msg").getAsString());
                }
                notes = checkCard.get("msg").getAsString();
                if (removeQueue != null) removeQueue.add(card);
                TheSieuToc.getInstance().getDonorLog().writeLog(player, card.serial, card.pin, card.type, card.amount, false, notes);
        }
        return false;
    }

    private static void successAction(Player player, int amount) {
        List<String> commands = TheSieuToc.getInstance().getSettings().yaml().getConfig().getStringList("Card-Reward." + amount);
        for (String command : commands) {
            ArtxeCommands.dispatchCommand(player, command);
        }
    }
}
