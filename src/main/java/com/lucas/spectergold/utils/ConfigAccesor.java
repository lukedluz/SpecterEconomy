package com.lucas.spectergold.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

public class ConfigAccesor {
    private final String fileName;
    private final JavaPlugin plugin;
    private File configFile;
    private FileConfiguration fileConfiguration;

    @SuppressWarnings("deprecation")
    public ConfigAccesor(JavaPlugin plugin, String fileName) {
        if (plugin == null)
            throw new IllegalArgumentException("plugin cannot be null");
        if (!plugin.isEnabled())
            throw new IllegalArgumentException("plugin must be initialized");
        this.plugin = plugin;
        this.fileName = fileName;
        File dataFolder = plugin.getDataFolder();
        if (dataFolder == null)
            throw new IllegalStateException();
        this.configFile = new File(plugin.getDataFolder(), fileName);
    }

    public void reloadConfig() {
        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defConfigStream = this.plugin.getResource(this.fileName);
        if (defConfigStream != null) {
            /*@SuppressWarnings("deprecation")
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            this.fileConfiguration.setDefaults(defConfig);*/
        }
    }

    public FileConfiguration getConfig() {
        if (this.fileConfiguration == null) {
            reloadConfig();
        }
        return this.fileConfiguration;
    }

    public void saveConfig() {
        if ((this.fileConfiguration == null) || (this.configFile == null))
            return;
        try {
            getConfig().save(this.configFile);
        } catch (IOException ex) {
            this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, ex);
        }
    }

    public void saveDefaultConfig() {
        if (!this.configFile.exists())
            this.plugin.saveResource(this.fileName, false);
    }
}