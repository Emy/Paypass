package me.Miny.Paypassage.config;

import me.Miny.Paypassage.Paypass;
import me.Miny.Paypassage.logger.LoggerUtility;
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
        //loading language files
//        try {
//                this.config = new ConfigHandler(this);
//                this.config.loadConfigonStart();
//                if (!changeLanguage(getConfig().getString("language"))) {
//                    getConfig().set("language", "en");
//                    saveConfig();
//                    reloadConfig();
//                    Logger("Language changed to en because your selection wasnt found!", "Error");
//                    if (!changeLanguage(getConfig().getString("language"))) {
//                        getConfig().set("language", "de");
//                        saveConfig();
//                        reloadConfig();
//                        Logger("Language changed to de because your selection wasnt found!", "Error");
//                    }
//                }
//                Logger("Version: " + aktuelleVersion(), "Debug");
//            } catch (Exception e1) {
//                ex1 = e1;
//                Logger("Error on loading config: " + e1.getMessage(), "Error");
//                e1.printStackTrace();
//                Logger("Version: " + this.Version + " failed to enable!", "Error");
//            }
    }
}
