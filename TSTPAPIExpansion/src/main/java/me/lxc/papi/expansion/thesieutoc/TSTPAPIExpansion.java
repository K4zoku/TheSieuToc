package me.lxc.papi.expansion.thesieutoc;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public final class TSTPAPIExpansion extends PlaceholderExpansion {
    Plugin plugin;

    @Override
    public boolean canRegister() {
        return Bukkit.getPluginManager().getPlugin("TheSieuToc") != null || Bukkit.getPluginManager().isPluginEnabled("TheSieuToc");
    }

    @Override
    public boolean register() {
        this.plugin = canRegister() ? Bukkit.getPluginManager().getPlugin("TheSieuToc") : null;
        if (this.plugin == null) return false;
        else return super.register();
    }

    public String getIdentifier() {
        return "TST";
    }

    public String getAuthor() {
        return plugin.getDescription().getAuthors().get(0);
    }

    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {

        return super.onPlaceholderRequest(p, params);
    }
}
