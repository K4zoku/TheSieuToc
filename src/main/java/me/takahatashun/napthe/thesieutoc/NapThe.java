package me.takahatashun.napthe.thesieutoc;

import me.takahatashun.napthe.thesieutoc.data.CardInfo;
import me.takahatashun.napthe.thesieutoc.data.PluginYAML;
import me.takahatashun.napthe.thesieutoc.internal.Commands;
import me.takahatashun.napthe.thesieutoc.internal.Settings;
import me.takahatashun.napthe.thesieutoc.tasks.CardCheck;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.NumberConversions;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public final class NapThe extends JavaPlugin {
    public static String PluginVersion;
    public static final String PREFIX = "§6[§b§lTheSieuToc§6] §r";

    private static final String CMD = "donate";
    private static final String CMD_DESCRIPTION = "Nạp thẻ";
    private static final String CMD_USAGE = "/<command>";
    private static final List<String> CMD_ALIASES = Arrays.asList("napthe", "nạpthẻ", "nạp_thẻ");

    public static List<String> cardList;

    private PluginYAML config;
    private Settings settings;
    private PluginYAML lang;
    private List<Integer> amountList;
    public HashMap<Player, List<CardInfo>> queue;

    public boolean hasAPIInfo;

    private static NapThe instance;

    public static NapThe getInstance(){
        return instance;
    }

    public PluginYAML getConfig() {
        return config;
    }

    public Settings getSettings() {
        return settings;
    }

    public PluginYAML getLanguage() {
        return lang;
    }

    public List<Integer> getAmountList(){
        return amountList;
    }

    public static void console(String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(PREFIX + msg);
    }

    @Override
    public void onEnable() {
        if(hookProtocolLib()) {
            preStartup();
            loadData();
            if (checkConfigVersion()) {
                setupCommands();
                lastStartup();
            } else {
                disableR("§cDisable due incompatible config version!");
            }

        } else {
            disableR("§cDisable due no ProtocolLib dependency!");
        }
    }

    private boolean hookProtocolLib(){
        Plugin protocolLib = Bukkit.getServer().getPluginManager().getPlugin("ProtocolLib");
        return  protocolLib != null;
    }

    private void preStartup() {
        PluginVersion = getDescription().getVersion();
        console("§b  ________            _____ _               ______");
        console("§b  /_  __/ /_  ___     / ___/(____  __  __   /_  ______  _____");
        console("§b   / / / __ \\/ _ \\    \\__ \\/ / _ \\/ / / /    / / / __ \\/ ___/");
        console("§b  / / / / / /  __/   ___/ / /  __/ /_/ /    / / / /_/ / /__");
        console("§b /_/ /_/ /_/\\___/   /____/_/\\___/\\__._/    /_/  \\____/\\___/");
        console("§b        §f| §bVersion: §6"+PluginVersion+" §f| §bAuthor: §6Takahata Shun §f|");
        instance = this;
        amountList = new ArrayList<>();
        queue = new HashMap<>();
    }

    private void setupCommands(){
        console("Registering commands...");
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

    public void loadData(){
        final String configFileName = "Config.yml";
        config = new PluginYAML(this, "", configFileName, getDataFolder().toString(), configFileName);
        config.saveDefault();
        config.reload();
        amountList = new ArrayList<>();
        for(String amountKey : config.getConfigurationSection("Card").getKeys(false)){
            int amount = NumberConversions.toInt(amountKey);
            amountList.add(amount);
        }
        settings = new Settings(config);
        String key = settings.TheSieuToc_Key.isEmpty() ? "§cNONE" : settings.TheSieuToc_Key;
        String secret = settings.TheSieuToc_Secret.isEmpty() ? "§cNONE" : settings.TheSieuToc_Secret;
        console("§9< §aKey: §f" + key + " §6| §aSecret: §f" + secret + " §9>");
        if(settings.TheSieuToc_Key.isEmpty() || settings.TheSieuToc_Secret.isEmpty()){
            console("§cMissing API information, could not use donate command!");
            this.hasAPIInfo = false;
        } else {
            this.hasAPIInfo = true;
        }

        cardList = settings.Card_Enabled;

        final String langFileName = MessageFormat.format("Messages_{0}.yml", settings.Locale);
        lang = new PluginYAML(this, "Languages", langFileName, getDataFolder() + "/Languages", langFileName);
        lang.saveDefault();
        lang.reload();
    }

    private boolean checkConfigVersion(){
        return config.get("Config-Version").toString().equals(PluginVersion) && lang.get("Config-Version").toString().equals(PluginVersion);
    }

    private void lastStartup(){
        new CardCheck(this);
    }

    private void disableR(String message){
        console(message);
        try {
            this.setEnabled(false);
        } catch (Exception ignore){
        }
    }

    public void addCardToQueue(Player player, String transactionID, String type, String serial, String pin, int amount){
        List<CardInfo> cardsQueue = new ArrayList<>();
        if(!queue.containsKey(player)){
            cardsQueue.add(new CardInfo(transactionID, type, serial, pin, amount));
            queue.put(player, cardsQueue);
        } else {
            if(queue.getOrDefault(player, cardsQueue) == cardsQueue){
                cardsQueue.add(new CardInfo(transactionID, type, serial, pin, amount));
                queue.put(player, cardsQueue);
            } else {
                cardsQueue = queue.get(player);
                cardsQueue.add(new CardInfo(transactionID, type, serial, pin, amount));
                queue.replace(player, cardsQueue);
            }
        }
    }

}
