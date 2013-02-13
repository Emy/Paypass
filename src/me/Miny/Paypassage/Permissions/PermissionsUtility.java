package me.Miny.Paypassage.Permissions;

import me.Miny.Paypassage.Paypassage;
import me.Miny.Paypassage.logger.LoggerUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PermissionsUtility {

    private Paypassage plugin;
    public int PermPlugin = 0;

    public PermissionsUtility(Paypassage pl) {
        this.plugin = pl;

        plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                try {
                    plugin.getLoggerUtility().log("checking PermissionsPlugin!", LoggerUtility.Level.DEBUG);
                    searchpermplugin();
                } catch (Exception e) {
                    plugin.getReportHandler().report(3324, "Checking Permissions plugin failed", e.getMessage(), "PermissionsChecker", e);
                }
            }
        }, 0);
    }

    public void searchpermplugin() {
        if (plugin.getServer().getPluginManager().isPluginEnabled("PermissionsEx")) {
            PermPlugin = 2;
            plugin.getLoggerUtility().log("Permissions: Hooked into PermissionsEX!", LoggerUtility.Level.DEBUG);
            return;
        }
        if (plugin.getServer().getPluginManager().isPluginEnabled("GroupManager")) {
            PermPlugin = 3;
            plugin.getLoggerUtility().log("Permissions: Hooked into GroupManager!", LoggerUtility.Level.DEBUG);
            return;
        }
        if (plugin.getServer().getPluginManager().isPluginEnabled("bPermissions")) {
            PermPlugin = 4;
            plugin.getLoggerUtility().log("Permissions: Hooked into bPermissions!", LoggerUtility.Level.DEBUG);
            return;
        }
        PermPlugin = 1;
    }

    public boolean checkpermissionssilent(Player player, String action) {
        try {
            if (player.isOp()) {
                return true;
            }
            if (PermPlugin == 1) {
                try {
                    return player.hasPermission(action) || player.hasPermission(action.toLowerCase());
                } catch (Exception e) {
                    e.printStackTrace();
                    plugin.getReportHandler().report(3325, "Couldnt check permission with BukkitPermission", e.getMessage(), "PermissionsChecker", e);
                    return false;
                }
            } else if (PermPlugin == 2) {
                if (!Bukkit.getPluginManager().isPluginEnabled("PermissionsEx")) {
                    return false;
                }
                try {
                    PermissionUser user = PermissionsEx.getUser(player);;
                    if (user.has(action)) {
                        return true;
                    }
                    return false;
                } catch (Exception e) {
                    if (plugin.getConfig().getBoolean("debug")) {
                        e.printStackTrace();
                    }
                    try {
                        PermissionManager permissions = PermissionsEx.getPermissionManager();
                        if (permissions.has(player, action)) {
                            return true;
                        }
                        return false;
                    } catch (Exception e1) {
                        e.printStackTrace();
                        plugin.getLoggerUtility().log("Error on checking Permission with PermissionsEx!", LoggerUtility.Level.ERROR);
                        plugin.getLoggerUtility().log("May the /reload command caused this issue!", LoggerUtility.Level.ERROR);
                        plugin.getLoggerUtility().log("May your permissions.yml is wrong, please check it!", LoggerUtility.Level.ERROR);
                        plugin.getLoggerUtility().log("------------", LoggerUtility.Level.ERROR);
                        plugin.getLoggerUtility().log("If you mean this is an error, use /" + plugin.getName() + " report myissueisthis", LoggerUtility.Level.ERROR);
                        plugin.getLoggerUtility().log("------------", LoggerUtility.Level.ERROR);
                        return false;
                    }
                }
            } else if (PermPlugin == 3) {
                if (!Bukkit.getPluginManager().isPluginEnabled("GroupManager")) {
                    return false;
                }
                try {
                    return player.hasPermission(action) || player.hasPermission(action.toLowerCase());
                } catch (Exception e) {
                    e.printStackTrace();
                    plugin.getReportHandler().report(3327, "Couldnt check permission with GroupManager", e.getMessage(), "PermissionsChecker", e);
                    return false;
                }
            } else if (PermPlugin == 4) {
                if (!Bukkit.getPluginManager().isPluginEnabled("bPermissions")) {
                    return false;
                }
                try {
                    if (player.hasPermission(action) || player.hasPermission(action.toLowerCase())) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    plugin.getReportHandler().report(3327, "Couldnt check permission with bPermissions", e.getMessage(), "PermissionsChecker", e);
                    return false;
                }
            } else {
                System.out.println("PermissionsEx plugin are not found.");
                return false;
            }
        } catch (Exception e) {
            plugin.getLoggerUtility().log("Error on checking permissions!", LoggerUtility.Level.ERROR);
            plugin.getReportHandler().report(3328, "Error on checking permissions", e.getMessage(), "PermissionsChecker", e);
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkpermissions(Player player, String action) {
        try {
            if (player.isOp()) {
                return true;
            }
            if (PermPlugin == 1) {
                try {
                    if (player.hasPermission(action) || player.hasPermission(action.toLowerCase())) {
                        return true;
                    } else {
                        sendErrorMessage(player, action);
                        return false;
                    }
                } catch (Exception e) {
                    sendGeneralErrorMessage(player, e);
                    e.printStackTrace();
                    return false;
                }
            } else if (PermPlugin == 2) {
                if (!Bukkit.getPluginManager().isPluginEnabled("PermissionsEx")) {
                    sendErrorMessagePluginNotEnabled(player);
                    return false;
                }
                try {
                    PermissionUser user = PermissionsEx.getUser(player);
                    if (user.has(action)) {
                        return true;
                    } else {
                        sendErrorMessage(player, action);
                        return false;
                    }
                } catch (Exception e) {
                    if (plugin.getConfig().getBoolean("debug")) {
                        e.printStackTrace();
                    }
                    try {
                        PermissionManager permissions = PermissionsEx.getPermissionManager();

                        if (permissions.has(player, action)) {
                            return true;
                        } else {
                            sendErrorMessage(player, action);
                            return false;
                        }
                    } catch (Exception e1) {
                        plugin.getLoggerUtility().log(player, "Error on checking Permission with PermissionsEx! Please inform an Admin!", LoggerUtility.Level.ERROR);
                        plugin.getLoggerUtility().log("Error on checking Permission with PermissionsEx!", LoggerUtility.Level.ERROR);
                        plugin.getLoggerUtility().log("May the /reload command caused this issue!", LoggerUtility.Level.ERROR);
                        plugin.getLoggerUtility().log("May your permissions.yml is wrong, please check it!", LoggerUtility.Level.ERROR);
                        plugin.getLoggerUtility().log("------------", LoggerUtility.Level.ERROR);
                        plugin.getLoggerUtility().log("If you mean this is an error, use /" + plugin.getName() + " report myissueisthis", LoggerUtility.Level.ERROR);
                        plugin.getLoggerUtility().log("------------", LoggerUtility.Level.ERROR);
                        e.printStackTrace();
                        return false;
                    }
                }
            } else if (PermPlugin == 3) {
                if (!Bukkit.getPluginManager().isPluginEnabled("GroupManager")) {
                    sendErrorMessagePluginNotEnabled(player);
                    return false;
                }
                try {
                    if (player.hasPermission(action) || player.hasPermission(action.toLowerCase())) {
                        return true;
                    } else {
                        sendErrorMessage(player, action);
                        return false;
                    }
                } catch (Exception e) {
                    sendGeneralErrorMessage(player, e);
                    e.printStackTrace();
                    return false;
                }
            } else if (PermPlugin == 4) {
                if (!Bukkit.getPluginManager().isPluginEnabled("bPermissions")) {
                    sendErrorMessagePluginNotEnabled(player);
                    return false;
                }
                try {
                    if (player.hasPermission(action) || player.hasPermission(action.toLowerCase())) {
                        return true;
                    } else {
                        sendErrorMessage(player, action);
                        return false;
                    }
                } catch (Exception e) {
                    sendGeneralErrorMessage(player, e);
                    e.printStackTrace();
                    return false;
                }
            } else {
                sendErrorMessage(player, action);
                System.out.println("PermissionsEx plugin are not found.");
                return false;
            }
        } catch (Exception e) {
            sendGeneralErrorMessage(player, e);
            e.printStackTrace();
            return false;
        }
    }

    private void sendErrorMessage(Player player, String action) {
        plugin.getLoggerUtility().log(player, player.getName() + " " + plugin.getConfigHandler().getLanguage_config().getString("permission.error") + " (" + action + ")", LoggerUtility.Level.ERROR);
    }

    private void sendErrorMessagePluginNotEnabled(Player player) {
        switch (PermPlugin) {
            default:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                plugin.getLoggerUtility().log(player, "GroupManager is not enabled!", LoggerUtility.Level.ERROR);
                break;
            case 4:
                plugin.getLoggerUtility().log(player, "bPermissions is not enabled!", LoggerUtility.Level.ERROR);
                break;
        }
    }

    private void sendGeneralErrorMessage(Player player, Exception e) {
        plugin.getLoggerUtility().log("Error on checking permissions!", LoggerUtility.Level.ERROR);
        plugin.getReportHandler().report(3331, "Couldnt check permission", e.getMessage(), "PermissionsChecker", e);
        plugin.getLoggerUtility().log(player, "Error on checking permissions!", LoggerUtility.Level.ERROR);
    }
}