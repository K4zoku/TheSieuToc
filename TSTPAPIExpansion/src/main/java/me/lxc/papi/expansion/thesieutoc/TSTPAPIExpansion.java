package me.lxc.papi.expansion.thesieutoc;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.lxc.artxeapi.utils.ArtxeNumber;
import me.lxc.thesieutoc.TheSieuToc;
import me.lxc.thesieutoc.utils.CalculateTop;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;

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
        String[] paramsSplitted = params.toLowerCase().split("[_]");
        String label = paramsSplitted[0];
        String[] args = Arrays.copyOfRange(paramsSplitted, 1, paramsSplitted.length);
        String result = "";
        String name = p.getName();
        switch (label) {
            case "rank":
                if (args.length == 2) {
                    name = args[1];
                } else {
                    result = getRank(name, "total");
                    break;
                }
                switch (args[0]) {
                    case "day":
                    case "month":
                    case "year":
                    case "total":
                    case "":
                        result = getRank(name, args[0]);
                        break;
                }
                break;
            case "amount":
                if (args.length == 2) {
                    name = args[1];
                } else {
                    result = getAmount(name, "total");
                    break;
                }
                switch (args[0]) {
                    case "day":
                    case "month":
                    case "year":
                    case "total":
                    case "":
                        result = getAmount(name, args[0]);
                        break;
                }
                break;
            case "top":
                int top = ArtxeNumber.isInteger(args[0]) ? Integer.parseInt(args[0]) : 0;
                if (args.length == 1) {
                    result = getTop(top, "total");
                } else if (args.length == 2) {
                    switch (args[1]) {
                        case "day":
                        case "month":
                        case "year":
                        case "total":
                        case "":
                            result = getTop(top, args[1]);
                            break;
                    }
                }
                break;
        }
        return result.isEmpty() ? '%' + getIdentifier() + '_' + params + '%' : result;
    }

    private static String getTop(int t, String type) {
        String result = "";
        if (t >= 0) {
            try {
                Map<String, Integer> top = CalculateTop.execute(type);
                int i = 0;
                for (Map.Entry<String, Integer> entry : top.entrySet()) {
                    i++;
                    if (i == t) {
                        result = entry.getKey();
                        break;
                    }
                }
            } catch (Exception e) {
                TheSieuToc.getInstance().getLogger().log(Level.SEVERE, "An error occurred", e);
            }
        }
        return result;
    }

    private static String getRank(String name, String type) {
        String rank = "";
        try {
            Map<String, Integer> top = CalculateTop.execute(type);
            int i = 0;
            for (Map.Entry<String, Integer> entry : top.entrySet()) {
                i++;
                if (entry.getKey().equals(name)) break;
            }
            rank = i > 0 ? String.valueOf(i) : "";
        } catch (Exception e) {
            TheSieuToc.getInstance().getLogger().log(Level.SEVERE, "An error occurred", e);
        }
        return rank;
    }

    private static String getAmount(String name, String type) {
        String ammount = "";
        try {
            ammount = String.valueOf(CalculateTop.execute(type).getOrDefault(name, 0));
        } catch (Exception e) {
            TheSieuToc.getInstance().getLogger().log(Level.SEVERE, "An error occurred", e);
        }
        return ammount;
    }
}
