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

    public ConfigurationHandler(Paypass plugin) {
        this.plugin = plugin;
    }

    public YamlConfiguration getLanguage_config() {
        return language_config;
    }

    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    public boolean onStart() {
        //loading main config
        try {
            plugin.getConfig().options().copyDefaults(true);
            plugin.saveConfig();
            plugin.reloadConfig();
            plugin.getLoggerUtility().log("Config loaded", LoggerUtility.Level.DEBUG);
            return true;
        } catch (Exception e) {
            plugin.getLoggerUtility().log("Cannot create config!", LoggerUtility.Level.ERROR);
            e.printStackTrace();
        }
        File configl = new File("language_" + plugin.getConfig().getString("language") + ".yml");
        if (!configl.exists()) {
            createLanguageConfig();
        }
        return false;
    }

    private void createLanguageConfig() {
        for (int i = 0; i < 2; i++) {
            String a = "";
            if(i == 0){
                a = "de";
            } else {
                a = "en";
            }
            File configl = new File("language_" + a + ".yml");
            try {
                configl.createNewFile();
            } catch (IOException ex) {
                plugin.getLoggerUtility().log("Couldnt create new config file!", LoggerUtility.Level.ERROR);
            }
            language_config = YamlConfiguration.loadConfiguration(configl);
            if (i == 0) {
                language_config.addDefault("permission.error", "Wir haben ein Problem! Dies darfst Du nicht machen!");
            } else {
                language_config.addDefault("permission.error", "we have a problem! You musnt do this!");
            }
            try {
                language_config.save(configl);
            } catch (IOException ex) {
                plugin.getLoggerUtility().log("Couldnt save config of the repair section!", LoggerUtility.Level.ERROR);
            }
        }
    }
}
