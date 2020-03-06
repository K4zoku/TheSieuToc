package me.lxc.thesieutoc.internal;

import me.lxc.artxeapi.data.ArtxeYAML;
import me.lxc.artxeapi.utils.ArtxeTime;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Settings {
    private ArtxeYAML settings;

    public String configVersion;

    public boolean debug;

    public String iTheSieuTocKey;
    public String iTheSieuTocSecret;

    public long cardCheckPeriod;

    public List<String> cardEnable;

    public File donorLogFile;

    public Settings(ArtxeYAML settingsYML) {
        this.settings = settingsYML;
        reloadData();

    }

    public void reloadData() {
        settings.reloadConfig();
        configVersion = settings.getConfig().get("Config-Version").toString();

        debug = settings.getConfig().getBoolean("Debug", false);

        iTheSieuTocKey = settings.getConfig().get("TheSieuToc.API-Key").toString();
        iTheSieuTocSecret = settings.getConfig().get("TheSieuToc.API-Secret").toString();

        cardCheckPeriod = ArtxeTime.toTick(settings.getConfig().get("Card-Check-Period","10s"));

        cardEnable = settings.getConfig().getStringList("Card-Enabled");
        if (cardEnable == null || cardEnable.isEmpty()) cardEnable = Arrays.asList("Viettel", "Vinaphone", "Mobifone", "Vietnamobile", "Vcoin", "Zing", "Gate");

        donorLogFile = new File(settings.getConfig().get("Donor-Log-File").toString());
    }

    public ArtxeYAML yml() {
        return settings;
    }
}

