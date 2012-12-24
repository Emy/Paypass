package me.Miny.Paypassage.config;

import java.io.File;
import java.io.IOException;
import me.Miny.Paypassage.Paypassage;
import me.Miny.Paypassage.logger.LoggerUtility;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author ibhh
 */
public class ConfigurationHandler {

    private YamlConfiguration language_config;
    private Paypassage plugin;

    /**
     * Creates a new ConfigurationHandler
     * @param plugin Needed for saving configs
     */
    public ConfigurationHandler(Paypassage plugin) {
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
                //permission output
                language_config.addDefault("permission.error", "Wir haben ein Problem! Dies darfst Du nicht machen!");
                //privacy output
                language_config.addDefault("privacy.notification.1", "Dieses plugin speichert nutzerbezogene Daten in eine Datei");
                language_config.addDefault("privacy.notification.2", "\"/pp allowtracking\" um den Plugin dies zu erlauben");
                language_config.addDefault("privacy.notification.3", "\"/pp denytracking\" um deine Daten zu anonymisieren");
                language_config.addDefault("privacy.notification.denied", "Das Plugin speichert nun keine nutzerbezogene Daten mehr");
                language_config.addDefault("privacy.notification.allowed", "Das Plugin speichert deine Daten, wende Dich an einen Admin um diese z.B. loeschen zu lassen");
                language_config.addDefault("creation.sign.notification1", "Bitte mache einen rechtsklick auf das Schild");
                language_config.addDefault("creation.sign.notification2", "Schild akzeptiert");
                language_config.addDefault("creation.sign.notification3", "Du hast bereits ein Schild ausgewaehlt.");
                //reload command
                language_config.addDefault("commands.reload.name", "reload");
                language_config.addDefault("commands.reload.permission", "Paypassage.reload");
                language_config.addDefault("commands.reload.description", "Laedt das Plugin neu");
                language_config.addDefault("commands.reload.usage", "/pp reload");
                
                language_config.addDefault("commands.denytracking.name", "denytracking");
                language_config.addDefault("commands.denytracking.permission", "Paypassage.user");
                language_config.addDefault("commands.denytracking.description", "Zwingt das Plugin deine Daten zu anonymisieren");
                language_config.addDefault("commands.denytracking.usage", "/pp denytracking");
                
                language_config.addDefault("commands.allowtracking.name", "allowtracking");
                language_config.addDefault("commands.allowtracking.permission", "Paypassage.user");
                language_config.addDefault("commands.allowtracking.description", "Erlaubt dem Plugin deine Daten zu speichern");
                language_config.addDefault("commands.allowtracking.usage", "/pp allowtracking");
                
                language_config.addDefault("commands.create.name", "create");
                language_config.addDefault("commands.create.permission", "Paypassage.create");
                language_config.addDefault("commands.create.description", "Erstellt ein neues Paypassage Schild");
                language_config.addDefault("commands.create.usage", "/pp create");
                
            } else {
                language_config.addDefault("permission.error", "we have a problem! You musnt do this!");
                language_config.addDefault("privacy.notification.1", "this plugin saves your interact events to a log");
                language_config.addDefault("privacy.notification.2", "\"/pp allowtracking\" to allow the plugin to save your data");
                language_config.addDefault("privacy.notification.3", "\"/pp denytracking\" to anonymise your data");
                language_config.addDefault("privacy.notification.denied", "The plugin anonymises your data now");
                language_config.addDefault("privacy.notification.allowed", "The plugin saves your data now, to delete the data, please tell an admin");
                language_config.addDefault("creation.sign.notification1", "Please do a right-click on a sign");
                language_config.addDefault("creation.sign.notification2", "Sign accepted");
                language_config.addDefault("creation.sign.notification3", "You have already choosen a sign.");
                //reload command
                language_config.addDefault("commands.reload.name", "reload");
                language_config.addDefault("commands.reload.permission", "Paypassage.reload");
                language_config.addDefault("commands.reload.description", "Reloads the plugin");
                language_config.addDefault("commands.reload.usage", "/pp reload");
                
                language_config.addDefault("commands.denytracking.name", "denytracking");
                language_config.addDefault("commands.denytracking.permission", "Paypassage.user");
                language_config.addDefault("commands.denytracking.description", "forces the plugin to anonymise your data");
                language_config.addDefault("commands.denytracking.usage", "/pp denytracking");
                
                language_config.addDefault("commands.allowtracking.name", "allowtracking");
                language_config.addDefault("commands.allowtracking.permission", "Paypassage.user");
                language_config.addDefault("commands.allowtracking.description", "Allows the plugin to save userdata");
                language_config.addDefault("commands.allowtracking.usage", "/pp allowtracking");
                
                language_config.addDefault("commands.create.name", "create");
                language_config.addDefault("commands.create.permission", "Paypassage.create");
                language_config.addDefault("commands.create.description", "Creates a new Paypassage sign");
                language_config.addDefault("commands.create.usage", "/pp create");
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
