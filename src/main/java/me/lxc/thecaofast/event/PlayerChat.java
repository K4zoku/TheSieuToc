package me.lxc.thecaofast.event;

import com.google.gson.JsonObject;
import me.lxc.thecaofast.TheCaoFast;
import me.lxc.thecaofast.internal.Messages;
import me.lxc.thecaofast.internal.Settings;
import me.lxc.thecaofast.internal.Ui;
import me.lxc.thecaofast.tasks.CardCheckTask;
import net.thecaofast.TheCaoFastAPI;
import net.thecaofast.data.CardInfo;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static me.lxc.thecaofast.handlers.InputCardHandler.*;

public class PlayerChat implements Listener {

    @EventHandler
    public void event(AsyncPlayerChatEvent e) {
        final String text = ChatColor.stripColor(e.getMessage());
        final Settings settings = TheCaoFast.getInstance().getSettings();
        final Messages msg = TheCaoFast.getInstance().getMessages();
        final Ui ui = TheCaoFast.getInstance().getUi();
        final Player player = e.getPlayer();

        if (stepOne(player) && !stepTwo(player)) {
            e.setCancelled(true);
            if (ui.cancel.stream().noneMatch(text::equalsIgnoreCase)) {
                player.sendMessage(msg.serial.replaceAll("(?ium)[{]Serial[}]", text));
                unTriggerStep1(player);
                triggerStepTwo(player, text);
            } else {
                unTriggerStep1(player);
                purgePlayer(player);
                player.sendMessage(msg.cancelled);
            }
            return;
        }

        if (!stepOne(player) && stepTwo(player)) {
            e.setCancelled(true);
            if (ui.cancel.stream().noneMatch(text::equalsIgnoreCase)) {
                LocalCardInfo info = lastStep(player, text);
                unTriggerStep2(player);
                player.sendMessage(msg.pin.replaceAll("(?ium)[{]Pin[}]", text));
                JsonObject sendCard = TheCaoFastAPI.sendCard(settings.iTheCaoFastKey, settings.iTheCaoFastSecret, info.type, info.amount, info.serial, info.pin);
                TheCaoFast.pluginDebug.debug("Response: " + (sendCard != null ? sendCard.toString() : "NULL"));
                assert sendCard != null;
                JsonObject result = sendCard.get("result").getAsJsonObject();
                if (result.get("status").getAsInt() != 201) {
                    player.sendMessage(msg.fail);
                    player.sendMessage(result.get("msg").getAsString());
                    return;
                }
                String transactionID = result.get("transaction_id").getAsString();
                CardInfo tcfInfo = new CardInfo(transactionID, info.type, info.amount, info.serial, info.pin);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (CardCheckTask.checkOne(player, tcfInfo, null)) {
                            List<CardInfo> queue = TheCaoFast.getInstance().queue.get(player);
                            if (queue == null) {
                                queue = new ArrayList<>();
                            }
                            queue.add(tcfInfo);
                            if (TheCaoFast.getInstance().queue.containsKey(player))
                                TheCaoFast.getInstance().queue.replace(player, queue);
                            else TheCaoFast.getInstance().queue.put(player, queue);
                        }
                    }
                }.runTaskLaterAsynchronously(TheCaoFast.getInstance(), 20L);
            } else {
                unTriggerStep2(player);
                purgePlayer(player);
                player.sendMessage(msg.cancelled);
            }
        }
    }
}
