package me.lxc.artxeapi.utils;

import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

public class ArtxeDebug {
    private static final String SUBPREFIX = " [Debug] ";
    private Plugin plugin;
    private boolean debug_enable;

    public ArtxeDebug (Plugin plugin, boolean debug_enable) {
        this.plugin = plugin;
        this.debug_enable = debug_enable;
    }

    public void debug(String message){
        if(debug_enable) this.plugin.getLogger().log(Level.INFO, SUBPREFIX + message);
    }

    public void debug(String[] messages){
        if (debug_enable) {
            for (String message : messages) {
                debug(message);
            }
        }
    }

}
