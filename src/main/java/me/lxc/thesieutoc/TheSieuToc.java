package me.lxc.thesieutoc;

import me.lxc.artxeapi.data.ArtxeYAML;
import me.lxc.artxeapi.utils.ArtxeDebug;
import me.lxc.artxeapi.utils.ArtxeTime;
import me.lxc.thesieutoc.internal.Commands;
import me.lxc.thesieutoc.internal.DonorLog;
import me.lxc.thesieutoc.internal.Settings;
import net.thesieutoc.data.CardInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import static me.lxc.artxeapi.utils.ArtxeChat.console;

public final class TheSieuToc extends JavaPlugin {
    public static String pluginVersion;
    public static ArtxeDebug artxeDebug;
    public static final String PREFIX = "§6[§b§lTheSieuToc§6] §r";

    private static final String CMD = "donate";
    private static final String CMD_DESCRIPTION = "Nạp thẻ";
    private static final String CMD_USAGE = "/<command>";
    private static final List<String> CMD_ALIASES = Arrays.asList("napthe", "nạpthẻ", "nạp_thẻ", "thesieutoc", "tst", "thẻsiêutốc", "thẻ_siêu_tốc");

    public static List<String> cardList;

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
        registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void preStartup() {
        pluginVersion = getDescription().getVersion();
        console("§b  ________            _____ _               ______");
        console("§b  /_  __/ /_  ___     / ___/(____  __  __   /_  ______  _____");
        console("§b   / / / __ \\/ _ \\    \\__ \\/ / _ \\/ / / /    / / / __ \\/ ___/");
        console("§b  / / / / / /  __/   ___/ / /  __/ /_/ /    / / / /_/ / /__");
        console("§b /_/ /_/ /_/\\___/   /____/_/\\___/\\__._/    /_/  \\____/\\___/");
        console("               §f| §bVersion: §6" + pluginVersion + " §f| §bAuthor: §6LXC §f|");
        console("            §f| §aCopyright (c) 2018-" + ArtxeTime.getCurrentYear() + " §bTheSieuToc §f|");
        instance = this;
        amountList = new ArrayList<>();
        queue = new HashMap<>();
    }

    public void loadData() {
        ArtxeYAML settingsYml = new ArtxeYAML(this, getDataFolder() + File.separator + "settings", "general.yml", "settings/general.yml");
        settingsYml.saveDefaultConfig();
        settings = new Settings(settingsYml);
        hasAPIInfo = !(settings.iTheSieuTocKey.isEmpty() && settings.iTheSieuTocSecret.isEmpty());
        donorLog = new DonorLog(settings);
        artxeDebug = new ArtxeDebug(this, settingsYml.getConfig().getBoolean("Debug", false));
        messages = new ArtxeYAML(this, getDataFolder() + File.separator + "languages", "messages.yml", "languages/messages.yml");
    }

    private void registerCommands() {
        try {
            final Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap) field.get(Bukkit.getServer());
            commandMap.register(CMD, new Commands(CMD, CMD_DESCRIPTION, CMD_USAGE, CMD_ALIASES));
            console("Commands has been registered");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            getLogger().log(Level.SEVERE, "Could not register command!", e);
        }
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

    public List<Integer> getAmountList() {
        return amountList;
    }
}
