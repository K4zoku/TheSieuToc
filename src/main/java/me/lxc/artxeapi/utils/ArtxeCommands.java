package me.lxc.artxeapi.utils;

import me.lxc.thecaofast.TheCaoFast;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.MessageFormat;

public class ArtxeCommands {

    private ArtxeCommands() {}

    private static final String MATCH = "(?ium)^(player:|op:|console:|)(.*)$";

    public static void dispatchCommand(Player player, String command) {
        new BukkitRunnable() {
            @Override
            public void run() {
                final String type = command.replaceAll(MATCH, "$1").replace(":","").toLowerCase();
                final String cmd = command.replaceAll(MATCH, "$2").replaceAll("(?ium)([{]Player[}])", player.getName());
                TheCaoFast.pluginDebug.debug(MessageFormat.format("Run command: '{'type: {0}, command: {1}'}'", type, cmd));
                switch (type){
                    case "op":
                        if(player.isOp()){
                            player.performCommand(cmd);
                        } else {
                            player.setOp(true);
                            player.performCommand(cmd);
                            player.setOp(false);
                        }
                        break;
                    case "":
                    case "player":
                        player.performCommand(cmd);
                        break;
                    case "console":
                    default:
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                        break;
                }
            }
        }.runTask(TheCaoFast.getInstance());
    }
}
