package me.lxc.thesieutoc.internal;

import me.lxc.artxeapi.data.ArtxeYAML;
import me.lxc.artxeapi.utils.ArtxeTime;

import java.util.List;

public class Settings {
    private ArtxeYAML settings;

    public String ConfigVersion;

    public boolean Debug;

    public String TheSieuToc_Key;
    public String TheSieuToc_Secret;

    public long Card_Check_Period;

    public List<String> Card_Enable;

    public Settings(ArtxeYAML settingsYML){
        this.settings = settingsYML;
        reloadData();

    }

    public void reloadData(){
        settings.reloadConfig();
        ConfigVersion = settings.getConfig().get("Config-Version").toString();

        Debug = settings.getConfig().getBoolean("Debug", false);

        TheSieuToc_Key = settings.getConfig().get("TheSieuToc.API-Key").toString();
        TheSieuToc_Secret = settings.getConfig().get("TheSieuToc.API-Secret").toString();

        Card_Check_Period = ArtxeTime.toTick(settings.getConfig().get("Card-Check-Period","10s"));

    }
}

