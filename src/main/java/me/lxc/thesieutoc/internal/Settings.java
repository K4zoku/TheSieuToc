package me.lxc.thesieutoc.internal;

import me.lxc.artxeapi.data.ArtxeYAML;
import me.lxc.artxeapi.utils.ArtxeTime;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Settings extends IConfiguration {

    public boolean debug;

    public String iTheSieuTocKey;
    public String iTheSieuTocSecret;

    public long cardCheckPeriod;

    public List<String> cardEnable;

    public File donorLogFile;

    public Settings(ArtxeYAML settingsYML) {
        super(settingsYML);
    }

    @Override
    public void load() {
        debug = yaml.getConfig().getBoolean("Debug", false);
        iTheSieuTocKey = yaml.getConfig().get("TheSieuToc.API-Key").toString();
        iTheSieuTocSecret = yaml.getConfig().get("TheSieuToc.API-Secret").toString();
        cardCheckPeriod = ArtxeTime.toTick(yaml.getConfig().get("Card-Check-Period","10s"));
        cardEnable = yaml.getConfig().getStringList("Card-Enabled");
        if (cardEnable == null || cardEnable.isEmpty()) cardEnable = Arrays.asList("Viettel", "Vinaphone", "Mobifone", "Vietnamobile", "Vcoin", "Zing", "Gate");
        donorLogFile = new File(yaml.getConfig().get("Donor-Log-File").toString());
    }

    public ArtxeYAML yaml() {
        return yaml;
    }
}

