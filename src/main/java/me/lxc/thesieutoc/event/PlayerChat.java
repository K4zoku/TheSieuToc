package me.lxc.thesieutoc.event;

import com.google.gson.JsonObject;
import me.lxc.thesieutoc.TheSieuToc;
import me.lxc.thesieutoc.internal.Messages;
import me.lxc.thesieutoc.internal.Settings;
import me.lxc.thesieutoc.internal.Ui;
import me.lxc.thesieutoc.tasks.CardCheckTask;
import net.thesieutoc.TheSieuTocAPI;
import net.thesieutoc.data.CardInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static me.lxc.thesieutoc.handlers.InputCardHandler.*;

public class PlayerChat implements Listener {

    @EventHandler
    public void event(AsyncPlayerChatEvent e) {
        final Settings settings = TheSieuToc.getInstance().getSettings();
        final Messages msg = TheSieuToc.getInstance().getMessages();
        final Ui ui = TheSieuToc.getInstance().getUi();
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
                JsonObject sendCard = TheSieuTocAPI.sendCard(settings.iTheSieuTocKey, settings.iTheSieuTocSecret, info.type, info.amount, info.serial, info.pin);
                TheSieuToc.pluginDebug.debug(sendCard.toString());
                if (!sendCard.get("status").getAsString().equals("00")) {
                    player.sendMessage(msg.fail);
                    player.sendMessage(sendCard.get("msg").getAsString());
                    return;
                }
                String transactionID = sendCard.get("transaction_id").getAsString();
                CardInfo tstInfo = new CardInfo(transactionID, info.type, info.amount, info.serial, info.pin);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (CardCheckTask.checkOne(player, tstInfo, null)) {
                            List<CardInfo> queue = TheSieuToc.getInstance().queue.get(player);
                            if (queue == null) {
                                queue = new ArrayList<>();
                            }
                            queue.add(tstInfo);
                            if (TheSieuToc.getInstance().queue.containsKey(player))
                                TheSieuToc.getInstance().queue.replace(player, queue);
                            else TheSieuToc.getInstance().queue.put(player, queue);
                        }
                    }
                }.runTaskLaterAsynchronously(TheSieuToc.getInstance(), 20L);
            } else {
                unTriggerStep2(player);
                purgePlayer(player);
                player.sendMessage(msg.cancelled);
            }
            return;
        }

    }
}
