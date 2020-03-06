package me.lxc.thesieutoc.internal;

import me.lxc.artxeapi.data.ArtxeYAML;
import me.lxc.artxeapi.utils.ArtxeTime;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Settings {
    private ArtxeYAML settingsYml;

    public String configVersion;

    public boolean debug;

    public String iTheSieuTocKey;
    public String iTheSieuTocSecret;

    public long cardCheckPeriod;

    public List<String> cardEnable;

    public File donorLogFile;

    public Settings(ArtxeYAML settingsYML) {
        this.settingsYml = settingsYML;
        reloadData();

    }

    public void reloadData() {
        settingsYml.reloadConfig();
        configVersion = settingsYml.getConfig().get("Config-Version").toString();

        debug = settingsYml.getConfig().getBoolean("Debug", false);

        iTheSieuTocKey = settingsYml.getConfig().get("TheSieuToc.API-Key").toString();
        iTheSieuTocSecret = settingsYml.getConfig().get("TheSieuToc.API-Secret").toString();

        cardCheckPeriod = ArtxeTime.toTick(settingsYml.getConfig().get("Card-Check-Period","10s"));

        cardEnable = settingsYml.getConfig().getStringList("Card-Enabled");
        if (cardEnable == null || cardEnable.isEmpty()) cardEnable = Arrays.asList("Viettel", "Vinaphone", "Mobifone", "Vietnamobile", "Vcoin", "Zing", "Gate");

        donorLogFile = new File(settingsYml.getConfig().get("Donor-Log-File").toString());
    }

    public ArtxeYAML yml() {
        return settingsYml;
    }
}

