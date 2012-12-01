package me.Miny.Paypassage.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import me.Miny.Paypassage.Paypass;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author ibhh
 */
public class LoggerUtility {

    private Paypass plugin;
    private boolean debugfile;
    private boolean debug;
    private String Prefix;
    private boolean usePrefix;
    public ChatColor PrefixColor, TextColor;

    public enum Level {

        DEBUG, INFO, SEVERE, WARNING, ERROR;
    }

    public LoggerUtility(Paypass plugin) {
        this.plugin = plugin;
        debugfile = plugin.getConfigHandler().getConfig().getBoolean("debugfile");
        debug = plugin.getConfigHandler().getConfig().getBoolean("debug");
        Prefix = plugin.getConfigHandler().getConfig().getString("Prefix");
        usePrefix = plugin.getConfigHandler().getConfig().getBoolean("UsePrefix");
        loadcolors();
    }

    private void loadcolors() {
        PrefixColor = ChatColor.getByChar(plugin.getConfigHandler().getConfig().getString("PrefixColor"));
        TextColor = ChatColor.getByChar(plugin.getConfigHandler().getConfig().getString("TextColor"));
    }

    public void log(String msg, Level TYPE) {
        try {
            if ((TYPE == Level.WARNING) || (TYPE == Level.ERROR)) {
                System.err.println("[" + plugin.getName() + "]" + TYPE + ": " + msg);
                if (debugfile) {
                    this.log("Error: " + msg);
                }
            } else if (TYPE == Level.DEBUG) {
                if (debug) {
                    System.out.println(Prefix + "Debug: " + msg);
                }
                if (debugfile) {
                    this.log("Debug: " + msg);
                }
            } else {
                System.out.println(Prefix + msg);
                if (debugfile) {
                    this.log(msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[BookShop] Error: Uncatch Exeption!");
            if (plugin.getReportHandler() != null) {
                plugin.getReportHandler().report(3317, "Logger doesnt work", e.getMessage(), "BookShop", e);
            }
        }
    }

    public void log(Player p, String msg, Level TYPE) {
        try {
            if (TYPE == Level.ERROR) {
                if (usePrefix) {
                    p.sendMessage(PrefixColor + Prefix + ChatColor.RED + "Error: " + TextColor + msg);
                    if (debugfile) {
                        this.log("Player: " + p.getName() + " Error: " + msg);
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Error: " + TextColor + msg);
                    if (debugfile) {
                        this.log("Player: " + p.getName() + " Error: " + msg);
                    }
                }
            } else {
                if (usePrefix) {
                    p.sendMessage(PrefixColor + Prefix + TextColor + msg);
                    if (debugfile) {
                        this.log("Player: " + p.getName() + " Msg: " + msg);
                    }
                } else {
                    p.sendMessage(TextColor + msg);
                    if (debugfile) {
                        this.log("Player: " + p.getName() + " Msg: " + msg);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[BookShop] Error: Uncatch Exeption!");
            if (plugin.getReportHandler() != null) {
                plugin.getReportHandler().report(3317, "PlayerLogger doesnt work", e.getMessage(), "BookShop", e);
            }
        }
    }

    public void log(String in) {
        Date now = new Date();
        String Stream = now.toString();
        String path = plugin.getDataFolder().toString() + File.separator + "debugfiles" + File.separator;
        File directory = new File(path);
        directory.mkdirs();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd 'at' HH");
        File file = new File(path + "debug-" + ft.format(now) + ".txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
        try {
            // Create file
            FileWriter fstream = new FileWriter(file, true);
            PrintWriter out = new PrintWriter(fstream);
            out.println("[" + Stream + "] " + in);
            //Close the output stream
            out.close();
        } catch (Exception e) {//Catch exception if any
            System.out.println("Error: " + e.getMessage());
        }
    }
}
