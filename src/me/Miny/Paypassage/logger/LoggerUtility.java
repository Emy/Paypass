package me.Miny.Paypassage.logger;

import me.Miny.Paypassage.Paypass;

/**
 *
 * @author ibhh
 */
public class LoggerUtility {

    private Paypass plugin;
    private enum Level {
        DEBUG, INFO, SEVERE, WARNING, ERROR; 
    }

    public LoggerUtility(Paypass plugin) {
        this.plugin = plugin;
    }

    public void Logger(String msg, Level) {
        try {
            if ((TYPE.equalsIgnoreCase("Warning")) || (TYPE.equalsIgnoreCase("Error"))) {
                System.err.println(PrefixConsole + TYPE + ": " + msg);
                if (this.config.debugfile) {
                    this.Loggerclass.log("Error: " + msg);
                }
                if (this.playerManager != null) {
                    this.playerManager.BroadcastconsoleMsg("BookShop.consolemsg", " Warning: " + msg);
                }
            } else if (TYPE.equalsIgnoreCase("Debug")) {
                if (this.config.debug) {
                    System.out.println(PrefixConsole + "Debug: " + msg);
                }
                if (this.config.debugfile) {
                    this.Loggerclass.log("Debug: " + msg);
                }
                if (this.playerManager != null) {
                    this.playerManager.BroadcastconsoleMsg("BookShop.consolemsg", " Debug: " + msg);
                }
            } else {
                if (this.playerManager != null) {
                    this.playerManager.BroadcastconsoleMsg("BookShop.consolemsg", msg);
                }
                System.out.println(PrefixConsole + msg);
                if (this.config.debugfile) {
                    this.Loggerclass.log(msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[BookShop] Error: Uncatch Exeption!");
            if (this.report != null) {
                this.report.report(3317, "Logger doesnt work", e.getMessage(), "BookShop", e);
            }
            try {
                MetricsHandler.Error += 1;
            } catch (Exception e1) {
            }
        }
    }

    public void PlayerLogger(Player p, String msg, String TYPE) {
        try {
            if (TYPE.equalsIgnoreCase("Error")) {
                if (this.config.UsePrefix) {
                    p.sendMessage(this.config.Prefix + Prefix + ChatColor.RED + "Error: " + this.config.Text + msg);
                    if (this.config.debugfile) {
                        this.Loggerclass.log("Player: " + p.getName() + " Error: " + msg);
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Error: " + this.config.Text + msg);
                    if (this.config.debugfile) {
                        this.Loggerclass.log("Player: " + p.getName() + " Error: " + msg);
                    }
                }
                if (this.playerManager != null) {
                    this.playerManager.BroadcastconsoleMsg("BookShop.gamemsg", "Player: " + p.getName() + " Error: " + msg);
                }
            } else {
                if (this.config.UsePrefix) {
                    p.sendMessage(this.config.Prefix + Prefix + this.config.Text + msg);
                    if (this.config.debugfile) {
                        this.Loggerclass.log("Player: " + p.getName() + " Msg: " + msg);
                    }
                } else {
                    p.sendMessage(this.config.Text + msg);
                    if (this.config.debugfile) {
                        this.Loggerclass.log("Player: " + p.getName() + " Msg: " + msg);
                    }
                }
                if (this.playerManager != null) {
                    this.playerManager.BroadcastconsoleMsg("BookShop.gamemsg", "Player: " + p.getName() + " " + msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[BookShop] Error: Uncatch Exeption!");
            this.report.report(3317, "PlayerLogger doesnt work", e.getMessage(), "BookShop", e);
            try {
                MetricsHandler.Error += 1;
            } catch (Exception e1) {
            }
        }
    }
}
