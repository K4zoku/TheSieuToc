package me.lxc.artxeapi.utils;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public final class ArtxeChat {

    public static TextComponent getTextComponent(HoverEvent.Action hover, BaseComponent[] hoverValue, ClickEvent.Action click, String clickValue, String text){
        TextComponent tc = new TextComponent(ChatColor.translateAlternateColorCodes('&', text));
        if(click != null && !clickValue.isEmpty()) {
            tc.setClickEvent(new ClickEvent(click, clickValue));
        }
        if(hover != null && hoverValue != null) {
            tc.setHoverEvent(new HoverEvent(hover, hoverValue));
        }
        return tc;
    }

    public static String toggleCase(String text) {
        char[] b = text.toCharArray();
        for (int i = 0; i < b.length; i++) {
            b[i] = Character.isUpperCase(b[i]) ? Character.toLowerCase(b[i]) : Character.toUpperCase(b[i]);
        }
        return new String(b);
    }

    public static void console(String text){
        Bukkit.getServer().getConsoleSender().sendMessage(text);
    }

}
