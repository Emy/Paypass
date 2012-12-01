/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.Miny.Paypassage.Listeners;

import me.Miny.Paypassage.Paypass;

/**
 *
 * @author Simon
 */
public class Listener implements org.bukkit.event.Listener{
    public Paypass plugin;

    public Listener(Paypass plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    
    
}
