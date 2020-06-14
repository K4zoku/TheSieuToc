package me.lxc.artxeapi.utils;

import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

public class ArtxeDebug {
    private static final String SUBPREFIX = "[Debug] ";
    private Plugin plugin;
    private boolean debugEnable;

    public ArtxeDebug (Plugin plugin, boolean debugEnable) {
        this.plugin = plugin;
        this.debugEnable = debugEnable;
    }

    public void debug(String message) {
        if(debugEnable) this.plugin.getLogger().log(Level.INFO, SUBPREFIX + message);
    }

    public void debug(String[] messages){
        if (debugEnable) {
            for (String message : messages) {
                debug(message);
            }
        }
    }

}
