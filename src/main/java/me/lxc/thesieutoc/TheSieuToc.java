package me.lxc.thesieutoc;

import me.lxc.artxeapi.data.ArtxeYAML;
import me.lxc.artxeapi.utils.ArtxeChat;
import me.lxc.artxeapi.utils.ArtxeDebug;
import me.lxc.artxeapi.utils.ArtxeTime;
import me.lxc.thesieutoc.internal.DonorLog;
import me.lxc.thesieutoc.internal.Settings;
import net.thesieutoc.data.CardInfo;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public final class TheSieuToc extends JavaPlugin {
    public static String PluginVersion;
    public static ArtxeDebug PluginDebug;
    public static final String PREFIX = "§6[§b§lTheSieuToc§6] §r";

    private static final String CMD = "donate";
    private static final String CMD_DESCRIPTION = "Nạp thẻ";
    private static final String CMD_USAGE = "/<command>";
    private static final List<String> CMD_ALIASES = Arrays.asList("napthe", "nạpthẻ", "nạp_thẻ", "thesieutoc", "tst", "thẻsiêutốc", "thẻ_siêu_tốc");

    public static List<String> cardList;

    private ArtxeYAML settingsYml;
    private Settings settings;
    private DonorLog donorLog;
    private ArtxeYAML messages;
    private List<Integer> amountList;
    public HashMap<Player, List<CardInfo>> queue;

    public boolean hasAPIInfo;

    private static TheSieuToc instance;

    @Override
    public void onEnable() {
        preStartup();
        loadData();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void preStartup() {
        PluginVersion = getDescription().getVersion();
        ArtxeChat.console("§b  ________            _____ _               ______");
        ArtxeChat.console("§b  /_  __/ /_  ___     / ___/(____  __  __   /_  ______  _____");
        ArtxeChat.console("§b   / / / __ \\/ _ \\    \\__ \\/ / _ \\/ / / /    / / / __ \\/ ___/");
        ArtxeChat.console("§b  / / / / / /  __/   ___/ / /  __/ /_/ /    / / / /_/ / /__");
        ArtxeChat.console("§b /_/ /_/ /_/\\___/   /____/_/\\___/\\__._/    /_/  \\____/\\___/");
        ArtxeChat.console("               §f| §bVersion: §6" + PluginVersion + " §f| §bAuthor: §6LXC §f|");
        ArtxeChat.console("            §f| §aCopyright (c) 2018-" + ArtxeTime.getCurrentYear() + " §bTheSieuToc §f|");
        instance = this;
        amountList = new ArrayList<>();
        queue = new HashMap<>();
    }

    private void loadData() {
        settingsYml = new ArtxeYAML(this, getDataFolder() + File.separator + "settings", "general.yml", "settings/general.yml");
        settingsYml.saveDefaultConfig();
        settings = new Settings(settingsYml);
        donorLog = new DonorLog(settings);
        PluginDebug = new ArtxeDebug(this, settingsYml.getConfig().getBoolean("Debug", false));
    }


    public static TheSieuToc getInstance() {
        return instance;
    }

    public Settings getSettings() {
        return this.settings;
    }

    public ArtxeYAML getMessages() {
        return this.messages;
    }

    public DonorLog getDonorLog() {
        return this.donorLog;
    }
}
