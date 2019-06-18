package me.takahatashun.napthe.thesieutoc.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandUtils {

    private static final String MATCH = "(?ium)^(player:|op:|console:|)(.*)$";

    public static void dispatchCommand(Player player, String command){
        final String cmd = command.replaceAll(MATCH, "$2");
        final String type = command.replaceAll(MATCH, "$1".replace(":","").toLowerCase());
        switch (type){
            case "":
            case "player":
                player.performCommand(cmd);
                break;
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
        }

    }

}
