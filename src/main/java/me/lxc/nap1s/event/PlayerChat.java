package me.lxc.nap1s.event;

import com.google.gson.JsonObject;
import com.nap1s.Nap1sAPI;
import com.nap1s.data.CardInfo;
import me.lxc.nap1s.Nap1S;
import me.lxc.nap1s.internal.Messages;
import me.lxc.nap1s.internal.Settings;
import me.lxc.nap1s.internal.Ui;
import me.lxc.nap1s.tasks.CardCheckTask;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static me.lxc.nap1s.handlers.InputCardHandler.*;

public class PlayerChat implements Listener {

    @EventHandler
    public void event(AsyncPlayerChatEvent e) {
        final Settings settings = Nap1S.getInstance().getSettings();
        final Messages msg = Nap1S.getInstance().getMessages();
        final Ui ui = Nap1S.getInstance().getUi();
        final Player player = e.getPlayer();
        final String text = e.getMessage();

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
                JsonObject sendCard = Nap1sAPI.sendCard(settings.nap1sMethod, settings.iNap1sKey, settings.iNap1sSecret, info.type, info.amount, info.serial, info.pin);
                Nap1S.pluginDebug.debug("Response: " + (sendCard != null ? sendCard.toString() : "NULL"));
                if (sendCard == null || !sendCard.has("result")) {
                    return;
                }
                if (sendCard.get("result").getAsJsonObject().get("status").getAsInt() != 201) {
                    player.sendMessage(msg.fail);
                    player.sendMessage(sendCard.get("result").getAsJsonObject().get("msg").getAsString());
                    return;
                }
                String transactionID = sendCard.get("result").getAsJsonObject().get("transaction_id").getAsString();
                CardInfo tstInfo = new CardInfo(transactionID, info.type, info.amount, info.serial, info.pin);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            if (CardCheckTask.checkOne(player, tstInfo, null)) {
                                List<CardInfo> queue = Nap1S.getInstance().queue.get(player);
                                if (queue == null) {
                                    queue = new ArrayList<>();
                                }
                                queue.add(tstInfo);
                                if (Nap1S.getInstance().queue.containsKey(player))
                                    Nap1S.getInstance().queue.replace(player, queue);
                                else Nap1S.getInstance().queue.put(player, queue);
                            }
                        } catch (Exception e) {
                            player.sendMessage(msg.systemError);
                        }
                    }
                }.runTaskLaterAsynchronously(Nap1S.getInstance(), 20L);
            } else {
                unTriggerStep2(player);
                purgePlayer(player);
                player.sendMessage(msg.cancelled);
            }
        }

    }
}
