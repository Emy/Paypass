package me.Miny.Paypassage;

import com.iCo6.system.Accounts;
import com.iConomy.iConomy;
import com.nijikokun.register.payment.Methods;

import me.Miny.Paypassage.logger.LoggerUtility;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class IConomyHandler {

    private static int iConomyversion = 0;
    private com.iConomy.system.Holdings balance5;
    private Double balance;
    private Paypassage plugin;
    public static Economy economy = null;

    public IConomyHandler(Paypassage pl) {
        plugin = pl;
        if (setupEconomy() == true) {
            iConomyversion = 2;
            plugin.getLoggerUtility().log("hooked into Vault", LoggerUtility.Level.DEBUG);
        }
        plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
            	plugin.getLoggerUtility().log("checking MoneyPlugin!", LoggerUtility.Level.DEBUG);
                iConomyversion();
            }
        }, 0);
    }

    private static boolean packageExists(String[] packages) {
        try {
            String[] arrayOfString = packages;
            int j = packages.length;
            for (int i = 0; i < j; i++) {
                String pkg = arrayOfString[i];
                Class.forName(pkg);
            }
            return true;
        } catch (Exception localException) {
        }
        return false;
    }

    private Boolean setupEconomy() {
        try {
            RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
            if (economyProvider != null) {
                economy = economyProvider.getProvider();
            }
        } catch (NoClassDefFoundError e) {
            return false;
        }
        return (economy != null);
    }

    public int iConomyversion() {
        if (iConomyversion == 0) {
            try {
                if (packageExists(new String[]{"net.milkbowl.vault.economy.Economy"})) {
                    iConomyversion = 2;
                    plugin.getLoggerUtility().log("hooked into Vault", LoggerUtility.Level.DEBUG);
                } else if (packageExists(new String[]{"com.nijikokun.register.payment.Methods"})) {
                    iConomyversion = 1;
                    plugin.getLoggerUtility().log("hooked into Register", LoggerUtility.Level.DEBUG);
                } else if (packageExists(new String[]{"com.iConomy.iConomy", "com.iConomy.system.Account", "com.iConomy.system.Holdings"})) {
                    iConomyversion = 5;
                    plugin.getLoggerUtility().log("hooked into iConomy5", LoggerUtility.Level.DEBUG);
                } else if (packageExists(new String[]{"com.iCo6.system.Accounts"})) {
                    iConomyversion = 6;
                    plugin.getLoggerUtility().log("hooked into iConomy6", LoggerUtility.Level.DEBUG);
                } else {
                    plugin.getLoggerUtility().log("cant hook into iConomy5, iConomy6, Vault or Register. Downloading Vault!", LoggerUtility.Level.ERROR);
                    plugin.getLoggerUtility().log(" ************ Please download and configure Vault!!!!! **********", LoggerUtility.Level.ERROR);
                }
            } catch (Exception E) {
                E.printStackTrace();
                plugin.getReportHandler().report(3334, "Error on searching EconomyPlugin", E.getMessage(), "iConomyHandler", E);
                iConomyversion = 0;
            }
            return iConomyversion;
        } else {
            return 2;
        }
    }

    public double getBalance(Player player) {
        String name = player.getName();
        return getBalance(name);
    }

    public double getBalance(String name) {
        if (iConomyversion == 5) {
            try {
                this.balance5 = getAccount5(name).getHoldings();
            } catch (Exception E) {
                plugin.getLoggerUtility().log("No Account! Please report it to an admin!", LoggerUtility.Level.ERROR);
                E.printStackTrace();
                this.balance5 = null;
                return this.balance;
            }
            try {
                this.balance = Double.valueOf(this.balance5.balance());
            } catch (Exception E) {
                plugin.getLoggerUtility().log("No Account! Please report it to an admin!", LoggerUtility.Level.ERROR);
                E.printStackTrace();
                this.balance5 = null;
                return this.balance;
            }
            balance = balance5.balance();
            return this.balance;
        }
        if (iConomyversion == 6) {
            try {
                this.balance = new Accounts().get(name).getHoldings().getBalance();
            } catch (Exception e) {
                plugin.getLoggerUtility().log("No Account! Please report it to an admin!", LoggerUtility.Level.ERROR);
                e.printStackTrace();
                balance = null;
                return this.balance;
            }
            return balance;
        }
        if (iConomyversion == 1) {
            try {
                this.balance = Double.valueOf(Methods.getMethod().getAccount(name).balance());
            } catch (Exception e) {
                plugin.getLoggerUtility().log("No Account! Please report it to an admin!", LoggerUtility.Level.ERROR);
                e.printStackTrace();
                this.balance = null;
                return this.balance;
            }
            return balance;
        }
        if (iConomyversion == 2) {
            this.balance = economy.getBalance(name);
            return balance;
        }
        return 0;
    }

    private com.iConomy.system.Account getAccount5(String name) {
        return iConomy.getAccount(name);
    }

    public void substract(double amountsubstract, String name) {
        if (iConomyversion == 5) {
            try {
                getAccount5(name).getHoldings().subtract(amountsubstract);
            } catch (Exception e) {
                plugin.getLoggerUtility().log("Cant substract money! Does account exist?", LoggerUtility.Level.ERROR);
                e.printStackTrace();
            }
        } else if (iConomyversion == 6) {
            try {
                com.iCo6.system.Account account = new Accounts().get(name);
                account.getHoldings().subtract(amountsubstract);
            } catch (Exception e) {
                plugin.getLoggerUtility().log("Cant substract money! Does account exist?", LoggerUtility.Level.ERROR);
                e.printStackTrace();
            }
        } else if (iConomyversion == 1) {
            try {
                Methods.getMethod().getAccount(name).subtract(amountsubstract);
            } catch (Exception e) {
                plugin.getLoggerUtility().log("Cant substract money! Does account exist?", LoggerUtility.Level.ERROR);
                e.printStackTrace();
            }
        } else if (iConomyversion == 2) {
            try {
                economy.withdrawPlayer(name, amountsubstract);
            } catch (Exception e) {
                plugin.getLoggerUtility().log("Cant substract money! Does account exist?", LoggerUtility.Level.ERROR);
                e.printStackTrace();
            }
        }
    }

    public void substract(double amountsubstract, Player player) {
        String name = player.getName();
        substract(amountsubstract, name);
    }

    public void addmoney(double amountadd, String name) {
        if (iConomyversion == 5) {
            try {
                getAccount5(name).getHoldings().add(amountadd);
            } catch (Exception e) {
                plugin.getLoggerUtility().log("Cant substract money! Does account exist?", LoggerUtility.Level.ERROR);
                e.printStackTrace();
            }
        } else if (iConomyversion == 6) {
            try {
                com.iCo6.system.Account account = new Accounts().get(name);
                account.getHoldings().add(amountadd);
            } catch (Exception e) {
                plugin.getLoggerUtility().log("Cant substract money! Does account exist?", LoggerUtility.Level.ERROR);
                e.printStackTrace();
            }
        } else if (iConomyversion == 1) {
            try {
                Methods.getMethod().getAccount(name).add(amountadd);
            } catch (Exception e) {
                plugin.getLoggerUtility().log("Cant substract money! Does account exist?", LoggerUtility.Level.ERROR);
                e.printStackTrace();
            }
        } else if (iConomyversion == 2) {
            try {
                economy.depositPlayer(name, amountadd);
            } catch (Exception e) {
                plugin.getLoggerUtility().log("Cant substract money! Does account exist?", LoggerUtility.Level.ERROR);
                e.printStackTrace();
            }
        }
    }

    public void addmoney(double amountadd, Player player) {
        String name = player.getName();
        addmoney(amountadd, name);
    }
}