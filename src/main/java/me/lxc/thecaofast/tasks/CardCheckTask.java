package me.lxc.thecaofast.tasks;

import com.google.gson.JsonObject;
import me.lxc.artxeapi.utils.ArtxeCommands;
import me.lxc.thecaofast.TheCaoFast;
import me.lxc.thecaofast.internal.Messages;
import net.thecaofast.TheCaoFastAPI;
import net.thecaofast.data.CardInfo;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CardCheckTask extends BukkitRunnable {

    public CardCheckTask(TheCaoFast instance) {
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
                if (TheCaoFast.getInstance().queue.size() == 0) return;
                TheCaoFast.pluginDebug.debug("Checking in progress...");
                for (Map.Entry<Player, List<CardInfo>> playerCards : TheCaoFast.getInstance().queue.entrySet()) {
                    Player player = playerCards.getKey();
                    List<CardInfo> cards = playerCards.getValue();
                    List<CardInfo> removeQueue = new ArrayList<>();
                    for (CardInfo card : cards) {
                        checkOne(player, card, removeQueue);
                    }
                    cards.removeAll(removeQueue);
                    TheCaoFast.getInstance().queue.replace(player, cards);
                }
            }
        }.runTaskAsynchronously(TheCaoFast.getInstance());
    }

    public static boolean checkOne(final Player player, final CardInfo card, List<CardInfo> removeQueue) {
        final Messages messages = TheCaoFast.getInstance().getMessages();
        String notes;
        JsonObject checkCard = TheCaoFastAPI.checkCard(TheCaoFast.getInstance().getSettings().iTheCaoFastKey, TheCaoFast.getInstance().getSettings().iTheCaoFastSecret, card.transactionID);
        TheCaoFast.pluginDebug.debug("Data sent: " + card.toString());
        TheCaoFast.pluginDebug.debug("Response: " + (checkCard != null ? checkCard.toString() : "NULL"));
        assert checkCard != null;
        JsonObject result = checkCard.get("result").getAsJsonObject();
        int status = result.get("status").getAsInt();
        boolean isOnline = player.isOnline();
        switch (status) {
            case 200:
                notes = messages.success.replaceAll("(?ium)[{]Amount[}]", String.valueOf(card.amount));
                if (isOnline) player.sendMessage(notes);
                notes = ChatColor.stripColor(notes);
                successAction(player, card.amount);
                TheCaoFast.getInstance().getDonorLog().writeLog(player, card.serial, card.pin, card.type, card.amount, true, notes);
                if (removeQueue != null) removeQueue.add(card);
                return true;
            case 201:
                if (isOnline) player.sendMessage(messages.awaitingApproval);
                return true;
            default:
                if (isOnline) {
                    player.sendMessage(messages.fail);
                    player.sendMessage(result.get("msg").getAsString());
                }
                notes = result.get("msg").getAsString();
                if (removeQueue != null) removeQueue.add(card);
                TheCaoFast.getInstance().getDonorLog().writeLog(player, card.serial, card.pin, card.type, card.amount, false, notes);
                return false;
        }
    }

    private static void successAction(Player player, int amount) {
        List<String> commands = TheCaoFast.getInstance().getSettings().yaml().getConfig().getStringList("Card-Reward." + amount);
        for (String command : commands) {
            ArtxeCommands.dispatchCommand(player, command);
        }
    }
}
