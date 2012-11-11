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
        System.out.println("Paypassage Deaktiviert");
 
    }
 
    @Override
    public void onEnable() {
        System.out.println("Paypassage Aktiviert");
 
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
                        player.sendMessage(ChatColor.GRAY + "[Paypassage]" + ChatColor.DARK_AQUA + "Paypassage Erstellt");
                        return true;
                    }else if (args[0].equalsIgnoreCase("create2")) {
                        player.sendMessage(ChatColor.GRAY + "[Paypassage]" + ChatColor.DARK_AQUA + "Paypassage Erstellt");
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
