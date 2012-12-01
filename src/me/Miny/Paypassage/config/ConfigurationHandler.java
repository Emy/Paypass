package me.Miny.Paypassage.config;

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
    private LoggerUtility l;
    private Paypass plugin;

    public ConfigurationHandler(Paypass plugin) {
        this.plugin = plugin;
    }

    public YamlConfiguration getLanguage_config() {
        return language_config;
    }
    
    public FileConfiguration getConfig(){
        return plugin.getConfig();
    }

    public boolean onStart(){
        //loading main config
        try {
            plugin.getConfig().options().copyDefaults(true);
            plugin.saveConfig();
            plugin.reloadConfig();
            plugin.getLogger().config("Config loaded");
            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("Cannot create config!");
            e.printStackTrace();
        }
        return false;
    }
}
