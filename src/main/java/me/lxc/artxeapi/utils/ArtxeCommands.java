package me.lxc.artxeapi.utils;

import me.lxc.thesieutoc.TheSieuToc;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ArtxeCommands {

    private ArtxeCommands() {}

    private static final String MATCH = "(?ium)^(player:|op:|console:|)(.*)$";

    public static void dispatchCommand(Player player, String command) {
        new BukkitRunnable() {
            @Override
            public void run() {
                final String cmd = command.replaceAll(MATCH, "$2").replaceAll("(?ium)([{]player[}])", player.getName());
                TheSieuToc.pluginDebug.debug(cmd);
                final String type = command.replaceAll(MATCH, "$1".replace(":","").toLowerCase());
                TheSieuToc.pluginDebug.debug(type);
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
                    case "console":
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                        break;
                    case "":
                    case "player":
                    default:
                        player.performCommand(cmd);
                        break;
                }
            }
        }.runTask(TheSieuToc.getInstance());
    }
}
