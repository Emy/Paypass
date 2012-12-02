package me.Miny.Paypassage.config;

import java.io.File;
import java.io.IOException;
import me.Miny.Paypassage.Paypass;
import me.Miny.Paypassage.logger.LoggerUtility;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author ibhh
 */
public class ConfigurationHandler {

    private YamlConfiguration language_config;
    private Paypass plugin;

    /**
     * Creates a new ConfigurationHandler
     * @param plugin Needed for saving configs
     */
    public ConfigurationHandler(Paypass plugin) {
        this.plugin = plugin;
    }

    /**
     * Returns the current language configuration
     * @return YamlConfiguration
     */
    public YamlConfiguration getLanguage_config() {
        return language_config;
    }

    
    /**
     * 
     * @return plugin.getConifg();
     */
    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    /**
     * Called on start
     * @return true if config was successfully loaded, false if it failed;
     */
    public boolean onStart() {
        //loading main config
        try {
            plugin.getConfig().options().copyDefaults(true);
            plugin.saveConfig();
            plugin.reloadConfig();
            plugin.getLoggerUtility().log("Config loaded", LoggerUtility.Level.DEBUG);
        } catch (Exception e) {
            plugin.getLoggerUtility().log("Cannot create config!", LoggerUtility.Level.ERROR);
            e.printStackTrace();
            plugin.onDisable();
        }
        createLanguageConfig();
        return true;
    }

    /**
     * Creates the language config and added defaults
     */
    private void createLanguageConfig() {
        for (int i = 0; i < 2; i++) {
            String a = "";
            if (i == 0) {
                a = "de";
            } else {
                a = "en";
            }
            File folder = new File(plugin.getDataFolder() + File.separator);
            folder.mkdirs();
            File configl = new File(plugin.getDataFolder() + File.separator + "language_" + a + ".yml");
            if (!configl.exists()) {
                try {
                    configl.createNewFile();
                } catch (IOException ex) {
                    plugin.getLoggerUtility().log("Couldnt create new config file!", LoggerUtility.Level.ERROR);
                }
            }
            language_config = YamlConfiguration.loadConfiguration(configl);
            if (i == 0) {
                language_config.addDefault("permission.error", "Wir haben ein Problem! Dies darfst Du nicht machen!");
            } else {
                language_config.addDefault("permission.error", "we have a problem! You musnt do this!");
            }
            try {
                language_config.options().copyDefaults(true);
                language_config.save(configl);
            } catch (IOException ex) {
                ex.printStackTrace();
                plugin.getLoggerUtility().log("Couldnt save language config!", LoggerUtility.Level.ERROR);
            }
        }
        File configl = new File(plugin.getDataFolder() + File.separator + "language_" + plugin.getConfig().getString("language") + ".yml");
        try {
            language_config = YamlConfiguration.loadConfiguration(configl);
        } catch (Exception e) {
            e.printStackTrace();
            plugin.getLoggerUtility().log("Couldnt load language config!", LoggerUtility.Level.ERROR);
            plugin.getConfig().set("language", "en");
            plugin.saveConfig();
            plugin.onDisable();
            return;
        }
        plugin.getLoggerUtility().log("language config loaded", LoggerUtility.Level.DEBUG);
    }
}
