package me.takahatashun.napthe.thesieutoc.internal;

import me.takahatashun.napthe.thesieutoc.NapThe;
import me.takahatashun.napthe.thesieutoc.data.PluginYAML;
import me.takahatashun.napthe.thesieutoc.utils.TimeUtils;

import java.io.File;
import java.util.List;

public class Settings {
    private PluginYAML config;

    public String ConfigVersion;

    public String Locale;

    public long Callback_Refresh;

    public String TheSieuToc_Secret;
    public String TheSieuToc_Key;

    public File LogFile;

    public List<String> Card_Enabled;

    public Settings(final PluginYAML config){
        this.config = config;
        load();
    }

    public void load(){
        ConfigVersion = config.get("Config-Version").toString();

        Locale = config.get("Settings.Locale", "VN").toString();

        Callback_Refresh = TimeUtils.toTick(config.get("Settings.Callback.Callback_Refresh", "5m").toString());

        TheSieuToc_Key = config.get("Settings.TheSieuTocAPI.Key", "").toString();
        TheSieuToc_Secret = config.get("Settings.TheSieuTocAPI.Secret", "").toString();


        LogFile = new File(config.get("Settings.Log-File", NapThe.getInstance().getDataFolder() + File.separator + "logs" + File.separator + "NapThe.log").toString());

        Card_Enabled = config.getStringList("Card-Enabled");
    }


}
