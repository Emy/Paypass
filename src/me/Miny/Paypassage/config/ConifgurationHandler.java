package me.Miny.Paypassage.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author ibhh
 */
public class ConifgurationHandler {
    
    private static YamlConfiguration language_config;
    private static Logger l;
    
    public static boolean onStart(Plugin plugin){
        //loading main config
        try {
            plugin.getConfig().options().copyDefaults(true);
            plugin.saveConfig();
            plugin.reloadConfig();
            plugin.getLogger().config("Config loaded");
        } catch (Exception e) {
            plugin.getLogger().severe("Cannot create config!");
            e.printStackTrace();
            return false;
        }
        //loading language files
        try {
                this.config = new ConfigHandler(this);
                this.config.loadConfigonStart();
                if (!changeLanguage(getConfig().getString("language"))) {
                    getConfig().set("language", "en");
                    saveConfig();
                    reloadConfig();
                    Logger("Language changed to en because your selection wasnt found!", "Error");
                    if (!changeLanguage(getConfig().getString("language"))) {
                        getConfig().set("language", "de");
                        saveConfig();
                        reloadConfig();
                        Logger("Language changed to de because your selection wasnt found!", "Error");
                    }
                }
                Logger("Version: " + aktuelleVersion(), "Debug");
            } catch (Exception e1) {
                ex1 = e1;
                Logger("Error on loading config: " + e1.getMessage(), "Error");
                e1.printStackTrace();
                Logger("Version: " + this.Version + " failed to enable!", "Error");
            }
    }
}
