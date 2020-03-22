package me.lxc.thesieutoc.internal;

import me.lxc.artxeapi.data.ArtxeYAML;
import me.lxc.artxeapi.utils.ArtxeTime;
import org.bukkit.util.NumberConversions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Settings extends IConfiguration {

    public boolean debug;

    public String iTheSieuTocKey;
    public String iTheSieuTocSecret;

    public long cardCheckPeriod;

    public List<String> cardEnable;

    public List<Integer> amountList;

    public Settings(ArtxeYAML settingsYML) {
        super(settingsYML);
    }

    @Override
    public void load() {
        debug = yaml.getConfig().getBoolean("Debug", false);
        iTheSieuTocKey = yaml.getConfig().get("TheSieuToc.API-Key").toString();
        iTheSieuTocSecret = yaml.getConfig().get("TheSieuToc.API-Secret").toString();
        cardCheckPeriod = ArtxeTime.toTick(yaml.getConfig().get("Card-Check-Period","5m"));
        cardEnable = yaml.getConfig().getStringList("Card-Enabled");
        if (cardEnable == null || cardEnable.isEmpty()) cardEnable = Arrays.asList("Viettel", "Vinaphone", "Mobifone", "Vietnamobile", "Vcoin", "Zing", "Gate");
        loadAmountList();
    }

    private void loadAmountList() {
        amountList = new ArrayList<>();
        for (String amountKey : yaml.getConfig().getConfigurationSection("Card-Reward").getKeys(false)) {
            int amount = NumberConversions.toInt(amountKey);
            amountList.add(amount);
        }
    }

    public ArtxeYAML yaml() {
        return yaml;
    }
}

