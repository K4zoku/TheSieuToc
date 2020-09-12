package me.lxc.thecaofast;

import me.lxc.artxeapi.data.ArtxeYAML;
import me.lxc.artxeapi.utils.ArtxeDebug;
import me.lxc.artxeapi.utils.ArtxeTime;
import me.lxc.thecaofast.event.PlayerChat;
import me.lxc.thecaofast.internal.*;
import me.lxc.thecaofast.tasks.CardCheckTask;
import net.thecaofast.data.CardInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

import static me.lxc.artxeapi.utils.ArtxeChat.console;

public final class TheCaoFast extends JavaPlugin {
    public static String pluginVersion;
    public static ArtxeDebug pluginDebug;

    private static final String CMD = "donate";
    private static final String CMD_DESCRIPTION = "Nạp thẻ";
    private static final String CMD_USAGE = "/<command>";
    private static final List<String> CMD_ALIASES = Arrays.asList("napthe", "nạpthẻ", "nạp_thẻ", "thecaofast", "tcf");

    private Settings settings;
    private DonorLog donorLog;
    private Messages messages;
    private Ui ui;
    public HashMap<Player, List<CardInfo>> queue;

    public boolean hasAPIInfo;
    public CardCheckTask cardCheckTask;
    private static TheCaoFast instance;

    @Override
    public void onEnable() {
        preStartup();
        loadData();
        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        // Nothing Here?
    }

    private void preStartup() {
        pluginVersion = getDescription().getVersion();
        console("               §f| §bVersion: §6" + pluginVersion + " §f| §bAuthor: §6LXC §f|");
        console("            §f| §aCopyright (c) 2018-" + ArtxeTime.getCurrentYear() + " §bTheCaoFast §f|");
        instance = this;
        queue = new HashMap<>();
    }

    public void loadData() {
        settings = new Settings(new ArtxeYAML(this, getDataFolder() + File.separator + "settings", "general.yml", "settings"));
        hasAPIInfo = !(settings.iTheCaoFastKey.isEmpty() && settings.iTheCaoFastSecret.isEmpty());
        donorLog = new DonorLog(new File(getDataFolder() + File.separator + "logs", "donation.log"));
        pluginDebug = new ArtxeDebug(this, settings.debug);
        messages = new Messages(new ArtxeYAML(this, getDataFolder() + File.separator + "languages", "messages.yml", "languages"));
        ui = new Ui(new ArtxeYAML(this, getDataFolder() + File.separator + "ui", "chat.yml", "ui"));
        cardCheckTask = new CardCheckTask(this);
    }

    public void reload(short type) {
        switch (type) {
            case 1: settings.reload(); break;
            case 2: messages.reload(); break;
            case 3: ui.reload(); break;
            default:
                settings.reload();
                hasAPIInfo = !(settings.iTheCaoFastKey.isEmpty() && settings.iTheCaoFastSecret.isEmpty());
                pluginDebug = new ArtxeDebug(this, settings.debug);
                messages.reload();
                ui.reload();
                break;
        }
    }

    private void registerCommands() {
        try {
            final Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap) field.get(Bukkit.getServer());
            commandMap.register(CMD, new Commands(CMD, CMD_DESCRIPTION, CMD_USAGE, CMD_ALIASES));
            getLogger().log(Level.INFO, "Commands has been registered");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            getLogger().log(Level.SEVERE, "Could not register command!", e);
        }
    }

    private void registerListeners() {
        PluginManager bkplm = Bukkit.getPluginManager();
        bkplm.registerEvents(new PlayerChat(), this);
    }

    public static TheCaoFast getInstance() {
        return instance;
    }

    public Settings getSettings() {
        return this.settings;
    }

    public Messages getMessages() {
        return this.messages;
    }

    public Ui getUi() {
        return this.ui;
    }

    public DonorLog getDonorLog() {
        if (donorLog.logFile != null && Objects.requireNonNull(donorLog.logFile).exists())
            return this.donorLog;
        else {
            donorLog.createFile();
            return this.donorLog;
        }
    }
}
