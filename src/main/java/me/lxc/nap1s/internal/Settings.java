package me.lxc.nap1s.internal;

import me.lxc.artxeapi.data.ArtxeYAML;
import me.lxc.artxeapi.utils.ArtxeTime;
import org.bukkit.util.NumberConversions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Settings extends IConfiguration {

    public boolean debug;

    public String iNap1sKey;
    public String iNap1sSecret;
    public String nap1sMethod;

    public long cardCheckPeriod;

    public List<String> cardEnable;

    public List<Integer> amountList;

    public Settings(ArtxeYAML settingsYML) {
        super(settingsYML);
    }

    @Override
    public void load() {
        debug = yaml.getConfig().getBoolean("Debug", false);
        iNap1sKey = yaml.getConfig().get("Nap1s.API-Key").toString();
        iNap1sSecret = yaml.getConfig().get("Nap1s.API-Secret").toString();
        nap1sMethod = yaml.getConfig().get("Nap1s.Method", "SLOW").toString();
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

