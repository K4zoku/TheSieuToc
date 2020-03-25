package me.lxc.artxeapi.data;

import me.lxc.artxeapi.utils.ArtxeDebug;
import me.lxc.thesieutoc.TheSieuToc;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginAwareness;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;

import static com.google.common.io.ByteStreams.toByteArray;

public class ArtxeYAML {
    private Plugin plugin;
    private File file;
    private FileConfiguration config;
    private String internalPath;
    private static final ArtxeDebug DEBUG = new ArtxeDebug(TheSieuToc.getInstance(), true);
    public ArtxeYAML(Plugin plugin, String filepath, String filename, String internalPath) {
        this.plugin = plugin;
        this.file = new File(filepath, filename);
        if (internalPath.isEmpty()) {
            this.internalPath = filename;
        } else {
            this.internalPath = internalPath + File.separator + filename;
        }
        this.config = null;
        saveDefaultConfig();
    }

    public ArtxeYAML(String filepath, String filename) {
        this.file = new File(filepath, filename);
        this.internalPath = filename;
        this.config = null;
        saveDefaultConfig();
    }

    public File getFile() {
        return this.file;
    }

    public FileConfiguration getConfig() {
        if (this.config == null) {
            reloadConfig();
        }
        return this.config;
    }

    public void reloadConfig() {
        this.config = formatConfig(YamlConfiguration.loadConfiguration(file));

        final InputStream configStream = this.plugin.getResource(this.internalPath);
        if (configStream == null) {
            return;
        }

        final YamlConfiguration ymlConfig;
        if (isStrictlyUTF8()) {
            ymlConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(configStream, StandardCharsets.UTF_8));
        } else {
            final byte[] contents;
            ymlConfig = new YamlConfiguration();
            try {
                contents = toByteArray(configStream);
            } catch (final IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Unexpected failure reading " + this.file.getName());
                return;
            }

            final String text = new String(contents, Charset.defaultCharset());
            if (!text.equals(new String(contents, StandardCharsets.UTF_8))) {
                plugin.getLogger().log(Level.WARNING, "Default system encoding may have misread " + this.file.getName()+" from plugin jar");
            }

            try {
                ymlConfig.loadFromString(text);
            } catch (final InvalidConfigurationException e) {
                plugin.getLogger().log(Level.WARNING, "Cannot load " + (this.file != null ? this.file.getName() : "null") + " from jar");
            }
        }
        this.config.setDefaults(ymlConfig);
        try {
            this.config.load(this.file);
            formatConfig(this.config);
        } catch (FileNotFoundException e) {
            saveDefaultConfig();
            reloadConfig();
        } catch (InvalidConfigurationException | IOException e) {
            plugin.getLogger().log(Level.SEVERE, e.getClass().getSimpleName());
        }
    }
    public void saveDefaultConfig() {
        if(!(this.file.exists())){
            try {
                plugin.getLogger().log(Level.WARNING, this.file.getName() + " not found, copying defaults...");
                this.plugin.saveResource(this.internalPath, false);
            } catch (NullPointerException ex) {
                plugin.getLogger().log(Level.SEVERE, "File " + this.file.getName() + " in jar not found");
            }
        }
    }

    private static <T extends ConfigurationSection> T formatConfig(T section) {
        Set<String> keys = section.getKeys(true);
        for (String key : keys) {
            Object value = section.get(key);
            if (value instanceof String) {
                String stringValue = ChatColor.translateAlternateColorCodes('&', (String) value);
                section.set(key, stringValue);
            }
        }
        return section;
    }

    private boolean isStrictlyUTF8() {
        return this.plugin.getDescription().getAwareness().contains(Objects.requireNonNull(PluginAwareness.Flags.values()[0]));
    }
}

