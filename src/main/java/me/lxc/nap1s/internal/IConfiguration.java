package me.lxc.nap1s.internal;

import me.lxc.artxeapi.data.ArtxeYAML;
import me.lxc.nap1s.Nap1S;

import java.util.List;

public class IConfiguration {
    protected String configVersion;
    protected ArtxeYAML yaml;

    public IConfiguration(ArtxeYAML yaml){
        this.yaml = yaml;
        this.configVersion = yaml.getConfig().get("Config-Version").toString();
        load();
    }

    public void load() {}

    public void reload() {
        yaml.reloadConfig();
        load();
    }

    protected String getString(String path, String def) {
        return configVersion.equals(Nap1S.pluginVersion) ? this.yaml.getConfig().getString(path, def) : def;
    }

    protected List<String> getStringList(String path, List<String> def) {
        return configVersion.equals(Nap1S.pluginVersion) ? this.yaml.getConfig().getStringList(path) : def;
    }
}
