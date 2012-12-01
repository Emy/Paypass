package me.Miny.Paypassage;
 
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
 
/**
 *
 * @author Miny
 */
public class Paypass extends JavaPlugin {
 
    @Override
    public void onDisable() {
        long time = System.nanoTime();
        System.out.println("Paypassage Deaktiviert in " + ((System.nanoTime() - time) / 1000000 ) + " ms");
 
    }
 
    @Override
    public void onEnable() {
        long time = System.nanoTime();
        System.out.println("Paypassage Aktiviert in" + ((System.nanoTime() - time) / 1000000 ) + " ms");
    }
 
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
 
            Player player = (Player) sender;
 
            /**
             * /pp create command
             */
            if (command.getName().equalsIgnoreCase("pp")) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("create")) {
                        player.sendMessage(ChatColor.GRAY + "[Paypassage]" + ChatColor.DARK_AQUA + "Paypassage created");
                        return true;
                    }else if (args[0].equalsIgnoreCase("delete")) {
                        player.sendMessage(ChatColor.GRAY + "[Paypassage]" + ChatColor.DARK_AQUA + "Paypassage deleted");
                        return true;
                    }else if (args[0].equalsIgnoreCase("info")) {
                        player.sendMessage(ChatColor.GRAY + "[Paypassage]" + ChatColor.DARK_AQUA + "Paypassage Status:" + ChatColor.GREEN + "Working!" );
                        return true;
                    } else {
                        player.sendMessage(ChatColor.GRAY + "[Paypassage]" + ChatColor.RED + "Du hast irgendetwas falsch gemacht!");
                        return false;
                    }
                }
            }
        }
        return false;
    }
}
