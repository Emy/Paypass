package Permissions;

import de.bananaco.bpermissions.api.util.CalculableType;
import me.Miny.Paypassage.Paypass;
import me.Miny.Paypassage.logger.LoggerUtility;
import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.dataholder.worlds.WorldsHolder;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PermissionsUtility {

    private Paypass plugin;
    private GroupManager groupManager;
    public int PermPlugin = 0;

    public PermissionsUtility(Paypass pl, String von) {
        this.plugin = pl;
        final String von2 = von;
        final PluginManager pluginManager = plugin.getServer().getPluginManager();
        final Plugin GMplugin = pluginManager.getPlugin("GroupManager");

        plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                try {
                    if (GMplugin != null) {
                        groupManager = (GroupManager) GMplugin;
                    }
                    plugin.getLoggerUtility().log("checking PermissionsPlugin!", LoggerUtility.Level.DEBUG);
                    searchpermplugin();
                } catch (Exception e) {
                    plugin.getReportHandler().report(3324, "Checking Permissions plugin failed", e.getMessage(), "PermissionsChecker", e);
                }
            }
        }, 0);
    }

    public void searchpermplugin() {
        try {
            plugin.getServer().getServicesManager().getRegistration(ru.tehkode.permissions.bukkit.PermissionsEx.class);
            PermPlugin = 2;
            plugin.getLoggerUtility().log("Permissions: Hooked into PermissionsEX!", LoggerUtility.Level.DEBUG);
            return;
        } catch (NoClassDefFoundError e) {
        }
        try {
            plugin.getServer().getServicesManager().getRegistration(org.anjocaido.groupmanager.GroupManager.class);
            PermPlugin = 3;
            plugin.getLoggerUtility().log("Permissions: Hooked into GroupManager!", LoggerUtility.Level.DEBUG);
            return;
        } catch (NoClassDefFoundError e) {
        }
        try {
            plugin.getServer().getServicesManager().getRegistration(de.bananaco.bpermissions.api.util.CalculableType.class);
            PermPlugin = 4;
            plugin.getLoggerUtility().log("Permissions: Hooked into bPermissions!", LoggerUtility.Level.DEBUG);
            return;
        } catch (NoClassDefFoundError e) {
        }
        PermPlugin = 1;
    }

    public boolean checkpermissionssilent(Player player, String action) {
        if (!plugin.isStarted()) {
            return false;
        }
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
                    if (plugin.getConfigHandler().getConfig().getBoolean("debug")) {
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
                if (!Bukkit.getPluginManager().isPluginEnabled("GroupManager") && groupManager != null) {
                    return false;
                }
                try {
                    WorldsHolder holder = groupManager.getWorldsHolder();
                    if(holder == null) {
                        return false;
                    }
                    final AnjoPermissionsHandler handler = holder.getWorldPermissions(player);
                    if (handler == null) {
                        return false;
                    }
                    return handler.has(player, action);
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
                    if (de.bananaco.bpermissions.api.ApiLayer.hasPermission(player.getWorld().getName(), CalculableType.USER, player.getName(), action)) {
                        return true;
                    } else if (de.bananaco.bpermissions.api.ApiLayer.hasPermission(player.getWorld().getName(), CalculableType.GROUP, player.getName(), action)) {
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
        if (!plugin.isStarted()) {
            return false;
        }
        try {
            if (player.isOp()) {
                return true;
            }
            if (PermPlugin == 1) {
                try {
                    if (player.hasPermission(action)) {
                        return true;
                    } else {
                        plugin.getLoggerUtility().log(player, player.getName() + " " + plugin.getConfig().getString("permissions.error." + plugin.getConfig().getString("language")) + " (" + action + ")", LoggerUtility.Level.ERROR);
                        return false;
                    }
                } catch (Exception e) {
                    plugin.getLoggerUtility().log("Error on checking permissions with BukkitPermissions!", LoggerUtility.Level.ERROR);
                    plugin.getReportHandler().report(3329, "Couldnt check permission with BukkitPermissions", e.getMessage(), "PermissionsChecker", e);
                    plugin.getLoggerUtility().log(player, "Error on checking permissions with BukkitPermissions!", LoggerUtility.Level.ERROR);
                    e.printStackTrace();
                    return false;
                }
            } else if (PermPlugin == 2) {
                if (!Bukkit.getPluginManager().isPluginEnabled("PermissionsEx")) {
                    plugin.getLoggerUtility().log(player, "PermissionsEX is not enabled!", LoggerUtility.Level.ERROR);
                    return false;
                }
                try {
                    PermissionUser user = PermissionsEx.getUser(player);
                    if (user.has(action)) {
                        return true;
                    } else {
                        plugin.getLoggerUtility().log(player, player.getName() + " " + plugin.getConfig().getString("permissions.error." + plugin.getConfig().getString("language")) + " (" + action + ")", LoggerUtility.Level.ERROR);
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
                            plugin.getLoggerUtility().log(player, player.getName() + " " + plugin.getConfig().getString("permissions.error." + plugin.getConfig().getString("language")) + " (" + action + ")", LoggerUtility.Level.ERROR);
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
                    plugin.getLoggerUtility().log(player, "GroupManager is not enabled!", LoggerUtility.Level.ERROR);
                    return false;
                }
                try {
                    WorldsHolder holder = groupManager.getWorldsHolder();
                    if(holder == null) {
                        return false;
                    }
                    final AnjoPermissionsHandler handler = holder.getWorldPermissions(player);
                    if (handler == null) {
                        return false;
                    }
                    if (handler.has(player, action)) {
                        return true;
                    } else {
                        plugin.getLoggerUtility().log(player, player.getName() + " " + plugin.getConfig().getString("permissions.error." + plugin.getConfig().getString("language")) + " (" + action + ")", LoggerUtility.Level.ERROR);
                        return false;
                    }
                } catch (Exception e) {
                    plugin.getLoggerUtility().log("Error on checking permissions with GroupManager!", LoggerUtility.Level.ERROR);
                    plugin.getReportHandler().report(3331, "Couldnt check permission with GroupManager", e.getMessage(), "PermissionsChecker", e);
                    plugin.getLoggerUtility().log(player, "Error on checking permissions with GroupManager!", LoggerUtility.Level.ERROR);
                    e.printStackTrace();
                    return false;
                }
            } else if (PermPlugin == 4) {
                if (!Bukkit.getPluginManager().isPluginEnabled("bPermissions")) {
                    plugin.getLoggerUtility().log(player, "bPermissions is not enabled!", LoggerUtility.Level.ERROR);
                    return false;
                }
                try {
                    if (de.bananaco.bpermissions.api.ApiLayer.hasPermission(player.getWorld().getName(), CalculableType.USER, player.getName(), action)) {
                        return true;
                    } else if (de.bananaco.bpermissions.api.ApiLayer.hasPermission(player.getWorld().getName(), CalculableType.GROUP, player.getName(), action)) {
                        return true;
                    } else {
                        plugin.getLoggerUtility().log(player, player.getName() + " " + plugin.getConfig().getString("permissions.error." + plugin.getConfig().getString("language")) + " (" + action + ")", LoggerUtility.Level.ERROR);
                        return false;
                    }
                } catch (Exception e) {
                    plugin.getLoggerUtility().log("Error on checking permissions with bPermissions!", LoggerUtility.Level.ERROR);
                    plugin.getReportHandler().report(3332, "Couldnt check permission with bPermissions", e.getMessage(), "PermissionsChecker", e);
                    plugin.getLoggerUtility().log(player, "Error on checking permissions with bPermissions!", LoggerUtility.Level.ERROR);
                    e.printStackTrace();
                    return false;
                }
            } else {
                plugin.getLoggerUtility().log(player, player.getName() + " " + plugin.getConfig().getString("permissions.error." + plugin.getConfig().getString("language")) + " (" + action + ")", LoggerUtility.Level.ERROR);
                System.out.println("PermissionsEx plugin are not found.");
                return false;
            }
        } catch (Exception e) {
            plugin.getLoggerUtility().log("Error on checking permissions!", LoggerUtility.Level.ERROR);
            plugin.getReportHandler().report(3333, "Error on checking permissions", e.getMessage(), "PermissionsChecker", e);
            plugin.getLoggerUtility().log(player, "Error on checking permissions!", LoggerUtility.Level.ERROR);
            e.printStackTrace();
            return false;
        }
    }
}