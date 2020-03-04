package me.lxc.artxeapi.data;

import com.google.common.base.Charsets;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.charset.Charset;
import java.util.logging.Level;

import static com.google.common.io.ByteStreams.toByteArray;
import static org.bukkit.plugin.PluginAwareness.Flags.UTF8;

public class ArtxeYAML {
    private Plugin plugin;
    private File file;
    private FileConfiguration config;
    private String internalPath;

    public ArtxeYAML(Plugin plugin, String filepath, String filename, String internalPath) {
        this.plugin = plugin;
        this.file = new File(filepath, filename);
        if (internalPath.isEmpty()) {
            this.internalPath = filename;
        } else {
            this.internalPath = internalPath + File.separator + filename;
        }
        this.config = null;
    }

    public ArtxeYAML(String filepath, String filename) {
        this.file = new File(filepath, filename);
        this.internalPath = filename;
        this.config = null;
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
        this.config = YamlConfiguration.loadConfiguration(file);

        final InputStream ConfigStream = this.plugin.getResource(this.internalPath);
        if (ConfigStream == null) {
            return;
        }

        final YamlConfiguration Config;
        if (isStrictlyUTF8()) {
            Config = YamlConfiguration.loadConfiguration(new InputStreamReader(ConfigStream, Charsets.UTF_8));
        } else {
            final byte[] contents;
            Config = new YamlConfiguration();
            try {
                contents = toByteArray(ConfigStream);
            } catch (final IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Unexpected failure reading " + this.file.getName());
                return;
            }

            final String text = new String(contents, Charset.defaultCharset());
            if (!text.equals(new String(contents, Charsets.UTF_8))) {
                plugin.getLogger().log(Level.WARNING, "Default system encoding may have misread " + this.file.getName()+" from plugin jar");
            }

            try {
                Config.loadFromString(text);
            } catch (final InvalidConfigurationException e) {
                plugin.getLogger().log(Level.WARNING, "Cannot load " + this.file.getName() + " from jar");
            }
        }
        this.config.setDefaults(Config);
        try {
            this.config.load(this.file);
        } catch (FileNotFoundException e) {
            this.saveDefaultConfig();
        } catch (InvalidConfigurationException | IOException e) {
            plugin.getLogger().log(Level.SEVERE, e.getClass().getSimpleName());
        }
    }
    public void saveDefaultConfig() {
        if(this.file == null || !(this.file.exists())){
            try {
                plugin.getLogger().log(Level.WARNING, this.file.getName() + " not found, copying defaults...");
                this.plugin.saveResource(this.internalPath, false);
            } catch (NullPointerException ex) {
                plugin.getLogger().log(Level.SEVERE, "File " + this.file.getName() + " in jar not found");
            }
        }
    }

    @SuppressWarnings("deprecation")
    private boolean isStrictlyUTF8() {
        return this.plugin.getDescription().getAwareness().contains(UTF8);
    }
}

