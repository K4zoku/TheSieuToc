package me.takahatashun.napthe.thesieutoc.data;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginAwareness;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;

import static org.bukkit.util.NumberConversions.toFloat;
import static org.bukkit.util.NumberConversions.toShort;

@SuppressWarnings("ALL")
public class PluginYAML extends FileConfiguration {

    private Plugin plugin;

    private String resourcePath = "";
    private String resourceName = "";

    private boolean resource = true;

    private String filePath = "";
    private String fileName = "";

    private FileConfiguration yml = null;

    public PluginYAML(Plugin plugin, String resourcePath, String resourceName, String filePath, String fileName){
        this.plugin = plugin;

        this.resourcePath = resourcePath;
        this.resourceName = resourceName;

        this.resource = true;

        this.filePath = filePath;
        this.fileName = fileName;

        this.reload();
    }

    public PluginYAML(Plugin plugin, String filePath, String fileName){
        this.plugin = plugin;

        this.resource = false;

        this.filePath = filePath;
        this.fileName = fileName;

        this.reload();
    }

    @Override
    public String saveToString() {
        try {
            return new Scanner(new File(filePath, fileName)).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            return resource ? new Scanner(this.plugin.getResource(resourcePath + File.pathSeparator + resourceName)).useDelimiter("\\Z").next() : "";
        }
    }

    @Override
    public void loadFromString(String contents) throws InvalidConfigurationException {
        yml.loadFromString(contents);
    }

    @Override
    protected String buildHeader() {
        return "";
    }

    public FileConfiguration getConfig(){
        if (this.yml == null) {
            reload();
        }
        return this.yml;
    }

    public void reload(){
        this.yml = YamlConfiguration.loadConfiguration(new File(this.filePath, this.fileName));

        final InputStream configStream = this.resource ? this.plugin.getResource(this.resourcePath + File.pathSeparator + this.resourceName) : null;

        if (configStream == null) {
            return;
        } else {
            final YamlConfiguration config;
            if (isStrictlyUTF8()) {
                config = YamlConfiguration.loadConfiguration(new InputStreamReader(configStream, Charsets.UTF_8));
            } else {
                final byte[] contents;
                config = new YamlConfiguration();
                try {
                    contents = ByteStreams.toByteArray(configStream);
                } catch (IOException ex) {
                    this.plugin.getLogger().log(Level.SEVERE, "Unexpected failure reading " + this.resourceName, ex);
                    return;
                }

                final String text = new String(contents, Charset.defaultCharset());
                if (!text.equals(new String(contents, Charsets.UTF_8))) {
                    this.plugin.getLogger().log(Level.SEVERE, "Default system encoding may have misread " + this.fileName + " from plugin jar");
                }

                try {
                    config.loadFromString(text);
                } catch (final InvalidConfigurationException ex) {
                    this.plugin.getLogger().log(Level.SEVERE, "Cannot load " + this.fileName + " from jar", ex);
                }
            }

            this.yml.setDefaults(config);
        }
    }

    public void saveDefault(File file){
        if(this.resource) {
            if(!file.exists()){
                try {
                    this.plugin.getLogger().log(Level.WARNING, file.getName() + " not found, copying defaults...");
                    if (file.createNewFile()){
                        InputStream content = this.plugin.getResource(resourcePath + File.pathSeparator + resourceName);
                        assert content != null;
                        byte[] buffer = new byte[content.available()];
                        content.read(buffer);
                        OutputStream out = new FileOutputStream(file);
                        out.write(buffer);
                    } else {
                        this.plugin.getLogger().log(Level.WARNING, "Could not create new file!");
                    }
                } catch (IOException ioExc) {
                    this.plugin.getLogger().log(Level.SEVERE, "Error while saving default", ioExc);
                }
            }
        }
    }

    public void saveDefault(String file){
        if(file.isEmpty()) return;
        else saveDefault(new File(file));
    }

    public void saveDefault(){
        File file = new File(filePath, fileName);
        if(!file.exists()) {
            this.plugin.getLogger().log(Level.WARNING, file.getName() + " not found, copying defaults...");
            this.plugin.saveResource((resourcePath.isEmpty() ? "" : (resourcePath + "/")) + resourceName, false);
        }
    }

    public void save(File file){
        try {
            getConfig().save(file);
        } catch (IOException ex) {
            this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + file.getName(), ex);
        }
    }

    public void save(String file){
        try {
            getConfig().save(file);
        }catch (IOException ex){
            this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + file, ex);
        }
    }

    public void set(String path, Object obj){
        getConfig().set(path, obj);
    }

    @SuppressWarnings("deprecation")
    private boolean isStrictlyUTF8() {
        return this.plugin.getDescription().getAwareness().contains(PluginAwareness.Flags.UTF8);
    }

    @Override
    public Object get(String path){
        return getConfig().get(path);
    }

    @Override
    public Object get(String path, Object def){
        return getConfig().get(path, def);
    }

    @Override
    public String getString(String path){
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString(path));
    }

    @Override
    public String getString(String path, String def){
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString(path, def));
    }

    @Override
    public boolean isString(String path){
        return getConfig().isString(path);
    }

    @Override
    public int getInt(String path){
        return getConfig().getInt(path);
    }

    @Override
    public int getInt(String path, int def){
        return getConfig().getInt(path, def);
    }

    @Override
    public boolean isInt(String path){
        return getConfig().isInt(path);
    }

    @Override
    public boolean getBoolean(String path){
        return getConfig().getBoolean(path);
    }

    @Override
    public boolean getBoolean(String path, boolean def){
        return getConfig().getBoolean(path, def);
    }

    @Override
    public boolean isBoolean(String path){
        return getConfig().isBoolean(path);
    }

    @Override
    public double getDouble(String path){
        return getConfig().getDouble(path);
    }

    @Override
    public double getDouble(String path, double def){
        return getConfig().getDouble(path, def);
    }

    @Override
    public boolean isDouble(String path){
        return getConfig().isDouble(path);
    }

    public float getFloat(String path){
        Object def = get(path);
        return getFloat(path, (def instanceof Float) ? toFloat(def) : 00.00F);
    }

    public float getFloat(String path, float def){
        Object val = get(path);
        return (val instanceof Float) ? toFloat(val) : def;
    }

    public boolean isFloat(String path){
        Object val = get(path);
        return (val instanceof Float);
    }

    @Override
    public long getLong(String path){
        return getConfig().getLong(path);
    }

    @Override
    public long getLong(String path, long def){
        return getConfig().getLong(path, def);
    }

    @Override
    public boolean isLong(String path){
        return getConfig().isLong(path);
    }

    public short getShort(String path){
        Object def = get(path);
        return getShort(path, (def instanceof Short) ? toShort(def) : 0);
    }

    public short getShort(String path, short def){
        Object val = get(path);
        return (val instanceof Short) ? toShort(val) : def;
    }

    public short getShort(String path, int def){
        Object val = get(path);
        return (val instanceof Short) ? toShort(val) : (short) def;
    }

    public boolean isShort(String path){
        Object val = get(path);
        return (val instanceof Short);
    }

    @Override
    public ConfigurationSection getConfigurationSection(String path){
        return getConfig().getConfigurationSection(path);
    }

    @Override
    public List<?> getList(String path){
        return getConfig().getList(path);
    }

    @Override
    public List<String> getStringList(String path){
        return getConfig().getStringList(path);
    }

    @Override
    public String toString(){
        return saveToString();
    }
}
